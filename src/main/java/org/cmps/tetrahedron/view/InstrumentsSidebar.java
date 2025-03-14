package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.enums.VerticeMoveMode;
import javafx.scene.control.Button;

public class InstrumentsSidebar {

    private final MouseController mouseController = MouseController.getInstance();

    @FXML
    private Button cursor;

    @FXML
    private Button topBottom;

    @FXML
    private Button leftRight;

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
