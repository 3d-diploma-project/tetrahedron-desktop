package org.cmps.tetrahedron.view.mesh.component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.cmps.tetrahedron.viewmodel.FileLocationViewModel;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.enums.MeshStatus;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.view.common.Switch;
import org.cmps.tetrahedron.view.mesh.MeshFilesPickerDialog;
import org.cmps.tetrahedron.viewmodel.MeshViewModel;

import java.io.File;
import java.util.Objects;

public class RightToolbar {

    private final MeshViewModel meshViewModel = MeshViewModel.getInstance();

    @FXML
    private Switch elementGridController;
    @FXML
    private Switch lightController;

    @FXML
    private Label stlFileNameLabel;

    @FXML
    private TextField minElementSize;
    @FXML
    private TextField maxElementSize;
    @FXML
    private TextField engel;

    @FXML
    private Label nodesCount;
    @FXML
    private Label elementsCount;
    @FXML
    private Button saveModelButton;
    @FXML
    private Button meshButton;

    public void initialize() {
        LocalizationController localization = LocalizationController.getInstance();
        ModelViewSettings modelViewSettings = ModelViewSettings.getInstance();

        elementGridController.setInitialState(
                localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"),
                modelViewSettings.isShowElementMesh(),
                modelViewSettings::setShowElementMesh);
        lightController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "light"),
                                        modelViewSettings.isShowLight(),
                                        modelViewSettings::setShowLight);
        minElementSize.textProperty().bindBidirectional(meshViewModel.getMinMeshSize());
        maxElementSize.textProperty().bindBidirectional(meshViewModel.getMaxMeshSize());
        meshViewModel.getAngle().bind(engel.textProperty());

        nodesCount.textProperty().bind(meshViewModel.getNodesCount());
        elementsCount.textProperty().bind(meshViewModel.getElementsCount());

        if (meshViewModel.getStlFileName().get() == null) {
            saveModelButton.setDisable(true);
        }

        meshViewModel.getMeshStatus().addListener((_, _, newValue) -> {
            boolean meshButtonDisabled = Objects.equals(newValue, MeshStatus.IN_PROGRESS);
            meshButton.setDisable(meshButtonDisabled);

            boolean saveButtonDisabled = !Objects.equals(newValue, MeshStatus.SUCCESS);
            saveModelButton.setDisable(saveButtonDisabled);
        });
    }

    @FXML
    private void changeStlFile() {
        Platform.runLater(MeshFilesPickerDialog::openDialogWindow);
    }

    @FXML
    private void meshModel() {
        meshViewModel.meshModel();
    }

    @FXML
    private void saveModel() {
        FileLocationViewModel fileLocationViewModel = FileLocationViewModel.getInstance();
        DirectoryChooser fileChooser = fileLocationViewModel.createDirectoryChooser();
        File directory = fileChooser.showDialog(stlFileNameLabel.getScene().getWindow());

        if (directory == null) {
            return;
        }

        fileLocationViewModel.saveLastUsedDirectory(directory);
        meshViewModel.saveModel(directory);
    }
}
