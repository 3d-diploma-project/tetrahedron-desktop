package org.cmps.tetrahedron.controller;

import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.view.InfoPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Objects;

public class MouseController extends MouseAdapter {

    @Getter
    private static final MouseController instance = new MouseController();

    @Getter
    private float zoomFactor = 1.0f;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;
    @Getter
    @Setter
    private String verticalMoveMode = "cursor";

    private MouseController() {
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        VertexInfoController.getInstance().setDisplayInfo(InfoPanel.getInstance()::setText);
        VertexInfoController.getInstance().setClickCoords(x, y);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        switch (verticalMoveMode) {
            case "upDown":
                deltaY += (y - lastMouseY);
                break;
            case "leftRight":
                deltaX += (x - lastMouseX);
                break;
            default:
                deltaX += (x - lastMouseX);
                deltaY += (y - lastMouseY);
        }

        lastMouseX = x;
        lastMouseY = y;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        lastMouseX = mouseEvent.getX();
        lastMouseY = mouseEvent.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent scrollEvent) {
        if (scrollEvent.isShiftDown()) {
            return;
        }
        zoomFactor += (float) scrollEvent.getPreciseWheelRotation() / 50;
        zoomFactor = Math.max(1f, Math.min(zoomFactor, 500.0f));
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }

}
