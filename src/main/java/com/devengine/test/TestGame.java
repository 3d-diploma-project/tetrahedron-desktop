package com.devengine.test;

import com.devengine.core.*;
import com.devengine.core.entity.Entity;
import com.devengine.core.entity.Model;
import com.devengine.core.entity.Texture;
import com.devengine.core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class TestGame implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowsManager window;

    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        String indicesFilename = "/models/Indices (model 1).txt";
        String verticesFilename = "/models/Vertices (model 1).txt";

        Model model = loader.loadTXTModel(indicesFilename, verticesFilename);
        model.setTexture(new Texture(loader.loadTexture("textures/sky.png")));
        entity = new Entity(model, new Vector3f(0, 0, -1), new Vector3f(0, 0, 0), 1);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPressed(GLFW.GLFW_KEY_UP))
            cameraInc.y = -1;
        if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN))
            cameraInc.y = 1;
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED,
                cameraInc.y * Consts.CAMERA_MOVE_SPEED,
                cameraInc.z * Consts.CAMERA_MOVE_SPEED);
        if (mouseInput.isRightButtonPress()) {
            Vector2f rotVec = mouseInput.getDispVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }
//        entity.incRotation(0.0f, 0.25f, 0.0f);
    }

    @Override
    public void render() {
        renderer.render(entity, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
