package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.view.LegendView;

import java.io.File;
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
        Map<Integer, float[]> originalVertices = ModelController.getInstance().getOriginalVertices();

        if (originalVertices == null || model == null) {
            return;
        }

        lastAppliedDeformations = DataReader.readDeformations(file, originalVertices.size());
        System.out.println("[DEBUG] Deformations loaded: " + lastAppliedDeformations.size());

        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendView.getInstance().setVisible(false);
        applyDeformationScale(DEFAULT_SCALE);
    }

    public void applyDeformationScale(float scale, String component) {
        Model model = ModelController.getInstance().getModel();
        Map<Integer, float[]> originalVertices = ModelController.getInstance().getOriginalVertices();

        if (lastAppliedDeformations == null || originalVertices == null || model == null) {
            return;
        }

        int i = 0;
        Map<Integer, float[]> modelVertices = model.getVertices();
        for (Integer idx : originalVertices.keySet()) {
            float[] orig = originalVertices.get(idx);
            float[] def = lastAppliedDeformations.get(i++);
            float[] current = modelVertices.get(idx);

            switch (component) {
                case "x" -> current[0] = orig[0] + def[0] * scale;
                case "y" -> current[1] = orig[1] + def[1] * scale;
                case "z" -> current[2] = orig[2] + def[2] * scale;
                case "xy" -> {
                    current[0] = orig[0] + def[0] * scale;
                    current[1] = orig[1] + def[1] * scale;
                }
                case "yz" -> {
                    current[1] = orig[1] + def[1] * scale;
                    current[2] = orig[2] + def[2] * scale;
                }
                case "xz" -> {
                    current[0] = orig[0] + def[0] * scale;
                    current[2] = orig[2] + def[2] * scale;
                }
            }
        }

        currentScale = scale;
        ModelController.getInstance().centerModel();
        ModelController.getInstance().setModelReady(true);
    }

    public void applyDeformationScale(float scale) {
        Model model = ModelController.getInstance().getModel();
        Map<Integer, float[]> originalVertices = ModelController.getInstance().getOriginalVertices();

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

        ModelController.getInstance().centerModel();
        ModelController.getInstance().setModelReady(true);
    }
}
