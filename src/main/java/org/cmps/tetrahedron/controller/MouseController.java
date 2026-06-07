package org.cmps.tetrahedron.controller;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.enums.VerticeMoveMode;
import org.cmps.tetrahedron.view.model.component.InfoPanel;

public class MouseController {

    @Getter
    private static final MouseController instance = new MouseController();
    @Getter
    private float zoomFactor = 2.5f;
    @Setter
    private VerticeMoveMode verticalMoveMode = VerticeMoveMode.CURSOR;

    private boolean wasDragged = false;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private double deltaX = 0;
    private double deltaY = 0;

    private MouseController() {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (wasDragged) {
            return;
        }

        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        VertexInfoController.getInstance().setDisplayInfo(InfoPanel.getInstance()::setText);
        VertexInfoController.getInstance().setClickCoords(x, y);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        wasDragged = true;
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        switch (verticalMoveMode) {
            case UP_DOWN:
                deltaY += (y - lastMouseY);
                break;
            case LEFT_RIGHT:
                deltaX += (x - lastMouseX);
                break;
            default:
                deltaX += (x - lastMouseX);
                deltaY += (y - lastMouseY);
        }

        lastMouseX = x;
        lastMouseY = y;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        lastMouseX = mouseEvent.getX();
        lastMouseY = mouseEvent.getY();
        wasDragged = false;
    }

    public void mouseWheelMoved(ScrollEvent scrollEvent) {
        if (scrollEvent.isShiftDown()) {
            return;
        }
        zoomFactor += (float) scrollEvent.getDeltaY() / 50;
        zoomFactor = Math.max(1f, Math.min(zoomFactor, 5.0f));
    }

    public float getY() {
        return (float) deltaY * 0.01f;
    }

    public float getX() {
        return (float) deltaX * 0.01f;
    }
}
