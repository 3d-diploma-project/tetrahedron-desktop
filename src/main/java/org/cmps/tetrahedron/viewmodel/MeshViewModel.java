package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.TetraModelApi;
import org.cmps.tetrahedron.utils.FileUtils;
import org.cmps.tetrahedron.utils.StlToTetraMesh;
import org.cmps.tetrahedron.view.common.ErrorDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MeshViewModel {

    private static final String CONSTANTS_FILE = "0_constants.txt";
    private static final String COORDINATES_FILE = "1_coordinates_matrix.txt";
    private static final String ELEMENTS_FILE = "2_elements_matrix.txt";

    @Getter
    private static final MeshViewModel instance = new MeshViewModel();

    private final ModelController modelController = ModelController.getInstance();

    @Getter
    private final StringProperty stlFileName = new SimpleStringProperty();
    @Getter
    private final StringProperty minMeshSize = new SimpleStringProperty("1");
    @Getter
    private final StringProperty maxMeshSize = new SimpleStringProperty("5");
    @Getter
    private final StringProperty angel = new SimpleStringProperty("40");

    @Getter
    private final StringProperty nodesCount = new SimpleStringProperty("-");
    @Getter
    private final StringProperty elementsCount = new SimpleStringProperty("-");

    TetraModelApi tetraModelApi;

    private MeshViewModel() {
        stlFileName.addListener((_, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty() || Objects.equals(newValue, oldValue)) {
                return;
            }

            try {
                tetraModelApi = StlToTetraMesh.extractStlData(newValue);
                nodesCount.set(String.valueOf(tetraModelApi.coordinates().size()));
                modelController.initModelData(tetraModelApi);
            } catch (Throwable e) {
                new ErrorDialog(new ModelValidationException("Error when creating mesh. " + e.getMessage()));
            }
        });
    }

    public void meshModel() {
        double minMeshSize = Double.parseDouble(this.minMeshSize.getValue().replace(",", "."));
        double maxMeshSize = Double.parseDouble(this.maxMeshSize.getValue().replace(",", "."));
        double angel = Double.parseDouble(this.angel.getValue().replace(",", "."));

        tetraModelApi = StlToTetraMesh.generateMesh(stlFileName.get(), minMeshSize, maxMeshSize, angel);
        modelController.initModelData(tetraModelApi);
        nodesCount.set(String.valueOf(tetraModelApi.coordinates().size()));
        elementsCount.set(String.valueOf(tetraModelApi.indices().length));
    }

    public void saveModel(File directory) {
        var sortedNodeIndices = sortCoordinates();
        Map<Integer, Integer> indexToSortedIndex = new HashMap<>();
        for (int i = 0; i < sortedNodeIndices.size(); i++) {
            indexToSortedIndex.put(sortedNodeIndices.get(i), i + 1);
        }

        try {
            writeConstantsToFile(directory);
            writeCoordinatesToFile(directory, sortedNodeIndices);
            writeIndicesToFile(directory, indexToSortedIndex);
        } catch (IOException | RuntimeException e) {
            new ErrorDialog(new ModelValidationException("Error when saving model. " + e.getMessage()));
        }
    }

    private List<Integer> sortCoordinates() {
        return tetraModelApi
                .coordinates()
                .entrySet()
                .stream()
                .sorted(Comparator.comparingDouble((Map.Entry<Integer, float[]> entry) -> entry.getValue()[0])
                                  .thenComparingDouble(entry -> entry.getValue()[1])
                                  .thenComparingDouble(entry -> entry.getValue()[2]))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void writeConstantsToFile(File directory) throws IOException {
        File file = FileUtils.createFile(directory, CONSTANTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.format("FiniteElementNodesCount=%d", tetraModelApi.indices()[0].length));
            writer.newLine();
            writer.write(String.format("NodesCount=%d", tetraModelApi.coordinates().size()));
            writer.newLine();
            writer.write(String.format("ElementsCunt=%d", tetraModelApi.indices().length));
            writer.newLine();
        }
    }

    private void writeCoordinatesToFile(File directory, List<Integer> sortedNodeIndices) throws IOException {
        File file = FileUtils.createFile(directory, COORDINATES_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            var coordinates = tetraModelApi.coordinates();

            for (int i = 0; i < sortedNodeIndices.size(); i++) {
                int originalIndex = sortedNodeIndices.get(i);

                writer.write(String.format("%7d ", i + 1));

                writer.write(String.format("%7.12E ", coordinates.get(originalIndex)[0]));
                writer.write(String.format("%7.12E ", coordinates.get(originalIndex)[1]));
                writer.write(String.format("%7.12E ", coordinates.get(originalIndex)[2]));

                writer.newLine();
            }
        }
    }

    private void writeIndicesToFile(File directory, Map<Integer, Integer> indexToSortedIndex) throws IOException {
        File file = FileUtils.createFile(directory, ELEMENTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            var elements = tetraModelApi.indices();

            for (int i = 0; i < elements.length; i++) {
                writer.write(String.format("%7d ", i + 1));

                int[] element = elements[i];
                writer.write(String.format("%7d ", indexToSortedIndex.get(element[0])));
                writer.write(String.format("%7d ", indexToSortedIndex.get(element[1])));
                writer.write(String.format("%7d ", indexToSortedIndex.get(element[2])));
                writer.write(String.format("%7d ", indexToSortedIndex.get(element[3])));

                writer.newLine();
            }
        }
    }
}
