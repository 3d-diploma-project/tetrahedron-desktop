package org.cmps.tetrahedron.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.i18n.LocalizationListener;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class WarningDialog {

    @FXML
    private Label errorTitle;
    @FXML
    private Label errorMessage;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    private Stage stage;
    private boolean result = false;

    public WarningDialog(String title, String message) {
        try {
            Locale.setDefault(LocalizationController.getInstance().getCurrentLocale());

            URL fxmlPath = getClass().getClassLoader().getResource("view/WarningDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath, ResourceBundle.getBundle(LocalizationController.WARNING_DIALOG_BUNDLE));

            loader.setController(this);
            Pane root = loader.load();

            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);

            errorTitle.setText(title);
            errorMessage.setText(message);

            if (yesButton != null) {
                yesButton.setOnAction(event -> {
                    result = true;
                    closeDialog();
                });
            }

            if (noButton != null) {
                noButton.setOnAction(event -> {
                    System.out.println("Натиснуто 'Ні', закриваємо лише WarningDialog");
                    result = false;
                    closeDialog();
                });
            }

        } catch (IOException e) {
            throw new RuntimeException("Помилка відкриття вікна попередження: " + e.getMessage(), e);
        }
    }

    public WarningDialog(String title, String message, String continueKey) {
        this(title, message + "\n\n" + continueKey);
    }

    @FXML
    private void closeDialog() {
        if (stage != null) {
            stage.close();
        }
    }

    public boolean showAndWait() {
        if (stage != null) {
            stage.showAndWait();
        }
        return result;
    }
}
