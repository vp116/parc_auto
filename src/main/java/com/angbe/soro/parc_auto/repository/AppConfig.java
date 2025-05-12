package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AppConfig {
    private static RepositoryFactory repositoryFactory;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void initialize(EntityManager em) {
        repositoryFactory = new RepositoryFactory(em);
    }

    public static void initialize() {
        if (repositoryFactory == null) {
            try {
                emf = Persistence.createEntityManagerFactory("bdGestionParcAuto");
                em = emf.createEntityManager();
                repositoryFactory = new RepositoryFactory(em);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de l'initialisation de la configuration : " + e.getMessage(), e);
            }
        }
    }

    public static RepositoryFactory getRepositoryFactory() {
        if (repositoryFactory == null) {
            throw new IllegalStateException("RepositoryFactory non initialisée. Appelez initialize() d'abord.");
        }
        return repositoryFactory;
    }

    public static void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        repositoryFactory = null;
        em = null;
        emf = null;
    }
}
