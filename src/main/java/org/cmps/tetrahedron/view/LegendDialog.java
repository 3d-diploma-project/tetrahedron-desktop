package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.cmps.tetrahedron.utils.DialogUtils;
import org.cmps.tetrahedron.utils.ResourceReader;
import org.cmps.tetrahedron.view.component.LegendItem;

import java.util.List;

public class LegendDialog {

    @FXML
    private LegendView legend;

    public static void showDialog(double x, double y) {
        DialogPane pane = ResourceReader.readComponent("/view/LegendDialog.fxml", DialogPane.class);

        DialogUtils.displayDialogOnRight(pane, x, y);
    }

    public void updateLegend() {
        List<LegendItem> items = legend.getItemControllers();

        //TODO: validate legend items, build new Legend.legendItem list, update it in Legend viewmodel

        Stage stage = (Stage) legend.getScene().getWindow();
        stage.close();
    }
}
