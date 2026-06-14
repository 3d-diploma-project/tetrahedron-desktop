package org.cmps.tetrahedron.view.mesh;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.cmps.tetrahedron.utils.NativesExtractor;
import org.cmps.tetrahedron.viewmodel.MeshViewModel;

import java.nio.file.Path;
import java.util.Objects;

public class MeshPage {

    private final MeshViewModel meshViewModel = MeshViewModel.getInstance();

    @FXML
    private HBox progressContainer;

    @FXML
    private Label meshStatus;

    @FXML
    private void initialize() {
        meshViewModel.getMeshStatus().addListener((_, _, newValue) -> {
            boolean progressBarVisible = newValue != null && !newValue.isEmpty();
            progressContainer.visibleProperty().set(progressBarVisible);
        });
        meshStatus.textProperty().bind(meshViewModel.getMeshStatus());

        String gmshName = "libgmsh" + NativesExtractor.getPlatformExtension();
        Path gmshPath = NativesExtractor.getNativesDir().resolve(gmshName);
        System.load(gmshPath.toString());

        if (meshViewModel.getStlFileName().get() != null) {
            return;
        }

        if (Objects.equals(System.getProperty("debug"), "true")) {
            initModelIfInDebug();
        } else {
            Platform.runLater(MeshFilesPickerDialog::openDialogWindow);
        }
    }

    private void initModelIfInDebug() {
        meshViewModel.getIs2D().set(false);
        meshViewModel.displayStlModel("models/model-3d.stl");
    }

    @FXML
    private void onProgressClicked() {
        var dialog = MeshProgressDialog.openDialogWindow();
        dialog.appendLog(meshViewModel.getMesherLogs().toString());
        meshViewModel.setMeshProgressDialog(dialog);
    }
}
