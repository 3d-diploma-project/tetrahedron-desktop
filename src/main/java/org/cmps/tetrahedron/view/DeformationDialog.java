package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.DeformationController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.Switch;

import java.util.Locale;
import java.util.ResourceBundle;

public class DeformationDialog {

    private static final DeformationController deformationController = DeformationController.getInstance();
    private static final LocalizationController local = LocalizationController.getInstance();

    @FXML
    private ComboBox<String> displacementComponentComboBox;

    @FXML
    private TextField scaleInput;

    @FXML
    private Button saveButton;

    @FXML
    private Switch elementGridController;

    private boolean isGlobalMode;
    private static String lastSelectedComponent = "x";

    public static void showDialog(double x, double y) {
        Locale.setDefault(local.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/DeformationScaleDialog.fxml", DialogPane.class,
                ResourceBundle.getBundle(LocalizationController.DEFORMATION_DIALOG_BUNDLE));

        DialogUtils.displayDialogOnLeft(pane, x, y);
    }

    @FXML
    public void initialize() {
        float currentScale = deformationController.getCurrentScale();
        scaleInput.setText(String.valueOf(currentScale));

        displacementComponentComboBox.setValue(lastSelectedComponent);
        displacementComponentComboBox.setPromptText(
                local.getString(LocalizationController.DEFORMATION_DIALOG_BUNDLE, "component-deformation-choose"));

        isGlobalMode = true;
        elementGridController.setInitialState(
                LocalizationController.getInstance().getString(
                        LocalizationController.DEFORMATION_DIALOG_BUNDLE,
                        "global-deformation-label"
                ),
                isGlobalMode,
                this::onSwitchToggle
        );
        elementGridController.setLabelStyle("-fx-font-family: 'Geologica Roman'; -fx-font-size: 13px; -fx-text-fill: #0E0E0E;");
        onSwitchToggle(true);
    }

    @FXML
    private void applyChanges() {
        try {
            float scale = Float.parseFloat(scaleInput.getText());
            String component = displacementComponentComboBox.getValue();

            if (!isGlobalMode && component != null) {
                lastSelectedComponent = component;
                ModelController.getInstance().clearDisplacement();
                deformationController.applyDeformationScale(scale, component);
            } else {
                ModelController.getInstance().clearDisplacement();
                deformationController.applyDeformationScale(scale);
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            scaleInput.setStyle("-fx-background-color: FAE1E1;");
        }
    }

    private void onSwitchToggle(boolean isOn) {
        isGlobalMode = isOn;
        displacementComponentComboBox.setDisable(isOn);
    }

}
