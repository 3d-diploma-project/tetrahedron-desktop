package org.cmps.tetrahedron.view.mesh.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.cmps.tetrahedron.controller.FileChooserController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.view.common.Switch;
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
        meshViewModel.getMinMeshSize().bindBidirectional(minElementSize.textProperty());
        meshViewModel.getMaxMeshSize().bindBidirectional(maxElementSize.textProperty());
        meshViewModel.getAngle().bind(engel.textProperty());

        nodesCount.textProperty().bind(meshViewModel.getNodesCount());
        elementsCount.textProperty().bind(meshViewModel.getElementsCount());

        if (meshViewModel.getStlFileName().get() == null) {
            saveModelButton.setDisable(true);
        }

        meshViewModel.getMeshStatus().addListener((_, _, newValue) -> {
            boolean meshButtonDisabled = Objects.equals(newValue, "In progress");
            meshButton.setDisable(meshButtonDisabled);

            boolean saveButtonDisabled = !Objects.equals(newValue, "Success");
            saveModelButton.setDisable(saveButtonDisabled);
        });
    }

    @FXML
    private void changeStlFile() {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(stlFileNameLabel.getScene().getWindow());

        meshViewModel.getStlFileName().set(file.getAbsolutePath());
    }

    @FXML
    private void meshModel() {
        meshViewModel.meshModel();
    }

    @FXML
    private void saveModel() {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        DirectoryChooser fileChooser = fileChooserController.createDirectoryChooser();
        File directory = fileChooser.showDialog(stlFileNameLabel.getScene().getWindow());

        fileChooserController.saveLastUsedDirectory(directory);
        meshViewModel.saveModel(directory);
    }
}
