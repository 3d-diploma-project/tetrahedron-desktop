package com.example.demo1;

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
import javafx.scene.shape.Line;
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

public class HelloApplication extends Application{

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

    @Override
    public void start(Stage primaryStage) {

        Button loadVerticesButton = new Button("Load Vertices");
        Button loadFacesButton = new Button("Load Faces");
        Button createMeshButton = new Button("Create Mesh");

        loadVerticesButton.setOnAction(e -> loadVerticesFromFile());
        loadFacesButton.setOnAction(e -> loadFacesFromFile());
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

    private void loadVerticesFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Vertices File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                List<Float> vertexList = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    String[] values = line.split("\\s+");

                    for (int i = 1; i < values.length; i++) {
                        try {
                            vertexList.add(Float.parseFloat(values[i]));
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing number: " + values[i]);
                        }
                    }
                }
                vertices = new float[vertexList.size()];
                for (int i = 0; i < vertexList.size(); i++) {
                    vertices[i] = vertexList.get(i);
                }
                System.out.println("Vertices loaded successfully.");
                System.out.println("Vertices length is " + vertices.length);

                // Для отладки - выводим массив вершин
                for (float i : vertices) {
                    System.out.println(i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        texture = new float[vertices.length / 3 * 2];
        for (int i = 0; i < texture.length; i += 2) {
            texture[i] = 0;    // Значение u
            texture[i + 1] = 0; // Значение v
        }
        System.out.println("Textures array");
        System.out.println("Texture length is " + vertices.length);
        for (float i : texture) {
            System.out.println(i);
        }
    }

    private void loadFacesFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Faces File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                List<Integer> facesList = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    String[] values = line.split("\\s+");

                    // Пропускаем первое значение (индекс)
                    int[] indices = new int[values.length - 1];
                    for (int i = 1; i < values.length; i++) {
                        try {
                            indices[i - 1] = Integer.parseInt(values[i]) - 1;
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing number: " + values[i]);
                        }
                    }

                    addTriangleToFacesList(facesList, indices[0], indices[1], indices[2]);
                    addTriangleToFacesList(facesList, indices[0], indices[1], indices[3]);
                    addTriangleToFacesList(facesList, indices[1], indices[2], indices[3]);
                    addTriangleToFacesList(facesList, indices[0], indices[2], indices[3]);
                }

                faces = new int[facesList.size()];
                for (int i = 0; i < facesList.size(); i++) {
                    faces[i] = facesList.get(i);
                }
                System.out.println("Faces loaded successfully.");
                for (int i : faces) {
                    System.out.println(i);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addTriangleToFacesList(List<Integer> facesList, int index1, int index2, int index3) {
        facesList.add(index1);
        facesList.add(0);
        facesList.add(index2);
        facesList.add(0);
        facesList.add(index3);
        facesList.add(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }

}