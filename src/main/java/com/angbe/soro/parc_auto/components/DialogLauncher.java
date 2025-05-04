package com.angbe.soro.parc_auto.components;

import com.angbe.soro.parc_auto.views.AddMissionForm;
import com.angbe.soro.parc_auto.models.Mission;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class DialogLauncher {

    public Mission showAddMissionDialog() {
        AddMissionForm content = new AddMissionForm();

        Dialog<Mission> dialog = new Dialog<>(); // Changé pour utiliser Mission comme type
        dialog.setTitle("Nouvelle mission");
        dialog.setHeaderText("Remplissez les informations de la mission");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Configurer le convertisseur de résultat
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return content.createMissionFromField();
            }
            return null;
        });

        // Afficher la dialog et retourner le résultat
        return dialog.showAndWait().orElse(null);
    }
}