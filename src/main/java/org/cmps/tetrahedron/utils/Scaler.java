package org.cmps.tetrahedron.utils;

import javafx.stage.Screen;

/**
 * TODO: add description and check if it works with multiple monitors.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Scaler {

    private static final boolean IS_WINDOWS = System.getProperty("os.name", "").contains("Windows");

    public static int scaleByX(double value) {
        if (IS_WINDOWS) {
            return (int) value;
        }

        //AffineTransform scaleFactor = ModelCanvas.getInstance().getScaleFactor();
        return (int) (value * Screen.getPrimary().getOutputScaleX());
    }

    public static int scaleByY(double value) {
        if (IS_WINDOWS) {
            return (int) value;
        }

        //AffineTransform scaleFactor = ModelCanvas.getInstance().getScaleFactor();
        return (int) (value * Screen.getPrimary().getOutputScaleY());
    }
}
