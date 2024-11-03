package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.config.WindowProperties;
import org.joml.*;
import org.lwjgl.opengl.GL11C;

import java.lang.Math;
import java.util.Map;

public class CoordinatesConvertor {

    private final Matrix4f projMatrix;
    private final Matrix4x3f viewMatrix;

    private final Map<Integer, float[]> vertices;

    public CoordinatesConvertor(Matrix4f projMatrix, Matrix4x3f viewMatrix, Map<Integer, float[]> vertices) {
        this.projMatrix = projMatrix;
        this.viewMatrix = viewMatrix;
        this.vertices = vertices;
    }

    public void print3DCoordinates(double mouseX, double mouseY) {
        final double maxAcceptableDistanceSqr = 1e-3;
        Vector3f mouseCords = convertScreenToWorld(mouseX, mouseY);
        System.out.println("Mouse coordinates -> X: " + mouseCords.x + ", Y: " + mouseCords.y + ", Z: " + mouseCords.z);

        for (int i = 1; i < vertices.size(); i++) {
            double xVertex = vertices.get(i)[0];
            double yVertex = vertices.get(i)[1];
            double zVertex = vertices.get(i)[2];

            double distanceSqr = Math.pow(xVertex - mouseCords.x, 2)
                    + Math.pow(yVertex - mouseCords.y, 2)
                    + Math.pow(zVertex - mouseCords.z, 2);

            if (distanceSqr < maxAcceptableDistanceSqr) {
                System.out.println("Key " + i + "-> X: " + xVertex + ", Y: " + yVertex + ", Z: " + zVertex);
            }
        }
    }

    private Vector3f convertScreenToWorld(double mouseX, double mouseY) {
        Matrix4f viewProjMatrix = new Matrix4f(projMatrix).mul(viewMatrix);
        float[] depth = new float[1];
        GL11C.glReadPixels((int) mouseX, (int) (WindowProperties.getHeight() - mouseY), 1, 1,
                           GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, depth);

        return viewProjMatrix.unproject((float) mouseX,
                                        (float) (WindowProperties.getHeight() - mouseY),
                                        depth[0],
                                        new int[]{0, 0, WindowProperties.getWidth(), WindowProperties.getHeight()},
                                        new Vector3f());
    }
}
