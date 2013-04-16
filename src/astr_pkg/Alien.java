package astr_pkg;

//imported packages
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

//class Alien is the AI of the game
//called from the AsteroidsGame.java class
public class Alien {
    
    //class variables
    private static final int scoreValue = 100;// score value of killing an alien
    private static Image AlienImage;// creates image variable 
    private static int drawDelay = 25;// variable for drawing aliens
    private static int alienCount = 1;// variable for drawing aliens
    private static int pointsPlayer1 = 0;// keeps track of score from alien class
    private static int numberOfAliens;// variable for keeping track of amount of aliens
    private static int arrayPos;// variable for array list
    private static int shootDelay;// time variable
    private static int[] scoreX;// an array to track score from alien
    private static int[] scoreY;// an array to track score from alien
    private static int[] scoreTime;// time variable
    private static int[] deathTimer;// time variable
    private static Clip alienGoBoom;// sound
    private static Clip alienLaser;// sound
    private static ImageIcon alienExplosion = new ImageIcon("src/astr_pkg/explosion3.gif");// gives alienExplosion a source
    private static ArrayList<Alien> aliens = new ArrayList<>();// array list that will contain aliens
    private ArrayList<ProjectilesAliens> projectiles;// creates an array list using the class ProjectilesALiens
    private Ellipse2D.Double circle;// creates a circle variable
    private double alienHeight, alienWidth;// creates variables for height and width
    double shipX, shipY, xPos, yPos;// variables for ship and alien coordinates
    double shoot;// variable for shooting
    int xVelocity, yVelocity;// variable for speed of alien
    
    // method that sets the blue print for all aliens
    public Alien(double alienXPos, double alienYPos, double shipXPos, double shipYPos){
        xPos = alienXPos;// sets x position of alien
        yPos = alienYPos;// sets y position of alien
        shipX = shipXPos;// gets x position of ship
        shipY = shipYPos;// gets y position of ship
        AlienImage = new ImageIcon("src/astr_pkg/AlienM.png").getImage();// sets the AlienImage to an image
        alienHeight = AlienImage.getHeight(null);// sets the height of the alien
        alienWidth = AlienImage.getWidth(null);// sets the width of the alien
        projectiles = new ArrayList<>();// creates a new projectiles array list
        // function that sets the difficulty of the alien class
        if(MainMenu.getDifficulty()==1){
        	shootDelay = 100;
        }
        else if(MainMenu.getDifficulty()==2){
        	shootDelay = 80;
        }
        else if(MainMenu.getDifficulty()==3){
        	shootDelay = 60;
        }
	if(MainMenu.isSfxOn() && !Constants.LINUX){
		initializeSound();// plays sound when alien is spawned
	}
    }
    
    // method that creates the number of aliens needed
    public static void generateAliens(int numAliens, double alienX, double alienY, double shipX, double shipY){
    	numberOfAliens = numAliens;
        for(int i = 0; i < numAliens; i++){
            aliens.add(new Alien(alienX, alienY, shipX, shipY));
        }
        scoreX = new int[numAliens];
		scoreY = new int[numAliens];
		scoreTime = new int[numAliens];
		deathTimer = new int[numAliens];
		arrayPos = 0;
    }
   
    // method that determines the spawn location of the aliens 
    public static void spawnAlienAtLocation(int numAliens){
    	numberOfAliens = numAliens;// creats this many aliens
        double alienX, alienY;// two variabels used for the position of the alien
        // uses a random function
        for(int i = 0; i < numAliens; i++){
            alienX =  (Math.ceil(Math.random() * 500));
            alienY =  (Math.ceil(Math.random() * 500));
            aliens.add(new Alien(alienX, alienY, Constants.SHIP.getX(), Constants.SHIP.getY()));
        }
        scoreX = new int[numAliens];
		scoreY = new int[numAliens];
		scoreTime = new int[numAliens];
		deathTimer = new int[numAliens];
		arrayPos = 0;
    }
    
