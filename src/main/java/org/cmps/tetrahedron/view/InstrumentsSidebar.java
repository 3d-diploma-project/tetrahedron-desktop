package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import org.cmps.tetrahedron.controller.ModelController;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.enums.VerticeMoveMode;
import javafx.scene.control.Button;
import org.cmps.tetrahedron.model.ColorSettings;

public class InstrumentsSidebar {

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
    private Button deleteModel;

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
        ColorPickerComponent.showColorPicker(bounds.getMinX(), bounds.getMinY());
    }

    @FXML
    public void clickOnDelete() {
        modelController.clearModel();
    }

    private void focus(Button button) {
        if(!button.getStyleClass().contains("button-selected")) {
            button.getStyleClass().add("button-selected");
        }

        Node graphic = button.getGraphic();
        if (graphic == null) {
            return;
        }

        if(!graphic.getStyleClass().contains("button-selected")) {
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
