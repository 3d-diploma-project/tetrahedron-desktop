package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
        Stage stage = new Stage();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();

            float[] colorArray = new float[]{
                    (float) selectedColor.getRed(),
                    (float) selectedColor.getGreen(),
                    (float) selectedColor.getBlue()
            };

            ColorSettings.getInstance().setModelColor(colorArray);
            ColorSettings.getInstance().setColoredInSelectedColor(true);
            stage.close();
        });

        StackPane root = new StackPane(colorPicker);
        Scene scene = new Scene(root, 100, 40);
        stage.setScene(scene);
        stage.show();
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
