package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.Camera;
import io.github.overrun.ldgt.block.Block;
import io.github.overrun.ldgt.block.BlockModel;
import io.github.overrun.ldgt.client.gl.GlProgram;
import io.github.overrun.ldgt.world.World;

import java.io.Closeable;

import static io.github.overrun.ldgt.util.Utils.readShaderLines;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class GameRenderer implements Closeable {
    public final Camera camera = new Camera();
    private final World world = new World();
    private Block hud;
    private final Transformation transformation = new Transformation();
    private GlProgram program;

    public void init() {
        program = new GlProgram();
        program.createVsh(readShaderLines("item.vsh"));
        program.createFsh(readShaderLines("item.fsh"));
        program.link();
        world.init(program);
        hud = new Block() {
            @Override
            public int getPrevX() {
                return x;
            }

            @Override
            public int getPrevY() {
                return y;
            }
        }.setPos(100, 100).createMesh(program, BlockModel.of("block.json"));
    }

    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT);
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        program.bind();
        program.setUniform("textureSampler", 0);
        world.render(transformation, camera, window);
        Mesh mesh = hud.getMesh();
        mesh.program.setUniform("projModelMatrix",
                transformation.getProjMatrix(window.getWidth(), window.getHeight(), hud));
        mesh.render();
        program.unbind();
    }

    @Override
    public void close() {
        if (program != null) {
            program.close();
        }
    }
}
