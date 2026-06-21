package org.cmps.tetrahedron.view.model;

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
import org.cmps.tetrahedron.model.TetraModelApi;
import org.cmps.tetrahedron.router.Page;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.common.ErrorDialog;
import org.cmps.tetrahedron.view.common.FilePicker;
import org.cmps.tetrahedron.viewmodel.DimensionViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ModelFilesPickerDialog {

    private final ModelController modelController = ModelController.getInstance();

    @FXML
    private FilePicker nodesController;
    @FXML
    private FilePicker indicesController;

    @Setter
    private Dialog<Scene> dialog;

    @FXML
    private Button createModelButton;

    public static void openDialogWindow() {
        Locale.setDefault(LocalizationController.getInstance().getCurrentLocale());
        Dialog<Scene> dialog = new Dialog<>();

        URL fxmlUrl = ModelFilesPickerDialog.class.getClassLoader().getResource("view/model/ModelFilesPickerDialog.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl,
                                           ResourceBundle.getBundle(LocalizationController.MODEL_FILES_PICKER_BUNDLE));

        try {
            dialog.setDialogPane(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ModelFilesPickerDialog modelFilesPickerDialog = loader.getController();
        modelFilesPickerDialog.setDialog(dialog);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(modelFilesPickerDialog::onClose);

        ImageView logo = ResourceReader.imageReader("/logo.png");
        stage.getIcons().add(logo.getImage());

        dialog.setTitle("Select model");
        dialog.show();
    }

    public void onClick() {
        boolean nodesValidation = validateFileExistence(nodesController);
        boolean indicesValidation = validateFileExistence(indicesController);

        if (!nodesValidation || !indicesValidation) {
            new ErrorDialog(new ModelValidationException("vertices-faces-read"));
            return;
        }

        try {
            TetraModelApi model = DataReader.readModel(nodesController.getFile(), indicesController.getFile(),
                                                       DimensionViewModel.getInstance().getDimension());
            modelController.initModelData(model);
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
        nodesController.setOnFileSelectedCallback(this::updateCreateButtonState);
        indicesController.setOnFileSelectedCallback(this::updateCreateButtonState);
        updateCreateButtonState();
    }

    private void updateCreateButtonState() {
        boolean bothFilesSelected = nodesController.getFile() != null && indicesController.getFile() != null;
        createModelButton.setDisable(!bothFilesSelected);
    }
}
