package org.cmps.tetrahedron.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColorSettings {

    private static final float[] DEFAULT_MODEL_COLOR = new float[] { 0.395f, 0, 0.797f };
    private static final float[] DEFAULT_BACKGROUND_COLOR = new float[] { 0.93f, 0.956f, 0.992f };

    @Getter
    private static final ColorSettings instance = new ColorSettings();

    private float[] modelColor = DEFAULT_MODEL_COLOR;
    private float[] backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private boolean coloredInSelectedColor = true;
    private boolean grayscaleLegendTheme = false;
}
