package org.cmps.tetrahedron.enums;

import lombok.Getter;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public enum MeshStatus {

    SUCCESS("success"),
    FAILURE("failure"),
    IN_PROGRESS("in-progress");

    @Getter
    private final String localizedTextId;

    MeshStatus(String localizedTextId) {
        this.localizedTextId = localizedTextId;
    }
}
