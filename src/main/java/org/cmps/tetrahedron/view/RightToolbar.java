package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.cmps.tetrahedron.controller.*;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.i18n.LocalizationListener;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.model.Stress;

import javafx.scene.input.MouseEvent;
import org.cmps.tetrahedron.view.component.Switch;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.io.File;

import static org.cmps.tetrahedron.enums.StressDisplayOption.MISES;

public class RightToolbar implements LocalizationListener {

    @Getter
    private static RightToolbar instance;

    @FXML
    private Label stressLabel, stressSecondaryLabel, displacementLabel, displacementSecondaryLabel, characteristicLabel;

    @FXML
    private Button stressButton, displacementButton, characteristicButton;

    @FXML
    private Button stressSettings;

    @FXML
    private Switch elementGridController;
    @FXML
    private Switch lightController;

    public void initialize() {
        instance = this;

        LocalizationController localization = LocalizationController.getInstance();
        ModelViewSettings modelViewSettings = ModelViewSettings.getInstance();

        localization.registerListener(this);
        elementGridController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"),
                                              modelViewSettings.isShowElementMesh(),
                                              modelViewSettings::setShowElementMesh);
        lightController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "light"),
                                        modelViewSettings.isShowLight(),
                                        modelViewSettings::setShowLight);

        stressSettings.setVisible(false);
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
        lightController.setLabel(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "light"));
    }

    public void setStressDisplayOption(StressDisplayOption stressDisplayOption) {
        if (stressDisplayOption == null) {
            return;
        }

        LocalizationController localization = LocalizationController.getInstance();
        String text;
        if (MISES.equals(stressDisplayOption)) {
            text = localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-secondaryLabel");
        } else {
            text = localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "stress-secondaryLabel2");
            text = text.formatted(stressDisplayOption.toString().toLowerCase());
        }

        stressSecondaryLabel.setVisible(true);
        stressSecondaryLabel.setText(text);
    }

    @FXML
    private void selectStressFile() {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);
                StressController.getInstance().applyStress(file);
                Stress stressModel = StressController.getInstance().getStress();

                LegendData.getInstance().updateValuesRange(stressModel.getMinStress(), stressModel.getMaxStress());
                stressSettings.setVisible(true);
            }
        } catch (ModelValidationException e) {
            new ErrorDialog(e);
        }
    }

    @FXML
    private void openStressScaleDialog() {
        Bounds bounds = stressButton.localToScreen(stressButton.getBoundsInParent());
        StressDialog.showDialog(bounds.getMinX(), bounds.getMinY());
    }

    @FXML
    private void selectCustomCharacteristicFile() {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);
                ModelController.getInstance().initCustomCharacteristic(file);
                CustomCharacteristic customModel = ModelController.getInstance().getCustomCharacteristic();

                LegendData.getInstance().updateValuesRange(customModel.getMinValue(), customModel.getMaxValue());
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
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);

                DeformationController.getInstance().applyDisplacements(file);
            }
        } catch (ModelValidationException e) {
            new ErrorDialog(e);
        }
    }
}
