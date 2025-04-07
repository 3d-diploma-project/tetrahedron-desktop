package org.cmps.tetrahedron.model;

import lombok.Data;
import lombok.Getter;

@Data
public class StressViewSettings {

    @Getter
    private static final StressViewSettings instance = new StressViewSettings();

    private boolean showMises = true;
    private String selectedComponent;
}
