package org.cmps.tetrahedron.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record LegendItemData(float[] color, float min, float max) implements Comparable<LegendItemData> {

    public LegendItemData(float max) {
        this(null, 0, max);
    }

    @Override
    public int compareTo(LegendItemData o) {
        return Float.compare(max, o.max);
    }
}
