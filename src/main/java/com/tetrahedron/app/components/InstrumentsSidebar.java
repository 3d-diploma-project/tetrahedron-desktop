package com.tetrahedron.app.components;

import com.tetrahedron.app.utils.ResourceReader;
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
        return customButton;
    }

    public InstrumentsSidebar() {
        getStyleClass().add("sidebar");

        String[] iconsFilePaths = {"/toolsUI/cursor.png", "/toolsUI/move.png", "/toolsUI/reload.png", "/toolsUI/scale.png",
                "/toolsUI/copy.png", "/toolsUI/delete.png"};

        for (String iconFilePath : iconsFilePaths) {
            Button customSidebarBtn = customButtonWithImage(iconFilePath);
            getChildren().add(customSidebarBtn);
        }
    }
}

