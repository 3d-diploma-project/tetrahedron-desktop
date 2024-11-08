package org.cmps.tetrahedron.utils;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseControls {

    private float zoomFactor = 1.0f;

    private boolean isRotating;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    // Most likely we will remove coordinatesConvertor from MouseControls in future
    public MouseControls(long windowHandle, CoordinatesConvertor coordinatesConvertor) {
        GLFW.glfwSetScrollCallback(windowHandle, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xOffset, double yOffset) {
                zoomFactor += (float) yOffset;
                zoomFactor = Math.max(1f, Math.min(zoomFactor, 100.0f));
            }
        });
        GLFW.glfwSetMouseButtonCallback(windowHandle, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                    isRotating = (action == GLFW.GLFW_PRESS);
                }
                else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                    System.out.println("Cursor X: " + lastMouseX);
                    System.out.println("Cursor Y: " + lastMouseY);
                    coordinatesConvertor.print3DCoordinates(lastMouseX, lastMouseY);
                }
            }
        });
        GLFW.glfwSetCursorPosCallback(windowHandle, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                Vector2d pos = scaleCursorPosIfNeeded(window, xPos, yPos);

                if (isRotating) {
                    deltaX += pos.x - lastMouseX;
                    deltaY += pos.y - lastMouseY;
                }
                lastMouseX = pos.x;
                lastMouseY = pos.y;
            }
        });
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }

    private Vector2d scaleCursorPosIfNeeded(long window, double xPos, double yPos) {
        Vector2d cursor = new Vector2d(xPos, yPos);
        String osName = System.getProperty("os.name", "");

        if (osName.contains("Windows")) {
            return cursor;
        }

        float[] xScale = new float[1];
        float[] yScale = new float[1];
        GLFW.glfwGetWindowContentScale(window, xScale, yScale);
        xPos *= xScale[0];
        yPos *= yScale[0];
        cursor.set(xPos, yPos);

        return cursor;
    }
}
