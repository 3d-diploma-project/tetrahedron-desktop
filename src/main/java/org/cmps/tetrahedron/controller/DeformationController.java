package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeformationController {

    private List<float[]> lastAppliedDeformations;

    public static final float DEFAULT_SCALE = 1.0f;

    @Getter
    private float currentScale = DEFAULT_SCALE;

    private Map<Integer, float[]> originalVertices;


    public void setOriginalVertices(Map<Integer, float[]> vertices) {
        this.originalVertices = deepCopyVertices(vertices);
    }

    public void applyDisplacements(File file, float scale, Model model) throws ModelValidationException {
        if (originalVertices == null || model == null) {
            return;
        }

        lastAppliedDeformations = DataReader.readDeformations(file, originalVertices.size());
        currentScale = scale;
        applyDeformationScale(scale, model);
    }

    public void applyDeformationScale(float scale, Model model) {
        if (lastAppliedDeformations == null || originalVertices == null || model == null) {
            return;
        }

        int i = 0;
        Map<Integer, float[]> modelVertices = model.getVertices();
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);
            float[] def = lastAppliedDeformations.get(i++);
            float[] current = modelVertices.get(idx);
            current[0] = orig[0] + def[0] * scale;
            current[1] = orig[1] + def[1] * scale;
            current[2] = orig[2] + def[2] * scale;
        }

        currentScale = scale;
        model.calculateModelCenter();
    }

    private Map<Integer, float[]> deepCopyVertices(Map<Integer, float[]> source) {
        Map<Integer, float[]> copy = new HashMap<>();
        for (Map.Entry<Integer, float[]> e : source.entrySet()) {
            float[] v = e.getValue();
            copy.put(e.getKey(), new float[]{v[0], v[1], v[2]});
        }
        return copy;
    }
}
