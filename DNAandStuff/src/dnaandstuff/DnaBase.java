
package dnaandstuff;
/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */
public class DnaBase extends Shape{
    
    boolean paired; 
    int pairedType;
    
    public DnaBase(String s, double x, double y,int strandType){
        super(s,x,y); 
        pairedType = strandType; //if it is leading or lagging 1 = lead / -1 = lag
        paired = true; //if the base is connected to another (paired)
        
        if (pairedType == -1){
            this.setAngle(Math.PI); //flips the nucleotide
        }
        
        
    }
}
