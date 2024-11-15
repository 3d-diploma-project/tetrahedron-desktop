package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.utils.Scaler;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Checks if a vertex present on clicked coordinates and display vertex info.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class VertexInfoController {

    private static final double MAX_ACCEPTABLE_DISTANCE_SQR = 0.002;

    private static final VertexInfoController instance = new VertexInfoController();

    private final CoordinatesConvertor coordinatesConvertor = CoordinatesConvertor.getInstance();
    private Consumer<String> displayInfo;

    private int x;
    private int y;
    private float depth;
    private boolean clicked;

    private String lastInfoToDisplay = "Click on a vertex";

    private VertexInfoController() {}

    public static VertexInfoController getInstance() {
        return instance;
    }

    public void setDisplayInfo(Consumer<String> displayInfo) {
        this.displayInfo = displayInfo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClickCoords(int x, int y) {
        this.x = Scaler.scaleByX(x);
        this.y = Scaler.scaleByY(y);
        clicked = true;
    }

    public void setDepthAndUpdateInfo(float depth) {
        this.depth = depth;
        Vector3f worldCoord = coordinatesConvertor.getWorldCoordinates(x, y, depth);
        lastInfoToDisplay = buildNodeInfo(worldCoord);

        Platform.runLater(() -> displayInfo.accept(lastInfoToDisplay));
        clicked = false;
    }

    /**
     * TODO: Make this search more efficient.
     */
    private String buildNodeInfo(Vector3f worldCoord) {
        for (Map.Entry<Integer, float[]> entry : DataReader.getVertices().entrySet()) {
            float[] vertex = entry.getValue();;
            double xVertex = vertex[0];
            double yVertex = vertex[1];
            double zVertex = vertex[2];

            double distanceSqr = Math.pow(xVertex - worldCoord.x, 2)
                    + Math.pow(yVertex - worldCoord.y, 2)
                    + Math.pow(zVertex - worldCoord.z, 2);

            if (distanceSqr < MAX_ACCEPTABLE_DISTANCE_SQR) {
                return "Key " + entry.getKey() + "-> X: " + xVertex + ", Y: " + yVertex + ", Z: " + zVertex;
            }
        }

        return "Clicked coords -> X: " + worldCoord.x + ", Y: " + worldCoord.y + ", Z: " + worldCoord.z + ", depth: " + depth;
    }
}
