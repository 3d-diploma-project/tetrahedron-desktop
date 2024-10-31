package com.tetrahedron.app.htmlApp;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class InstrumentsSidebar extends VBox {

    public InstrumentsSidebar() {
        getStyleClass().add("sidebar");

        // Добавляем кнопки в сайдбар
        for (int i = 0; i < 5; i++) {
            Circle button = new Circle(20, Color.BLUE); // Пример кнопки
            button.getStyleClass().add("sidebar-button");
            getChildren().add(button);
        }
    }
}

