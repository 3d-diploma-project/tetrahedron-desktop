package org.cmps.tetrahedron.router;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;
import org.cmps.tetrahedron.Tetrahedron;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.graphics.ModelView;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.InfoPanel;
import org.cmps.tetrahedron.view.Legend;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Router {

    @Getter
    private static final Router instance = new Router();
    private static final LocalizationController locale = LocalizationController.getInstance();

    private Page currentPage = Page.HOME;

    public void openPage(Page page) {
        currentPage = page;
        Parent root = getPage(page);

        Stage stage = Tetrahedron.getPrimaryStage();
        stage.getScene().setRoot(root);

        if (page == Page.MODEL) {
            stage.addEventFilter(MouseEvent.MOUSE_PRESSED,
                                 MouseController.getInstance()::mousePressed);
            stage.addEventFilter(MouseEvent.MOUSE_RELEASED,
                                 MouseController.getInstance()::mouseReleased);
            stage.addEventFilter(MouseEvent.MOUSE_DRAGGED,
                                 MouseController.getInstance()::mouseDragged);
            stage.addEventFilter(ScrollEvent.SCROLL,
                                 MouseController.getInstance()::mouseWheelMoved);
        }
    }

    public void reloadCurrentPage() {
        Tetrahedron.getPrimaryStage().getScene().setRoot(getPage(currentPage));
    }

    public Parent getHomePage() {
        Locale.setDefault(locale.getCurrentLocale());

        return ResourceReader.readComponent("/view/home/HomePage.fxml", VBox.class,
                                            ResourceBundle.getBundle("i18n.home"));
    }

    private Parent getPage(Page page) {
        return switch (page) {
            case HOME -> getHomePage();
            case MODEL -> getModelPage();
            case MESH -> getModelPage();
        };
    }

    private Parent getModelPage() {
        Locale.setDefault(locale.getCurrentLocale());
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
