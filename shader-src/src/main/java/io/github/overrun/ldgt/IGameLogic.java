package io.github.overrun.ldgt;

/**
 * @author squid233
 * @since 1
 */
public interface IGameLogic {
    void init();

    void input(Window window);

    void update(float delta);

    void render(Window window);
}
