package org.cmps.tetrahedron.enums;

import lombok.Getter;

public enum Dimension {
    TWO_D("2D"),
    THREE_D("3D");

    @Getter
    private final String label;

    Dimension(String label) {
        this.label = label;
    }

    public static Dimension getDimensionByLabel(String label) {
        for (Dimension dimension : values()) {
            if (dimension.label.equals(label)) {
                return dimension;
            }
        }
        return null;
    }
}
