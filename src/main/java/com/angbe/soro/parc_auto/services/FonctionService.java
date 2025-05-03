package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Fonction;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;

import java.util.List;
import java.util.Optional;

public class FonctionService {
    private final Repository<Fonction> fonctionRepository;

    public FonctionService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.fonctionRepository = factory.getRepository(Fonction.class);
    }

    public Fonction saveFonction(Fonction fonction) {
        return fonctionRepository.save(fonction);
    }

    public Optional<Fonction> getFonctionById(int id) {
        return fonctionRepository.findById(id);
    }

    public List<Fonction> getAllFonctions() {
        return fonctionRepository.findAll();
    }

    public void deleteFonction(int id) {
        fonctionRepository.delete(id);
    }
}