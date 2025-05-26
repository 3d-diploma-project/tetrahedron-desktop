package org.cmps.tetrahedron.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.Switch;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ColorPickerComponent {

    private static final LocalizationController local = LocalizationController.getInstance();

    private Color tempSelectedColor;
    private boolean isLegendThemeGrayscale = false;

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
    private Switch elementGridController;

    @FXML
    public void initialize() {
        float[] colorArray = ColorSettings.getInstance().getModelColor();
        tempSelectedColor = Color.color(colorArray[0], colorArray[1], colorArray[2]);

        colorPicker.setValue(tempSelectedColor);
        updateColorDisplay(tempSelectedColor);
        legendCountInput.setText(String.valueOf(LegendUtils.getColorArraySize()));

        isLegendThemeGrayscale = ColorSettings.getInstance().isGrayscaleLegendTheme();

        elementGridController.setInitialState(
                local.getString(LocalizationController.COLOR_PICKER_BUNDLE, "legend-theme"),
                isLegendThemeGrayscale,
                this::onSwitchToggle
        );
        elementGridController.setLabelStyle("-fx-font-family: 'Geologica Roman'; -fx-font-size: 13px; -fx-text-fill: #0E0E0E;");
    }

    private void onSwitchToggle(boolean isOn) {
        isLegendThemeGrayscale = isOn;
        ColorSettings.getInstance().setGrayscaleLegendTheme(isLegendThemeGrayscale);
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

        int colorCount;
        try {
            colorCount = Integer.parseInt(legendCountInput.getText());
            if (colorCount < 2 || colorCount > 15) {
                new ErrorDialog(new ModelValidationException("legend-range"));
                return;
            }
        } catch (NumberFormatException e) {
            new ErrorDialog(new ModelValidationException("legend-format"));
            return;
        }

        LegendUtils.setColorArraySizeAndTheme(colorCount, isLegendThemeGrayscale);

        StressDisplayOption displayOption = StressController.getInstance().getStress().getDisplayOption();
        StressController.getInstance().processStressData(displayOption);

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        try {
            int colorCount = Integer.parseInt(legendCountInput.getText());
            if (colorCount >= 2 && colorCount <= 15) {
                LegendUtils.setColorArraySize(colorCount);
                StressDisplayOption displayOption = StressController.getInstance().getStress().getDisplayOption();
                StressController.getInstance().processStressData(displayOption);
            } else {
                new ErrorDialog(new ModelValidationException("legend-range"));
            }
        } catch (NumberFormatException e) {
            new ErrorDialog(new ModelValidationException("legend-format"));
        }
    }
}
