package org.cmps.tetrahedron.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class StressUtils {
    private static final int COLOR_ARRAY_SIZE = 7;

    public static float misesStress(double qx, double txy, double tzx, double qy, double tyz, double qz) {
        return (float) (1 / Math.sqrt(2)
                * Math.sqrt(Math.pow(qx - qy, 2)
                                    + Math.pow(qy - qz, 2)
                                    + Math.pow(qz - qx, 2)
                                    + 6 * (Math.pow(txy, 2) + Math.pow(tyz, 2) + Math.pow(tzx, 2))));
    }

    private static Map<Integer, float[]> colors = getColors();

    public static Map<Integer, float[]> getColors() {

        double jump = 0.66 / (COLOR_ARRAY_SIZE * 1.0);
        Map<Integer, float[]> colors = new HashMap<>();

        for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
            Color color = Color.getHSBColor((float) (jump * i), 0.7f, 0.7f);

            colors.put(i, new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f});
        }

        return colors;
    }


    private static TreeMap<Float, Integer> stressColorMap =
            new TreeMap<>(Map.of(5.79417E-6f, 6, //[0.21176471, 0.5058824, 0.7019608]
                                 9.0176787E-4f, 5, //[0.21176471, 0.7019608, 0.6156863]
                                 0.0017977416f, 4, //[0.21176471, 0.7019608, 0.3372549]
                                 0.0026937153f, 3, //[0.35686275, 0.7019608, 0.21176471]
                                 0.003589689f, 2, //[0.63529414, 0.7019608, 0.21176471]
                                 0.0044856626f, 1, //[0.7019608, 0.4862745, 0.21176471]
                                 0.0053816363f, 0 //[0.7019608, 0.21176471, 0.21176471]
            ));

//            new TreeMap<>(Map.of(5.79417E-6f, 6, //[0.21176471, 0.5058824, 0.7019608]
//                                 6.0176787E-4f, 5, //[0.21176471, 0.7019608, 0.6156863]
//                                 0.0020977416f, 4, //[0.21176471, 0.7019608, 0.3372549]
//                                 0.0026937153f, 3, //[0.35686275, 0.7019608, 0.21176471]
//                                 0.003589689f, 2, //[0.63529414, 0.7019608, 0.21176471]
//                                 0.0044856626f, 1, //[0.7019608, 0.4862745, 0.21176471]
//                                 0.0053816363f, 0 //[0.7019608, 0.21176471, 0.21176471]
//            ));

    public static void printColorDiapasons() {
        float stressDiapason = DataReader.getMaxStress() - DataReader.getMinStress();
        float stressChunk = stressDiapason / COLOR_ARRAY_SIZE;

        System.out.println(DataReader.getMinStress() + " - " + DataReader.getMaxStress());
        System.out.println("{");
        for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
            float stress = DataReader.getMinStress() + (stressChunk * i);
            //stressColorMap.put(stress, i);
            System.out.print(stress + "f, " + (COLOR_ARRAY_SIZE - i - 1) + (i == COLOR_ARRAY_SIZE - 1 ? "" : ","));
            System.out.println(" //" + Arrays.toString(getColors().get(COLOR_ARRAY_SIZE - i - 1)));
        }
        System.out.println("}");
    }

    public static float[] getColor(float stress) {
        return colors.get(stressColorMap.get(stressColorMap.floorKey(stress)));
    }
}
