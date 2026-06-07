package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.view.ModelFilesPicker;
import org.cmps.tetrahedron.view.model.SceneController;

import java.io.File;
import java.util.Objects;


/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class HomePageController {

    @FXML
    private Button meshButton;
    @FXML
    private Button modelButton;
    @FXML
    private Label meshLabel;
    @FXML
    private Label modelLabel;

    public void onClick() {
        Stage stage = (Stage) meshButton.getScene().getWindow();
        stage.getScene().setRoot(SceneController.getScene());

        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            MouseController.getInstance().mousePressed(e);
        });
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            MouseController.getInstance().mouseReleased(e);
        });
        stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            MouseController.getInstance().mouseDragged(e);
        });
        stage.addEventFilter(ScrollEvent.SCROLL, e -> {
            MouseController.getInstance().mouseWheelMoved(e);
        });

        if (Objects.equals(System.getProperty("debug"), "true")) {
            initModelIfInDebug();
        } else {
            Platform.runLater(ModelFilesPicker::openDialogWindow);
        }
    }

    private void initModelIfInDebug() {
        try {
            ModelController.getInstance()
                           .initModelData(new File("models/Vertices (model 1).txt"),
                                          new File("models/Indices (model 1).txt"));
        } catch (ModelValidationException | InternalValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
