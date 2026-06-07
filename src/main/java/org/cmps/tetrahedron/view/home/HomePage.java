package org.cmps.tetrahedron.view.home;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Builds scene for home page.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class HomePage {

    private static final LocalizationController locale = LocalizationController.getInstance();

    public static Parent getScene() {
        return buildScene();
    }

    private static Parent buildScene() {
        Locale.setDefault(locale.getCurrentLocale());
        VBox home = ResourceReader.readComponent("/view/home/HomePage.fxml", VBox.class,
                                                 ResourceBundle.getBundle("i18n.home"));

        return home;
    }
}
