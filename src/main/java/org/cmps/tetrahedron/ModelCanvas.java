package org.cmps.tetrahedron;

import javafx.scene.Scene;
import org.cmps.tetrahedron.config.WindowProperties;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.cmps.tetrahedron.utils.MouseControls;
import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import static org.cmps.tetrahedron.utils.DataReader.readIndexesAndConvertToFaces;
import static org.cmps.tetrahedron.utils.DataReader.readVerticesCoordinates;
import static org.cmps.tetrahedron.utils.ShaderLoader.createShader;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.glUniform2f;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
public class ModelCanvas extends AWTGLCanvas {

    private static final long serialVersionUID = 1L;

    private int vao;

    private int viewportSizeUniform;
    private int viewMatrixUniform;
    private int projMatrixUniform;

    private final Matrix4x3f viewMatrix = new Matrix4x3f();
    private final Matrix4f projMatrix = new Matrix4f();

    MouseControls mouseControls;
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private final Map<Integer, float[]> vertices = readVerticesCoordinates();
    private final List<float[][]> faces = readIndexesAndConvertToFaces(vertices);

    Scene scene;

    public ModelCanvas(GLData data, Scene scene) {
        super(data);

        this.scene = scene;
    }

    @Override
    public void initGL() {
        System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
        GL.createCapabilities();
        glClearColor(0.918f, 0.956f, 1.0f, 1.0f);

        // Enable depth test
        glEnable(GL_DEPTH_TEST);
        // Accept fragment if it closer to the camera than the former one
        glDepthFunc(GL_LESS);

        // Display only faces edges
//        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        /* Create all needed GL resources */
        createVao();
        int program = createRasterProgram();
        initProgram(program);

        CoordinatesConvertor coordinatesConvertor = new CoordinatesConvertor(projMatrix, viewMatrix, vertices);
        mouseControls = new MouseControls(scene, coordinatesConvertor);
    }

    @Override
    public void paintGL() {
        glViewport(0, 0, WindowProperties.getWidth(), WindowProperties.getHeight());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        updateMatrix(mouseControls.getZoomFactor(), mouseControls.getX(), mouseControls.getY());
        renderModel();
        swapBuffers();
    }

    private void updateMatrix(float zoomFactor, float x, float y) {
        float fov = (float) Math.toRadians(30 / zoomFactor);
        projMatrix.setPerspective((float) Math.min(fov, Math.PI),
                                  (float) WindowProperties.getWidth() / WindowProperties.getHeight(),
                                  0.1f,
                                  Float.POSITIVE_INFINITY);

        viewMatrix.setLookAt(0.0f, 2.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                  .rotateY(x)
                  .rotateX(y);
    }

    private void renderModel() {
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.get4x4(matrixBuffer));
        glUniformMatrix4fv(projMatrixUniform, false, projMatrix.get(matrixBuffer));

        glUniform2f(viewportSizeUniform, WindowProperties.getWidth(),  WindowProperties.getHeight());

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, faces.size() * 3);
        glBindVertexArray(0);
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

    private int createRasterProgram() {
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

        return program;
    }

    private void initProgram(int program) {
        glUseProgram(program);
        viewMatrixUniform = glGetUniformLocation(program, "viewMatrix");
        projMatrixUniform = glGetUniformLocation(program, "projMatrix");
        viewportSizeUniform = glGetUniformLocation(program, "viewportSize");
    }
}
