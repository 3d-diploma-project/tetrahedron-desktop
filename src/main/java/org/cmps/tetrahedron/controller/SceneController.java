package org.cmps.tetrahedron.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.InfoPanel;
import org.cmps.tetrahedron.view.LegendView;
import org.cmps.tetrahedron.graphics.ModelView;

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
        infoPanel.setPadding(new Insets(0, 0, 30, 0));

        VBox controls = new VBox(navbar, mainBlock, infoPanel);
        controls.getStyleClass().add("model-view-page");

        StackPane root = new StackPane(new ModelView(), controls);

        Scene scene = new Scene(root);
        scene.getStylesheets()
             .add(Objects.requireNonNull(SceneController.class.getResource("/styles.css")).toExternalForm());
        return scene;
    }
}
