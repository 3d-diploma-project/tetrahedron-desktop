package org.cmps.tetrahedron.view;

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

    private boolean result = false;

    private static final LocalizationController local = LocalizationController.getInstance();

    public WarningDialog(String title, String message, String continueKey) {
        try {
            Locale.setDefault(local.getCurrentLocale());

            URL fxmlPath = getClass().getClassLoader().getResource("view/WarningDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath, ResourceBundle.getBundle(LocalizationController.WARNING_DIALOG_BUNDLE));

            loader.setController(this);
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);

            errorTitle.setText(local.getString(LocalizationController.WARNING_DIALOG_BUNDLE, title));
            String translatedMessage = local.getString(LocalizationController.WARNING_DIALOG_BUNDLE, message);
            if (continueKey != null) {
                translatedMessage += "\n\n" + local.getString(LocalizationController.WARNING_DIALOG_BUNDLE, continueKey);
            }
            errorMessage.setText(translatedMessage);

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
            throw new RuntimeException("Error happen during component load: " + e.getMessage(), e);
        }
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

    public boolean showAndWait() {
        Stage stage = (Stage) yesButton.getScene().getWindow();
        if (stage != null) {
            stage.showAndWait();
        }
        return result;
    }
}
