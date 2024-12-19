package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.model.Stress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    private static final int VERTICES_WITH_INDICES = 4;
    private static final int FACE_WITH_INDICES = 5;

    public static Map<Integer, float[]> readVertices(File coordinatesTableFile) {
        Locale.setDefault(US);

        int i = 1;
        try (Scanner fid = new Scanner(coordinatesTableFile)) {
            Map<Integer, float[]> coordinates = new HashMap<>();

            while (fid.hasNextLine()) {
                String[] elements = fid.nextLine().trim().split("\\s+");
                int index, startIndex;

                if (elements.length == VERTICES_WITH_INDICES) {
                    index = Integer.parseInt(elements[0]);
                    startIndex = 1;
                } else {
                    index = i++;
                    startIndex = 0;
                }

                float[] vertex = new float[3];
                for (int j = startIndex; j < elements.length; j++) {
                    vertex[j - startIndex] = Float.parseFloat(elements[j]);
                }

                coordinates.put(index, vertex);
            }

            return coordinates;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<float[][]> readIndexesAndConvertToFaces(File indicesMatrix,
                                                               Map<Integer, float[]> verticesCoordinates) {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(indicesMatrix)) {
            List<float[][]> faces = new ArrayList<>();
            while (fid.hasNextLine()) {
                String[] elements = fid.nextLine().trim().split("\\s+");
                float[][] tetrahedron = new float[4][];
                int startIndex;

                startIndex = elements.length == FACE_WITH_INDICES ? 1 : 0;
                for (int j = startIndex; j < elements.length; j++){
                    tetrahedron[j - startIndex] = verticesCoordinates.get(Integer.parseInt(elements[j]));
                }

                float[] vertex1 = tetrahedron[0];
                float[] vertex2 = tetrahedron[1];
                float[] vertex3 = tetrahedron[2];
                float[] vertex4 = tetrahedron[3];

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

    public static Stress readStress(File stressData) {
        Locale.setDefault(US);

        Stress stressModel = new Stress();

        try (Scanner fid = new Scanner(stressData)) {
            List<Float> stress = new ArrayList<>();

            while (fid.hasNext()) {
                float stressValue = fid.nextFloat();

                if (stressValue < stressModel.getMinStress()) {
                    stressModel.setMinStress(stressValue);
                } else if (stressValue > stressModel.getMaxStress()) {
                    stressModel.setMaxStress(stressValue);
                }

                stress.add(stressValue);
            }

            stressModel.setStress(stress);
            return stressModel;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
