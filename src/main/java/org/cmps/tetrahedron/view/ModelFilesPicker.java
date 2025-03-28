package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Setter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.InvalidModelDataException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ModelFilesPicker {

    private final ModelController modelController = ModelController.getInstance();

    @FXML
    private Button createModelButton;

    @FXML
    private FilePicker nodesController;
    @FXML
    private FilePicker indicesController;

    @Setter
    private Dialog<Scene> dialog;

    public static void openDialogWindow() {
        Dialog<Scene> dialog = new Dialog<>();

        URL fxmlUrl = ModelFilesPicker.class.getClassLoader().getResource("view/ModelFilesPicker.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, ResourceBundle.getBundle(LocalizationController.MODEL_FILES_PICKER_BUNDLE));

        try {
            dialog.setDialogPane(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ModelFilesPicker modelFilesPicker = loader.getController();
        modelFilesPicker.setDialog(dialog);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(modelFilesPicker::onClose);

        ImageView logo = ResourceReader.imageReader("/logo.png");
        stage.getIcons().add(logo.getImage());

        dialog.setTitle("Select model");
        dialog.show();
    }

    public void onClick() {
        boolean nodesValidation = validateFileExistence(nodesController);
        boolean indicesValidation = validateFileExistence(indicesController);
        if (!nodesValidation || !indicesValidation) {
            LocalizationController localization = LocalizationController.getInstance();
            ModelValidationException e = new ModelValidationException(
                    localization.getString(LocalizationController.ERROR_DIALOG_BUNDLE, "vertices-faces-read"));

            new ErrorDialog(e).show();
            return;
        }

        try {
            modelController.initModelData(nodesController.getFile(), indicesController.getFile());
        } catch (ModelValidationException e) {
            new ErrorDialog(e).show();
            return;
        } catch (InvalidModelDataException e) {
            return;
        }

        if (dialog != null) {
            dialog.close();
        }
    }

    private void onClose(WindowEvent dialogEvent) {
        dialogEvent.consume();
        onClick();
    }

    private boolean validateFileExistence(FilePicker filePicker) {
        if (filePicker.getFile() == null) {
            filePicker.showNotSelectedFileError();
            return false;
        }

        return true;
    }
}
