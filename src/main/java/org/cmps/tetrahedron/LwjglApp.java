package org.cmps.tetrahedron;

import org.cmps.tetrahedron.config.WindowProperties;
import org.cmps.tetrahedron.modelview.Renderer;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.cmps.tetrahedron.utils.MouseControls;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import static org.cmps.tetrahedron.utils.DataReader.readIndexesAndConvertToFaces;
import static org.cmps.tetrahedron.utils.DataReader.readVerticesCoordinates;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class LwjglApp {

    private long window;

    private MouseControls mouseControls;
    private Renderer renderer;

    private final Map<Integer, float[]> vertices = readVerticesCoordinates();
    private final List<float[][]> faces = readIndexesAndConvertToFaces(vertices);

    public static void main(String[] args) {
        new LwjglApp().run();
    }

    public void run() {
        init();
        loop();

        glfwDestroyWindow(window);
    }

    private void init() {
        glfwInit();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WindowProperties.getWidth(), WindowProperties.getHeight(),
                                  "Wireframe rendering with geometry shader", NULL, NULL);

        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0
                        && (WindowProperties.getWidth() != width ||  WindowProperties.getHeight() != height)) {
                    WindowProperties.setWidth(width);
                    WindowProperties.setHeight(height);
                }
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - WindowProperties.getWidth()) / 2,
                         (vidMode.height() - WindowProperties.getHeight()) / 2);

        try (MemoryStack frame = stackPush()) {
            IntBuffer framebufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            WindowProperties.setWidth(framebufferSize.get(0));
            WindowProperties.setHeight(framebufferSize.get(1));
        }

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        glClearColor(0.918f, 0.956f, 1.0f, 1.0f);

        renderer = new Renderer(faces);
        CoordinatesConvertor coordinatesConvertor = new CoordinatesConvertor(renderer.getProjMatrix(),
                                                                             renderer.getViewMatrix(),
                                                                             vertices);
        mouseControls = new MouseControls(window, coordinatesConvertor);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glViewport(0, 0, WindowProperties.getWidth(), WindowProperties.getHeight());
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.updateMatrix(mouseControls.getZoomFactor(), mouseControls.getX(), mouseControls.getY());
            renderer.render();

            glfwSwapBuffers(window);
        }
    }
}