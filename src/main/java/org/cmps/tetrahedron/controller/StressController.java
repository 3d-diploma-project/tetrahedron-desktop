package org.cmps.tetrahedron.controller;

import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.Stress;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.view.LegendView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.cmps.tetrahedron.utils.LegendUtils.COLORS;

@Setter
public class StressController {

    @Getter
    private static StressController instance = new StressController();
    @Getter
    private Stress stress = new Stress();
    @Getter
    private File lastStressFile;

    public void applyStress(File stressData) throws ModelValidationException {
        ModelController modelController = ModelController.getInstance();
        modelController.clearDisplacement();

        stress.setDisplayOption(StressDisplayOption.MISES);

        lastStressFile = stressData;
        Map<Integer, float[]> stressDataWithIndex = DataReader.readStress(stressData);
        stress.setStress(stressDataWithIndex);

        processStressData(stress.getDisplayOption());
    }

    public void processStressData(StressDisplayOption option) {
        Map<Integer, float[]> stressDataWithIndex = stress.getStress();

        stress.setMinStress(Float.MAX_VALUE);
        stress.setMaxStress(Float.MIN_VALUE);

        List<Float> stressToDisplay = new ArrayList<>();

        for (Map.Entry<Integer, float[]> entry : stressDataWithIndex.entrySet()) {
            float[] stressValues = entry.getValue();
            float stressValue = calculateStress(stressValues, option);

            if (stressValue < stress.getMinStress()) {
                stress.setMinStress(stressValue);
            } else if (stressValue > stress.getMaxStress()) {
                stress.setMaxStress(stressValue);
            }

            stressToDisplay.add(stressValue);
        }

        stress.setStressToDisplay(stressToDisplay);

        TreeMap<Float, Integer> legend = LegendUtils.buildLegend(stress.getMinStress(), stress.getMaxStress());
        stress.setColors(stressToDisplay.stream()
                .map(value -> COLORS.get(legend.get(legend.floorKey(value))))
                .toList());

        ModelController.getInstance().setModelColors(stress.getColors());
        LegendView.getInstance().updateLegend(stress.getMinStress(), stress.getMaxStress());
    }

    private float calculateStress(float[] stressValues, StressDisplayOption option) {
        return switch (option) {
            case MISES -> misesStress(stressValues[0], stressValues[1], stressValues[2],
                    stressValues[3], stressValues[4], stressValues[5]);
            case X -> stressValues[0];
            case Y -> stressValues[3];
            case Z -> stressValues[5];
            case XY -> stressValues[1];
            case YZ -> stressValues[4];
            case XZ -> stressValues[2];
            default -> throw new IllegalArgumentException("Unsupported stress display option: " + option);
        };
    }

    private float misesStress(double qx, double txy, double tzx, double qy, double tyz, double qz) {
        return (float) (1 / Math.sqrt(2)
                * Math.sqrt(Math.pow(qx - qy, 2)
                + Math.pow(qy - qz, 2)
                + Math.pow(qz - qx, 2)
                + 6 * (Math.pow(txy, 2) + Math.pow(tyz, 2) + Math.pow(tzx, 2))));
    }
}
