package com.angbe.soro.parc_auto.components;

import com.angbe.soro.parc_auto.MainApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehiculeFilter extends VBox {

    @FXML
    private ComboBox<String> marqueComboBox;
    @FXML
    private ComboBox<String> etatComboBox;
    @FXML
    private DatePicker dateAcquisitionDatePicker;
    @FXML
    private Button filterButton;

    public VehiculeFilter() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/vehicule-filter.fxml"));
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
        etatComboBox.getItems().addAll("Tous", "Disponible", "En maintenance", "Loué", "Hors service");
        etatComboBox.getSelectionModel().selectFirst();

        // Initialisation des marques de véhicules courantes
        if (marqueComboBox.getItems().isEmpty()) {
            marqueComboBox.getItems().addAll("Tous", "Toyota", "Renault", "Peugeot", "Mercedes", "BMW", "Audi", "Volkswagen", "Ford", "Nissan", "Hyundai");
            marqueComboBox.getSelectionModel().selectFirst();
        }
    }

    // Méthodes pour récupérer les valeurs des filtres

    public String getSelectedMarque() {
        return marqueComboBox.getValue();
    }

    public String getSelectedEtat() {
        return etatComboBox.getValue();
    }

    public LocalDate getSelectedDateAcquisition() {
        return dateAcquisitionDatePicker.getValue();
    }

    // Méthode pour définir l'action du bouton Filtrer
    public void setOnFilterAction(EventHandler<ActionEvent> eventHandler) {
        filterButton.setOnAction(eventHandler);
    }

    // Méthode pour réinitialiser les filtres
    public void resetFilters() {
        marqueComboBox.getSelectionModel().selectFirst();
        etatComboBox.getSelectionModel().selectFirst();
        dateAcquisitionDatePicker.setValue(null);
    }
}

