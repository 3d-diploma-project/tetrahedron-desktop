module com.tetrahedron.app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tetrahedron.app to javafx.fxml;
    exports com.tetrahedron.app;
}