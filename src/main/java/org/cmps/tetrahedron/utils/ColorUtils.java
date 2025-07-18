package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.model.LegendItemData;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorUtils {

    public static List<float[]> matchColorsWithValues(List<Float> values) {
        TreeSet<LegendItemData> legend = new TreeSet<>(Legend.getInstance().getItems().get());

        return values.parallelStream()
                     .map(value -> legend.floor(new LegendItemData(value)))
                     .filter(Objects::nonNull)
                     .map(LegendItemData::color)
                     .toList();
    }

    public static Map<Integer, float[]> getHSVColors(int colorArraySize) {
        double jump = 0.66 / (colorArraySize * 1.0);
        Map<Integer, float[]> colors = new HashMap<>();

        for (int i = 0; i < colorArraySize; i++) {
            Color color = Color.getHSBColor((float) (jump * i), 0.7f, 0.8f);
            colors.put(i, new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f});
        }

        return colors;
    }

    public static Map<Integer, float[]> getGrayColors(int colorArraySize) {
        Map<Integer, float[]> colors = new HashMap<>();
        float minGray = 0.2f;
        float maxGray = 0.8f;

        for (int i = 0; i < colorArraySize; i++) {
            float gray = minGray + (maxGray - minGray) * (i / (float) (colorArraySize - 1));
            colors.put(i, new float[]{gray, gray, gray});
        }

        return colors;
    }
}
