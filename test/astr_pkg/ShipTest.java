package astr_pkg;

import static org.junit.Assert.*;

import junit.framework.Assert;

import org.junit.Test;

public class ShipTest {
	
	// Create ship s for multiple test cases
	Ship s = new Ship(400, 300, Math.PI, 0.35, 0.98, .1);

	@Test
	public void testShip() {
		// Tests if the constructor works
		new Ship(400, 300, Math.PI, 0.35, 0.98, .1);
	}

	@Test
	public final void testDecreaseShotWait() {
		s.shotWait = 34;
		s.decreaseShotWait();
		assertTrue(s.shotWait == 32);
		
		s.shotWait = 4;
		s.decreaseShotWait();
		assertTrue(s.shotWait == 4);
		
		s.shotWait = 3;
		s.decreaseShotWait();
		assertTrue(s.shotWait == 3);
	}

	@Test
	public final void testSetScattershot() {
		s.scatterShot = 3;
		s.setScatterShot();
		assertTrue(s.scatterShot == 3);	
		
		s.scatterShot = 0;
		s.setScatterShot();
		assertTrue(s.scatterShot == 1);	
		
	}

	@Test
	public final void testGetX() {
		assertTrue(s.getX() == 400);		
	}

	@Test
	public final void testGetY() {
		assertTrue(s.getY() == 300);	
	}

	@Test
	public final void testGetTheta() {
		assertTrue(s.getTheta() == Math.PI);
	}

	@Test
	public final void testGetProjectiles() {
		s.getProjectiles();
	}

	@Test
	public final void testSetTurningLeft() {
		s.setTurningLeft(true);
	}

	@Test
	public final void testSetTurningRight() {
		s.setTurningRight(true);
	}

	@Test
	public final void testSetAccelerating() {
		s.setAccelerating(true);
	}

	@Test
	public final void testGetXVelocity() {
		assertTrue(s.getXVelocity() == 0);
	}

	@Test
	public final void testGetYVelocity() {
		assertTrue(s.getYVelocity() == 0);
	}

	@Test
	public final void testGetAcceleration() {
		assertTrue(s.getAcceleration() == 0.35);
	}

	@Test
	public final void testIncrementRespawnTime() {
		int res = s.getRespawnTime();
		s.incrementRespawnTime();
		assertTrue(s.getRespawnTime() == res + 1);
	}

	@Test
	public final void testResetRespawnTime() {
		s.resetRespawnTime();
		assertTrue(s.getRespawnTime() == 0);
	}

	@Test
	public final void testGetRespawnTime() {
		s.getRespawnTime();
	}

	@Test
	public final void testIsInvulnerable() {
		s.resetInvulnerabilityTime();
		assertTrue(s.isInvulnerable() == true);
		
		s.incrementInvulnerabilityTime();
		assertTrue(s.isInvulnerable() == true);
		
		for(int i = 1; i < 1003; i++){
			s.incrementInvulnerabilityTime();
		}
		assertTrue(s.isInvulnerable() == false);
	}

	@Test
	public final void testIncrementInvulnerabilityTime() {
		int test = s.getInvulnerabilityTime();
		s.incrementInvulnerabilityTime();
		Assert.assertTrue(s.getInvulnerabilityTime() == test + 1);
	}

	@Test
	public final void testResetInvulnerabilityTime() {
		s.resetInvulnerabilityTime();
		Assert.assertTrue(s.getInvulnerabilityTime() == 0);
	}

	@Test
	public final void testGetInvulnerabilityTime() {
		s.getInvulnerabilityTime();
	}

	@Test
	public final void testSetAlive() {
		s.setAlive(false);
		Assert.assertTrue(s.isAlive() == false);
	}

	@Test
	public final void testReset() {
		s.reset();
		assertTrue(s.getX() == 400);
	}

	@Test
	public final void testGetBounds() {
		s.getBounds();
	}

	@Test
	public final void testMakeItRain() {
		s.makeItRain(true);
		assertTrue(s.fire == true);
	}

	@Test
	public final void testCheckCollisionProjectile() {
		//With no Aliens around, the ship should not be colliding
		s.checkCollisionProjectile();
		assertTrue(s.isAlive() == true);
		
		s.resetInvulnerabilityTime(); //Make ship invulnerable
		s.checkCollisionProjectile();
		assertTrue(s.isAlive() == true); //Test again
		
		//Spawn one alien directly on top the Ship 
		Alien.generateAliens(1, s.getX(), s.getY(), s.getX(), s.getY());
		
		//Shoot the ship, man
		Alien.getAliens().get(0).resetShootDelay();
		Alien.getAliens().get(0).shoot();
		
		//Makes sure that a projectile is created. This tests the ALien.shoot() method in the meantime.
		assertTrue(Alien.getAliens().get(0).getShots().get(0) != null);		
		
		
		//Make the ship vulnerable again (there should be a better way to do this!!!!)
		for(int i = 1; i < 1003; i++){
			s.incrementInvulnerabilityTime();
		}
		
		//This initializes the vertices of the ship polygon for detection (HACK!!!)
		//But at least this also tests that the bounds do work
		s.move(800, 600);
		
		//Check if ship is die
		s.checkCollisionProjectile();
		//Is die?????
		assertTrue(s.isAlive() == false);		
	}
	
	@Test
	
	public final void testMove(){
		
		//Simple test to see if the X-coordinate would work or not
		double oldX = s.getX();
		double oldV = s.getXVelocity();
		s.setAccelerating(false);
		s.move(800, 600);
		assertTrue(s.getX() == oldX + oldV);	
	}

	@Test
	public final void testFire() {
		
		//If there ARE projectiles, shooting works
		s.shotWaitLeft = -1;
		s.makeItRain(true);
		s.move(800, 600);
		assertTrue(s.getProjectiles() != null);
		
		//Make sure scattershot works. If there are more than two projectiles after shooting is called once, it works
		s.shotWaitLeft = -1;
		s.setScatterShot();
		s.makeItRain(true);
		s.move(800, 600);
		assertTrue(s.getProjectiles().size() > 2);
	}
	
	@Test
	public final void testTurning(){
		
		//See if turning actually changed theta, the rotation vector
		double testT = s.getTheta();
		s.setTurningLeft(true);
		s.move(800, 600);
		assertTrue(s.getTheta() == testT - 0.1); 
		//0.1 is the rotation speed, set when the ship is constructed
		
	}
	
	
	@Test
	public final void testAcceleration(){
		
		//See if making the shit accelerate makes velocity change
		double testV = s.getXVelocity();
		s.setAccelerating(true);
		s.move(800, 600);
		assertEquals(s.getXVelocity(), (s.getAcceleration() * Math.cos(s.getTheta())) + testV, 0.01); 
		//It's off by a little bit due to the deceleration rate
		//We know it works
		
	}
	
	@Test
	public final void testOffScreen(){
		
		//Spawn ship off screen!! Insanity
		Ship testS = new Ship(900, 900, Math.PI, 0.35, 0.98, .1);
		testS.move(800, 600);
		// 900 is the x, and 800 is the screen width. 900 - 800 means the ship goes to the other side of the screen
		// Same logic for y
		assertTrue(testS.getX() == 900 - 800);
		assertTrue(testS.getY() == 900 - 600);
		
	}
	
	
	
	@Test
	public final void testDrawShip() {
		//This is not tested here because paintComponent() from AsteroidsGame have to be called for this method to be called properly
		//However, this method is called in AsteroidsGameTest
	}

}
