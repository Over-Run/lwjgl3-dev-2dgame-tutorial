package io.github.overrun.ldgt.block;

import io.github.overrun.ldgt.client.Mesh;
import io.github.overrun.ldgt.client.Texture;
import io.github.overrun.ldgt.client.game.GameObject;
import io.github.overrun.ldgt.client.game.Rotatable;
import io.github.overrun.ldgt.client.game.Scalable;
import io.github.overrun.ldgt.client.gl.GlProgram;

import java.io.Closeable;

/**
 * @author squid233
 * @since 2021/03/08
 */
public class Block implements GameObject, Scalable, Rotatable, Closeable {
    public int x, y;
    public float scale = 1;
    public float rotation;
    private Mesh mesh;

    public Block createMesh(GlProgram program, BlockModel model) {
        if (mesh == null) {
            mesh = Mesh.builder()
                    .program(program)
                    .vertices(model.getVerticesV())
                    .texCoords(model.getUv())
                    .colors(model.getColors())
                    .indices(model.getIndices())
                    .texture(new Texture(model.getTexture() + ".png"))
                    .build(-1);
        }
        return this;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Block setPos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public int getPrevX() {
        return x << 5;
    }

    @Override
    public int getPrevY() {
        return y << 5;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void close() {
        mesh.close();
    }
}
