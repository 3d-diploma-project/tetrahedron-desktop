package org.cmps.tetrahedron.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.AccessibleAttribute;
import javafx.scene.layout.*;
import javafx.util.Pair;
import lombok.Getter;
import org.cmps.tetrahedron.model.LegendItemData;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.util.*;

public class Legend extends VBox {

    private final BooleanProperty editable = new SimpleBooleanProperty(this, "editable", false);

    @Getter
    private List<LegendItem> itemControllers = new ArrayList<>();

    public Legend() {
        visibleProperty().bind(LegendData.getInstance().getVisible());
        var itemsProperty = LegendData.getInstance().getItems();
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
        generateLegendItems(LegendData.getInstance().getItems().get());
    }

    private void handleLegendUpdate(ObservableValue<? extends List<LegendItemData>> observable,
                                    List<LegendItemData> oldValue,
                                    List<LegendItemData> newValue) {
        if (editable.get() && !itemControllers.isEmpty()) {
            // skip regeneration to let user update ranges manually
            return;
        }

        generateLegendItems(newValue);
        LegendData.getInstance().setVisible(newValue != null);
    }

    private void generateLegendItems(List<LegendItemData> items) {
        if (items == null) {
            this.setPadding(new Insets(0));
            return;
        }

        this.setPadding(new Insets(20, 0, 0, 0));

        getChildren().clear();
        itemControllers.clear();

        for (int i = 0; i < items.size(); i++) {
            LegendItemData itemData = items.get(i);

            Pair<VBox, LegendItem> legendItem
                    = ResourceReader.readComponent("/view/component/LegendItem.fxml", VBox.class, LegendItem.class);
            LegendItem itemController = legendItem.getValue();

            if (i != 0) {
                itemController.setEditable(editable.getValue());
            }
            itemController.setMax(itemData.max());
            itemController.setColor(itemData.color());
            if (i == items.size() - 1) {
                itemController.setMin(itemData.min());
            }

            itemControllers.add(itemController);
            getChildren().add(legendItem.getKey());
        }

        LegendData.getInstance().setVisible(true);
    }
}
