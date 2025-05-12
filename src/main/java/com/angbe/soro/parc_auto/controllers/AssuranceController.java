package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.models.Assurance;
import com.angbe.soro.parc_auto.models.Couvrir;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.AssuranceService;
import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.angbe.soro.parc_auto.ViewFactory.ShowEchecEnregAlert;
import static com.angbe.soro.parc_auto.ViewFactory.showSuccessAlert;

public class AssuranceController implements Initializable {

    @FXML
    public ScrollPane AssuranceScrollPane;
    @FXML
    public VBox AssuranceContainer;

    private final AssuranceService assuranceService = new AssuranceService();
    private DynamicTableCard<Assurance> assuranceTableCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAssurances();
    }

    private void loadAssurances() {
        List<Assurance> assurances = assuranceService.getAllAssurances();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat formatterNbr = NumberFormat.getInstance(Locale.FRANCE);

        var btnExport = new Button("", new FontAwesomeIconView("fileExport"));
        btnExport.getStyleClass().add("btn-actions");
        var btnPrint = new Button("", new FontAwesomeIconView("print"));
        btnPrint.getStyleClass().add("btn-actions");

        assuranceTableCard = new DynamicTableCard<>("Liste des assurances", null, null, List.of(btnExport, btnPrint), true);
        assuranceTableCard.setItems(FXCollections.observableArrayList(assurances));

        // Correction: Utiliser la relation Couvrir pour accéder au véhicule
        assuranceTableCard.addCustomColumn("Véhicule", a -> {
            if (a.getCouvertures() != null && !a.getCouvertures().isEmpty()) {
                Vehicule v = a.getCouvertures().iterator().next().getVehicule();
                return v.getImmatriculation() + " (" + v.getMarque() + " " + v.getModele() + ")";
            }
            return "Non assigné";
        });
        
        // Correction: Utiliser agence au lieu de compagnie
        assuranceTableCard.addPropertyColumn("Compagnie", "agence");
        
        // Correction: Utiliser les noms corrects des attributs
        assuranceTableCard.addCustomColumn("Date début", a -> formatter.format(a.getDateDebutAssurance()));
        assuranceTableCard.addCustomColumn("Date fin", a -> formatter.format(a.getDateFinAssurance()));
        assuranceTableCard.addCustomColumn("Coût", a -> formatterNbr.format(a.getCoutAssurance()) + " FCFA");
        
        assuranceTableCard.addColumn(DynamicTableCard.createBadgeColumn("Statut", a -> {
            // Vérifier si l'assurance est expirée
            return a.getDateFinAssurance().before(new java.util.Date()) ? "Expirée" : "Valide";
        }, status -> {
            return status.equals("Expirée") ? "badge-red" : "badge-green";
        }));
        
        assuranceTableCard.addColumn(DynamicTableCard.createActionsColumn("Actions", this::viewAssurance, this::updateAssurance, this::deleteAssurance));

        AssuranceScrollPane.setContent(assuranceTableCard);
    }

    @FXML
    public void handleAddAssurance(ActionEvent actionEvent) {
        var formResult = DialogLauncher.showAddAssuranceDialog();
        formResult.ifPresent(assurance -> {
            try {
                assuranceService.saveAssurance(assurance);
                refreshAssuranceTable();
                showSuccessAlert("Assurance enregistrée avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        });
    }

    private void viewAssurance(Assurance assurance) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'assurance");
        
        // Correction: Accéder au véhicule via la relation Couvrir
        String vehiculeInfo = "Non assigné";
        if (assurance.getCouvertures() != null && !assurance.getCouvertures().isEmpty()) {
            Vehicule v = assurance.getCouvertures().iterator().next().getVehicule();
            vehiculeInfo = v.getImmatriculation() + " (" + v.getMarque() + ")";
            alert.setHeaderText("Informations sur l'assurance du véhicule " + v.getImmatriculation());
        } else {
            alert.setHeaderText("Informations sur l'assurance");
        }

        String content = "ID: " + assurance.getNumCarteAssurance() + "\n" +
                "Véhicule: " + vehiculeInfo + "\n" +
                "Compagnie: " + assurance.getAgence() + "\n" +
                "Date début: " + new SimpleDateFormat("dd/MM/yyyy").format(assurance.getDateDebutAssurance()) + "\n" +
                "Date fin: " + new SimpleDateFormat("dd/MM/yyyy").format(assurance.getDateFinAssurance()) + "\n" +
                "Coût: " + NumberFormat.getInstance(Locale.FRANCE).format(assurance.getCoutAssurance()) + " FCFA\n" +
                "Statut: " + (assurance.getDateFinAssurance().before(new java.util.Date()) ? "Expirée" : "Valide");

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateAssurance(Assurance assurance) {
        var formResult = DialogLauncher.showEditAssuranceDialog(assurance);
        formResult.ifPresent(a -> {
            try {
                // Correction: Utiliser saveAssurance au lieu de updateAssurance
                assuranceService.saveAssurance(a);
                refreshAssuranceTable();
                showSuccessAlert("Assurance mise à jour avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        });
    }

    private void deleteAssurance(Assurance assurance) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'assurance");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette assurance ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Correction: Utiliser numCarteAssurance au lieu de idAssurance
                assuranceService.deleteAssurance(assurance.getNumCarteAssurance());
                refreshAssuranceTable();
                showSuccessAlert("Assurance supprimée avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        }
    }

    private void refreshAssuranceTable() {
        assuranceTableCard.getTableView().getItems().setAll(assuranceService.getAllAssurances());
    }
}
