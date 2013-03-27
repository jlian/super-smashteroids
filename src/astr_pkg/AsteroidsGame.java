package astr_pkg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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
	
//	private static int score1;
	private static Font scoreFont;

        static Rectangle AlienImage = new Rectangle(250, 250, 16, 16);
        static Alien AI;
//        Clip clip;
        
	public void init(){
		setScoreFont();
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		addKeyListener(this);
		ship = new Ship(400, 300, 0, .35, .98, .1);
		//double speed = 20*Math.random();
		//double asteroidTheta = Math.random()*2*Math.PI;
		Asteroid.generateAsteroids(10);
		AI = new Alien(AlienImage, Constants.SHIP.getX(), Constants.SHIP.getY());
                thread = new Thread(this);
		thread.start();
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
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
//		if(e.getKeyCode() == KeyEvent.VK_LEFT && !MainMenu.isControlWasd()){
//			ship.setTurningLeft(true);
//		}else if(e.getKeyCode() == KeyEvent.VK_A && MainMenu.isControlWasd()){
//			ship.setTurningLeft(true);
//		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT && !MainMenu.isControlWasd()){
//			ship.setTurningRight(true);
//		}else if(e.getKeyCode() == KeyEvent.VK_D && MainMenu.isControlWasd()){
//			ship.setTurningRight(true);
//		}

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
			case KeyEvent.VK_SPACE:
				if(Constants.SHIP.shotWaitLeft <= 0){
					Constants.SHIP.makeItRain(true);
				}
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
//		if(e.getKeyCode() == KeyEvent.VK_SPACE){
//			if(Constants.SHIP.shotWaitLeft <= 0){
//				Constants.SHIP.makeItRain(true);
////				System.out.println(clip.getMicrosecondLength());
////				clip.setFramePosition(0);
////				clip.start();
//			}
//		}
		
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
//			if(Constants.SHIP.shotWaitLeft <= 0){
//				Constants.SHIP.makeItRain(false);
//				
//			}
//			if(clip.isRunning()){
//				clip.stop();
//			}
//			clip.setFramePosition(0);
		}
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Check");
		while (true){
			startTime = System.currentTimeMillis();
			for(int i = 0; i < Asteroid.getAsteroids().size(); i++){
				Asteroid.getAsteroids().get(i).move(getWidth(), getHeight());
			}
			Constants.SHIP.move(getWidth(), getHeight());
			AI.xCoor = Constants.SHIP.getX();
            AI.yCoor = Constants.SHIP.getY();
                        AI.find();
                        AI.move();
                        AI.detectEdges();
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
		AI.drawAlien(g);
                Constants.SHIP.drawShip(g);
		if(Constants.SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
					Constants.SHIP.getProjectiles().get(i).move();
			}
			Projectiles.drawProjectiles(g);	
		}
		
		g.setColor(Color.CYAN);
		g.setFont(scoreFont);
		g.drawString("SCORE   " + Asteroid.getPointsP1(), 10, 20);
		
	}



}