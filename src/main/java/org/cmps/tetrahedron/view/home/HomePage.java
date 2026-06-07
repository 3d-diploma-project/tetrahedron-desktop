package org.cmps.tetrahedron.view.home;

import javafx.application.Platform;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.exception.InternalValidationException;
import org.cmps.tetrahedron.exception.ModelValidationException;
import org.cmps.tetrahedron.router.Page;
import org.cmps.tetrahedron.router.Router;
import org.cmps.tetrahedron.view.model.ModelFilesPickerDialog;

import java.io.File;
import java.util.Objects;

public class HomePage {

    public void onClick() {
        Router.getInstance().openPage(Page.MODEL);

        if (Objects.equals(System.getProperty("debug"), "true")) {
            initModelIfInDebug();
        } else {
            Platform.runLater(ModelFilesPickerDialog::openDialogWindow);
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
