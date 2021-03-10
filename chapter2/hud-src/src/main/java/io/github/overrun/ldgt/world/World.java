package io.github.overrun.ldgt.world;

import io.github.overrun.ldgt.Camera;
import io.github.overrun.ldgt.block.Block;
import io.github.overrun.ldgt.client.Mesh;
import io.github.overrun.ldgt.client.Transformation;
import io.github.overrun.ldgt.client.Window;
import io.github.overrun.ldgt.client.gl.GlProgram;

import java.io.Closeable;

/**
 * @author squid233
 * @since 2021/03/10
 */
public final class World implements Closeable {
    private Block[] blocks;

    private Block block(int x, int y, GlProgram program) {
        return new Block().setPos(x, y).createMesh(program);
    }

    public void init(GlProgram program) {
        if (blocks == null) {
            blocks = new Block[]{
                    block(0, 0, program),
                    block(2, 0, program),
                    block(1, 1, program),
                    block(0, 2, program),
                    block(2, 2, program)
            };
        }
    }

    public void render(Transformation transformation, Camera camera, Window window) {
        for (Block block : blocks) {
            Mesh mesh = block.getMesh();
            mesh.program.setUniform("projModelMatrix",
                    transformation.getProjModelMatrix(camera, block, window.getWidth(), window.getHeight()));
            mesh.render();
        }
    }

    @Override
    public void close() {
        for (Block block : blocks) {
            block.close();
        }
    }
}
