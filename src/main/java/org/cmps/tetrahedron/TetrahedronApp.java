package org.cmps.tetrahedron;

import org.cmps.tetrahedron.components.InstrumentsSidebar;
import org.cmps.tetrahedron.components.Navbar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class TetrahedronApp extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.getStyleClass().add("model-view-page");

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        HBox navbar = new Navbar(scene);
        HBox main = new HBox();
        main.getStyleClass().add("main");

        VBox instrumentSidebar = new InstrumentsSidebar();
        instrumentSidebar.getStyleClass().add("instrument-sidebar");

        VBox experience = new VBox();
        experience.getStyleClass().add("experience");

        main.getChildren().addAll(instrumentSidebar, experience);

        root.getChildren().addAll(navbar, main);

        stage.setScene(scene);
        stage.setTitle("Tetrahedron Model View");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
