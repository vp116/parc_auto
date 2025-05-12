package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Participer;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;

import java.util.List;
import java.util.Optional;

public class ParticipationServices {
    private final Repository<Participer> participerRepository;
    private final Repository<Personnel> personnelRepository;
    private final Repository<Mission> missionRepository;

    public ParticipationServices() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.participerRepository = factory.getRepository(Participer.class);
        this.personnelRepository = factory.getRepository(Personnel.class);
        this.missionRepository = factory.getRepository(Mission.class);
    }

    public void save(Participer participer) {
        participerRepository.save(participer);
    }
    
    public List<Participer> getParticipationsByMission(int missionId) {
        return participerRepository.query(em -> {
            return em.createQuery(
                "SELECT p FROM Participer p WHERE p.mission.idMission = :missionId", 
                Participer.class)
                .setParameter("missionId", missionId)
                .getResultList();
        });
    }
    
    public List<Participer> getParticipationsByPersonnel(int personnelId) {
        return participerRepository.query(em -> {
            return em.createQuery(
                "SELECT p FROM Participer p WHERE p.personnel.idPersonnel = :personnelId", 
                Participer.class)
                .setParameter("personnelId", personnelId)
                .getResultList();
        });
    }
    
    public void deleteParticipation(int missionId, int personnelId) {
        participerRepository.query(em -> {
            return em.createQuery(
                "DELETE FROM Participer p WHERE p.mission.idMission = :missionId AND p.personnel.idPersonnel = :personnelId")
                .setParameter("missionId", missionId)
                .setParameter("personnelId", personnelId)
                .executeUpdate();
        });
    }
    
    public void deleteAllParticipationsForMission(int missionId) {
        participerRepository.query(em -> {
            return em.createQuery(
                "DELETE FROM Participer p WHERE p.mission.idMission = :missionId")
                .setParameter("missionId", missionId)
                .executeUpdate();
        });
    }
}
