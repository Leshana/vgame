///////////////////////////////////////////////////////////////////////////////////////////////////
//
// AudioLibrairie.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;
import java.lang.ref.*;
import java.applet.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public final class AudioLibrairie
{
	private static Hashtable<String,SoftReference<AudioClip>> clipTable = new Hashtable<>();
	
	public static AudioClip getClip( String name )
	{
		SoftReference<AudioClip> tableEntry = clipTable.get( name );
		
		if (tableEntry == null || tableEntry.get() == null)
		{
			AudioClip clip = null;
			
			try
			{
				File file = new File( name );
				clip = JApplet.newAudioClip( file.toURI().toURL() );
			}
			catch (Exception e)
			{
				Util.error( "Couldn't load audio clip " + name, e );
				return null;
			}
			
			clipTable.put( name, new SoftReference<AudioClip>( clip ) );
			
			return clip;
		}
		else
		{
			return tableEntry.get();
		}
	}
	
	public static void playClip( AudioClip clip )
	{
		if (clip != null && FenetreTest.instance.isSoundEnabled())
		{
			clip.play();
		}
	}
	
	/*
    private Vector<AudioClip> tableauSon;
    private Vector<contenuSon> nomSon;
    private int temps;
    public FenetreTest mere;
    private final int MEMOIRE=5;
    private static final int NBSONS=3;
    private static AudioLibrairie pointeur;
    
    // Creates a new instance of AudioLibrairie
    public AudioLibrairie() {
        tableauSon=new Vector<AudioClip>();
        nomSon=new Vector<contenuSon>();
        temps=0;
    }
    
    public static AudioLibrairie getLibrairies(){
        if(pointeur ==null)
            pointeur=new AudioLibrairie();
        return pointeur;
    }
    
    public int estDedans(String nom){
        int position=-1;
        if(nom==null)
            return position;
        for(int i=0;i<nomSon.size();i++){
            contenuSon temp=(contenuSon)nomSon.elementAt(i);
            if(temp.nom.equalsIgnoreCase(nom))
                return i;
        }
        return position;
    }
    
    public void play(int x){
        if(x<0 || !mere.isSoundEnabled())
            return;
        contenuSon temp=(contenuSon)nomSon.elementAt(x);
        ((AudioClip)tableauSon.elementAt(temp.getIndex())).play();
    }
    
    public int loadSon(String chemin){
        if(chemin.trim().equalsIgnoreCase("none"))
            return -5;
        int tVal=estDedans(chemin);
        if(tVal>-1){
            contenuSon temp=(contenuSon)nomSon.elementAt(tVal);
            temp.temps=temps;
            return tVal;
        }
        else{
            int i;
            contenuSon te;
            for(i=0;i<nomSon.size();i++){
                te=(contenuSon)nomSon.elementAt(i);
                if(te.index==-1){
                    break;
                }
            }
            if(i<nomSon.size()){
                contenuSon temp=(contenuSon)nomSon.elementAt(i);
                temp.index=tableauSon.size();
                temp.temps=temps;
                temp.compteur=0;
                temp.nom=chemin;
            }
            else{
                nomSon.addElement(new contenuSon(chemin,temps,tableauSon.size()));
            }
            
            try{
                File fichier=new File(chemin);
                for(int j=0;j<NBSONS;j++){
                    tableauSon.addElement(JApplet.newAudioClip(fichier.toURI().toURL()));
                }
            }
            catch(Exception e)
            {
				VGame.error( e );
            }
            
            return i;
        }
    }
    
    public void enleverSon(int x){
        contenuSon temp =(contenuSon)nomSon.elementAt(x);
        int index=temp.index;
        temp.index=-1;
        temp.nom="";
        if(index>0){
            for(int i=0;i<NBSONS;i++){
                tableauSon.remove(index);
            }
            for(int i=0;i<nomSon.size();i++){
                temp =(contenuSon)nomSon.elementAt(i);
                if(temp.index>index)
                    temp.index-=NBSONS;
            }
        }
    }
    
    public void enleverOld(){
        contenuSon temp;
        for(int i=0;i<nomSon.size();i++){
           temp =(contenuSon)nomSon.elementAt(i);
           if(temp.temps+MEMOIRE<temps){
               enleverSon(i);
               //i--;
           }
        }
    }
    
    public void newLoad(){
        if(temps<Integer.MAX_VALUE)
            temps++;
        else
            temps=0;
        
    }
     private class contenuSon{
        String nom;
        int temps;
        int index;
        int compteur;
        
        public contenuSon(String n, int t, int ind){
            nom=n;
            temps=t;
            index=ind;
            compteur=0;
        }
        public int getIndex(){
            compteur++;
            if(compteur>=NBSONS)
                compteur=0;
            return compteur+index;
        }
    }
    */
}
