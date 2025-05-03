package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.*;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public class MissionService {
    private final Repository<Mission> missionRepository;
    private final Repository<Vehicule> vehiculeRepository;
    private final Repository<Personnel> personnelRepository;

    public MissionService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.missionRepository = factory.getRepository(Mission.class);
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
        this.personnelRepository = factory.getRepository(Personnel.class);
    }

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Optional<Mission> getMissionById(int id) {
        return missionRepository.findById(id);
    }

    public Mission saveMission(Mission mission) {
        Optional<Vehicule> vehicule = vehiculeRepository.findById(mission.getVehicule().getIdVehicule());
        if (vehicule.isPresent() && !"Disponible".equals(vehicule.get().getEtatVoiture().getLibelleEtat())) {
            throw new IllegalStateException("Le véhicule n'est pas disponible pour une mission");
        }

        Mission savedMission = missionRepository.save(mission);

        vehicule.get().setEtatVoiture(new EtatVoiture(2, "En mission"));
        vehiculeRepository.save(vehicule.get());

        return savedMission;
    }

    public void deleteMission(int id) {
        Optional<Mission> mission = missionRepository.findById(id);
        if (mission.isPresent()) {
            Vehicule vehicule = mission.get().getVehicule();
            vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));
            vehiculeRepository.save(vehicule);

            missionRepository.delete(id);
        }
    }

    public List<Mission> getMissionsByVehicule(int vehiculeId) {
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Mission> query = cb.createQuery(Mission.class);
            Root<Mission> root = query.from(Mission.class);
            query.where(cb.equal(root.get("vehicule").get("idVehicule"), vehiculeId));
            query.orderBy(cb.desc(root.get("dateDebutMission")));
            return em.createQuery(query).getResultList();
        });
    }

    public void ajouterParticipant(int missionId, int personnelId) {
        Optional<Mission> mission = missionRepository.findById(missionId);
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);

        if (mission.isPresent() && personnel.isPresent()) {
            Mission m = mission.get();
            m.getParticipants().add(new Participer(personnel.get(), m));
            missionRepository.save(m);
        } else {
            throw new RuntimeException("Mission ou personnel non trouvé");
        }
    }

    public double getCoutTotalMissions() {
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Mission> root = query.from(Mission.class);
            query.select(cb.sum(root.get("coutMission")));
            Double result = em.createQuery(query).getSingleResult();
            return result != null ? result : 0.0;
        });
    }
}
