package com.tetrahedron.app.htmlApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class ViewScene extends Application {

//    @Override
//    public void start(Stage stage) throws Exception {
//        Button button1 = new Button("Button 1");
//        Button button2 = new Button("Button 2");
//
//        VBox vbox = new VBox(button1, button2);
//
//        Scene scene = new Scene(vbox);
//
////        vbox.getStylesheets().add("style1/button-styles.css");
//
//        stage.setScene(scene);
//        stage.setFullScreen(true);
//        stage.setTitle("Test Application");
//        stage.show();
//    }

//    @Override
//    public void start(Stage stage) throws Exception {
//        HTMLEditor htmlEditor = new HTMLEditor();
//
//        VBox vBox = new VBox(htmlEditor);
//        Scene scene = new Scene(vBox);
//
//        stage.setScene(scene);
//        stage.setTitle("Test Application");
//        stage.show();
//    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        VBox root = new VBox();
        root.getStyleClass().add("model-view-page");

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        HBox navbar = new Navbar(scene);
        VBox experience = new VBox();
        experience.getStyleClass().add("experience");

        root.getChildren().addAll(navbar, experience);

        stage.setScene(scene);
        stage.setTitle("Tetrahedron Model View");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
