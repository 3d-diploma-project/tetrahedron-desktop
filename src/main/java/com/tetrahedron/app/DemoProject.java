package com.tetrahedron.app;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DemoProject extends Application{

    private float[] vertices;
    private int[] faces;
    private float[] texture;

    private static final float WIDTH = 1200;
    private static final float HEIGHT = 700;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private FileReader fileReader = new FileReader();

    @Override
    public void start(Stage primaryStage) {

        Button loadVerticesButton = new Button("Load Vertices");
        Button loadFacesButton = new Button("Load Faces");
        Button createMeshButton = new Button("Create Mesh");

        loadVerticesButton.setOnAction(e -> loadVertices());
        loadFacesButton.setOnAction(e -> loadFaces());
        createMeshButton.setOnAction(e -> createMeshView(primaryStage));

        VBox layout = new VBox(10, loadVerticesButton, loadFacesButton, createMeshButton);
        layout.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mesh Loader");
        primaryStage.show();
    }

    private void createMeshView(Stage primaryStage) {
        if (vertices == null || faces == null) {
            System.out.println("Please load both vertices and faces files.");
            return;
        }

        MeshView mesh = getMeshView();
        SmartGroup group = new SmartGroup();
        group.getChildren().addAll(mesh);

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(WIDTH / 2);
        pointLight.setTranslateY(-200);
        pointLight.setTranslateZ(-300);

        AmbientLight ambientLight = new AmbientLight(Color.rgb(255, 255, 255, 0.4));
        group.getChildren().addAll(pointLight, ambientLight);

        Camera camera = new PerspectiveCamera(false);
        camera.setNearClip(0.0001);  // Установить ближнюю плоскость срезки
//        camera.setFarClip(3000);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.LIGHTGRAY);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1300);

        initMouseControl(group, scene, primaryStage);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case E:
                    group.translateZProperty().set(group.getTranslateZ() + 5);
                    break;
                case Q:
                    group.translateZProperty().set(group.getTranslateZ() - 5);
                    break;
                case S:
                    group.translateYProperty().set(group.getTranslateY() + 1);
                    break;
                case W:
                    group.translateYProperty().set(group.getTranslateY() - 1);
                    break;
                case D:
                    group.translateXProperty().set(group.getTranslateX() + 1);
                    break;
                case A:
                    group.translateXProperty().set(group.getTranslateX() - 1);
                    break;
                case UP:
                    group.rotateByX(10);
                    break;
                case DOWN:
                    group.rotateByX(-10);
                    break;
                case RIGHT:
                    group.rotateByY(10);
                    break;
                case LEFT:
                    group.rotateByY(-10);
                    break;
            }
        });

        primaryStage.setScene(scene);
    }

    public MeshView getMeshView() {
        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(vertices);
        mesh.getTexCoords().addAll(texture);
        mesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(mesh);
        meshView.setMaterial(new PhongMaterial(Color.YELLOW));
        meshView.setCullFace(CullFace.NONE);

        return meshView;
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    private void loadVertices() {
        vertices = fileReader.loadVerticesFromFile();
        if (vertices != null) {
            texture = new float[vertices.length / 3 * 2];
            for (int i = 0; i < texture.length; i += 2) {
                texture[i] = 0;    // Значение u
                texture[i + 1] = 0; // Значение v
            }
            System.out.println("Vertices loaded successfully.");
        }
    }

    private void loadFaces() {
        faces = fileReader.loadFacesFromFile();
        if (faces != null) {
            System.out.println("Faces loaded successfully.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
