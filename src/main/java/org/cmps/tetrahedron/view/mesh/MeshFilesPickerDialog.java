package org.cmps.tetrahedron.view.mesh;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Setter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.router.Page;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.common.ErrorDialog;
import org.cmps.tetrahedron.view.common.FilePicker;
import org.cmps.tetrahedron.viewmodel.MeshViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MeshFilesPickerDialog {

    private final MeshViewModel meshViewModel = MeshViewModel.getInstance();
    private final ModelController modelController = ModelController.getInstance();

    @FXML
    private FilePicker stlController;
    @FXML
    private ComboBox<String> modelDimension;

    @Setter
    private Dialog<Scene> dialog;

    @FXML
    private Button createModelButton;

    public static void openDialogWindow() {
        Locale.setDefault(LocalizationController.getInstance().getCurrentLocale());
        Dialog<Scene> dialog = new Dialog<>();

        URL fxmlUrl = MeshFilesPickerDialog.class.getClassLoader().getResource("view/mesh/MeshFilesPickerDialog.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, ResourceBundle.getBundle("i18n.mesh-file-picker"));

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

        meshViewModel.getIs2D().set(modelDimension.getValue().equals("2D"));
        meshViewModel.displayStlModel(stlController.getFile().getAbsolutePath());

        if (dialog != null) {
            dialog.close();
        }
    }

    private void onClose(WindowEvent dialogEvent) {
        if (meshViewModel.getStlFileName().get() != null) {
            return;
        }

        modelController.clearModel();
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
        modelDimension.setValue(meshViewModel.getIs2D().get() ? "2D" : "3D");
        stlController.setOnFileSelectedCallback(this::updateCreateButtonState);
        updateCreateButtonState();
    }

    private void updateCreateButtonState() {
        boolean bothFilesSelected = stlController.getFile() != null;
        createModelButton.setDisable(!bothFilesSelected);
    }
}
