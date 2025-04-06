package org.cmps.tetrahedron.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.config.WindowProperties;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.InfoPanel;
import org.cmps.tetrahedron.view.LegendView;

import java.util.Objects;
import java.util.ResourceBundle;

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
        HBox navbar = ResourceReader.readComponent("/view/Navbar.fxml", HBox.class);

        VBox instrumentSidebar = ResourceReader.readComponent("/view/LeftToolBar.fxml", VBox.class);

        LegendView legend = LegendView.getInstance();
        Pane pane = new Pane();
        HBox.setHgrow(pane, Priority.ALWAYS);
        VBox rightToolbar = ResourceReader.readComponent("/view/RightToolbar.fxml", VBox.class,
                                                         ResourceBundle.getBundle("i18n.right-toolbar"));

        HBox mainBlock = new HBox(30, instrumentSidebar, legend, pane, rightToolbar);
        mainBlock.setPadding(new Insets(0, 15, 0, 15));
        mainBlock.setAlignment(Pos.CENTER);
        mainBlock.setFillHeight(false);
        VBox.setVgrow(mainBlock, Priority.ALWAYS);

        InfoPanel infoPanel = InfoPanel.getInstance();
        infoPanel.setPadding(new Insets(0, 0, 50, 0));

        VBox root = new VBox(navbar, mainBlock, infoPanel);
        root.getStyleClass().add("model-view-page");

        Scene scene = new Scene(root, WindowProperties.getLogicalWidth(), WindowProperties.getLogicalHeight());
        scene.getStylesheets()
             .add(Objects.requireNonNull(SceneController.class.getResource("/styles.css")).toExternalForm());
        return scene;
    }
}
