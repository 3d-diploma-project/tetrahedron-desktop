package org.cmps.tetrahedron.view.mesh;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.viewmodel.MeshViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MeshProgressDialog {

    @FXML
    private Label meshStatus;

    @FXML
    private TextArea logArea;

    @Setter
    private Dialog<Scene> dialog;

    public static MeshProgressDialog openDialogWindow() {
        Locale.setDefault(LocalizationController.getInstance().getCurrentLocale());
        Dialog<Scene> dialog = new Dialog<>();

        URL fxmlUrl = MeshProgressDialog.class.getClassLoader().getResource("view/mesh/MeshProgressDialog.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, ResourceBundle.getBundle("i18n.mesh-file-picker"));

        try {
            dialog.setDialogPane(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MeshProgressDialog meshProgressDialog = loader.getController();
        meshProgressDialog.setDialog(dialog);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        ImageView logo = ResourceReader.imageReader("/logo.png");
        stage.getIcons().add(logo.getImage());

        dialog.setTitle("Meshing Process");
        dialog.show();
        return meshProgressDialog;
    }

    public void initialize() {
        meshStatus.textProperty().bind(MeshViewModel.getInstance().getMeshStatus());
    }

    public void appendLog(String log) {
        logArea.appendText(log + System.lineSeparator());
    }

    @FXML
    private void closeModal() {
        dialog.close();
    }
}
