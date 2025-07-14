package org.cmps.tetrahedron.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.AccessibleAttribute;
import javafx.scene.layout.*;
import javafx.util.Pair;
import lombok.Getter;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.util.*;

public class LegendView extends VBox {

    private final BooleanProperty editable = new SimpleBooleanProperty(this, "editable", false);

    @Getter
    private List<LegendItem> itemControllers = new ArrayList<>();

    public LegendView() {
        this.setSpacing(-38);

        visibleProperty().bind(Legend.getInstance().getVisible());
        var itemsProperty = Legend.getInstance().getItems();
        itemsProperty.addListener(this::handleLegendUpdate);
        generateLegendItems(itemsProperty.get());
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case EDITABLE: return isEditable();
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    public final boolean isEditable() { return editable.getValue(); }

    public final void setEditable(boolean value) {
        editable.setValue(value);
        generateLegendItems(Legend.getInstance().getItems().get());
    }

    private void handleLegendUpdate(ObservableValue<? extends List<Legend.LegendItem>> observable,
                                    List<Legend.LegendItem> oldValue,
                                    List<Legend.LegendItem> newValue) {
        generateLegendItems(newValue);
        Legend.getInstance().setVisible(newValue != null);
    }

    private void generateLegendItems(List<Legend.LegendItem> items) {
        if (items == null) {
            return;
        }

        getChildren().clear();
        itemControllers.clear();
        var reversedLegendItems = items.reversed();

        for (int i = 0; i < items.size(); i++) {
            Legend.LegendItem itemData = reversedLegendItems.get(i);

            Pair<VBox, LegendItem> legendItem
                    = ResourceReader.readComponent("/view/component/LegendItem.fxml", VBox.class, LegendItem.class);
            LegendItem itemController = legendItem.getValue();

            itemController.setEditable(editable.getValue());
            itemController.setStart(itemData.min());
            itemController.setColor(itemData.color());
            if (i == items.size() - 1) {
                itemController.setEnd(itemData.max());
            }

            itemControllers.add(itemController);
            getChildren().add(legendItem.getKey());
        }

        //TODO: reverse legend in store and remove reverse from this method
        itemControllers = itemControllers.reversed();
        Legend.getInstance().setVisible(true);
    }
}
