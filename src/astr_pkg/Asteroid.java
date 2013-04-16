package astr_pkg;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;


public class Asteroid {
	
	private static ArrayList<Asteroid> arrayAsteroid = new ArrayList<Asteroid>();
	
	private int[] xPtsS 	= {0, 10, 22, 25, 20, 7, 0, 0}, //Asteroid will initially spawn off-screen
				yPtsS 	= {0, -4, -4, 4, 14, 17, 0, 0};
	private int[] xPtsM 	= {0, 15, 32, 56, 48, 34, 8, 0}, //med
				yPtsM 		= {0, -12, -12, 10, 36, 36, 25, 0};
	private int[] 	xPtsL	= {0, 25, 64, 98, 113, 92, 42, 0}, //large
					yPtsL	= {0, -33, -22, -35, -22, 32, 43, 0};
	private int[] initialXPts,
					initialYPts;
	
	private double x, y, thetaVelocity, xVelocity, yVelocity; //Asteroid has a constant velocity so no acceleration. Also no rotation.
	
	private int size; // size of the Asteroid 1-3, 1 being smallest
	
	private Clip asteroidSound;	
	private int[] xPts, yPts;
	private double speed;
	
	private static int pointsPlayer1 = 0;
	private static int scoreTimeOnScreen = 0;
	
	private static int[] scoreX;
	private static int[] scoreY;
	private static int[] scoreTime;
	private static int[] scoreValue;
	
	private int invulnerable = 0;
	
