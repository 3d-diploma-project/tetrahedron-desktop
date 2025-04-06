package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.cmps.tetrahedron.controller.*;
import org.cmps.tetrahedron.i18n.LocalizationListener;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.model.Stress;

import javafx.scene.input.MouseEvent;
import org.cmps.tetrahedron.view.component.Switch;

import java.io.File;

public class RightToolbar implements LocalizationListener {

    @FXML
    private Label stressLabel, stressSecondaryLabel, displacementLabel, displacementSecondaryLabel, characteristicLabel;

    @FXML
    private Button stressButton, displacementButton, characteristicButton;

    @FXML
    private Switch elementGridController;

    public void initialize() {
        LocalizationController localization = LocalizationController.getInstance();
        ModelViewSettings modelViewSettings = ModelViewSettings.getInstance();

        localization.registerListener(this);
        elementGridController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"),
                                              modelViewSettings.isShowElementMesh(),
                                              modelViewSettings::setShowElementMesh);
    }

    @Override
    public void onUpdateLanguage() {
        LocalizationController localization = LocalizationController.getInstance();

        stressLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-label"));
        stressSecondaryLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-secondaryLabel"));
        stressButton.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "load-button"));

        displacementLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "displacement-label"));
        displacementSecondaryLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "displacement-secondaryLabel"));
        displacementButton.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "load-button"));

        characteristicLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "characteristic-label"));
        characteristicButton.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "load-button"));

        elementGridController.setLabel(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"));
    }

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
            new ErrorDialog(e);
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
            new ErrorDialog(e);
        }
    }

    @FXML
    private void openDeformationScaleDialog() {
        Bounds bounds = displacementButton.localToScreen(displacementButton.getBoundsInParent());
        DeformationDialog.showDialog(bounds.getMinX(), bounds.getMinY());
    }

    @FXML
    private void selectDisplacementsFile(MouseEvent event) {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);

                DeformationController.getInstance().applyDisplacements(file);
                ModelController.getInstance().centerModel();
                ModelController.getInstance().setModelReady(true);
            }
        } catch (ModelValidationException e) {
            new ErrorDialog(e);
        }
    }
}
