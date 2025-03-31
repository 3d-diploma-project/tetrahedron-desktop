package org.cmps.tetrahedron.utils;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtils {

    public static void displayDialogWindow(Pane content, double x, double y) {
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

        stage.show();
        stage.setX(x - content.getWidth() - 20);
        stage.setY(y - content.getHeight() / 3);
    }
}
