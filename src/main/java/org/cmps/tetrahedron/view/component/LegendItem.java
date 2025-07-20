package org.cmps.tetrahedron.view.component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import lombok.Getter;

public class LegendItem {

    @FXML
    private TextField start;
    @FXML
    private TextField end;
    @FXML
    private HBox color;
    @Getter
    private float[] rgbColor;

    public void setStart(float startValue) {
        start.setText(String.format("%.2e", startValue));
    }

    public float getStart() {
        return parseNumber(start.getText());
    }

    public void setEnd(float endValue) {
        end.setText(String.format("%.2e", endValue));
        end.setVisible(true);
        color.getStyleClass().remove("color-border");
    }

    public float getEnd() {
        return parseNumber(end.getText());
    }

    private float parseNumber(String number) {
        if (number.isBlank()) {
            return 0;
        }

        return Float.parseFloat(number.replace(",", "."));
    }

    public void setColor(float[] rgbColor) {
        this.rgbColor = rgbColor;
        color.setStyle("-fx-background-color: " + toHex(rgbColor) + ";");
    }

    public void setEditable(boolean editable) {
        start.setEditable(editable);
    }

    private String toHex(float[] rgbColor) {
        return String.format("#%02X%02X%02X",
                             (int) (rgbColor[0] * 255),
                             (int) (rgbColor[1] * 255),
                             (int) (rgbColor[2] * 255));
    }
}
