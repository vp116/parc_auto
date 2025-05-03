package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

public class RepositoryFactory {
    private final EntityManager entityManager;
    private final Map<Class<?>, Repository<?>> repositories = new HashMap<>();

    public RepositoryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public <T> Repository<T> getRepository(Class<T> entityClass) {
        return (Repository<T>) repositories.computeIfAbsent(entityClass,
                cls -> new JpaRepository<>(entityManager, entityClass));
    }

    public static RepositoryFactory with(EntityManager entityManager) {
        return new RepositoryFactory(entityManager);
    }
}


