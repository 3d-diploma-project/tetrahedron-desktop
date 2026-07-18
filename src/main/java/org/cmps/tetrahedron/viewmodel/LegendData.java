package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import org.cmps.tetrahedron.enums.LegendTheme;
import org.cmps.tetrahedron.model.LegendItemData;

import java.util.ArrayList;
import java.util.List;

import static org.cmps.tetrahedron.enums.LegendTheme.RAINBOW;
import static org.cmps.tetrahedron.utils.ColorUtils.getGrayColors;
import static org.cmps.tetrahedron.utils.ColorUtils.getHSVColors;

@Getter
public class LegendData {

    @Getter
    private static final LegendData instance = new LegendData();

    private final BooleanProperty visible = new SimpleBooleanProperty(false);
    private final ObjectProperty<List<LegendItemData>> items = new SimpleObjectProperty<>();

    private int colorsCount = 7;
    private LegendTheme theme = RAINBOW;

    private float min;
    private float max;

    private LegendData() {
    }

    public void setVisible(boolean visibleValue) {
        visible.set(visibleValue);
    }

    public void updateValuesRange(float min, float max) {
        if (this.min == min && this.max == max) {
            visible.set(true);
            return;
        }

        this.min = min;
        this.max = max;
        generateLegend();
    }

    public void updateColorsCount(int colorsCount) {
        this.colorsCount = colorsCount;
        if (items.getValue() == null) {
            return;
        }
        generateLegend();
    }

    public void updateTheme(LegendTheme theme) {
        this.theme = theme;

        if (items.getValue() == null) {
            return;
        }

        var colors = theme == RAINBOW ? getHSVColors(colorsCount) : getGrayColors(colorsCount);
        List<LegendItemData> legendItems = new ArrayList<>(items.getValue());

        for (int i = 0; i < colorsCount; i++) {
            LegendItemData item = legendItems.get(i).toBuilder().color(colors.get(i)).build();
            legendItems.set(i, item);
        }

        items.setValue(legendItems);
    }

    public void resetLegend() {
        min = 0;
        max = 0;

        visible.set(false);
        items.setValue(null);
    }

    public void updateLegendItems(List<LegendItemData> itemData) {
        List<LegendItemData> newItems = new ArrayList<>(itemData);

        // set max and min value to not have loses when parsing text from input
        LegendItemData firstItem = itemData.getFirst().toBuilder().max(max).build();
        newItems.set(0, firstItem);
        LegendItemData lastItem = itemData.getLast().toBuilder().min(min).build();
        newItems.set(itemData.size() - 1, lastItem);

        // preserve currently set colors
        for (int i = 0; i < itemData.size(); i++) {
            LegendItemData oldItem = items.get().get(i);
            LegendItemData newItem = itemData.get(i).toBuilder().color(oldItem.color()).build();
            newItems.set(i, newItem);
        }

        items.setValue(newItems);
    }

    private void generateLegend() {
        List<LegendItemData> legendItemData = new ArrayList<>();

        var colors = theme == RAINBOW ? getHSVColors(colorsCount) : getGrayColors(colorsCount);

        float valuesRange = max - min;
        float stressChunk = valuesRange / colorsCount;

        for (int i = colorsCount - 1; i >= 0; i--) {
            float[] color = colors.get(colorsCount - i - 1);

            float minValue = min + (stressChunk * i);
            float maxValue = minValue + stressChunk;
            if (i == colorsCount - 1) {
                maxValue = max;
            }

            LegendItemData item = new LegendItemData(color, minValue, maxValue);
            legendItemData.add(item);
        }

        items.setValue(legendItemData);
    }
}
