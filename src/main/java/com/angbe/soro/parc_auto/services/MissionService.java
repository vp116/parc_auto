package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.models.Participer;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MissionService {
    private final Repository<Mission> missionRepository;
    private final Repository<Vehicule> vehiculeRepository;
    private final ParticipationServices participationServices;

    public MissionService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.missionRepository = factory.getRepository(Mission.class);
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
        this.participationServices = new ParticipationServices();
    }

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Optional<Mission> getMissionById(int id) {
        return missionRepository.findById(id);
    }

    public Mission saveMission(Mission mission) {
        // Vérifier si le véhicule est disponible
        if (mission.getVehicule() == null) {
            throw new RuntimeException("Impossible de créer une mission sans véhicule");
        }

        Optional<Vehicule> vehicule = vehiculeRepository.findById(mission.getVehicule().getIdVehicule());
        if (!vehicule.isPresent()) {
            throw new RuntimeException("Le véhicule avec l'ID " + mission.getVehicule().getIdVehicule() + " n'existe pas");
        }

        // Changer l'état du véhicule à "En mission"
        vehicule.get().setEtatVoiture(new EtatVoiture(2, "En mission"));
        vehiculeRepository.save(vehicule.get());

        // Sauvegarder la mission
        Mission savedMission = missionRepository.save(mission);

        // Sauvegarder les participants
        if (savedMission.getParticipants() != null) {
            for (Participer participant : savedMission.getParticipants()) {
                participant.setMission(savedMission);
                participationServices.save(participant);
            }
        }

        return savedMission;
    }

    public void updateMission(Mission mission) {
        if (!missionRepository.existsById(mission.getIdMission())) {
            throw new RuntimeException("La mission avec l'ID " + mission.getIdMission() + " n'existe pas");
        }

        // Mettre à jour la mission
        missionRepository.update(mission);

        // Mettre à jour les participants
        if (mission.getParticipants() != null) {
            for (Participer participant : mission.getParticipants()) {
                participationServices.save(participant);
            }
        }
    }

    public void deleteMission(int id) {
        Optional<Mission> mission = missionRepository.findById(id);
        if (mission.isPresent()) {
            // Libérer le véhicule
            Vehicule vehicule = mission.get().getVehicule();
            if (vehicule != null) {
                vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));
                vehiculeRepository.save(vehicule);
            }

            // Supprimer la mission
            missionRepository.delete(id);
        }
    }

    public List<Mission> getMissionsByVehicule(int vehiculeId) {
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Mission> query = cb.createQuery(Mission.class);
            Root<Mission> root = query.from(Mission.class);
            Join<Mission, Vehicule> vehiculeJoin = root.join("vehicule");
            query.where(cb.equal(vehiculeJoin.get("idVehicule"), vehiculeId));
            query.orderBy(cb.desc(root.get("dateDebut")));
            return em.createQuery(query).getResultList();
        });
    }

    public List<Mission> getMissionsEnCours() {
        Date now = new Date();
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Mission> query = cb.createQuery(Mission.class);
            Root<Mission> root = query.from(Mission.class);
            query.where(
                    cb.and(
                            cb.lessThanOrEqualTo(root.get("dateDebut"), now),
                            cb.greaterThanOrEqualTo(root.get("dateFin"), now)
                    )
            );
            return em.createQuery(query).getResultList();
        });
    }

    public double getCoutTotalMissions() {
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Mission> root = query.from(Mission.class);
            query.select(cb.sum(root.get("cout")));
            Double result = em.createQuery(query).getSingleResult();
            return result != null ? result : 0.0;
        });
    }

    public double getCoutTotalCarburant() {
        return missionRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Mission> root = query.from(Mission.class);
            query.select(cb.sum(root.get("coutCarburant")));
            Double result = em.createQuery(query).getSingleResult();
            return result != null ? result : 0.0;
        });
    }

    /**
     * Recherche les missions dont la date de début est comprise entre deux dates
     *
     * @param dateDebut la date de début de la période
     * @param dateFin   la date de fin de la période
     * @return la liste des missions dans la période spécifiée
     */
    public List<Mission> findByDateDebutBetween(Date dateDebut, Date dateFin) {
        // Utiliser le repository pour exécuter une requête avec critères
        return missionRepository.query(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Mission> criteriaQuery = criteriaBuilder.createQuery(Mission.class);
            Root<Mission> root = criteriaQuery.from(Mission.class);

            // Créer la condition : dateDebut entre dateDebut et dateFin
            Predicate predicate = criteriaBuilder.between(root.get("dateDebut"), dateDebut, dateFin);

            // Appliquer la condition à la requête
            criteriaQuery.where(predicate);

            // Exécuter la requête et retourner les résultats
            return entityManager.createQuery(criteriaQuery).getResultList();
        });
    }
}
