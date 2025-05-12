package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.components.StatCard;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.AssuranceService;
import com.angbe.soro.parc_auto.services.VehiculeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    private AssuranceService assuranceService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            // Initialisation des services
            vehiculeService = new VehiculeService();
            assuranceService = new AssuranceService();

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
        DynamicTableCard<Vehicule> vehiculeCardTable = new DynamicTableCard<>("Derniers véhicules ajoutés", null, null, List.of(voirBtn), true);
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
        listContent.setContent(vehiculeCardTable);
    }


    private void loadCard() {
        // Mise à jour des valeurs des cartes
        int vehiculesDisponibles = vehiculeService.getVehiculesByEtat("Disponible").size();
        int vehiculesEnMission = vehiculeService.getVehiculesByEtat("En mission").size();
        int vehiculesEnEntretien = vehiculeService.getVehiculesByEtat("En entretien").size();

        // Initialisation du service d'assurance si nécessaire

        // Récupérer les assurances qui expirent dans les 30 jours
        LocalDateTime dateExpiration = LocalDateTime.now().plusDays(30);
        int assurancesExpirant = assuranceService.getAssurancesExpirantAvant(dateExpiration).size();

        // Mise à jour des valeurs des cartes
        vehiculeCard.setValue(String.valueOf(vehiculesDisponibles));
        vehiculeCard.setTitle("Véhicules disponibles");
        vehiculeCard.setTrend(vehiculesDisponibles > 0 ? "arrowUp" : "arrowDown",
                vehiculesDisponibles + " véhicules",
                vehiculesDisponibles > 0 ? Color.web("#10b981") : Color.web("#ef4444"));

        routeCard.setValue(String.valueOf(vehiculesEnMission));
        routeCard.setTitle("Véhicules en mission");
        routeCard.setTrend(vehiculesEnMission > 0 ? "arrowUp" : "arrowDown",
                vehiculesEnMission + " véhicules",
                vehiculesEnMission > 0 ? Color.web("#10b981") : Color.web("#ef4444"));

        entretientCard.setValue(String.valueOf(vehiculesEnEntretien));
        entretientCard.setTitle("Véhicules en entretien");
        entretientCard.setTrend(vehiculesEnEntretien > 0 ? "arrowUp" : "arrowDown",
                vehiculesEnEntretien + " véhicules",
                vehiculesEnEntretien > 0 ? Color.web("#ef4444") : Color.web("#10b981"));

        assuranceCard.setValue(String.valueOf(assurancesExpirant));
        assuranceCard.setTitle("Assurances expirant bientôt");
        assuranceCard.setTrend(assurancesExpirant > 0 ? "exclamationTriangle" : "check",
                assurancesExpirant + " assurances",
                assurancesExpirant > 0 ? Color.web("#f59e0b") : Color.web("#10b981"));
    }

    @Override
    public void close() throws Exception {
        // Utilisation de la méthode close() d'AppConfig
        AppConfig.close();
    }
}
