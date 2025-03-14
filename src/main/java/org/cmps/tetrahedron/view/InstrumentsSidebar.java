package org.cmps.tetrahedron.view;

import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.utils.ResourceReader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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
                    customSidebarBtn.setOnAction(event -> {
                        MouseController.getInstance().setVerticalMoveMode("cursor");
                    });
                    break;
                case 1:
                    customSidebarBtn.setOnAction(event -> {
                        MouseController.getInstance().setVerticalMoveMode("upDown");
                    });
                    break;
                case 2:
                    customSidebarBtn.setOnAction(event -> {
                        MouseController.getInstance().setVerticalMoveMode("leftRight");
                    });
                    break;
            }

            getChildren().add(customSidebarBtn);
            index++;
        }
    }
}

