package org.cmps.tetrahedron.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class DialogUtils {

    public static <T> T openModal(String fxmlPath, StageStyle style, boolean transparent) {
        try {
            URL resource = DialogUtils.class.getClassLoader().getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(resource);

            Scene scene = new Scene(loader.load());
            if (transparent) {
                scene.setFill(Color.TRANSPARENT);
            }

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(style);
            stage.setScene(scene);

            stage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                if(!stage.isFocused())
                    Platform.runLater(stage::close);
            });

            T controller = loader.getController();

            try {
                var field = controller.getClass().getDeclaredField("dialogStage");
                field.setAccessible(true);
                field.set(controller, stage);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}

            stage.showAndWait();
            return controller;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
