package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.DeformationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.utils.DialogUtils;

import java.io.IOException;
import java.net.URL;

public class DeformationDialog {
    @FXML
    private TextField scaleInput;

    @FXML
    private Button saveButton;

    private static float currentScaleValue = DeformationController.DEFAULT_SCALE;

    public static void updateCurrentScale(float scale) {
        currentScaleValue = scale;
    }

    public static void showDialog() {
        try {
            URL fxmlPath = DeformationDialog.class.getClassLoader().getResource("view/DeformationScaleDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath);

            Scene scene = new Scene(loader.load());
            scene.setFill(Color.TRANSPARENT);
            Stage stage = DialogUtils.createModalWindow(scene);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        float currentScale = DeformationController.getInstance().getCurrentScale();
        setScaleValue(currentScale);
    }

    @FXML
    private void applyChanges() {
        try {
            float scale = Float.parseFloat(scaleInput.getText());
            if (scale > 0) {
                DeformationController.getInstance().applyDeformationScale(
                        scale,
                        ModelController.getInstance().getModel(),
                        ModelController.getInstance().getOriginalVertices()
                );
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
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
