package com.angbe.soro.parc_auto.components;

import com.angbe.soro.parc_auto.MainApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MissionFilter extends HBox {
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button filterButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private ComboBox<String> vehiculeComboBox;

    public MissionFilter() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/mission-filter.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            // Initialisation des valeurs par défaut
            initializeComboBoxes();

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initializeComboBoxes() {
        // Initialisation de l'état (vous pouvez ajouter d'autres états si nécessaire)
        statusCombo.getSelectionModel().selectFirst();

        // La marque est déjà initialisée dans le FXML
        vehiculeComboBox.getSelectionModel().selectFirst();
    }

    // Méthode pour récupérer la date de début
    public LocalDate getDateDebut() {
        return startDatePicker.getValue();
    }

    // Méthode pour récupérer la date de fin
    public LocalDate getDateFin() {
        return endDatePicker.getValue();
    }

    // Méthode pour récupérer le statut sélectionné
    public String getSelectedStatus() {
        return statusCombo.getValue();
    }

    // Méthode pour récupérer le véhicule sélectionné
    public String getSelectedVehicule() {
        return vehiculeComboBox.getValue();
    }

    // Méthode pour définir l'action du bouton Filtrer
    public void setOnFilterAction(EventHandler<ActionEvent> eventHandler) {
        filterButton.setOnAction(eventHandler);
    }

    // Méthode pour réinitialiser les filtres
    public void resetFilters() {
        statusCombo.getSelectionModel().selectFirst();
        vehiculeComboBox.getSelectionModel().selectFirst();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
}
