package io.github.overrun.ldgt.client;

import io.github.overrun.ldgt.Camera;
import io.github.overrun.ldgt.block.Block;

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
        Block block = renderer.block;
        Camera camera = renderer.camera;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            ++camera.y;
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            --camera.y;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            ++camera.x;
            System.out.println(block.x);
        }
        if (window.isKeyPressed(GLFW_KEY_D)){
            --camera.x;
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            block.scale += .1f;
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            block.scale -= .1f;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            block.rotation -= .1f;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            block.rotation += .1f;
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
