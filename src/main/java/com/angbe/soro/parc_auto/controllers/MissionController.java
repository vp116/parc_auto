package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.components.MissionFilter;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.services.MissionService;
import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.angbe.soro.parc_auto.ViewFactory.ShowEchecEnregAlert;
import static com.angbe.soro.parc_auto.ViewFactory.showSuccessAlert;

public class MissionController {
    @FXML
    private ScrollPane missionScrollPane;
    @FXML
    private VBox missionContainer;
    @FXML
    private MissionFilter missionFilter;
    @FXML
    private Button addMissionBtn;
    private final MissionService missionService = new MissionService();
    private EntityManagerFactory emf;
    private EntityManager em;
    private DynamicTableCard<Mission> missionCardTable;


    private void loadMissions() {
        // Récupérer les missions depuis le service
        List<Mission> missions = missionService.getAllMissions();

        // Créer les boutons d'export et d'impression
        var btnExport = new Button("", new FontAwesomeIconView("fileExport"));
        btnExport.getStyleClass().add("btn-actions");
        var btnPrint = new Button("", new FontAwesomeIconView("print"));
        btnPrint.getStyleClass().add("btn-actions");

        // Initialiser le DynamicTableCard
        missionCardTable = new DynamicTableCard<>("Liste des missions", null, null, List.of(btnExport, btnPrint), true);
        missionCardTable.setItems(FXCollections.observableArrayList(missions));

        // Ajouter les colonnes
        missionCardTable.addCustomColumn("Véhicule", m -> m.getVehicule().getImmatriculation() + " (" + m.getVehicule().getMarque() + " " + m.getVehicule().getModele() + ")");
        missionCardTable.addCustomColumn("Date début", m -> new SimpleDateFormat("dd/MM/yyyy").format(m.getDateDebut()));
        missionCardTable.addCustomColumn("Date fin", m -> new SimpleDateFormat("dd/MM/yyyy").format(m.getDateFin()));
        missionCardTable.addPropertyColumn("Circuit", "circuit");
        missionCardTable.addCustomColumn("Budget", m -> NumberFormat.getInstance(Locale.FRANCE).format(m.getCout()) + " FCFA");
        missionCardTable.addPropertyColumn("Observation", "observation");
        missionCardTable.addColumn(DynamicTableCard.createActionsColumn("Actions", this::viewMission, this::updateMission, this::deleteMission));

        // Ajouter le tableau au ScrollPane
        missionScrollPane.setContent(missionCardTable);
    }

    private void updateMission(Mission mission) {
    }

    private void viewMission(Mission mission) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la mission");
        alert.setHeaderText("Informations sur la mission");

        String content = "Véhicule: " + mission.getVehicule().getImmatriculation() + "\n" +
                "Circuit: " + mission.getCircuit() + "\n" +
                "Période: " + new SimpleDateFormat("dd/MM/yyyy").format(mission.getDateDebut()) +
                " - " + new SimpleDateFormat("dd/MM/yyyy").format(mission.getDateFin()) + "\n" +
                "Budget: " + NumberFormat.getInstance(Locale.FRANCE).format(mission.getCout()) + " FCFA\n" +
                "Coût carburant: " + NumberFormat.getInstance(Locale.FRANCE).format(mission.getCoutCarburant()) + " FCFA\n" +
                "Observation: " + mission.getObservation();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void deleteMission(Mission mission) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la mission");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette mission ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                missionService.deleteMission(mission.getIdMission());
                refreshMissionTable();
                showSuccessAlert("Mission supprimée avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        }
    }

    private void applyFilters() {
        // Récupérer les valeurs des filtres depuis missionFilter
        String vehicule = missionFilter.getSelectedVehicule();
        LocalDate dateDebut = missionFilter.getDateDebut();
        LocalDate dateFin = missionFilter.getDateDebut();

        // Filtrer les missions
        List<Mission> filteredMissions = missionService.getAllMissions().stream()
                .filter(m -> vehicule == null || vehicule.isEmpty() ||
                        m.getVehicule().getImmatriculation().contains(vehicule) ||
                        m.getVehicule().getMarque().contains(vehicule))
                .filter(m -> dateDebut == null ||
                        m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(dateDebut) ||
                        m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(dateDebut))
                .filter(m -> dateFin == null ||
                        m.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(dateFin) ||
                        m.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(dateFin))
                .collect(Collectors.toList());

        // Mettre à jour le tableau
        missionCardTable.setItems(FXCollections.observableArrayList(filteredMissions));
    }

    private void refreshMissionTable() {
        missionCardTable.getTableView().getItems().setAll(missionService.getAllMissions());
    }


}

