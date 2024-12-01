package org.cmps.tetrahedron.controller;

import org.cmps.tetrahedron.view.InfoPanel;
import org.cmps.tetrahedron.view.InstrumentsSidebar;
import org.cmps.tetrahedron.view.Navbar;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.components.RightToolbar;
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

    private SceneController() {
        scene = buildScene();
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

        VBox rightToolbar = new RightToolbar();
        main.getChildren().addAll(instrumentSidebar, rightToolbar);
        rightToolbar.toFront();

        root.getChildren().addAll(navbar, main, InfoPanel.getInstance());

        return scene;
    }
}
