package org.cmps.tetrahedron.model;

import lombok.Data;
import lombok.Getter;

@Data
public class ModelSettings {

    @Getter
    private static final ModelSettings instance = new ModelSettings();

    private boolean showElementMesh = true;
}
