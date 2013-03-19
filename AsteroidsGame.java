package astr_pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class AsteroidsGame extends JPanel implements Runnable, KeyListener{

	long startTime, endTime, frameRate;
	Thread thread;
	//Ship ship;
	
	public void init(){
		startTime = 0;
		endTime = 0;
		frameRate = 25;
		addKeyListener(this);
		//ship = new Ship(400, 300, 0, .35, .98, .1);
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
					System.out.println("CHECK3");
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
			case KeyEvent.VK_SPACE:
				if(Constants.SHIP.shotWaitLeft <= 0){
					Constants.SHIP.makeItRain(false);
				}
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
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Check");
		while (true){
			startTime = System.currentTimeMillis();
			Constants.SHIP.move(getWidth(), getHeight());
			
			
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
		Constants.SHIP.drawShip(g);
		if(Constants.SHIP.getProjectiles().size() > 0){
			for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
					Constants.SHIP.getProjectiles().get(i).move();
			}
			Projectiles.drawProjectiles(g);	
		}
		
	}
	
	

}
