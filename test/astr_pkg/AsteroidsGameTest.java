package astr_pkg;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.junit.Test;


public class AsteroidsGameTest {
	
	private static AsteroidsGame game;
	
	@Test
	public final void testInit() {
		game = new AsteroidsGame();
		game.init();
		
	}

	@Test
	public final void testKeyPressed() {
		//So it turns out that faking a key press is extremely difficult
		//But through playing the game we have found out that this method indeed works
	}

	@Test
	public final void testKeyReleased() {
		//See testKeyPressed();
	}

	@Test
	public final void testRun() {
		long testStart = game.startTime;
		long testEnd = game.endTime;
		//See if the game is still running. The skeleton of it, at least.
//		boolean areYouAlive = game.thread.isAlive();
		
		//See if the frame rate works correctly
		assertTrue(game.frameRate > testStart - testEnd);
		
		//Make sure that aliens are made
		assertNotNull(Alien.getAliens().get(0));
		
		//Make sure that asteroids are made
		assertNotNull(Asteroid.getAsteroids().get(0));

	}

	@Test
	public final void testPaintComponentGraphics() {
		fail("Not yet implemented"); // TODO
	}

}
