package astr_pkg;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;

//I'm mostly just copying the attributes and methods from the Ship class
//Feel free to edit as you please.



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
//	private int[] hitXS = {25, 25, 0, 0},
//			hitYS = {17, -4, -4, 17};
//	private int[] hitXM = {56, 56, 0, 0},
//			hitYM = {36, -12, -12, 36};
//	private int[] hitXL = {113, 113, 0, 0},
//			hitYL = {43, -35, -35, 43};
//	private int[] initHitX, initHitY;
	
	private double x, y, thetaImage, thetaVelocity, xVelocity, yVelocity; //Asteroid has a constant velocity so no acceleration. Also no rotation.
	
	private int size; // size of the Asteroid 1-3, 1 being smallest
	
	private boolean onScreen;
	
	private Clip clip;
	
	private int[] xPts, yPts;// hitXPts, hitYPts;
//	int[] hitXPts, hitYPts;
	private double speed = 1*Math.random() + 1;
	
	private static int pointsPlayer1 = 0;
	
	public Asteroid(double x, double y, double thetaImage, double thetaVelocity, int size) { //Constructor
		this.x = x; 
		this.y = y;
		this.thetaImage = thetaImage;
		this.thetaVelocity = thetaVelocity;
		this.xVelocity = speed*Math.cos(thetaVelocity);
		this.yVelocity = speed*Math.sin(thetaVelocity);
		this.size = size;
		onScreen = true;
		xPts = new int[8]; //Insert number of polygon points here
		yPts = new int[8]; //Insert number of polygon points here
//		hitXPts = new int[4];
//		hitYPts = new int[4];
		initializeSound();
	}
	
	public static int getPointsP1(){
		return pointsPlayer1;
	}
	
	private void initializeSound(){
		try {
			File menuSelection = new File("src/bangLarge.wav");
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
	
	public void playHitSound(){
		clip.setFramePosition(0);
		clip.start();
	}
	
	public boolean isOnScreen()	{ return onScreen;}
	
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
			System.out.println(random);
			arrayAsteroid.add(new Asteroid(newX, newY, Math.random()*2*Math.PI, Math.random()*2*Math.PI, 3));
		}
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
						//initHitX = hitXS; initHitY = hitYS;}
		if(size == 2){ initialXPts = xPtsM; initialYPts = yPtsM;}
						//initHitX = hitXM; initHitY = hitYM;}
		if(size == 3){ initialXPts = xPtsL; initialYPts = yPtsL;}
						//initHitX = hitXL; initHitY = hitYL;}
		
		for(int i = 0; i < 8; i++){
			xPts[i] = (int) (initialXPts[i] * Math.cos(thetaVelocity) - 
					initialYPts[i] * Math.sin(thetaVelocity) + x + 0.5);
			yPts[i] = (int) (initialYPts[i] * Math.cos(thetaVelocity) + 
					initialXPts[i] * Math.sin(thetaVelocity) + y + 0.5);
		}
//		for(int i = 0; i < 4; i++){
//			hitXPts[i] = (int) (initHitX[i] * Math.cos(thetaVelocity) - 
//					initHitY[i] * Math.sin(thetaVelocity) + x + 0.5);
//			hitYPts[i] = (int) (initHitY[i] * Math.cos(thetaVelocity) + 
//					initHitX[i] * Math.sin(thetaVelocity) + y + 0.5);
//		}
	}
	
	public boolean collisionShip(){
		Polygon p = new Polygon(this.xPts, this.yPts, 8);
		return p.intersects(Constants.SHIP.getBounds());
	}
	public boolean collisionProjectile(){
		Polygon p = new Polygon(this.xPts, this.yPts, 8);
		for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
			if(p.intersects(Constants.SHIP.getProjectiles().get(i).getProjectileBounds())){
				Constants.SHIP.getProjectiles().remove(i);
				this.playHitSound();
				arrayAsteroid.remove(this);
				pointsPlayer1 += 20;
				if(this.size>=2){
					arrayAsteroid.add(new Asteroid(this.x, this.y,this.thetaImage+(Math.PI/4), this.thetaVelocity+(Math.PI/4), this.size-1));
					arrayAsteroid.add(new Asteroid(this.x, this.y,this.thetaImage-(Math.PI/4), this.thetaVelocity-(Math.PI/4), this.size-1));
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
			g.setColor(Color.WHITE); //Are the asteroids also going to be white?
			if(a.collisionShip() || a.collisionProjectile()){
				g.setColor(Color.red);
			}
//			g.fillPolygon(a.xPts, a.yPts, 8); //Probably needs to be changed.
			g.drawPolygon(a.xPts, a.yPts, 8);
//			g.setColor(Color.GREEN);
//			g.drawPolygon(a.hitXPts, a.hitYPts, 4);
		}
	}
	
	public void whenHit(){
		onScreen = false;
		if (size <2) { return;}
	}
	

	//collision stuff
//	public Asteroid whenHit(boolean firstAsteroid){
//		if(this.size==1){return null;}
//		else{
//			int popPop = 1;
//			if(!firstAsteroid){popPop = -1;} 
//			
//			double newThetaImage = Math.random()*2*Math.PI;
//			double newThetaVelocity = popPop*Math.random()*Math.PI/4+this.thetaVelocity;
//			//ADJUST Speed constant i.e 1
//			double newSpeed = 1*Math.random();
//			double newVx = newSpeed*Math.cos(newThetaVelocity);
//			double newVy = newSpeed*Math.sin(newThetaVelocity);
//			
//			Asteroid breakup = new Asteroid(this.x, this.y, newThetaImage, newThetaVelocity, newVx, newVy, this.size--);
//			return breakup;
//		}
//	}
}