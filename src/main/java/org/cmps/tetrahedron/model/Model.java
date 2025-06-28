package org.cmps.tetrahedron.model;

import lombok.*;
import org.joml.Vector3f;

import java.util.*;

@Getter
public class Model {

    private final Map<Integer, float[]> originalVertices;
    private final Map<Integer, float[]> vertices;
    private final List<float[][]> faces;

    private Vector3f center;
    private Vector3f min;
    private Vector3f max;
    private float radius;

    @Builder
    public Model(Map<Integer, float[]> vertices, List<float[][]> faces) {
        this.vertices = vertices != null ? vertices : new HashMap<>();
        this.originalVertices = deepCopyVertices(this.vertices);
        this.faces = faces != null ? faces : new ArrayList<>();

        if (this.vertices.isEmpty()) {
            System.out.println("Model has no vertices");
            return;
        }

        calculateCenterAndBoundaries();
    }

    public void updateVertices(Map<Integer, float[]> vertices) {
        for (Map.Entry<Integer, float[]> entry : vertices.entrySet()) {
            float[] current = this.vertices.get(entry.getKey());
            float[] newValue = entry.getValue();

            current[0] = newValue[0];
            current[1] = newValue[1];
            current[2] = newValue[2];
        }

        calculateCenterAndBoundaries();
    }

    public void clear() {
        this.vertices.clear();
        this.originalVertices.clear();
        this.faces.clear();
    }

    private void calculateCenterAndBoundaries() {
        if (vertices == null || vertices.isEmpty()) {
            System.err.println("Model has no vertices");
            return;
        }

        Vector3f min = new Vector3f(vertices.get(1)[0], vertices.get(1)[1], vertices.get(1)[2]);
        Vector3f max = new Vector3f(vertices.get(1)[0], vertices.get(1)[1], vertices.get(1)[2]);

        for (float[] vertex : vertices.values()) {
            Vector3f v = new Vector3f(vertex[0], vertex[1], vertex[2]);
            min.min(v);
            max.max(v);
        }

        this.center = new Vector3f(min).add(max).mul(-0.5f);
        System.out.println("Model Center: " + center.x + ", " + center.y + ", " + center.z);

        this.min = min;
        this.max = max;
        this.radius = max.distance(min) / 2;
    }

    private Map<Integer, float[]> deepCopyVertices(Map<Integer, float[]> source) {
        Map<Integer, float[]> copy = new HashMap<>();
        for (Map.Entry<Integer, float[]> e : source.entrySet()) {
            float[] v = e.getValue();
            copy.put(e.getKey(), new float[]{v[0], v[1], v[2]});
        }
        return copy;
    }
}
