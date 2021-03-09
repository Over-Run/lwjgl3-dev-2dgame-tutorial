package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.block.Block;
import io.github.overrun.ldgt.client.gl.GlProgram;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import static io.github.overrun.ldgt.util.Utils.readShaderLines;
import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class GameRenderer implements Closeable {
    public final Block block = new Block();
    private final List<Integer> vboList = new ArrayList<>();
    private final Transformation transformation = new Transformation();
    private GlProgram program;

    public void init() {
        program = new GlProgram();
        program.createVsh(readShaderLines("item.vsh"));
        program.createFsh(readShaderLines("item.fsh"));
        program.link();
        float[] vertices = {
                // 左上
                0, 0,
                // 左下
                0, 256,
                // 右下
                256, 256,
                // 右上
                // 16, 0
        };
        float[] colors = {
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
        };
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
    }

    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT);
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        program.bind();
        program.setUniform("orthoMatrix",
                transformation.getOrthoMatrix(block, window.getWidth(), window.getHeight()));
        glDrawArrays(GL_TRIANGLES, 0, 3);
        program.unbind();
    }

    @Override
    public void close() {
        program.disableVertexAttribArrays("vert", "in_color");
        if (program != null) {
            program.close();
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vbo : vboList) {
            glDeleteBuffers(vbo);
        }
    }
}
