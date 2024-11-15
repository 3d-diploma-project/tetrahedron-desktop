package org.cmps.tetrahedron;

import org.cmps.tetrahedron.config.CanvasProperties;
import org.cmps.tetrahedron.config.WindowProperties;
import org.cmps.tetrahedron.controller.SceneController;
import org.cmps.tetrahedron.controller.VertexInfoController;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.cmps.tetrahedron.utils.DataReader;
import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import java.nio.FloatBuffer;
import java.util.List;

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
 * Class responsible for painting of a 3D model.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class ModelCanvas extends AWTGLCanvas {

    private static final long serialVersionUID = 1L;

    private int vao;

    private int viewportSizeUniform;
    private int viewMatrixUniform;
    private int projMatrixUniform;

    private final Matrix4x3f viewMatrix = new Matrix4x3f();
    private final Matrix4f projMatrix = new Matrix4f();

    private final SceneController sceneController = SceneController.getInstance();
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ModelCanvas(GLData data) {
        super(data);
    }

    @Override
    public void initGL() {
        GL.createCapabilities();
        glClearColor(0.93f, 0.956f, 0.992f, 1.0f);

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

        CoordinatesConvertor.initInstance(projMatrix, viewMatrix);
    }

    @Override
    public void paintGL() {
        glViewport(0, 0, CanvasProperties.getPhysicalWidth(), CanvasProperties.getPhysicalHeight());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        updateMatrix(sceneController.getZoomFactor(), sceneController.getX(), sceneController.getY());
        renderModel();
        swapBuffers();

        VertexInfoController vertexInfoController = VertexInfoController.getInstance();
        if (vertexInfoController.isClicked()) {
            float[] depth = new float[1];
            GL11C.glReadPixels(vertexInfoController.getX(),
                               CanvasProperties.getPhysicalHeight() - vertexInfoController.getY(), 1, 1,
                               GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, depth);
            vertexInfoController.setDepthAndUpdateInfo(depth[0]);
        }
    }

    private void updateMatrix(float zoomFactor, float x, float y) {
        float fov = (float) Math.toRadians(30 / zoomFactor);
        projMatrix.setPerspective((float) Math.min(fov, Math.PI),
                                  (float) CanvasProperties.getPhysicalWidth() / CanvasProperties.getPhysicalHeight(),
                                  0.1f,
                                  Float.POSITIVE_INFINITY);

        viewMatrix.setLookAt(0.0f, 2.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                  .rotateY(x)
                  .rotateX(y);
    }

    private void renderModel() {
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.get4x4(matrixBuffer));
        glUniformMatrix4fv(projMatrixUniform, false, projMatrix.get(matrixBuffer));

        glUniform2f(viewportSizeUniform, WindowProperties.getPhysicalWidth(),  WindowProperties.getPhysicalHeight());

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, DataReader.getFaces().size() * 3);
        glBindVertexArray(0);
    }

    private void createVao() {
        // vao - Vertex Array Object
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);

        List<float[][]> faces = DataReader.getFaces();
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
