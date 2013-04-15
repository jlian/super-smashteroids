package astr_pkg;

import static org.junit.Assert.*;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.junit.Test;

public class ProjectilesTest {

	Ship ship = new Ship(100, 150, 25, 10, 0, 6);
	Projectiles p = new Projectiles(50,45,20,ship);
	double xVelocity = ship.getXVelocity() + 10 * Math.cos(p.getTheta());
	double yVelocity = ship.getYVelocity() + 10 * Math.sin(p.getTheta());
	
	@Test
	public void testProjectiles() {
		fail("Not yet implemented");
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
		assertTrue(p.getTheta() == 20);
	}

	@Test
	public void testGetSource() {
		assertTrue(p.getSource() == ship);
	}

	@Test
	public void testIsOnScreen() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProjectileBounds() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlayShotSound() {
		fail("Not yet implemented");
	}

	@Test
	public void testMove() {
		double expected = p.getX() + xVelocity;
		p.move();
		assertEquals("x velocity", expected, p.getX(), 0.0001);
//		fail("Not yet implemented");
	}

	@Test
	public void testDrawProjectiles() {
		fail("Not yet implemented");
	}

}