	public Asteroid(double x, double y,double thetaVelocity,
				int size, double speed) { //Constructor
		this.x = x; 
		this.y = y;
		this.thetaVelocity = thetaVelocity;
		this.xVelocity = speed*Math.cos(thetaVelocity);
		this.yVelocity = speed*Math.sin(thetaVelocity);
		this.size = size;
		this.speed = speed;
		
		xPts = new int[8]; //Insert number of polygon points here
		yPts = new int[8]; //Insert number of polygon points here
		
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSound();
		}
	}
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getThetaVelocity() {
		return this.thetaVelocity;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public static int getPointsP1(){
		return pointsPlayer1;
	}
	
	public static void resetPlayerScore(){
		pointsPlayer1 = 0;
		//PP2
	}
	
	public static void reset(){
		arrayAsteroid.removeAll(arrayAsteroid);
	}
	
	private void initializeSound(){
		try {
			File asteroidHit = new File("FX/audio/bangLarge.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(asteroidHit);
			asteroidSound = AudioSystem.getClip();
			asteroidSound.open(audioIn);
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
	
	public void playHitSound(){
		asteroidSound.setFramePosition(0);
		asteroidSound.start();
	}
	
	public static ArrayList<Asteroid> getAsteroids(){
		return arrayAsteroid;
	}
	
	public static void generateAsteroids(int numberOfAsteroids){
		
		for(int i = 0; i< numberOfAsteroids; i++){
			int random = (int) (Math.random() * 8);
			double newX;
			double newY;
			if(random <= 2){
				newX = 0;
				newY = Math.random()*Constants.HEIGHT;
			}else if(random > 2 && random <= 4){
				newY = 0;
				newX = Math.random()*Constants.WIDTH;
			}else if(random > 4 && random <= 6){
				newX = Constants.WIDTH;
				newY = Math.random()*Constants.HEIGHT;
			}else{
				newY = Constants.HEIGHT;
				
				newX = Math.random()*Constants.WIDTH;
			}
			double astrTheta = Math.random() * 2 * Math.PI;
			int astrSize = ((int) (Math.random() * 10) % 3) + 1;
			double astrSpeed = ((Math.random() * 10) % 4) + 1;
			arrayAsteroid.add(new Asteroid(newX, newY, astrTheta,
						astrSize, astrSpeed));
		}
		scoreX = new int[5 * numberOfAsteroids];
		scoreY = new int[5 * numberOfAsteroids];
		scoreTime = new int[5 * numberOfAsteroids];
		scoreValue = new int[5 * numberOfAsteroids];
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
		if(size == 1){ initialXPts = xPtsS; initialYPts = yPtsS;}
						
		if(size == 2){ initialXPts = xPtsM; initialYPts = yPtsM;}
						
		if(size == 3){ initialXPts = xPtsL; initialYPts = yPtsL;}
						
		for(int i = 0; i < 8; i++){
			xPts[i] = (int) (initialXPts[i] * Math.cos(thetaVelocity) - 
					initialYPts[i] * Math.sin(thetaVelocity) + x + 0.5);
			yPts[i] = (int) (initialYPts[i] * Math.cos(thetaVelocity) + 
					initialXPts[i] * Math.sin(thetaVelocity) + y + 0.5);
		}
	}
	
	public void collisionShip(){
		Polygon p = new Polygon(this.xPts, this.yPts, 8);
		if(Constants.SHIP.getBounds() != null && p.intersects(Constants.SHIP.getBounds()) && 
				Constants.SHIP.isAlive() && !Constants.SHIP.isInvulnerable()){
			if(MainMenu.isSfxOn() && !Constants.LINUX){
				this.playHitSound();
			}
			Constants.SHIP.setAlive(false);
			arrayAsteroid.remove(this);
			if(this.size>=2){
				arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity+Math.random()*(Math.PI/2), this.size-1, this.speed));
				arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity-Math.random()*(Math.PI/2), this.size-1, this.speed));
			}
//			return true;
		//MP
		}/*else */
		if(MainMenu.isMultiplayer() && Constants.P2SHIP.getBounds() != null &&
				p.intersects(Constants.P2SHIP.getBounds()) && Constants.P2SHIP.isAlive() && !Constants.P2SHIP.isInvulnerable()){
			if(MainMenu.isSfxOn() && !Constants.LINUX){
				this.playHitSound();
			}
			Constants.P2SHIP.setAlive(false);
//			return true;
		}
//		return false;
	}
	public boolean collisionProjectile(){
		Polygon p = new Polygon(this.xPts, this.yPts, 8);
		
		if(MainMenu.isMultiplayer() && Constants.P2SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.P2SHIP.getProjectiles().size(); i++){
				if(p.intersects(Constants.P2SHIP.getProjectiles().get(i).getProjectileBounds())){
					Constants.P2SHIP.getProjectiles().remove(i);
					if(MainMenu.isSfxOn() && !Constants.LINUX){
						this.playHitSound();
					}
					scoreX[i] = (int) this.x;
					scoreY[i] = (int) this.y;
					scoreTime[i] = 80;
					if(this.size == 1){
						scoreValue[i] = 10;
					}else if(this.size == 2){
						scoreValue[i] = 20;
					}else if(this.size == 3){
						scoreValue[i] = 40;
					}
					arrayAsteroid.remove(this);
					pointsPlayer1 += scoreValue[i];
					if(this.size>=2){
						arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity+Math.random()*(Math.PI/2), this.size-1, this.speed));
						arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity-Math.random()*(Math.PI/2), this.size-1, this.speed));
					}
					return true;
				}
			}
		}
		for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
			if(p.intersects(Constants.SHIP.getProjectiles().get(i).getProjectileBounds())){
				Constants.SHIP.getProjectiles().remove(i);
				if(MainMenu.isSfxOn() && !Constants.LINUX){
					this.playHitSound();
				}
				scoreX[i] = (int) this.x;
				scoreY[i] = (int) this.y;
				scoreTime[i] = 80;
				if(this.size == 1){
					scoreValue[i] = 10;
				}else if(this.size == 2){
					scoreValue[i] = 20;
				}else if(this.size == 3){
					scoreValue[i] = 40;
				}
				arrayAsteroid.remove(this);
				pointsPlayer1 += scoreValue[i];
				if(this.size>=2){
					arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity+Math.random()*(Math.PI/2), this.size-1, this.speed));
					arrayAsteroid.add(new Asteroid(this.x, this.y, this.thetaVelocity-Math.random()*(Math.PI/2), this.size-1, this.speed));
				}
				return true;
			}
		}
		return false;
	}
	public static void drawAsteroid(Graphics g){
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		for(int i = 0; i < arrayAsteroid.size(); i++){
			Asteroid a = arrayAsteroid.get(i);
			g.setColor(Color.WHITE); 
			a.collisionShip();
			a.collisionProjectile();
			
			g.setColor(Color.WHITE);
			g.fillPolygon(a.xPts, a.yPts, 8);
		}
		for(int i = 0; i < scoreX.length; i++){
			if(scoreX[i] != 0 || scoreY[i] != 0){ //There is a score
				if(scoreTime[i] > 0){
					g.setFont(new Font("Arial", Font.BOLD, 12));
					g.setColor(Color.RED);
					g.drawString("" + scoreValue[i], scoreX[i], scoreY[i]);
					scoreTime[i]--;
					scoreY[i] -= 1;
				}
			}
		}
	}

}
