package org.cmps.tetrahedron.controller;

import javafx.scene.input.MouseButton;
import org.cmps.tetrahedron.Launcher;
import org.cmps.tetrahedron.components.InfoPanel;
import org.cmps.tetrahedron.components.InstrumentsSidebar;
import org.cmps.tetrahedron.components.Navbar;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.config.WindowProperties;

import java.util.Objects;

/**
 * Builds scene and adds to it all mouse event listeners.
 *
 * @author Vadim Startchak (VadimST04)
 * @since 1.0
 */
public class SceneController {

    private static final SceneController instance = new SceneController();

    private final Scene scene;

    private InfoPanel infoPanel;

    private float zoomFactor = 1.0f;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    private SceneController() {
        scene = buildScene();
        setupEventListeners();
    }

    public static SceneController getInstance() {
        return instance;
    }

    public static Scene getScene() {
        return instance.scene;
    }

    private Scene buildScene() {
        VBox root = new VBox();
        root.getStyleClass().add("model-view-page");

        Scene scene = new Scene(root, WindowProperties.getLogicalWidth(), WindowProperties.getLogicalHeight());
        scene.getStylesheets().add(Objects.requireNonNull(SceneController.class.getResource("/styles.css")).toExternalForm());

        HBox navbar = new Navbar(scene);
        HBox main = new HBox();
        main.getStyleClass().add("main");

        VBox instrumentSidebar = new InstrumentsSidebar();
        instrumentSidebar.getStyleClass().add("instrument-sidebar");

        VBox experience = new VBox();
        experience.getStyleClass().add("experience");

        main.getChildren().addAll(instrumentSidebar, experience);

        infoPanel = new InfoPanel();
        root.getChildren().addAll(navbar, main, infoPanel);

        return scene;
    }

    private void setupEventListeners() {
        scene.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                return;
            }
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            deltaX += (x - lastMouseX);
            deltaY += (y - lastMouseY);
            lastMouseX = x;
            lastMouseY = y;
        });

        scene.setOnMousePressed((mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                return;
            }
            lastMouseX = (int) mouseEvent.getX();
            lastMouseY = (int) mouseEvent.getY();
        }));

        scene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                return;
            }
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            System.out.println("Cursor X: " + x);
            System.out.println("Cursor Y: " + y);

            VertexInfoController.getInstance().setDisplayInfo(infoPanel::setText);
            VertexInfoController.getInstance().setClickCoords(x, y);
        });

        scene.setOnScroll(scrollEvent -> {
            if (scrollEvent.isShiftDown()) {
                return;
            }
            zoomFactor += (float) scrollEvent.getDeltaY() / 100;
            zoomFactor = Math.max(1f, Math.min(zoomFactor, 100.0f));
        });
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }
}
