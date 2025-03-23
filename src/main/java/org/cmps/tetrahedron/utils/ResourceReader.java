package org.cmps.tetrahedron.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class ResourceReader {
    public static ImageView imageReader(String filePath) {
        InputStream input = ResourceReader.class.getResourceAsStream(filePath);
        if (input == null) {
            try {
                throw new FileNotFoundException("Image file was not found");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Image image = new Image(input);
        return new ImageView(image);
    }

    public static <T> T readComponent(String path, Class<T> type) {
        FXMLLoader loader = new FXMLLoader(ResourceReader.class.getResource(path));
        try {
            Node component = loader.load();
            return type.cast(component);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file: " + e.getMessage());
        }
    }

    public static <T> T readComponent(String path, Class<T> type, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(ResourceReader.class.getResource(path), resourceBundle);
        try {
            Node component = loader.load();
            return type.cast(component);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML file: " + e.getMessage());
        }
    }

}
