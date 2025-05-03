package com.angbe.soro.parc_auto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

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
}
