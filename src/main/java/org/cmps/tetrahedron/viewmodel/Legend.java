package org.cmps.tetrahedron.viewmodel;

import javafx.beans.property.*;
import lombok.Getter;
import org.cmps.tetrahedron.enums.LegendTheme;

import java.util.ArrayList;
import java.util.List;

import static org.cmps.tetrahedron.enums.LegendTheme.RAINBOW;
import static org.cmps.tetrahedron.utils.ColorUtils.getGrayColors;
import static org.cmps.tetrahedron.utils.ColorUtils.getHSVColors;

@Getter
public class Legend {

    @Getter
    private static final Legend instance = new Legend();

    private final BooleanProperty visible = new SimpleBooleanProperty(false);
    private final ObjectProperty<List<LegendItem>> items = new SimpleObjectProperty<>();

    private int colorsCount = 7;
    private LegendTheme theme = RAINBOW;
    
    private float min;
    private float max;

    private Legend() {
    }

    public void setVisible(boolean visibleValue) {
        visible.set(visibleValue);
    }

    public void updateValuesRange(float min, float max) {
        this.min = min;
        this.max = max;
        regenerateLegend();
    }

    public void updateColorsCount(int colorsCount) {
        this.colorsCount = colorsCount;
        regenerateLegend();
    }

    public void updateTheme(LegendTheme theme) {
        this.theme = theme;
        regenerateLegend();
    }

    public void resetLegend() {
        visible.set(false);
        items.setValue(null);
    }

    private void regenerateLegend() {
        List<LegendItem> legendItems = new ArrayList<>();

        var colors = theme == RAINBOW ? getHSVColors(colorsCount) : getGrayColors(colorsCount);

        float valuesRange = max - min;
        float stressChunk = valuesRange / colorsCount;

        for (int i = 0; i < colorsCount; i++) {
            float[] color = colors.get(colorsCount - i - 1);

            float minValue = min + (stressChunk * i);
            float maxValue = minValue + stressChunk;
            if (i == colorsCount - 1) {
                maxValue = max;
            }

            LegendItem item = new LegendItem(color, minValue, maxValue);
            legendItems.add(item);
        }

        items.setValue(legendItems);
    }

    public record LegendItem(float[] color, float min, float max) implements Comparable<LegendItem> {

        public LegendItem(float min) {
            this(null, min, 0);
        }

        @Override
        public int compareTo(LegendItem o) {
            return Float.compare(min, o.min);
        }
    }
}
