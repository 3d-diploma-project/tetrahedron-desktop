package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.enums.Language;
import org.cmps.tetrahedron.i18n.LocalizationListener;

import java.util.*;
import java.util.stream.Stream;


public class LocalizationController {

    @Getter
    private static LocalizationController instance = new LocalizationController();
    @Getter
    private Locale currentLocale;
    private final Map<String, ResourceBundle> bundles = new HashMap<>();
    private final List<LocalizationListener> listeners = new ArrayList<>();

    public static final String RIGHT_TOOLBAR_BUNDLE = "i18n.right-toolbar";
    public static final String INDEX_FILE_SELECTOR_BUNDLE = "i18n.index-file-selector";
    public static final String NODE_FILE_SELECTOR_BUNDLE = "i18n.node-file-selector";
    public static final String ERROR_DIALOG_BUNDLE = "i18n.error-dialog";
    public static final String WARNING_DIALOG_BUNDLE = "i18n.warning-dialog";
    public static final String MODEL_FILES_PICKER_BUNDLE = "i18n.model-files-picker";
    public static final String STRESS_DIALOG_BUNDLE = "i18n.stress-dialog";
    public static final String DEFORMATION_DIALOG_BUNDLE = "i18n.deformation-dialog";
    public static final String VERTEX_INFO_BUNDLE = "i18n.vertex-info";
    public static final String COLOR_PICKER_BUNDLE = "i18n.color-picker";


    private LocalizationController() {
        Language appLanguage = Language.getLanguage(Locale.getDefault());
        currentLocale = appLanguage.getLocale();
        loadBundles();
    }

    private void loadBundles() {
        bundles.clear();
        Stream.of(RIGHT_TOOLBAR_BUNDLE, INDEX_FILE_SELECTOR_BUNDLE, NODE_FILE_SELECTOR_BUNDLE,
                  ERROR_DIALOG_BUNDLE, WARNING_DIALOG_BUNDLE, MODEL_FILES_PICKER_BUNDLE,
                  STRESS_DIALOG_BUNDLE, VERTEX_INFO_BUNDLE, COLOR_PICKER_BUNDLE, DEFORMATION_DIALOG_BUNDLE)
              .forEach(bundle -> bundles.put(bundle, ResourceBundle.getBundle(bundle, currentLocale)));
    }

    public ResourceBundle getBundle(String name) {
        return bundles.get(name);
    }

    public String getString(String bundleName, String key) {
        ResourceBundle bundle = getBundle(bundleName);
        return (bundle != null && bundle.containsKey(key)) ? bundle.getString(key) : "!" + key + "!";
    }

    public void registerListener(LocalizationListener listener) {
        listeners.add(listener);
        listener.onUpdateLanguage();
    }

    public void setLocale(Locale locale) {
        currentLocale = locale;
        Locale.setDefault(locale);
        loadBundles();
        notifyListeners();
    }

    private void notifyListeners() {
        for (LocalizationListener listener : listeners) {
            listener.onUpdateLanguage();
        }
    }
}
