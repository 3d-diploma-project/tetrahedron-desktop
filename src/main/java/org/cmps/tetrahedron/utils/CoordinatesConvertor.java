package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.config.CanvasProperties;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CoordinatesConvertor {

    private static CoordinatesConvertor instance;

    private final Matrix4f projMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelMatrix;

    public CoordinatesConvertor(Matrix4f projMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        this.projMatrix = projMatrix;
        this.viewMatrix = viewMatrix;
        this.modelMatrix = modelMatrix;
    }

    public static void initInstance(Matrix4f projMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        instance = new CoordinatesConvertor(projMatrix, viewMatrix, modelMatrix);
    }

    public static CoordinatesConvertor getInstance() {
        if (instance == null) {
            synchronized (CoordinatesConvertor.class) {
                if (instance == null) {
                    throw new RuntimeException("Init the class using getInstance with parameters");
                }
            }
        }

        return instance;
    }

    public Vector3f getWorldCoordinates(int mouseX, int mouseY, float depth) {
        Matrix4f viewProjMatrix = new Matrix4f(projMatrix).mul(viewMatrix).mul(modelMatrix);

        return viewProjMatrix.unproject(mouseX,
                                        mouseY,
                                        depth,
                                        new int[]{0, 0, CanvasProperties.getWidth(), CanvasProperties.getHeight()},
                                        new Vector3f());
    }
}
