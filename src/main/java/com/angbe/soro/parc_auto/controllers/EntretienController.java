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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
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
        entretienTableCard.addColumn(DynamicTableCard.createBadgeColumn("Statut", e -> e.getVehicule().getEtatVoiture().getLibelleEtat(), libelle -> {
            return switch (libelle) {
                case "Disponible" -> "badge-red";
                case "En entretien" -> "badge-yellow";
                default -> "status-default";
            };

        }));
        entretienTableCard.addColumn(DynamicTableCard.createActionsColumn("Actions", null, null, null));
        entretienScrollPane.setContent(entretienTableCard);
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
