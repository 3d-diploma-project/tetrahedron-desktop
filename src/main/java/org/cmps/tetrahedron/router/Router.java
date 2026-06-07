package org.cmps.tetrahedron.router;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.cmps.tetrahedron.Tetrahedron;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.model.ModelPage;

import java.util.Locale;
import java.util.ResourceBundle;

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
        return new ModelPage();
    }
}
