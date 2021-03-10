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
    private static final float[] VERTICES = {
            // 左上
            0, 0,
            // 左下
            0, 32,
            // 右下
            32, 32,
            // 右上
            32, 0
    };
    private static final float[] TEX_COORDS = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };
    private static final int[] INDICES = {
            0, 1, 3, 3, 2, 1
    };
    public int x, y;
    public float scale = 1;
    public float rotation;
    private Mesh mesh;

    public static Mesh.Builder defaultBuilder(GlProgram program) {
        return Mesh.builder()
                .program(program)
                .vertices(VERTICES)
                .texCoords(TEX_COORDS)
                .indices(INDICES);
    }

    public Block createMesh(Mesh.Builder builder) {
        if (mesh == null) {
            mesh = builder.texture(new Texture("grass_block.png")).build(-1);
        }
        return this;
    }

    public Block createMesh(GlProgram program) {
        return createMesh(defaultBuilder(program));
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
