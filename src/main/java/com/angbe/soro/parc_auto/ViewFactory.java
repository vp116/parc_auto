package com.angbe.soro.parc_auto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ViewFactory {
    private static final Logger logger = Logger.getLogger(ViewFactory.class.getName());
    private static final Map<String, String> VIEW_MAP = new HashMap<>();

    static {
        // Associez chaque vue à son fichier FXML
        VIEW_MAP.put("dashboard", "dashboard-content.fxml");
        VIEW_MAP.put("vehicles", "vehicles-view.fxml");
        VIEW_MAP.put("missions", "missions-view.fxml");
        VIEW_MAP.put("maintenance", "maintenance-view.fxml");
        VIEW_MAP.put("insurances", "insurances-view.fxml");
        VIEW_MAP.put("staff", "staff-view.fxml");
        VIEW_MAP.put("reports", "reports-view.fxml");
    }

    public static Node getView(String viewName) {
        String fxmlPath = VIEW_MAP.get(viewName);
        if (fxmlPath == null) {
            logger.log(Level.SEVERE, "View {0} not found in view map", viewName);
            return null;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("views/" + fxmlPath));
            return loader.load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading view: " + viewName, e);
            return null;
        }
    }

    public static void loadFxml(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre", Alert.AlertType.ERROR);
        }
    }

    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void ShowEchecEnregAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Échec de l'enregistrement");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
