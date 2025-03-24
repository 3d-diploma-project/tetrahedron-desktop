package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.controller.LocalizationController;

public class ErrorMessages {

    private static final String BUNDLE = LocalizationController.ERROR_DIALOG_BUNDLE;

    public static String ERROR, OK, CHECK;
    public static String CHECK_STRING, REPEATING_INDEX, NOT_FOUND_FILE, READ_NUMBER;
    public static String VERTICES_FACES_READ;
    public static String VERTICES_COUNT, VERTICES_READ;
    public static String FACES_COUNT, FACES_READ, FACES_NOT_EXIST;
    public static String DISPLACEMENTS_FORMAT, DISPLACEMENTS_NODES, DISPLACEMENTS_OUT_RANGE;
    public static String STRESS_FORMAT;
    public static String CHARACTERISTIC_NOT_FOUND, CHARACTERISTIC_READ_NUMBER, CHARACTERISTIC_IS_EMPTY;

    public static void loadMessages() {
        LocalizationController localization = LocalizationController.getInstance();

        ERROR = localization.getString(BUNDLE, "error");
        OK = localization.getString(BUNDLE, "ok");
        CHECK = localization.getString(BUNDLE, "check");

        CHECK_STRING = localization.getString(BUNDLE, "check-string");
        REPEATING_INDEX = localization.getString(BUNDLE, "repeating-index");
        NOT_FOUND_FILE = localization.getString(BUNDLE, "not-found-file");
        READ_NUMBER = localization.getString(BUNDLE, "read-number");

        VERTICES_FACES_READ = localization.getString(BUNDLE, "vertices-faces-read");

        VERTICES_COUNT = localization.getString(BUNDLE, "vertices-count");
        VERTICES_READ = localization.getString(BUNDLE, "vertices-read");

        FACES_COUNT = localization.getString(BUNDLE, "faces-count");
        FACES_READ = localization.getString(BUNDLE, "faces-read");
        FACES_NOT_EXIST = localization.getString(BUNDLE, "faces-not-exist");

        DISPLACEMENTS_FORMAT = localization.getString(BUNDLE, "displacements-format");
        DISPLACEMENTS_NODES = localization.getString(BUNDLE, "displacements-nodes");
        DISPLACEMENTS_OUT_RANGE = localization.getString(BUNDLE, "displacements-out-range");

        STRESS_FORMAT = localization.getString(BUNDLE, "stress-format");

        CHARACTERISTIC_NOT_FOUND = localization.getString(BUNDLE, "characteristic-not-found");
        CHARACTERISTIC_READ_NUMBER = localization.getString(BUNDLE, "characteristic-read-number");
        CHARACTERISTIC_IS_EMPTY = localization.getString(BUNDLE, "characteristic-is-empty");
    }
}
