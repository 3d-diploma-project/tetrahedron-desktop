package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import org.cmps.tetrahedron.controller.FileChooserController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.SceneController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.Stress;

import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.Optional;

public class RightToolbar {
    @FXML
    private void selectStressFile(MouseEvent mouseEvent) {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        LegendView legendView = LegendView.getInstance();
        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);
                ModelController.getInstance().applyStress(file);
                Stress stressModel = StressController.getInstance().getStress();

                legendView.updateLegend(stressModel.getMinStress(), stressModel.getMaxStress());
            }
        } catch (ModelValidationException e) {
            new ErrorDialog("Помилка!", e.getMessage()).show();
        }
    }

    @FXML
    private void selectCustomCharacteristicFile(MouseEvent mouseEvent) {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        LegendView legendView = LegendView.getInstance();
        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);
                ModelController.getInstance().initCustomCharacteristic(file);
                CustomCharacteristic customModel = ModelController.getInstance().getCustomCharacteristic();

                legendView.updateLegend(customModel.getMinValue(), customModel.getMaxValue());
            }
        } catch (ModelValidationException e) {
            new ErrorDialog("Помилка!", e.getMessage()).show();
        }
    }

    @FXML
    private void openDeformationScaleDialog() {
        DeformationScaleDialog.showDialog();
    }

    @FXML
    private void selectDisplacementsFile(MouseEvent event) {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);

                float defaultScale = ModelController.DEFAULT_SCALE;
                ModelController.getInstance().applyDisplacements(file, defaultScale);

                DeformationScaleDialog.updateCurrentScale(defaultScale);
            }
        } catch (ModelValidationException e) {
            new ErrorDialog("Помилка!", e.getMessage()).show();
        }
    }
}
