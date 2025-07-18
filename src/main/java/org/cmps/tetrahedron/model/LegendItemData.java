package org.cmps.tetrahedron.model;

public record LegendItemData(float[] color, float min, float max) implements Comparable<LegendItemData> {

    public LegendItemData(float min) {
        this(null, min, 0);
    }

    @Override
    public int compareTo(LegendItemData o) {
        return Float.compare(min, o.min);
    }
}
