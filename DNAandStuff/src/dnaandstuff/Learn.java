package dnaandstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;



/*
 * Toby Xia
 * Mr. Schattman
 * Final Project
 * 2016-01-19
 */
public class Learn {
    BufferedImage display;
    String info; 
    
    JPanel drawingPanel;
    public Learn (JPanel jp){
        this.drawingPanel = jp; 
    }
     public void paintImage(Color c) {
        Graphics f1 = drawingPanel.getGraphics();
        Image img = getImage(c);
        f1.drawImage(img, 0, 0, drawingPanel);
    }
     
    public Image getImage(Color c) {
        
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        
       //sets the rectangle to the colour of whatever the user is clicking on
        g.setColor(c);
        g.fillRect(0, 0, 100, 100);

        
        return bufferedImage; //returns the image 
        
    }
    
    public String[] getInfo(Color co, Board b) throws IOException {
 
        paintImage(co); //calls for the colour to be drawn in the colour box
        String name; 
        String text; 
        
        //board b doesnt know where specific dna bases are, so I declare them here for colour testing
        DnaBase a = new DnaBase("A",0,0,1); 
        DnaBase g = new DnaBase("G",0,0,1); 
        DnaBase t = new DnaBase("T",0,0,1); 
        DnaBase c = new DnaBase("C",0,0,1); 
        DnaBase u = new DnaBase("U",0,0,1);
        
        //compares colours to board b's objects
        
        if (co.getRGB() == b.helicase.colour.getRGB()){
            name = "DNA Helicase"; 
            text = "DNA Helicase splits the leading and lagging strand of DNA"; 
        }
        else if (co.getRGB() == b.polymeraseDNA.colour.getRGB()){
            name = "DNA Polymerase III"; 
            text = "DNA Polymerase III places bounding nucleotide base pairs to a split DNA leading-strand"; 
        }
        else if (co.getRGB() == b.rnaPolymerase.colour.getRGB()){
            name = "RNA Polymerase"; 
            text = "Creates an opposing RNA strand with Uracil instead of Thymine. Travels to Ribosomes with RNA for protein synthesis. Has nowhere to go during Mitosis"; 
        }
        else if (co.getRGB() == b.laggingPolymerase.get(0).colour.getRGB()){
            name = "DNA Polymerase I"; 
            text = "Matches nucleotides to the lagging-strand of DNA, requires multiple to fixate one strand. Due to their nature, they generally make no errors"; 
        }
        else if (co.getRGB() == b.hiv.colour.getRGB()){
            name = "HIV (Human Immuno Virus) Integrase (Retroviral Integrase Enzyme)"; 
            text = "Replaces nucleotides on a DNA strand with its own, with the intention to make HIV proteins with cellular mechanisms. Induces cell components to copy and fixate the DNA"; 
        }
        else if (co.getRGB() == a.colour.getRGB()){
            name = "Adenine"; 
            text = "Purine - Nucleotide, bonds with Thymine"; 
        }
        else if (co.getRGB() == g.colour.getRGB()){
            name = "Guanine"; 
            text = "Purine - Nucleotide, bonds with Cytosine"; 
        }
        else if (co.getRGB() == t.colour.getRGB()){
            name = "Thymine"; 
            text = "Pyrimadine - Nucleotide, bonds with Adenine"; 
        }
        else if (co.getRGB() == c.colour.getRGB()){
            name = "Cytosine"; 
            text = "Pyrimadine - Nucleotide, bonds with Guanine"; 
        }
        else if (co.getRGB() == u.colour.getRGB()){
            name = "Uracil"; 
            text = "Only found in RNA, replaces Thymine"; 
        }
        else{
            
            name = "Cytosol"; 
            text = "Fluid of the cell surrounding the nucleus"; 

        }
        //packages into array to return to MainUI
        String[] data = {name,text}; 
        return data; 

    }
    
    
}
