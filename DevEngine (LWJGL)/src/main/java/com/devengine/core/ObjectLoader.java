package com.devengine.core;

import com.devengine.core.entity.Model;
import com.devengine.core.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public Model loadTXTModel(String indFilename, String vertFilename) {
        List<String> linesIndices = Utils.readAllLines(indFilename);
        List<String> linesVertices = Utils.readAllLines(vertFilename);

        List<Vector3f> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (String line : linesIndices) {
            String[] tokens = line.split("\\s+");
            if (tokens.length < 5) continue;

            int a = Integer.parseInt(tokens[1]) - 1,
                    b = Integer.parseInt(tokens[2]) - 1,
                    c = Integer.parseInt(tokens[3]) - 1,
                    d = Integer.parseInt(tokens[4]) - 1;

            Collections.addAll(indices, Utils.tetrahedronToTriangles(a, b, c, d).toArray(new Integer[0]));
        }

        for (String line : linesVertices) {
            String[] tokens1 = line.split("\\s+");
            Vector3f verticesVec = new Vector3f(
                    Float.parseFloat(tokens1[1]),
                    Float.parseFloat(tokens1[2]),
                    Float.parseFloat(tokens1[3])
            );
            vertices.add(verticesVec);
        }

        float[] verticesArr = new float[vertices.size() * 3];
        float[] texCoordArr = new float[vertices.size() * 2];

        int i = 0;
        for (Vector3f pos : vertices) {
            verticesArr[i * 3] = pos.x;
            verticesArr[i * 3 + 1] = pos.y;
            verticesArr[i * 3 + 2] = pos.z;
            i++;
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        return loadModel(verticesArr, texCoordArr, indicesArr);
    }

    private static void processVertex(int pos, int texCoord, List<Vector2f> texCoordList,
                                      float[] texCoordArr, List<Integer> indicesList) {
        indicesList.add(pos);
        if (texCoord >= 0) {
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArr[pos * 2] = texCoordVec.x;
            texCoordArr[pos * 2 + 1] = 1 - texCoordVec.y;
        }

    }

    private static void processFace(String token, List<Vector3i> faces) {
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(token) - 1;
        Vector3i faceVec = new Vector3i(pos, coords, normal);
        faces.add(faceVec);
    }

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttribList(0, 3, vertices);
        storeDataInAttribList(1, 2, textureCoords);
        unbind();
        return new Model(id, indices.length);
    }

    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null)
                throw new Exception("Image File " + filename + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttribList(int attribNo, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            GL30.glDeleteBuffers(vbo);
        for (int texture : textures)
            GL11.glDeleteTextures(texture);
    }

}
