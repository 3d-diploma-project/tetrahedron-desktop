package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.Main;
import org.joml.*;
import org.lwjgl.opengl.GL11C;

import java.lang.Math;


public class CoordinatesConvertor {

    private Main main;
    private final MouseControls mouseControls;

    public CoordinatesConvertor(Main main, MouseControls mouseControls) {
        this.main = main;
        this.mouseControls = mouseControls;
    }

    public Vector3f convertScreenToWorld(double mouseX, double mouseY) {
        Matrix4f viewProjMatrix = new Matrix4f(main.projMatrix).mul(main.viewMatrix);
        float[] depth = new float[1];
        GL11C.glReadPixels((int) mouseX, (int) (main.height - mouseY), 1, 1, GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, depth);

        return viewProjMatrix.unproject((float) mouseX, (float) (main.height - mouseY), depth[0], new int[] {0, 0, main.width, main.height}, new Vector3f());
    }

    public void print3DCoordinates() {
        final double maxAcceptableDistanceSqr = 1e-3;
        double xMouse = mouseControls.getLastMouseX();
        double yMouse = mouseControls.getLastMouseY();
        Vector3f mouseCords = convertScreenToWorld(xMouse, yMouse);
        System.out.println("Mouse coordinates -----> X: " + mouseCords.x + ", Y: " + mouseCords.y + ", Z: " +mouseCords.z);

        for (int i = 1; i < main.vertices.size(); i++) {
            double xVertex = main.vertices.get(i)[0];
            double yVertex = main.vertices.get(i)[1];
            double zVertex = main.vertices.get(i)[2];

            double distanceSqr = Math.pow(xVertex - mouseCords.x, 2)
                           + Math.pow(yVertex - mouseCords.y, 2)
                           + Math.pow(zVertex - mouseCords.z, 2);

            if (distanceSqr < maxAcceptableDistanceSqr) {
                System.out.println("Key " + i + "-----> X: " + xVertex + ", Y: " + yVertex + ", Z: " + zVertex);
            }
        }
    }

}
