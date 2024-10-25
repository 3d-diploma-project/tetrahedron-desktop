package org.cmps.tetrahedron.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    public static final String COORDINATES_FILE_NAME = "models/Vertices (model 3).txt";
    public static final String INDEXES_FILE_NAME = "models/Indices (model 3).txt";

    public static Map<Integer, float[]> readVerticesCoordinates() {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(new File(COORDINATES_FILE_NAME))) {
            Map<Integer, float[]> coordinates = new HashMap<>();

            while (fid.hasNext()) {
                coordinates.put(fid.nextInt(), new float[]{fid.nextFloat(), fid.nextFloat(), fid.nextFloat()});
            }

            return coordinates;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<float[][]> readIndexesAndConvertToFaces(Map<Integer, float[]> verticesCoordinates) {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(new File(INDEXES_FILE_NAME))) {
            List<float[][]> faces = new ArrayList<>();

            while (fid.hasNext()) {
                // reading index of a tetrahedron
                fid.nextInt();

                float[] vertex1 = verticesCoordinates.get(fid.nextInt());
                float[] vertex2 = verticesCoordinates.get(fid.nextInt());
                float[] vertex3 = verticesCoordinates.get(fid.nextInt());
                float[] vertex4 = verticesCoordinates.get(fid.nextInt());

                faces.add(new float[][]{vertex1, vertex2, vertex3});
                faces.add(new float[][]{vertex1, vertex2, vertex4});
                faces.add(new float[][]{vertex1, vertex4, vertex3});
                faces.add(new float[][]{vertex4, vertex2, vertex3});
            }

            return faces;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
