package org.cmps.tetrahedron.model;

import lombok.Builder;

import java.util.Map;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
@Builder
public record TetraModelApi(Map<Integer, float[]> coordinates, int[][] indices) {

}
