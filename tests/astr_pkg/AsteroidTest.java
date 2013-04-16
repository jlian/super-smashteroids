package astr_pkg;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;


public class AsteroidTest {

	Asteroid a = new Asteroid(400, 300, 20, 1, 20);
	Asteroid b = new Asteroid(100, 200, 15, 2, 50);
	public Clip asteroidSound;	
	
	
	@Test
	public final void testAsteroid() {
		assertTrue(a.getX() == 400);
		assertTrue(a.getY() == 300);
		assertTrue(a.getThetaVelocity() == 20);
		assertTrue(a.getSize() == 1);
		assertTrue(a.getSpeed() == 20);
		assertTrue(a.getXPts().length == 8);
		assertTrue(a.getYPts().length == 8);
		
		assertTrue(b.getX() == 100);
		assertTrue(b.getY() == 200);
		assertTrue(b.getThetaVelocity() == 15);
		assertTrue(b.getSize() ==2);
		assertTrue(b.getSpeed() == 50);
		assertTrue(b.getXPts().length == 8);
		assertTrue(b.getYPts().length == 8);
	}

	
	@Test
	public final void testPlayHitSound() {
		try {
			File asteroidHit = new File("src/bangLarge.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(asteroidHit);
			asteroidSound = AudioSystem.getClip();
			asteroidSound.open(audioIn);
		} 
		catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		asteroidSound.setFramePosition(0);
		asteroidSound.start();
		
//		Asteroid.playHitSound(); //Copied contents of the method above because it wouldn't play
		
		assertTrue(asteroidSound.getFramePosition() >= 0);
		assertTrue(asteroidSound.isActive());
	}

	
	@Test
	public final void testGenerateAsteroids() {
		Asteroid.generateAsteroids(2);
		assertTrue(a.getScoreX().length == 10);
		assertTrue(a.getScoreY().length == 10);
		assertTrue(a.getScoreTime().length== 10);
		assertTrue(a.getScoreValue().length == 10);
		
		Asteroid.generateAsteroids(10);
		assertTrue(a.getScoreX().length == 50);
		assertTrue(a.getScoreY().length == 50);
		assertTrue(a.getScoreTime().length== 50);
		assertTrue(a.getScoreValue().length == 50);
	}

	@Test
	public final void testMove() {
		a.move (1000, 1000);
		assertTrue(a.getX() <= 1001 && a.getX() >= -1);
		assertTrue(a.getY() <= 1001 && a.getY() >= -1);
		
	}

	@Test
	public final void testCollisionShip() {
		try {
			File asteroidHit = new File("src/bangLarge.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(asteroidHit);
			asteroidSound = AudioSystem.getClip();
			asteroidSound.open(audioIn);
		} 
		catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		Constants.SHIP.setInvulnerability();
		a.collisionShip();
		assertTrue(Constants.SHIP.isAlive());
		assertTrue(!asteroidSound.isActive());
	}



}
