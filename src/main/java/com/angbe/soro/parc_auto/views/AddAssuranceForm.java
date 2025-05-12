package com.angbe.soro.parc_auto.views;

import com.angbe.soro.parc_auto.MainApplication;
import com.angbe.soro.parc_auto.components.DialogLauncher;
import com.angbe.soro.parc_auto.models.Assurance;
import com.angbe.soro.parc_auto.models.Couvrir;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAssuranceForm extends GridPane implements DialogLauncher.EntityForm<Assurance> {
    private Assurance assuranceEnEdition;
    
    @FXML
    private ComboBox<Vehicule> vehicleCombo;
    
    @FXML
    private TextField agenceField;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private TextField coutField;
    
    @FXML
    private TextField numCarteField;
    
    private final VehiculeService vehiculeService = new VehiculeService();
    
    public AddAssuranceForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/add_assurance_form.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
            configureVehicleCombo();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void configureVehicleCombo() {
        ObservableList<Vehicule> vehicules = FXCollections.observableArrayList(vehiculeService.getAllVehicules());
        vehicleCombo.setItems(vehicules);
        vehicleCombo.setConverter(new StringConverter<Vehicule>() {
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
    
    public void setAssuranceData(Assurance assurance) {
        this.assuranceEnEdition = assurance;
        
        // Configurer le numéro de carte
        numCarteField.setText(String.valueOf(assurance.getNumCarteAssurance()));
        
        // Configurer l'agence
        agenceField.setText(assurance.getAgence());
        
        // Configurer les dates
        if (assurance.getDateDebutAssurance() != null) {
            LocalDate startDate = assurance.getDateDebutAssurance().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            startDatePicker.setValue(startDate);
        }
        
        if (assurance.getDateFinAssurance() != null) {
            LocalDate endDate = assurance.getDateFinAssurance().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            endDatePicker.setValue(endDate);
        }
        
        // Configurer le coût
        if (assurance.getCoutAssurance() != null) {
            coutField.setText(String.valueOf(assurance.getCoutAssurance()));
        }
        
        // Configurer le véhicule si disponible
        if (assurance.getCouvertures() != null && !assurance.getCouvertures().isEmpty()) {
            Vehicule vehicule = assurance.getCouvertures().iterator().next().getVehicule();
            vehicleCombo.setValue(vehicule);
        }
    }
    
    public void clearForm() {
        this.assuranceEnEdition = null;
        numCarteField.clear();
        agenceField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        coutField.clear();
        vehicleCombo.getSelectionModel().clearSelection();
    }
    
    public Assurance createAssuranceFromField() {
        Assurance assurance = (assuranceEnEdition != null) ? assuranceEnEdition : new Assurance();
        
        // Validation des champs obligatoires
        if (numCarteField.getText().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de carte d'assurance est obligatoire");
        }
        if (agenceField.getText().isEmpty()) {
            throw new IllegalArgumentException("L'agence d'assurance est obligatoire");
        }
        if (startDatePicker.getValue() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        if (endDatePicker.getValue() == null) {
            throw new IllegalArgumentException("La date de fin est obligatoire");
        }
        if (vehicleCombo.getValue() == null) {
            throw new IllegalArgumentException("Un véhicule doit être sélectionné");
        }
        
        // Numéro de carte
        try {
            assurance.setNumCarteAssurance(Integer.parseInt(numCarteField.getText()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le numéro de carte doit être un nombre entier");
        }
        
        // Agence
        assurance.setAgence(agenceField.getText());
        
        // Dates
        LocalDate startDate = startDatePicker.getValue();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        assurance.setDateDebutAssurance(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        
        LocalDate endDate = endDatePicker.getValue();
        LocalDateTime endDateTime = endDate.atStartOfDay();
        assurance.setDateFinAssurance(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        
        // Coût
        try {
            if (!coutField.getText().isEmpty()) {
                assurance.setCoutAssurance(Double.parseDouble(coutField.getText()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le coût doit être un nombre valide");
        }
        
        // Véhicule et relation Couvrir
        Vehicule vehicule = vehicleCombo.getValue();
        
        // Initialiser la collection si nécessaire
        if (assurance.getCouvertures() == null) {
            assurance.setCouvertures(new HashSet<>());
        } else {
            assurance.getCouvertures().clear();
        }
        
        // Ajouter la relation Couvrir
        Couvrir couvrir = new Couvrir(vehicule, assurance);
        assurance.getCouvertures().add(couvrir);
        
        return assurance;
    }
    
    @Override
    public Node getContent() {
        return this;
    }
    
    @Override
    public Assurance createEntity() throws IllegalArgumentException {
        return createAssuranceFromField();
    }
}