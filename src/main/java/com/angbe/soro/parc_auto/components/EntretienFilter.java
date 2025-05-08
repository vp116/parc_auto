package com.angbe.soro.parc_auto.components;

import com.angbe.soro.parc_auto.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntretienFilter extends HBox {
    @FXML
    private DatePicker enterDatePicker;

    @FXML
    private Button filterButton;

    @FXML
    private DatePicker sortieDatePicker;

    @FXML
    private ComboBox<?> statusCombo;
    public EntretienFilter() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/entretien-filter.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            // Initialisation des valeurs par défaut
//            initializeComboBoxes();

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
