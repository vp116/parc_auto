package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.components.MissionFilter;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.MissionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionController implements Initializable, AutoCloseable {
    @FXML
    private ScrollPane missionScrollPane;
    @FXML
    private VBox missionContainer;
    @FXML
    private MissionFilter missionFilter;
    @FXML
    private Button addMissionBtn;
    private MissionService missionService;
    private EntityManagerFactory emf;
    private EntityManager em;
    private DynamicTableCard<Mission> missionCardTable;


    private void loadMissions() {
        missionContainer.getChildren().clear();

        // Exemple avec des données statiques
        List<Mission> missions = missionService.getAllMissions();

        missions.forEach(mission -> {
            HBox card = createMissionCard(mission);
            missionContainer.getChildren().add(card);
        });
    }

    private HBox createMissionCard(Mission mission) {
        HBox card = new HBox(15);
        card.getStyleClass().add("mission-card");
        card.setPadding(new Insets(15));

        Label vehicleLabel = new Label(mission.getVehicule().getImmatriculation());
        Label periodLabel = new Label(mission.getDateDebut() + " - " + mission.getDateFin());
        Label statusLabel = new Label(mission.getObservation());
        statusLabel.getStyleClass().add("status-" + mission.getObservation().toLowerCase().replace(" ", "-"));

        card.getChildren().addAll(vehicleLabel, periodLabel, statusLabel);
        return card;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            // Initialisation de l'EntityManager et AppConfig
            emf = Persistence.createEntityManagerFactory("bdGestionParcAuto");
            em = emf.createEntityManager();
            AppConfig.initialize(em);

            // Initialisation du service
            missionService = new MissionService();

            // Affichage des véhicules dans la table
            loadMissions();

            missionFilter.setOnFilterAction(e -> applyFilters());

            addMissionBtn.setOnAction(e -> DialogLauncher.showAddMissionDialog());


        } catch (Exception e) {
            Logger.getLogger(VehiculeController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void showAddMissionDialog() {



    }

    private void applyFilters() {
        System.out.println("applyFilters");
    }


    @Override
    public void close() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}

