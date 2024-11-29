package org.cmps.tetrahedron.controller;

import org.cmps.tetrahedron.view.InfoPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseController extends MouseAdapter {

    private static final MouseController instance = new MouseController();

    private float zoomFactor = 1.0f;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    public static MouseController getInstance() {
        return instance;
    }

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
        deltaX += (x - lastMouseX);
        deltaY += (y - lastMouseY);
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

    public float getZoomFactor() {
        return zoomFactor;
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }
}
