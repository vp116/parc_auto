package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.models.EtatVoiture;
import com.angbe.soro.parc_auto.models.Vehicule;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.Date;

public class AddVehicleForm {
    private TextField chassisField;
    private TextField plateField;
    private ComboBox<String> brandCombo;
    private TextField modelField;
    private Spinner<Integer> seatsSpinner;
    private ComboBox<String> energyCombo;
    private TextField powerField;
    private TextField colorField;
    private TextField priceField;

    public Dialog<Vehicule> createAddVehicleDialog() {
        Dialog<Vehicule> dialog = new Dialog<>();
        dialog.setTitle("Nouveau véhicule");
        dialog.setHeaderText("Remplissez les informations du véhicule");

        // Appliquer le style CSS
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/com/angbe/soro/parc_auto/styles/style.css").toExternalForm()
        );
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        // Conteneur principal
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        // Initialisation des champs
        initializeFields();

        // Configuration des validateurs
        setupValidators();

        // Ajout des éléments au grid
        grid.add(new Label("N° de châssis:"), 0, 0);
        grid.add(chassisField, 1, 0);
        grid.add(new Label("Immatriculation:"), 0, 1);
        grid.add(plateField, 1, 1);
        grid.add(new Label("Marque:"), 0, 2);
        grid.add(brandCombo, 1, 2);
        grid.add(new Label("Modèle:"), 0, 3);
        grid.add(modelField, 1, 3);
        grid.add(new Label("Nombre de places:"), 0, 4);
        grid.add(seatsSpinner, 1, 4);
        grid.add(new Label("Énergie:"), 0, 5);
        grid.add(energyCombo, 1, 5);
        grid.add(new Label("Puissance (CV):"), 0, 6);
        grid.add(powerField, 1, 6);
        grid.add(new Label("Couleur:"), 0, 7);
        grid.add(colorField, 1, 7);
        grid.add(new Label("Prix:"), 0, 8);
        grid.add(priceField, 1, 8);

        // Boutons
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Activer/désactiver le bouton Enregistrer selon la validation
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(
                chassisField.textProperty().isEmpty()
                        .or(plateField.textProperty().isEmpty())
        );

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en objet Vehicule
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {

                return createVehiculeFromFields();

            }
            return null;
        });

        return dialog;
    }

    private void initializeFields() {
        chassisField = new TextField();
        plateField = new TextField();

        brandCombo = new ComboBox<>();
        brandCombo.getItems().addAll("Toyota", "Peugeot", "Renault", "Ford", "Hyundai");
        brandCombo.setPromptText("Sélectionnez une marque");

        modelField = new TextField();

        seatsSpinner = new Spinner<>(2, 9, 5);
        seatsSpinner.setEditable(true);

        energyCombo = new ComboBox<>();
        energyCombo.getItems().addAll("Essence", "Diesel", "Hybride", "Électrique");
        energyCombo.setPromptText("Sélectionnez un type");

        powerField = new TextField();
        colorField = new TextField();
        priceField = new TextField();

        // Appliquer les styles CSS
        chassisField.getStyleClass().add("form-field");
        plateField.getStyleClass().add("form-field");
        brandCombo.getStyleClass().add("form-field");
        modelField.getStyleClass().add("form-field");
        seatsSpinner.getStyleClass().add("form-field");
        energyCombo.getStyleClass().add("form-field");
        powerField.getStyleClass().add("form-field");
        colorField.getStyleClass().add("form-field");
        priceField.getStyleClass().add("form-field");
    }

    private void setupValidators() {
        // Validation numérique pour la puissance et le prix
        powerField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));

        priceField.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                if (string.matches("\\d*(\\.\\d*)?")) {
                    return string;
                }
                return "";
            }
        }));
    }

    private boolean validateForm() {
        // Validation du numéro de châssis (17 caractères standard)
        if (chassisField.getText().length() != 17) {
            showAlert("Erreur de validation", "Le numéro de châssis doit contenir exactement 17 caractères");
            return false;
        }

        // Validation de l'immatriculation (format simplifié)
        if (!plateField.getText().matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
            showAlert("Erreur de validation", "Format d'immatriculation invalide. Exemple: AB-123-CD");
            return false;
        }

        return true;
    }

    private Vehicule createVehiculeFromFields() {
        Vehicule vehicule = new Vehicule();
        vehicule.setNumeroChassi(chassisField.getText());
        vehicule.setEtatVoiture(new EtatVoiture(1, "Disponible"));
        vehicule.setImmatriculation(plateField.getText());
        vehicule.setMarque(brandCombo.getValue());
        vehicule.setModele(modelField.getText());
        vehicule.setNbPlaces(seatsSpinner.getValue());
        vehicule.setEnergie(energyCombo.getValue());
        vehicule.setDateEtat(new Date());
        vehicule.setDateAcquisition(new Date());

        try {
            vehicule.setPuissance(Integer.parseInt(powerField.getText()));
        } catch (NumberFormatException e) {
            vehicule.setPuissance(0);
        }

        vehicule.setCouleur(colorField.getText());

        try {
            vehicule.setPrix(Integer.parseInt(priceField.getText()));
        } catch (NumberFormatException e) {
            vehicule.setPrix(0);
        }

        return vehicule;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}