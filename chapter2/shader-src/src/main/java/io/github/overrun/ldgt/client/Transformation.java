package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.block.Block;
import org.joml.Matrix4f;

/**
 * @author squid233
 * @since 2021/03/08
 */
public final class Transformation {
    public enum Axis {
        /**
         * The axis x.
         */
        X(1, 0, 0),
        NEGATE_X(-1, 0, 0),
        Y(0, 1, 0),
        NEGATE_Y(0, -1, 0),
        Z(0, 0, 1),
        NEGATE_Z(0, 0, -1);

        private final int x, y, z;

        Axis(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private final Matrix4f worldMatrix = new Matrix4f();

    public Matrix4f getWorldMatrix(int w, int h, Axis axis, Block block) {
        return worldMatrix.identity()
                .ortho(0, w, h, 0, 1, -1)
                .translate(block.x, block.y, 0)
                .scale(block.scale, block.scale, 0)
                .rotate(block.rotation, axis.x, axis.y, axis.z);
    }
}
