package astr_pkg;

import java.awt.*;

public class Ship {

	private final int[] initialXPts = {16, -11, -6, -11},
			initialYPts = {0, 8, 0, -8};

	private double x, y, theta, acceleration, rotationSpeed, 
			decelerationRate, xVelocity, yVelocity;

	private boolean accelerating, turningLeft, turningRight, active;

	private int[] xPts, yPts;

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

	public void move(int screenWidth, int screenHeight){
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
	}

	public void drawShip(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		g.setColor(Color.WHITE);
		g.fillPolygon(xPts, yPts, 4);
	}
}