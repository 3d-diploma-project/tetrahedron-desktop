package org.cmps.tetrahedron.view;

import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.utils.ResourceReader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class InstrumentsSidebar extends VBox {

    private Button customButtonWithImage(String filePath) {
        ImageView customButtonImage = ResourceReader.imageReader(filePath);
        Button customButton = new Button("", customButtonImage);
        customButtonImage.setFitWidth(24);
        customButtonImage.setFitHeight(24);
        customButton.getStyleClass().add("sidebar-button");
        customButton.setMinSize(40, 40);
        customButtonImage.setPickOnBounds(true);
        customButtonImage.setMouseTransparent(false);
        return customButton;
    }

    public InstrumentsSidebar() {
        getStyleClass().add("sidebar");

        String[] iconsFilePaths = {"/icon/cursor.png", "/icon/upDown.png", "/icon/leftRight.png", "/icon/img.png",
                "/icon/filling.png", "/icon/delete.png"};

        int index = 0;
        for (String iconFilePath : iconsFilePaths) {
            Button customSidebarBtn = customButtonWithImage(iconFilePath);

            switch (index) {
                case 0:
                    customSidebarBtn.setOnAction(event -> MouseController.getInstance().setVerticalMoveMode("cursor"));
                    break;
                case 1:
                    customSidebarBtn.setOnAction(event -> MouseController.getInstance().setVerticalMoveMode("upDown"));
                    break;
                case 2:
                    customSidebarBtn.setOnAction(event -> MouseController.getInstance().setVerticalMoveMode("leftRight"));
                    break;
                case 4:
                    customSidebarBtn.setOnAction(event -> openColorPicker());
                    break;
            }

            getChildren().add(customSidebarBtn);
            index++;
        }
    }

    // TODO: Add design to ColorPicker
    private void openColorPicker() {
        Stage stage = new Stage();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();

            float[] colorArray = new float[]{
                    (float) selectedColor.getRed(),
                    (float) selectedColor.getGreen(),
                    (float) selectedColor.getBlue()
            };

            int faceCount = ModelController.getInstance().getFaces().size();
            List<float[]> colorsList = new ArrayList<>();
            for (int i = 0; i < faceCount; i++) {
                colorsList.add(colorArray);
            }

            ModelController.getInstance().setModelColors(colorsList);

            stage.close();
        });

        StackPane root = new StackPane(colorPicker);
        Scene scene = new Scene(root, 100, 40);
        stage.setScene(scene);
        stage.setTitle("Choose Model Color");
        stage.show();
    }

}

