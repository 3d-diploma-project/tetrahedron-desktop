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
import org.cmps.tetrahedron.utils.ErrorMessages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorDialog implements LocalizationListener {

    @FXML
    private Label errorTitle;
    @FXML
    private Label errorMessage;
    @FXML
    private Button closeButton;

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    private final String originalMessageKey;

    public void initialize() {
        LocalizationController.getInstance().registerListener(this);
    }

    @Override
    public void onUpdateLanguage() {
        Platform.runLater(() -> {
            errorTitle.setText(ErrorMessages.ERROR);
            closeButton.setText(ErrorMessages.OK);

            if (originalMessageKey != null) {
                String translatedMessage = getTranslatedMessage(originalMessageKey);
                errorMessage.setText(translatedMessage != null ? translatedMessage : originalMessageKey);
            }
        });
    }

    public ErrorDialog(String title, String message) {
        try {
            URL fxmlPath = getClass().getClassLoader().getResource("view/ErrorDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath, ResourceBundle.getBundle(LocalizationController.ERROR_DIALOG_BUNDLE));

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
            originalMessageKey = message;
            String translatedMessage = getTranslatedMessage(message);
            errorMessage.setText(translatedMessage != null ? translatedMessage : message);

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

        } catch (IOException e) {
            throw new RuntimeException("Ошибка открытия окна ошибки: " + e.getMessage(), e);
        }
    }

    private String getTranslatedMessage(String key) {
        try {
            return ErrorMessages.class.getField(key).get(null).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    @FXML
    private void closeDialog() {
        if (stage != null) {
            stage.close();
        }
    }

    public void show() {
        if (stage != null) {
            stage.showAndWait();
        }
    }
}
