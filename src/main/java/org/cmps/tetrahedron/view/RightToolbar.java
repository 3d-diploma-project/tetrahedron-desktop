package org.cmps.tetrahedron.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.SceneController;
import org.cmps.tetrahedron.utils.FontUtils;

import java.io.File;

public class RightToolbar extends VBox {

    public RightToolbar() {
        getStyleClass().add("right-toolbar");

        setupColourLayout();
        setupFileUploadSection("Напруження у вузлах", this::selectStressFile);
        setupFileUploadSection("Переміщення у вузлах", null);
        setupFileUploadSection("Прикладені сили", null);
        setupFileUploadSection("Закріплені вузли", null);
    }

    private void selectStressFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(SceneController.getScene().getWindow());
        if (file != null) {
            ModelController.getInstance().initStress(file);
        }
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
        HBox mainBox = new HBox();
        mainBox.getStyleClass().add("main-box-switches");

        HBox additionalBox = new HBox(6);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(additionalBox);
        AnchorPane.setLeftAnchor(additionalBox, 30d);
        VBox vLabels = new VBox(10);
        VBox vColours = new VBox(10);
        VBox vRGB = new VBox(10);

        addColours("Колір фону", "v-box-colour-1", vLabels, vColours, vRGB);
        addColours("Колір моделі", "v-box-colour-2", vLabels, vColours, vRGB);
        addColours("Колір вузлів", "v-box-colour-3", vLabels, vColours, vRGB);

        additionalBox.getChildren().addAll(vColours, vRGB);
        mainBox.getChildren().addAll(vLabels, anchorPane);
        getChildren().addAll(mainBox);
    }

    private void setupFileUploadSection(String text, EventHandler<ActionEvent> eventHandler) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 5 0 0 0;");
        label.setFont(FontUtils.getGeolocicaFont(10));

        Button button = new Button("Завантажити файл");
        button.getStyleClass().add("load-button");
        button.setFont(FontUtils.getGeolocicaFont(10));
        button.setOnAction(eventHandler);

        getChildren().addAll(label, button);
    }

    private void addCoordinates(String text) {
        Label label = new Label(text);
        label.setFont(FontUtils.getGeolocicaFont(16));

        HBox hBox = new HBox();
        Label label1 = new Label("X 0");
        Label label2 = new Label("Y 0");
        Label label3 = new Label("Z 0");
        label1.setFont(FontUtils.getGeolocicaFont(16));
        label2.setFont(FontUtils.getGeolocicaFont(16));
        label3.setFont(FontUtils.getGeolocicaFont(16));

        hBox.getStyleClass().add("h-box-coordinates");
        hBox.getChildren().addAll(label1, label2, label3);
        getChildren().addAll(label, hBox);
    }

    private void addToggleSwitches(String text, VBox vBoxLabels, VBox vBoxSwitches) {
        Label label = new Label(text);
        label.setFont(FontUtils.getGeolocicaFont(16));
        ToggleSwitch toggleSwitch = new ToggleSwitch();

        vBoxLabels.getChildren().add(label);
        vBoxSwitches.getChildren().add(toggleSwitch);
    }

    private void addColours(String text, String style, VBox vLabels, VBox vColours, VBox vRGB) {
        Label label = new Label(text);
        label.setFont(FontUtils.getGeolocicaFont(10));

        Region colorBox = new Region();
        colorBox.getStyleClass().add(style);
        colorBox.setPrefSize(14, 13);

        Label rgb = new Label("#FE720A");
        rgb.setFont(FontUtils.getGeolocicaFont(10));

        vLabels.getChildren().addAll(label);
        vColours.getChildren().add(colorBox);
        vRGB.getChildren().addAll(rgb);
    }
}
