package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implémentation JPA d'un repository générique.
 *
 * @param <T> le type de l'entité gérée par ce repository
 */
public class JpaRepository<T> implements Repository<T> {
    private final EntityManager em;
    private final Class<T> entityClass;

    public JpaRepository(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return em.createQuery(query).getResultList();
    }

    @Override
    public T save(T entity) {
        return executeInTransaction(() -> em.merge(entity));
    }

    @Override
    public void delete(int id) {
        executeInTransaction(() -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public boolean existsById(int id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean any(Function<CriteriaBuilder, Predicate> predicateBuilder) {
        return count(predicateBuilder) > 0;
    }

    @Override
    public long count() {
        return executeCountQuery(createCountQuery());
    }

    @Override
    public long count(Function<CriteriaBuilder, Predicate> predicateBuilder) {
        CriteriaQuery<Long> query = createCountQuery();
        query.where(predicateBuilder.apply(em.getCriteriaBuilder()));
        return executeCountQuery(query);
    }

    @Override
    public <R> R query(Function<EntityManager, R> queryExecutor) {
        return queryExecutor.apply(em);
    }

    private CriteriaQuery<Long> createCountQuery() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        query.select(cb.count(query.from(entityClass)));
        return query;
    }

    private long executeCountQuery(CriteriaQuery<Long> query) {
        return em.createQuery(query).getSingleResult();
    }

    private void executeInTransaction(Runnable operation) {
        boolean transactionActive = em.getTransaction().isActive();
        try {
            if (!transactionActive) {
                em.getTransaction().begin();
            }
            operation.run();
            if (!transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (!transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    private <R> R executeInTransaction(Supplier<R> operation) {
        boolean transactionActive = em.getTransaction().isActive();
        try {
            if (!transactionActive) {
                em.getTransaction().begin();
            }
            R result = operation.get();
            if (!transactionActive) {
                em.getTransaction().commit();
            }
            return result;
        } catch (Exception e) {
            if (!transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
}