package org.cmps.tetrahedron.model;

import lombok.Data;
import lombok.Getter;

@Data
public class ModelViewSettings {

    @Getter
    private static final ModelViewSettings instance = new ModelViewSettings();

    private boolean showElementMesh = true;
}
