package io.github.overrun.ldgt.client;

import java.io.Closeable;

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
    public void input(Window window) { }

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
