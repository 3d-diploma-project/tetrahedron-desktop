package org.cmps.tetrahedron.utils;

import javafx.scene.Scene;
import javafx.scene.input.MouseButton;

public class MouseControls {

    private float zoomFactor = 1.0f;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    // Most likely we will remove coordinatesConvertor from MouseControls in future
    public MouseControls(Scene scene, CoordinatesConvertor coordinatesConvertor) {
        scene.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                return;
            }
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            deltaX += (x - lastMouseX);
            deltaY += (y - lastMouseY);
            lastMouseX = x;
            lastMouseY = y;
        });

        scene.setOnMousePressed((mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                return;
            }
            lastMouseX = (int) mouseEvent.getX();
            lastMouseY = (int) mouseEvent.getY();
        }));

        scene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                return;
            }
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            System.out.println("Cursor X: " + x);
            System.out.println("Cursor Y: " + y);
            coordinatesConvertor.print3DCoordinates(x, y);
        });

        scene.setOnScroll(scrollEvent -> {
            zoomFactor += (float) scrollEvent.getDeltaY() / 100;
            zoomFactor = Math.max(1f, Math.min(zoomFactor, 100.0f));
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

//    private Vector2d scaleCursorPosIfNeeded(long window, double xPos, double yPos) {
//        Vector2d cursor = new Vector2d(xPos, yPos);
//        String osName = System.getProperty("os.name", "");
//
//        if (osName.contains("Windows")) {
//            return cursor;
//        }
//
//        float[] xScale = new float[1];
//        float[] yScale = new float[1];
//        GLFW.glfwGetWindowContentScale(window, xScale, yScale);
//        xPos *= xScale[0];
//        yPos *= yScale[0];
//        cursor.set(xPos, yPos);
//
//        return cursor;
//    }
}
