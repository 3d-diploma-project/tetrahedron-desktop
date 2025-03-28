package org.cmps.tetrahedron.exception;

import org.cmps.tetrahedron.controller.LocalizationController;

public class ModelValidationException extends Exception {

    private static final LocalizationController local = LocalizationController.getInstance();
    private static final String errBundle = LocalizationController.ERROR_DIALOG_BUNDLE;

    public ModelValidationException(String key) {
        super(local.getString(errBundle, key));
    }

    public ModelValidationException(String key, int index) {
        super(local.getString(errBundle, key) + " \n" + index);
    }

    public ModelValidationException(String firstKey, String secondKey) {
        super(local.getString(errBundle, firstKey) + "\n\n" + local.getString(errBundle, secondKey));
    }

    public ModelValidationException(String firstKey, String secondKey, String line) {
        super(local.getString(errBundle, firstKey) + "\n\n" + local.getString(errBundle, secondKey) + " " + line);
    }

    public ModelValidationException(String firstKey, String secondKey, int index) {
        super(local.getString(errBundle, firstKey) + "\n\n" + local.getString(errBundle, secondKey) + " " + index);
    }

}
