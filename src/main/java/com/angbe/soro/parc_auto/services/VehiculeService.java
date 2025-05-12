package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class VehiculeService {
    private final Repository<Vehicule> vehiculeRepository;
    private final Repository<EtatVoiture> etatRepository;

    public VehiculeService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
        this.etatRepository = factory.getRepository(EtatVoiture.class);
    }

    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    public Optional<Vehicule> getVehiculeById(int id) {
        return vehiculeRepository.findById(id);
    }

    public Vehicule saveVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    public void deleteVehicule(int id) {
        vehiculeRepository.delete(id);
    }

    public boolean vehiculeExists(int id) {
        return vehiculeRepository.existsById(id);
    }

    public List<Vehicule> getVehiculesByEtat(String etatLibelle) {
        return vehiculeRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Vehicule> query = cb.createQuery(Vehicule.class);
            Root<Vehicule> root = query.from(Vehicule.class);
            Join<Vehicule, EtatVoiture> etatJoin = root.join("etatVoiture");
            query.where(cb.equal(etatJoin.get("libelleEtat"), etatLibelle));
            return em.createQuery(query).getResultList();
        });
    }

    public long countVehiculesByEnergie(String energie) {
        return vehiculeRepository.count(cb -> {
            Root<Vehicule> root = cb.createQuery(Vehicule.class).from(Vehicule.class);
            return cb.equal(root.get("energie"), energie);
        });
    }

    public Vehicule changerEtatVehicule(int vehiculeId, int nouvelEtatId) {
        Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(vehiculeId);
        Optional<EtatVoiture> optionalEtat = etatRepository.findById(nouvelEtatId);

        if (optionalVehicule.isPresent() && optionalEtat.isPresent()) {
            Vehicule vehicule = optionalVehicule.get();
            vehicule.setEtatVoiture(optionalEtat.get());
            vehicule.setDateEtat(new Date());
            return vehiculeRepository.save(vehicule);
        }
        throw new RuntimeException("Véhicule ou état non trouvé");
    }

    public List<Vehicule> getVehiculesDisponibles() {
        return getVehiculesByEtat("Disponible");
    }

    public List<Vehicule> getVehiculesEnMission() {
        return getVehiculesByEtat("En mission");
    }

    public List<Vehicule> getVehiculesEnEntretien() {
        return getVehiculesByEtat("En entretien");
    }

    public void updateVehicule(Vehicule v) {
        if(!vehiculeRepository.existsById(v.getIdVehicule())){
            throw new RuntimeException("Le vehicule  avec l'ID" + v.getIdVehicule() + " n'existe pas");
        }
        vehiculeRepository.update(v);
    }
}




