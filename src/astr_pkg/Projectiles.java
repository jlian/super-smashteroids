package astr_pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Projectiles {
	//Initialization
	private Ship ship;
	private double x, y, theta, xVelocity, yVelocity;
	boolean onScreen;
	private final int PROJECTILE_SPEED = 10;
	private Rectangle2D rect;
	private Clip clip;
	
	//Constructor
	public Projectiles(double x, double y, double theta, Ship ship){
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.ship = ship;
		onScreen = true;
		/*These next two velocities are for the ship projectiles. To be realistic, we have
		 *set them to always be faster than the current velocity of the ship when they
		 *are fired. We take the current ship velocity in x and y, then add the constant
		 *projectile speed in the theta direction the ship is facing.
		 */
		xVelocity = ship.getXVelocity() + PROJECTILE_SPEED * Math.cos(theta);
		yVelocity = ship.getYVelocity() + PROJECTILE_SPEED * Math.sin(theta);
			
		/*This bit of code is to turn the sound on and off depending on two factors:
		 *First, if the player selects to turn off the sound in the Options menu, and
		 *second if the game is running on a linux machine. Errors with sound files
		 *come up on linux machine, so this had to be done for the game to run.
		 *Our game does not auto-detect linux machines, so this constant must
		 *be changed manually
		 */
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSound();
		}
		
	}
	
	private void initializeSound(){
		try {
			File shotSound = new File("FX/audio/fire.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(shotSound);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	//Getters and Setters
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getTheta(){
		return theta;
	}
	public Ship getSource(){
		return ship;
	}
	public boolean isOnScreen(){
		return onScreen;
	}
	public Rectangle2D getProjectileBounds(){
		rect = new Rectangle((int) getX()-2,  (int) getY()-2, 4, 4);
		return rect;
	}
	public void playShotSound(){
		clip.setFramePosition(0);
		clip.start();
	}
	
	//Move method for the projectiles
	public void move(){
		//Updates the x and y position according to the velocity determined earlier
		x+= xVelocity;
		y+= yVelocity;
		
		/*This statement ensures that our projectiles do not loop around the screen
		 *like other entities. Once they reach the end of the screen, they are removed from the game.
		 */
		if(x > Constants.WIDTH || x < 0 || y > Constants.HEIGHT || y < 0){
			onScreen = false;
			ship.getProjectiles().remove(this);
		}
	}
	
	//Draw method for the projectiles
	public static void drawProjectiles(Graphics g){
		Graphics g2D = (Graphics2D) g;
		ArrayList<Projectiles> shootArrayP1;
		shootArrayP1 = Constants.SHIP.getProjectiles();
		
		/*We used an arraylist to handle the projectiles, so to draw them we must travel
		 *through the array to set the colour and shape of each projectile for drawing
		 */
		for(int i = 0; i<shootArrayP1.size(); i++){
			Projectiles p = shootArrayP1.get(i);
			g2D.setColor(new Color(0, 216, 65));
			g2D.fillOval((int) p.getX(),  (int)p.getY(), 4, 4);
			
		}
		
		//Multiplayer
		if(MainMenu.isMultiplayer()){
			ArrayList<Projectiles> shootArrayP2;
			shootArrayP2 = Constants.P2SHIP.getProjectiles();
			
			for(int i = 0; i < shootArrayP2.size(); i++){
				Projectiles p = shootArrayP2.get(i);
				g2D.setColor(new Color(0, 216, 65));
				g2D.fillOval((int) p.getX(),  (int)p.getY(), 4, 4);
			}
		}
		
	}
}
