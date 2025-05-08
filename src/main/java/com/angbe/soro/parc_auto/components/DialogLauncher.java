package com.angbe.soro.parc_auto.components;

import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.views.AddEntretienForm;
import com.angbe.soro.parc_auto.views.AddMissionForm;
import com.angbe.soro.parc_auto.views.AddPersonnelForm;
import com.angbe.soro.parc_auto.views.AddVehiculeForm;
import javafx.scene.control.*;

import java.util.Optional;

public class DialogLauncher {

    // Méthodes pour les missions
    public static Optional<Mission> showAddMissionDialog() {
        AddMissionForm content = new AddMissionForm();
        return createEntityDialog((EntityForm<Mission>) content, "Nouvelle mission", "Remplissez les informations de la mission");
    }

    public static Optional<Mission> showEditMissionDialog(Mission mission) {
        AddMissionForm content = new AddMissionForm();
        content.setMissionData(mission);
        return createEntityDialog((EntityForm<Mission>) content, "Modifier mission", "Modifiez les informations de la mission");
    }

    // Méthodes pour le personnel
    public static Optional<Personnel> showAddPersonnelDialog() {
        AddPersonnelForm content = new AddPersonnelForm();
        return createEntityDialog(content, "Nouveau personnel", "Remplissez les informations du personnel");
    }

    public static Optional<Personnel> showEditPersonnelDialog(Personnel personnel) {
        AddPersonnelForm content = new AddPersonnelForm();
        content.setPersonnelData(personnel);
        return createEntityDialog(content, "Modifier personnel", "Modifiez les informations du personnel");
    }

    // Méthodes pour les entretiens
    public static Optional<Entretien> showAddEntretienDialog() {
        AddEntretienForm content = new AddEntretienForm();
        return createEntityDialog((EntityForm<Entretien>) content, "Nouvel entretien", "Remplissez les informations de l'entretien");
    }

    public static Optional<Entretien> showEditEntretienDialog(Entretien entretien) {
        AddEntretienForm content = new AddEntretienForm();
        content.setEntretienData(entretien);
        return createEntityDialog((EntityForm<Entretien>) content, "Modifier entretien", "Modifiez les informations de l'entretien");
    }

    // Méthodes pour les véhicules
    public static Optional<Vehicule> showAddVehiculeDialog() {
        AddVehiculeForm content = new AddVehiculeForm();
        return createEntityDialog(content, "Nouveau véhicule", "Remplissez les informations du véhicule");
    }

    public static Optional<Vehicule> showEditVehiculeDialog(Vehicule vehicule) {
        AddVehiculeForm content = new AddVehiculeForm();
        content.setVehiculeData(vehicule);
        return createEntityDialog(content, "Modifier véhicule", "Modifiez les informations du véhicule");
    }

    // Méthode générique pour créer les dialogues
    private static <T> Optional<T> createEntityDialog(EntityForm<T> form, String title, String header) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(form.getContent());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Désactiver le bouton OK si le formulaire n'est pas valide
        if (form instanceof ValidatableForm) {
            ((ValidatableForm) form).addValidationListener(valid -> {
                Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
                okButton.setDisable(!valid);
            });
        }

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
//                    form.clear();
                    return form.createEntity();
                } catch (IllegalArgumentException e) {
                    showValidationError(e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de validation");
        alert.setHeaderText("Données invalides");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Interfaces pour le typage générique
    public interface EntityForm<T> {
        javafx.scene.Node getContent();

        T createEntity() throws IllegalArgumentException;

//        void clear();
    }

    private interface ValidatableForm {
        void addValidationListener(java.util.function.Consumer<Boolean> listener);
    }
}