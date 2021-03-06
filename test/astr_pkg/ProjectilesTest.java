package astr_pkg;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import org.junit.Test;

public class ProjectilesTest {

	Ship ship = new Ship(100, 150, 25, 10, 0, 6);
	Projectiles p = new Projectiles(50, 45, Math.PI, ship);
	double xVelocity = ship.getXVelocity() + 10 * Math.cos(p.getTheta());
	double yVelocity = ship.getYVelocity() + 10 * Math.sin(p.getTheta());
	int counter;

	@Test
	public void testProjectiles() {
		Projectiles p = new Projectiles(50, 45, Math.PI, ship);
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
	public void testGetSource() {
		assertTrue(p.getSource() == ship);
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
		double xExpected = p.getX() + xVelocity;
		double yExpected = p.getY() + yVelocity;
		
		p.move();

		//Test for math and for x and y within the screen bounds
		assertEquals("x velocity", xExpected, p.getX(), 0.0001);
		assertEquals("y velocity", yExpected, p.getY(), 0.0001);
		assertTrue("both within range", p.isOnScreen() == true);
		
		//x > screen WIDTH and y > screen HEIGHT
		Projectiles p2 = new Projectiles(1000,1000,20,ship);
		p2.move();
		assertTrue("both bigger than range", p2.isOnScreen() == false);
		
		//x < screen WIDTH and y < screen HEIGHT
		Projectiles p3 = new Projectiles(-20,-20,20,ship);
		p3.move();
		assertTrue("both less than range", p3.isOnScreen() == false);
		
		//x < screen WIDTH and y inside screen bounds
		Projectiles p4 = new Projectiles(-20,100,20,ship);
		p4.move();
		assertTrue("x less than range", p4.isOnScreen() == false);
		
		//x within screen bounds and y < screen HEIGHT
		Projectiles p5 = new Projectiles(100,-20,20,ship);
		p5.move();
		assertTrue("y less than range", p5.isOnScreen() == false);
		
		//x > screen WIDTH and y inside screen bounds
		Projectiles p6 = new Projectiles(1000,100,20,ship);
		p6.move();
		assertTrue("x larger than range", p6.isOnScreen() == false);
		
		//x within screen bounds and y > screen HEIGHT
		Projectiles p7 = new Projectiles(100,1000,20,ship);
		p7.move();
		assertTrue("y larger than range", p7.isOnScreen() == false);
	}

//	@Test
//	public void testDrawProjectiles() {
//		Not tested here. See AsteroidGameTest.java
//	}
//
}
