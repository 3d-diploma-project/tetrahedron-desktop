package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.*;
import lombok.Getter;

import java.util.Objects;

/**
 * Controls model dimension (2d or 3d).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class DimensionViewModel {

    @Getter
    private static final DimensionViewModel instance = new DimensionViewModel();

    private final ObjectProperty<String> dimension = new SimpleObjectProperty<>("2D");

    public String getDimension() {
        return dimension.get();
    }

    public boolean is2D() {
        return Objects.equals(dimension.getValue(), "2D");
    }

    public void bind(ReadOnlyObjectProperty<String> dimensionProperty) {
        dimension.bind(dimensionProperty);
    }
}
