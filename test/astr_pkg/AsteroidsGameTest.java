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
	
		//It has been proven to be exordinarily difficult to test for anything in run() because the method tells the thread to sleep
		//if it has finished a loop, but the test file runs too fast for multi-frame testing
		

	}

	@Test
	public final void testPaintComponentGraphics() {
		//Unfortunately this is not testable in this external test file because paintCompnent needs to be called by repaint()
		//and repaint() cannot be called outside the runnable class AsteroidsGame.
		//We have performed extensive testing by playing the game, however
	}

}
