package astr_pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.ImageIcon;

public class ProjectilesAliens {
	
	private double x, y, theta, xVelocity, yVelocity;
	private static Image pImage;
	boolean onScreen;
	private final int BOARD_WIDTH = 800;
	private final int BOARD_HEIGHT = 600;
	private final int PROJECTILE_SPEED = 2;
	private Rectangle2D rect;
	
	private Clip clip;
	public ProjectilesAliens(double x, double y, double theta, 
			double alienVelocityX, double alienVelocityY){
		this.x = x;
		this.y = y;
		this.theta = theta;
		xVelocity = alienVelocityX + PROJECTILE_SPEED * Math.cos(theta);
		yVelocity = alienVelocityY + PROJECTILE_SPEED * Math.sin(theta);
		onScreen = true;
		ImageIcon ii = new ImageIcon("src/shipprojectile.png");
		pImage = ii.getImage();
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSound();
		}
		
	}
	
	private void initializeSound(){
		try {
			File menuSelection = new File("src/fire.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(menuSelection);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void playShotSound(){
		clip.setFramePosition(0);
		clip.start();
	}
	
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
	public Image getPImage(){
		return pImage;
	}
	public Rectangle2D getProjectileBounds(){
		rect = new Rectangle((int) getX()-2,  (int) getY()-2, 4, 4);
		return rect;
	}
	public void move(){
		x+= xVelocity;
		y+= yVelocity;
		
		if(x > BOARD_WIDTH || x < 0){
			onScreen = false;
			Constants.SHIP.getProjectiles().remove(this);
		}
		if(y > BOARD_HEIGHT || y < 0){
			onScreen = false;
			Constants.SHIP.getProjectiles().remove(this);
		}

	}
	
	public static void drawProjectiles(Graphics g){
		Graphics g2D = (Graphics2D) g;
		ArrayList<ProjectilesAliens> shootArray;
		for(int i = 0; i < Alien.getAliens().length; i++){
			if(Alien.getAliens()[i] != null){
				shootArray = Alien.getAliens()[i].getShots();
				for(int j = 0; j < shootArray.size(); j++){
					ProjectilesAliens p = shootArray.get(j);
					g2D.setColor(new Color(255, 0, 0));
					g2D.fillOval((int) p.getX(),  (int)p.getY(), 4, 4);
				}
			}
			
		}
	}
		
}
