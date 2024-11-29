package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.Getter;
import org.cmps.tetrahedron.controller.SceneController;

import java.io.File;
import java.util.ResourceBundle;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
@Getter
public class FilePicker {

    @FXML
    private ResourceBundle resources;
    @FXML
    private Label label;

    private File file;

    @FXML
    public void onClick() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());
        label.setText(resources.getString("file-type") + ": " + file.getName());
    }

    public void showNotSelectedFileError() {
        label.setText(resources.getString("file-is-not-selected"));
    }
}
