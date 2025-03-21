package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.ModelController;

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

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(null);
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.setScene(new Scene(loader.load()));

            DeformationScaleDialog controller = loader.getController();
            controller.dialogStage = stage;
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
}
