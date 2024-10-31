package com.tetrahedron.app.htmlApp;

import com.tetrahedron.app.utils.ResourceReader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Logo extends VBox {
    public Logo() {
        ImageView logo = ResourceReader.imageReader("/logo.png");

        Label logoText = new Label("TETRAHEDRON");
        logoText.getStyleClass().add("logo-text");

        getChildren().addAll(logo, logoText);
        setAlignment(javafx.geometry.Pos.CENTER);
    }
}
