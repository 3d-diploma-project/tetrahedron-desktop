package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.DeformationController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.util.Locale;
import java.util.ResourceBundle;

public class DeformationDialog {

    private static final DeformationController deformationController = DeformationController.getInstance();
    private static final LocalizationController local = LocalizationController.getInstance();

    @FXML
    private TextField scaleInput;
    @FXML
    private Button saveButton;

    public static void showDialog() {
        Locale.setDefault(local.getCurrentLocale());
        Pane pane = ResourceReader.readComponent("/view/DeformationScaleDialog.fxml", Pane.class,
                                                       ResourceBundle.getBundle("i18n.deformation-dialog"));

        Stage stage = DialogUtils.createModalWindow(pane);
        stage.showAndWait();
    }

    @FXML
    public void initialize() {
        float currentScale = deformationController.getCurrentScale();
        scaleInput.setText(String.valueOf(currentScale));
    }

    @FXML
    private void applyChanges() {
        try {
            float scale = Float.parseFloat(scaleInput.getText());
            deformationController.applyDeformationScale(scale);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            scaleInput.setStyle("-fx-background-color: FAE1E1;");
        }
    }
}
