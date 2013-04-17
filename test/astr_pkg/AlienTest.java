package astr_pkg;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlienTest {

	// Create ship s for multiple test cases
	Alien a = new Alien(200, 200, 300, 300);

	@Test
	public void testShip() {
		// Tests if the constructor works
		new Alien(200, 200, 400, 400);
	}
	
	@Test
	public void testGenerateAliens() {
		a.generateAliens(1, 200, 200, 300, 300);
		assertTrue(a.getAliens().get(0) != null);
	}
	
	@Test
	public void testSpawnAlienAtLocation() {
		a.spawnAlienAtLocation(1);
		assertTrue(a.getX() > 0 && a.getX() < 800);
		assertTrue(a.getX() > 0 && a.getX() < 600);
		
	}
	@Test
	public void testReset() {
		a.reset();
		assertTrue(a.getAliens().size() == 0);
	}
	
	@Test
	public void testFind(){
		a.find();
		assertTrue(a.getX() == 200);
	}
	@Test 
	public void testCheckCollisionProjectile(){
		//With no Aliens around, the ship should not be colliding
				a.checkCollisionProjectile();
				assertTrue(a.getAliens() != null);
				

//				//Spawn one alien directly on top the Ship 
//				Alien.generateAliens(1, s.getX(), s.getY(), s.getX(), s.getY());
//				
//				//Shoot the ship, man
//				Alien.getAliens().get(0).resetShootDelay();
//				Alien.getAliens().get(0).shoot();
//				
//				//Make the ship vulnerable again (there should be a better way to do this!!!!)
//				for(int i = 1; i < 1003; i++){
//					s.incrementInvulnerabilityTime();
//				}
//				
//				//This initializes the vertices of the ship polygon for detection (HACK!!!)
//				//But at least this also tests that the bounds do work
//				s.move(800, 600);
//				
//				//Check if ship is die
//				s.checkCollisionProjectile();
//				//Is die?????
//				assertTrue(s.isAlive() == false);		
	}
	@Test
	public void testCollisionShip(){
	
	}		 
	
	@Test
	public void testMove(){
		a.changeX(1);
		a.move();
		assertTrue(a.getX() == 201);
	}		 
	
	@Test
	public void testDetectEdges(){
		a.changeX(1000);
		a.detectEdges();
		assertTrue(a.getX() != 1000);
	}		 
		 
	 





}