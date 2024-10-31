module com.tetrahedron.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.tetrahedron.app to javafx.fxml;
    exports com.tetrahedron.app;
    exports com.tetrahedron.app.htmlApp;
    opens com.tetrahedron.app.htmlApp to javafx.fxml;
}