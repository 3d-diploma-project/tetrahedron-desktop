package org.cmps.tetrahedron.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class InfoPanel extends HBox {

    Label logoText = new Label("Click on a vertex");

    public InfoPanel() {
        getChildren().add(logoText);
    }

    public void setText(String text) {
        logoText.setText(text);
    }
}
