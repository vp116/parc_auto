package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.models.Fonction;
import com.angbe.soro.parc_auto.models.Personnel;
import com.angbe.soro.parc_auto.models.Service;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.FonctionService;
import com.angbe.soro.parc_auto.services.ServiceService;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddPersonnelForm extends GridPane implements DialogLauncher.EntityForm<Personnel> {
    private final FonctionService fonctionService = new FonctionService();
    private final ServiceService serviceService = new ServiceService();
    private final VehiculeService vehiculeService = new VehiculeService();
    private Personnel personnelEnEdition;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField contactField;
    @FXML
    private ComboBox<Fonction> fonctionCombo;
    @FXML
    private ComboBox<Service> serviceCombo;
    @FXML
    private ComboBox<Vehicule> vehiculeCombo;

    public AddPersonnelForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/add_personnel_form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            configureCombos();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void configureCombos() {
        // Configuration ComboBox Fonction
        fonctionCombo.setItems(FXCollections.observableArrayList(fonctionService.getAllFonctions()));
        fonctionCombo.setConverter(new StringConverter<Fonction>() {
            @Override
            public String toString(Fonction fonction) {
                return fonction != null ? fonction.getLibelleFonction() : "";
            }

            @Override
            public Fonction fromString(String string) {
                return null;
            }
        });

        // Configuration ComboBox Service
        serviceCombo.setItems(FXCollections.observableArrayList(serviceService.getAllServices()));
        serviceCombo.setConverter(new StringConverter<Service>() {
            @Override
            public String toString(Service service) {
                return service != null ? service.getLibelle() : "";
            }

            @Override
            public Service fromString(String string) {
                return null;
            }
        });

        // Configuration ComboBox Véhicule
        vehiculeCombo.setItems(FXCollections.observableArrayList(vehiculeService.getAllVehicules()));
        vehiculeCombo.setConverter(new StringConverter<Vehicule>() {
            @Override
            public String toString(Vehicule vehicule) {
                return vehicule != null
                        ? String.format("%s %s (%s)", vehicule.getMarque(), vehicule.getModele(), vehicule.getImmatriculation())
                        : "";
            }

            @Override
            public Vehicule fromString(String string) {
                return null;
            }
        });
    }

    public Personnel createPersonnelFromFields() {
        Personnel personnel = (personnelEnEdition != null) ? personnelEnEdition : new Personnel();

        // Validation des champs obligatoires
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (fonctionCombo.getValue() == null) {
            throw new IllegalArgumentException("La fonction est obligatoire");
        }
        if (serviceCombo.getValue() == null) {
            throw new IllegalArgumentException("Le service est obligatoire");
        }

        personnel.setNom(nomField.getText().trim());
        personnel.setPrenom(prenomField.getText().trim());
        personnel.setContact(contactField.getText().trim());
        personnel.setFonction(fonctionCombo.getValue());
        personnel.setService(serviceCombo.getValue());

        // Véhicule est optionnel
        if (vehiculeCombo.getValue() != null) {
            personnel.setVehicule(vehiculeCombo.getValue());
            if (personnel.getVehicule() == null) {
                personnel.setDateAttribution(new Date());

            }
        }

        return personnel;
    }

    public void clearForm() {
        this.personnelEnEdition = null;
        nomField.clear();
        prenomField.clear();
        contactField.clear();
        fonctionCombo.getSelectionModel().clearSelection();
        serviceCombo.getSelectionModel().clearSelection();
        vehiculeCombo.getSelectionModel().clearSelection();
    }

    public void setPersonnelData(Personnel personnel) {
        this.personnelEnEdition = personnel;
        nomField.setText(personnel.getNom());
        prenomField.setText(personnel.getPrenom());
        contactField.setText(personnel.getContact());
        fonctionCombo.setValue(personnel.getFonction());
        serviceCombo.setValue(personnel.getService());
        vehiculeCombo.setValue(personnel.getVehicule());

    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public Personnel createEntity() throws IllegalArgumentException {
        return createPersonnelFromFields();
    }

}
