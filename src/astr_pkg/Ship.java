package astr_pkg;

import java.awt.*;
import java.util.ArrayList;

public class Ship {
	
	private final int[] initialXPts = {16, -11, -6, -11},
			initialYPts = {0, 8, 0, -8};

	private final int[] initialThrusterXPts = {-6, -9, -16, -20, -16, -9},
			initialThrusterYPts = {0 ,4, 2, 0, -2, -4};
	
	private double x, y, theta, acceleration, rotationSpeed, 
			decelerationRate, xVelocity, yVelocity;
	
	private boolean accelerating, turningLeft, turningRight, active;
	
	boolean fire;
	
	int shotWaitLeft, shotWait = 10;
	
	private int[] xPts, yPts, xThrusters, yThrusters;
	
	private ArrayList<Projectiles> projectiles;
	
	public Ship(double x, double y, double theta, double acceleration,
			double decelerationRate, double rotationSpeed){
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.acceleration = acceleration;
		this.rotationSpeed = rotationSpeed;
		this.decelerationRate = decelerationRate;
		xVelocity = yVelocity = 0;
		turningLeft = turningRight = false;
		accelerating = false;
		active = true;
		xPts = new int[4];
		yPts = new int[4];
		xThrusters = new int[6];
		yThrusters = new int[6];
		projectiles = new ArrayList<Projectiles>();
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
	public ArrayList<Projectiles> getProjectiles(){
		return projectiles;
	}
	public void setTurningLeft(boolean left){
		turningLeft = left;
	}
	
	public void setTurningRight(boolean right){
		turningRight = right;
	}
	
	public void setAccelerating(boolean accelerating){
		this.accelerating = accelerating;
	}
	
	public double getXVelocity(){
		return xVelocity;
	}
	
	public double getYVelocity(){
		return yVelocity;
	}
	
	public double getAcceleration(){
		return acceleration;
	}
	
	public void makeItRain(boolean fire){
		this.fire = fire;
		shotWaitLeft = shotWait;
		projectiles.add(new Projectiles(x+(16*Math.cos(theta)), y+(16*Math.sin(theta)),
				theta));
	}
	//public
	public void move(int screenWidth, int screenHeight){
		if(shotWaitLeft > 0){
			shotWaitLeft--;
		}
		
		if(turningLeft){
			theta -= rotationSpeed;
		}
		if(turningRight){
			theta += rotationSpeed;
		}
		if(theta > 2 * Math.PI){
			theta -= 2 * Math.PI;
		}else if(theta < 0){
			theta += 2 * Math.PI;
		}
		if(accelerating){
			xVelocity += acceleration * Math.cos(theta);
			yVelocity += acceleration * Math.sin(theta);
		}
		
		x += xVelocity;
		y += yVelocity;
		
		xVelocity *= decelerationRate;
		yVelocity *= decelerationRate;
		
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
		
		for(int i = 0; i < 4; i++){
			xPts[i] = (int) (initialXPts[i] * Math.cos(theta) - 
					initialYPts[i] * Math.sin(theta) + x + 0.5);
			yPts[i] = (int) (initialYPts[i] * Math.cos(theta) + 
					initialXPts[i] * Math.sin(theta) + y + 0.5);
		}

		for (int i = 0; i < 6; i++){
			xThrusters[i] = (int) (initialThrusterXPts[i] * Math.cos(theta) - 
					initialThrusterYPts[i] * Math.sin(theta) + x + 0.5);
			yThrusters[i] = (int) (initialThrusterYPts[i] * Math.cos(theta) + 
					initialThrusterXPts[i] * Math.sin(theta) + y + 0.5);
		}

//		if(fire){
//			System.out.println("CHECK");
//			fire();

		//}
	}
	
//	public void fire(){
//		System.out.println("CHECK2");
//		projectiles.add(new Projectiles(x, y, theta));
//	}
	
	public void drawShip(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		g.setColor(Color.WHITE);
		g.fillPolygon(xPts, yPts, 4);
		if(accelerating){
//			if(g instanceof Graphics2D){
//				Graphics2D g2d = (Graphics2D)g;
//				g2d.setPaint(new GradientPaint(xThrusters[0], yThrusters[0], Color.BLUE,
//						xThrusters[4], yThrusters[4], Color.WHITE));
//				g2d.fillPolygon(xThrusters, yThrusters, 6);
//			}
			g.setColor(Color.RED);
			g.fillPolygon(xThrusters, yThrusters, 6);
		}
	}
}