package org.cmps.tetrahedron.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    public static final String COORDINATES_FILE_NAME = "models/таблиця координат.txt";
    public static final String INDEXES_FILE_NAME = "models/матриця індексів.txt";
    public static final String STRESS_FILE_NAME = "models/stress.txt";

    private static final boolean WITH_INDICES = false;

    private static final Map<Integer, float[]> vertices = readVerticesCoordinates();
    private static final List<float[][]> faces = readIndexesAndConvertToFaces(vertices);


    private static float minStress = Float.MAX_VALUE;
    private static float maxStress = Float.MIN_VALUE;
    private static final float[] stress = readStress();

    public static Map<Integer, float[]> getVertices() {
        return vertices;
    }

    public static List<float[][]> getFaces() {
        return faces;
    }

    public static float[] getStress() {
        return stress;
    }

    public static float getMinStress() {
        return minStress;
    }

    public static float getMaxStress() {
        return maxStress;
    }

    private static Map<Integer, float[]> readVerticesCoordinates() {
        Locale.setDefault(US);

        int i = 1;
        try (Scanner fid = new Scanner(new File(COORDINATES_FILE_NAME))) {
            Map<Integer, float[]> coordinates = new HashMap<>();

            while (fid.hasNext()) {
                int index;
                if (WITH_INDICES) {
                    index = fid.nextInt();
                } else {
                    index = i++;
                }

                coordinates.put(index, new float[]{fid.nextFloat(), fid.nextFloat(), fid.nextFloat()});
            }

            return coordinates;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<float[][]> readIndexesAndConvertToFaces(Map<Integer, float[]> verticesCoordinates) {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(new File(INDEXES_FILE_NAME))) {
            List<float[][]> faces = new ArrayList<>();

            while (fid.hasNext()) {
                // reading index of a tetrahedron
                if (WITH_INDICES) {
                    fid.nextInt();
                }

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

    private static float[] readStress() {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(new File(STRESS_FILE_NAME))) {
            float[] stress = new float[getFaces().size() / 4];
            int i = 0;

            while (fid.hasNext()) {
                if (WITH_INDICES) {
                    fid.nextInt();
                    stress[i] = StressUtils.misesStress(fid.nextFloat(),fid.nextFloat(), fid.nextFloat(), fid.nextFloat(), fid.nextFloat(), fid.nextFloat());
                } else {
                    stress[i] = fid.nextFloat();
                }

                if (stress[i] < minStress) {
                    minStress = stress[i];
                }

                if (stress[i] > maxStress) {
                    maxStress = stress[i];
                }

                i++;
            }

            return stress;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
