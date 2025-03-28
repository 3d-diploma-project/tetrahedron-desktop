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
import org.cmps.tetrahedron.exception.ModelValidationException;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorDialog {

    private static final LocalizationController local = LocalizationController.getInstance();

    @FXML
    private Label errorMessage;
    @FXML
    private Button closeButton;

    private double xOffset = 0;
    private double yOffset = 0;

    public ErrorDialog(ModelValidationException exception) {
        try {
            Locale.setDefault(local.getCurrentLocale());

            URL fxmlPath = getClass().getClassLoader().getResource("view/ErrorDialog.fxml");
            FXMLLoader loader =
                    new FXMLLoader(fxmlPath, ResourceBundle.getBundle(LocalizationController.ERROR_DIALOG_BUNDLE));

            loader.setController(this);
            Pane root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);

            String message = local.getString(LocalizationController.ERROR_DIALOG_BUNDLE, exception.getMessage());
            if (exception.getSecondaryMessage() != null) {
                message += "\n\n"
                        + local.getString(LocalizationController.ERROR_DIALOG_BUNDLE, exception.getSecondaryMessage());
            }
            errorMessage.setText(message.formatted(exception.getParams()));

            if (closeButton != null) {
                closeButton.setOnAction(event -> closeDialog());
            }

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException("Error happen during component load: " + e.getMessage(), e);
        }
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
