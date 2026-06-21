package org.cmps.tetrahedron.view.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.viewmodel.FileLocationViewModel;

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

    @Setter
    private Runnable onFileSelectedCallback;

    @FXML
    public void onClick() {
        FileLocationViewModel fileLocationViewModel = FileLocationViewModel.getInstance();
        FileChooser fileChooser = fileLocationViewModel.createFileChooser();
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            fileLocationViewModel.saveLastUsedDirectory(file.getParentFile());
            label.setText(resources.getString("file-type") + ": " + file.getName());
            if (onFileSelectedCallback != null) {
                onFileSelectedCallback.run();
            }
        }
    }

    public void showNotSelectedFileError() {
        label.setText(resources.getString("file-is-not-selected"));
    }
}
