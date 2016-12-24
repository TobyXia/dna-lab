package dnaandstuff;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.util.ArrayList;

/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */

public class Animation implements Runnable{ //Animation Thread
    public static int ScW = 2000; 
    public static int ScH = 2000; 
    
    public static int viewSize = 500; //jPanel view size is 500x500
    
    Board b; 
    JPanel drawingPanel;
    Boolean paused; 
    BufferedImage globalOutput; 
    int sleepTime; 
    
    public Animation(JPanel jp, Board B){
        this.drawingPanel = jp; 
        this.b = B; 
        sleepTime = 0; 
        paused = false; 
    
    }
    
    
    public void paintImage() {
        Graphics f1 = drawingPanel.getGraphics();
        Image img = b.drawYourself(); //calls the one board object to return an image of itseft
        f1.drawImage(img, 0, 0, drawingPanel);
        globalOutput = (BufferedImage) img; 
    }
    public static void sleep(int duration) {
            try {
                Thread.sleep(duration);
            } 
            catch (Exception e) {}
    }
    
    
    @Override
    public void run() {
        while (true){
            if (!paused){ //if program is paused, it doesn't call the paint, which inturn doesn't update
                this.paintImage(); 
               
            }
            sleep(5 + sleepTime); //pH affect the sleep time 
     
            
        }
    }

  
   
  
}
