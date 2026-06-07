package org.cmps.tetrahedron.view.common;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;

import java.util.function.Consumer;

public class Switch {

    @FXML
    private Button button;
    @FXML
    private Label label;

    @Getter
    private boolean state;
    Consumer<Boolean> updateState;

    @FXML
    void onClick() {
        state = !state;
        updateStyle();
        if (updateState != null) {
            updateState.accept(state);
        }
    }

    public void setInitialState(String text, Boolean state, Consumer<Boolean> updateState) {
        this.state = state;
        this.updateState = updateState;

        setLabel(text);
        updateStyle();
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setLabelStyle(String style) {
        label.setStyle(style);
    }

    private void updateStyle() {
        button.getStyleClass().removeAll("switch-on", "switch-off");
        button.getStyleClass().add(state ? "switch-on" : "switch-off");
    }
}
