package org.cmps.tetrahedron.exception;

/**
 * Internal exception which is used to stop flow.
 */
public class InternalValidationException extends Exception {
    public InternalValidationException(String message) {
        super(message);
    }
}
