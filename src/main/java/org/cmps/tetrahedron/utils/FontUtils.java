package org.cmps.tetrahedron.utils;

import javafx.scene.text.Font;

public class FontUtils {

    private static final String GEOLOGICA_FONT_PATH = "/fonts/Geologica.ttf";

    public static Font getGeolocicaFont(int size) {
        return Font.loadFont(
                FontUtils.class.getResource(GEOLOGICA_FONT_PATH).toExternalForm(),
                size
        );
    }

}
