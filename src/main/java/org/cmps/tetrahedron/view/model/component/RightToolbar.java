package org.cmps.tetrahedron.view.model.component;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.cmps.tetrahedron.controller.DeformationController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.Dimension;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.model.Stress;
import org.cmps.tetrahedron.view.common.ErrorDialog;
import org.cmps.tetrahedron.view.common.Switch;
import org.cmps.tetrahedron.view.model.DeformationDialog;
import org.cmps.tetrahedron.view.model.StressDialog;
import org.cmps.tetrahedron.viewmodel.DimensionViewModel;
import org.cmps.tetrahedron.viewmodel.FileLocationViewModel;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.io.File;

import static org.cmps.tetrahedron.enums.StressDisplayOption.MISES;

public class RightToolbar {

    @Getter
    private static RightToolbar instance;

    @FXML
    private Label stressSecondaryLabel;

    @FXML
    private Button stressButton, displacementButton;

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

        elementGridController.setInitialState(
                localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "elements-mesh"),
                modelViewSettings.isShowElementMesh(),
                modelViewSettings::setShowElementMesh);
        lightController.setInitialState(localization.getString(LocalizationController.RIGHT_TOOLBAR_BUNDLE, "light"),
                                        modelViewSettings.isShowLight(),
                                        modelViewSettings::setShowLight);

        stressSettings.setVisible(false);

        stressButton.setDisable(DimensionViewModel.getInstance().getDimension() == Dimension.TWO_D);
        DimensionViewModel
                .getInstance()
                .addDimensionListener(
                        (_, _, newValue)
                                -> stressButton.setDisable(Dimension.getDimensionByLabel(newValue) == Dimension.TWO_D));
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
        FileLocationViewModel fileLocationViewModel = FileLocationViewModel.getInstance();
        FileChooser fileChooser = fileLocationViewModel.createFileChooser();
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        try {
            if (file != null) {
                fileLocationViewModel.saveLastUsedDirectory(file.getParentFile());
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
        FileLocationViewModel fileLocationViewModel = FileLocationViewModel.getInstance();
        FileChooser fileChooser = fileLocationViewModel.createFileChooser();
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        if (file == null) {
            return;
        }

        try {
            fileLocationViewModel.saveLastUsedDirectory(file.getParentFile());
            ModelController.getInstance().initCustomCharacteristic(file);
            CustomCharacteristic customModel = ModelController.getInstance().getCustomCharacteristic();

            LegendData.getInstance().updateValuesRange(customModel.getMinValue(), customModel.getMaxValue());
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
        FileLocationViewModel fileLocationViewModel = FileLocationViewModel.getInstance();
        FileChooser fileChooser = fileLocationViewModel.createFileChooser();
        File file = fileChooser.showOpenDialog(stressButton.getScene().getWindow());

        if (file == null) {
            return;
        }

        try {
            fileLocationViewModel.saveLastUsedDirectory(file.getParentFile());
            DeformationController.getInstance().applyDisplacements(file);
        } catch (ModelValidationException e) {
            new ErrorDialog(e);
        }
    }
}
