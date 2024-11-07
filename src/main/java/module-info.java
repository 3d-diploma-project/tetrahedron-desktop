module org.cmps.tetrahedron {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens org.cmps.tetrahedron to javafx.fxml;
    exports org.cmps.tetrahedron;
    exports org.cmps.tetrahedron.components;
    opens org.cmps.tetrahedron.components to javafx.fxml;
    exports org.cmps.tetrahedron.utils;
    opens org.cmps.tetrahedron.utils to javafx.fxml;
}