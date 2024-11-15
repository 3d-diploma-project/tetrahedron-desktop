package org.cmps.tetrahedron.config;

import org.cmps.tetrahedron.utils.Scaler;

import java.awt.*;

/**
 * Stores properties related to lwjgl window.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class WindowProperties {

    private static int logicalWidth = 1200;
    private static int logicalHeight = 800;

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 800;

    private static boolean changed = true;

    public static boolean isChanged() {
        if (changed) {
            changed = false;
            return true;
        }

        return false;
    }

    public static Dimension getLogicalSize() {
        return new Dimension(logicalWidth, logicalHeight);
    }

    public static void setWidth(int width) {
        if (WindowProperties.logicalWidth == width) {
            return;
        }

        changed = true;
        WindowProperties.logicalWidth = width;
    }

    public static void setHeight(int height) {
        if (WindowProperties.logicalHeight == height) {
            return;
        }

        changed = true;
        WindowProperties.logicalHeight = height;
    }

    public static int getLogicalWidth() {
        return logicalWidth;
    }

    public static int getLogicalHeight() {
        return logicalHeight;
    }

    public static int getPhysicalWidth() {
        //AffineTransform scaleFactor = ModelCanvas.getInstance().getScaleFactor();
        return Scaler.scaleByX(logicalWidth);
    }

    public static int getPhysicalHeight() {
        return Scaler.scaleByY(logicalHeight);
    }
}
