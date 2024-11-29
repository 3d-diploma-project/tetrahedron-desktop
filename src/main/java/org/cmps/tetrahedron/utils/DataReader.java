package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.model.Stress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    private static final boolean WITH_INDICES = true;

    public static Map<Integer, float[]> readVertices(File coordinatesTableFile) {
        Locale.setDefault(US);

        int i = 1;
        try (Scanner fid = new Scanner(coordinatesTableFile)) {
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

    public static List<float[][]> readIndexesAndConvertToFaces(File indicesMatrix,
                                                               Map<Integer, float[]> verticesCoordinates) {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(indicesMatrix)) {
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

    public static Stress readStress(File stressData) {
        Locale.setDefault(US);

        Stress stressModel = new Stress();

        try (Scanner fid = new Scanner(stressData)) {
            List<Float> stress = new ArrayList<>();

            while (fid.hasNext()) {
                float stressValue;
                if (WITH_INDICES) {
                    fid.nextInt();
                    stressValue = StressUtils.misesStress(fid.nextFloat(),fid.nextFloat(),
                                                       fid.nextFloat(), fid.nextFloat(),
                                                       fid.nextFloat(), fid.nextFloat());
                } else {
                    stressValue = fid.nextFloat();
                }

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
