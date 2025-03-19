package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.cmps.tetrahedron.controller.*;
import org.cmps.tetrahedron.enums.LocalizationListener;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.Stress;

import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class RightToolbar implements LocalizationListener {

    @FXML
    private Label stressLabel, stressSecondaryLabel, displacementLabel, characteristicLabel;

    @FXML
    private Button stressButton, displacementButton, characteristicButton;

    private ResourceBundle bundle;

    public void initialize() {
        LocalizationController.getInstance().registerListener(this);
    }

    @Override
    public void onUpdateLanguage(ResourceBundle bundle) {
        this.bundle = bundle;

        stressLabel.setText(bundle.getString("stress.label"));
        stressSecondaryLabel.setText(bundle.getString("stress.secondaryLabel"));
        stressButton.setText(bundle.getString("stress.button"));

        displacementLabel.setText(bundle.getString("displacement.label"));
        displacementButton.setText(bundle.getString("displacement.button"));

        characteristicLabel.setText(bundle.getString("characteristic.label"));
        characteristicButton.setText(bundle.getString("characteristic.button"));
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
                StressController.getInstance().initStress(file);
                Stress stressModel = StressController.getInstance().getStress();

                legendView.updateLegend(stressModel.getMinStress(), stressModel.getMaxStress());
            }
        } catch (ModelValidationException e) {
            new ErrorDialog(bundle.getString("error.title"), e.getMessage()).show();
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
            new ErrorDialog(bundle.getString("error.title"), e.getMessage()).show();
        }
    }

    @FXML
    private void selectDisplacementsFile(MouseEvent event) {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        try {
            if (file != null) {
                fileChooserController.saveLastUsedDirectory(file);
                ModelController.getInstance().applyDisplacements(file);
            }
        } catch (ModelValidationException e) {
            new ErrorDialog(bundle.getString("error.title"), e.getMessage()).show();
        }
    }
}
