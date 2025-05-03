package com.angbe.soro.parc_auto.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Interface générique pour les opérations de base sur un repository.
 *
 * @param <T> le type de l'entité gérée par ce repository
 */
public interface Repository<T> {
    /**
     * Recherche une entité par son identifiant.
     *
     * @param id l'identifiant de l'entité à rechercher
     * @return un Optional contenant l'entité trouvée ou empty si non trouvée
     */
    Optional<T> findById(int id);

    /**
     * Retourne toutes les entités de ce type.
     *
     * @return une liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Sauvegarde une entité (création ou mise à jour).
     *
     * @param entity l'entité à sauvegarder
     * @return l'entité sauvegardée
     */
    T save(T entity);

    /**
     * Supprime une entité par son identifiant.
     *
     * @param id l'identifiant de l'entité à supprimer
     */
    void delete(int id);

    /**
     * Vérifie si une entité existe pour l'identifiant donné.
     *
     * @param id l'identifiant à vérifier
     * @return true si une entité existe, false sinon
     */
    boolean existsById(int id);

    /**
     * Vérifie si au moins une entité satisfait le prédicat donné.
     *
     * @param predicateBuilder fonction qui construit le prédicat de recherche
     * @return true si au moins une entité correspond, false sinon
     */
    boolean any(Function<CriteriaBuilder, Predicate> predicateBuilder);

    /**
     * Compte toutes les entités.
     *
     * @return le nombre total d'entités
     */
    long count();

    /**
     * Compte les entités satisfaisant le prédicat donné.
     *
     * @param predicateBuilder fonction qui construit le prédicat de recherche
     * @return le nombre d'entités correspondant au prédicat
     */
    long count(Function<CriteriaBuilder, Predicate> predicateBuilder);

    /**
     * Exécute une requête personnalisée sur le repository.
     *
     * @param <R>           le type de résultat de la requête
     * @param queryExecutor fonction qui exécute la requête en utilisant l'EntityManager
     * @return le résultat de la requête
     */
    <R> R query(Function<EntityManager, R> queryExecutor);
}



