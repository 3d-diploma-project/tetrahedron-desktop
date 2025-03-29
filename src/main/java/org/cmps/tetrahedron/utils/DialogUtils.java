package org.cmps.tetrahedron.utils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtils {

    public static Stage createModalWindow(Parent content) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

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
