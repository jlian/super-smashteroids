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

import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AsteroidsGame extends JPanel implements Runnable, KeyListener{

	long startTime, endTime, frameRate;
    Thread thread;
	Ship ship;
	
	static Font scoreFont;
	
	static Rectangle AlienImage = new Rectangle(250, 250, 16, 16);
    static Alien AI;

    private static int respawnTime;
    private static int delay, level, difficulty, startAstr, numAliens;
    private static boolean nextWave, levelUp;
    private static int numLivesP1;
    private Clip thrusterSound;
    private static ImageIcon gameBackground = new ImageIcon("src/astr_pkg/BG-game.jpg");
    private boolean blowup = true;
    private static ImageIcon shipExplosion = new ImageIcon("src/astr_pkg/explosion.gif");
    private static int count = 0;
	
    public void init(){
		nextWave = false;
		levelUp = true;
		delay = 100;
		level = 1;
		numLivesP1 = 3;
		startAstr = MainMenu.getDifficulty() * 5;
		numAliens = MainMenu.getDifficulty();
		difficulty = MainMenu.getDifficulty();
		
		setScoreFont();
		if(MainMenu.isSfxOn() && !Constants.LINUX){
			initializeSounds();
		}
		
		
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		respawnTime = 0;
		addKeyListener(this);
		Asteroid.generateAsteroids(startAstr);
		Alien.generateAliens(numAliens);
        thread = new Thread(this);
		thread.start();
	}

	private void initializeSounds(){
		try {
			File thrusters = new File("src/thrust.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(thrusters);
			thrusterSound = AudioSystem.getClip();
			thrusterSound.open(audioIn);
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
					new File("src/arcadeClassic.ttf"));
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
	

	public void playThrusterSound(){
		thrusterSound.setFramePosition(0);
		thrusterSound.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void setRespawnTime(int time){
		respawnTime = time;
	}
	
	public static int getNumLivesP1(){
		return numLivesP1;
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
//				if(MainMenu.isSfxOn() && !Constants.LINUX){
//					playThrusterSound();
//				}
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
//				if(MainMenu.isSfxOn() && !Constants.LINUX){
//					playThrusterSound();
//				}
				break;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Constants.SHIP.makeItRain(true);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
//				if(MainMenu.isSfxOn() && !Constants.LINUX){
//					thrusterSound.stop();
//					thrusterSound.setFramePosition(0);
//				}
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
//				if(MainMenu.isSfxOn() && !Constants.LINUX){
//					thrusterSound.stop();
//					thrusterSound.setFramePosition(0);
//				}
			
				break;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Constants.SHIP.makeItRain(false);
		}
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (numLivesP1 > 0){
			startTime = System.currentTimeMillis();
			if(Constants.SHIP.isAlive()){
				Constants.SHIP.move(getWidth(), getHeight());
			}
			for(int i = 0; i < Asteroid.getAsteroids().size(); i++){
				Asteroid.getAsteroids().get(i).move(getWidth(), getHeight());
			}
			if(Asteroid.getAsteroids().isEmpty() && Alien.getNumAliens() == 0){
				nextWave = true;
				if(levelUp){
					level ++;
					levelUp = false;
				}
				if(delay > 0){
					delay--;
				}
				if(delay <= 0){
					delay = 100;
					startAstr += difficulty;
					numAliens += difficulty;
					Asteroid.generateAsteroids(startAstr);
					Alien.generateAliens(numAliens);
					nextWave = false;
					levelUp = true;
				}
			} 
			
//			for (int i = 0; i < numAliens; i++){
			for(int i = 0; i < Alien.getAliens().size(); i++){
				Alien.getAliens().get(i).shipX = Constants.SHIP.getX();
				Alien.getAliens().get(i).shipY = Constants.SHIP.getY();
				Alien.getAliens().get(i).find();
				Alien.getAliens().get(i).shoot();
				Alien.getAliens().get(i).move();
				Alien.getAliens().get(i).detectEdges();
//				if(Alien.getAliens()[i] != null){
//					Alien.getAliens()[i].shipX = Constants.SHIP.getX();
//					Alien.getAliens()[i].shipY = Constants.SHIP.getY();
//					Alien.getAliens()[i].find();
//					Alien.getAliens()[i].shoot();
//					Alien.getAliens()[i].move();
//					Alien.getAliens()[i].detectEdges();
//				}
			}
			repaint();
			endTime = System.currentTimeMillis();
			try {
				if(frameRate - (endTime - startTime) > 0){
					Thread.sleep(frameRate - (endTime - startTime));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		g.drawImage(getGameBackground().getImage(), 0, 0, getWidth(), getHeight(), this);
		Asteroid.drawAsteroid(g);
		Alien.drawAlien(g);
        if(Constants.SHIP.isAlive()){
        	Constants.SHIP.drawShip(g);
        }
        else if(blowup){
        	if(count<285){
        		g.drawImage(getExplosion().getImage(), (int) Constants.SHIP.getX()-180, (int) Constants.SHIP.getY()-180, this);
        		count++;
        	}
        	else{
        		Constants.SHIP.reset();
        		blowup = false;
        		count = 0;
        		
        	}
        }
        if(!Constants.SHIP.isAlive()){
			if(Constants.SHIP.getRespawnTime() < 80){
				Constants.SHIP.incrementRespawnTime();
			}else{
				Constants.SHIP.reset();
				Constants.SHIP.resetRespawnTime();
				Constants.SHIP.resetInvulnerabilityTime();//If ship has just respawned, make invulnerable
				Constants.SHIP.setAlive(true);
				blowup = true;
				numLivesP1--;
			}
		}
        
        if(Constants.SHIP.isInvulnerable()){
        	Constants.SHIP.incrementInvulnerabilityTime();
        }
        
		if(Constants.SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
					Constants.SHIP.getProjectiles().get(i).move();
			}
			Projectiles.drawProjectiles(g);	
		}
		/*
		 * ADDED THIS
		 */
//		for(int i = 0; i < Alien.getAliens().length; i++){
//			if(Alien.getAliens()[i] != null && 
//					Alien.getAliens()[i].getShots().size() > 0){
//				for(int j = 0; j < Alien.getAliens()[i].getShots().size(); j++){
//					Alien.getAliens()[i].getShots().get(j).move();
//				}
//				ProjectilesAliens.drawProjectiles(g);
//			}
//		}
		
		for(int i = 0; i < Alien.getAliens().size(); i++){
			if(Alien.getAliens().get(i).getShots().size() > 0){
				for(int j = 0; j < Alien.getAliens().get(i).getShots().size(); j++){
					Alien.getAliens().get(i).getShots().get(j).move();
				}
				ProjectilesAliens.drawProjectiles(g);
			}
		}
		
		
		g.setColor(Color.CYAN);
		g.setFont(scoreFont);

		g.drawString("SCORE   " + (Asteroid.getPointsP1() + Alien.getPointsP1()),
				10, 40);
		g.drawString("Level   " + level, Constants.WIDTH - 110, 40);
		g.drawString("Lives Left  " + numLivesP1, Constants.WIDTH - 160, Constants.HEIGHT - 20);
		
		if(nextWave){
			g.setFont(Constants.MENU_FONT);
			FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
			String strLevel = "Level   " + (level);
			g.setColor(Color.orange);
			g.drawString(strLevel, 400-(metrics.stringWidth(strLevel)/2), 300);
		} 
	}



}