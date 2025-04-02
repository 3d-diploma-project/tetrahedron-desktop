package org.cmps.tetrahedron.view.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.function.Consumer;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Switch {

    @FXML
    private Button button;
    @FXML
    private Label label;

    private boolean state;
    Consumer<Boolean> updateState;

    @FXML
    void onClick() {
        state = !state;
        updateStyle();
        updateState.accept(state);
    }

    public void setInitialState(String text, Boolean state, Consumer<Boolean> updateState) {
        setLabel(text);
        this.state = state;
        updateStyle();
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    private void updateStyle() {
        button.getStyleClass().removeAll("switch-on", "switch-off");
        button.getStyleClass().add(state ? "switch-on" : "switch-off");
    }
}
