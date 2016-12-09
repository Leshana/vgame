/*
 * Projectile.java
 *
 * Created on 31 janvier 2006, 21:51
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

import java.io.*;
import java.awt.*;
 
class ProjectileData implements Serializable
{
	public int ProjectileID;
	
	public String Image1;
	public int Delai1;
	public int NombreImage1;
	public String Image2;
	public int Delai2;
	public int NombreImage2;
	public int Taille1X;
	public int Taille1Y;
	public int Taille2X;
	public int Taille2Y;
	public int Deplacement;
}

public class Projectile
{
    public int projectileID;
    public String image1;
    public int delai1;
    public int nombreImage1;
    public String image2;
    public int delai2;
    public int nombreImage2;
    public int taille1X;
    public int taille1Y;
    public int taille2X;
    public int taille2Y;
	private int positionX;
    private int positionY;
    public int varX=0;
    public int varY=0;
    public int cibleX;
    public int cibleY;
    public int frame=0;
    public int modeAffichage=1;
    public int angle=0;
    public Image tableauImage1[][];
    public Image tableauImage2[];
    public PanneauProjectile panneau;
    public int deplacement;
    public int base1Y;
    public int base2Y;
	
	public int getX() { return positionX; }
	public int getY() { return positionY; }
	public void setPosition( int x, int y ) { positionX = x; positionY = y; }
    
    /** Creates a new instance of Projectile */
    public Projectile(int projectileID, int positionX, int positionY, int cibleX, int cibleY, int base1Y, int base2Y) {
        this.projectileID=projectileID;
        this.positionX=positionX;
        this.positionY=positionY;
        this.cibleX=cibleX;
        this.cibleY=cibleY;
        this.base1Y=base1Y;
        this.base2Y=base2Y;
        
		ProjectileData data = DataUtil.getProjectileData( projectileID );
		
		if (data != null)
		{
			image1 = data.Image1;
			delai1 = data.Delai1;
			nombreImage1 = data.NombreImage1;
			image2 = data.Image2;
			delai2 = data.Delai2;
			nombreImage2 = data.NombreImage2;
			taille1X = data.Taille1X;
			taille1Y = data.Taille1Y;
			taille2X = data.Taille2X;
			taille2Y = data.Taille2Y;
			deplacement = data.Deplacement;
		}

        tableauImage1=new Image[8][nombreImage1];
        tableauImage2=new Image[nombreImage2];
         
        int ind;
        String chaine,fin;
        
        ind=image1.indexOf("001");
        fin=image1.substring(ind+3,image1.length());
        chaine=image1.substring(0,ind);
        int c1=0;
        int c2=0;
        int c3;
        for(int i=0;i<8;i++){
            c3=1;
            c2=0;
            for(int j=0;j<nombreImage1;j++){
                tableauImage1[i][j]=ImageLibrairies.getImage(chaine+i+c2+c3+fin);
                
                c3++;
                if(c3>9){
                    c3=0;
                    c2++;
                }
                if(c2>9){
                    c2=0;
                }
            }
        }
        ind=image2.indexOf("001");
        fin=image2.substring(ind+3,image2.length());
        chaine=image2.substring(0,ind);
        c3=1;
        c2=0;
        for(int j=0;j<nombreImage2;j++){
            tableauImage2[j]=ImageLibrairies.getImage(chaine+c1+c2+c3+fin);
            c3++;
            if(c3>9){
                c3=0;
                c2++;
            }
            if(c2>9){
                c2=0;
                c1++;
            }
        }
        int difX=positionX-cibleX;
        int difY=positionY-cibleY;
        if(difX==0 && difY>0)
            angle=2;
        else if(difX==0 && difY<0)
            angle=6;
        else if(difX>0 && difY>0)
            angle=1;
        else if(difX>0 && difY<0)
            angle=7;
        else if(difX<0 && difY>0)
            angle=3;
        else if(difX<0 && difY<0)
            angle=5;
        else if(difX<0 && difY==0)
            angle=4;
    }
    
}
