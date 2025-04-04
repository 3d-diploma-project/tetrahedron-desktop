package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.utils.ResourceReader;

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
                    HBox container = new HBox();
                    Label languageText = new Label(item);
                    Pane pane = new Pane();
                    HBox.setHgrow(pane, Priority.ALWAYS);
                    container.setAlignment(Pos.CENTER_LEFT);

                    SVGPath checkMark = ResourceReader.readComponent("/icon/Checkmark.fxml", SVGPath.class);
                    HBox.setMargin(checkMark, new Insets(0, 5, 0, 0));
                    checkMark.setVisible(languageSelector.getValue().equals(item));

                    container.getChildren().addAll(languageText, pane, checkMark);
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
