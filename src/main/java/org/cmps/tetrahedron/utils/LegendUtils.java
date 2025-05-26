package org.cmps.tetrahedron.utils;

import lombok.Getter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LegendUtils {

    @Getter
    private static int colorArraySize = 7;

    public static Map<Integer, float[]> COLORS = getHSVColors();

    public static TreeMap<Float, Integer> buildLegend(float min, float max) {
        TreeMap<Float, Integer> stressColorMap = new TreeMap<>();
        float stressDiapason = max - min;
        float stressChunk = stressDiapason / colorArraySize;

        for (int i = 0; i < colorArraySize; i++) {
            float stress = min + (stressChunk * i);
            stressColorMap.put(stress, colorArraySize - i - 1);
        }

        return stressColorMap;
    }

    private static Map<Integer, float[]> getHSVColors() {
        double jump = 0.66 / (colorArraySize * 1.0);
        Map<Integer, float[]> colors = new HashMap<>();

        for (int i = 0; i < colorArraySize; i++) {
            Color color = Color.getHSBColor((float) (jump * i), 0.7f, 0.8f);
            colors.put(i, new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f});
        }

        return colors;
    }

    private static Map<Integer, float[]> getGrayColors() {
        Map<Integer, float[]> colors = new HashMap<>();
        float minGray = 0.2f;
        float maxGray = 0.8f;

        for (int i = 0; i < colorArraySize; i++) {
            float gray = minGray + (maxGray - minGray) * (i / (float) (colorArraySize - 1));
            colors.put(i, new float[]{gray, gray, gray});
        }

        return colors;
    }

    public static void setColorArraySizeAndTheme(int size, boolean grayscale) {
        colorArraySize = size;
        COLORS = grayscale ? getGrayColors() : getHSVColors();
    }
}
