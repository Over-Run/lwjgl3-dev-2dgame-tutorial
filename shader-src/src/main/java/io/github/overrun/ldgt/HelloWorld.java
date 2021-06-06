package io.github.overrun.ldgt;

public class HelloWorld {
	public static void main(String[] args) {
		try (DummyGame game = new DummyGame();
			 GameEngine engine = new GameEngine("Game", 896, 512, game)) {
			engine.run();
		}
	}
}
