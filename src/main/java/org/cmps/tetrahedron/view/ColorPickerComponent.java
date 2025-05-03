package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.util.Locale;
import java.util.ResourceBundle;

public class ColorPickerComponent {

    private static final LocalizationController local = LocalizationController.getInstance();

    private Color tempSelectedColor;

    @FXML
    public Button pickColor;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    public Label hexValueLabel;

    @FXML
    public Button saveButton;

    @FXML
    public void initialize() {
        if (ColorSettings.getInstance().isColoredInSelectedColor()) {
            float[] colorArray = ColorSettings.getInstance().getModelColor();
            tempSelectedColor = Color.color(colorArray[0], colorArray[1], colorArray[2]);
        }

        colorPicker.setValue(tempSelectedColor);
        updateColorDisplay(tempSelectedColor);
    }

    private void updateColorDisplay(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        String hex = String.format("#%02X%02X%02X", red, green, blue);

        pickColor.setStyle("-fx-background-color: " + hex + ";");
        hexValueLabel.setText(hex);
    }

    @FXML
    public void clickOnColorPicker() {
        colorPicker.show();

        colorPicker.setOnAction(event -> {
            tempSelectedColor = colorPicker.getValue();
            updateColorDisplay(tempSelectedColor);
        });
    }

    public static void showColorPicker(double x, double y) {
        Locale.setDefault(local.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/ColorPicker.fxml", DialogPane.class,
                ResourceBundle.getBundle("i18n.color-picker"));

        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    public void applyColor(ActionEvent actionEvent) {
        if (tempSelectedColor != null) {
            float[] colorArray = new float[] {
                    (float) tempSelectedColor.getRed(),
                    (float) tempSelectedColor.getGreen(),
                    (float) tempSelectedColor.getBlue()
            };

            ColorSettings.getInstance().setModelColor(colorArray);
            ColorSettings.getInstance().setColoredInSelectedColor(true);
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
