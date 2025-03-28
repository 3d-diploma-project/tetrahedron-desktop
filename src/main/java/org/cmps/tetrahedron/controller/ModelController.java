package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.view.LegendView;
import org.cmps.tetrahedron.view.ModelFilesPicker;
import org.joml.Vector3f;

import java.io.File;
import java.util.*;

import static org.cmps.tetrahedron.utils.LegendUtils.COLORS;

@Getter
public class ModelController {

    @Getter
    private static ModelController instance = new ModelController();

    private Model model;
    @Setter
    private boolean modelReady = false;
    @Getter
    private CustomCharacteristic customCharacteristic;
    private List<float[]> modelColors = null;

    private Map<Integer, float[]> originalVertices;

    private ModelController() {
        model = Model.builder()
                .vertices(new HashMap<>())
                .faces(new ArrayList<>())
                .build();
    }

    public void initModelData(File nodes, File indices) throws ModelValidationException, InternalValidationException {
        Map<Integer, float[]> vertices = DataReader.readVertices(nodes);

        originalVertices = deepCopyVertices(vertices);

        model = Model.builder()
                .vertices(vertices)
                .faces(DataReader.readIndexesAndConvertToFaces(indices, vertices))
                .build();
        modelReady = true;

        centerModel();
    }

    public List<float[][]> getFaces() {
        return model.getFaces();
    }

    public Map<Integer, float[]> getVertices() {
        if (model == null) {
            throw new RuntimeException("Model is not initialized");
        }
        return model.getVertices();
    }

    public void initCustomCharacteristic(File customDataFile) throws ModelValidationException {
        customCharacteristic = DataReader.readCustomCharacteristic(customDataFile);

        TreeMap<Float, Integer> legend = LegendUtils.buildLegend(customCharacteristic.getMinValue(), customCharacteristic.getMaxValue());
        customCharacteristic.setColors(customCharacteristic.getValues()
                .stream()
                .map(value -> COLORS.get(legend.get(legend.floorKey(value))))
                .toList());

        modelColors = customCharacteristic.getColors();
    }

    public void applyDisplacements(File deformationsFile) throws ModelValidationException {
        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendView.getInstance().reset();

        List<float[]> deformations = DataReader.readDeformations(deformationsFile, originalVertices.size());

        int i = 0;
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);
            float[] def = deformations.get(i++);

            float[] current = model.getVertices().get(idx);
            current[0] = orig[0] + def[0];
            current[1] = orig[1] + def[1];
            current[2] = orig[2] + def[2];
        }

        model.calculateModelCenter();
        centerModel();

        modelReady = true;
    }

    public void applyStress(File stressData) throws ModelValidationException {
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);

            float[] current = model.getVertices().get(idx);
            current[0] = orig[0];
            current[1] = orig[1];
            current[2] = orig[2];
        }

        model.calculateModelCenter();
        centerModel();
        modelReady = true;

        StressController.getInstance().initStress(stressData);
    }

    public void setModelColors(List<float[]> modelColors) {
        this.modelColors = modelColors;
        ColorSettings.getInstance().setColoredInSelectedColor(false);
    }

    private Map<Integer, float[]> deepCopyVertices(Map<Integer, float[]> source) {
        Map<Integer, float[]> copy = new HashMap<>();
        for (Map.Entry<Integer, float[]> e : source.entrySet()) {
            float[] v = e.getValue();
            copy.put(e.getKey(), new float[]{v[0], v[1], v[2]});
        }
        return copy;
    }

    private void centerModel() {
        Vector3f center = model.getCenter();

        Map<Integer, float[]> vertices = getVertices();
        for (Map.Entry<Integer, float[]> entry : vertices.entrySet()) {
            float[] vertex = entry.getValue();
            vertex[0] -= center.x;
            vertex[1] -= center.y;
            vertex[2] -= center.z;
        }
    }

    public void clearModel() {
        modelReady = true;
        model.setVertices(new HashMap<>());
        model.setFaces(new ArrayList<>());

        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendView.getInstance().reset();

        Platform.runLater(ModelFilesPicker::openDialogWindow);
    }
}
