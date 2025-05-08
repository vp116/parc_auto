package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class AddEntretienForm extends GridPane {

    private final VehiculeService vehiculeService = new VehiculeService();
    @FXML
    private ComboBox<Vehicule> vehiculeCombo;
    @FXML
    private DatePicker dateEntreePicker;
    @FXML
    private ComboBox<LocalTime> heureEntreeCombo;
    @FXML
    private DatePicker dateSortiePicker;
    @FXML
    private ComboBox<LocalTime> heureSortieCombo;
    @FXML
    private TextField motifField;
    @FXML
    private TextField lieuField;
    @FXML
    private TextField coutField;
    @FXML
    private TextArea observationArea;

    public AddEntretienForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/add_entretien_form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            configureCombos();
            configureTimeCombos();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void configureCombos() {
        // Configuration ComboBox Véhicule
        vehiculeCombo.setItems(FXCollections.observableArrayList(vehiculeService.getAllVehicules()));
        vehiculeCombo.setConverter(new StringConverter<Vehicule>() {
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

    private void configureTimeCombos() {
        ObservableList<LocalTime> hours = FXCollections.observableArrayList();
        IntStream.rangeClosed(0, 23).forEach(hour -> {
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

        heureEntreeCombo.setItems(hours);
        heureEntreeCombo.setConverter(timeConverter);
        heureSortieCombo.setItems(hours);
        heureSortieCombo.setConverter(timeConverter);
    }

    public Entretien createEntretienFromFields() {
        Entretien entretien = new Entretien();

        // Validation des champs obligatoires
        if (vehiculeCombo.getValue() == null) {
            throw new IllegalArgumentException("Un véhicule doit être sélectionné");
        }
        if (dateEntreePicker.getValue() == null || heureEntreeCombo.getValue() == null) {
            throw new IllegalArgumentException("La date et heure d'entrée sont obligatoires");
        }
        if (motifField.getText() == null || motifField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le motif est obligatoire");
        }

        // Véhicule
        entretien.setVehicule(vehiculeCombo.getValue());

        // Dates
        LocalDateTime entreeDateTime = LocalDateTime.of(
                dateEntreePicker.getValue(),
                heureEntreeCombo.getValue()
        );
        entretien.setDateEntree(java.sql.Timestamp.valueOf(entreeDateTime));

        if (dateSortiePicker.getValue() != null && heureSortieCombo.getValue() != null) {
            LocalDateTime sortieDateTime = LocalDateTime.of(
                    dateSortiePicker.getValue(),
                    heureSortieCombo.getValue()
            );
            entretien.setDateSortie(java.sql.Timestamp.valueOf(sortieDateTime));
        }

        // Autres champs
        entretien.setMotif(motifField.getText().trim());
        entretien.setLieu(lieuField.getText().trim());

        try {
            if (!coutField.getText().isEmpty()) {
                entretien.setCout(Integer.parseInt(coutField.getText()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le coût doit être un nombre valide");
        }

        entretien.setObservation(observationArea.getText());

        return entretien;
    }

    public void clearForm() {
        vehiculeCombo.getSelectionModel().clearSelection();
        dateEntreePicker.setValue(null);
        heureEntreeCombo.getSelectionModel().clearSelection();
        dateSortiePicker.setValue(null);
        heureSortieCombo.getSelectionModel().clearSelection();
        motifField.clear();
        lieuField.clear();
        coutField.clear();
        observationArea.clear();
    }

    public void setEntretienData(Entretien entretien) {

    }
}