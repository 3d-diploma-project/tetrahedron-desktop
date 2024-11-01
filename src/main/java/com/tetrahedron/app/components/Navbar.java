package com.tetrahedron.app.components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Navbar extends HBox {

    public Navbar(Scene scene) {
        getStyleClass().add("navbar");

        VBox logo = new Logo();
        logo.getStyleClass().add("logo");

        Label languageSelector = new Label("EN");
        languageSelector.getStyleClass().add("language-selector");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(logo, spacer, languageSelector);
        setAlignment(Pos.CENTER_LEFT);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double logoWidth = logo.getBoundsInParent().getWidth();
            double selectorWidth = languageSelector.getBoundsInParent().getWidth();
            if (logoWidth > 0 && selectorWidth > 0) {
                double spacing = (newVal.doubleValue() - logoWidth - selectorWidth) / 2;
                setSpacing(spacing);
            }
        });
    }
}
