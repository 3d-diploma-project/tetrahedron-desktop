package org.cmps.tetrahedron.controller;

import javafx.scene.layout.*;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.*;
import javafx.scene.Scene;
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

        HBox navbar = new Navbar();

        VBox instrumentSidebar = ResourceReader.readComponent("/view/LeftToolBar.fxml", VBox.class);

        VBox rightToolbar = ResourceReader.readComponent("/view/RightToolbar.fxml", VBox.class);

        AnchorPane anchorPane = new AnchorPane(instrumentSidebar, rightToolbar);
        anchorPane.getStyleClass().add("main");
        VBox.setVgrow(anchorPane, Priority.ALWAYS);

        AnchorPane.setLeftAnchor(instrumentSidebar, 10d);
        AnchorPane.setTopAnchor(instrumentSidebar, 75d);
        AnchorPane.setRightAnchor(rightToolbar, 20d);
        AnchorPane.setTopAnchor(rightToolbar, 75d);

        LegendView legend = LegendView.getInstance();
        anchorPane.getChildren().add(legend);
        AnchorPane.setLeftAnchor(legend, 100d);
        AnchorPane.setTopAnchor(legend, 0.0);
        AnchorPane.setBottomAnchor(legend, 0.0);

        root.getChildren().addAll(navbar, anchorPane);

        InfoPanel infoPanel = InfoPanel.getInstance();
        AnchorPane absolutePane = new AnchorPane(infoPanel);
        AnchorPane.setBottomAnchor(infoPanel, 80d);
        VBox.setVgrow(absolutePane, Priority.ALWAYS);

        root.getChildren().add(absolutePane);
        return scene;
    }
}
