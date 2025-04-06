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

    private static int logicalWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int logicalHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 800;

    public static Dimension getLogicalSize() {
        return new Dimension(logicalWidth, logicalHeight);
    }

    public static void setWidth(int width) {
        if (WindowProperties.logicalWidth == width) {
            return;
        }

        WindowProperties.logicalWidth = width;
    }

    public static void setHeight(int height) {
        if (WindowProperties.logicalHeight == height) {
            return;
        }

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