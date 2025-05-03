package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.*;


import com.angbe.soro.parc_auto.models.*;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
}
