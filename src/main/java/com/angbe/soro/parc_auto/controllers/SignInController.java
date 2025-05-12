package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.models.Utilisateur;
import com.angbe.soro.parc_auto.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

import static com.angbe.soro.parc_auto.ViewFactory.loadFxml;
import static com.angbe.soro.parc_auto.ViewFactory.showAlert;

public class SignInController {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private final AuthenticationService authService;
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

    public SignInController() {
        this.authService = new AuthenticationService();
    }

    @FXML
    public void initialize() {
        inscriptionButton.setOnAction(event -> handleInscription());
        loginLink.setOnAction(event -> handleLoginLink());
        setupFieldValidators();
    }

    private void handleInscription() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validateFields(nom, email, password, confirmPassword)) {
            return;
        }

        try {
            Utilisateur utilisateur = authService.inscrire(nom, "", email, password, null);
            if (utilisateur != null) {
                showAlert("Succès", "Inscription réussie ! Vous pouvez maintenant vous connecter.", Alert.AlertType.INFORMATION);
                openLoginWindow();
                closeCurrentWindow();
            }
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'inscription.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields(String nom, String email, String password, String confirmPassword) {
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires", Alert.AlertType.ERROR);
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "Format d'email invalide", Alert.AlertType.ERROR);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas", Alert.AlertType.ERROR);
            return false;
        }

        if (password.length() < 8) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 8 caractères", Alert.AlertType.ERROR);
            return false;
        }

        if (!conditionsCheckBox.isSelected()) {
            showAlert("Erreur", "Vous devez accepter les conditions d'utilisation", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void handleLoginLink() {
        openLoginWindow();
        closeCurrentWindow();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private void setupFieldValidators() {
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailField.setStyle(!newVal.isEmpty() && !isValidEmail(newVal) ?
                    "-fx-border-color: #e53e3e;" : "-fx-border-color: #e2e8f0;");
        });

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmPasswordField.setStyle(!newVal.isEmpty() && !newVal.equals(passwordField.getText()) ?
                    "-fx-border-color: #e53e3e;" : "-fx-border-color: #e2e8f0;");
        });
    }

    private void openLoginWindow() {
        loadFxml("views/login-view.fxml", "Connexion");

    }

    private void closeCurrentWindow() {
        ((Stage) inscriptionButton.getScene().getWindow()).close();
    }


}
