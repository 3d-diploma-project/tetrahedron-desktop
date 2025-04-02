package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.cmps.tetrahedron.controller.*;
import org.cmps.tetrahedron.i18n.LocalizationListener;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.ModelSettings;
import org.cmps.tetrahedron.model.Stress;

import javafx.scene.input.MouseEvent;
import org.cmps.tetrahedron.view.component.Switch;

import java.io.File;

public class RightToolbar implements LocalizationListener {

    @FXML
    private Label stressLabel, stressSecondaryLabel, displacementLabel, characteristicLabel;

    @FXML
    private Button stressButton, displacementButton, characteristicButton;

    @FXML
    private Switch elementGridController;

    public void initialize() {
        LocalizationController localization = LocalizationController.getInstance();
        ModelSettings modelSettings = ModelSettings.getInstance();

        localization.registerListener(this);
        elementGridController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"),
                                              modelSettings.isShowElementMesh(),
                                              modelSettings::setShowElementMesh);
    }

    @Override
    public void onUpdateLanguage() {
        LocalizationController localization = LocalizationController.getInstance();

        stressLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-label"));
        stressSecondaryLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-secondaryLabel"));
        stressButton.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "load-button"));

        displacementLabel.setText(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "displacement-label"));
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
        DeformationDialog.showDialog();
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
