package astr_pkg;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.Test;

public class ProjectilesAliensTest {

	ProjectilesAliens p = new ProjectilesAliens(50, 45, Math.PI, 10, 12);
	
	@Test
	public void testProjectilesAliens() {
		ProjectilesAliens p = new ProjectilesAliens(50, 45, Math.PI, 10, 12);
	}

	@Test
	public void testGetX() {
		assertTrue(p.getX() == 50);
	}

	@Test
	public void testGetY() {
		assertTrue(p.getY() == 45);
	}

	@Test
	public void testGetTheta() {
		assertTrue(p.getTheta() == Math.PI);
	}

	@Test
	public void testIsOnScreen() {
		//Testing for this method has been done in the move() method
	}

	@Test
	public void testGetProjectileBounds() {
		Rectangle expected = new Rectangle((int) p.getX()-2, (int) p.getY()-2, 4, 4);
		assertEquals(expected, p.getProjectileBounds());
	}

	@Test
	public void testMove() {
		double xVelocityTest = 10 + 2 * Math.cos(p.getTheta());
		double yVelocityTest = 12 + 2 * Math.sin(p.getTheta());
		double xExpected = p.getX() + xVelocityTest;
		double yExpected = p.getY() + yVelocityTest;
		
		p.move();
		//Test for math and for x and y within the screen bounds
		assertEquals("x velocity", xExpected, p.getX(), 0.0001);
		assertEquals("y velocity", yExpected, p.getY(), 0.0001);
		assertTrue("both within range", p.isOnScreen() == true);
		
		//pos 0 - ship inside bounds
		Alien.generateAliens(1, 350, 300, 400, 450);
		//pos 1 - outside bounds (both larger)
		Alien.generateAliens(1, 800, 600, 1000, 1000); 
		//pos 2 - outside bounds (both smaller)
		Alien.generateAliens(1, 0, 0, -100, -100); 
		//pos 3 - x outside bounds (smaller), y inside bounds
		Alien.generateAliens(1, 0, 300, -100, 300); 
		//pos 4 - x inside bounds, y outside bounds (smaller)
		Alien.generateAliens(1, 300, 0, 300, -100);
		//pos 5 - x outside bounds(larger), y inside
		Alien.generateAliens(1, 800, 300, 1000, 300); 
		//pos 6 - x inside, y outside (larger)
		Alien.generateAliens(1, 300, 600, 300, 1000); 
		
		//x and y inside screen boundaries
		Alien.getAliens().get(0).resetShootDelay();
		Alien.getAliens().get(0).shoot();
		Alien.getAliens().get(0).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(0).getShots().get(0).isOnScreen() == true);
		
		//x > screen WIDTH and y > screen HEIGHT
		Alien.getAliens().get(1).resetShootDelay();
		Alien.getAliens().get(1).shoot();
		Alien.getAliens().get(1).getShots().get(0).move();
		//projectiles array should be empty since it is removed once it is off the screen
		assertTrue(Alien.getAliens().get(1).getShots().isEmpty());
		
		//x < screen WIDTH and y < screen HEIGHT
		Alien.getAliens().get(2).resetShootDelay();
		Alien.getAliens().get(2).shoot();
		Alien.getAliens().get(2).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(2).getShots().isEmpty());
		
		//x < screen WIDTH and y inside screen bounds
		Alien.getAliens().get(3).resetShootDelay();
		Alien.getAliens().get(3).shoot();
		Alien.getAliens().get(3).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(3).getShots().isEmpty());
		
		//x within screen bounds and y < screen HEIGHT
		Alien.getAliens().get(4).resetShootDelay();
		Alien.getAliens().get(4).shoot();
		Alien.getAliens().get(4).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(4).getShots().isEmpty());
		
		//x > screen WIDTH and y inside screen bounds
		Alien.getAliens().get(5).resetShootDelay();
		Alien.getAliens().get(5).shoot();
		Alien.getAliens().get(5).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(5).getShots().isEmpty());
		
		//x within screen bounds and y > screen HEIGHT
		Alien.getAliens().get(6).resetShootDelay();
		Alien.getAliens().get(6).shoot();
		Alien.getAliens().get(6).getShots().get(0).move();
		assertTrue(Alien.getAliens().get(6).getShots().isEmpty());
	}

//	@Test
//	public void testDrawProjectiles() {
//		Not tested here. See AsteroidGameTest.java
//	}

}
