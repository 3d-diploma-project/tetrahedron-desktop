package org.cmps.tetrahedron.view.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import org.cmps.tetrahedron.graphics.ModelView;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.model.component.InfoPanel;
import org.cmps.tetrahedron.view.model.component.Legend;

import java.util.ResourceBundle;

/**
 * Page used to display model and calculation results (ex. stress, deformation, etc.)
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class ModelPage extends StackPane {

    public ModelPage() {
        HBox navbar = ResourceReader.readComponent("/view/common/Navbar.fxml", HBox.class);

        VBox instrumentSidebar = ResourceReader.readComponent("/view/model/component/LeftToolBar.fxml", VBox.class);
        Legend legend = new Legend();
        Pane pane = new Pane();
        HBox.setHgrow(pane, Priority.ALWAYS);
        VBox rightToolbar = ResourceReader.readComponent("/view/model/component/RightToolbar.fxml", VBox.class,
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

        this.getChildren().addAll(new ModelView(), controls);
    }
}
