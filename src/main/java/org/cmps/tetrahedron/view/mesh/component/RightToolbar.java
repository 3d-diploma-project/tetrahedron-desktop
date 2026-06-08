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
        meshViewModel.getMinMeshSize().bind(minElementSize.textProperty());
        meshViewModel.getMaxMeshSize().bind(maxElementSize.textProperty());
        meshViewModel.getAngel().bind(engel.textProperty());

        nodesCount.textProperty().bind(meshViewModel.getNodesCount());
        elementsCount.textProperty().bind(meshViewModel.getElementsCount());

        if (meshViewModel.getStlFileName().get() == null) {
            saveModelButton.setDisable(true);
        }
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
        saveModelButton.setDisable(false);
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
