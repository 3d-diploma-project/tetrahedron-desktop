package org.cmps.tetrahedron.modelView;

import org.cmps.tetrahedron.config.WindowProperties;
import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.List;

import static org.cmps.tetrahedron.utils.ShaderLoader.createShader;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

/**
 * A class responsible for rendering a model and storing
 * all required for this functionality data.
 *
 * @author mariia.borodin (HappyMary16)
 * @since 1.1
 */
public class Renderer {

    private int vao;

    private int viewportSizeUniform;
    private int viewMatrixUniform;
    private int projMatrixUniform;

    private final Matrix4x3f viewMatrix = new Matrix4x3f();
    private final Matrix4f projMatrix = new Matrix4f();

    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private final List<float[][]> faces;

    public Renderer(List<float[][]> faces) {
        this.faces = faces;
        init();
    }

    public void updateMatrix(float zoomFactor, float x, float y) {
        float fov = (float) Math.toRadians(30 / zoomFactor);
        projMatrix.setPerspective((float) Math.min(fov, Math.PI),
                                  (float) WindowProperties.getWidth() / WindowProperties.getHeight(),
                                  0.1f,
                                  Float.POSITIVE_INFINITY);

        viewMatrix.setLookAt(0.0f, 2.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                  .rotateY(x)
                  .rotateX(y);
    }

    public void render() {
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.get4x4(matrixBuffer));
        glUniformMatrix4fv(projMatrixUniform, false, projMatrix.get(matrixBuffer));

        glUniform2f(viewportSizeUniform, WindowProperties.getWidth(),  WindowProperties.getHeight());

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, faces.size() * 3);
        glBindVertexArray(0);
    }

    public Matrix4x3f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    private void init() {
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
