package org.cmps.tetrahedron;

import org.cmps.tetrahedron.utils.MouseControls;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import static org.cmps.tetrahedron.utils.DataReader.readIndexesAndConvertToFaces;
import static org.cmps.tetrahedron.utils.DataReader.readVerticesCoordinates;
import static org.cmps.tetrahedron.utils.ShaderLoader.createShader;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class Main {

    private long window;
    private int width = 1024;
    private int height = 768;

    private int vao;

    private int viewportSizeUniform;
    private int viewMatrixUniform;

    private MouseControls mouseControls;
    private final Matrix4f viewMatrix = new Matrix4f();
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private final Map<Integer, float[]> vertices = readVerticesCoordinates();
    private final List<float[][]> faces = readIndexesAndConvertToFaces(vertices);

    public static void main(String[] args) {
        new Main().run();
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

        window = glfwCreateWindow(width, height, "Wireframe rendering with geometry shader", NULL, NULL);
        mouseControls = new MouseControls(window);

        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0
                        && (Main.this.width != width || Main.this.height != height)) {
                    Main.this.width = width;
                    Main.this.height = height;
                }
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        try (MemoryStack frame = stackPush()) {
            IntBuffer framebufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            width = framebufferSize.get(0);
            height = framebufferSize.get(1);
        }

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();

        glClearColor(0.55f, 0.75f, 0.95f, 1.0f);
        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);

        // Display only faces edges
//        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        /* Create all needed GL resources */
        createVao();
        createRasterProgram();
    }

    private void createVao() {
        // vao - Vertex Array Object
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);

        FloatBuffer pb = BufferUtils.createFloatBuffer(faces.size() * 3 * 3);
        for (float[][] face : faces) {
            for (float[] vertex : face) {
                pb.put(vertex[0]).put(vertex[1]).put(vertex[2]);
            }
        }
        pb.flip();

        // setup vertex positions buffer
        int posVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, posVbo);
        glBufferData(GL_ARRAY_BUFFER, pb, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
    }

    private void createRasterProgram() {
        int program = glCreateProgram();

        int vShader = createShader("shaders/vs.glsl", GL_VERTEX_SHADER);
        int fShader = createShader("shaders/fs.glsl", GL_FRAGMENT_SHADER);
        int gShader = createShader("shaders/gs.glsl", GL_GEOMETRY_SHADER);
        glAttachShader(program, vShader);
        glAttachShader(program, fShader);
        glAttachShader(program, gShader);

        glBindAttribLocation(program, 0, "position");
        glBindFragDataLocation(program, 0, "color");

        glLinkProgram(program);

        glUseProgram(program);
        viewMatrixUniform = glGetUniformLocation(program, "viewMatrix");
        viewportSizeUniform = glGetUniformLocation(program, "viewportSize");
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glViewport(0, 0, width, height);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            updateMatrix();
            render();

            glfwSwapBuffers(window);
        }
    }

    private void updateMatrix() {
        final Vector3f position = new Vector3f(0.5f, 0, 3).negate();
        Matrix4f modelMatrix = new Matrix4f()
                .translate(position)
                .rotate(mouseControls.getRotation());

        float fov = (float) Math.toRadians(60 / mouseControls.getZoomFactor());
        viewMatrix.setPerspective(fov, (float) width / height, 0.1f, 1000.0f)
                .mul(modelMatrix);
    }

    private void render() {
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.get(matrixBuffer));
        glUniform2f(viewportSizeUniform, width, height);

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, faces.size() * 3);
        glBindVertexArray(0);
    }
}