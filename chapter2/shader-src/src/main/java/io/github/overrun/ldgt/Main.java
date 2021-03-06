package io.github.overrun.ldgt;

import io.github.overrun.ldgt.client.DummyGame;
import io.github.overrun.ldgt.client.GameEngine;

/**
 * @author squid233
 * @since 2021/03/04
 */
public final class Main {
    public static void main(String[] args) {
        new GameEngine("Game", 896, 512, new DummyGame()).run();
    }
}
