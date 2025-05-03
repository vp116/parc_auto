package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    public AnchorPane sidebar;
    @FXML
    public StackPane contentPane;
    @FXML
    public AnchorPane dashboardContent;

    @FXML
    public void showDashboard(ActionEvent actionEvent) {
        loadView("dashboard");
    }

    @FXML
    public void showVehicles(ActionEvent actionEvent) {
        loadView("vehicles");
    }

    @FXML
    public void showMissions(ActionEvent actionEvent) {
        loadView("missions");
    }

    @FXML
    public void showMaintenance(ActionEvent actionEvent) {
        loadView("maintenance");
    }

    @FXML
    public void showInsurances(ActionEvent actionEvent) {
        loadView("insurances");
    }

    @FXML
    public void showStaff(ActionEvent actionEvent) {
        loadView("staff");
    }

    @FXML
    public void showReports(ActionEvent actionEvent) {
        loadView("reports");
    }

    private void loadView(String viewName) {
        Node view = ViewFactory.getView(viewName);
        if (view != null) {
            contentPane.getChildren().setAll(view);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDashboard(null);
    }
}