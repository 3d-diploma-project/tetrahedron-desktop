package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.model.Stress;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.utils.StressUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.cmps.tetrahedron.utils.LegendUtils.COLORS;

public class StressController {

    @Getter
    private static StressController instance = new StressController();
    @Getter
    private Stress stress = new Stress();

    public void initStress(File stressData) {
        Map<Integer, float[]> stressDataWithIndex= DataReader.readStress(stressData);

        processStressData(stressDataWithIndex, StressDisplayOption.MISES);

        TreeMap<Float, Integer> legend = LegendUtils.buildLegend(stress.getMinStress(), stress.getMaxStress());
        stress.setColors(stress.getStressToDisplay()
                .stream()
                .map(value -> COLORS.get(legend.get(legend.floorKey(value))))
                .toList());

        ModelController.getInstance().setModelColors(stress.getColors());
    }

    private void processStressData(Map<Integer, float[]> stressDataWithIndex, StressDisplayOption option) {
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
        stress.setStress(stressDataWithIndex);
    }

    private static float calculateStress(float[] stressValues, StressDisplayOption option) {
        return switch (option) {
            case MISES -> StressUtils.misesStress(stressValues[0], stressValues[1], stressValues[2],
                    stressValues[3], stressValues[4], stressValues[5]);
            default -> throw new IllegalArgumentException("Unsupported stress display option: " + option);
        };
    }

}
