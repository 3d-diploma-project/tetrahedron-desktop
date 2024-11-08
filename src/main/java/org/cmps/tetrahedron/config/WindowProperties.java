package org.cmps.tetrahedron.config;

/**
 * Stores properties related to lwjgl window.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class WindowProperties {

    private static int width = 1024;
    private static int height = 768;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setWidth(int width) {
        WindowProperties.width = width;
    }

    public static void setHeight(int height) {
        WindowProperties.height = height;
    }
}
