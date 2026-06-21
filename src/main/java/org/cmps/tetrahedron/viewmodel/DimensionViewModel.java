package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import lombok.Getter;
import org.cmps.tetrahedron.enums.Dimension;

import java.util.Objects;

import static org.cmps.tetrahedron.enums.Dimension.TWO_D;

/**
 * Controls model dimension (2d or 3d).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class DimensionViewModel {

    @Getter
    private static final DimensionViewModel instance = new DimensionViewModel();

    private final ObjectProperty<String> dimension = new SimpleObjectProperty<>(TWO_D.getLabel());

    public Dimension getDimension() {
        return Dimension.getDimensionByLabel(dimension.get());
    }

    public boolean is2D() {
        return Objects.equals(dimension.getValue(), TWO_D.getLabel());
    }

    public void bind(ReadOnlyObjectProperty<String> dimensionProperty) {
        dimension.bind(dimensionProperty);
    }

    public void addDimensionListener(ChangeListener<String> listener) {
        dimension.addListener(listener);
    }
}
