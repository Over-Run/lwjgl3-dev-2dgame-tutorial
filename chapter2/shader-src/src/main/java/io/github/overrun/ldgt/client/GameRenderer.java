package io.github.overrun.ldgt.client;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class GameRenderer {
    public void init() { }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
