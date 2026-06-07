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
import org.cmps.tetrahedron.view.model.ModelFilesPickerDialog;
import org.cmps.tetrahedron.viewmodel.LegendData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ModelController {

    @Getter
    private static final ModelController instance = new ModelController();

    private Model model;
    @Setter
    private boolean modelReady = false;
    @Getter
    private CustomCharacteristic customCharacteristic;
    @Getter
    private List<Float> valuesToDisplay = null;

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
        LegendData.getInstance()
                  .updateValuesRange(customCharacteristic.getMinValue(), customCharacteristic.getMaxValue());
        setValuesToDisplay(customCharacteristic.getValues());
    }

    public void setValuesToDisplay(List<Float> valuesToDisplay) {
        this.valuesToDisplay = valuesToDisplay;

        ColorSettings.getInstance().setColoredInSelectedColor(false);
        modelReady = true;

        LegendData.getInstance().getItems().addListener((e1, e2, e3) -> {
            modelReady = true;
        });
    }

    public void clearModel() {
        valuesToDisplay = null;
        modelReady = true;
        model.clear();

        ColorSettings.getInstance().setColoredInSelectedColor(true);
        LegendData.getInstance().resetLegend();

        Platform.runLater(ModelFilesPickerDialog::openDialogWindow);
    }

    public void clearDisplacement() {
        model.updateVertices(model.getOriginalVertices());
        modelReady = true;
    }
}
