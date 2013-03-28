package astr_pkg;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Alien implements Runnable {
    
    private static Image AlienImage;
    double xCoor, yCoor, AIx, AIy;
    int xDIR, yDIR;
    private static Alien[] aliens;
    private static int drawDelay = 25;
    private static int alienCount = 1;
    private ArrayList<Projectiles> projectiles;
    double shoot;
    private Ellipse2D.Double circle;
    private double height;
    private double width;
    
    public Alien(double A, double B, double shipXPos, double shipYPos){
        AIx = A;
        AIy = B;
        xCoor = shipXPos;
        yCoor = shipYPos;
        AlienImage = new ImageIcon("src/astr_pkg/Alien.png").getImage();
        height = AlienImage.getHeight(null);
        width = AlienImage.getWidth(null);
        projectiles = new ArrayList<Projectiles>();
    }
    
    public static void generateAliens(int count){
        aliens = new Alien[count];
        double alienX, alienY;
        for(int i = 0; i < count; i++){
            alienX =  (Math.ceil(Math.random() * 500));
            alienY =  (Math.ceil(Math.random() * 500));
            aliens[i] = new Alien(alienX, alienY, Constants.SHIP.getX(), Constants.SHIP.getY());
        }
    }
    
    public static Alien[] getAliens(){
        return aliens;
    }
    
    public ArrayList<Projectiles> getShots(){
        return this.projectiles;
    }
    
    public static void drawAlien(Graphics g){
        for(int i = 0; i < alienCount; i++){
            g.drawImage(AlienImage, (int)aliens[i].AIx, (int)aliens[i].AIy, null);
        }
        if(drawDelay < 0 && alienCount < aliens.length){
            alienCount++;
            drawDelay = 125;
        }else{
            drawDelay--;
        }
        Graphics2D g2D = (Graphics2D) g;
//        for(int i = 0; i < aliens.length; i++){
//            if(aliens[i].collisionProjectile() || aliens[i].collisionShip()){
//                g.setColor(Color.red);
//                g2D.draw(aliens[i].circle);
//            }
//        }
    }
    
    public void find(){
        
        /*if((Math.abs(xCoor-AIx)) < 0.5){
         int random = (int) (Math.random() * 8);
         if(random <= 2){
         changeX(25);
         changeY(25);
         }else if(random > 2 && random <= 4){
         changeX(25);
         changeY(575);
         }else if(random > 4 && random <= 6){
         changeX(775);
         changeY(25);
         }else{
         changeX(775);
         changeY(575);
         }
         }else{*/
        if((Math.abs(xCoor-AIx)) < 32){
            changeX(0);
        }else{
            if(AIx < xCoor){
                changeX(1);
            }
            if(AIx > xCoor){
                changeX(-1);
            }
        }
        /*if((Math.abs(yCoor-AIy)) < 0.5){
         System.out.println(yCoor-AIy);
         int random = (int) (Math.random() * 8);
         if(random <= 2){
         changeX(25);
         changeY(25);
         }else if(random > 2 && random <= 4){
         changeX(25);
         changeY(575);
         }else if(random > 4 && random <= 6){
         changeX(775);
         changeY(25);
         }else{
         changeX(775);
         changeY(575);
         }
         }else{*/
        if((Math.abs(yCoor-AIy)) < 32){
            changeY(0);
        }else{
            if(AIy < yCoor){
                changeY(1);
            }
            if(AIy > yCoor){
                changeY(-1);
            }
        }
    }
    
    public void shoot(){
        if(shoot >= 50){
            double theta = 120; //need function
            Projectiles AlienProjectiles = new Projectiles(AIx, AIy, theta);
            projectiles.add(AlienProjectiles);
            shoot = 0;
        }else{
            shoot++;
        }
    }
    
    public void detectEdges(){
        if(AIx <= 16){
            changeX(1);
        }
        if(AIx >= 784){
            changeX(-1);
        }
        if(AIy <= 16){
            changeY(1);
        }
        if(AIy >= 584){
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
        AIx += xDIR;
        AIy += yDIR;
        
        circle = new Ellipse2D.Double(AIx, AIy, width, height);
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
                shoot();
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