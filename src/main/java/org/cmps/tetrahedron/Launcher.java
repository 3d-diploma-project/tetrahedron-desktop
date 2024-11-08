package org.cmps.tetrahedron;

import javafx.application.Application;

/**
 * Don't know why, but it's required to have a main method in a separate file.
 * We can't have it inside of class which extends {@link Application}.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class Launcher {

    public static void main(String[] args) {
        Application.launch(TetrahedronApp.class, args);
    }
}
