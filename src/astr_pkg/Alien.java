package astr_pkg;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;

public class Alien implements Runnable {
    
    int xDIR, yDIR;
    Image AlienImage;
    Rectangle AI, Ship;
    double xCoor, yCoor;
    private Ellipse2D.Double circle;
    private double height;
    private double width;
    
    public Alien(Rectangle A, Double B, Double C){
        xCoor = B;
        yCoor = C;
        AI = A;
        AlienImage = new ImageIcon("src/astr_pkg/Alien.png").getImage();
        height = AlienImage.getHeight(null);
        width = AlienImage.getWidth(null);
    }
    
    public void drawAlien(Graphics g){
        g.drawImage(AlienImage, AI.x, AI.y, null);
        
        Graphics2D g2D = (Graphics2D) g;
        
        if(collisionShip() || collisionProjectile()){
            g.setColor(Color.red);
        	g2D.draw(circle);
        }
    }

    public void find(){
        if(AI.x < xCoor){
            changeX(1);
        }
        if(AI.x > xCoor){
            changeX(-1);
        }
        if(AI.y < yCoor){
            changeY(1);
        }
        if(AI.y > yCoor){
            changeY(-1);
        }
    }
    
    public void detectEdges(){
        if(AI.x <= 0){
            changeX(1);
        }
        if(AI.x >= 800-AI.width){
            changeX(-1);
        }
        if(AI.y <= 0){
            changeY(1);
        }
        if(AI.y >= 800-AI.height){
            changeY(-1);
        }
    }
    
    public void changeX(int dir){
        xDIR = dir;
    }
    
    public void changeY(int dir){
        yDIR = dir;
    }

    public void move(){
        AI.x += xDIR;
        AI.y += yDIR;
       
        circle = new Ellipse2D.Double(AI.x, AI.y, width, height);
    }
    public Ellipse2D getBounds(){
    	return circle;
    }
    
    public boolean collisionShip(){
    	return circle.intersects(Constants.SHIP.getBounds());
    }
    public boolean collisionProjectile(){
        for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
    		if(circle.intersects(Constants.SHIP.getProjectiles().get(i).getProjectileBounds())){
				Constants.SHIP.getProjectiles().remove(i);
				return true;
			}
    	}
    	return false;
    }
    
    public void run(){
        try{
            while(true){
                find();
                move();
                detectEdges();
                Thread.sleep(5);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    } 
}
