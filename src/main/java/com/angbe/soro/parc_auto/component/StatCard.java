package com.angbe.soro.parc_auto.component;

import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import com.cardosama.fontawesome_fx_6.FontAwesomeType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Composant StatCard - Une carte statistique réutilisable pour afficher des indicateurs clés.
 * <p>
 * Ce composant affiche:
 * - Un titre
 * - Une valeur principale
 * - Une icône illustrative
 * - Une tendance (optionnelle) avec icône et texte
 * <p>
 * Tous les éléments sont personnalisables (couleurs, tailles, icônes).
 */
public class StatCard extends Pane {

    // Éléments UI
    private VBox card;
    private Label titleLabel;
    private Label valueLabel;
    private FontAwesomeIconView icon;
    private Circle iconBackground;
    private HBox trendContainer;
    private FontAwesomeIconView trendIcon;
    private Label trendLabel;

    /**
     * Constructeur par défaut avec des valeurs de base.
     */
    public StatCard() {
        this("Titre", "0", "circleInfo", Color.rgb(219, 234, 254));
    }

    /**
     * Constructeur avec paramètres principaux.
     *
     * @param title       Texte du titre
     * @param value       Valeur principale à afficher
     * @param iconName    Nom de l'icône FontAwesome (solid)
     * @param iconBgColor Couleur de fond de l'icône
     */
    public StatCard(String title, String value, String iconName, Color iconBgColor) {
        initializeCard();
        setTitle(title);
        setValue(value);
        setIcon(iconName, iconBgColor);
        setTrend("arrowUp", "0%", Color.web("#10b981"));
        setPrefHeight(260.0);
        setPrefWidth(360.0);
    }

    /**
     * Initialise la structure de base de la carte.
     */
    private void initializeCard() {
        // Conteneur principal
        card = new VBox();
        card.getStyleClass().add("stat-card");
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setPadding(new Insets(20));
        card.setSpacing(10);

        // Conteneur supérieur (titre, valeur, icône)
        HBox topContainer = new HBox();
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setSpacing(15);

        // Conteneur texte (titre + valeur)
        VBox textContainer = new VBox();
        textContainer.setSpacing(5);

        titleLabel = new Label();
        titleLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 14px;");

        valueLabel = new Label();
        valueLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 32px; -fx-font-weight: bold;");

        textContainer.getChildren().addAll(titleLabel, valueLabel);

        // Conteneur icône
        iconBackground = new Circle(24);
        icon = new FontAwesomeIconView();
        icon.setSize(20);

        Pane iconContainer = new Pane();
        iconContainer.getChildren().addAll(iconBackground, icon);
        iconBackground.centerXProperty().bind(iconContainer.widthProperty().divide(2));
        iconBackground.centerYProperty().bind(iconContainer.heightProperty().divide(2));
        icon.setLayoutX(14.5);
        icon.setLayoutY(43.0);


        topContainer.getChildren().addAll(textContainer, iconContainer);

        // Conteneur tendance
        trendContainer = new HBox();
        trendContainer.setSpacing(5.0);
        trendContainer.setAlignment(Pos.CENTER_LEFT);

        trendIcon = new FontAwesomeIconView();
        trendIcon.setSize(12.0);

        trendLabel = new Label();
        trendLabel.setStyle("-fx-font-size: 12px;");

        trendContainer.getChildren().addAll(trendIcon, trendLabel);

        card.getChildren().addAll(topContainer, trendContainer);
        this.getChildren().add(card);
    }

    // Méthodes de configuration

    public String getTitle() {
        return titleLabel.getText();
    }

    /**
     * Définit le titre de la carte.
     *
     * @param title Texte du titre
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public String getValue() {

        return valueLabel.getText();
    }

    /**
     * Définit la valeur principale de la carte.
     *
     * @param value Valeur à afficher
     */
    public void setValue(String value) {
        valueLabel.setText(value);
    }

    /**
     * Définit l'icône principale.
     *
     * @param iconName Nom de l'icône FontAwesome
     * @param bgColor  Couleur de fond de l'icône
     */
    public void setIcon(String iconName, Color bgColor) {
        icon.setIconName(iconName);
        icon.setFill(bgColor);
        iconBackground.setFill(bgColor);
    }

    public String getIconName() {
        return icon.getIconName();
    }

    public void setIconName(String iconName) {
        icon.setIconName(iconName);
    }

    public Paint getIconColor() {
        return icon.getColor();
    }

    public void setIconColor(Paint color) {
        icon.setColor(color);
    }

    public Paint getIconBgColor() {
        return iconBackground.getFill();
    }

    public void setIconBgColor(Paint bgColor) {
        iconBackground.setFill(bgColor);
    }

    public void setTrendText(String trend) {
        trendLabel.setText(trend);
    }

    public String getTrendText() {
        return trendLabel.getText();
    }

    public void setTrendIconName(String trendIconName){
        trendIcon.setIconName(trendIconName);
    }
    public String getTrendIconName() {
        return trendIcon.getIconName();
    }

    public void setTrendColor(Paint paint) {
        trendIcon.setColor(paint);

    }
    public Paint getTrendColor() {
      return trendIcon.getColor();
    }

    /**
     * Définit les informations de tendance.
     *
     * @param trendIconName Nom de l'icône de tendance
     * @param trendText     Texte de tendance
     * @param color         Couleur de la tendance (positive/négative)
     */
    public void setTrend(String trendIconName, String trendText, Color color) {
        trendIcon.setIconName(trendIconName);
        trendIcon.setType(FontAwesomeType.SOLID);
        trendIcon.setColor(color);
        trendLabel.setText(trendText);
        trendLabel.setTextFill(color);
    }

    /**
     * Active ou désactive l'affichage de la tendance.
     *
     * @param visible true pour afficher, false pour cacher
     */
    public void setTrendVisible(boolean visible) {
        trendContainer.setVisible(visible);
    }

    public boolean getTrendVisible() {
        return trendContainer.isVisible();
    }

    public void setTrendLabelColor(Paint color) {
        trendLabel.setTextFill(color);
    }
    public Paint getTrendLabelColor() {
        return trendLabel.getTextFill();
    }

    /**
     * Définit la couleur du texte de la valeur principale.
     *
     * @param color Couleur à appliquer
     */
    public void setValueColor(Color color) {
        valueLabel.setTextFill(color);
    }

    /**
     * Définit la taille de la police de la valeur principale.
     *
     * @param size Taille en points
     */
    public void setValueFontSize(double size) {
        valueLabel.setStyle("-fx-text-fill: " + (valueLabel.getTextFill()) +
                "; -fx-font-size: " + size + "px; -fx-font-weight: bold;");
    }
}