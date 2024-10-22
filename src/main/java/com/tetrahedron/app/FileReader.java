package com.tetrahedron.app;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public float[] loadVerticesFromFile() {
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
                float[] vertices = new float[vertexList.size()];
                for (int i = 0; i < vertexList.size(); i++) {
                    vertices[i] = vertexList.get(i);
                }
                return vertices;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int[] loadFacesFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Faces File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                List<Integer> facesList = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    String[] values = line.split("\\s+");

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

                int[] faces = new int[facesList.size()];
                for (int i = 0; i < facesList.size(); i++) {
                    faces[i] = facesList.get(i);
                }
                return faces;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void addTriangleToFacesList(List<Integer> facesList, int index1, int index2, int index3) {
        facesList.add(index1);
        facesList.add(0);
        facesList.add(index2);
        facesList.add(0);
        facesList.add(index3);
        facesList.add(0);
    }
}
