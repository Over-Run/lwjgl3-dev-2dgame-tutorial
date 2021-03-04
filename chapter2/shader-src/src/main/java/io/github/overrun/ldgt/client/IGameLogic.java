package io.github.overrun.ldgt.client;

/**
 * @author squid233
 * @since 2021/03/04
 */
public interface IGameLogic {
    void init();

    void input();

    void update(float delta);

    void render(int mouseX, int mouseY);
}
