package io.github.overrun.ldgt;

import org.lwjgl.system.MemoryStack;
import org.overrun.glutils.GlProgram;
import org.overrun.glutils.Mesh;
import org.overrun.glutils.Textures;

import java.io.Closeable;

import static org.lwjgl.opengl.GL20.*;
import static org.overrun.glutils.ShaderReader.lines;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class GameRenderer implements Closeable {
    public final Block block = new Block();
    private final Transformation transformation = new Transformation();
    private Mesh mesh;
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
                256, 0
        };
        float[] colors = {
                1, 1, 1,
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };
        float[] texCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        int[] indices = {
                0, 1, 3, 3, 1, 2
        };
        mesh = Mesh.builder()
                .program(program)
                .vertices(vertices)
                .vertIdx("vert")
                .vertSize(2)
                .colors(colors)
                .colorIdx("in_color")
                .texCoords(texCoords)
                .texIdx("in_texCoord")
                .texSize(2)
                .texture(Textures.loadAWT(GameRenderer.class.getClassLoader(),
                        "grass_block.png",
                        GL_NEAREST))
                .indices(indices)
                .build();
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
        glUniform1i(program.getUniform("textureSampler"), 0);
        mesh.render();
        program.unbind();
    }

    @Override
    public void close() {
        program.disableVertexAttribArrays("vert", "in_color", "in_texCoord");
        if (program != null) {
            program.close();
        }
        mesh.close();
    }
}