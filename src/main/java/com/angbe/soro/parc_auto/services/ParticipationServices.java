package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Participer;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;

public class ParticipationServices {
    private final Repository<Participer> participerRepository;

    public ParticipationServices() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.participerRepository = factory.getRepository(Participer.class);

    }

    public void save(Participer participer) {
        participerRepository.save(participer);
    }

}
