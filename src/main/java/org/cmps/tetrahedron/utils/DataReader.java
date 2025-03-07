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
    private static final int DEFORMATIONS_WITH_INDEX = 4;

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

        Set<Integer> usedIndices = new HashSet<>();
        Set<Integer> availableVertices = verticesCoordinates.keySet();
        List<Integer> invalidIndices = new ArrayList<>();

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
                    int index = Integer.parseInt(elements[j]);
                    usedIndices.add(index);

                    if (!verticesCoordinates.containsKey(index)) {
                        invalidIndices.add(index);
                    } else {
                        tetrahedron[j - startIndex] = verticesCoordinates.get(index);
                    }
                }

                float[] vertex1 = tetrahedron[0];
                float[] vertex2 = tetrahedron[1];
                float[] vertex3 = tetrahedron[2];
                float[] vertex4 = tetrahedron[3];

                faces.add(new float[][]{vertex1, vertex2, vertex3});
                faces.add(new float[][]{vertex1, vertex2, vertex4});
                faces.add(new float[][]{vertex1, vertex4, vertex3});
                faces.add(new float[][]{vertex4, vertex2, vertex3});

                if (!invalidIndices.isEmpty()) {
                    throw new ModelValidationException("Матриця індексів використовує неіснуючі координати!\n" +
                            "Завантажте правильні файли.\n\n" +
                            "Неправильні індекси: " + invalidIndices);
                }
            }

            Set<Integer> unusedVertices = new HashSet<>(availableVertices);
            unusedVertices.removeAll(usedIndices);

            // Maybe create a new special ModalWindow
            if (!unusedVertices.isEmpty()) {
                throw new ModelValidationException("Таблиця координат містить точки, які не використовуються в матриці індексів. " +
                        "Ви впевнені, що хочете продовжити?");
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

    public static List<float[]> readDeformations(File deformationsFile, int expectedVerticesCount)
            throws ModelValidationException {
        Locale.setDefault(US);

        Map<Integer, float[]> deformationsMap = new HashMap<>();

        int autoIndex = 1;
        try (Scanner fid = new Scanner(deformationsFile)) {
            while (fid.hasNextLine()) {
                String line = fid.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] elements = line.split("\\s+");
                if (elements.length < 3 || elements.length > 4) {
                    throw new ModelValidationException(
                            "У кожному рядку файлу деформацій має бути 3 (dx, dy, dz) чи 4 (index, dx, dy, dz) числа.\n" +
                                    "Перевірте строку: " + line
                    );
                }

                int index;
                int startIdx;

                if (elements.length == DEFORMATIONS_WITH_INDEX) {
                    index = Integer.parseInt(elements[0]);
                    startIdx = 1;
                } else {
                    index = autoIndex++;
                    startIdx = 0;
                }

                float dx = Float.parseFloat(elements[startIdx]);
                float dy = Float.parseFloat(elements[startIdx + 1]);
                float dz = Float.parseFloat(elements[startIdx + 2]);

                if (deformationsMap.containsKey(index)) {
                    throw new ModelValidationException(
                            "Файл деформацій містить індекс, що повторюється: " + index
                    );
                }
                deformationsMap.put(index, new float[]{dx, dy, dz});
            }
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(
                    "Не вдалося знайти файл деформацій: " + deformationsFile.getAbsolutePath());
        } catch (NumberFormatException e) {
            throw new ModelValidationException("Помилка перетворення числа у файлі деформацій");
        }

        if (deformationsMap.size() != expectedVerticesCount) {
            throw new ModelValidationException(
                    "Кількість прочитаних деформацій (" + deformationsMap.size() +
                            ") не збігається з кількістю вершин (" + expectedVerticesCount + ")"
            );
        }

        List<float[]> result = new ArrayList<>(Collections.nCopies(expectedVerticesCount, null));

        for (Map.Entry<Integer, float[]> e : deformationsMap.entrySet()) {
            int idx = e.getKey();
            float[] def = e.getValue();

            if (idx < 1 || idx > expectedVerticesCount) {
                throw new ModelValidationException(
                        "Індекс у файлі деформацій вийшов за межі: " + idx
                );
            }
            result.set(idx - 1, def);
        }

        return result;
    }
}
