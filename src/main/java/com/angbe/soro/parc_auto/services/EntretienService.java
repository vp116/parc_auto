package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public class EntretienService {
    private final Repository<Entretien> entretienRepository;
    private final Repository<Vehicule> vehiculeRepository;

    public EntretienService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.entretienRepository = factory.getRepository(Entretien.class);
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
    }

    public List<Entretien> getAllEntretiens() {
        return entretienRepository.findAll();
    }

    public Optional<Entretien> getEntretienById(int id) {
        return entretienRepository.findById(id);
    }

    public Entretien planifierEntretien(Entretien entretien) {
        Optional<Vehicule> vehicule = vehiculeRepository.findById(entretien.getVehicule().getIdVehicule());
        if (vehicule.isPresent() && !"Disponible".equals(vehicule.get().getEtatVoiture().getLibelleEtat())) {
            throw new IllegalStateException("Le véhicule n'est pas disponible pour un entretien");
        }

        Entretien savedEntretien = entretienRepository.save(entretien);

        vehicule.get().setEtatVoiture(new EtatVoiture(4, "En entretien"));
        vehiculeRepository.save(vehicule.get());

        return savedEntretien;
    }

    public void cloturerEntretien(int entretienId) {
        Optional<Entretien> entretien = entretienRepository.findById(entretienId);
        if (entretien.isPresent()) {
            Entretien e = entretien.get();
            e.setDateSortie(new Date());
            entretienRepository.save(e);

            Vehicule vehicule = e.getVehicule();
            vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));
            vehiculeRepository.save(vehicule);
        }
    }

    public List<Entretien> getEntretiensByVehicule(int vehiculeId) {
        return entretienRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Entretien> query = cb.createQuery(Entretien.class);
            Root<Entretien> root = query.from(Entretien.class);
            query.where(cb.equal(root.get("vehicule").get("idVehicule"), vehiculeId));
            query.orderBy(cb.desc(root.get("dateEntreeEntr")));
            return em.createQuery(query).getResultList();
        });
    }

    public double getCoutTotalEntretiens() {
        return entretienRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Entretien> root = query.from(Entretien.class);
            query.select(cb.sum(root.get("coutEntr")));
            Double result = em.createQuery(query).getSingleResult();
            return result != null ? result : 0.0;
        });
    }

    public void updateEntretien(Entretien e) {
    }

    public void deleteEntretien(int id) {
    }

    /**
     * Recherche les entretiens dont la date d'entrée est comprise entre deux dates
     *
     * @param dateDebut la date de début de la période
     * @param dateFin   la date de fin de la période
     * @return la liste des entretiens dans la période spécifiée
     */
    public List<Entretien> findByDateEntreeBetween(Date dateDebut, Date dateFin) {
        // Utiliser le repository pour exécuter une requête avec critères
        return entretienRepository.query(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Entretien> criteriaQuery = criteriaBuilder.createQuery(Entretien.class);
            Root<Entretien> root = criteriaQuery.from(Entretien.class);

            // Créer la condition : dateEntree entre dateDebut et dateFin
            Predicate predicate = criteriaBuilder.between(root.get("dateEntree"), dateDebut, dateFin);

            // Appliquer la condition à la requête
            criteriaQuery.where(predicate);

            // Exécuter la requête et retourner les résultats
            return entityManager.createQuery(criteriaQuery).getResultList();
        });
    }
}
