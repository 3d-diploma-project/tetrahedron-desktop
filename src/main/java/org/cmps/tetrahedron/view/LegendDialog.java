package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.StressController;
import org.cmps.tetrahedron.enums.StressDisplayOption;
import org.cmps.tetrahedron.model.LegendItemData;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;
import org.cmps.tetrahedron.viewmodel.Legend;

import java.util.List;

public class LegendDialog {

    @FXML
    private LegendView legend;

    public static void showDialog(double x, double y) {
        DialogPane pane = ResourceReader.readComponent("/view/LegendDialog.fxml", DialogPane.class);

        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    public void updateLegend() {
        List<LegendItemData> items = legend.getItemControllers().stream().map(this::mapToLegendItem).toList();

        if (items.isEmpty()) {
            return;
        }

        float prev = items.getFirst().max();
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i).max() >= prev) {
                // set invalid error
                System.out.println("Invalid legend");
                return;
            }
            prev =  items.get(i).max();
        }

        LegendItemData lastLegendItem = items.getLast();
        if (lastLegendItem.min() >= lastLegendItem.max()) {
            System.out.println("Invalid legend");
            // set invalid error
            return;
        }

        Legend.getInstance().updateLegendItems(items);

        //TODO: process all data not just stress
        StressDisplayOption displayOption = StressController.getInstance().getStress().getDisplayOption();
        StressController.getInstance().processStressData(displayOption);

        Stage stage = (Stage) legend.getScene().getWindow();
        stage.close();
    }

    private LegendItemData mapToLegendItem(LegendItem legendItem) {
        return new LegendItemData(legendItem.getRgbColor(), legendItem.getEnd(), legendItem.getStart());
    }
}
