package org.cmps.tetrahedron.utils;

import javafx.scene.text.Font;

public class Utils {

    private static final String GEOLOGICA_FONT_PATH = "/fonts/Geologica.ttf";

    public static Font getGeolocicaFont(int size) {
        return Font.loadFont(
                Utils.class.getResource(GEOLOGICA_FONT_PATH).toExternalForm(),
                size
        );
    }

}
