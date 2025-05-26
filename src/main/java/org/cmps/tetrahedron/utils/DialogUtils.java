package org.cmps.tetrahedron.utils;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtils {

    public static void displayDialogOnLeft(Pane content, double x, double y) {
        Stage dialog = buildDialogWindow(content);

        dialog.show();
        // -20 is a shift to have space between windows
        dialog.setX(x - content.getWidth() - 20);
        dialog.setY(y - content.getHeight() / 3);
    }

    public static void displayDialogOnRight(Pane content, double x, double y) {
        Stage dialog = buildDialogWindow(content);

        dialog.show();
        // +40 is a shift to have space between windows
        dialog.setX(x + 40);
        dialog.setY(y - content.getHeight() * 1);
    }

    private static Stage buildDialogWindow(Pane content) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        stage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                Platform.runLater(stage::close);
            }
        });

        return stage;
    }
}
