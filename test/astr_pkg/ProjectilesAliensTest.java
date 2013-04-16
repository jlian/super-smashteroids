package astr_pkg;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectilesAliensTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	ProjectilesAliens p = new ProjectilesAliens(50, 45, Math.PI, 10, 12);
	
//	@Test
//	public void testProjectilesAliens() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPlayShotSound() {
//		fail("Not yet implemented");
//	}

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
		
//		//x > screen WIDTH and y > screen HEIGHT
//		ProjectilesAliens p2 = new ProjectilesAliens(1000, 1000, Math.PI, 10, 12);
//		p2.move();
//		assertTrue("both bigger than range", p2.isOnScreen() == false);
//		
//		//x < screen WIDTH and y < screen HEIGHT
//		ProjectilesAliens p3 = new ProjectilesAliens(-20, -20, Math.PI, 10, 12);
//		p3.move();
//		assertTrue("both less than range", p3.isOnScreen() == false);
//		
//		//x < screen WIDTH and y inside screen bounds
//		ProjectilesAliens p4 = new ProjectilesAliens(-20, 100, Math.PI, 10, 12);
//		p4.move();
//		assertTrue("x less than range", p4.isOnScreen() == false);
//		
//		//x within screen bounds and y < screen HEIGHT
//		ProjectilesAliens p5 = new ProjectilesAliens(100, -20, Math.PI, 10, 12);
//		p5.move();
//		assertTrue("y less than range", p5.isOnScreen() == false);
//		
//		//x > screen WIDTH and y inside screen bounds
//		ProjectilesAliens p6 = new ProjectilesAliens(1000, 100, Math.PI, 10, 12);
//		p6.move();
//		assertTrue("x larger than range", p6.isOnScreen() == false);
//		
//		//x within screen bounds and y > screen HEIGHT
//		ProjectilesAliens p7 = new ProjectilesAliens(100, 1000, Math.PI, 10, 12);
//		p7.move();
//		assertTrue("y larger than range", p7.isOnScreen() == false);
	}

//	@Test
//	public void testDrawProjectiles() {
//		fail("Not yet implemented");
//	}

}
