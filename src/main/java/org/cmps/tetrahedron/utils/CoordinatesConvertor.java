package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.Main;
import org.joml.*;
import org.lwjgl.opengl.GL11C;

import java.lang.Math;
import java.util.Map;


public class CoordinatesConvertor {

    private final MouseControls mouseControls;
    private final Matrix4f projMatrix;
    private final Matrix4x3f viewMatrix;
    private final int width;
    private final int height;
    private final Map<Integer, float[]> vertices;

    public CoordinatesConvertor(Matrix4f projMatrix, Matrix4x3f viewMatrix,
                                int width, int height,
                                Map<Integer, float[]> vertices,
                                MouseControls mouseControls) {
        this.projMatrix = projMatrix;
        this.viewMatrix = viewMatrix;
        this.width = width;
        this.height = height;
        this.vertices = vertices;
        this.mouseControls = mouseControls;
    }

    public Vector3f convertScreenToWorld(double mouseX, double mouseY) {
        Matrix4f viewProjMatrix = new Matrix4f(projMatrix).mul(viewMatrix);
        float[] depth = new float[1];
        GL11C.glReadPixels((int) mouseX, (int) (height - mouseY), 1, 1, GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, depth);

        return viewProjMatrix.unproject((float) mouseX,
                (float) (height - mouseY),
                depth[0],
                new int[] {0, 0, width, height},
                new Vector3f());
    }

    public void print3DCoordinates() {
        final double maxAcceptableDistanceSqr = 1e-3;
        double xMouse = mouseControls.getLastMouseX();
        double yMouse = mouseControls.getLastMouseY();
        Vector3f mouseCords = convertScreenToWorld(xMouse, yMouse);
        System.out.println("Mouse coordinates -----> X: " + mouseCords.x + ", Y: " + mouseCords.y + ", Z: " +mouseCords.z);

        for (int i = 1; i < vertices.size(); i++) {
            double xVertex = vertices.get(i)[0];
            double yVertex = vertices.get(i)[1];
            double zVertex = vertices.get(i)[2];

            double distanceSqr = Math.pow(xVertex - mouseCords.x, 2)
                           + Math.pow(yVertex - mouseCords.y, 2)
                           + Math.pow(zVertex - mouseCords.z, 2);

            if (distanceSqr < maxAcceptableDistanceSqr) {
                System.out.println("Key " + i + "-----> X: " + xVertex + ", Y: " + yVertex + ", Z: " + zVertex);
            }
        }
    }

}
