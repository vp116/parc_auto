package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.component.DynamicTableCard;
import com.angbe.soro.parc_auto.component.StatCard;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.VehiculeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardContentController implements Initializable, AutoCloseable {

    @FXML
    public StatCard vehiculeCard;
    @FXML
    public StatCard routeCard;
    @FXML
    public StatCard entretientCard;
    @FXML
    public StatCard assuranceCard;
    @FXML
    public ScrollPane listContent;
    private VehiculeService vehiculeService;
    private EntityManagerFactory emf;
    private EntityManager em;

    private DynamicTableCard<Vehicule> vehiculeCardTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Initialisation de l'EntityManager et AppConfig
            emf = Persistence.createEntityManagerFactory("bdGestionParcAuto");
            em = emf.createEntityManager();
            AppConfig.initialize(em);

            // Initialisation du service
            vehiculeService = new VehiculeService();

            // Affichage des véhicules dans la table
            loadVehicles();
            loadCard();

        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les exceptions avec un message d'erreur (UI ou log)
        }
    }

    private void loadVehicles() {
        // Supposons que le service VehiculeService a une méthode pour récupérer les véhicules
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        var voirBtn = new Button("Voir tous");
        voirBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: blue;");
        // Créer un DynamicTableCard pour afficher les véhicules
        vehiculeCardTable = new DynamicTableCard<>("Derniers véhicules ajoutés", null, null, List.of(voirBtn));
        vehiculeCardTable.setItems(FXCollections.observableArrayList(vehicules));
        vehiculeCardTable.addPropertyColumn("Immatriculation", "immatriculation");
        vehiculeCardTable.addCustomColumn("Marque/Modèle", v -> v.getMarque() + " " + v.getModele());
        vehiculeCardTable.addColumn(DynamicTableCard.createBadgeColumn("État", vehicule -> vehicule.getEtatVoiture().getLibelleEtat(), libelle -> {
            return switch (libelle) {
                case "Disponible" -> "status-available";
                case "En mission" -> "status-mission";
                case "En entretien" -> "status-maintenance";
                case "Hors service" -> "status-outofservice";
                default -> "status-default";
            };

        }));
        vehiculeCardTable.addCustomColumn("Date acquisition", v -> formatter.format(v.getDateAcquisition()));
        vehiculeCardTable.addColumn(DynamicTableCard.createActionsColumn("Actions", null, null, null));
        listContent.setContent(vehiculeCardTable);
    }


    private void loadCard() {
        vehiculeCard.setValue(String.valueOf(vehiculeService.getVehiculesByEtat("Disponible").size()));
        routeCard.setValue(String.valueOf(vehiculeService.getVehiculesByEtat("En mission").size()));
        entretientCard.setValue(String.valueOf(vehiculeService.getVehiculesByEtat("En entretien").size()));
    }

    @Override
    public void close() throws Exception {
        // Fermeture des ressources de manière explicite
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
