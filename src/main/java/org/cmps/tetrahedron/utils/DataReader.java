package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.Stress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    private static final int VERTICES_WITH_INDICES = 4;
    private static final int FACE_WITH_INDICES = 5;

    public static Map<Integer, float[]> readVertices(File coordinatesTableFile)
            throws ModelValidationException {
        Locale.setDefault(US);

        int i = 1;
        try (Scanner fid = new Scanner(coordinatesTableFile)) {
            Map<Integer, float[]> coordinates = new HashMap<>();

            while (fid.hasNextLine()) {
                String[] elements = fid.nextLine().trim().split("\\s+");
                int index, startIndex;

                if (elements.length < 3 || elements.length > 4) {
                    throw new ModelValidationException("""
                            Для кожної вершини має бути вказано 3 координати.\s
                                                             
                            Перевірте дані та спробуйте знову""");
                }

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
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new ModelValidationException("""
                    Помилка під час зчитування матриці координат.\s
                                                
                    Перевірте дані та спробуйте знову""");
        }
    }

    public static List<float[][]> readIndexesAndConvertToFaces(File indicesMatrix,
                                                               Map<Integer, float[]> verticesCoordinates)
            throws ModelValidationException {
        Locale.setDefault(US);

        try (Scanner fid = new Scanner(indicesMatrix)) {
            List<float[][]> faces = new ArrayList<>();
            while (fid.hasNextLine()) {
                String[] elements = fid.nextLine().trim().split("\\s+");

                if (elements.length < 4 || elements.length > 5) {
                    throw new ModelValidationException("""
                            Для кожного елементу має бути вказано 4 індекси координат.\s
                                                             
                            Перевірте дані та спробуйте знову""");
                }

                float[][] tetrahedron = new float[4][];
                int startIndex;

                startIndex = elements.length == FACE_WITH_INDICES ? 1 : 0;
                for (int j = startIndex; j < elements.length; j++) {
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
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new ModelValidationException("""
                    Помилка під час зчитування матриці індексів.\s
                                                
                    Перевірте дані та спробуйте знову""");
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

    public static CustomCharacteristic readCustomCharacteristic(File customData) {
        Locale.setDefault(US);

        CustomCharacteristic customModel = new CustomCharacteristic();

        try (Scanner fid = new Scanner(customData)) {
            List<Float> values = new ArrayList<>();

            while (fid.hasNext()) {
                float value = fid.nextFloat();

                if (value < customModel.getMinValue()) {
                    customModel.setMinValue(value);
                } else if (value > customModel.getMaxValue()) {
                    customModel.setMaxValue(value);
                }

                values.add(value);
            }

            customModel.setValues(values);
            return customModel;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
