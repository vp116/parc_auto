package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;

public class JpaUnitOfWork implements UnitOfWork {
    private final EntityManager em;

    public JpaUnitOfWork(EntityManager em) {
        this.em = em;
    }

    @Override
    public void begin() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    @Override
    public void commit() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    @Override
    public void rollback() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Override
    public boolean isActive() {
        return em.getTransaction().isActive();
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
