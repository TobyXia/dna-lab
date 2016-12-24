package dnaandstuff;

/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */
public class Protein extends Shape {
    String target; 
    int backTrack; //the ability to move backward
    int direct; 
    boolean fixing; //means polymerase is fixing a dna repair
    
    public Protein(String s, double x, double y){ //for basic protiens 
        super(s,x,y); 
        target = "none"; 
        
        fixing = false; 
        backTrack = 0; 
        
    }
    public Protein(String s, double x, double y,String t){ //proteins that target object with name
        super(s,x,y); 
        target = t; 
        
        fixing = false; 
        backTrack = 0; 
    }
    //Below, most used constructor 
    public Protein(String s, double x, double y,int target){ //proteins which target DNA
        super(s,x,y); 
        direct = target; 
        
        fixing = false; 
        backTrack = 0; 
    }
    public DnaBase copyDna(String A, int direct){ //all proteins can copy DNA (but not rna) 
        DnaBase newBase = new DnaBase(A, this.center.x, this.center.y, direct); //returns a base at the location of the protein 
       
        return newBase;  
    }
}
