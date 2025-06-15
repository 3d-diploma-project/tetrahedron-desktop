package org.cmps.tetrahedron.graphics;

import org.cmps.tetrahedron.config.CanvasProperties;
import org.cmps.tetrahedron.controller.ModelController;
import org.cmps.tetrahedron.controller.MouseController;
import org.cmps.tetrahedron.controller.VertexInfoController;
import org.cmps.tetrahedron.model.ColorSettings;
import org.cmps.tetrahedron.model.Model;
import org.cmps.tetrahedron.model.ModelViewSettings;
import org.cmps.tetrahedron.utils.CoordinatesConvertor;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11C;

import java.nio.FloatBuffer;
import java.util.List;

import static org.cmps.tetrahedron.utils.ShaderLoader.createShader;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL15.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL15.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_LESS;
import static org.lwjgl.opengl.GL15.glClear;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glDepthFunc;
import static org.lwjgl.opengl.GL15.glEnable;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20C.glBindAttribLocation;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glUniform2f;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

/**
 * Class responsible for painting of a 3D model.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class ModelRenderer {

    private final ModelController modelController = ModelController.getInstance();

    private int vao;

    private int viewportSizeUniform;
    private int viewMatrixUniform;
    private int projMatrixUniform;
    private int modelMatrixUniform;

    private int coloredInSelectedColor;
    private int modelColor;
    private int showElementMesh;

    private Matrix4f modelMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projMatrix = new Matrix4f();

    private final MouseController mouseController = MouseController.getInstance();
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public void initGL() {
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

    public void paintGL() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        updateMatrix(mouseController.getZoomFactor(), mouseController.getX(), mouseController.getY());
        renderModel();

        VertexInfoController vertexInfoController = VertexInfoController.getInstance();
        if (vertexInfoController.isClicked()) {
            float[] depth = new float[1];
            GL11C.glReadPixels(vertexInfoController.getX(),
                               vertexInfoController.getY(), 1, 1,
                               GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, depth);
            vertexInfoController.updateVertexInfoToDisplay(depth[0]);
        }
    }

    private void updateMatrix(float zoomFactor, float x, float y) {
        Model model = ModelController.getInstance().getModel();
        if (model.getVertices().isEmpty()) {
            return;
        }

        modelMatrix = new Matrix4f().rotateY(x)
                                    .rotateX(y)
                                    .translate(model.getCenter());

        float eyeZ = zoomFactor * model.getRadius();
        viewMatrix.setLookAt(0.0f, 0.0f, eyeZ, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        float aspectRatio = (float) CanvasProperties.getWidth() / CanvasProperties.getHeight();
        Vector4f min = new Vector4f(model.getMin(), 1);
        Vector4f max = new Vector4f(model.getMax(), 1);
        Vector2f planes = getPlanes(min, max, modelMatrix, viewMatrix);
        projMatrix.setPerspective(45f, aspectRatio, planes.x, planes.y);
    }

    private Vector2f getPlanes(Vector4f min, Vector4f max, Matrix4f modelMatrix, Matrix4f viewMatrix) {
        Vector4f[] bounds = new Vector4f[]{
                new Vector4f(min.x, min.y, min.z, 1.0f),
                new Vector4f(max.x, min.y, min.z, 1.0f),
                new Vector4f(min.x, max.y, min.z, 1.0f),
                new Vector4f(max.x, max.y, min.z, 1.0f),
                new Vector4f(min.x, min.y, max.z, 1.0f),
                new Vector4f(max.x, min.y, max.z, 1.0f),
                new Vector4f(min.x, max.y, max.z, 1.0f),
                new Vector4f(max.x, max.y, max.z, 1.0f)
        };

        var minZ = Float.MAX_VALUE;
        var maxZ = -Float.MAX_VALUE;

        for (Vector4f v : bounds) {
            v.mul(modelMatrix)
             .mul(viewMatrix);

            minZ = Math.min(minZ, -v.z);
            maxZ = Math.max(maxZ, -v.z);
        }

        float margin = 0.000001f;
        return new Vector2f(Math.max(margin, minZ - margin), maxZ + margin);
    }

    private void renderModel() {
        glUniformMatrix4fv(viewMatrixUniform, false, viewMatrix.get(matrixBuffer));
        glUniformMatrix4fv(modelMatrixUniform, false, modelMatrix.get(matrixBuffer));
        glUniformMatrix4fv(projMatrixUniform, false, projMatrix.get(matrixBuffer));

        initColors();
        initModelSettings();

        glUniform2f(viewportSizeUniform, CanvasProperties.getWidth(), CanvasProperties.getHeight());
        glBindVertexArray(vao);

        if (modelController.isModelReady()) {
            initVao();
            modelController.setModelReady(false);
        }

        if (modelController.getModelColors() != null) {
            createColorBuffer(modelController.getModelColors());
            modelController.setModelColors(null);
        }

        glDrawArrays(GL_TRIANGLES, 0, modelController.getFaces().size() * 3);
        glBindVertexArray(0);
    }

    private void createVao() {
        // vao - Vertex Array Object
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);
        initVao();
    }

    private void initVao() {
        List<float[][]> faces = modelController.getFaces();
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

    //TODO: fix memory leak
    private void createColorBuffer(List<float[]> colorsList) {
        int colorBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorBuffer);

        FloatBuffer colors = BufferUtils.createFloatBuffer(modelController.getFaces().size() * 3 * 3);
        for (int i = 0; i < modelController.getFaces().size() / 4; i++) {
            for (int j = 0; j < 4 * 3; j++) {
                for (float colorPart : colorsList.get(i)) {
                    colors.put(colorPart);
                }
            }
        }
        colors.flip();

        // setup color positions buffer
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0L);
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
        glBindAttribLocation(program, 1, "color");

        glLinkProgram(program);

        return program;
    }

    private void initProgram(int program) {
        glUseProgram(program);
        viewMatrixUniform = glGetUniformLocation(program, "viewMatrix");
        projMatrixUniform = glGetUniformLocation(program, "projMatrix");
        modelMatrixUniform = glGetUniformLocation(program, "modelMatrix");
        viewportSizeUniform = glGetUniformLocation(program, "viewportSize");

        coloredInSelectedColor = glGetUniformLocation(program, "coloredInSelectedColor");
        modelColor = glGetUniformLocation(program, "modelColor");
        showElementMesh = glGetUniformLocation(program, "showElementMesh");
    }

    private void initColors() {
        glUniform1i(coloredInSelectedColor, ColorSettings.getInstance().isColoredInSelectedColor() ? 1 : 0);

        float[] modelColorArr = ColorSettings.getInstance().getModelColor();
        glUniform3f(modelColor, modelColorArr[0], modelColorArr[1], modelColorArr[2]);
    }

    private void initModelSettings() {
        glUniform1i(showElementMesh, ModelViewSettings.getInstance().isShowElementMesh() ? 1 : 0);
    }
}
