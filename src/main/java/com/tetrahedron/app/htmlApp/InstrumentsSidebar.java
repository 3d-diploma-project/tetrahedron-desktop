package com.tetrahedron.app.htmlApp;

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

        String[] iconsFilePaths = {"/cursor.png", "/move.png", "/reload.png", "/scale.png",
                "/copy.png", "/delete.png"};

        for (String iconFilePath : iconsFilePaths) {
            Button customSidebarBtn = customButtonWithImage(iconFilePath);
            getChildren().add(customSidebarBtn);
        }
    }
}

