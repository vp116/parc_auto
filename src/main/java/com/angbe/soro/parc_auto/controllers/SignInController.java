package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class SignInController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private CheckBox conditionsCheckBox;
    @FXML
    private Button inscriptionButton;
    @FXML
    private Hyperlink loginLink;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @FXML
    public void initialize() {
        // Configurer les actions
        inscriptionButton.setOnAction(event -> handleInscription());
        loginLink.setOnAction(event -> handleLoginLink());

        // Validation en temps réel
        setupFieldValidators();
    }

    private void handleInscription() {
        // Récupération des valeurs
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "Format d'email invalide", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas", Alert.AlertType.ERROR);
            return;
        }

        if (password.length() < 8) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 8 caractères", Alert.AlertType.ERROR);
            return;
        }

        if (!conditionsCheckBox.isSelected()) {
            showAlert("Erreur", "Vous devez accepter les conditions d'utilisation", Alert.AlertType.ERROR);
            return;
        }

        // Enregistrement (ici simulé)
        boolean registrationSuccess = registerUser(nom, email, password);

        if (registrationSuccess) {
            showAlert("Succès", "Inscription réussie ! Vous pouvez maintenant vous connecter.", Alert.AlertType.INFORMATION);
            openLoginWindow();
            closeCurrentWindow();
        } else {
            showAlert("Erreur", "L'inscription a échoué. Veuillez réessayer.", Alert.AlertType.ERROR);
        }
    }

    private void handleLoginLink() {
        openLoginWindow();
        closeCurrentWindow();
    }

    private boolean registerUser(String nom, String email, String password) {
        // Ici, vous implémenteriez la logique réelle d'enregistrement
        // (Base de données, API, etc.)
        // Pour l'exemple, nous simulons un enregistrement réussi
        System.out.println("Enregistrement de l'utilisateur:");
        System.out.println("Nom: " + nom);
        System.out.println("Email: " + email);
        System.out.println("Mot de passe: " + password);

        return true; // Simule un succès
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private void setupFieldValidators() {
        // Validation en temps réel pour l'email
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !isValidEmail(newVal)) {
                emailField.setStyle("-fx-border-color: #e53e3e;");
            } else {
                emailField.setStyle("-fx-border-color: #e2e8f0;");
            }
        });

        // Validation en temps réel pour la confirmation du mot de passe
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.equals(passwordField.getText())) {
                confirmPasswordField.setStyle("-fx-border-color: #e53e3e;");
            } else {
                confirmPasswordField.setStyle("-fx-border-color: #e2e8f0;");
            }
        });
    }

    private void openLoginWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Connexion");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) inscriptionButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
