package org.cmps.tetrahedron.enums;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Language {
    UA(Locale.of("uk")),
    EN(Locale.of("en")),
    DE(Locale.of("de")),
    PL(Locale.of("pl"));

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public static Language getLanguage(Locale locale) {
        for (Language language : Language.values()) {
            if (language.locale.getLanguage().equals(locale.getLanguage())) {
                return language;
            }
        }

        return getDefaultLanguage();
    }

    public static Language getDefaultLanguage() {
        return EN;
    }
}
