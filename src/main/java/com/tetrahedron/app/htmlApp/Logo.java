package com.tetrahedron.app.htmlApp;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Logo extends VBox {
    public Logo() {
        // TODO: create a separate class for reading image
        InputStream input = getClass().getResourceAsStream("/logo.png");
        if (input == null) {
            try {
                throw new FileNotFoundException("Файл logo.svg не найден в папке ресурсов");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Image image = new Image(input);
        ImageView logo = new ImageView(image);

        Label logoText = new Label("TETRAHEDRON");
        logoText.getStyleClass().add("logo-text");

        getChildren().addAll(logo, logoText);
        setAlignment(javafx.geometry.Pos.CENTER);
    }
}
