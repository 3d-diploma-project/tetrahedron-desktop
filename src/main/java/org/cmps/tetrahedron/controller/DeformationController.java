package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.view.LegendView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeformationController {

    private List<float[]> lastAppliedDeformations;

    public static final float DEFAULT_SCALE = 1.0f;

    @Getter
    private float currentScale = DEFAULT_SCALE;

    @Getter
    private static final DeformationController instance = new DeformationController();

    private DeformationController() {}

    public void applyDisplacements(File file) throws ModelValidationException {
        Model model = ModelController.getInstance().getModel();
        Map<Integer, float[]> originalVertices = model.getOriginalVertices();

        if (originalVertices.isEmpty()) {
            return;
        }

        lastAppliedDeformations = DataReader.readDeformations(file, originalVertices.size());
        System.out.println("[DEBUG] Deformations loaded: " + lastAppliedDeformations.size());

        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendView.getInstance().setVisible(false);
        applyDeformationScale(DEFAULT_SCALE);
    }

    public void applyDeformationScale(float scale) {
        Model model = ModelController.getInstance().getModel();
        Map<Integer, float[]> originalVertices = model.getOriginalVertices();

        if (lastAppliedDeformations == null || originalVertices.isEmpty()) {
            return;
        }

        currentScale = scale;

        Map<Integer, float[]> modelVertices = new HashMap<>();
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);
            float[] def = lastAppliedDeformations.get(idx - 1);
            modelVertices.put(idx, new float[]{
                    orig[0] + def[0] * scale,
                    orig[1] + def[1] * scale,
                    orig[2] + def[2] * scale
            });
        }

        model.updateVertices(modelVertices);
        ModelController.getInstance().setModelReady(true);
    }
}
