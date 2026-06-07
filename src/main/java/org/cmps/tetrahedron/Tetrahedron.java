package org.cmps.tetrahedron;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.utils.ResourceReader;

import java.util.Objects;

/**
 * Creates a program window and inits all components (LWJGL and JavaFX parts).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Tetrahedron extends Application {

    @Getter
    private static Stage primaryStage;

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Tetrahedron.primaryStage = primaryStage;
        Scene scene = new Scene(Router.getInstance().getHomePage());
        scene.getStylesheets()
             .add(Objects.requireNonNull(Tetrahedron.class.getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.setTitle("Tetrahedron");

        ImageView logo = ResourceReader.imageReader("/logo.png");
        primaryStage.getIcons().add(logo.getImage());

        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMaximized(true);

        primaryStage.show();
        primaryStage.requestFocus();
    }
}
