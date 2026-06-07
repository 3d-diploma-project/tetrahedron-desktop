package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.ColorEditor;

import java.util.Locale;
import java.util.ResourceBundle;

public class ColorPicker {

    private static final LocalizationController locale = LocalizationController.getInstance();

    @FXML
    private ColorEditor modelColorPickerController;
    @FXML
    private ColorEditor backgroundColorPickerController;

    @FXML
    public Button saveButton;

    public static void showColorPicker(double x, double y) {
        Locale.setDefault(locale.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/ColorPicker.fxml", DialogPane.class,
                                                       ResourceBundle.getBundle("i18n.color-picker"));

        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    @FXML
    public void initialize() {
        ColorSettings colorSettings = ColorSettings.getInstance();

        modelColorPickerController.initialize("model-color", colorSettings.getModelColor());
        backgroundColorPickerController.initialize("background-color", colorSettings.getBackgroundColor());
    }

    public void applyColor(ActionEvent actionEvent) {
        ColorSettings.getInstance().setModelColor(modelColorPickerController.getColor());
        if (modelColorPickerController.isWasUpdated()) {
            ColorSettings.getInstance().setColoredInSelectedColor(true);
        }
        ColorSettings.getInstance().setBackgroundColor(backgroundColorPickerController.getColor());

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
