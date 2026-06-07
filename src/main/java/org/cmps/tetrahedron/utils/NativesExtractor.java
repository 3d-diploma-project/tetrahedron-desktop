package org.cmps.tetrahedron.utils;

import lombok.Getter;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class NativesExtractor {

    private static final List<String> NATIVES = List.of("EGL", "GLESv2", "gmsh");

    public static Path NATIVES_DIR;

    public static Path getNativesDir() {
        if (NATIVES_DIR == null) {
            try {
                NATIVES_DIR = extractResourcesToTempDir();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return NATIVES_DIR;
    }

    private static Path extractResourcesToTempDir() throws IOException {
        Path destination = new TemporaryDirectory().getPath();
        Files.createDirectories(destination);

        for (String library : NativesExtractor.NATIVES) {
            String libraryName = System.mapLibraryName(library);
            String libraryPath = "/natives/" + libraryName;
            InputStream binary = NativesExtractor.class.getResourceAsStream(libraryPath);
            if (binary == null) {
                throw new IOException("Cannot find resource " + libraryPath);
            }

            Path fileDestination = destination.resolve(libraryName);
            Files.copy(binary, fileDestination, StandardCopyOption.REPLACE_EXISTING);

            binary.close();
        }

        return destination;
    }

    private static class TemporaryDirectory {

        private static final String TETRAHEDRON_PATH = "tetrahedron-natives";

        @Getter
        final Path path;

        public TemporaryDirectory() throws IOException {
            Path tempDir = Path.of(System.getProperty("java.io.tmpdir")).resolve(TETRAHEDRON_PATH);

            delete(tempDir);
            this.path = Files.createDirectory(tempDir);
        }

        private void delete(Path path) {
            if (!Files.exists(path)) {
                return;
            }

            try {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {
                    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                        Files.deleteIfExists(dir);
                        return super.postVisitDirectory(dir, e);
                    }

                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return super.visitFile(file, attrs);
                    }
                });
            } catch (IOException e) {
                System.err.println("Error deleting temporary directory: " + e.getMessage());
            }
        }
    }
}
