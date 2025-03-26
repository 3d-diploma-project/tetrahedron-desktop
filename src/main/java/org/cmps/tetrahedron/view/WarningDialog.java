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
import org.cmps.tetrahedron.utils.WarningMessages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WarningDialog implements LocalizationListener {

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

    private final String originalMessageKey;

    public void initialize() {
        LocalizationController.getInstance().registerListener(this);
    }

    @Override
    public void onUpdateLanguage() {
        Platform.runLater(() -> {
            errorTitle.setText(WarningMessages.ATTENTION);
            yesButton.setText(WarningMessages.YES);
            noButton.setText(WarningMessages.NO);

            if (originalMessageKey != null) {
                String translatedMessage = getTranslatedMessage(originalMessageKey);
                errorMessage.setText(translatedMessage != null ? translatedMessage : originalMessageKey);
            }
        });
    }

    public WarningDialog(String title, String message) {
        try {
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
            originalMessageKey = message;
            String translatedMessage = getTranslatedMessage(message);
            errorMessage.setText(translatedMessage != null ? translatedMessage : message);

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

    private String getTranslatedMessage(String key) {
        try {
            return WarningMessages.class.getField(key).get(null).toString();
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

    public boolean showAndWait() {
        if (stage != null) {
            stage.showAndWait();
        }
        return result;
    }
}
