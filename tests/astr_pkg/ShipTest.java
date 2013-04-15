package astr_pkg;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class ShipTest {
	
	// Create ship s for multiple test cases
	Ship s = new Ship(380, 300, Math.PI, 0.35, 0.98, .1);

	@Test
	public void testShip() {
		// Tests if the constructor works
		new Ship(380, 300, Math.PI, 0.35, 0.98, .1);
	}
	
	@Test
	public final void testPlayShipHitSound() {
		fail("Not yet implemented"); // TODO
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
		s.setScattershot();
		assertTrue(s.scatterShot == 3);	
		
		s.scatterShot = 0;
		s.setScattershot();
		assertTrue(s.scatterShot == 1);	
		
	}

	@Test
	public final void testGetX() {
		assertTrue(s.getX() == 380);		
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
		//Spawn one alien directly on top the Ship 
		Alien.spawnAlienAtLocation(1, s.getX(), s.getY(), s.getX(), s.getY());
		
		//Shoot the ship, man
		Alien.getAliens().get(0).resetShootDelay();
		Alien.getAliens().get(0).shoot();
		
		//Make the ship vulnerable again (there should be a better way to do this!!!!)
		for(int i = 1; i < 1003; i++){
			s.incrementInvulnerabilityTime();
		}
		
		//This initializes the vertices of the ship polygon..
		s.move(800, 600);
		//Check if ship is die
		s.checkCollisionProjectile();
		//Is die?????
		assertTrue(s.isAlive() == false);
	}

	@Test
	public final void testMove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDrawShip() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}

}
