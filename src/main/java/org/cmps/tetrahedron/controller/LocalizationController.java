package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.interfaces.LocalizationListener;

import java.util.*;


public class LocalizationController {

    @Getter
    private static LocalizationController instance = new LocalizationController();
    private Locale currentLocale;
    private final Map<String, ResourceBundle> bundles = new HashMap<>();
    private final List<LocalizationListener> listeners = new ArrayList<>();


    private LocalizationController() {
        currentLocale = Locale.getDefault();
        loadBundles();
    }

    private void loadBundles() {
        bundles.put("right-toolbar", ResourceBundle.getBundle("i18n.right-toolbar", currentLocale));
        bundles.put("index-file-selector", ResourceBundle.getBundle("i18n.index-file-selector", currentLocale));
        bundles.put("node-file-selector", ResourceBundle.getBundle("i18n.node-file-selector", currentLocale));
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
