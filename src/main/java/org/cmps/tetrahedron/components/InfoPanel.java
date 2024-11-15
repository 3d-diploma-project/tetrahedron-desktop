package org.cmps.tetrahedron.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A component for displaying a vertex info (number and coordinates).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class InfoPanel extends HBox {

    Label nodeInfo = new Label("Click on a vertex");

    public InfoPanel() {
        getChildren().add(nodeInfo);
        nodeInfo.getStyleClass().add("info-text");
    }

    public void setText(String text) {
        nodeInfo.setText(text);
    }
}
