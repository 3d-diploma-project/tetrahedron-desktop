package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.utils.DialogUtils;

public class DeformationScaleDialog {

    @FXML
    private TextField scaleInput;
    @FXML
    private Button saveButton;

    public Stage dialogStage;

    public static void showDialog() {
        DeformationScaleDialog controller = DialogUtils.openModal(
                "view/DeformationScaleDialog.fxml",
                javafx.stage.StageStyle.TRANSPARENT,
                true
        );

        if (controller != null) {
            float currentScale = ModelController.getInstance().getCurrentScale();
            controller.setScaleValue(currentScale);
        }
    }

    @FXML
    private void applyChanges() {
        try {
            float scale = Float.parseFloat(scaleInput.getText());
            if (scale > 0) {
                ModelController.getInstance().applyDeformationScale(scale);
                dialogStage.close();
            } else {
                scaleInput.setStyle("-fx-border-color: red;");
            }
        } catch (NumberFormatException e) {
            scaleInput.setStyle("-fx-border-color: red;");
        }
    }

    public void setScaleValue(float scale) {
        scaleInput.setText(String.valueOf(scale));
    }
}
