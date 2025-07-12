package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.io.File;
import java.util.*;

public class DeformationController {

    private List<float[]> lastAppliedDeformations;

    public static final float DEFAULT_SCALE = 1.0f;

    @Getter
    private float currentScale = DEFAULT_SCALE;
    @Getter
    private Set<String> deformationComponents = null;

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
        Legend.getInstance().setVisible(false);
        applyDeformationScale(DEFAULT_SCALE, null);
    }

    public void applyDeformationScale(float scale, Set<String> components) {
        Model model = ModelController.getInstance().getModel();
        Map<Integer, float[]> originalVertices = ModelController.getInstance().getModel().getOriginalVertices();

        if (lastAppliedDeformations == null || originalVertices == null) {
            return;
        }

        deformationComponents = components;
        currentScale = scale;

        int i = 0;
        Map<Integer, float[]> modelVertices = new HashMap<>();
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);
            float[] def = lastAppliedDeformations.get(i++);
            float[] current = new float[] {orig[0], orig[1], orig[2]};
            modelVertices.put(idx, current);

            for (String component : Optional.ofNullable(deformationComponents).orElse(Set.of("x", "y", "z"))) {
                switch (component) {
                    case "x" -> current[0] = orig[0] + def[0] * currentScale;
                    case "y" -> current[1] = orig[1] + def[1] * currentScale;
                    case "z" -> current[2] = orig[2] + def[2] * currentScale;
                }
            }
        }

        model.updateVertices(modelVertices);
        ModelController.getInstance().setModelReady(true);
    }
}
