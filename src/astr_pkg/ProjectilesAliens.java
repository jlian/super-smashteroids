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


public class ProjectilesAliens {
	//Initialization
	private double x, y, theta, xVelocity, yVelocity;
	boolean onScreen;
	private final int PROJECTILE_SPEED = 2;
	private Rectangle2D rect;
	private Clip clip;
	
	public ProjectilesAliens(double x, double y, double theta, 
			double alienVelocityX, double alienVelocityY){
		this.x = x;
		this.y = y;
		this.theta = theta;
		onScreen = true;
		/*These next two velocities are for the alien projectiles. To be realistic, we have
		 *set them to always be faster than the current velocity of the alien that fired the shot at
		 *the moment they are shot. We take the current alien velocity in x and y, then add the constant
		 *alien projectile speed in the theta direction the current alien is facing.
		 */
		xVelocity = alienVelocityX + PROJECTILE_SPEED * Math.cos(theta);
		yVelocity = alienVelocityY + PROJECTILE_SPEED * Math.sin(theta);
			
		/*This bit of code is to turn the sound on and off depending on two factors:
		 *First, if the player selects to turn off the sound in the Options menu, and
		 *second if the game is running on a linux machine. Errors with sound files
		 *come up on linux machine, so this had to be done for the game to run.
		 */
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSound();
		}
		
	}
	
	private void initializeSound(){
		try {
			File menuSelection = new File("FX/audio/alien_laser.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(menuSelection);
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
	
	public void playShotSound(){
		clip.setFramePosition(0);
		clip.start();
	}
	
	//Getters
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getTheta(){
		return theta;
	}
	public boolean isOnScreen(){
		return onScreen;
	}
	public Rectangle2D getProjectileBounds(){
		rect = new Rectangle((int) getX()-2,  (int) getY()-2, 4, 4);
		return rect;
	}
	
	//Move method for the projectiles
	public void move(){
		//Updates the x and y position according to the velocity determined earlier
		x+= xVelocity;
		y+= yVelocity;
		
		/*These two if statements ensure that our projectiles do not loop around the screen
		 * like other entities. Once they reach the end of the screen, they are removed from the game.
		 */
		//Does not work yet
		if(x > Constants.WIDTH || x < 0){
			onScreen = false;
			Alien.getShots().remove(this);
		}
		if(y > Constants.HEIGHT || y < 0){
			onScreen = false;
			Alien.getShots().remove(this);
		}

	}
	
	//Draw method for the projectiles
	public static void drawProjectiles(Graphics g){
		Graphics g2D = (Graphics2D) g;
		ArrayList<ProjectilesAliens> shootArray;
		/*Since Aliens and Alien projectiles are both implemented with array lists,
		 *we needed an embedded for loop to travel through both arrays and set the colour
		 *and shape of the projectiles needed to be displayed for all aliens on the screen		 
		 */
		for(int i = 0; i < Alien.getAliens().size(); i++){
			shootArray = Alien.getAliens().get(i).getShots();
			for(int j = 0; j < shootArray.size(); j++){
				ProjectilesAliens p = shootArray.get(j);
				g2D.setColor(new Color(255, 0, 0));
				g2D.fillOval((int) p.getX(),  (int)p.getY(), 4, 4);
			}	
		}
	}	
}
