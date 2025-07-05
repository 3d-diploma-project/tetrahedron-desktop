package org.cmps.tetrahedron.view.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.cmps.tetrahedron.controller.LocalizationController;

public class ColorEditor {

    private Color color;
    @Getter
    private boolean wasUpdated = false;

    @FXML
    private Label colorName;
    @FXML
    private Label hexValue;
    @FXML
    private Button pickColorButton;
    @FXML
    private ColorPicker colorPicker;

    public void initialize(String label, float[] color) {
        LocalizationController localization = LocalizationController.getInstance();
        colorName.setText(localization.getString(LocalizationController.COLOR_PICKER_BUNDLE, label));
        updateColor(Color.color(color[0], color[1], color[2]));
    }

    @FXML
    public void selectModelColor() {
        colorPicker.show();

        colorPicker.setOnAction(event -> {
            updateColor(colorPicker.getValue());
            wasUpdated =  true;
        });
    }

    public float[] getColor() {
        return new float[] {
                (float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue()
        };
    }

    private void updateColor(Color color) {
        this.color = color;

        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        String hex = String.format("#%02X%02X%02X", red, green, blue);

        pickColorButton.setStyle("-fx-background-color: " + hex + ";");
        hexValue.setText(hex);
    }
}
