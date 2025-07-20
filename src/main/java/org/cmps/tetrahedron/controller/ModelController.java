package org.cmps.tetrahedron.controller;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.CustomCharacteristic;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.utils.DataReader;
import org.cmps.tetrahedron.view.ModelFilesPicker;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.io.File;
import java.util.*;

import static org.cmps.tetrahedron.utils.ColorUtils.matchColorsWithValues;

@Getter
public class ModelController {

    @Getter
    private static final ModelController instance = new ModelController();

    private Model model;
    @Setter
    private boolean modelReady = false;
    @Getter
    private CustomCharacteristic customCharacteristic;
    private List<float[]> modelColors = null;

    private ModelController() {
        model = Model.builder()
                     .vertices(new HashMap<>())
                     .faces(new ArrayList<>())
                     .build();
    }

    public void initModelData(File nodes, File indices) throws ModelValidationException, InternalValidationException {
        Map<Integer, float[]> vertices = DataReader.readVertices(nodes);

        model = Model.builder()
                     .vertices(vertices)
                     .faces(DataReader.readIndexesAndConvertToFaces(indices, vertices))
                     .build();
        modelReady = true;
    }

    public List<float[][]> getFaces() {
        return model.getFaces();
    }

    public Map<Integer, float[]> getVertices() {
        if (model == null) {
            throw new RuntimeException("Model is not initialized");
        }
        return model.getVertices();
    }

    public void initCustomCharacteristic(File customDataFile) throws ModelValidationException {
        customCharacteristic = DataReader.readCustomCharacteristic(customDataFile);

        LegendData.getInstance().updateValuesRange(customCharacteristic.getMinValue(), customCharacteristic.getMaxValue());

        customCharacteristic.setColors(matchColorsWithValues(customCharacteristic.getValues()));
        modelColors = customCharacteristic.getColors();
    }

    public void setModelColors(List<float[]> modelColors) {
        this.modelColors = modelColors;
        ColorSettings.getInstance().setColoredInSelectedColor(false);
        ModelController.getInstance().setModelReady(true);
    }

    public void clearModel() {
        modelReady = true;
        model.clear();

        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendData.getInstance().resetLegend();

        Platform.runLater(ModelFilesPicker::openDialogWindow);
    }

    public void clearDisplacement() {
        model.updateVertices(model.getOriginalVertices());
        setModelReady(true);
    }
}
