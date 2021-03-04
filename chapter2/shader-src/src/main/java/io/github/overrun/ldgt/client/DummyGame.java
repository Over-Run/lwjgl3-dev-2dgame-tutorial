package io.github.overrun.ldgt.client;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class DummyGame implements IGameLogic {
    private final GameRenderer renderer = new GameRenderer();

    @Override
    public void init() {
        renderer.init();
    }

    @Override
    public void input(Window window) { }

    @Override
    public void update(float delta) { }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        renderer.render();
    }
}
