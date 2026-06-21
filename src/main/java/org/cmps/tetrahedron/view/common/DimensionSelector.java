package org.cmps.tetrahedron.view.common;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.cmps.tetrahedron.viewmodel.DimensionViewModel;

public class DimensionSelector {

    private final DimensionViewModel dimensionViewModel = DimensionViewModel.getInstance();

    @FXML
    private ComboBox<String> dimension;

    @FXML
    public void initialize() {
        dimension.setValue(dimensionViewModel.getDimension().getLabel());
        dimensionViewModel.bind(dimension.getSelectionModel().selectedItemProperty());
    }
}
