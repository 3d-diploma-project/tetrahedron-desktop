package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
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
    }

    @FXML
    public void changeLanguage(ActionEvent event) {
        String selectedLanguage = languageSelector.getValue();

        Locale newLocale = selectedLanguage.equals("EN") ? Locale.of("en") : Locale.of("uk");
        LocalizationController.getInstance().setLocale(newLocale);
    }
}
