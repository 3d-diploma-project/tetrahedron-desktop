package org.cmps.tetrahedron.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.cmps.tetrahedron.utils.LegendUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.util.*;

public class LegendView extends HBox {

    ObjectProperty<Legend.ValuesRange> valuesRange = new SimpleObjectProperty<>();

    private final VBox items = new VBox();

    public LegendView() {
        items.setSpacing(-38);

        getChildren().add(items);

        visibleProperty().bind(Legend.getInstance().getVisible());
        valuesRange.bind(Legend.getInstance().getValuesRange());

        valuesRange.addListener(this::handleLegendUpdate);
    }

    private void handleLegendUpdate(ObservableValue<? extends Legend.ValuesRange> observable,
                                    Legend.ValuesRange oldValue,
                                    Legend.ValuesRange newValue) {
        items.getChildren().clear();

        if (newValue == null) {
            Legend.getInstance().setVisible(false);
            return;
        }

        var legend = LegendUtils.buildLegend(newValue.min(), newValue.max());

        while (!legend.isEmpty()) {
            Pair<VBox, LegendItem> legendItem
                    = ResourceReader.readComponent("/view/component/LegendItem.fxml", VBox.class, LegendItem.class);
            LegendItem itemController = legendItem.getValue();

            Map.Entry<Float, Integer> start = legend.pollFirstEntry();
            itemController.setStart(start.getKey());
            itemController.setColor(LegendUtils.getColorsLegend().get(start.getValue()));

            if (legend.firstEntry() == null) {
                itemController.setEnd(newValue.max());
            }

            items.getChildren().add(legendItem.getKey());
        }

        Legend.getInstance().setVisible(true);
    }
}
