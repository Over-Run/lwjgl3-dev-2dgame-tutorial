package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.client.gl.GlProgram;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 2021/03/09
 */
public final class Mesh implements Closeable {
    private final List<Integer> vboList = new ArrayList<>();
    private final int vertexCount;
    private final GlProgram program;
    private final Texture texture;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private GlProgram program;
        private float[] vertices, colors, texCoords;
        private int[] indices;
        private Texture texture;

        public Builder program(GlProgram program) {
            this.program = program;
            return this;
        }

        public Builder vertices(float[] vertices) {
            this.vertices = vertices;
            return this;
        }

        public Builder colors(float[] colors) {
            this.colors = colors;
            return this;
        }

        public Builder indices(int[] indices) {
            this.indices = indices;
            return this;
        }

        public Builder texCoords(float[] texCoords) {
            this.texCoords = texCoords;
            return this;
        }

        public Builder texture(Texture texture) {
            this.texture = texture;
            return this;
        }

        /**
         * Build the mesh.
         *
         * @param i If {@code colors} is null,
         *         it will construct by default color array
         *         [1, 1, 1] * {@code i}.
         * @return The mesh.
         */
        public Mesh build(int i) {
            float[] c = colors;
            if (c == null) {
                c = new float[3 * (i == -1 ? indices.length : i)];
                Arrays.fill(c, 1);
            }
            return new Mesh(program,
                    vertices,
                    c,
                    indices,
                    texCoords,
                    texture);
        }
    }

    private Mesh(GlProgram program,
                 float[] vertices,
                 float[] colors,
                 int[] indices,
                 float[] texCoords,
                 Texture texture) {
        vertexCount = vertices.length;
        this.program = program;
        this.texture = texture;
        // vertices
        int vertVbo = glGenBuffers();
        vboList.add(vertVbo);
        glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        program.enableVertexAttribArray("vert");
        program.vertexAttribPointer("vert", 2, GL_FLOAT, false, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // colors
        int colorVbo = glGenBuffers();
        vboList.add(colorVbo);
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        program.enableVertexAttribArray("in_color");
        program.vertexAttribPointer("in_color", 3, GL_FLOAT, false, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // tex coords
        int texVbo = glGenBuffers();
        vboList.add(texVbo);
        glBindBuffer(GL_ARRAY_BUFFER, texVbo);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        program.enableVertexAttribArray("in_texCoord");
        program.vertexAttribPointer("in_texCoord", 2, GL_FLOAT, false, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // indices
        int idxVbo = glGenBuffers();
        vboList.add(idxVbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void close() {
        texture.close();
        program.disableVertexAttribArrays("vert", "in_color", "in_texCoord");
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vbo : vboList) {
            glDeleteBuffers(vbo);
        }
    }
}
