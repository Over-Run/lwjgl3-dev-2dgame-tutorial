package io.github.overrun.ldgt;

import org.lwjgl.system.MemoryStack;
import org.overrun.glutils.GlProgram;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.overrun.glutils.ShaderReader.lines;

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
        var cl = GameRenderer.class.getClassLoader();
        program.createVsh(lines(cl, "item.vsh"));
        program.createFsh(lines(cl, "item.fsh"));
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
        program.enableVertexAttribArrays("vert");
        glVertexAttribPointer(program.getAttrib("vert"), 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // colors
        int colorVbo = glGenBuffers();
        vboList.add(colorVbo);
        glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
        glBufferData(GL_ARRAY_BUFFER, colors, GL_STATIC_DRAW);
        program.enableVertexAttribArrays("in_color");
        glVertexAttribPointer(program.getAttrib("in_color"), 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render(Window window) {
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        program.bind();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(program.getUniform("orthoMatrix"),
                    false,
                    transformation.getOrthoMatrix(block, window.getWidth(), window.getHeight())
                            .get(stack.mallocFloat(16)));
        }
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