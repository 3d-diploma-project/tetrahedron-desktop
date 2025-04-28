package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.i18n.LocalizationListener;

import java.util.*;


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
    public static final String VERTEX_INFO_BUNDLE = "i18n.vertex-info";


    private LocalizationController() {
        currentLocale = Locale.getDefault();
        loadBundles();
    }

    private void loadBundles() {
        bundles.clear();
        bundles.put(RIGHT_TOOLBAR_BUNDLE, ResourceBundle.getBundle(RIGHT_TOOLBAR_BUNDLE, currentLocale));
        bundles.put(INDEX_FILE_SELECTOR_BUNDLE, ResourceBundle.getBundle(INDEX_FILE_SELECTOR_BUNDLE, currentLocale));
        bundles.put(NODE_FILE_SELECTOR_BUNDLE, ResourceBundle.getBundle(NODE_FILE_SELECTOR_BUNDLE, currentLocale));
        bundles.put(ERROR_DIALOG_BUNDLE, ResourceBundle.getBundle(ERROR_DIALOG_BUNDLE, currentLocale));
        bundles.put(WARNING_DIALOG_BUNDLE, ResourceBundle.getBundle(WARNING_DIALOG_BUNDLE, currentLocale));
        bundles.put(MODEL_FILES_PICKER_BUNDLE, ResourceBundle.getBundle(MODEL_FILES_PICKER_BUNDLE, currentLocale));
        bundles.put(STRESS_DIALOG_BUNDLE, ResourceBundle.getBundle(STRESS_DIALOG_BUNDLE, currentLocale));
        bundles.put(VERTEX_INFO_BUNDLE, ResourceBundle.getBundle(VERTEX_INFO_BUNDLE, currentLocale));
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
