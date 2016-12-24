/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */
package dnaandstuff;
import java.awt.Color;
import java.util.ArrayList;

public class Shape {
    String shapeName; //shape name dictates actual vertex and colour
    
    Color colour; 
    Vertice center; 
    Vertice velocity; 
    
    double angle; //angle of the shape
    
    Vertice[] vertex; //array of points
    
    //holds onto individual x and y for polygon
    int[] vertexX;
    int[] vertexY;
    
    //this scalar increases the size of all shapes
    public static int scaler = 5; 
    
    public Shape(String s,double x, double y){ //for shapes that have initial x,y
        shapeName = s;
        vertex = getVertices(s); 
        colour = getColour(s); 
        
        center = new Vertice (x,y); 
        angle = 0; 
        
        velocity = new Vertice (0,0); 
    }
    public Shape(String s){
        shapeName = s;
        vertex = getVertices(s); 
        colour = getColour(s); 
        
        center = new Vertice (300,300); 
        angle = 0; 
        
        velocity = new Vertice (0,0); 
    }
    
    private Vertice[] getVertices(String s){
         
        //uses shape name to get pre-defined x and y points;
     
        
        if (s.equalsIgnoreCase("a")){ //purine

            int[] xs = {-1,0,1,1,0,0,-1}; //cannot use this array notation outside of declaration. 
            int[] ys = {-1,0,-1,2,2,1,1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
            
        }
        else if (s.equalsIgnoreCase("g")){//purine
            int[] xs = {-1,0,1,1,0,0,-1}; 
            //int[] ys = {1,0,1,-2,-2,-1,-1}; 
            int[] ys = {-1,0,-1,2,2,1,1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
            
        }
        else if (s.equalsIgnoreCase("t")){//pyrimidine
            int[] xs = {-1,1,1,0,-1}; 
            int[] ys = {2,2,1,0,1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("u")){//pyrimidine of RNA
            int[] xs = {-1,1,1,0,-1}; 
            int[] ys = {2,2,1,0,1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("c")){//pyrimidine
            int[] xs = {-1,1,1,0,-1}; 
            int[] ys = {2,2,1,0,1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("dnaPolymerase")){
            int[] xs = {0,6,6,2,-2,-6,-4}; 
            int[] ys = {6,4,0,-4,-4,0,4}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("PolymeraseOne")){
            int[] xs = {0,6,6,2,-2,-5,-4,1}; 
            int[] ys = {5,4,0,-4,-4,0,4,-1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("helicase")){
            int[] xs = {0,3,0}; 
            int[] ys = {4,0,-4}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("rnaSynthase")){
            int[] xs = {0,12,13,2,-2,-3,-4}; 
            int[] ys = {4,2,0,-3,-3,0,4}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else if (s.equalsIgnoreCase("HIV")){
            int[] xs = {-2,-1,-1,1,1,2,2,4,2,-2,-4,-2}; 
            int[] ys = {1,1,-1,-1,1,1,-1,-2,-3,-3,-2,-1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            
            return convertArraysToVertex(xs,ys); 
        }
        else{
            //this returns a generic shape, a square 
            int[] xs = {-1,1,1,-1}; 
            int[] ys = {1,1,-1,-1}; 
            
            //multiplies the scalar into x and y
            xs = this.multiplyScalarToArray(xs); 
            ys = this.multiplyScalarToArray(ys); 
            return convertArraysToVertex(xs,ys);
        }
        
        
        
        
    }
    private Color getColour(String s){
        int r = 0; 
        int g = 0;
        int b = 0; 
        //uses the values of the string to set r/g/b
        for (int i = 0; i < s.length();i++){
            int numberValue = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(s.charAt(i)); 
            r = r + numberValue *10; 
            
            int numberValue2 = "MNOCDEPQRSTUVWXYZABFGHIJKL".indexOf(s.charAt(i));
            g = g + numberValue2 *10; 
             
             
            int numberValue3 = "TDEFGHBIJPQRSYZAKLUVWXMNOC".indexOf(s.charAt(i));
            b = b + numberValue3 *10; 
        
        }
        //takes average
        r = r / s.length(); 
        g = g/ s.length();
        b = b/ s.length(); 
      
        Color c = new Color(r,g,b); 
        return c; 
        
    }
    private Vertice[] convertArraysToVertex(int[]xs, int[]ys){
        //takes 2 arrays and forms one array of vertices
        if (xs.length != ys.length){
            System.out.println("ERROR-convertArrayToVerticeList-Size Does Not Match");
            return null; 
        }
        vertexX = xs; 
        vertexY = ys; 
        
        Vertice[] points = new Vertice[xs.length]; 
        
        for (int i = 0; i < xs.length; i++){
            Vertice newV = new Vertice (xs[i],ys[i]); //new vertice point
            points[i] = newV; 
        }
        
        return points; 
        
    }
    private int[] multiplyScalarToArray(int[]array){
        for (int i = 0; i < array.length; i++){
            array[i] = array[i]*scaler; //multiplies scalar to array
        }
        return array; 
    }
    public void setAngle(double a){
        this.angle = a; 
        
        for (int i = 0; i < vertexX.length; i++){
            
            //gets the original distance and angle
            double standardDistance = Math.sqrt(vertexX[i]*vertexX[i] + vertexY[i]*vertexY[i]); 
            double standardAngle = Math.atan2(vertexY[i], vertexX[i]); 
            
            //gets new distance and angle
            double newX = standardDistance * Math.cos(a+standardAngle); 
            double newY = standardDistance * Math.sin(a+standardAngle);
            
           
            
            vertexX[i] = (int) Math.round(newX); 
            vertexY[i] = (int) Math.round(newY); 
            
         
        } 
        
        
    }
    public void update(){
        //when a shape changes (DNA Base change, refines vertex and colour) 
        vertex = getVertices(shapeName); 
        colour = getColour(shapeName); 
    }
    public void checkSelf(){
        //makes sure shape can't leave screen bounds be reversing velocity
        if (center.x < 0 || center.x > 500){
            this.velocity.x = this.velocity.x * -1; 
        }
        if (center.y < 0 || center.y > 500){
            this.velocity.y = this.velocity.y * -1; 
        }
    }
    
}
