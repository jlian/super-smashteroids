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
import javax.swing.JPanel;

public class AsteroidsGame extends JPanel implements Runnable, KeyListener{

	long startTime, endTime, frameRate;
    Thread thread;
	Ship ship;
	int count = 5;
	Thread A1;
	
//	private static int score1;
	static Font scoreFont;
	
	static Rectangle AlienImage = new Rectangle(250, 250, 16, 16);
    static Alien AI;

    private static int respawnTime;
    private static int delay, level, difficulty, startAstr;
    private static boolean nextWave, levelUp;
    private Clip thrusterSound;
     
        
	public void init(){
		nextWave = false;
		levelUp = true;
		delay = 100;
		level = 1;
		startAstr = 10;
		difficulty = 1; //change when implement difficulty selection 
		
		setScoreFont();
		initializeSounds();
		
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		respawnTime = 0;
		addKeyListener(this);
		Asteroid.generateAsteroids(startAstr);
		Alien.generateAliens(count);
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
//				playThrusterSound();
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
//				playThrusterSound();
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
//				thrusterSound.stop();
//				thrusterSound.setFramePosition(0);
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
//				thrusterSound.stop();
//				thrusterSound.setFramePosition(0);
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
		
		while (true){
			startTime = System.currentTimeMillis();
			for(int i = 0; i < Asteroid.getAsteroids().size(); i++){
				Asteroid.getAsteroids().get(i).move(getWidth(), getHeight());
			}
			if(Asteroid.getAsteroids().isEmpty()){
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
					Asteroid.generateAsteroids(startAstr);
					nextWave = false;
					levelUp = true;
				}
			} 
			if(respawnTime <= 0){
				Constants.SHIP.move(getWidth(), getHeight());
			}else{
				respawnTime--;
			}
			for (int i = 0; i < count; i++){
				Alien.getAliens()[i].xCoor = Constants.SHIP.getX();
				Alien.getAliens()[i].yCoor = Constants.SHIP.getY();
				Alien.getAliens()[i].shoot();
				Alien.getAliens()[i].move();
				Alien.getAliens()[i].detectEdges();
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
		Asteroid.drawAsteroid(g);
		Alien.drawAlien(g);
        if(respawnTime <= 0){
        	Constants.SHIP.drawShip(g);
        }
		
		if(Constants.SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
					Constants.SHIP.getProjectiles().get(i).move();
			}
			Projectiles.drawProjectiles(g);	
		}
		
		g.setColor(Color.CYAN);
		g.setFont(scoreFont);
		
		g.drawString("SCORE   " + Asteroid.getPointsP1(), 10, 40);
		g.drawString("SCORE   " + Asteroid.getPointsP1(), 10, 40);
		g.drawString("Level   " + level, Constants.WIDTH - 110, 40);
		if(nextWave){
			g.setFont(Constants.MENU_FONT);
			FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
			String strLevel = "Level   " + (level);
			g.setColor(Color.orange);
			g.drawString(strLevel, 400-(metrics.stringWidth(strLevel)/2), 300);
		} 
	}



}