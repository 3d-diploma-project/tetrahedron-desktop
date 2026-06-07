package org.cmps.tetrahedron.view.mesh;

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
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.router.Page;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.common.ErrorDialog;
import org.cmps.tetrahedron.view.common.FilePicker;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class MeshFilesPickerDialog {

    private final ModelController modelController = ModelController.getInstance();

    @FXML
    private FilePicker stlController;

    @Setter
    private Dialog<Scene> dialog;

    @FXML
    private Button createModelButton;

    public static void openDialogWindow() {
        Locale.setDefault(LocalizationController.getInstance().getCurrentLocale());
        Dialog<Scene> dialog = new Dialog<>();

        URL fxmlUrl = MeshFilesPickerDialog.class.getClassLoader().getResource("view/mesh/MeshFilesPickerDialog.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl,
                                           ResourceBundle.getBundle(LocalizationController.MODEL_FILES_PICKER_BUNDLE));

        try {
            dialog.setDialogPane(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MeshFilesPickerDialog meshFilesPickerDialog = loader.getController();
        meshFilesPickerDialog.setDialog(dialog);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(meshFilesPickerDialog::onClose);

        ImageView logo = ResourceReader.imageReader("/logo.png");
        stage.getIcons().add(logo.getImage());

        dialog.setTitle("Select model");
        dialog.show();
    }

    public void onClick() {
        boolean stlValidation = validateFileExistence(stlController);

        if (!stlValidation) {
            new ErrorDialog(new ModelValidationException("vertices-faces-read"));
            return;
        }

        try {
            modelController.initModelData(stlController.getFile(), stlController.getFile());
        } catch (ModelValidationException e) {
            new ErrorDialog(e);
            return;
        } catch (InternalValidationException e) {
            return;
        }

        if (dialog != null) {
            dialog.close();
        }
    }

    private void onClose(WindowEvent dialogEvent) {
        Router.getInstance().openPage(Page.HOME);
    }

    private boolean validateFileExistence(FilePicker filePicker) {
        if (filePicker.getFile() == null) {
            filePicker.showNotSelectedFileError();
            return false;
        }

        return true;
    }

    @FXML
    public void initialize() {
        stlController.setOnFileSelectedCallback(this::updateCreateButtonState);
        updateCreateButtonState();
    }

    private void updateCreateButtonState() {
        boolean bothFilesSelected = stlController.getFile() != null;
        createModelButton.setDisable(!bothFilesSelected);
    }
}
