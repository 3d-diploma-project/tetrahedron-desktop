package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.controller.LocalizationController;

public class WarningMessages {

    private static final String BUNDLE = LocalizationController.WARNING_DIALOG_BUNDLE;

    public static String ATTENTION, YES, NO, CONTINUE;
    public static String VERTICES_NOT_USED_IN_FACES, CANCEL_CONTINUE;


    public static void loadMessages() {
        LocalizationController localization = LocalizationController.getInstance();

        ATTENTION = localization.getString(BUNDLE, "attention");
        YES = localization.getString(BUNDLE, "yes");
        NO = localization.getString(BUNDLE, "no");
        CONTINUE = localization.getString(BUNDLE, "continue");

        VERTICES_NOT_USED_IN_FACES = localization.getString(BUNDLE, "vertices-not-used-in-faces");
        CANCEL_CONTINUE = localization.getString(BUNDLE, "cancel-continue");
    }

}

