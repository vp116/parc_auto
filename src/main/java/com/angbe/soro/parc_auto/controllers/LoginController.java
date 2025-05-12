package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.models.Utilisateur;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.angbe.soro.parc_auto.ViewFactory.loadFxml;
import static com.angbe.soro.parc_auto.ViewFactory.showAlert;

public class LoginController implements Initializable {
    private AuthenticationService authService;
    @FXML
    private VBox rootPane;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberCheckBox;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink forgotPasswordLink;
    @FXML
    private Hyperlink createAccountLink;

    public LoginController() {
    }

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });


    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "Format d'email invalide", Alert.AlertType.ERROR);
            return;
        }

        try {
            if (authService.authentifier(email, password)) {
                if (rememberCheckBox.isSelected()) {
                    // TODO: Implémenter la sauvegarde des identifiants

                    Utilisateur utilisateurConnecter = authService.getUtilisateurConnecte();
                    email = utilisateurConnecter.getEmail();
                    password = utilisateurConnecter.getPassword();
                }
                openMainWindow();
                closeCurrentWindow();
            } else {
                showAlert("Erreur", "Email ou mot de passe incorrect", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la connexion: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleForgotPassword() {
        // TODO: Implémenter la récupération de mot de passe
        showAlert("Info", "Fonctionnalité en cours de développement", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleCreateAccount() {
        loadFxml("views/sign-in-view.fxml", "Inscription");
        closeCurrentWindow();
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void openMainWindow() {
        // TODO: Implémenter l'ouverture de la fenêtre principale
        loadFxml("views/dashboard-view.fxml", "DashBoard");
    }

    private void closeCurrentWindow() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            AppConfig.initialize();
            authService = new AuthenticationService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




