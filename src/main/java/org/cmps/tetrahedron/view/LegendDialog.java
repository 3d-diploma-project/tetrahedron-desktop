package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.LegendItemData;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.view.component.Switch;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.cmps.tetrahedron.enums.LegendTheme.GREY;
import static org.cmps.tetrahedron.enums.LegendTheme.RAINBOW;

public class LegendDialog {

    private static final LocalizationController locale = LocalizationController.getInstance();

    @FXML
    private Label info;
    @FXML
    private Legend legend;
    @FXML
    private TextField legendCountInput;
    @FXML
    private Switch legendThemeController;

    public static void showDialog(double x, double y) {
        Locale.setDefault(locale.getCurrentLocale());

        DialogPane pane = ResourceReader.readComponent("/view/LegendDialog.fxml", DialogPane.class,
                                                       ResourceBundle.getBundle(LocalizationController.LEGEND_BUNDLE));
        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    @FXML
    private void initialize() {
        if (legend.getItemControllers().isEmpty()) {
            info.setText(locale.getString(LocalizationController.LEGEND_BUNDLE, "info-empty-legend"));
        } else {
            info.setText(locale.getString(LocalizationController.LEGEND_BUNDLE, "info-ranges-order"));
        }

        legendCountInput.setText(String.valueOf(LegendData.getInstance().getColorsCount()));

        legendThemeController.setInitialState(
                locale.getString(LocalizationController.LEGEND_BUNDLE, "legend-grey-theme"),
                LegendData.getInstance().getTheme() == GREY,
                null
        );
    }

    @FXML
    private void updateLegendSettings() {
        LegendData.getInstance().updateTheme(legendThemeController.isState() ? GREY : RAINBOW);

        boolean legendItemsCountChanged = updateLegendItemsCount();
        boolean legendItemsChanged = false;

        if (!legendItemsCountChanged) {
            legendItemsChanged = updateLegendItems();
        }

        if (legendItemsCountChanged || legendItemsChanged) {
            closeDialog();
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) legend.getScene().getWindow();
        stage.close();
    }

    private LegendItemData mapToLegendItem(LegendItem legendItem) {
        return new LegendItemData(legendItem.getRgbColor(), legendItem.getMin(), legendItem.getMax());
    }

    private boolean updateLegendItemsCount() {
        if (!validateLegendItemsCount()) {
            return false;
        }

        int itemsCount = Integer.parseInt(legendCountInput.getText());
        if (LegendData.getInstance().getColorsCount() == itemsCount) {
            return false;
        }

        LegendData.getInstance().updateColorsCount(itemsCount);
        return true;
    }

    private boolean validateLegendItemsCount() {
        try {
            int colorCount = Integer.parseInt(legendCountInput.getText());
            if (colorCount < 2 || colorCount > 15) {
                new ErrorDialog(new ModelValidationException("legend-range"));
                return false;
            }
        } catch (NumberFormatException e) {
            new ErrorDialog(new ModelValidationException("legend-format"));
            return false;
        }
        return true;
    }

    private boolean updateLegendItems() {
        List<LegendItemData> items = legend.getItemControllers().stream().map(this::mapToLegendItem).toList();
        if (items.isEmpty()) {
            return true;
        }

        boolean legendItemsValid = validateLegendItems(items);
        if  (!legendItemsValid) {
            info.getStyleClass().add("info-text-error");
            return false;
        } else {
            info.getStyleClass().removeAll("info-text-error");
        }

        LegendData.getInstance().updateLegendItems(items);
        return true;
    }

    private boolean validateLegendItems(List<LegendItemData> items) {
        float prev = items.getFirst().max();
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i).max() >= prev) {
                return false;
            }
            prev = items.get(i).max();
        }

        LegendItemData lastLegendItem = items.getLast();
        if (lastLegendItem.min() >= lastLegendItem.max()) {
            return false;
        }

        return true;
    }
}
