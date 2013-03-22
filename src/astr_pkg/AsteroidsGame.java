package astr_pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class AsteroidsGame extends JPanel implements Runnable, KeyListener{

	long startTime, endTime, frameRate;
	Thread thread;
	Ship ship;
	Asteroid asteroid;

	public void init(){
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		addKeyListener(this);
		ship = new Ship(400, 300, 0, .35, .98, .1);
		double speed = 20*Math.random();
		double asteroidTheta = Math.random()*2*Math.PI;
		asteroid = new Asteroid(400, 300, 0, asteroidTheta,
				speed*Math.cos(asteroidTheta), speed*Math.sin(asteroidTheta), 3);
		thread = new Thread(this);
		thread.start();
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
				ship.setTurningLeft(true);
				break;
			case KeyEvent.VK_D:
				ship.setTurningRight(true);
				break;
			case KeyEvent.VK_W:
				ship.setAccelerating(true);
				break;
			}
		}else{
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				ship.setTurningLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				ship.setTurningRight(true);
				break;
			case KeyEvent.VK_UP:
				ship.setAccelerating(true);
				break;
			}
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
				ship.setTurningLeft(false);
				break;
			case KeyEvent.VK_D:
				ship.setTurningRight(false);
				break;
			case KeyEvent.VK_W:
				ship.setAccelerating(false);
				break;
			}
		}else{
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				ship.setTurningLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				ship.setTurningRight(false);
				break;
			case KeyEvent.VK_UP:
				ship.setAccelerating(false);
				break;
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Check");
		while (true){
			startTime = System.currentTimeMillis();
			asteroid.move(getWidth(), getHeight());
			ship.move(getWidth(), getHeight());
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
		asteroid.drawAsteroid(g);
		ship.drawShip(g);
	}



}