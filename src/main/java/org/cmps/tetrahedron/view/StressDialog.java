package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.Stress;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.Switch;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class StressDialog {

    private static final LocalizationController local = LocalizationController.getInstance();

    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> stressComponentComboBox;
    @FXML
    private Switch elementGridController;

    public static void showDialog(double x, double y) {
        Locale.setDefault(local.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/StressDialog.fxml", DialogPane.class,
                ResourceBundle.getBundle("i18n.stress-dialog"));

        DialogUtils.displayDialogOnLeft(pane, x, y);
    }

    @FXML
    public void initialize() {
        Stress stress = StressController.getInstance().getStress();
        StressDisplayOption option = stress.getDisplayOption();

        boolean isMises = option == StressDisplayOption.MISES;

        elementGridController.setInitialState(
                local.getString(LocalizationController.STRESS_DIALOG_BUNDLE, "mises-stress-label"),
                isMises,
                this::onSwitchToggle
        );
        elementGridController.setLabelStyle("-fx-font-family: 'Geologica Roman'; -fx-font-size: 13px; -fx-text-fill: #0E0E0E;");

        if (!isMises) {
            stressComponentComboBox.setValue(option.name().toLowerCase());
        } else {
            stressComponentComboBox.setValue(null);
            stressComponentComboBox.setPromptText(
                    local.getString(LocalizationController.STRESS_DIALOG_BUNDLE, "component-stress-choose"));
        }

        onSwitchToggle(isMises);
    }

    @FXML
    public void applyChanges() throws ModelValidationException {
        Stress stress = StressController.getInstance().getStress();
        if (!stressComponentComboBox.isDisabled()) {
            String selected = stressComponentComboBox.getValue();
            if (selected != null) {
                stress.setDisplayOption(StressDisplayOption.valueOf(selected.toUpperCase()));
            }
        } else {
            stress.setDisplayOption(StressDisplayOption.MISES);
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        StressController.getInstance().processStressData(stress.getDisplayOption());
        RightToolbar.getInstance().setStressDisplayOption(stress.getDisplayOption());
    }

    private void onSwitchToggle(boolean isOn) {
        stressComponentComboBox.setDisable(isOn);

        stressComponentComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox();
                    Label componentName = new Label(item);
                    componentName.setStyle("-fx-text-fill: #0E0E0E;");

                    Pane pane = new Pane();
                    HBox.setHgrow(pane, Priority.ALWAYS);
                    container.setAlignment(Pos.CENTER_LEFT);

                    SVGPath checkMark = ResourceReader.readComponent("/icon/Checkmark.fxml", SVGPath.class);
                    HBox.setMargin(checkMark, new Insets(0, 5, 0, 0));
                    checkMark.setVisible(java.util.Objects.equals(item, stressComponentComboBox.getValue()));

                    container.getChildren().addAll(componentName, pane, checkMark);
                    setGraphic(container);
                }
            }
        });
    }
}
