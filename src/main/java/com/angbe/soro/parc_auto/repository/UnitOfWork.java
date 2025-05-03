package com.angbe.soro.parc_auto.repository;


import jakarta.persistence.EntityManager;


public interface UnitOfWork {
    void begin();
    void commit();
    void rollback();
    boolean isActive();
    EntityManager getEntityManager();
}