package com.angbe.soro.parc_auto.component;

import com.angbe.soro.parc_auto.MainApplication;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Badge extends StackPane {
    private static final Random RANDOM = new Random();
    private static final List<String> ALL_STYLES = Arrays.asList(
            "badge-yellow", "badge-red", "badge-green",
            "badge-blue", "badge-gray", "badge-purple", "badge-pink"
    );
    @FXML
    private Label label;

    public Badge() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("components/badge.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }


    }

    public Badge(String content) {
        this();
        setContent(content);
        setRandomStyle();

    }

    public String getContent() {
        return textProperty().get();
    }

    public void setContent(String content) {
        textProperty().set(content);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }

    // Style methods
    public void setYellowStyle() {
        setStyleClass("badge-yellow");
    }

    public void setRedStyle() {
        setStyleClass("badge-red");
    }

    public void setGreenStyle() {
        setStyleClass("badge-green");
    }

    public void setBlueStyle() {
        setStyleClass("badge-blue");
    }

    public void setGrayStyle() {
        setStyleClass("badge-gray");
    }

    public void setPurpleStyle() {
        setStyleClass("badge-purple");
    }

    public void setPinkStyle() {
        setStyleClass("badge-pink");
    }

    private void setStyleClass(String style) {
        this.getStyleClass().removeAll(ALL_STYLES);
        this.getStyleClass().add(style);
    }

    public void setRandomStyle() {
        setStyleClass(ALL_STYLES.get(RANDOM.nextInt(ALL_STYLES.size())));
    }

    public void setConsistentRandomStyle(String seedText) {
        int colorIndex = Math.abs(seedText.hashCode()) % ALL_STYLES.size();
        setStyleClass(ALL_STYLES.get(colorIndex));
    }
}
