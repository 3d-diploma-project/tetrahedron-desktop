package org.cmps.tetrahedron.utils;

import org.cmps.tetrahedron.model.TetraModelApi;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class ModelDimensionUtils {

    public static TetraModelApi addInfoAboutZeroCoordinateOrThrowException(TetraModelApi model) {
        float[] firstCoord = model.coordinates().get(1);

        Set<Integer> usedIn2dModelCoordinates = new HashSet<>();
        for (float[] coordinate : model.coordinates().values()) {
            for (int i = 0; i < coordinate.length; i++) {
                if (coordinate[i] != firstCoord[i]) {
                    usedIn2dModelCoordinates.add(i);
                }
            }
            if (usedIn2dModelCoordinates.size() == 3) {
                break;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!usedIn2dModelCoordinates.contains(i)) {
                return model.toBuilder()
                            .zeroCoordinateIndex(i)
                            .build();
            }
        }

        throw new RuntimeException("mesh-error-not-2d-mesh-loaded");
    }
}
