package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;

public class AppConfig {
    private static RepositoryFactory repositoryFactory;

    public static void initialize(EntityManager em) {
        repositoryFactory = new RepositoryFactory(em);
    }

    public static RepositoryFactory getRepositoryFactory() {
        if (repositoryFactory == null) {
            throw new IllegalStateException("RepositoryFactory non initialisée. Appelez initialize() d'abord.");
        }
        return repositoryFactory;
    }
}
