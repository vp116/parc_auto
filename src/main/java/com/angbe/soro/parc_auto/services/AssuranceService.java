package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Assurance;
import com.angbe.soro.parc_auto.models.Couvrir;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AssuranceService {
    private final Repository<Assurance> assuranceRepository;
    private final Repository<Vehicule> vehiculeRepository;

    public AssuranceService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.assuranceRepository = factory.getRepository(Assurance.class);
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
    }

    public List<Assurance> getAllAssurances() {
        return assuranceRepository.findAll();
    }

    public Optional<Assurance> getAssuranceById(int id) {
        return assuranceRepository.findById(id);
    }

    public Assurance saveAssurance(Assurance assurance) {
        return assuranceRepository.save(assurance);
    }

    public void deleteAssurance(int id) {
        assuranceRepository.delete(id);
    }

    public void ajouterVehiculeAssurance(int assuranceId, int vehiculeId) {
        Optional<Assurance> assurance = assuranceRepository.findById(assuranceId);
        Optional<Vehicule> vehicule = vehiculeRepository.findById(vehiculeId);

        if (assurance.isPresent() && vehicule.isPresent()) {
            Assurance a = assurance.get();
            a.getCouvertures().add(new Couvrir(vehicule.get(), a));
            assuranceRepository.save(a);
        } else {
            throw new RuntimeException("Assurance ou véhicule non trouvé");
        }
    }

    public List<Assurance> getAssurancesExpirantAvant(LocalDateTime date) {
        return assuranceRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Assurance> query = cb.createQuery(Assurance.class);
            Root<Assurance> root = query.from(Assurance.class);
            query.where(cb.lessThanOrEqualTo(root.get("dateFinAssurance"), date));
            query.orderBy(cb.asc(root.get("dateFinAssurance")));
            return em.createQuery(query).getResultList();
        });
    }

    public List<Assurance> getAssurancesVehicule(int vehiculeId) {
        return assuranceRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Assurance> query = cb.createQuery(Assurance.class);
            Root<Assurance> root = query.from(Assurance.class);
            Join<Assurance, Couvrir> couvrirJoin = root.join("couvertures");
            Join<Couvrir, Vehicule> vehiculeJoin = couvrirJoin.join("vehicule");

            query.where(cb.equal(vehiculeJoin.get("idVehicule"), vehiculeId));
            query.orderBy(cb.desc(root.get("dateFinAssurance")));

            return em.createQuery(query).getResultList();
        });
    }



    public double getCoutTotalAssurances() {
        return assuranceRepository.query(em -> {
            Double result = em.createQuery(
                            "SELECT SUM(a.coutAssurance) FROM Assurance a", Double.class)
                    .getSingleResult();
            return result != null ? result : 0.0;
        });
    }
}
