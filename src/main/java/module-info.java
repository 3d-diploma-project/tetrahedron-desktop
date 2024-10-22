module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tetrahedron.app to javafx.fxml;
    exports com.tetrahedron.app;
}