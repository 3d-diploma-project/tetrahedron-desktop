package org.cmps.tetrahedron.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.io.IOException;
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
    private TextField legendCountInput;

    @FXML
    public Button saveButton;

    @FXML
    public void initialize() {
        float[] colorArray = ColorSettings.getInstance().getModelColor();
        tempSelectedColor = Color.color(colorArray[0], colorArray[1], colorArray[2]);

        colorPicker.setValue(tempSelectedColor);
        updateColorDisplay(tempSelectedColor);

        legendCountInput.setText(String.valueOf(LegendUtils.getColorArraySize()));

//        Platform.runLater(() -> {
//            legendCountInput.setDisable(ModelController.getInstance().getModelColors() == null);
//        });
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

        try {
            int colorCount = Integer.parseInt(legendCountInput.getText());
            if (colorCount >= 2 && colorCount <= 15) {
                LegendUtils.setColorArraySize(colorCount);
                StressDisplayOption displayOption = StressController.getInstance().getStress().getDisplayOption();
                StressController.getInstance().processStressData(displayOption);
            } else {
                System.err.println("Legend color count must be between 2 and 15!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid legend color count input!");
        }
    }
}
