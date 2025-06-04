package org.cmps.tetrahedron.graphics;

import com.sun.javafx.perf.PerformanceTracker;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.cmps.tetrahedron.config.CanvasProperties;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ModelView extends Pane {

    private static final ModelRenderer modelRenderer = new ModelRenderer();
    private static final ImageView imageView = new ImageView();

    private static int fbo;
    private static int tex;
    private static ByteBuffer buffer;
    private static PixelBuffer<ByteBuffer> pixelBuffer;
    private static PerformanceTracker performanceTracker;

    public ModelView() {
        Label label = new Label();
        StackPane labelPane = new StackPane(label);
        labelPane.setAlignment(Pos.TOP_LEFT);

        StackPane root = new StackPane(imageView, labelPane);
        getChildren().add(root);

        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            CanvasProperties.setWidth(newVal.intValue());
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            CanvasProperties.setHeight(newVal.intValue());
        });
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (Objects.equals(System.getProperty("glStats"), "true")) {
                performanceTracker = PerformanceTracker.getSceneTracker(newScene);
            }
        });

        initGl();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (CanvasProperties.isSizeChanged()) {
                    Dimension size = CanvasProperties.getSize();

                    recreateFramebuffer(size.width, size.height);
                    recreateWritableImage(size.width, size.height);
                }

                drawFrame(CanvasProperties.getWidth(), CanvasProperties.getHeight());
                pixelBuffer.updateBuffer(b -> null);

                if (performanceTracker != null) {
                    label.setText(String.format(
                            "Current resolution: %dx%d, FPS: %f",
                            CanvasProperties.getWidth(),
                            CanvasProperties.getHeight(),
                            performanceTracker.getInstantFPS()
                    ));
                }
            }
        }.start();
    }

    private void initGl() {
        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

        long window = GLFW.glfwCreateWindow(1, 1, "", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        recreateFramebuffer(CanvasProperties.getWidth(), CanvasProperties.getHeight());
        recreateWritableImage(CanvasProperties.getWidth(), CanvasProperties.getHeight());

        modelRenderer.initGL();
    }

    private void recreateFramebuffer(int width, int height) {
        if (fbo != 0) {
            GL30.glDeleteFramebuffers(fbo);
        }
        if (tex != 0) {
            GL11.glDeleteTextures(tex);
        }

        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

        tex = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, tex, 0);

        int depthRenderbuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthRenderbuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT32F, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthRenderbuffer);
    }

    private void recreateWritableImage(int width, int height) {
        buffer = BufferUtils.createByteBuffer(width * height * 4);
        pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getByteBgraPreInstance());
        imageView.setImage(new WritableImage(pixelBuffer));
    }

    private void drawFrame(int width, int height) {
        GL11.glViewport(0, 0, width, height);

        modelRenderer.paintGL();

        GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);
    }
}
