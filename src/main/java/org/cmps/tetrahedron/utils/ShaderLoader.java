package org.cmps.tetrahedron.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

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

    private static ByteBuffer ioResourceToByteBuffer(String resource) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        File file = new File(Objects.requireNonNull(url).getFile());

        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {
            return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}