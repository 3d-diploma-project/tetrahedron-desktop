package org.cmps.tetrahedron.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;
import org.cmps.tetrahedron.controller.FileChooserController;
import org.cmps.tetrahedron.controller.LocalizationController;
import org.cmps.tetrahedron.controller.SceneController;
import org.cmps.tetrahedron.i18n.LocalizationListener;

import java.io.File;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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
    private static final String LAST_USED_DIRECTORY_KEY = "last_used_directory";
    private static final Preferences prefs = Preferences.userNodeForPackage(FilePicker.class);

    private Runnable onFileSelectedCallback;

    public void setOnFileSelectedCallback(Runnable callback) {
        this.onFileSelectedCallback = callback;
    }

    @FXML
    public void onClick() {
        FileChooserController fileChooserController = FileChooserController.getInstance();
        FileChooser fileChooser = fileChooserController.createFileChooser();
        file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());

        if (file != null) {
            fileChooserController.saveLastUsedDirectory(file);
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
