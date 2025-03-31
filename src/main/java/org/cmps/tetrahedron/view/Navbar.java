package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.cmps.tetrahedron.controller.LocalizationController;

import java.io.IOException;
import java.util.Locale;
import java.util.prefs.Preferences;

public class Navbar {

    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    public void initialize() {
        languageSelector.setValue("UA");

        languageSelector.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(20);
                    Label languageText = new Label(item);

                    SVGPath checkMark = new SVGPath();
                    checkMark.setContent("M1,5 L3,7 L9,1");
                    checkMark.setFill(null);
                    checkMark.setStroke(Color.GREEN);
                    checkMark.setStrokeWidth(1.5);
                    checkMark.setVisible(item.equals(languageSelector.getValue()));

                    languageText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                    container.getChildren().addAll(languageText, checkMark);
                    container.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    public void changeLanguage(ActionEvent event) {
        String selectedLanguage = languageSelector.getValue();

        Locale newLocale = selectedLanguage.equals("EN") ? Locale.of("en") : Locale.of("uk");
        LocalizationController.getInstance().setLocale(newLocale);
    }
}
