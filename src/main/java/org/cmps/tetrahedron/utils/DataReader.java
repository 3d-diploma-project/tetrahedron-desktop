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
                int index;
                float x, y, z;

                if (elements.length == VERTICES_WITH_INDICES) {
                    index = Integer.parseInt(elements[0]);
                    x = Float.parseFloat(elements[1]);
                    y = Float.parseFloat(elements[2]);
                    z = Float.parseFloat(elements[3]);
                } else {
                    index = i++;
                    x = Float.parseFloat(elements[0]);
                    y = Float.parseFloat(elements[1]);
                    z = Float.parseFloat(elements[2]);
                }

                coordinates.put(index, new float[]{x, y, z});
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
                float[] vertex1, vertex2, vertex3, vertex4;
                if (elements.length == FACE_WITH_INDICES) {
                    vertex1 = verticesCoordinates.get(Integer.parseInt(elements[1]));
                    vertex2 = verticesCoordinates.get(Integer.parseInt(elements[2]));
                    vertex3 = verticesCoordinates.get(Integer.parseInt(elements[3]));
                    vertex4 = verticesCoordinates.get(Integer.parseInt(elements[4]));
                }
                else {
                    vertex1 = verticesCoordinates.get(Integer.parseInt(elements[0]));
                    vertex2 = verticesCoordinates.get(Integer.parseInt(elements[1]));
                    vertex3 = verticesCoordinates.get(Integer.parseInt(elements[2]));
                    vertex4 = verticesCoordinates.get(Integer.parseInt(elements[3]));
                }

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
