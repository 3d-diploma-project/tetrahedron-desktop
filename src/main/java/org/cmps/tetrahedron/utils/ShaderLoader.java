package org.cmps.tetrahedron.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderLoader {

    /**
     * Create a shader object from the given classpath resource.
     *
     * @param resource the class path
     * @param type the shader type
     * @return the shader object id
     */
    public static int createShader(String resource, int type) {
        int shader = glCreateShader(type);

        ByteBuffer source = ioResourceToByteBuffer(resource);
        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);
        strings.put(0, source);
        lengths.put(0, source.remaining());

        glShaderSource(shader, strings, lengths);
        glCompileShader(shader);

        return shader;
    }

    private static ByteBuffer ioResourceToByteBuffer(String resourcePath) {
        try (InputStream inputStream = ShaderLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            byte[] bytes = inputStream.readAllBytes();

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();

            return byteBuffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}