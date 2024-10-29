package org.cmps.tetrahedron.utils;

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

    public MouseControls(long windowHandle) {
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
                if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    isRotating = (action == GLFW.GLFW_PRESS);
                }
            }
        });
        GLFW.glfwSetCursorPosCallback(windowHandle, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                if (isRotating) {
                    deltaX += xPos - lastMouseX;
                    deltaY += yPos - lastMouseY;
                }
                lastMouseX = xPos;
                lastMouseY = yPos;
            }
        });
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public float getY() {
        return (float) deltaY * 0.1f;
    }

    public float getX() {
        return (float) deltaX * 0.1f;
    }
}
