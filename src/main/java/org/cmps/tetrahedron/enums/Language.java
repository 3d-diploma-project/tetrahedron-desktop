package org.cmps.tetrahedron.enums;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Language {
    UA("Українська", Locale.of("uk")),
    EN("English", Locale.of("en")),
    DE("Deutsch", Locale.of("de"));

    private final String displayName;
    private final Locale locale;

    Language(String displayName, Locale locale) {
        this.displayName = displayName;
        this.locale = locale;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
