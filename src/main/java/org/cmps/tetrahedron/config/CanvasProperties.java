package org.cmps.tetrahedron.config;

import org.cmps.tetrahedron.utils.Scaler;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class CanvasProperties {

    public static final int X_SHIFT = 250;
    public static final int Y_SHIFT = 150;

    public static int getWidth() {
        return WindowProperties.getLogicalWidth() - X_SHIFT * 2;
    }

    public static int getHeight() {
        return WindowProperties.getLogicalHeight() - Y_SHIFT * 2;
    }

    public static int getPhysicalWidth() {
        return Scaler.scaleByX(getWidth());
    }

    public static int getPhysicalHeight() {
        return Scaler.scaleByY(getHeight());
    }
}