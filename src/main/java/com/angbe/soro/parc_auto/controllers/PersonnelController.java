package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.services.PersonnelService;
import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.angbe.soro.parc_auto.ViewFactory.ShowEchecEnregAlert;
import static com.angbe.soro.parc_auto.ViewFactory.showSuccessAlert;

public class PersonnelController implements Initializable {

    private final PersonnelService personnelService = new PersonnelService();
    @FXML
    public Button addPersonnelBtn;
    @FXML
    public ScrollPane personnelScrollPane;
    @FXML
    public VBox personnelContainer;
    public DynamicTableCard<Personnel> personnelTableCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPersonnels();
    }

    private void loadPersonnels() {
        List<Personnel> personnels = personnelService.getAllPersonnel();

        var btnExport = new Button("", new FontAwesomeIconView("fileExport"));
        btnExport.getStyleClass().add("btn-actions");
        var btnPrint = new Button("", new FontAwesomeIconView("print"));
        btnPrint.getStyleClass().add("btn-actions");

        personnelTableCard = new DynamicTableCard<>("Liste du personnel", null, null, List.of(btnExport, btnPrint), true);
        personnelTableCard.setItems(FXCollections.observableArrayList(personnels));
        personnelTableCard.addCustomColumn("Nom & Prénom", p -> p.getNom() + " " + p.getPrenom());
        personnelTableCard.addCustomColumn("Service", p -> p.getService().getLibelle());
        personnelTableCard.addCustomColumn("Fonction", p -> p.getFonction().getLibelleFonction());
        personnelTableCard.addCustomColumn("Contact", Personnel::getContact);
        personnelTableCard.addCustomColumn("Véhicule attribué", p ->
                p.getVehicule() != null ?
                        p.getVehicule().getMarque() + " " + p.getVehicule().getModele() + " (" + p.getVehicule().getImmatriculation() + ")" :
                        "Aucun"
        );
        personnelTableCard.addColumn(DynamicTableCard.createActionsColumn("Actions", this::viewPersonnel, this::updatePersonnel, this::deletePersonnel));
        personnelScrollPane.setContent(personnelTableCard);
    }

    private void viewPersonnel(Personnel personnel) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du personnel");
        alert.setHeaderText("Informations sur " + personnel.getNom() + " " + personnel.getPrenom());

        String content = "ID: " + personnel.getIdPersonnel() + "\n" +
                "Service: " + personnel.getService().getLibelle() + "\n" +
                "Fonction: " + personnel.getFonction().getLibelleFonction() + "\n" +
                "Contact: " + personnel.getContact() + "\n" +
                "Véhicule attribué: " + (personnel.getVehicule() != null ?
                personnel.getVehicule().getMarque() + " " + personnel.getVehicule().getModele() :
                "Aucun") + "\n" +
                "Date d'attribution: " + (personnel.getDateAttribution() != null ?
                personnel.getDateAttribution().toString() : "Non attribué");

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updatePersonnel(Personnel personnel) {
        var formResult = DialogLauncher.showEditPersonnelDialog(personnel);
        formResult.ifPresent(p -> {
            try {
                personnelService.updatePersonnel(p);
                refreshPersonnelTable();
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        });

    }


    private void deletePersonnel(Personnel personnel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le personnel");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer " + personnel.getNom() + " " + personnel.getPrenom() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                personnelService.deletePersonnel(personnel.getIdPersonnel());
                refreshPersonnelTable();
                showSuccessAlert("Personnel supprimé avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        }
    }

    @FXML
    public void handleAddMember() {
        var formResult = DialogLauncher.showAddPersonnelDialog();
        formResult.ifPresent(personnel -> {
            try {
                personnelService.savePersonnel(personnel);
                refreshPersonnelTable();
                showSuccessAlert("Personnel enregistré avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        });
    }

    private void refreshPersonnelTable() {
        personnelTableCard.getTableView().getItems().setAll(personnelService.getAllPersonnel());
    }
}