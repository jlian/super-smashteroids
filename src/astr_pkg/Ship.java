package astr_pkg;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Ship {
	//Initialize the starting points of the ship shape
	private final int[] initialXPts = {16, -11, -6, -11},
			initialYPts = {0, 8, 0, -8};
	//Initialize the starting points of the shape of the ship thruster
	private final int[] initialThrusterXPts = {-6, -9, -16, -20, -16, -9},
			initialThrusterYPts = {0 ,4, 2, 0, -2, -4};
	//Initialize the starting points of the hit box around the ship
	private final int[] initHitX = {16, 16, -11, -11} ,
			initHitY = {8, -8, -8, 8};
	//Initialization
	private double x, y, theta, acceleration, rotationSpeed, 
			decelerationRate, xVelocity, yVelocity;
	
	private Rectangle2D rect;
	
	private boolean accelerating, turningLeft, turningRight, alive;
	
	boolean fire;
	
	int shotWaitLeft, scatterShot; 
	int shotWait = 10;
	
	private int[] xPts, yPts, xThrusters, yThrusters, hitXPts, hitYPts;
	
	private ArrayList<Projectiles> projectiles;
	
	public static final int INVULNERABILITY_TIME = 1000;
	public static final int RESPAWN_TIME = 80;
	private int respawnTime, invulnerabilityTime;
	
	private static Clip shipGoBoom;
	
	public Ship(double x, double y, double theta, double acceleration,
			double decelerationRate, double rotationSpeed){
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.acceleration = acceleration;
		this.rotationSpeed = rotationSpeed;
		this.decelerationRate = decelerationRate;
		xVelocity = yVelocity = 0;
		respawnTime = 0;
		invulnerabilityTime = 0;
		turningLeft = turningRight = false;
		accelerating = false;
		alive = true;
		xPts = new int[4];
		yPts = new int[4];
		hitXPts = new int[4];
		hitYPts = new int[4];
		xThrusters = new int[6];
		yThrusters = new int[6];
		projectiles = new ArrayList<Projectiles>();
		scatterShot = 0;
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSound();
		}
	}
	
	private void initializeSound(){
		try {
			File shipHit = new File("src/bangLarge.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(shipHit);
			shipGoBoom = AudioSystem.getClip();
			shipGoBoom.open(audioIn);
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
    
    public void playShipHitSound(){
		shipGoBoom.setFramePosition(0);
		shipGoBoom.start();
	}
	
	public void decreaseShotWait() {
		if (shotWait > 4) {
			shotWait = shotWait -2;
		}
	}
	
	public void setScattershot() {
		if (scatterShot <= 1) {
			scatterShot++;
		}
	}
	
	public void setInvulnerability() {
		this.invulnerabilityTime = 1000000;
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
		this.turningLeft = left;
	}
	
	public void setTurningRight(boolean right){
		this.turningRight = right;
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
	
	public void incrementRespawnTime(){
		respawnTime++;
	}
	
	public void resetRespawnTime(){
		respawnTime = 0;
	}
	
	public int getRespawnTime(){
		return respawnTime;
	}
	
	public boolean isInvulnerable(){
		if(invulnerabilityTime > INVULNERABILITY_TIME){
			return false;
		}
		return true;
	}
	
	public void incrementInvulnerabilityTime(){
		invulnerabilityTime++;
	}
	
	public void resetInvulnerabilityTime(){
		invulnerabilityTime = 0;
	}
	
	public int getInvulnerabilityTime(){
		return invulnerabilityTime;
	}
	
	public void setAlive(boolean isAlive){
		alive = isAlive;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void reset(){
		x = 400;
		y = 300;
		theta = 0;
		xVelocity = yVelocity = 0;
		turningLeft = turningRight = false;
		accelerating = false;
	}
	
	public Rectangle2D getBounds(){
		return rect;
	}
	
	public void makeItRain(boolean fire){
		this.fire = fire;
	}
	
	public void checkCollisionProjectile(){
		Polygon shipPoly = new Polygon(xPts, yPts, 4);
//		for(int j = 0; j < Alien.getAliens().length; j++){
//			while(Alien.getAliens()[j] == null){
//				if(j < Alien.getAliens().length - 1){
//					j++;
//				}else{
//					return;
//				}
//			}
		for(int i = 0; i < Alien.getAliens().size(); i++){
			for(int j = 0; j < Alien.getAliens().get(i).getShots().size(); j++){
				if(shipPoly.intersects(Alien.getAliens().get(i).getShots().get(j).getProjectileBounds()) &&
						!isInvulnerable()){
					Alien.getAliens().get(i).getShots().remove(j);
					if(MainMenu.isSfxOn() && !Constants.LINUX){
		            	this.playShipHitSound();
		            }
//					Constants.SHIP.setAlive(false);
					this.setAlive(false);
					return;
				}
			}
//			for(int i = 0; i < Alien.getAliens()[j].getShots().size(); i++){
//				if(shipPoly.intersects(Alien.getAliens()[j].getShots().get(i).getProjectileBounds())){
//					Alien.getAliens()[j].getShots().remove(i);
//					Constants.SHIP.setAlive(false);
//					return;
//				}
//			}
		}
        
    }
	
	public void move(int screenWidth, int screenHeight){
		if(shotWaitLeft > 0){
			shotWaitLeft--;
		}
		if(fire){
			if(shotWaitLeft <= 0){
				Projectiles p = new Projectiles(x+(16*Math.cos(theta)), y+(16*Math.sin(theta)),
						theta, this);
				projectiles.add(p);
				if (scatterShot >=1) {
					Projectiles p1 = new Projectiles(x+(16*Math.cos(theta+180)), y+(16*Math.sin(theta+180)),
						theta, this);
					Projectiles p2 = new Projectiles(x+(16*Math.cos(theta-180)), y+(16*Math.sin(theta-180)),
						theta, this);
					projectiles.add(p1);
					projectiles.add(p2);
				}
				if (scatterShot == 2) {
					Projectiles p1 = new Projectiles(x+(16*Math.cos(theta+180)), y+(16*Math.sin(theta+180)),
						theta, this);
					Projectiles p2 = new Projectiles(x+(16*Math.cos(theta-180)), y+(16*Math.sin(theta-180)),
						theta, this);
					projectiles.add(p1);
					projectiles.add(p2);
				}
				
				if(MainMenu.isSfxOn() && !Constants.LINUX){
					p.playShotSound();
				}
				shotWaitLeft = shotWait;
			}
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
			
			hitXPts[i] = (int) (initHitX[i] * Math.cos(theta) - 
					initHitY[i] * Math.sin(theta) + x + 0.5);
			hitYPts[i] = (int) (initHitY[i] * Math.cos(theta) + 
					initHitX[i] * Math.sin(theta) + y + 0.5);
		}

		for (int i = 0; i < 6; i++){
			xThrusters[i] = (int) (initialThrusterXPts[i] * Math.cos(theta) - 
					initialThrusterYPts[i] * Math.sin(theta) + x + 0.5);
			yThrusters[i] = (int) (initialThrusterYPts[i] * Math.cos(theta) + 
					initialThrusterXPts[i] * Math.sin(theta) + y + 0.5);
		}
		Polygon ship = new Polygon(xPts, yPts, 4);
		rect = ship.getBounds2D();
	}
		
	public void drawShip(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setColor(Color.WHITE);
		
		if(isInvulnerable()){
			g.setColor(Color.PINK);
		}
		
		//MP
		if(this.equals(Constants.P2SHIP)){
			g.setColor(Color.GRAY);
		}
		
		g.fillPolygon(xPts, yPts, 4);
		checkCollisionProjectile();
		if(accelerating){
			g.setColor(Color.RED);
			g.fillPolygon(xThrusters, yThrusters, 6);
		}
	}
}
