package astr_pkg;

import java.awt.*;
import javax.swing.ImageIcon;

public class Alien implements Runnable {
    
    int xDIR, yDIR;
    Image AlienImage;
    Rectangle AI, Ship;
    double xCoor, yCoor;
    
    public Alien(Rectangle A, Double B, Double C){
        xCoor = B;
        yCoor = C;
        AI = A;
        AlienImage = new ImageIcon("src/astr_pkg/Alien.png").getImage();
    }
    
    public void drawAlien(Graphics g){
        g.drawImage(AlienImage, AI.x, AI.y, null);
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