package org.cmps.tetrahedron.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * A component for displaying a vertex info (number and coordinates).
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class InfoPanel extends HBox {

    @Getter
    private static final InfoPanel instance = new InfoPanel();

    Label nodeInfo = new Label("Click on a vertex");

    private InfoPanel() {
        getChildren().add(nodeInfo);
        nodeInfo.getStyleClass().add("info-text");
    }

    public void setText(String text) {
        nodeInfo.setText(text);
    }
}
