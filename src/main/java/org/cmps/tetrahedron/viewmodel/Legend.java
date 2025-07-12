package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Legend {

    @Getter
    private static final Legend instance = new Legend();

    @Getter
    private BooleanProperty visible = new SimpleBooleanProperty(false);
    @Getter
    private ObjectProperty<ValuesRange> valuesRange = new SimpleObjectProperty<>();

    private Legend() {
    }

    public void setVisible(boolean visibleValue) {
        visible.set(visibleValue);
    }

    public void updateLegend(float min, float max) {
        valuesRange.setValue(new ValuesRange(min, max));
    }

    public void resetLegend() {
        visible.set(false);
        valuesRange.setValue(null);
    }

    public record ValuesRange(float min, float max) {

    }
}
