package com.angbe.soro.parc_auto;

import com.angbe.soro.parc_auto.models.Assurance;
import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.AssuranceService;
import com.angbe.soro.parc_auto.services.EntretienService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainTest {
    public static void main(String[] args) {
        // Initialisation de l'EntityManager et des services
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bdGestionParcAuto");
        EntityManager em = emf.createEntityManager();

        // Initialisation de AppConfig avec l'EntityManager
        AppConfig.initialize(em);

        // Création des services
        AssuranceService assuranceService = new AssuranceService();
        EntretienService entretienService = new EntretienService();

        try {
            // Test AssuranceService
            testAssuranceService(assuranceService);

            // Test EntretienService
            testEntretienService(entretienService);

        } finally {
            // Fermeture des ressources
            em.close();
            emf.close();
        }
    }

    private static void testAssuranceService(AssuranceService service) {
        System.out.println("\n=== TEST ASSURANCE SERVICE ===");

        // Création d'une assurance
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(1);
        assurance.setDateDebutAssurance(new Date());
        assurance.setDateFinAssurance(new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000)); // +1 an
        assurance.setAgence("AXA Assurance");
        assurance.setCoutAssurance(500.0);

        // Sauvegarde de l'assurance
        assurance = service.saveAssurance(assurance);
        System.out.println("Assurance sauvegardée: " + assurance.getNumCarteAssurance());

        // Récupération par ID
        Optional<Assurance> assuranceOpt = service.getAssuranceById(1);
        assuranceOpt.ifPresent(a -> System.out.println("Assurance trouvée: " + a.getAgence()));

        // Liste de toutes les assurances
        List<Assurance> assurances = service.getAllAssurances();
        System.out.println("Nombre d'assurances: " + assurances.size());

        // Coût total des assurances
        double coutTotal = service.getCoutTotalAssurances();
        System.out.println("Coût total des assurances: " + coutTotal);

        // Suppression de l'assurance (à décommenter si nécessaire)
        // service.deleteAssurance(1);
    }

    private static void testEntretienService(EntretienService service) {
        System.out.println("\n=== TEST ENTRETIEN SERVICE ===");

        // Création d'un véhicule pour le test
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(1);
        vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));

        // Création d'un entretien
        Entretien entretien = new Entretien();
        entretien.setIdEntretien(1);
        entretien.setVehicule(vehicule);
        entretien.setDateSortie(new Date());
        entretien.setMotif("Vidange et contrôle technique");
        entretien.setCout(200);
        entretien.setLieu("Garage du Centre");

        try {
            // Planification de l'entretien
            entretien = service.planifierEntretien(entretien);
            System.out.println("Entretien planifié avec ID: " + entretien.getIdEntretien());

            // Liste des entretiens pour le véhicule
            List<Entretien> entretiens = service.getEntretiensByVehicule(1);
            System.out.println("Nombre d'entretiens pour le véhicule: " + entretiens.size());

            // Clôture de l'entretien
            service.cloturerEntretien(1);
            System.out.println("Entretien clôturé");

            // Coût total des entretiens
            double coutTotal = service.getCoutTotalEntretiens();
            System.out.println("Coût total des entretiens: " + coutTotal);

        } catch (IllegalStateException e) {
            System.err.println("Erreur lors de la planification de l'entretien: " + e.getMessage());
        }
    }
}