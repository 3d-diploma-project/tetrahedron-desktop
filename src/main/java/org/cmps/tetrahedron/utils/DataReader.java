package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.controller.LocalizationController;
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

    private static final LocalizationController local = LocalizationController.getInstance();
    private static String errBundle = LocalizationController.ERROR_DIALOG_BUNDLE;
    private static String warnBundle = LocalizationController.WARNING_DIALOG_BUNDLE;


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
                    throw new ModelValidationException("vertices-count", "check-string");
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
            throw new ModelValidationException("vertices-read", "check-string");
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
                    throw new ModelValidationException("faces-count", "check-string");
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
                    throw new ModelValidationException(local.getString(errBundle, "faces-not-exist") +
                            " " + invalidIndices);
                }
            }

            Set<Integer> unusedVertices = new HashSet<>(availableVertices);
            unusedVertices.removeAll(usedIndices);

            if (!unusedVertices.isEmpty()) {
                WarningDialog dialog = new WarningDialog("attention", "vertices-not-used-in-faces","continue");
                boolean userChoice = dialog.showAndWait();

                if (!userChoice) {
                    throw new InvalidModelDataException(local.getString(warnBundle, "cancel-continue"));
                }
            }

            return faces;
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new ModelValidationException("faces-read", "check-string");
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
                        throw new ModelValidationException("stress-format");
                    }

                    if (elements.length == STRESS_WITH_INDICES) {
                        index = Integer.parseInt(elements[0]);
                        startIndex = 1;
                    } else {
                        index = i++;
                        startIndex = 0;
                    }

                    if (stressOneElement.containsKey(index)) {
                        throw new ModelValidationException("repeating-index", index);
                    }

                    for (int j = startIndex; j < elements.length; j++) {
                        stressValues[j - startIndex] = Float.parseFloat(elements[j]);
                    }
                } catch (NumberFormatException e) {
                    throw new ModelValidationException("read-number", "check-string", line);
                }

                stressOneElement.put(index, stressValues);
            }

            return stressOneElement;
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(local.getString(errBundle, "not-found-file") + stressData.getAbsolutePath());
        }
    }

    public static CustomCharacteristic readCustomCharacteristic(File customData) throws ModelValidationException {
        Locale.setDefault(US);

        CustomCharacteristic customModel = new CustomCharacteristic();
        List<Float> values = new ArrayList<>();

        int index = 1;

        try (Scanner fid = new Scanner(customData)) {
            if (!fid.hasNext()) {
                throw new ModelValidationException("characteristic-not-found");
            }

            while (fid.hasNext()) {
                String token = fid.next();
                try {
                    float value = Float.parseFloat(token);

                    if (Float.isNaN(value) || Float.isInfinite(value)) {
                        throw new ModelValidationException(local.getString(errBundle, "characteristic-read-number") +
                                " " + value);
                    }

                    if (value < customModel.getMinValue()) {
                        customModel.setMinValue(value);
                    } else if (value > customModel.getMaxValue()) {
                        customModel.setMaxValue(value);
                    }

                    index = index + 1;
                    values.add(value);
                } catch (NumberFormatException e) {
                    throw new ModelValidationException("read-number", "check-string", index);
                }
            }

            customModel.setValues(values);
            return customModel;
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(local.getString(errBundle, "not-found-file") + customData.getAbsolutePath());
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
                    throw new ModelValidationException("displacements-format", "check-string", line);
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
                    throw new ModelValidationException("repeating-index", index);
                }
                deformationsMap.put(index, new float[]{dx, dy, dz});
            }
        } catch (FileNotFoundException e) {
            throw new ModelValidationException(
                    local.getString(errBundle, "not-found-file") + deformationsFile.getAbsolutePath());
        } catch (NumberFormatException e) {
            throw new ModelValidationException("read-number");
        }

        if (deformationsMap.size() != expectedVerticesCount) {
            throw new ModelValidationException(
                    String.format(local.getString(errBundle, "displacements-nodes"),
                            deformationsMap.size(), expectedVerticesCount)
            );
        }

        List<float[]> result = new ArrayList<>(Collections.nCopies(expectedVerticesCount, null));

        for (Map.Entry<Integer, float[]> e : deformationsMap.entrySet()) {
            int idx = e.getKey();
            float[] def = e.getValue();

            if (idx < 1 || idx > expectedVerticesCount) {
                throw new ModelValidationException("displacements-out-range", idx);
            }
            result.set(idx - 1, def);
        }

        return result;
    }
}
