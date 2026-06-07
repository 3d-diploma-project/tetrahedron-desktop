package org.cmps.tetrahedron.view.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.InfoPanel;
import org.cmps.tetrahedron.view.Legend;
import org.cmps.tetrahedron.graphics.ModelView;

import java.util.ResourceBundle;

/**
 * Builds scene and adds to it all mouse event listeners.
 *
 * @author Vadim Startchak (VadimST04)
 * @since 1.0
 */
public class SceneController {

    private static final SceneController instance = new SceneController();

    private final Parent scene;

    private SceneController() {
        scene = buildScene();
    }

    public static Parent getScene() {
        return instance.scene;
    }

    private Parent buildScene() {
        HBox navbar = ResourceReader.readComponent("/view/Navbar.fxml", HBox.class);

        VBox instrumentSidebar = ResourceReader.readComponent("/view/LeftToolBar.fxml", VBox.class);
        Legend legend = new Legend();
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

        return new StackPane(new ModelView(), controls);
    }
}
