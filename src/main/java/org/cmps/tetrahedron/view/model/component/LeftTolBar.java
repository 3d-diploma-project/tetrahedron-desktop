package org.cmps.tetrahedron.view.model.component;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.enums.VerticeMoveMode;
import org.cmps.tetrahedron.view.model.ColorPickerDialog;
import org.cmps.tetrahedron.view.model.LegendDialog;

public class LeftTolBar {

    private final MouseController mouseController = MouseController.getInstance();
    private final ModelController modelController = ModelController.getInstance();

    @FXML
    private Button cursor;
    @FXML
    private Button topBottom;
    @FXML
    private Button leftRight;
    @FXML
    public Button colorPicker;
    @FXML
    public Button legendSettings;

    @FXML
    public void clickOnCursor() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.CURSOR);

        focus(cursor);
        unFocus(leftRight);
        unFocus(topBottom);
    }

    @FXML
    public void clickOnTopBottom() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.UP_DOWN);

        focus(topBottom);
        unFocus(leftRight);
        unFocus(cursor);
    }

    @FXML
    public void clickOnLeftRight() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.LEFT_RIGHT);

        focus(leftRight);
        unFocus(topBottom);
        unFocus(cursor);
    }

    @FXML
    public void clickOnColorPicker() {
        Bounds bounds = colorPicker.localToScreen(colorPicker.getBoundsInParent());
        ColorPickerDialog.showColorPicker(bounds.getMinX(), bounds.getMinY());
    }

    public void clickOnLegendSettings() {
        Bounds bounds = colorPicker.localToScreen(colorPicker.getBoundsInParent());
        LegendDialog.showDialog(bounds.getMinX(), bounds.getMinY());
    }

    @FXML
    public void clickOnDelete() {
        modelController.clearModel();
    }

    private void focus(Button button) {
        if (!button.getStyleClass().contains("button-selected")) {
            button.getStyleClass().add("button-selected");
        }

        Node graphic = button.getGraphic();
        if (graphic == null) {
            return;
        }

        if (!graphic.getStyleClass().contains("button-selected")) {
            graphic.getStyleClass().add("button-selected");
        }
    }

    private void unFocus(Button button) {
        button.getStyleClass().remove("button-selected");
        Node graphic = button.getGraphic();
        if (graphic == null) {
            return;
        }

        graphic.getStyleClass().removeAll("button-selected");
    }
}
