package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;

import java.util.List;
import java.util.Optional;

public class EtatVoitureService {
    private final Repository<EtatVoiture> etatRepository;

    public EtatVoitureService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.etatRepository = factory.getRepository(EtatVoiture.class);
    }

    public EtatVoiture saveEtat(EtatVoiture etat) {
        return etatRepository.save(etat);
    }

    public Optional<EtatVoiture> getEtatById(int id) {
        return etatRepository.findById(id);
    }

    public List<EtatVoiture> getAllEtats() {
        return etatRepository.findAll();
    }
}