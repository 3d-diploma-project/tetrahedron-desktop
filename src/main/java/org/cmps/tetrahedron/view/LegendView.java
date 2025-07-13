package org.cmps.tetrahedron.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.util.*;

public class LegendView extends HBox {

    ObjectProperty<List<Legend.LegendItem>> valuesRange = new SimpleObjectProperty<>();

    private final VBox items = new VBox();

    public LegendView() {
        items.setSpacing(-38);

        getChildren().add(items);

        visibleProperty().bind(Legend.getInstance().getVisible());
        valuesRange.bind(Legend.getInstance().getItems());

        valuesRange.addListener(this::handleLegendUpdate);
    }

    private void handleLegendUpdate(ObservableValue<? extends List<Legend.LegendItem>> observable,
                                    List<Legend.LegendItem> oldValue,
                                    List<Legend.LegendItem> newValue) {
        items.getChildren().clear();

        if (newValue == null) {
            Legend.getInstance().setVisible(false);
            return;
        }

        for (int i = 0; i < newValue.size(); i++) {
            Legend.LegendItem itemData = newValue.get(i);

            Pair<VBox, LegendItem> legendItem
                    = ResourceReader.readComponent("/view/component/LegendItem.fxml", VBox.class, LegendItem.class);
            LegendItem itemController = legendItem.getValue();

            itemController.setStart(itemData.min());
            itemController.setColor(itemData.color());
            if (i == newValue.size() - 1) {
                itemController.setEnd(itemData.max());
            }

            items.getChildren().add(legendItem.getKey());
        }

        Legend.getInstance().setVisible(true);
    }
}
