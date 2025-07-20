package org.cmps.tetrahedron;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.ModelFilesPicker;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.controller.SceneController;

import java.io.File;
import java.util.Objects;

/**
 * Creates a program window and inits all components (LWJGL and JavaFX parts).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Tetrahedron extends Application {

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setScene(SceneController.getScene());

        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            MouseController.getInstance().mousePressed(e);
        });
        primaryStage.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            MouseController.getInstance().mouseReleased(e);
        });
        primaryStage.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            MouseController.getInstance().mouseDragged(e);
        });
        primaryStage.addEventFilter(ScrollEvent.SCROLL, e -> {
            MouseController.getInstance().mouseWheelMoved(e);
        });

        primaryStage.setTitle("Tetrahedron");

        ImageView logo = ResourceReader.imageReader("/logo.png");
        primaryStage.getIcons().add(logo.getImage());

        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMaximized(true);

        primaryStage.show();
        primaryStage.requestFocus();

        if (Objects.equals(System.getProperty("debug"), "true")) {
            initModelIfInDebug();
        } else {
            Platform.runLater(ModelFilesPicker::openDialogWindow);
        }
    }

    private void initModelIfInDebug() {
        try {
            ModelController.getInstance().initModelData(new File("models/Vertices (model 1).txt"), new File("models/Indices (model 1).txt"));
        } catch (ModelValidationException | InternalValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
