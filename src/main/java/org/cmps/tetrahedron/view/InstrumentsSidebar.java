package org.cmps.tetrahedron.view;

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
        return customButton;
    }

    public InstrumentsSidebar() {
        getStyleClass().add("sidebar");

        String[] iconsFilePaths = {"/icon/cursor.png", "/icon/move.png", "/icon/reload.png", "/icon/scale.png",
                "/icon/copy.png", "/icon/delete.png"};

        for (String iconFilePath : iconsFilePaths) {
            Button customSidebarBtn = customButtonWithImage(iconFilePath);
            getChildren().add(customSidebarBtn);
        }
    }
}

