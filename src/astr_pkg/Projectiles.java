package astr_pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Projectiles {
	private double x, y, theta, xVelocity, yVelocity;
	private static Image pImage;
	boolean onScreen;
	private final int BOARD_WIDTH = 800;
	private final int BOARD_HEIGHT = 600;
	private final int PROJECTILE_SPEED = 10;
	
	
	public Projectiles(double x, double y, double theta){
		this.x = x;
		this.y = y;
		this.theta = theta;
		xVelocity = Constants.SHIP.getXVelocity() + PROJECTILE_SPEED * Math.cos(theta);
		yVelocity = Constants.SHIP.getYVelocity() + PROJECTILE_SPEED * Math.sin(theta);
		onScreen = true;
		ImageIcon ii = new ImageIcon("src/shipprojectile.png");
		pImage = ii.getImage();
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
		ArrayList<Projectiles> shootArray;
		shootArray = Constants.SHIP.getProjectiles();
		for(int i = 0; i<shootArray.size(); i++){
			Projectiles p = shootArray.get(i);
			g2D.setColor(new Color(51, 153, 255));
			g2D.fillOval((int) p.getX(),  (int)p.getY(), 4, 4);
//			g2D.drawImage(pImage, (int)p.getX(), (int)p.getY(), null);
			
		}
		
	}
}
