package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseControls {

    private float zoomFactor = 1.0f;
    private Main main;
    private final ConvertTo3D convertTo3D;

    private boolean isRotating;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    public MouseControls(long windowHandle, Main main) {
        this.main = main;
        this.convertTo3D = new ConvertTo3D(main, this);

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
                    convertTo3D.get3Dcoordinate();
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

    public double getLastMouseX() {
        return lastMouseX;
    }

    public double getLastMouseY() {
        return lastMouseY;
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }
}
