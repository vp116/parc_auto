package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.models.Vehicule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class AddMissionForm extends GridPane {

    @FXML
    public VBox memberAdd;
    @FXML
    private ComboBox<Vehicule> vehicleCombo;

    @FXML
    private ComboBox<Personnel> personnelCombo;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<LocalTime> startTimeCombo;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<LocalTime> endTimeCombo;

    @FXML
    private TextArea circuitArea;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField fuelField;

    @FXML
    private TextArea obsArea;


    public AddMissionForm() {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/add_mission_form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            configureTimeCombos();

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }


       /* configureVehicleCombo();
        configurePersonnelCombo();
        configureTimeCombos();*/
    }

    private void configureTimeCombos() {
        ObservableList<LocalTime> hours = FXCollections.observableArrayList();
        IntStream.rangeClosed(8, 18).forEach(hour -> {
            hours.add(LocalTime.of(hour, 0));
            hours.add(LocalTime.of(hour, 30));
        });

        StringConverter<LocalTime> timeConverter = new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime time) {
                return time != null ? time.format(formatter) : "";
            }

            @Override
            public LocalTime fromString(String string) {
                return LocalTime.parse(string, formatter);
            }
        };

        startTimeCombo.setItems(hours);
        startTimeCombo.setConverter(timeConverter);
        startTimeCombo.setPromptText("Heure début");

        endTimeCombo.setItems(hours);
        endTimeCombo.setConverter(timeConverter);
        endTimeCombo.setPromptText("Heure fin");
    }

/*
    private void configureVehicleCombo() {
        ObservableList<Vehicule> vehicules = FXCollections.observableArrayList(vehiculeService.getAllVehicules());
        vehicleCombo.setItems(vehicules);
        vehicleCombo.setConverter(new StringConverter<Vehicule>() {
            @Override
            public String toString(Vehicule vehicule) {
                return vehicule != null
                        ? String.format("%s %s (%s)", vehicule.getMarque(), vehicule.getModele(), vehicule.getImmatriculation())
                        : "";
            }

            @Override
            public Vehicule fromString(String string) {
                return null;
            }
        });
    }

    private void configurePersonnelCombo() {
        ObservableList<Personnel> personnelList = FXCollections.observableArrayList(personnelService.getAllPersonnel());
        personnelCombo.setItems(personnelList);
        formateComboBox(personnelCombo);
    }

   */

    private void formateComboBox(ComboBox<Personnel> personnelCombo) {
        personnelCombo.setConverter(new StringConverter<Personnel>() {
            @Override
            public String toString(Personnel personnel) {
                return personnel != null
                        ? String.format("%s %s", personnel.getNom(), personnel.getPrenom())
                        : "";
            }

            @Override
            public Personnel fromString(String string) {
                return null;
            }
        });
    }

    /*// Appelée depuis l’extérieur pour initialiser les services
    public void setServices(VehiculeService vehiculeService,
                            MissionService missionService,
                            PersonnelService personnelService) {
        this.vehiculeService = vehiculeService;
        this.missionService = missionService;
        this.personnelService = personnelService;
    }*/

    // Méthode appelée sur le clic du bouton "+"
    @FXML
    private void handleAddPersonnel() {
        // TODO : Ouvrir une fenêtre pour ajouter un nouveau personnel
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout de personnel");
        alert.setHeaderText(null);
        alert.setContentText("Ouverture de la fenêtre d’ajout de personnel (à implémenter)");
        alert.showAndWait();
    }

    public Mission createMissionFromField() {
        var mission = new Mission();

        // Récupérer et assigner le véhicule
        if (vehicleCombo.getValue() != null) {
            mission.setVehicule(vehicleCombo.getValue());
        }

        // Récupérer et assigner les dates et heures
        if (startDatePicker.getValue() != null && startTimeCombo.getValue() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(
                    startDatePicker.getValue(),
                    startTimeCombo.getValue()
            );
            mission.setDateDebut(java.sql.Timestamp.valueOf(startDateTime));
        }

        if (endDatePicker.getValue() != null && endTimeCombo.getValue() != null) {
            LocalDateTime endDateTime = LocalDateTime.of(
                    endDatePicker.getValue(),
                    endTimeCombo.getValue()
            );
            mission.setDateFin(java.sql.Timestamp.valueOf(endDateTime));
        }

        // Récupérer et assigner le circuit
        if (circuitArea.getText() != null && !circuitArea.getText().isEmpty()) {
            mission.setCircuit(circuitArea.getText());
        }

        // Récupérer et assigner le budget
        if (budgetField.getText() != null && !budgetField.getText().isEmpty()) {
            try {
                mission.setCout(Integer.parseInt(budgetField.getText()));
            } catch (NumberFormatException e) {
                // Gérer l'erreur de conversion si nécessaire
                mission.setCout(null);
            }
        }

        // Récupérer et assigner le coût carburant
        if (fuelField.getText() != null && !fuelField.getText().isEmpty()) {
            try {
                mission.setCoutCarburant(Integer.parseInt(fuelField.getText()));
            } catch (NumberFormatException e) {
                // Gérer l'erreur de conversion si nécessaire
                mission.setCoutCarburant(null);
            }
        }

        // Récupérer et assigner les observations
        if (obsArea.getText() != null && !obsArea.getText().isEmpty()) {
            mission.setObservation(obsArea.getText());
        }

        return mission;
    }
}



