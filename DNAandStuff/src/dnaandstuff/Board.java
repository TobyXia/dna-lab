/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */
package dnaandstuff;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    Random rand = new Random(); //Random object for random integer generation
    
    boolean dnaReplicate = false; //if animation should be simulating replication
    
    boolean cancerPresent = false; 
    
    //error chance, determined by slider value
    int replicationError; 
    
    public static double speedScale = 1; //is constant, exists for easy change of protein speed within the code
    
    int lengthOfPrimaryStrand = 25; //length of dna strand
  
    
    public static int ScW = 500;
    public static int ScH = 500;
    
    String [] dnaCombo =  {"A", "G", "T", "C"};
    String [] dnaCombo2 = {"T", "C", "A", "G"}; //inverse of dnaCombo
    String [] rnaCombo2 = {"U", "C", "A", "G"}; //Uracil over thymine 
    
    //Arraylists that hold DnaBase pairs and represnt DNA/RNA strands
    ArrayList <DnaBase> dnaLeading; 
    ArrayList <DnaBase> dnaLagging; 
    
    ArrayList <DnaBase> dnaLeadingNEG; 
    ArrayList <DnaBase> dnaLaggingNEG; 
    
    ArrayList <DnaBase> rnaLagging; 
  
    //Replication Proteins
    Protein helicase; 
    Protein polymeraseDNA; 
    ArrayList <Protein> laggingPolymerase; 
   
    //Interfase Proteins 
    Protein rnaPolymerase;
    
    //Pathogens
    Protein hiv; 
    ArrayList <DnaBase> hivDNA;
    
    public Board(){
        this.reset();

    }
    
    public void updateBoard(){
    
        //checks basepairs for bonding and replication
        this.checkBasePairs(); 
        
        //Moves DNA Negative strand pieces accordingly after checkBasePairs(), they are not the same size all the time
        for (int pd = 0; pd < dnaLeadingNEG.size(); pd++){

            dnaLeadingNEG.get(pd).center.x = dnaLeadingNEG.get(pd).center.x + dnaLeadingNEG.get(pd).velocity.x*speedScale; 
            dnaLeadingNEG.get(pd).center.y = dnaLeadingNEG.get(pd).center.y + dnaLeadingNEG.get(pd).velocity.y*speedScale; 

        }
        for (int p = 0; p < dnaLaggingNEG.size(); p++){

            dnaLaggingNEG.get(p).center.x = dnaLaggingNEG.get(p).center.x + dnaLaggingNEG.get(p).velocity.x*speedScale; 
            dnaLaggingNEG.get(p).center.y = dnaLaggingNEG.get(p).center.y + dnaLaggingNEG.get(p).velocity.y*speedScale; 
            
            if (dnaLaggingNEG.get(p).center.y < -100){
                resetMitosis();
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            
        if (dnaReplicate){ //moves polymerase
            dnaReplicationUpdate(); 
            
        }
        else{//regular cell activities
            regularCellUpdate(); 
            
    
        }
        //HIV ACTIVITIES
        pathogenUpdate(); 

          
        


   
    }
    private void checkBasePairs(){
        //cycles through the length of the primary, to get both lagging and leading strand bases
        for (int i = 0; i < lengthOfPrimaryStrand; i++){
            
           
            DnaBase d1 = dnaLeading.get(i); 
            DnaBase d2 = dnaLagging.get(i); 

            //changes DNA positions and status if helicase is present
            if (areTheseTouching(helicase,d1) && d1.paired){
                    d1.paired = false; 
                    d2.paired = false; 
         
                    d2.velocity.y = -0.25;
      
                
            }
            //Allows the lagging strand to travel upward
            if (Math.abs(d2.center.y - (helicase.center.y-100))<0.5 && !d2.paired){
        
                d2.velocity.x = -Math.sqrt(0);
                d2.velocity.y = Math.sqrt(0);

            }
            //replication for the leading strand
            if (!d1.paired && areTheseTouching(d1,polymeraseDNA) ){

                d1.paired = true; 
                
                int numberValue = "AGTC".indexOf(d1.shapeName);
                
                if (polymeraseDNA.backTrack == 0 &&  polymeraseDNA.fixing){
                    polymeraseDNA.fixing = false; 
                    dnaLeadingNEG.remove(dnaLeadingNEG.size()-1); 
                }
                
                
                //determines is there will be a replication error
                int randomNum = rand.nextInt(100)+1;
                if (randomNum > replicationError || i == 0){
                    DnaBase a = polymeraseDNA.copyDna(dnaCombo2[numberValue], -1); //uses Protein to duplicate DNA
                    a.center.x = d1.center.x;
                    a.center.y = d1.center.y; 
                   
                    dnaLeadingNEG.add(a); 
                }
                else{
                    int check = rand.nextInt(4); 
                    while (check == numberValue){ //makes sure the error isn't the correct base
                        check = rand.nextInt(4); 
                    }
                    DnaBase a = polymeraseDNA.copyDna(dnaCombo2[check], -1); //adds error base, however, doesn't set it to paired
                
                    dnaLeadingNEG.add(a); 
                    a.center.x = d1.center.x;
                    a.center.y = d1.center.y; 
                    
                    if (!cancerPresent){ //when cancer is present, no repair is initiated, the error is treated as if it were correct
                        
                        //turns on the DNA polymerase's error correction backtracking
                        d1.paired = false; 
                        polymeraseDNA.backTrack = 50;
                        polymeraseDNA.fixing = true;
                    }
                }
                

            }
            //lagging strand replication 
            for (int pD = 0; pD < 5; pD++){
                if (!d2.paired && areTheseTouching(d2,laggingPolymerase.get(pD)) ){
                    d2.paired = true; 
                    
                    //will always create lagging strand perfectly, as each polymerase only needs to do 1-5 base pairs
                    int numberValue = "AGTC".indexOf(d2.shapeName);
                    DnaBase a = laggingPolymerase.get(pD).copyDna(dnaCombo2[numberValue], 1); 
                    a.center.x = d2.center.x;  //sets positions to the same
                    dnaLaggingNEG.add(a); 

                }
            }
            
            //moving dna base objects  
           
            d1.center.x = d1.center.x+d1.velocity.x*speedScale; 
            d2.center.x = d2.center.x+d2.velocity.x*speedScale; 
            
            
            d1.center.y = d1.center.y+d1.velocity.y*speedScale; 
            d2.center.y = d2.center.y+d2.velocity.y*speedScale;
 
             
        }
    
    }
    private void dnaReplicationUpdate(){
        helicase.center.x = helicase.center.x + 0.5; 
        
        //moves DNA Polymerase toward beginning of leading strand
        if (!dnaLeading.get(0).paired){
            moveShapeToShape(dnaLeading.get(0),polymeraseDNA,200);

        }
        else{
            //moves normally if no error correction is needed
            if (polymeraseDNA.backTrack == 0){
                polymeraseDNA.center.x = polymeraseDNA.center.x + 1;  
            }
            else{ //moving backward for error correction 
                polymeraseDNA.center.x = polymeraseDNA.center.x - 1;
                polymeraseDNA.backTrack--; 
            }

        }
        for (int pD = 0; pD < lengthOfPrimaryStrand/5; pD++){
            if (!dnaLagging.get(pD*5 + 4).paired){ //moves polymerase toward segments of lagging strand
                moveShapeToShape(dnaLagging.get(pD*5 + 4),laggingPolymerase.get(pD),200);

            }
            else{ //will move dna polymerase along the strand
                
                laggingPolymerase.get(pD).center.x = laggingPolymerase.get(pD).center.x - 1;  



            }
        }
        if (dnaLaggingNEG.size() == lengthOfPrimaryStrand && 
                laggingPolymerase.get(laggingPolymerase.size()-1).center.x < - 100 &&
                polymeraseDNA.center.x > ScW + 100){ //checks if replication is complete

            ArrayList <DnaBase> dnaLaggingCopy =  new ArrayList(); //temporary arrayList to hold onto each strand for a swap of values
            for (int k = 0; k < lengthOfPrimaryStrand; k++){
                DnaBase l = new DnaBase(dnaLagging.get(k).shapeName,dnaLagging.get(k).center.x,dnaLagging.get(k).center.y,-1); 
                dnaLaggingCopy.add(l); 
            }
            ///
            ArrayList <DnaBase> dnaLeadingNEGCopy =  new ArrayList(); //temporary arrayList to hold onto each strand for a swap of values
            for (int k = 0; k < lengthOfPrimaryStrand; k++){
                DnaBase l = new DnaBase(dnaLeadingNEG.get(k).shapeName,dnaLeadingNEG.get(k).center.x,dnaLeadingNEG.get(k).center.y,-1); 
                dnaLeadingNEGCopy.add(l); 
            }
            //calls method to prevents java referencing the copy to the original, this I found was the most effecient method
            this.setFirstToSecond(dnaLeadingNEG, dnaLaggingCopy);
            this.setFirstToSecond(dnaLagging, dnaLeadingNEGCopy); 


            if (dnaLeadingNEG.get(0).velocity.y != -2){ //sets the new copied DNA's velocity to -2 to remove it from the screen, towards new "cell" in mitosis 
                
                for (int negStrands = 0; negStrands < dnaLaggingNEG.size(); negStrands ++ ){

                    dnaLeadingNEG.get(negStrands).velocity.y = -2;
                    dnaLaggingNEG.get(negStrands).velocity.y = -2;


                }
            }
            //after replication, dnaReplicate is off, and cancer, if on, is set to off
            dnaReplicate = false; 
            cancerPresent = false; 

        }
        //Stuff that happens outside of replication but still should only happen during replication 
        
        //this moves rnaPolymerase and rna strand around bouncing around the screen, meaning it has nowhere to go during mitosis
        rnaPolymerase.center.x = rnaPolymerase.center.x + rnaPolymerase.velocity.x * this.speedScale;  
        rnaPolymerase.center.y = rnaPolymerase.center.y + rnaPolymerase.velocity.y * this.speedScale;
        rnaPolymerase.checkSelf(); //checks boundaries

        if (!rnaLagging.isEmpty()){ 
                for (int i = 0; i < 6; i++){
                    rnaLagging.get(i).center.y = rnaLagging.get(i).center.y + rnaLagging.get(i).velocity.y * this.speedScale;
                    rnaLagging.get(i).center.x = rnaLagging.get(i).center.x + rnaLagging.get(i).velocity.x * this.speedScale;

                    rnaLagging.get(i).checkSelf(); //checks boundaries
                }
        }
            
            
                

    }
    private void regularCellUpdate(){
        if (rnaLagging.isEmpty()){
            
            //moves rnaPolymerase toward its target location on the dna strand
            moveShapeToShape(dnaLeading.get(rnaPolymerase.direct), rnaPolymerase, 100);

            double slopeX = ( dnaLeading.get(rnaPolymerase.direct).center.x - rnaPolymerase.center.x);  
            double slopeY = ( dnaLeading.get(rnaPolymerase.direct).center.y - rnaPolymerase.center.y); 
            
            //when the enzyme gets close enough, it opens the lagging strand from the leading strand
            if ((Math.abs(slopeX) + Math.abs(slopeY))<10 && 
                    dnaLagging.get(rnaPolymerase.direct).center.y == dnaLeading.get(rnaPolymerase.direct).center.y){
                for (int i = 0; i < 6; i++){
                    dnaLagging.get(rnaPolymerase.direct+i).center.y = dnaLagging.get(rnaPolymerase.direct+i).center.y - 10; 
                }
            }
            //checks if rnaPolymerase has reached DNA
            if (areTheseTouching(dnaLeading.get(rnaPolymerase.direct), rnaPolymerase)){
                System.out.println("ds");
                for (int i = 0; i < 6; i++){
                    //creates a 6 long strand of RNA
                    int numberValue = "AGTC".indexOf(dnaLeading.get(rnaPolymerase.direct+i).shapeName);
                    //closes lagging strand
                    dnaLagging.get(rnaPolymerase.direct+i).center.y = dnaLeading.get(rnaPolymerase.direct+i).center.y; 


                    //Creates the RNA strand
                    DnaBase a = rnaPolymerase.copyDna(rnaCombo2[numberValue], -1);
                    a.center.y = rnaPolymerase.center.y; 
                    a.center.x = dnaLeading.get(rnaPolymerase.direct+i).center.x;
                    
                    //sets the velocity of the rna, first element
                    if (i == 0){
                        a.velocity.x = rand.nextInt(2) - 1; 
                        a.velocity.y = rand.nextInt(2) - 1; 
                        while (a.velocity.y == 0){ //prevents no movement
                            a.velocity.y = rand.nextInt(2) - 1; 
                        }
                        while (a.velocity.x == 0){
                            a.velocity.x = rand.nextInt(2) - 1; 
                        }

                    }
                    else{//rest of the rna gets same velocity as first one
                        a.velocity = rnaLagging.get(0).velocity; 
                    }

                    rnaLagging.add(a);
                    
                    //sets the velocity of the rnaPolymerase off the rna strand
                    rnaPolymerase.velocity = rnaLagging.get(0).velocity; 
                    rnaPolymerase.velocity = rnaLagging.get(0).velocity; 

                }
            }
        }
        else{
            //moves rnaPolymerase after it has created RNA
            rnaPolymerase.center.x = rnaPolymerase.center.x + rnaPolymerase.velocity.x * this.speedScale;  
            rnaPolymerase.center.y = rnaPolymerase.center.y + rnaPolymerase.velocity.y * this.speedScale; 

            //moves RNA strand after it is created
            for (int i = 0; i < 6; i++){

                rnaLagging.get(i).center.y = rnaLagging.get(i).center.y + rnaLagging.get(i).velocity.y * this.speedScale;
                rnaLagging.get(i).center.x = rnaLagging.get(i).center.x + rnaLagging.get(i).velocity.x * this.speedScale;
                
                //checks if RNA has left the screen
                if (rnaLagging.get(i).center.y > 520 || rnaLagging.get(i).center.y < -20
                        ||rnaLagging.get(i).center.x > 560 || rnaLagging.get(i).center.x < -60){
                    resetRNA(); 
                    break; //stops checking the rest of the rna strand if one is found outside
                }
            }

        }
    } 
    private void pathogenUpdate(){ 
        if (!hivDNA.isEmpty()){ //when there is active HIV
            //HIV moves toward it's targetted DNA location 
            moveShapeToShape(dnaLeading.get(hiv.direct), hiv, 100);
         
            
            //moving HIV Bases
            hivDNA.get(0).center.x = hiv.center.x-10;
            hivDNA.get(1).center.x = hiv.center.x;
            hivDNA.get(2).center.x = hiv.center.x+10;
            for (int i = -1; i < 2; i ++){
                 hivDNA.get(i+1).center.y = hiv.center.y;
            }
            //checks if HIV has reached DNA location target
            if (areTheseTouching(hiv,dnaLeading.get(hiv.direct))){
                //sets the dnastrand base at the location to the HIV "CCC" strand, this is arbitrary  
                for (int i = -1; i < 2; i ++){
                    DnaBase d = dnaLeading.get(hiv.direct+i);
                    DnaBase h = hivDNA.get(i+1); 
                    
                    d.shapeName = h.shapeName; 
                    d.update(); 
                    
                    
                }
                hiv.velocity.y = -1; //sets the velocity so it moves out the screen 
                hivDNA.clear(); 
                this.dnaReplicate = true; //HIV will then stimulate the cell to replicate with the broken gene
            }
            
          
        }
        else{
            //when HIV isn't active, it moves out of the screen 
            hiv.center.y = hiv.center.y + hiv.velocity.y * this.speedScale; 
        }
    }
    
    public Image drawYourself(){
        //is called by Animation Class, creates a buffered image to return 
        BufferedImage bufferedImage = new BufferedImage(ScW, ScH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics(); //graphics object 
        
        //draws dnaPolymerase
        g.setColor(polymeraseDNA.colour);
        g.fillPolygon(addXandYCord(polymeraseDNA,"x"), addXandYCord(polymeraseDNA,"y"), polymeraseDNA.vertexX.length);
        for (int pD = 0; pD < 5; pD++){
            g.setColor(laggingPolymerase.get(pD).colour);
            g.fillPolygon(addXandYCord(laggingPolymerase.get(pD),"x"), addXandYCord(laggingPolymerase.get(pD),"y"), laggingPolymerase.get(pD).vertexX.length);
        }
        
        //draws rnaPolymerase
        g.setColor(rnaPolymerase.colour); 
        g.fillPolygon(addXandYCord(rnaPolymerase,"x"), addXandYCord(rnaPolymerase,"y"), rnaPolymerase.vertexX.length);
        
        
        //draws the lagging and leading strand of dna 
        for (int i = 0; i < lengthOfPrimaryStrand; i++){

            DnaBase d1 = dnaLeading.get(i); 
            DnaBase d2 = dnaLagging.get(i); 
           
            g.setColor(d1.colour);
            g.fillPolygon(addXandYCord(d1,"x"), addXandYCord(d1,"y"), d1.vertexX.length);


            g.setColor(d2.colour);
            g.fillPolygon(addXandYCord(d2,"x"), addXandYCord(d2,"y"), d2.vertexX.length);

            

        }
        //draws the DNA copy strands, needs two seperate loops because they are created at different rates and may not be same length
        for (int p = 0; p < dnaLeadingNEG.size(); p++){
                g.setColor(dnaLeadingNEG.get(p).colour); 
                g.fillPolygon(addXandYCord(dnaLeadingNEG.get(p),"x"), addXandYCord(dnaLeadingNEG.get(p),"y"), dnaLeadingNEG.get(p).vertexX.length);
        }
        for (int p2 = 0; p2 < dnaLaggingNEG.size(); p2++){
                g.setColor(dnaLaggingNEG.get(p2).colour); 
                g.fillPolygon(addXandYCord(dnaLaggingNEG.get(p2),"x"), addXandYCord(dnaLaggingNEG.get(p2),"y"), dnaLaggingNEG.get(p2).vertexX.length);


        }
        //draws  helicase
        
        g.setColor(helicase.colour); 
        g.fillPolygon(addXandYCord(helicase,"x"), addXandYCord(helicase,"y"), helicase.vertexX.length);
        
        //Draws rna strand
        if (!rnaLagging.isEmpty()){
            for (int i = 0; i < 6; i++){
                g.setColor(rnaLagging.get(i).colour); 
                g.fillPolygon(addXandYCord(rnaLagging.get(i),"x"), addXandYCord(rnaLagging.get(i),"y"), rnaLagging.get(i).vertexX.length);
            }
        }
        
        //Draws Pathogens
        g.setColor(hiv.colour); 
        g.fillPolygon(addXandYCord(hiv,"x"), addXandYCord(hiv,"y"), hiv.vertexX.length);
        if (!hivDNA.isEmpty()){
            
            for (int i = 0; i < 3; i++){
                g.setColor(hivDNA.get(i).colour); 
                g.fillPolygon(addXandYCord(hivDNA.get(i),"x"), addXandYCord(hivDNA.get(i),"y"), hivDNA.get(i).vertexX.length);
            }
        }

      
        updateBoard(); //updates the animation, objects move here, and change value for the next frame 
        return bufferedImage; //returns the image 
        
    }
    //using the vertices of a shape object, adds on the center location 
    private int[] addXandYCord(Shape s,String v){
        int[] tempArray = new int[s.vertexX.length]; 
        
        for (int i = 0; i < s.vertexX.length; i++){
            if (v.equalsIgnoreCase("x")){
                tempArray[i] = s.vertexX[i] + (int) s.center.x; 
            } 
            else{
                tempArray[i] = s.vertexY[i] + (int) s.center.y;
            }
        }
        return tempArray; //returns the new x or y value array with the addition of the center value
    
        
    }
    private boolean areTheseTouching(Shape a, Shape b){
        if (Math.sqrt(Math.pow(a.center.x-b.center.x,2) + Math.pow(a.center.y - b.center.y,2))<0.5){ //uses length formula
            return true; //true if less than 0.5
        }
        else{
            return false; //false if greater
        }
        
        
    }
    private void moveShapeToShape(Shape a, Shape b,int factor){
        
        //the closer the object, the slower it moves to location, this is intended to simulate allignment of molecules
        double slopeX = ( a.center.x - b.center.x)/factor;  //factor is the speed division amount
        double slopeY = ( a.center.y - b.center.y)/factor; 
        
        //moves b toward a
        b.center.x = b.center.x + slopeX;  
        b.center.y = b.center.y + slopeY;  
    }
    private void resetRNA(){
        //clears the rna strand
        rnaLagging.clear();
        
        //generates random y value, for above or below
        int y = 0; 
        if (rand.nextInt(2) == 0){
            y = 500+rand.nextInt(100); 
        }
        else{
            y = -rand.nextInt(100); 
            //rnaPolymerase.setAngle(Math.PI);
        }
        rnaPolymerase.center = new Vertice (rand.nextInt(500),y);
        rnaPolymerase.direct = rand.nextInt(18);
        
        //generates new velocity vector, making sure no components are 0, that makes it look more interesting
        rnaPolymerase.velocity = new Vertice (rand.nextInt(2) - 1,rand.nextInt(2) - 1);
        while (rnaPolymerase.velocity.y == 0){ //prevents no movement
            rnaPolymerase.velocity.y = rand.nextInt(2) - 1; 
        }
        while (rnaPolymerase.velocity.x == 0){
            rnaPolymerase.velocity.x = rand.nextInt(2) - 1; 
        }
        
    }
    
    private void resetMitosis(){
        //resets the copy strands (NEG)
        dnaLeadingNEG.clear();
        dnaLaggingNEG.clear();
        
        //resets helicase to be on left of screen 
        helicase.center.x = -50; 
        
        
        //re-places DNA Polymerase III
        polymeraseDNA.center.x = -250; 
        polymeraseDNA.center.y = -250;
        
        
        for (int pD = 0; pD < 5; pD++){ //moves all the DNA Polymerase I to original positions
             
            laggingPolymerase.get(pD).center.x = 350;
            laggingPolymerase.get(pD).center.y = -250;
        }
    }
    private void setFirstToSecond(ArrayList<DnaBase> a,ArrayList<DnaBase> b){
        //this prevents arrays from referencing each other, so this method's job it copy indivudal values over
        if (a.size() != b.size()){
            System.out.println("ERROR a does not equal b"); //error catch (Shouldn't happen) 
        }
        else{
            for (int i = 0; i < a.size(); i++ ){
                //DNA bases from a, get b's values 
                
                DnaBase aValue = a.get(i);
                DnaBase bValue = b.get(i); 
                
                aValue.shapeName = bValue.shapeName;
                aValue.velocity.x = 0;
                aValue.velocity.y = 0;
                
                aValue.update(); 
                aValue.angle = bValue.angle; 
                aValue.setAngle(aValue.angle);
                
                aValue.center.x = bValue.center.x;
                aValue.center.y = bValue.center.y; 
            }
            
        }
    }

    public void turnOnReplication(){
        //turns on replication, called from MainUI 
       if (!dnaReplicate){
            this.dnaReplicate = true; 
       }
    }
    public void addPathogenNamed(String p){
        //adds HIV into board
        if (p.equalsIgnoreCase("HIV") && hivDNA.isEmpty()){
            
            int triesLeft = 10;
            int proposedTarget = rand.nextInt(23)+1; //This is so HIV targets an area that the user can see a difference in
            while (dnaLeading.get(proposedTarget).shapeName.equalsIgnoreCase("C") && triesLeft > 0){
                triesLeft--;
                proposedTarget = rand.nextInt(23)+1;
            }
            hiv = new Protein("HIV",100,100,proposedTarget);
         
            //creates HIV's bases, no loop used to change hiv codons in the future, right now they are all cytosine
            DnaBase hiv1 = new DnaBase("C",hiv.center.x-10,hiv.center.y,1);  
            DnaBase hiv2 = new DnaBase("C",hiv.center.x,hiv.center.y,1); 
            DnaBase hiv3 = new DnaBase("C",hiv.center.x+10,hiv.center.y,1); 
            hivDNA.add(hiv1); hivDNA.add(hiv2); hivDNA.add(hiv3); //Adds the HIV base pairs to the arrayList
        }
        else{ //this is for cancer
            //cancer simply turns off DNAPolymeres 3's backtracking
            cancerPresent = true; 
  
            dnaReplicate = true; //replicates
        }
        
    }
    public void reset(){ //
        //sets initial parameters
     
        replicationError = 0;
        dnaReplicate = false; 
        cancerPresent = false; 
        
        dnaLeading = new ArrayList(); 
        dnaLagging = new ArrayList(); 
        dnaLeadingNEG = new ArrayList(); 
        dnaLaggingNEG = new ArrayList(); 
        
        rnaLagging = new ArrayList(); 
        
        hivDNA = new ArrayList(); 
        
       
        //generating the initialDNAstrands
        int stringX = 125; //inital x value
        
        for (int i = 0; i < lengthOfPrimaryStrand; i ++){
            int select = rand.nextInt(dnaCombo.length);
            
            DnaBase x; 
            DnaBase y;
            //creates Dna Lagging and Leading strands

            x = new DnaBase(dnaCombo[select],stringX,250,1); 
            y = new DnaBase(dnaCombo2[select],stringX,250,-1);
            
            dnaLeading.add(x); 
            dnaLagging.add(y); 
            
            stringX = stringX + 10; //offsets x
        }
        
        //creates proteins
        helicase = new Protein("HELICASE",-50,250); 
        polymeraseDNA = new Protein("DNAPOLYMERASE", -250,-250,"leading"); 
        
        laggingPolymerase = new ArrayList(); 
        for (int pD = 0; pD < 5; pD++){
             Protein negativeLagging = new Protein("POLYMERASEONE", 350,-250,"lagging"); 
             laggingPolymerase.add(negativeLagging);
        }
        
        rnaPolymerase = new Protein("RNASYNTHASE",-100,700,rand.nextInt(15)); 
        //resets RNA for first transcription, this is because the program starts with rna/protein transcription
        resetRNA(); 
        
        //creates Pathogens
        hiv = new Protein("HIV",-100,-100,rand.nextInt(22));
    }
    
}
