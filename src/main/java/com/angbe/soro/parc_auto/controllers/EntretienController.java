package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.components.EntretienFilter;
import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.services.EntretienService;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.angbe.soro.parc_auto.ViewFactory.ShowEchecEnregAlert;
import static com.angbe.soro.parc_auto.ViewFactory.showSuccessAlert;

public class EntretienController implements Initializable {
    public final EntretienService entretienService = new EntretienService();
    @FXML
    public Button addEntretienBtn;
    @FXML
    public EntretienFilter entretienFilter;
    @FXML
    public ScrollPane entretienScrollPane;
    @FXML
    public VBox entretienContainer;


    public DynamicTableCard<Entretien> entretienTableCard;


    private void loadEntretiens() {
        List<Entretien> entretiens = entretienService.getAllEntretiens();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat formatterNbr = NumberFormat.getInstance(Locale.FRANCE);

        var btnExport = new Button("", new FontAwesomeIconView("fileExport"));
        btnExport.getStyleClass().add("btn-actions");
        var btnPrint = new Button("", new FontAwesomeIconView("print"));
        btnPrint.getStyleClass().add("btn-actions");

        entretienTableCard = new DynamicTableCard<>("liste des entretiens", null, null, List.of(btnExport, btnPrint), true);
        entretienTableCard.setItems(FXCollections.observableArrayList(entretiens));
        entretienTableCard.addCustomColumn("Véhicule", e -> e.getVehicule().getImmatriculation() + "(" + e.getVehicule().getMarque() + ")");
        entretienTableCard.addPropertyColumn("Motif", "motif");
        entretienTableCard.addCustomColumn("Date entrée", entretien -> formatter.format(entretien.getDateEntree()));
        entretienTableCard.addCustomColumn("Date sortie", entretien -> formatter.format(entretien.getDateSortie()));
        entretienTableCard.addPropertyColumn("Lieu", "lieu");
        entretienTableCard.addCustomColumn("Coût", e -> formatterNbr.format(e.getCout()) + " FCFA");
        entretienTableCard.addCustomColumn("Statut", e -> e.getVehicule().getEtatVoiture().getLibelleEtat());
        // Remplacer la ligne 68 par :
        entretienTableCard.addColumn(DynamicTableCard.createActionsColumn("Actions", this::viewEntretien, this::updateEntretien, this::deleteEntretien));


        entretienScrollPane.setContent(entretienTableCard);
    }

    // Ajouter ces méthodes :
    private void viewEntretien(Entretien entretien) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'entretien");
        alert.setHeaderText("Informations sur l'entretien du véhicule " + entretien.getVehicule().getImmatriculation());

        String content = "ID: " + entretien.getIdEntretien() + "\n" +
                "Véhicule: " + entretien.getVehicule().getImmatriculation() + " (" + entretien.getVehicule().getMarque() + ")\n" +
                "Motif: " + entretien.getMotif() + "\n" +
                "Date entrée: " + new SimpleDateFormat("dd/MM/yyyy").format(entretien.getDateEntree()) + "\n" +
                "Date sortie: " + new SimpleDateFormat("dd/MM/yyyy").format(entretien.getDateSortie()) + "\n" +
                "Lieu: " + entretien.getLieu() + "\n" +
                "Coût: " + NumberFormat.getInstance(Locale.FRANCE).format(entretien.getCout()) + " FCFA\n" +
                "Statut: " + entretien.getVehicule().getEtatVoiture().getLibelleEtat();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateEntretien(Entretien entretien) {
        var formResult = DialogLauncher.showEditEntretienDialog(entretien);
        formResult.ifPresent(e -> {
            try {
                entretienService.updateEntretien(e);
                refreshEntretienTable();
                showSuccessAlert("Entretien mis à jour avec succès!");
            } catch (Exception ex) {
                ShowEchecEnregAlert(ex);
            }
        });
    }

    private void deleteEntretien(Entretien entretien) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'entretien");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet entretien ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                entretienService.deleteEntretien(entretien.getIdEntretien());
                refreshEntretienTable();
                showSuccessAlert("Entretien supprimé avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        }
    }

    @FXML
    public void handleAddEntretien() {
        var formResult = DialogLauncher.showAddEntretienDialog();
        formResult.ifPresent(entretien -> {
            try {
                entretienService.planifierEntretien(entretien);
                refreshEntretienTable();
                showSuccessAlert("Entretien planifié avec succes!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);

            }
        });
    }

    private void refreshEntretienTable() {
        entretienTableCard.getTableView().getItems().setAll(entretienService.getAllEntretiens());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEntretiens();
    }
}
