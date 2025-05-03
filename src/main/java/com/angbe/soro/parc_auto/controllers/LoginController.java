package com.angbe.soro.parc_auto.controllers;
import com.angbe.soro.parc_auto.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

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

    // Méthode d'initialisation
    @FXML
    public void initialize() {
        // Permettre la connexion avec la touche ENTER
        passwordField.setOnKeyPressed(this::handleKeyPress);

        // Configurer les actions des boutons
        loginButton.setOnAction(event -> handleLogin());
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
        createAccountLink.setOnAction(event -> handleCreateAccount());
    }

    // Gestion de la connexion
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation simple
        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Format d'email invalide");
            return;
        }

        // Ici, vous implémenteriez la logique réelle de connexion
        boolean loginSuccess = authenticate(email, password);

        if (loginSuccess) {
            // Si "Se souvenir de moi" est coché, sauvegarder les infos
            if (rememberCheckBox.isSelected()) {
                saveCredentials(email, password);
            }

            // Ouvrir la fenêtre principale
            openMainWindow();

            // Fermer la fenêtre de login
            ((Stage) rootPane.getScene().getWindow()).close();
        } else {
            showError("Email ou mot de passe incorrect");
        }
    }

    // Gestion du mot de passe oublié
    private void handleForgotPassword() {
        System.out.println("Mot de passe oublié cliqué");
        // Ici vous pourriez ouvrir une nouvelle fenêtre ou changer de scène
    }

    // Gestion de la création de compte
    private void handleCreateAccount() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sign-in-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Inscription");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permettre la connexion avec la touche ENTER
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    // Méthodes utilitaires
    private boolean isValidEmail(String email) {
        // Validation simple d'email - à améliorer selon les besoins
        return email.contains("@") && email.contains(".");
    }

    private boolean authenticate(String email, String password) {
        // Ici, vous implémenteriez la logique réelle d'authentification
        // Pour l'exemple, nous utilisons des valeurs fixes
        return "admin@example.com".equals(email) && "password123".equals(password);
    }

    private void saveCredentials(String email, String password) {
        // Ici, vous pourriez sauvegarder les identifiants dans les préférences
        System.out.println("Sauvegarde des identifiants pour " + email);
    }

    private void openMainWindow() {
        System.out.println("Ouverture de la fenêtre principale...");
        // Ici vous chargeriez la fenêtre principale de l'application
    }

    private void showError(String message) {
        // Ici vous pourriez afficher un message d'erreur dans l'interface
        System.out.println("Erreur: " + message);
        // Exemple avec une alerte (à implémenter)
        // Alert alert = new Alert(Alert.AlertType.ERROR);
        // alert.setTitle("Erreur de connexion");
        // alert.setHeaderText(null);
        // alert.setContentText(message);
        // alert.showAndWait();
    }
}