    // sound method for the class
    private void initializeSound(){
		try {
			File alienHit = new File("src/bangLarge.wav");//for when aliens explode
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(alienHit);
			alienGoBoom = AudioSystem.getClip();
			alienGoBoom.open(audioIn);
			
			File alienShoot = new File("src/alien_laser.wav");//for when aliens shoot
			AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(alienShoot);
			alienLaser = AudioSystem.getClip();
			alienLaser.open(audioIn1);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
    
    // method to play sound when alien gets destroyed
    public void playAlienHitSound(){
		alienGoBoom.setFramePosition(0);
		alienGoBoom.start();
	}
   
    // method to play sound when alien shoots
    public void playAlienShootSound(){
    	alienLaser.setFramePosition(0);
    	alienLaser.start();
    }
    
    // method to time shooting intevals
    public void resetShootDelay(){
    	shoot = 200;
    }
    
    // method to get the aliens array list
    public static ArrayList<Alien> getAliens(){
    	return aliens;
    }
    
    // method to use the class ProjectilesAliens
    public ArrayList<ProjectilesAliens> getShots(){
        return this.projectiles;
    }
   
    // method to get the points for player one off of aliens
    public static int getPointsP1(){
    	return pointsPlayer1;
    }
    
    // method to get the number of aliens
    public static int getNumAliens(){
    	return numberOfAliens;
    }
    
    // method to get the .gif file for alien explosion
    public static ImageIcon getAlienExplosion(){
    	return alienExplosion;
    }
    
    // method to get aliens x coordinate
    public double getX(){
    	return xPos;
    }
    
    // method to get the aliens y coordinate
    public double getY(){
    	return yPos;
    }
    


    public static void reset(){
    	aliens.removeAll(aliens);
    	numberOfAliens = 0;
    }
    
    public static void resetPlayerScore(){
    	pointsPlayer1 = 0;
    	//PP2
    }
    
    // method to draw aliens and things that relate to the aliens
    public static void drawAlien(Graphics g){
        for(int i = 0; i < aliens.size(); i++){
    		g.drawImage(AlienImage, (int) aliens.get(i).xPos, (int)aliens.get(i).yPos, null);// draws aliens using the alien image and the alien x and y positions
    		aliens.get(i).collisionShip();// checks if the alien should be removed from the screen 
    		aliens.get(i).checkCollisionProjectile();// checks if the alien should be removed from the screen
    	}
    	// delay method for drawing the aliens
        if(drawDelay < 0 && alienCount < aliens.size()){
            alienCount++;
            drawDelay = 45*40;
        }else{
            drawDelay--;
        }
        // method that draws a small number above an exploded alien indicating the users gained score
        for(int i = 0; i < scoreX.length; i++){
			if(scoreX[i] != 0 || scoreY[i] != 0){ //There is a score
				if(scoreTime[i] > 0){
					g.setFont(new Font("Arial", Font.BOLD, 12)); // set font
					g.setColor(Color.RED);// set text to red
					g.drawString("" + scoreValue, scoreX[i], scoreY[i]);
					scoreTime[i]--;// how long the number should appear
					scoreY[i] -= 1;
				}
				// length of .gif file
                                if(deathTimer[i] > 0) {
					g.drawImage(getAlienExplosion().getImage(), scoreX[i]-40, scoreY[i]-40, null); // draw the explosion for as long as the timer
					deathTimer[i]--;
				}
			}
		}
    }
    
    // a method that allows the aliens to track and follow the users ship
    public void find(){
    	double alienDistance = //finds alien in relation to ship
    			Math.sqrt(Math.pow((shipX - xPos), 2) + 
    					Math.pow((shipY - yPos), 2));
    	// keeps aliens 100 pixels away from the aliens
    	if(alienDistance < 100){
    		changeX(0);
    		changeY(0);
        // random function that tells the alien how to move
        }else{
    		int randomValueX = (int) (Math.random() * 10) % 5;
    		int randomValueY = (int) (Math.random() * 10) % 5;
    		int xVel = xPos < shipX ? randomValueX : -randomValueX;
    		int yVel = yPos < shipY ? randomValueY : -randomValueY;
    		changeX(xVel);
    		changeY(yVel);
        }
    }
    
    // method that enables the aliens to find the ship and shoot at it
    public void shoot(){
        if(shoot >= shootDelay && Constants.SHIP.isAlive()){// keeps shooting interval constant
        	double deltaX = shipX - xPos;// sets deltaX which is the x direction it will be shot in
        	double deltaY = shipY - yPos;// sets deltaY which is the y direction it will be shot in
            double theta = Math.atan2(deltaY, deltaX);
            ProjectilesAliens AlienProjectiles = // calls and creates the AliensProjectiles class
            		new ProjectilesAliens(xPos, yPos, theta, xVelocity, yVelocity);
            projectiles.add(AlienProjectiles);
            if(MainMenu.isSfxOn() && !Constants.LINUX){
            	this.playAlienShootSound();// shooting sound plays
            }
            shoot = 0;
        }else{
            shoot++;
        }
    }
    
    //keeps the aliens on the screen
    public void detectEdges(){
        if(xPos <= 16){
            changeX(1);
        }
        if(xPos >= 784){
            changeX(-1);
        }
        if(yPos <= 16){
            changeY(1);
        }
        if(yPos >= 584){
            changeY(-1);
        }
    }
    
    // method that updates the x velocity
    public void changeX(int dir){
        xVelocity = dir;
    }
    
    // method that updates the y velocity
    public void changeY(int dir){
        yVelocity = dir;
    }
    
    // method that updates the x and y position of the alien
    public void move(){
        xPos += xVelocity;
        yPos += yVelocity;
        circle = new Ellipse2D.Double(xPos, yPos, alienWidth, alienHeight);// creates a hit box in essence
    }
    
    // method that gets the hit box of the alien
    public Ellipse2D getBounds(){
    	return circle;
    }
   
    // method that determines if a alien and ship have collided
    public boolean collisionShip(){
    	// checks if the hit box of the ship intersects the hit box of the alien
        if(Constants.SHIP.getBounds() != null && circle.intersects(Constants.SHIP.getBounds()) && 
				Constants.SHIP.isAlive() && !Constants.SHIP.isInvulnerable()){
			Constants.SHIP.setAlive(false);
			if(MainMenu.isSfxOn() && !Constants.LINUX){
				this.playAlienHitSound();// plays a sound if they collide
			}
			return true;
    	}
    	return false;
    }
    
    // method that determines if an alien has been hit by a ships projectile
    public void checkCollisionProjectile(){
        // check if the aliens hit box intersects the projectiles hit box
        for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
    		if(circle.intersects(Constants.SHIP.getProjectiles().get(i).getProjectileBounds())){
    			Constants.SHIP.getProjectiles().remove(i);
    			if(MainMenu.isSfxOn() && !Constants.LINUX){
					this.playAlienHitSound();// plays a sound if they intersect
				}
    			// updates the score array giving the player the points for hitting the alien
                        scoreX[arrayPos] = (int) this.xPos;
    			scoreY[arrayPos] = (int) this.yPos;
    			scoreTime[arrayPos] = 80;
    			deathTimer[arrayPos] = 75;
    			aliens.remove(this);// removes alien from array list
    			numberOfAliens--;// updates variable
    			pointsPlayer1 += scoreValue; // updates scroe
    			arrayPos++; // updates array
            }
    	}
    }
}