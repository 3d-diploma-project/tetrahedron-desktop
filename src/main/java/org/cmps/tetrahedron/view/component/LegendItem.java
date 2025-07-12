package org.cmps.tetrahedron.view.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LegendItem {

    @FXML
    private Label start;
    @FXML
    private Label end;
    @FXML
    private VBox endContainer;
    @FXML
    private HBox color;

    public void setStart(float startValue) {
        start.setText(String.format("%.2e", startValue));
    }

    public void setEnd(float endValue) {
        end.setText(String.format("%.2e", endValue));
        endContainer.setVisible(true);
    }

    public void setColor(float[] rgbColor) {
        color.setStyle("-fx-background-color: " + toHex(rgbColor) + ";");
    }

    private String toHex(float[] rgbColor) {
        return String.format("#%02X%02X%02X",
                             (int) (rgbColor[0] * 255),
                             (int) (rgbColor[1] * 255),
                             (int) (rgbColor[2] * 255));
    }
}
