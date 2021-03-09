package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.Camera;
import io.github.overrun.ldgt.block.Block;
import io.github.overrun.ldgt.client.gl.GlProgram;

import java.io.Closeable;

import static io.github.overrun.ldgt.util.Utils.readShaderLines;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class GameRenderer implements Closeable {
    public final Block block = new Block();
    public final Camera camera = new Camera();
    private final Transformation transformation = new Transformation();
    private Mesh mesh;
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
                0, 64,
                // 右下
                64, 64,
                // 右上
                64, 0
        };
        float[] texCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        int[] indices = {
                0, 1, 3, 3, 2, 1
        };
        mesh = Mesh.builder()
                .program(program)
                .vertices(vertices)
                .texCoords(texCoords)
                .indices(indices)
                .texture(new Texture("grass_block.png"))
                .build(-1);
    }

    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT);
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        program.bind();
        program.setUniform("textureSampler", 0);
        program.setUniform("orthoMatrix",
                transformation.getOrthoMatrix(camera, block, window.getWidth(), window.getHeight()));
        mesh.render();
        program.unbind();
    }

    @Override
    public void close() {
        mesh.close();
        if (program != null) {
            program.close();
        }
    }
}
