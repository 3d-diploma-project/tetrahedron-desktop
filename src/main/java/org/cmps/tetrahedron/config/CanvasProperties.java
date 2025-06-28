package org.cmps.tetrahedron.config;

import lombok.Getter;

import java.awt.*;

/**
 * Stores and updates all properties related to LWJGL canvas.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class CanvasProperties {

    @Getter
    private static int width = 1920;
    @Getter
    private static int height = 1080;
    @Getter
    private static boolean sizeChanged = true;

    public static void setWidth(int width) {
        sizeChanged = true;
        CanvasProperties.width = width;
    }

    public static void setHeight(int height) {
        sizeChanged = true;
        CanvasProperties.height =  height;
    }

    public static Dimension getSize() {
        sizeChanged = false;
        return new Dimension(width, height);
    }
}
