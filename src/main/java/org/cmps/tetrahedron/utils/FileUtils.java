package org.cmps.tetrahedron.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileUtils {

    public static File createFile(File directory, String fileName) {
        File file = Paths.get(directory.getAbsolutePath(), fileName).toFile();

        try {
            if (file.createNewFile()) {
                System.out.println("File '" + fileName + "' is created");
            } else {
                System.out.println("File '" + fileName + "' already exists");
            }
        } catch (IOException ex) {
            System.err.println("Cannot create or access file: " + fileName);
            throw new RuntimeException("Cannot create or access file: " + fileName);
        }

        return file;
    }
}
