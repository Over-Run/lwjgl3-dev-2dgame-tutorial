package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.Camera;
import io.github.overrun.ldgt.client.game.GameObject;
import io.github.overrun.ldgt.client.game.Rotatable;
import io.github.overrun.ldgt.client.game.Scalable;
import org.joml.Matrix4f;

/**
 * @author squid233
 * @since 2021/03/08
 */
public final class Transformation {
    private final Matrix4f projMatrix = new Matrix4f();
    private final Matrix4f modelMatrix = new Matrix4f();

    public Matrix4f getProjMatrix(float w, float h, GameObject object) {
        return projMatrix.setOrtho2D(0, w, h, 0)
                .translate(object.getPrevX(), object.getPrevY(), 0);
    }

    public Matrix4f getProjModelMatrix(Camera camera, GameObject object, float w, float h) {
        modelMatrix.translation(camera.x,
                camera.y,
                0);
        if (object instanceof Rotatable) {
            modelMatrix.rotateZ(((Rotatable) object).getRotation());
        }
        if (object instanceof Scalable) {
            float scale = ((Scalable) object).getScale();
            modelMatrix.scaleXY(scale, scale);
        }
        return getProjMatrix(w, h, object).mul(modelMatrix);
    }
}
