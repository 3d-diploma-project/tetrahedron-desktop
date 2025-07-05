package org.cmps.tetrahedron.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.cmps.tetrahedron.controller.DeformationController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.Switch;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class DeformationDialog {

    private static final DeformationController deformationController = DeformationController.getInstance();
    private static final LocalizationController local = LocalizationController.getInstance();

    @FXML
    private ComboBox<CheckBox> displacementComponentComboBox;

    @FXML
    private TextField scaleInput;

    @FXML
    private Button saveButton;

    @FXML
    private Switch totalDisplacementController;

    private boolean isGlobalMode;
    private static Set<String> selectedComponents = new HashSet<>();

    public static void showDialog(double x, double y) {
        Locale.setDefault(local.getCurrentLocale());
        DialogPane pane = ResourceReader.readComponent("/view/DeformationScaleDialog.fxml", DialogPane.class,
                                                       ResourceBundle.getBundle(
                                                               LocalizationController.DEFORMATION_DIALOG_BUNDLE));

        DialogUtils.displayDialogOnLeft(pane, x, y);
    }

    @FXML
    public void initialize() {
        float currentScale = deformationController.getCurrentScale();
        scaleInput.setText(String.valueOf(currentScale));

        // Prompt text is not displayed when value is reset (https://bugs.openjdk.org/browse/JDK-8296653)
        // As we use only prompt text, we don't need a value to be edited.
        // To disable editing we bind the value property to a property which will never be edited.
        displacementComponentComboBox.valueProperty().bind(new SimpleObjectProperty<>());
        displacementComponentComboBox.setCellFactory(new CellFactory());

        if (deformationController.getDeformationComponents() == null) {
            selectedComponents.add("x");
        } else {
            selectedComponents = deformationController.getDeformationComponents();
        }
        displacementComponentComboBox.setPromptText(buildPromptText());

        isGlobalMode = deformationController.getDeformationComponents() == null;
        totalDisplacementController.setInitialState(
                local.getString(LocalizationController.DEFORMATION_DIALOG_BUNDLE, "global-deformation-label"),
                isGlobalMode,
                this::onSwitchToggle
        );
        onSwitchToggle(isGlobalMode);
    }

    @FXML
    private void applyChanges() {
        try {
            float scale = Float.parseFloat(scaleInput.getText());

            if (!isGlobalMode) {
                ModelController.getInstance().clearDisplacement();
                deformationController.applyDeformationScale(scale, selectedComponents);
            } else {
                ModelController.getInstance().clearDisplacement();
                deformationController.applyDeformationScale(scale, null);
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            scaleInput.setStyle("-fx-background-color: 'FAE1E1';");
        }
    }

    private void onSwitchToggle(boolean isOn) {
        isGlobalMode = isOn;
        displacementComponentComboBox.setDisable(isOn);
    }

    private String buildPromptText() {
        return String.join(", ", selectedComponents);
    }

    private class CellFactory implements Callback<ListView<CheckBox>, ListCell<CheckBox>> {

        @Override
        public ListCell<CheckBox> call(ListView<CheckBox> tListView) {
            ListCell<CheckBox> listCell = new DeformationComponentCell();

            listCell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                boolean wasSelected = listCell.getItem().selectedProperty().get();
                String component = listCell.getItem().getText();
                listCell.getItem().selectedProperty().set(!wasSelected);

                if (selectedComponents.contains(component)) {
                    selectedComponents.remove(component);
                } else {
                    selectedComponents.add(component);
                }

                if (selectedComponents.isEmpty()) {
                    displacementComponentComboBox.setPromptText("-");
                } else {
                    displacementComponentComboBox.setPromptText(buildPromptText());
                }
            });

            return listCell;
        }
    }

    private class DeformationComponentCell extends ListCell<CheckBox> {
        @Override
        protected void updateItem(CheckBox item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox container = new HBox();
                Label componentName = new Label(item.getText());
                componentName.setStyle("-fx-text-fill: #0E0E0E;");

                Pane pane = new Pane();
                HBox.setHgrow(pane, Priority.ALWAYS);
                container.setAlignment(Pos.CENTER_LEFT);

                SVGPath checkMark = ResourceReader.readComponent("/icon/Checkmark.fxml", SVGPath.class);
                HBox.setMargin(checkMark, new Insets(0, 5, 0, 0));
                checkMark.visibleProperty().bind(item.selectedProperty());
                item.setSelected(selectedComponents.contains(item.getText()));

                container.getChildren().addAll(componentName, pane, checkMark);
                setGraphic(container);
            }
        }
    }
}
