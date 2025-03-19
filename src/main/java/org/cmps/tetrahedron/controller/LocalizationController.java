package org.cmps.tetrahedron.controller;

import lombok.Getter;
import org.cmps.tetrahedron.enums.LocalizationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class LocalizationController {

    @Getter
    private static LocalizationController instance = new LocalizationController();
    private Locale currentLocale;
    @Getter
    private ResourceBundle bundle;
    private final List<LocalizationListener> listeners = new ArrayList<>();


    private LocalizationController() {
        currentLocale = Locale.getDefault();
        loadBundle();
    }

    private void loadBundle() {
        bundle = ResourceBundle.getBundle("i18n.right-toolbar", currentLocale);
    }

    public void setLocale(Locale locale) {
        currentLocale = locale;
        Locale.setDefault(locale);
        loadBundle();
        notifyListeners();
    }

    public void registerListener(LocalizationListener listener) {
        listeners.add(listener);
        listener.onUpdateLanguage(bundle);
    }

    private void notifyListeners() {
        for (LocalizationListener listener : listeners) {
            listener.onUpdateLanguage(bundle);
        }
    }
}
