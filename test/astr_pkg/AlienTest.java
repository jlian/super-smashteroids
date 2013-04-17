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
	//test if aliens are generated
	public void testGenerateAliens() {
		a.generateAliens(1, 200, 200, 300, 300);
		assertTrue(a.getAliens().get(0) != null);
	}
	
	@Test
	//tests if aliens get spawned
	public void testSpawnAlienAtLocation() {
		a.spawnAlienAtLocation(1);
		assertTrue(a.getX() > 0 && a.getX() < 800);
		assertTrue(a.getX() > 0 && a.getX() < 600);
		
	}
	@Test
	//tests if rest
	public void testReset() {
		a.reset();
		assertTrue(a.getAliens().size() == 0);
	}
	
	@Test
	//tests find method
	public void testFind(){
		a.find();
		assertTrue(a.getX() == 200);
	}	 
	
	@Test
	//test move method
	public void testMove(){
		a.changeX(1);
		a.move();
		assertTrue(a.getX() == 201);
	}		 
	
	@Test
	//test if alien is on screen
	public void testDetectEdges(){
		a.changeX(1000);
		a.detectEdges();
		assertTrue(a.getX() != 1000);
	}		 

}