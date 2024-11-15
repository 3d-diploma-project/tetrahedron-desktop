package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.config.CanvasProperties;
import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;

public class CoordinatesConvertor {

    private static volatile CoordinatesConvertor instance;

    private final Matrix4f projMatrix;
    private final Matrix4x3f viewMatrix;

    public CoordinatesConvertor(Matrix4f projMatrix, Matrix4x3f viewMatrix) {
        this.projMatrix = projMatrix;
        this.viewMatrix = viewMatrix;
    }

    public static void initInstance(Matrix4f projMatrix,
                                    Matrix4x3f viewMatrix) {
        instance = new CoordinatesConvertor(projMatrix, viewMatrix);
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
        Matrix4f viewProjMatrix = new Matrix4f(projMatrix).mul(viewMatrix);

        return viewProjMatrix.unproject(mouseX,
                                        CanvasProperties.getPhysicalHeight() - mouseY,
                                        depth,
                                        new int[]{0, 0, CanvasProperties.getPhysicalWidth(), CanvasProperties.getPhysicalHeight()},
                                        new Vector3f());
    }
}
