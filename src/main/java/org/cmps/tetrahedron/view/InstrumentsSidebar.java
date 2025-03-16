package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.enums.VerticeMoveMode;
import javafx.scene.control.Button;

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
    private Button deleteModel;

    @FXML
    public void clickOnCursor() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.CURSOR);

        focus(cursor);
        unFocus(leftRight);
        unFocus(topBottom);
        unFocus(deleteModel);
    }

    @FXML
    public void clickOnTopBottom() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.UP_DOWN);

        focus(topBottom);
        unFocus(leftRight);
        unFocus(cursor);
        unFocus(deleteModel);
    }

    @FXML
    public void clickOnLeftRight() {
        mouseController.setVerticalMoveMode(VerticeMoveMode.LEFT_RIGHT);

        focus(leftRight);
        unFocus(topBottom);
        unFocus(cursor);
        unFocus(deleteModel);
    }

    @FXML
    public void clickOnColorPicker() {
    }

    @FXML
    public void clickOnDelete() {
        focus(deleteModel);
        unFocus(cursor);
        unFocus(leftRight);
        unFocus(topBottom);

        modelController.clearModel();

        focus(cursor);
        unFocus(leftRight);
        unFocus(topBottom);
        unFocus(deleteModel);
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
