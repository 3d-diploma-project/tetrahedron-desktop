package org.cmps.tetrahedron.view.common;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.cmps.tetrahedron.viewmodel.DimensionViewModel;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class DimensionSelector {

    private final DimensionViewModel dimensionViewModel = DimensionViewModel.getInstance();

    @FXML
    private ComboBox<String> dimension;

    @FXML
    public void initialize() {
        dimension.setValue(dimensionViewModel.getDimension());
        dimensionViewModel.bind(dimension.getSelectionModel().selectedItemProperty());
    }
}
