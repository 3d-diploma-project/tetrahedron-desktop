package org.cmps.tetrahedron.utils;

import lombok.Getter;
import org.lwjgl.system.Platform;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

public class NativesExtractor {

    private static final List<String> WINDOWS_NATIVES = List.of("/natives/libEGL.dll", "/natives/libGLESv2.dll");
    private static final List<String> MACOS_NATIVES = List.of("/natives/libEGL.dylib", "/natives/libGLESv2.dylib");
    private static final List<String> LINUX_NATIVES = List.of("/natives/libEGL.so", "/natives/libGLESv2.so");

    public static Path extractNatives() {
        try {
            return switch (Platform.get()) {
                case LINUX -> extractResourcesToTempDir(LINUX_NATIVES);
                case MACOSX -> extractResourcesToTempDir(MACOS_NATIVES);
                case WINDOWS -> extractResourcesToTempDir(WINDOWS_NATIVES);
                default -> throw new RuntimeException("Unsupported platform: " + Platform.get());
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Path extractResourcesToTempDir(List<String> resourcePaths) throws IOException {
        Path destination = new TemporaryDirectory().getPath();
        Files.createDirectories(destination);

        for (String resourcePath : resourcePaths) {
            InputStream binary = NativesExtractor.class.getResourceAsStream(resourcePath);
            if (binary == null) {
                throw new IOException("Cannot find resource " + resourcePath);
            }

            Path fileName = Paths.get(resourcePath).getFileName();
            Path fileDestination = destination.resolve(fileName);
            Files.copy(binary, fileDestination, StandardCopyOption.REPLACE_EXISTING);

            binary.close();
        }

        return destination;
    }

    private static class TemporaryDirectory {

        private static final String TETRAHEDRON_PREFIX = "tetrahedron-";

        @Getter
        final Path path;

        public TemporaryDirectory() throws IOException {
            this.path = Files.createTempDirectory(TETRAHEDRON_PREFIX);

            if (Platform.get() == Platform.WINDOWS) {
                deleteOldInstancesOnStart();
            } else {
                markDeleteOnExit();
            }
        }

        private void deleteOldInstancesOnStart() {
            Path tempDirectory = this.path.getParent();

            try (Stream<Path> paths = Files.walk(tempDirectory)) {
                paths.filter(Files::isDirectory)
                        .filter(path -> path.getFileName().toString().startsWith(TETRAHEDRON_PREFIX))
                        .forEach(this::delete);
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
        }

        private void markDeleteOnExit() {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> this.delete(this.path)));
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
