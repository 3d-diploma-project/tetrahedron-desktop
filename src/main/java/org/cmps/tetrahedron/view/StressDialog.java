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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.model.StressViewSettings;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.Switch;

import java.util.Locale;
import java.util.ResourceBundle;

public class StressDialog {

    private static final LocalizationController local = LocalizationController.getInstance();
    private static final StressViewSettings stressViewSettings = StressViewSettings.getInstance();

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
        elementGridController.setInitialState(local.getString(LocalizationController.STRESS_DIALOG_BUNDLE, "mises-stress-label"),
                stressViewSettings.isShowMises(),
                this::onSwitchToggle);
        elementGridController.setLabelStyle("-fx-font-family: 'Geologica Roman'; -fx-font-size: 13px; -fx-text-fill: #0E0E0E;");

        String savedComponent = stressViewSettings.getSelectedComponent();
        if (savedComponent != null && !stressViewSettings.isShowMises()) {
            stressComponentComboBox.setValue(savedComponent);
        } else {
            stressComponentComboBox.setValue(null);
            stressComponentComboBox.setPromptText(
                    local.getString(LocalizationController.STRESS_DIALOG_BUNDLE, "component-stress-choose"));
        }

        onSwitchToggle(stressViewSettings.isShowMises());
    }

    @FXML
    public void applyChanges() {
        if (!stressViewSettings.isShowMises()) {
            stressViewSettings.setSelectedComponent(stressComponentComboBox.getValue());
        } else {
            stressViewSettings.setSelectedComponent(null);
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void onSwitchToggle(boolean isOn) {
        stressViewSettings.setShowMises(isOn);

        if (isOn) {
            stressComponentComboBox.setDisable(true);
            stressComponentComboBox.setValue(null);
            stressComponentComboBox.getSelectionModel().clearSelection();
        } else {
            stressComponentComboBox.setDisable(false);
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


}
