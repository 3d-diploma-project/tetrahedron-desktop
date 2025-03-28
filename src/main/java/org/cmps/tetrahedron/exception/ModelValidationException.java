package org.cmps.tetrahedron.exception;

import lombok.Getter;

/**
 * Exception which is used to show validation messages to user
 */
@Getter
public class ModelValidationException extends Exception {

    private final String message;
    private final String secondaryMessage;
    private final Object[] params;

    public ModelValidationException(String message, String secondaryMessage, Object... params) {
        this.message = message;
        this.secondaryMessage = secondaryMessage;
        this.params = params;
    }

    public ModelValidationException(String message, Object... params) {
        this(message, null, params);
    }
}
