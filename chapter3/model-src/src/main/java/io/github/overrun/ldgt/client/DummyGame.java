package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.Camera;

import java.io.Closeable;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class DummyGame implements IGameLogic, Closeable {
    private final GameRenderer renderer = new GameRenderer();

    @Override
    public void init() {
        renderer.init();
    }

    @Override
    public void input(Window window) {
        Camera camera = renderer.camera;
        if (window.isKeyPressed(GLFW_KEY_W) || window.isKeyPressed(GLFW_KEY_SPACE)) {
            ++camera.y;
        }
        if (window.isKeyPressed(GLFW_KEY_S) || window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            --camera.y;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            ++camera.x;
        }
        if (window.isKeyPressed(GLFW_KEY_D)){
            --camera.x;
        }
    }

    @Override
    public void update(float delta) { }

    @Override
    public void render(Window window) {
        renderer.render(window);
    }

    @Override
    public void close() {
        renderer.close();
    }
}
