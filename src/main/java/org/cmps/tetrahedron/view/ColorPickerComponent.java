package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.ColorEditor;
import org.cmps.tetrahedron.view.component.Switch;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.cmps.tetrahedron.enums.LegendTheme.GREY;
import static org.cmps.tetrahedron.enums.LegendTheme.RAINBOW;

public class ColorPickerComponent {

    private static final LocalizationController local = LocalizationController.getInstance();

    private boolean isLegendThemeGrayscale = false;

    @FXML
    private ColorEditor modelColorPickerController;
    @FXML
    private ColorEditor backgroundColorPickerController;

    @FXML
    private TextField legendCountInput;

    @FXML
    public Button saveButton;

    @FXML
    private Switch elementGridController;

    public static void showColorPicker(double x, double y) {
        Locale.setDefault(local.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/ColorPicker.fxml", DialogPane.class,
                                                       ResourceBundle.getBundle("i18n.color-picker"));

        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    @FXML
    public void initialize() {
        ColorSettings colorSettings = ColorSettings.getInstance();

        modelColorPickerController.initialize("model-color", colorSettings.getModelColor());
        backgroundColorPickerController.initialize("background-color", colorSettings.getBackgroundColor());

        legendCountInput.setText(String.valueOf(LegendData.getInstance().getColorsCount()));
        isLegendThemeGrayscale = LegendData.getInstance().getTheme() == GREY;

        elementGridController.setInitialState(
                local.getString(LocalizationController.COLOR_PICKER_BUNDLE, "legend-theme"),
                isLegendThemeGrayscale,
                this::onSwitchToggle
        );
        elementGridController.setLabelStyle(
                "-fx-font-family: 'Geologica Roman'; -fx-font-size: 13px; -fx-text-fill: #0E0E0E;");
    }

    private void onSwitchToggle(boolean isOn) {
        isLegendThemeGrayscale = isOn;
    }

    public void applyColor(ActionEvent actionEvent) {
        ColorSettings.getInstance().setModelColor(modelColorPickerController.getColor());
        if (modelColorPickerController.isWasUpdated()) {
            ColorSettings.getInstance().setColoredInSelectedColor(true);
        }
        ColorSettings.getInstance().setBackgroundColor(backgroundColorPickerController.getColor());

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        if (modelColorPickerController.isWasUpdated()) {
            return;
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

        LegendData.getInstance().updateColorsCount(colorCount);
        LegendData.getInstance().updateTheme(isLegendThemeGrayscale ? GREY : RAINBOW);

        StressDisplayOption displayOption = StressController.getInstance().getStress().getDisplayOption();
        StressController.getInstance().processStressData(displayOption);
    }
}
