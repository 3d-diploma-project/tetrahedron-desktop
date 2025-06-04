package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.i18n.LocalizationListener;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Consumer;

import static org.cmps.tetrahedron.controller.LocalizationController.VERTEX_INFO_BUNDLE;

/**
 * Checks if a vertex present on clicked coordinates and display vertex info.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class VertexInfoController implements LocalizationListener {

    @Getter
    private static final VertexInfoController instance = new VertexInfoController();

    private final ModelController modelController = ModelController.getInstance();
    private final CoordinatesConvertor coordinatesConvertor = CoordinatesConvertor.getInstance();
    private final LocalizationController localizationController = LocalizationController.getInstance();

    @Setter
    private Consumer<String> displayInfo;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private boolean clicked;

    private String lastInfoToDisplay = "";

    private VertexInfoController() {
        localizationController.registerListener(this);
        updateDefaultInfo();
    }

    public void setClickCoords(int x, int y) {
        this.x = x;
        this.y = y;
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
            return "";
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

        String template = localizationController.getString(VERTEX_INFO_BUNDLE, "closest-node-info");
        return String.format(Locale.ROOT, template, closestNode, orig[0], orig[1], orig[2]);
    }

    private float[] moveToOriginal(float[] vertex) {
        Vector3f center = modelController.getModel().getCenter();
        return new float[]{
                vertex[0] + center.x,
                vertex[1] + center.y,
                vertex[2] + center.z
        };
    }

    private void updateDefaultInfo() {
        lastInfoToDisplay = "";

        if (displayInfo != null) {
            Platform.runLater(() -> displayInfo.accept(lastInfoToDisplay));
        }
    }

    @Override
    public void onUpdateLanguage() {
        if (!clicked) {
            lastInfoToDisplay = buildNodeInfo(coordinatesConvertor.getWorldCoordinates(x, y, 1.0f));
        } else {
            lastInfoToDisplay = "";
        }

        if (displayInfo != null) {
            Platform.runLater(() -> displayInfo.accept(lastInfoToDisplay));
        }
    }
}
