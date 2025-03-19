package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.controller.LocalizationController;

import java.util.Locale;
import java.util.prefs.Preferences;

public class Navbar {

    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    public void initialize() {
        languageSelector.setValue("UA");

        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();

            Locale newLocale = selectedLanguage.equals("EN") ? new Locale("en") : new Locale("uk");
            LocalizationController.getInstance().setLocale(newLocale);
        });
    }
}
