/*
 * PanneauProjectile.java
 *
 * Created on 3 février 2006, 16:48
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Krishna3ca
 */
public class PanneauProjectile extends JPanel{
    public FenetreTest mere;
    Projectile source;
    public int modX;
    
    /** Creates a new instance of PanneauProjectile */
    public PanneauProjectile(Projectile source, FenetreTest m) {
        mere=m;
        this.source=source;
        if(source.modeAffichage==1){
            modX=(source.taille1X-Map.TILE_WIDTH)/2;
            setBounds((source.getX()-1)*Map.TILE_WIDTH+mere.getBaseX()-modX,(source.getY()-1)*Map.TILE_HEIGHT+mere.getBaseY()-7-source.taille1Y-source.base1Y   , source.taille1X,source.taille1Y);
        }
        else{
            modX=(source.taille2X-Map.TILE_WIDTH)/2;
            setBounds((source.getX()-1)*Map.TILE_WIDTH+mere.getBaseX()-modX,(source.getY()-1)*Map.TILE_HEIGHT+mere.getBaseY()-7-source.taille2Y-source.base2Y   , source.taille2X,source.taille2Y);
        }
        setBackground(new Color(0,0,0,0));
        setVisible(true);
    }
    public void changerPosition(){
        if(source.modeAffichage==1){
            setBounds((source.getX()-1)*Map.TILE_WIDTH+mere.getBaseX()+source.varX-modX,(source.getY()-1)*Map.TILE_HEIGHT+mere.getBaseY()-7+source.varY-source.taille1Y-source.base1Y   , source.taille1X,source.taille1Y);
            //setBounds(10,10, source.taille1X,source.taille1Y);
            
        }
        else
            setBounds((source.getX()-1)*Map.TILE_WIDTH+mere.getBaseX()+source.varX-modX,(source.getY()-1)*Map.TILE_HEIGHT+mere.getBaseY()-7+source.varY-source.taille2Y -source.base2Y  , source.taille2X,source.taille2Y);
        
    }
    public void paint(Graphics g){
        super.paint(g);
        changerPosition();
        switch(source.modeAffichage){
            case 1:
                g.drawImage(source.tableauImage1[source.angle][source.frame],0,0,this);
                
                break;
            case 2:
                g.drawImage(source.tableauImage2[source.frame],0,0,this);
                break;
        }
        
    }
    
}
