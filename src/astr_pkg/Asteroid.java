package astr_pkg;

import java.awt.*;

//I'm mostly just copying the attributes and methods from the Ship class
//Feel free to edit as you please.
public class Asteroid {

	private int[] initialXPts = {400, 410, 422, 425, 420, 407, 400}, //Asteroid will initially spawn off-screen
				initialYPts = {300, 296, 296, 304, 314, 317, 300}; 
	
	private double x, y, thetaImage, thetaVelocity, xVelocity, yVelocity; //Asteroid has a constant velocity so no acceleration. Also no rotation.
	
	private int size; // size of the Asteroid 1-3, 1 being smallest
	
	private boolean active;
	
	private int[] xPts, yPts;
	
	public Asteroid(double x, double y, double thetaImage, double thetaVelocity, double xVelocity, double yVelocity, int size) { //Constructor
		this.x = x;
		this.y = y;
		this.thetaImage = thetaImage;
		this.thetaVelocity = thetaVelocity;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.size = size;
		active = true;
		xPts = new int[7]; //Insert number of polygon points here
		yPts = new int[7]; //Insert number of polygon points here
	}
	
	public void move(int screenWidth, int screenHeight) {
		//The method that controls the "float" or "drift" behaviour of the asteroid
		x += xVelocity;
		y += yVelocity;
		
		if(x > screenWidth){
			x -= screenWidth;
		}else if(x < 0){
			x += screenWidth;
		}

		if(y > screenHeight){
			y -= screenHeight;
		}else if(y < 0){
			y += screenHeight;
		}
		
		//Change points
		for(int i = 0; i < 7; i++){
			xPts[i] = (int) (initialXPts[i] * Math.cos(thetaVelocity) - 
					initialYPts[i] * Math.sin(thetaVelocity) + x + 0.5);
			yPts[i] = (int) (initialYPts[i] * Math.cos(thetaVelocity) + 
					initialXPts[i] * Math.sin(thetaVelocity) + y + 0.5);
		}
	}
	
	public void drawAsteroid(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		g.setColor(Color.MAGENTA); //Are the asteroids also going to be white?
		g.fillPolygon(xPts, yPts, 7); //Probably needs to be changed.
	}
	
	//collision stuff
	public Asteroid whenHit(boolean firstAsteroid){
		if(this.size==1){return null;}
		else{
			int popPop = 1;
			if(!firstAsteroid){popPop = -1;} 
			
			double newThetaImage = Math.random()*2*Math.PI;
			double newThetaVelocity = popPop*Math.random()*Math.PI/4+this.thetaVelocity;
			//ADJUST Speed constant i.e 1
			double newSpeed = 1*Math.random();
			double newVx = newSpeed*Math.cos(newThetaVelocity);
			double newVy = newSpeed*Math.sin(newThetaVelocity);
			
			Asteroid breakup = new Asteroid(this.x, this.y, newThetaImage, newThetaVelocity, newVx, newVy, this.size--);
			return breakup;
		}
	}
}