package soro.charles.parcauto;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AddVehicleForm {
    public Dialog<Boolean> createAddVehicleDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Nouveau véhicule");
        dialog.setHeaderText("Remplissez les informations du véhicule");

        // Conteneur principal
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        // Champs du formulaire
        Label chassisLabel = new Label("N° de châssis:");
        TextField chassisField = new TextField();

        Label plateLabel = new Label("Immatriculation:");
        TextField plateField = new TextField();

        Label brandLabel = new Label("Marque:");
        ComboBox<String> brandCombo = new ComboBox<>();
        brandCombo.getItems().addAll("Toyota", "Peugeot", "Renault", "Ford", "Hyundai");

        Label modelLabel = new Label("Modèle:");
        TextField modelField = new TextField();

        Label seatsLabel = new Label("Nombre de places:");
        Spinner<Integer> seatsSpinner = new Spinner<>(2, 9, 5);

        Label energyLabel = new Label("Énergie:");
        ComboBox<String> energyCombo = new ComboBox<>();
        energyCombo.getItems().addAll("Essence", "Diesel", "Hybride", "Électrique");

        Label powerLabel = new Label("Puissance (CV):");
        TextField powerField = new TextField();

        Label colorLabel = new Label("Couleur:");
        TextField colorField = new TextField();

        Label priceLabel = new Label("Prix:");
        TextField priceField = new TextField();

        // Ajout des éléments au grid
        grid.add(chassisLabel, 0, 0);
        grid.add(chassisField, 1, 0);
        grid.add(plateLabel, 0, 1);
        grid.add(plateField, 1, 1);
        grid.add(brandLabel, 0, 2);
        grid.add(brandCombo, 1, 2);
        grid.add(modelLabel, 0, 3);
        grid.add(modelField, 1, 3);
        grid.add(seatsLabel, 0, 4);
        grid.add(seatsSpinner, 1, 4);
        grid.add(energyLabel, 0, 5);
        grid.add(energyCombo, 1, 5);
        grid.add(powerLabel, 0, 6);
        grid.add(powerField, 1, 6);
        grid.add(colorLabel, 0, 7);
        grid.add(colorField, 1, 7);
        grid.add(priceLabel, 0, 8);
        grid.add(priceField, 1, 8);

        // Boutons
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return true;
            }
            return null;
        });

        return dialog;
    }
}