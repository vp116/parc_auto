package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.PersonnelService;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
private Mission missionEnEdition;
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

    private final ObservableList<Personnel> participantsList = FXCollections.observableArrayList();

    private final VehiculeService vehiculeService = new VehiculeService();
    private final PersonnelService personnelService = new PersonnelService();

    public AddMissionForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/add_mission_form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            configureTimeCombos();
            configureVehicleCombo();  // Décommenter et activer
            configurePersonnelCombo(); // Décommenter et activer
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
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


    // Méthode pour ajouter un participant
    public void ajouterParticipant(Personnel personnel) {
        if (personnel != null && !participantsList.contains(personnel)) {
            participantsList.add(personnel);
            updateParticipantsDisplay();
        }
    }

    private void updateParticipantsDisplay() {
        memberAdd.getChildren().clear();
        for (Personnel p : participantsList) {
            HBox hbox = new HBox(5);
            hbox.getChildren().add(new Label(p.getNom() + " " + p.getPrenom()));

            Button removeBtn = new Button("×");
            removeBtn.setOnAction(e -> {
                participantsList.remove(p);
                updateParticipantsDisplay();
            });
            hbox.getChildren().add(removeBtn);

            memberAdd.getChildren().add(hbox);
        }
    }


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
        Personnel selectedPersonnel = personnelCombo.getSelectionModel().getSelectedItem();
        if (selectedPersonnel != null) {
            ajouterParticipant(selectedPersonnel);
            personnelCombo.getSelectionModel().clearSelection();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun personnel sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un membre du personnel à ajouter");
            alert.showAndWait();
        }
    }

    public void setMissionData(Mission mission) {
        this.missionEnEdition = mission;
        vehicleCombo.setValue(mission.getVehicule());
//        startDatePicker.setValue();


    }

    public void clearForm() {
        this.missionEnEdition = null;
        vehicleCombo.getSelectionModel().clearSelection();
        personnelCombo.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        startTimeCombo.getSelectionModel().clearSelection();
        endDatePicker.setValue(null);
        endTimeCombo.getSelectionModel().clearSelection();
        circuitArea.clear();
        budgetField.clear();
        fuelField.clear();
        obsArea.clear();
        participantsList.clear();
        updateParticipantsDisplay();
    }

    public Mission createMissionFromField() {
       Mission  mission = (missionEnEdition != null) ? missionEnEdition : new Mission();

        // Validation des champs obligatoires
        if (vehicleCombo.getValue() == null) {
            throw new IllegalArgumentException("Un véhicule doit être sélectionné");
        }
        if (startDatePicker.getValue() == null || startTimeCombo.getValue() == null) {
            throw new IllegalArgumentException("La date et heure de début sont obligatoires");
        }
        if (endDatePicker.getValue() == null || endTimeCombo.getValue() == null) {
            throw new IllegalArgumentException("La date et heure de fin sont obligatoires");
        }

        // Récupérer et assigner le véhicule
        mission.setVehicule(vehicleCombo.getValue());

        // Dates et heures
        LocalDateTime startDateTime = LocalDateTime.of(
                startDatePicker.getValue(),
                startTimeCombo.getValue()
        );
        if(missionEnEdition.getDateDebut() == null){

        mission.setDateDebut(java.sql.Timestamp.valueOf(startDateTime));
        }

        LocalDateTime endDateTime = LocalDateTime.of(
                endDatePicker.getValue(),
                endTimeCombo.getValue()
        );

        if(missionEnEdition.getDateDebut() == null) {
            mission.setDateFin(java.sql.Timestamp.valueOf(endDateTime));
        }
        // Circuit
        mission.setCircuit(circuitArea.getText());

        // Budget
        try {
            if (!budgetField.getText().isEmpty()) {
                mission.setCout(Integer.parseInt(budgetField.getText()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le budget doit être un nombre valide");
        }

        // Carburant
        try {
            if (!fuelField.getText().isEmpty()) {
                mission.setCoutCarburant(Integer.parseInt(fuelField.getText()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le coût carburant doit être un nombre valide");
        }

        // Observations
        mission.setObservation(obsArea.getText());

        // Participants
        for (Personnel participant : participantsList) {
//            mission.getParticipants().add(participant);
        }

        return mission;
    }


}




