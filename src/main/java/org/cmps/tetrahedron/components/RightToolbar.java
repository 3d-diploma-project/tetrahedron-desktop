package org.cmps.tetrahedron.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.cmps.tetrahedron.utils.Utils;


public class RightToolbar extends VBox {

    public RightToolbar() {
        getStyleClass().add("right-toolbar");

        setupColourLayout();
        setupFileUploadSection("Напруження у вузлах");
        setupFileUploadSection("Переміщення у вузлах");
        setupFileUploadSection("Прикладені сили");
        setupFileUploadSection("Закріплені вузли");
    }

    private void setupLabelAndSwitchLayout() {
        HBox hBox = new HBox(90);
        hBox.getStyleClass().add("main-box-switches");

        VBox vBoxLabels = new VBox(15);
        VBox vBoxSwitches = new VBox(10);
        addToggleSwitches("Вузли", vBoxLabels, vBoxSwitches);
        addToggleSwitches("Номери вузлів", vBoxLabels, vBoxSwitches);
        addToggleSwitches("Легенда", vBoxLabels, vBoxSwitches);
        hBox.getChildren().addAll(vBoxLabels, vBoxSwitches);
        getChildren().addAll(hBox);
    }

    private void setupColourLayout() {
        HBox mainBox = new HBox(70);
        mainBox.getStyleClass().add("main-box-switches");
        VBox vLabels = new VBox(15);

        HBox additionalBox = new HBox(10);
        VBox vColours = new VBox(15);
        VBox vRGB = new VBox(18);

        addColours("Колір фону", "v-box-colour-1", vLabels, vColours, vRGB);
        addColours("Колір моделі", "v-box-colour-2", vLabels, vColours, vRGB);
        addColours("Колір вузлів", "v-box-colour-3", vLabels, vColours, vRGB);

        additionalBox.getChildren().addAll(vColours, vRGB);
        mainBox.getChildren().addAll(vLabels, additionalBox);
        getChildren().addAll(mainBox);
    }

    private void setupFileUploadSection(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 5 0 0 0;");
        label.setFont(Utils.getGeolocicaFont(16));
        Button button = new Button("Завантажити файл");
        button.getStyleClass().add("load-button");
        button.setFont(Utils.getGeolocicaFont(14));
        getChildren().addAll(label, button);
    }

    private void addCoordinates(String text) {
        Label label = new Label(text);
        label.setFont(Utils.getGeolocicaFont(16));

        HBox hBox = new HBox();
        Label label1 = new Label("X 0");
        Label label2 = new Label("Y 0");
        Label label3 = new Label("Z 0");
        label1.setFont(Utils.getGeolocicaFont(16));
        label2.setFont(Utils.getGeolocicaFont(16));
        label3.setFont(Utils.getGeolocicaFont(16));

        hBox.getStyleClass().add("h-box-coordinates");
        hBox.getChildren().addAll(label1, label2, label3);
        getChildren().addAll(label, hBox);
    }

    private void addToggleSwitches(String text, VBox vBoxLabels, VBox vBoxSwitches) {
        Label label = new Label(text);
        label.setFont(Utils.getGeolocicaFont(16));
        ToggleSwitch toggleSwitch = new ToggleSwitch();

        vBoxLabels.getChildren().add(label);
        vBoxSwitches.getChildren().add(toggleSwitch);
    }

    private void addColours(String text, String style, VBox vLabels, VBox vColours, VBox vRGB) {
        Label label = new Label(text);
        label.setFont(Utils.getGeolocicaFont(16));

        Region colorBox = new Region();
        colorBox.getStyleClass().add(style);
        colorBox.setPrefSize(20, 20);

        Label rgb = new Label("#FE720A");
        rgb.setFont(Utils.getGeolocicaFont(12));

        vLabels.getChildren().addAll(label);
        vColours.getChildren().add(colorBox);
        vRGB.getChildren().addAll(rgb);
    }
}
