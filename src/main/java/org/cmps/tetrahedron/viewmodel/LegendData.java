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
        regenerateColors();
    }

    public void resetLegend() {
        visible.set(false);
        items.setValue(null);
    }

    public void updateLegendItems(List<LegendItemData> itemData) {
        items.setValue(itemData);
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

    private void regenerateColors() {
        if (items.getValue() == null) {
            return;
        }

        var colors = theme == RAINBOW ? getHSVColors(colorsCount) : getGrayColors(colorsCount);
        List<LegendItemData> legendItems = new ArrayList<>(items.getValue());

        for (int i = 0; i < colorsCount; i++) {
            LegendItemData item = legendItems.get(i)
                                             .toBuilder()
                                             .color(colors.get(i))
                                             .build();
            legendItems.set(i, item);
        }

        items.setValue(legendItems);
    }
}
