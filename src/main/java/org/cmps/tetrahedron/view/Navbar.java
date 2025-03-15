package org.cmps.tetrahedron.view;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Navbar extends HBox {

    public Navbar() {
        getStyleClass().add("navbar");

        VBox logo = new Logo();
        logo.getStyleClass().add("logo");

        ComboBox<String> languageSelector = new ComboBox<>();

        languageSelector.getItems().add("EN");
        languageSelector.getItems().add("UA");
        languageSelector.getItems().add("DE");
        languageSelector.getItems().add("NL");
        languageSelector.setValue("UA");
        languageSelector.getStyleClass().add("language-selector");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(logo, spacer, languageSelector);
        setAlignment(Pos.CENTER_LEFT);
    }
}
