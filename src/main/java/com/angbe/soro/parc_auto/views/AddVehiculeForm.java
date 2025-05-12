package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddVehiculeForm extends GridPane implements DialogLauncher.EntityForm<Vehicule>  {
    private final VehiculeService vehiculeService = new VehiculeService();
    private Vehicule vehiculeEnEdition;

    @FXML
    private TextField chassisField;
    @FXML
    private TextField plateField;
    @FXML
    private ComboBox<String> brandCombo;
    @FXML
    private TextField modelField;
    @FXML
    private Spinner<Integer> seatsSpinner;
    @FXML
    private ComboBox<String> energyCombo;
    @FXML
    private TextField powerField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField priceField;

    public AddVehiculeForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("components/add_vehicule_form.fxml")
        );
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            initializeFields();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initializeFields() {
        // Initialisation des ComboBox
        brandCombo.setItems(FXCollections.observableArrayList(
                "Toyota", "Peugeot", "Renault", "Ford", "Hyundai"
        ));

        energyCombo.setItems(FXCollections.observableArrayList(
                "Essence", "Diesel", "Hybride", "Électrique"
        ));

        // Configuration du Spinner
        seatsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 9, 5)
        );

        // Configuration des validateurs
        setupValidators();
    }

    private void setupValidators() {
        // Validation numérique pour la puissance
        powerField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));

        // Validation numérique pour le prix
        priceField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }

    public Vehicule createVehiculeFromFields() throws IllegalArgumentException {
        Vehicule vehicule = (vehiculeEnEdition != null) ? vehiculeEnEdition : new Vehicule();

        // Validation des champs obligatoires
        if (chassisField.getText() == null || chassisField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de châssis est obligatoire");
        }
        if (plateField.getText() == null || plateField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }
        if (brandCombo.getValue() == null) {
            throw new IllegalArgumentException("La marque est obligatoire");
        }
        if (modelField.getText() == null || modelField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le modèle est obligatoire");
        }

        // Validation format châssis (17 caractères)
        if (chassisField.getText().length() != 17) {
            throw new IllegalArgumentException("Le numéro de châssis doit contenir 17 caractères");
        }

        // Validation format immatriculation (simplifiée)
        if (!plateField.getText().matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
            throw new IllegalArgumentException("Format d'immatriculation invalide. Exemple: AB-123-CD");
        }

        // Remplissage de l'objet Vehicule
        vehicule.setNumeroChassi(chassisField.getText().trim());
        vehicule.setImmatriculation(plateField.getText().trim());
        vehicule.setMarque(brandCombo.getValue());
        vehicule.setModele(modelField.getText().trim());
        vehicule.setNbPlaces(seatsSpinner.getValue());
        vehicule.setEnergie(energyCombo.getValue());
        vehicule.setCouleur(colorField.getText().trim());
        vehicule.setDateAcquisition(new Date());

        // État par défaut
        vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));
        vehicule.setDateEtat(new Date());

        try {
            vehicule.setPuissance(Integer.parseInt(powerField.getText()));
        } catch (NumberFormatException e) {
            vehicule.setPuissance(0);
        }

        try {
            vehicule.setPrix((int) Double.parseDouble(priceField.getText()));
        } catch (NumberFormatException e) {
            vehicule.setPrix(0);
        }

        return vehicule;
    }

    public void clearForm() {
        this.vehiculeEnEdition = null;
        chassisField.clear();
        plateField.clear();
        brandCombo.getSelectionModel().clearSelection();
        modelField.clear();
        seatsSpinner.getValueFactory().setValue(5);
        energyCombo.getSelectionModel().clearSelection();
        powerField.clear();
        colorField.clear();
        priceField.clear();
    }

    public void setVehiculeData(Vehicule vehicule) {
        this.vehiculeEnEdition = vehicule;
        chassisField.setText(vehicule.getNumeroChassi());
        plateField.setText(vehicule.getImmatriculation());
        brandCombo.setValue(vehicule.getMarque());
        modelField.setText(vehicule.getModele());
        seatsSpinner.getValueFactory().setValue(vehicule.getNbPlaces());
        energyCombo.setValue(vehicule.getEnergie());
        powerField.setText(String.valueOf(vehicule.getPuissance()));
        colorField.setText(vehicule.getCouleur());
        priceField.setText(String.valueOf(vehicule.getPrix()));
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public Vehicule createEntity() throws IllegalArgumentException {
        return createVehiculeFromFields();
    }

}