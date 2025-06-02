package org.cmps.tetrahedron.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import org.cmps.tetrahedron.config.CanvasProperties;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Platform;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModelView extends Pane {

    private static final ExecutorService renderExecutor = Executors.newSingleThreadExecutor();
    private static final ModelRenderer modelRenderer = new ModelRenderer();

    private static int fbo;
    private static int tex;
    private static ByteBuffer buffer;

    private static final ImageView imageView = new ImageView();

    public ModelView() {
        getChildren().add(imageView);

        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            submitToRenderExecutor(() -> CanvasProperties.setWidth(newVal.intValue()));
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            submitToRenderExecutor(() -> CanvasProperties.setHeight(newVal.intValue()));
        });

        submitToRenderExecutor(this::initGl);
        startTimer(() -> {
            Image image = submitToRenderExecutor(() -> {
                if (CanvasProperties.isSizeChanged()) {
                    Dimension size = CanvasProperties.getSize();

                    imageView.setFitWidth(size.width);
                    imageView.setFitHeight(size.height);

                    recreateFramebuffer(size.width, size.height);
                }

                return drawImage(CanvasProperties.getWidth(), CanvasProperties.getHeight());
            });

            imageView.setImage(image);
        });
    }

    private void initGl() {
        if (Platform.get() == Platform.MACOSX) {
            Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
        }

        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);

        long window = GLFW.glfwCreateWindow(1, 1, "", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        recreateFramebuffer(CanvasProperties.getWidth(), CanvasProperties.getHeight());
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

        buffer = BufferUtils.createByteBuffer(width * height * 4);
    }

    private Image drawImage(int width, int height) {
        GL11.glViewport(0, 0, width, height);

        modelRenderer.paintGL();

        GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);

        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
        PixelBuffer<ByteBuffer> pixelBuffer = new PixelBuffer<>(width, height, buffer, pixelFormat);

        return new WritableImage(pixelBuffer);
    }

    private void startTimer(Runnable task) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                task.run();
            }
        }.start();
    }

    @SneakyThrows
    private Image submitToRenderExecutor(Callable<Image> task) {
        return renderExecutor.submit(task).get();
    }

    @SneakyThrows
    private void submitToRenderExecutor(Runnable task) {
        renderExecutor.submit(task).get();
    }
}
