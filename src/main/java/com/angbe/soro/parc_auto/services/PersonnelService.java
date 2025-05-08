package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.*;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonnelService {
    private final Repository<Personnel> personnelRepository;
    private final Repository<Vehicule> vehiculeRepository;
    private final Repository<Fonction> fonctionRepository;
    private final Repository<Service> serviceRepository;

    public PersonnelService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.personnelRepository = factory.getRepository(Personnel.class);
        this.vehiculeRepository = factory.getRepository(Vehicule.class);
        this.fonctionRepository = factory.getRepository(Fonction.class);
        this.serviceRepository = factory.getRepository(Service.class);
    }

    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAll();
    }

    public Optional<Personnel> getPersonnelById(int id) {
        return personnelRepository.findById(id);
    }

    public Personnel savePersonnel(Personnel personnel) {
        return personnelRepository.save(personnel);
    }

    public void deletePersonnel(int id) {
        personnelRepository.delete(id);
    }

    public List<Personnel> getPersonnelByFonction(String fonctionLibelle) {
        return personnelRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Personnel> query = cb.createQuery(Personnel.class);
            Root<Personnel> root = query.from(Personnel.class);
            Join<Personnel, Fonction> fonctionJoin = root.join("fonction");
            query.where(cb.equal(fonctionJoin.get("libelleFonction"), fonctionLibelle));
            return em.createQuery(query).getResultList();
        });
    }

    public List<Personnel> getPersonnelByService(String serviceLibelle) {
        return personnelRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Personnel> query = cb.createQuery(Personnel.class);
            Root<Personnel> root = query.from(Personnel.class);
            Join<Personnel, Service> serviceJoin = root.join("service");
            query.where(cb.equal(serviceJoin.get("libelle"), serviceLibelle));
            return em.createQuery(query).getResultList();
        });
    }

    public void attribuerVehicule(int personnelId, int vehiculeId) {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        Optional<Vehicule> vehicule = vehiculeRepository.findById(vehiculeId);

        if (personnel.isPresent() && vehicule.isPresent()) {
            Personnel p = personnel.get();
            p.setVehicule(vehicule.get());
            p.setDateAttribution(new Date());
            personnelRepository.save(p);

            vehicule.get().setEtatVoiture(new EtatVoiture(5, "Attribuer"));
            vehiculeRepository.save(vehicule.get());
        } else {
            throw new RuntimeException("Personnel ou véhicule non trouvé");
        }
    }

    public void retirerVehicule(int personnelId) {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        if (personnel.isPresent() && personnel.get().getVehicule() != null) {
            Personnel p = personnel.get();
            Vehicule v = p.getVehicule();

            p.setVehicule(null);
            personnelRepository.save(p);

            v.setEtatVoiture(new EtatVoiture(1, "Disponible"));
            vehiculeRepository.save(v);
        }
    }

    public List<Personnel> getPersonnelAvecVehicule() {
        return personnelRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Personnel> query = cb.createQuery(Personnel.class);
            Root<Personnel> root = query.from(Personnel.class);
            query.where(cb.isNotNull(root.get("vehicule")));
            return em.createQuery(query).getResultList();
        });
    }

    public Map<String, Long> getStatistiquesPersonnelParFonction() {
        return personnelRepository.query(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<Personnel> root = query.from(Personnel.class);
            Join<Personnel, Fonction> fonctionJoin = root.join("fonction");

            query.multiselect(
                    fonctionJoin.get("libelleFonction"),
                    cb.count(root)
            );
            query.groupBy(fonctionJoin.get("libelleFonction"));

            List<Object[]> results = em.createQuery(query).getResultList();
            return results.stream().collect(Collectors.toMap(
                    arr -> (String) arr[0],
                    arr -> (Long) arr[1]
            ));
        });
    }

    public void updatePersonnel(Personnel personnel) {
        if(!vehiculeRepository.existsById(personnel.getIdPersonnel())){
            throw new RuntimeException("Le membre du Personnel avec l'ID" + personnel.getIdPersonnel() + " n'existe pas");
        }

        personnelRepository.update(personnel);
    }
}





