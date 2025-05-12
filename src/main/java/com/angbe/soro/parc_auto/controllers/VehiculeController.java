package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.components.DynamicTableCard;
import com.angbe.soro.parc_auto.components.VehiculeFilter;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.VehiculeService;
import com.angbe.soro.parc_auto.utils.PrintExportUtils;
import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.angbe.soro.parc_auto.ViewFactory.ShowEchecEnregAlert;
import static com.angbe.soro.parc_auto.ViewFactory.showSuccessAlert;

public class VehiculeController implements Initializable, AutoCloseable {
    @FXML
    public AnchorPane root;
    @FXML
    public ScrollPane tableVehicule;
    @FXML
    public VehiculeFilter vehiculeFilter;
    private VehiculeService vehiculeService;
    private EntityManagerFactory emf;
    private EntityManager em;
    private DynamicTableCard<Vehicule> vehiculeCardTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            // Initialisation de l'EntityManager et AppConfig
            emf = Persistence.createEntityManagerFactory("bdGestionParcAuto");
            em = emf.createEntityManager();
            AppConfig.initialize(em);

            // Initialisation du service
            vehiculeService = new VehiculeService();

            // Affichage des véhicules dans la table
            loadVehicles();

        } catch (Exception e) {
            Logger.getLogger(VehiculeController.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    private void loadVehicles() {
        // Supposons que le service VehiculeService a une méthode pour récupérer les véhicules
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        var btnExport = new Button("", new FontAwesomeIconView("fileExport"));
        btnExport.getStyleClass().add("btn-actions");
        var btnPrint = new Button("", new FontAwesomeIconView("print"));
        btnPrint.getStyleClass().add("btn-actions");

        // Créer un DynamicTableCard pour afficher les véhicules
        vehiculeCardTable = new DynamicTableCard<>("Liste des véhicules", null, null, List.of(btnExport, btnPrint), true);
        vehiculeCardTable.setItems(FXCollections.observableArrayList(vehicules));
        vehiculeCardTable.addPropertyColumn("Immatriculation", "immatriculation");
        vehiculeCardTable.addCustomColumn("Marque/Modèle", v -> v.getMarque() + " " + v.getModele());
        vehiculeCardTable.addPropertyColumn("Énergie", "energie");
        vehiculeCardTable.addCustomColumn("Puissance", v -> v.getPuissance() + " Km/h");
        vehiculeCardTable.addColumn(DynamicTableCard.createBadgeColumn("État", vehicule -> vehicule.getEtatVoiture().getLibelleEtat(), libelle -> {
            return switch (libelle) {
                case "Disponible" -> "status-available";
                case "En mission" -> "status-mission";
                case "En entretien" -> "status-maintenance";
                case "Hors service" -> "status-outofservice";
                default -> "status-default";
            };

        }));
        vehiculeCardTable.addCustomColumn("Date acquisition", v -> formatter.format(v.getDateAcquisition()));
        vehiculeCardTable.addColumn(DynamicTableCard.createActionsColumn("Actions", this::viewVehicule, this::updateVehicule, this::deleteVehicule));
        tableVehicule.setContent(vehiculeCardTable);

        
// Dans votre méthode loadVehicles() après la création des boutons
btnExport.setOnAction(e -> PrintExportUtils.exportTableData(
    vehiculeCardTable,
    btnExport.getScene().getWindow(),
    "Exporter la liste des véhicules"
));

btnPrint.setOnAction(e -> PrintExportUtils.printTableData(
    vehiculeCardTable,
    btnPrint.getScene().getWindow(),
    "Liste des véhicules"
));

    }

    private void deleteVehicule(Vehicule v) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le vehicule");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                vehiculeService.deleteVehicule(v.getIdVehicule());
                refreshVehicleTable();
                showSuccessAlert("Vehicule supprimé avec succès!");
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        }
    }

    private void updateVehicule(Vehicule v) {
        var formResult = DialogLauncher.showEditVehiculeDialog(v);
        formResult.ifPresent(
                vehicule -> {
                    try {
                        vehiculeService.updateVehicule(vehicule);
                        refreshVehicleTable();
                    } catch (Exception e) {
                        ShowEchecEnregAlert(e);
                    }
                }
        );
    }

    private void viewVehicule(Vehicule v) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du véhicule");
        alert.setHeaderText("Informations sur " + v.getMarque() + " " + v.getModele() + " (" + v.getImmatriculation() + ")");
        
        String content = "ID: " + v.getIdVehicule() + "\n" +
                "Immatriculation: " + v.getImmatriculation() + "\n" +
                "Marque: " + v.getMarque() + "\n" +
                "Modèle: " + v.getModele() + "\n" +
                "Énergie: " + v.getEnergie() + "\n" +
                "Puissance: " + v.getPuissance() + " Km/h\n" +
                "État: " + v.getEtatVoiture().getLibelleEtat() + "\n" +
                "Date d'acquisition: " + new SimpleDateFormat("dd/MM/yyyy").format(v.getDateAcquisition());
        
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddVehicle() {

        var formResult = DialogLauncher.showAddVehiculeDialog();
        formResult.ifPresent(vehicule -> {
            try {
                vehiculeService.saveVehicule(vehicule);
                refreshVehicleTable();
            } catch (Exception e) {
                ShowEchecEnregAlert(e);
            }
        });


    }

    private void refreshVehicleTable() {
        vehiculeCardTable.getTableView().getItems().setAll(vehiculeService.getAllVehicules());
    }


    @Override
    public void close() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
