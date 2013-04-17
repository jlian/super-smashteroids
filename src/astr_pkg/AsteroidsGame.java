package astr_pkg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

//This class is the main game class that handles the game loop
public class AsteroidsGame extends JPanel implements Runnable, KeyListener{

	long startTime, endTime, frameRate;
    Thread thread;
	Ship ship;
	
	static Font scoreFont;
	
	static Rectangle AlienImage = new Rectangle(250, 250, 16, 16);
    static Alien AI;

    private static int respawnTime;
    private static int delay, level, difficulty, startAstr, numAliens, rapidfire, scattershot, lifeup;
    private static boolean nextWave, levelUp;
    private static int numLivesP1, numLivesP2;
    private static Clip gameOverSound;
    private static ImageIcon gameBackground = new ImageIcon("FX/graphics/BG-game.jpg");
    private static ImageIcon shipExplosion = new ImageIcon("FX/graphics/explosion.gif");
    private static int count = 0;

    private int score;
    private long gameStartTime, gameEndTime;
	
    public void init(){
    	//Initializes the game: this is where the game begins the run!
		nextWave = false;
		levelUp = true;
		delay = 100;
		rapidfire = 0;
		scattershot = 1;
		lifeup = 2;
		level = 1;

		numLivesP1 = 3;
		
		//Start off multiplayer if selected as such
		if(MainMenu.isMultiplayer()){
			numLivesP2 = 3;
		}else{
			numLivesP2 = 0;
		}
		
		//The number of asteroids, aliens, are specified with respect to difficulty
		startAstr = MainMenu.getDifficulty() * 5;
		numAliens = MainMenu.getDifficulty();
		difficulty = MainMenu.getDifficulty();
		
		setScoreFont();
		
		//Sounds have issues on Linus systems, so this is our way around it
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSounds();
		}
		
		
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		respawnTime = 0;
		addKeyListener(this);
		//Make enemies
		Asteroid.generateAsteroids(startAstr);
		Alien.spawnAlienAtLocation(numAliens);
		//This calls run() to run the game
        thread = new Thread(this);
		thread.start();
	}

	private void initializeSounds(){
		try {
			File gameOver = new File("FX/audio/game_over.wav");
			AudioInputStream audioIn1 = AudioSystem.getAudioInputStream(gameOver);
			gameOverSound = AudioSystem.getClip();
			gameOverSound.open(audioIn1);
			
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
	
	private void setScoreFont(){
		try {
			scoreFont = Font.createFont(Font.TRUETYPE_FONT, 
					new File("FX/fonts/arcadeClassic.ttf"));
			scoreFont = scoreFont.deriveFont(Font.PLAIN, 26);
		GraphicsEnvironment ge = 
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(scoreFont);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void gameReset(){
		//Resets the game
		Alien.reset();
		Asteroid.reset();
		Alien.resetPlayerScore();
		Asteroid.resetPlayerScore();
		Constants.SHIP.resetScatterShot();
	}
	
	public boolean getIsHighScore() {
		File hs = null;
		//Find which file to read high scores from based on difficulty
		if(MainMenu.getDifficulty() == 1){
			hs = new File("src/astr_pkg/hs_easy.csv");
		}else if(MainMenu.getDifficulty() == 2){
			hs = new File("src/astr_pkg/hs_medium.csv");
		}else if(MainMenu.getDifficulty() == 3){
			hs = new File("src/astr_pkg/hs_hard.csv");
		}
		try {
			Scanner fread = new Scanner(hs); //open file
			int i = 1;
			String line = "error";
			String[] fields = null;
			String score;
			while (fread.hasNext()) { //While not end of file
				line = fread.next();
				fields = line.split(",");
				score = fields[1];
				i++;
				if(i == 11){ //Find the 10th high score position
					int scoreInt = Integer.parseInt(score);
					//If the high score in the 10th position is less than player score
					if((Asteroid.getPointsP1() + Alien.getPointsP1()) > scoreInt){
						fread.close();
						return true; //Then player got a high score
					}
					break;
				}
			}
			//If the high score does not have 10 high scores yet,
			if(i < 11){
				fread.close(); 
				return true; //Player got a high score
			}
			fread.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false; //If none of the cases above hold, no high score
	}
	public int getScore() {
		return score;
	}	
	public void playGameOverSound(){
		gameOverSound.setFramePosition(0);
		gameOverSound.start();
	}
	public static void setRespawnTime(int time){
		respawnTime = time;
	}
	
	public static int getNumLivesP1(){
		return numLivesP1;
	}
	
	public static int getNumLivesP2(){
		return numLivesP2;
	}
	
	public static ImageIcon getGameBackground(){
		return gameBackground;
	}
	
	public static ImageIcon getExplosion(){
		return shipExplosion;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		//This is the key listener 
		//It tells the ships what's up
		if(MainMenu.isControlWasd()){
			switch(e.getKeyCode()){
			case KeyEvent.VK_A:
				Constants.SHIP.setTurningLeft(true);
				break;
			case KeyEvent.VK_D:
				Constants.SHIP.setTurningRight(true);
				break;
			case KeyEvent.VK_W:
				Constants.SHIP.setAccelerating(true);
				break;
			
			}
		}else{
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				Constants.SHIP.setTurningLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				Constants.SHIP.setTurningRight(true);
				break;
			case KeyEvent.VK_UP:
				Constants.SHIP.setAccelerating(true);
				break;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Constants.SHIP.makeItRain(true);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		if(MainMenu.isMultiplayer()){
			if(MainMenu.isControlWasd()){
				switch(e.getKeyCode()){
				case KeyEvent.VK_LEFT:
					Constants.P2SHIP.setTurningLeft(true);
					break;
				case KeyEvent.VK_RIGHT:
					Constants.P2SHIP.setTurningRight(true);
					break;
				case KeyEvent.VK_UP:
					Constants.P2SHIP.setAccelerating(true);
					break;
				case KeyEvent.VK_ENTER:
					Constants.P2SHIP.makeItRain(true);
					break;
				}
			}else{
				switch(e.getKeyCode()){
				case KeyEvent.VK_A:
					Constants.P2SHIP.setTurningLeft(true);
					break;
				case KeyEvent.VK_D:
					Constants.P2SHIP.setTurningRight(true);
					break;
				case KeyEvent.VK_W:
					Constants.P2SHIP.setAccelerating(true);
					break;
				case KeyEvent.VK_F:
					Constants.P2SHIP.makeItRain(true);
					break;
				}
			}
//			if(e.getKeyCode() == KeyEvent.VK_CONTROL){
//				Constants.P2SHIP.makeItRain(true);
//			}
		}	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//This is the revere of keyPressed() for event that the user release the key press
		//That should make sure that the ship doesn't continue move forward
		if(MainMenu.isControlWasd()){
			switch(e.getKeyCode()){
			case KeyEvent.VK_A:
				Constants.SHIP.setTurningLeft(false);
				break;
			case KeyEvent.VK_D:
				Constants.SHIP.setTurningRight(false);
				break;
			case KeyEvent.VK_W:
				Constants.SHIP.setAccelerating(false);
				break;
			}
		}else{
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				Constants.SHIP.setTurningLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				Constants.SHIP.setTurningRight(false);
				break;
			case KeyEvent.VK_UP:
				Constants.SHIP.setAccelerating(false);
				break;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Constants.SHIP.makeItRain(false);
		}
		
		if(MainMenu.isMultiplayer()){
			if(MainMenu.isControlWasd()){
				switch(e.getKeyCode()){
				case KeyEvent.VK_LEFT:
					Constants.P2SHIP.setTurningLeft(false);
					break;
				case KeyEvent.VK_RIGHT:
					Constants.P2SHIP.setTurningRight(false);
					break;
				case KeyEvent.VK_UP:
					Constants.P2SHIP.setAccelerating(false);
					break;
				case KeyEvent.VK_ENTER:
					Constants.P2SHIP.makeItRain(false);
					break;
				}
			}else{
				switch(e.getKeyCode()){
				case KeyEvent.VK_A:
					Constants.P2SHIP.setTurningLeft(false);
					break;
				case KeyEvent.VK_D:
					Constants.P2SHIP.setTurningRight(false);
					break;
				case KeyEvent.VK_W:
					Constants.P2SHIP.setAccelerating(false);
					break;
				case KeyEvent.VK_F:
					Constants.P2SHIP.makeItRain(false);
					break;
				}
			}
//			if(e.getKeyCode() == KeyEvent.VK_CONTROL){
//				Constants.P2SHIP.makeItRain(false);
//			}
		}
		
	}


	@Override
	public void run() {
		// This method runs from init() and runs continuously until the players lose all of the lives.
		gameStartTime = System.currentTimeMillis();
		while (numLivesP1 > 0 || numLivesP2 > 0){
			startTime = System.currentTimeMillis();
			
			//Move the ship!
			if(Constants.SHIP.isAlive()){
				Constants.SHIP.move(getWidth(), getHeight());
			}
			score = (Asteroid.getPointsP1() + Alien.getPointsP1());
			
			//MP
			if(Constants.P2SHIP.isAlive()){
				Constants.P2SHIP.move(getWidth(), getHeight());
			}
			
			//Move the asteroids
			for(int i = 0; i < Asteroid.getAsteroids().size(); i++){
				Asteroid.getAsteroids().get(i).move(getWidth(), getHeight());
			}
			if(Asteroid.getAsteroids().isEmpty() && Alien.getNumAliens() == 0){
				//If all aliens and asteroids are destroyed, level up, difficulty up
				nextWave = true;
				if(levelUp){
					level ++;
					levelUp = false;
				}
				if(delay > 0){
					delay--;
				}
				if(delay <= 0){
					//With the increase of difficulty comes upgrades. Like scatter shot
					delay = 100;
					startAstr += difficulty;
					numAliens += difficulty;
					Asteroid.generateAsteroids(startAstr);
                                        if (rapidfire == 0) {
						Constants.SHIP.decreaseShotWait();
						rapidfire = 3;
					}
					rapidfire--;
					if (scattershot == 0) {
						Constants.SHIP.setScatterShot();
						scattershot =0;
					}
					scattershot--;
					if (lifeup ==0) {
						numLivesP1++;
						lifeup = 3;
						//put in message letting play know of upgrades
					}
					lifeup--;
					Alien.spawnAlienAtLocation(numAliens);
					nextWave = false;
					levelUp = true;
				}
			} 
			
			//Make the aliens do their thing: shoot at the player, follow the player, move, etc
			for(int i = 0; i < Alien.getAliens().size(); i++){
				Alien.getAliens().get(i).shipX = Constants.SHIP.getX();
				Alien.getAliens().get(i).shipY = Constants.SHIP.getY();
				Alien.getAliens().get(i).find();
				Alien.getAliens().get(i).shoot();
				Alien.getAliens().get(i).move();
				Alien.getAliens().get(i).detectEdges();

			}
			
			//Draws the frame
			repaint();
			endTime = System.currentTimeMillis();
			
			//If the loop runs faster than the frame rate, lock to the frame rate for better consistency, visually
			try {
				if(frameRate - (endTime - startTime) > 0){
					Thread.sleep(frameRate - (endTime - startTime));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			MainMenu.stopBackgroundMusic();
			playGameOverSound();
		}
		
		//If all lives are lost, go to game over.
		gameEndTime = System.currentTimeMillis();
		MainMenu.getMenu().gameOver();
	}
	
	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		AsteroidsGame.level = level;
	}

	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		AsteroidsGame.difficulty = difficulty;
	}

	public long getGameLength(){
		return (gameEndTime - gameStartTime)/1000;
	}


	@Override
	protected void paintComponent(Graphics g) {
		//This draw the frame
		super.paintComponent(g);
		
		//Draw all the background, etc
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		g.drawImage(getGameBackground().getImage(), 0, 0, getWidth(), getHeight(), this);
		Asteroid.drawAsteroid(g);
		Alien.drawAlien(g);
       
		//Draw the ship as long as it lives
		if(Constants.SHIP.isAlive()){
        	Constants.SHIP.drawShip(g);
        }
		//If the ship dies, do all of the following
		//This really shouldn't be here but we could not find a better way to do this.
		else if(!Constants.SHIP.isAlive() && numLivesP1 > 0){
        	if(Constants.SHIP.getRespawnTime() < 80){
				Constants.SHIP.incrementRespawnTime();
				//Now the code above is no longer needed: draw the explosion as the respawn timer counts!!
				//I've removed all the code and variables pertaining to count/blowup/etc
				//Still can't figure out how to make the animation play from the beginning each time
				g.drawImage(getExplosion().getImage(), (int) Constants.SHIP.getX()-180, (int) Constants.SHIP.getY()-180, this);
			}else{
				//This puts the player back in the game with one life less
				Constants.SHIP.reset();
				Constants.SHIP.resetRespawnTime();
				Constants.SHIP.resetInvulnerabilityTime();//If ship has just respawned, make invulnerable
				numLivesP1--;
				Constants.SHIP.setAlive(true);
			}
		}
        //	else if(numLivesP1 == 1){
//			numLivesP1--;
//		}
        
        if(Constants.SHIP.isInvulnerable()){
        	Constants.SHIP.incrementInvulnerabilityTime();
        }
        
        //Draw all the projectiles
		if(Constants.SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
					Constants.SHIP.getProjectiles().get(i).move();
			}
			Projectiles.drawProjectiles(g);	
		}
		
		//MP
		if(MainMenu.isMultiplayer()){
			if(Constants.P2SHIP.isAlive() && numLivesP2 > 0){
	        	Constants.P2SHIP.drawShip(g);
	        }

			else if(!Constants.P2SHIP.isAlive()){
				if(Constants.P2SHIP.getRespawnTime() < 80){
					Constants.P2SHIP.incrementRespawnTime();
					//Same deal as above! I like how we have repeating code, it's the best
					g.drawImage(getExplosion().getImage(), (int) Constants.P2SHIP.getX()-180, (int) Constants.P2SHIP.getY()-180, this);
				}else{
					Constants.P2SHIP.reset();
					Constants.P2SHIP.resetRespawnTime();
					Constants.P2SHIP.resetInvulnerabilityTime();//If ship has just respawned, make invulnerable
					numLivesP2--; //new Var
					Constants.P2SHIP.setAlive(true);

				}
			}
	        
	        if(Constants.P2SHIP.isInvulnerable()){
	        	Constants.P2SHIP.incrementInvulnerabilityTime();
	        }
	        
			if(Constants.P2SHIP.getProjectiles().size() > 0){
				for(int i = 0; i < Constants.P2SHIP.getProjectiles().size(); i++){
						Constants.P2SHIP.getProjectiles().get(i).move();
				}
				Projectiles.drawProjectiles(g);	
			}
		}
		
		//Draw alien projectiles, which are more powerful and scarier than regular ones
		for(int i = 0; i < Alien.getAliens().size(); i++){
			if(Alien.getAliens().get(i).getShots().size() > 0){
				for(int j = 0; j < Alien.getAliens().get(i).getShots().size(); j++){
					Alien.getAliens().get(i).getShots().get(j).move();
				}
				ProjectilesAliens.drawProjectiles(g);
			}
		}
		
		
		//Draw the rest of the stuff
		g.setColor(Color.CYAN);
		g.setFont(scoreFont);

		g.drawString("SCORE   " + (Asteroid.getPointsP1() + Alien.getPointsP1()),
				10, 40);
		g.drawString("Level   " + level, Constants.WIDTH - 110, 40);
		g.drawString("Lives Left P1  " + numLivesP1, Constants.WIDTH - 210, Constants.HEIGHT - 20);
		
		if(MainMenu.isMultiplayer()){
			g.drawString("Lives Left P2  " + numLivesP2, 10, Constants.HEIGHT - 20);
		}
		
		if(nextWave){
			g.setFont(Constants.MENU_FONT);
			FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
			FontMetrics upgradeMetrics = g.getFontMetrics(Constants.SELECTIONS_FONT);
			String strLevel = "Level   " + (level);
			g.setColor(Color.orange);
			g.drawString(strLevel, 400-(metrics.stringWidth(strLevel)/2), 300);
			
			if (rapidfire == 0 && Constants.SHIP.shotWait > 4) {
				g.setFont(Constants.SELECTIONS_FONT);
				String rapidfireMessage = "Your firing rate has increased!";
				g.drawString(rapidfireMessage, 400-(upgradeMetrics.stringWidth(rapidfireMessage)/2), 350);
			}
			if (scattershot == 0 && Constants.SHIP.scatterShot <= 1) {
				g.setFont(Constants.SELECTIONS_FONT);
				String scattershotMessage = "You've picked up a multi-shot upgrade!";
				g.drawString(scattershotMessage, 400-(upgradeMetrics.stringWidth(scattershotMessage)/2), 350);
			}
			if (lifeup == 0) {
				g.setFont(Constants.SELECTIONS_FONT);
				String lifeupMessage = "You've gained an additional life!";
				g.drawString(lifeupMessage, 400-(upgradeMetrics.stringWidth(lifeupMessage)/2), 350);
			}
		} 
	}



}
