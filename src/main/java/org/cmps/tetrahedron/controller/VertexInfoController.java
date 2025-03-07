package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.cmps.tetrahedron.utils.Scaler;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Consumer;

/**
 * Checks if a vertex present on clicked coordinates and display vertex info.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class VertexInfoController {

    @Getter
    private static final VertexInfoController instance = new VertexInfoController();

    private final ModelController modelController = ModelController.getInstance();
    private final CoordinatesConvertor coordinatesConvertor = CoordinatesConvertor.getInstance();

    @Setter
    private Consumer<String> displayInfo;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private boolean clicked;

    private String lastInfoToDisplay = "Click on a vertex";

    private VertexInfoController() {}

    public void setClickCoords(int x, int y) {
        this.x = Scaler.scaleByX(x);
        this.y = Scaler.scaleByY(y);
        clicked = true;
    }

    public void updateVertexInfoToDisplay(float depth) {
        Vector3f worldCoord = coordinatesConvertor.getWorldCoordinates(x, y, depth);
        lastInfoToDisplay = buildNodeInfo(worldCoord);

        Platform.runLater(() -> displayInfo.accept(lastInfoToDisplay));
        clicked = false;
    }

    /**
     * TODO: Make this search more efficient.
     */
    private String buildNodeInfo(Vector3f worldCoord) {
        if (modelController.getVertices().isEmpty()) {
            return lastInfoToDisplay;
        }

        int closestNode = 0;
        double distanceToClosestNode = Double.MAX_VALUE;

        for (Map.Entry<Integer, float[]> entry : modelController.getVertices().entrySet()) {
            float[] vertex = entry.getValue();
            double distanceSqr = Math.pow(vertex[0] - worldCoord.x, 2)
                    + Math.pow(vertex[1] - worldCoord.y, 2)
                    + Math.pow(vertex[2] - worldCoord.z, 2);

            if (distanceSqr < distanceToClosestNode) {
                closestNode = entry.getKey();
                distanceToClosestNode = distanceSqr;
            }
        }

        float[] orig = modelController.getOriginalVertices().get(closestNode);

        return "Closest node " + closestNode + "-> X: " + orig[0]
                + ", Y: " + orig[1] + ", Z: " + orig[2];
    }

    private float[] moveToOriginal(float[] vertex) {
        Vector3f center = modelController.getModel().getCenter();
        return new float[]{
                vertex[0] + center.x,
                vertex[1] + center.y,
                vertex[2] + center.z
        };
    }
}
