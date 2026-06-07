package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.enums.Language;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.awt.*;
import java.util.Locale;

public class Navbar {

    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    public void initialize() {
        Locale locale = LocalizationController.getInstance().getCurrentLocale();
        Language appLanguage = Language.getLanguage(locale);
        languageSelector.setValue(appLanguage.toString());

        languageSelector.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox();
                    Label languageText = new Label(item);
                    languageText.setStyle("-fx-text-fill: #0E0E0E;");

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
    public void changeLanguage() {
        Language selectedLanguage = Language.valueOf(languageSelector.getValue());
        LocalizationController.getInstance().setLocale(selectedLanguage.getLocale());

        Router.getInstance().reloadCurrentPage();
    }

    public void openInstructions() {
        String url = "https://github.com/NTU-KhPI-CMPS/tetrahedron-desktop/wiki";

        try {
            Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            System.out.println("Failed to open the browser: " + e.getMessage());
        }
    }
}
