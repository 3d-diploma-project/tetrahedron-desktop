package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.utils.DialogUtils;

import java.io.IOException;
import java.net.URL;

public class DeformationScaleDialog {
    @FXML
    private TextField scaleInput;
    @FXML
    private Button saveButton;

    private Stage dialogStage;

    public static void showDialog() {
        try {
            URL fxmlPath = DeformationScaleDialog.class.getClassLoader().getResource("view/DeformationScaleDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlPath);

            Scene scene = new Scene(loader.load());
            scene.setFill(Color.TRANSPARENT);
            Stage stage = DialogUtils.createModalWindow(scene);

            DeformationScaleDialog controller = loader.getController();
            controller.dialogStage = stage;
            float currentScale = ModelController.getInstance().getCurrentScale();
            controller.setScaleValue(currentScale);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
