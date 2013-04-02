package astr_pkg;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Alien {
    
    private static Image AlienImage;
    double shipX, shipY, xPos, yPos;
    private static double lastX, lastY;
    int xVelocity, yVelocity;
    private static Alien[] aliens;
    private static int drawDelay = 25;
    private static int alienCount = 1;
    private ArrayList<ProjectilesAliens> projectiles;
    double shoot;
    private Ellipse2D.Double circle;
    private double alienHeight;
    private double alienWidth;
    private static int pointsPlayer1 = 0;
    private static int numberOfAliens;
    
    private static int[] scoreX;
	private static int[] scoreY;
	private static int[] scoreTime;
	private static final int scoreValue = 100;
	
//	private static int count = 0;
	private static ImageIcon alienExplosion = new ImageIcon("src/astr_pkg/explosion3.gif");
    private static boolean collision = false;
	
    public Alien(double alienXPos, double alienYPos, double shipXPos, double shipYPos){
        xPos = alienXPos;
        yPos = alienYPos;
        shipX = shipXPos;
        shipY = shipYPos;
        AlienImage = new ImageIcon("src/astr_pkg/AlienM.png").getImage();
        alienHeight = AlienImage.getHeight(null);
        alienWidth = AlienImage.getWidth(null);
        projectiles = new ArrayList<ProjectilesAliens>();
    }
    
    public static void generateAliens(int numAliens){
    	numberOfAliens = numAliens;
        aliens = new Alien[numAliens];
        double alienX, alienY;
        for(int i = 0; i < numAliens; i++){
            alienX =  (Math.ceil(Math.random() * 500));
            alienY =  (Math.ceil(Math.random() * 500));
            aliens[i] = new Alien(alienX, alienY, Constants.SHIP.getX(), Constants.SHIP.getY());
        }
        scoreX = new int[numAliens];
		scoreY = new int[numAliens];
		scoreTime = new int[numAliens];
    }
    
    public static Alien[] getAliens(){
        return aliens;
    }
    
    public ArrayList<ProjectilesAliens> getShots(){
        return this.projectiles;
    }
    
    public static int getPointsP1(){
    	return pointsPlayer1;
    }
    
    public static int getNumAliens(){
    	return numberOfAliens;
    }
    
    public static ImageIcon getAlienExplosion(){
    	return alienExplosion;
    }
    public double getX(){
    	return xPos;
    }
    public double getY(){
    	return yPos;
    }
    public static void drawAlien(Graphics g){
        for(int i = 0; i < aliens.length; i++){
        	if(aliens[i] != null){
        		g.drawImage(AlienImage, (int)aliens[i].xPos, (int)aliens[i].yPos, null);
        		aliens[i].collisionShip();
        		aliens[i].checkCollisionProjectile();
        		if(collision){
//        			if(count<282){
//                		g.drawImage(getAlienExplosion().getImage(), scoreX[i], scoreY[i], null);
//                		count++;
//                	}
//        			else{
//        				count = 0;
//                		collision = false;
//        			}
                	
        		}
        	}  
        }
        if(drawDelay < 0 && alienCount < aliens.length){
            alienCount++;
            drawDelay = 45*40;
        }else{
            drawDelay--;
        }
        for(int i = 0; i < scoreX.length; i++){
			if(scoreX[i] != 0 || scoreY[i] != 0){ //There is a score
				if(scoreTime[i] > 0){
					g.setFont(new Font("Arial", Font.BOLD, 12));
					g.setColor(Color.RED);
					g.drawString("" + scoreValue, scoreX[i], scoreY[i]);
					g.drawImage(getAlienExplosion().getImage(), scoreX[i]-40, scoreY[i]-40, null);
					scoreTime[i]--;
					scoreY[i] -= 1;
				}
			}
		}
    }
    
    public void find(){
    	double alienDistance = 
    			Math.sqrt(Math.pow((shipX - xPos), 2) + 
    					Math.pow((shipY - yPos), 2));
    	
    	if(alienDistance < 100){
    		changeX(0);
    		changeY(0);
    	}else{
    		int randomValueX = (int) (Math.random() * 10) % 5;
    		int randomValueY = (int) (Math.random() * 10) % 5;
    		int xVel = xPos < shipX ? randomValueX : -randomValueX;
    		int yVel = yPos < shipY ? randomValueY : -randomValueY;
    		changeX(xVel);
    		changeY(yVel);
    	}
//        if((Math.abs(shipX-xPos)) < 32){
//            changeX(0);
//        }else{
//            if(xPos < shipX){
//                changeX(1);
//            }
//            if(xPos > shipX){
//                changeX(-1);
//            }
//        }
//        if((Math.abs(shipY-yPos)) < 32){
//            changeY(0);
//        }else{
//            if(yPos < shipY){
//                changeY(1);
//            }
//            if(yPos > shipY){
//                changeY(-1);
//            }
//        }
    }
    
    public void shoot(){
        if(shoot >= 50 && Constants.SHIP.isAlive()){
        	double deltaX = shipX - xPos;
        	double deltaY = shipY - yPos;
            double theta = Math.atan2(deltaY, deltaX);
            ProjectilesAliens AlienProjectiles = 
            		new ProjectilesAliens(xPos, yPos, theta, xVelocity, yVelocity);
            projectiles.add(AlienProjectiles);
            shoot = 0;
        }else{
            shoot++;
        }
    }
    
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
    
    public void changeX(int dir){
        xVelocity = dir;
    }
    
    public void changeY(int dir){
        yVelocity = dir;
    }
    
    public void move(){
        xPos += xVelocity;
        yPos += yVelocity;
        
        circle = new Ellipse2D.Double(xPos, yPos, alienWidth, alienHeight);
    }
    
    public Ellipse2D getBounds(){
    	return circle;
    }
    
    public boolean collisionShip(){
    	if(Constants.SHIP.getBounds() != null && circle.intersects(Constants.SHIP.getBounds()) && 
				Constants.SHIP.isAlive()){
			Constants.SHIP.setAlive(false);
			return true;
    	}
    	return false;
    }
    
    public void checkCollisionProjectile(){
        for(int i = 0; i < Constants.SHIP.getProjectiles().size(); i++){
    		if(circle.intersects(Constants.SHIP.getProjectiles().get(i).getProjectileBounds())){
    			collision = true;
    			Constants.SHIP.getProjectiles().remove(i);
				for(int j = 0; j < aliens.length; j++){
					if(aliens[j] != null && aliens[j].equals(this)){
						scoreX[j] = (int) this.xPos;
						scoreY[j] = (int) this.yPos;
//						lastX = aliens[i].xPos;
//						lastY = aliens[i].yPos;
						scoreTime[j] = 80;
						aliens[j] = null;
						numberOfAliens--;
						pointsPlayer1 += scoreValue;
					}
				}
            }
    	}
    }
}