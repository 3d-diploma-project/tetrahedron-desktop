package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.exception.InvalidModelDataException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.view.WarningDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Locale.US;

public class DataReader {

    private static final int VERTICES_WITH_INDICES = 4;
    private static final int FACE_WITH_INDICES = 5;
    private static final int DEFORMATIONS_WITH_INDEX = 4;
    private static final int STRESS_WITH_INDICES = 7;


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
                    throw new ModelValidationException(ErrorMessages.VERTICES_COUNT + "\n\n" + ErrorMessages.CHECK_STRING);
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
            throw new ModelValidationException(ErrorMessages.VERTICES_READ + "\n\n" + ErrorMessages.CHECK_STRING);
        }
    }

    public static List<float[][]> readIndexesAndConvertToFaces(File indicesMatrix,
                                                               Map<Integer, float[]> verticesCoordinates)
            throws InvalidModelDataException, ModelValidationException {
        Locale.setDefault(US);

        Set<Integer> usedIndices = new HashSet<>();
        Set<Integer> availableVertices = verticesCoordinates.keySet();
        List<Integer> invalidIndices = new ArrayList<>();

        try (Scanner fid = new Scanner(indicesMatrix)) {
            List<float[][]> faces = new ArrayList<>();
            while (fid.hasNextLine()) {
                String[] elements = fid.nextLine().trim().split("\\s+");

                if (elements.length < 4 || elements.length > 5) {
                    throw new ModelValidationException(ErrorMessages.FACES_COUNT + "\n\n" + ErrorMessages.CHECK_STRING);
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
                    throw new ModelValidationException(ErrorMessages.FACES_NOT_EXIST + " " + invalidIndices);
                }
            }

            Set<Integer> unusedVertices = new HashSet<>(availableVertices);
            unusedVertices.removeAll(usedIndices);

            if (!unusedVertices.isEmpty()) {
                WarningDialog dialog = new WarningDialog(WarningMessages.ATTENTION,
                        WarningMessages.VERTICES_NOT_USED_IN_FACES + "\n\n" + WarningMessages.CONTINUE);
                boolean userChoice = dialog.showAndWait();

                if (!userChoice) {
                    throw new InvalidModelDataException(WarningMessages.CANCEL_CONTINUE);
                }
            }

            return faces;
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new ModelValidationException(ErrorMessages.FACES_READ + "\n\n" + ErrorMessages.CHECK_STRING);
        }
    }

    public static Map<Integer, float[]> readStress(File stressData) throws ModelValidationException {
        Locale.setDefault(US);

        int i = 1, index, startIndex;
        Map<Integer, float[]> stressOneElement = new HashMap<>();

        try (Scanner fid = new Scanner(stressData)) {
            while (fid.hasNextLine()) {
                String line = fid.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] elements = line.split("\\s+");
                float[] stressValues = new float[6];

                try {
                    if (elements.length < 6 || elements.length > 7) {
                        throw new ModelValidationException(ErrorMessages.STRESS_FORMAT);
                    }

                    if (elements.length == STRESS_WITH_INDICES) {
                        index = Integer.parseInt(elements[0]);
                        startIndex = 1;
                    } else {
                        index = i++;
                        startIndex = 0;
                    }

                    if (stressOneElement.containsKey(index)) {
                        throw new ModelValidationException(ErrorMessages.REPEATING_INDEX + " \n" + index);
                    }

                    for (int j = startIndex; j < elements.length; j++) {
                        stressValues[j - startIndex] = Float.parseFloat(elements[j]);
                    }
                } catch (NumberFormatException e) {
                    throw new ModelValidationException(
                            ErrorMessages.READ_NUMBER + "\n\n" + ErrorMessages.CHECK_STRING + " " + line);
                }

                stressOneElement.put(index, stressValues);
            }

            return stressOneElement;
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(ErrorMessages.NOT_FOUND_FILE + stressData.getAbsolutePath());
        }
    }

    public static CustomCharacteristic readCustomCharacteristic(File customData) throws ModelValidationException {
        Locale.setDefault(US);

        CustomCharacteristic customModel = new CustomCharacteristic();
        List<Float> values = new ArrayList<>();

        try (Scanner fid = new Scanner(customData)) {
            if (!fid.hasNext()) {
                throw new ModelValidationException(ErrorMessages.CHARACTERISTIC_NOT_FOUND);
            }

            while (fid.hasNext()) {
                String token = fid.next();
                try {
                    float value = Float.parseFloat(token);

                    if (Float.isNaN(value) || Float.isInfinite(value)) {
                        throw new ModelValidationException(ErrorMessages.CHARACTERISTIC_READ_NUMBER + " " + value);
                    }

                    if (value < customModel.getMinValue()) {
                        customModel.setMinValue(value);
                    } else if (value > customModel.getMaxValue()) {
                        customModel.setMaxValue(value);
                    }

                    values.add(value);
                } catch (NumberFormatException e) {
                    throw new ModelValidationException(ErrorMessages.READ_NUMBER + " " + token);
                }
            }

            if (values.isEmpty()) {
                throw new ModelValidationException(ErrorMessages.CHARACTERISTIC_IS_EMPTY);
            }

            customModel.setValues(values);
            return customModel;
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(ErrorMessages.NOT_FOUND_FILE + customData.getAbsolutePath());
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
                    throw new ModelValidationException(ErrorMessages.DISPLACEMENTS_FORMAT + "\n\n" +
                            ErrorMessages.CHECK_STRING + " " + line);
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
                    throw new ModelValidationException(ErrorMessages.REPEATING_INDEX + " \n" + index);
                }
                deformationsMap.put(index, new float[]{dx, dy, dz});
            }
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(
                    ErrorMessages.NOT_FOUND_FILE + deformationsFile.getAbsolutePath());
        } catch (NumberFormatException e) {
            throw new ModelValidationException(ErrorMessages.READ_NUMBER);
        }

        if (deformationsMap.size() != expectedVerticesCount) {
            throw new ModelValidationException(
                    String.format(ErrorMessages.DISPLACEMENTS_NODES, deformationsMap.size(), expectedVerticesCount)
            );
        }

        List<float[]> result = new ArrayList<>(Collections.nCopies(expectedVerticesCount, null));

        for (Map.Entry<Integer, float[]> e : deformationsMap.entrySet()) {
            int idx = e.getKey();
            float[] def = e.getValue();

            if (idx < 1 || idx > expectedVerticesCount) {
                throw new ModelValidationException(
                        ErrorMessages.DISPLACEMENTS_OUT_RANGE + " " + idx
                );
            }
            result.set(idx - 1, def);
        }

        return result;
    }
}
