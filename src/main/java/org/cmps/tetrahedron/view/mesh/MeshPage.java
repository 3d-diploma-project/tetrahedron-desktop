package org.cmps.tetrahedron.view.mesh;

import javafx.application.Platform;
import javafx.fxml.FXML;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.utils.NativesExtractor;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class MeshPage {

    @FXML
    private void initialize() {
        String gmshName = System.mapLibraryName("gmsh");
        Path gmshPath = NativesExtractor.getNativesDir().resolve(gmshName);
        System.load(gmshPath.toString());

        if (Objects.equals(System.getProperty("debug"), "true")) {
            initModelIfInDebug();
        } else {
            Platform.runLater(MeshFilesPickerDialog::openDialogWindow);
        }
    }

    private void initModelIfInDebug() {
        try {
            ModelController.getInstance()
                           .initModelData(new File("models/Vertices (model 1).txt"),
                                          new File("models/Indices (model 1).txt"));
        } catch (ModelValidationException | InternalValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
