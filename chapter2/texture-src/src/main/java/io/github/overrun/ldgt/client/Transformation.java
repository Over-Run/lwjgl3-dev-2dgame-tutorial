package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.block.Block;
import org.joml.Matrix4f;

/**
 * @author squid233
 * @since 2021/03/08
 */
public final class Transformation {
    private final Matrix4f orthoMatrix = new Matrix4f();

    public Matrix4f getOrthoMatrix(Block block, float w, float h) {
        return orthoMatrix.identity()
                .ortho2D(0, w, h, 0)
                .translate(block.x, block.y, 0)
                .rotateZ(block.rotation)
                .scaleXY(block.scale, block.scale);
    }
}
