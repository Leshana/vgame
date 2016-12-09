/*
 * SoundEffect.java
 *
 * Created on 29 janvier 2006, 11:50
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
import java.net.URL;
import java.io.File;
import javax.swing.*;
import java.applet.AudioClip;
/**
 *
 * @author Krishna3ca
 */
public class SoundEffect {
    public String son;
    public AudioClip sound;
    /** Creates a new instance of SoundEffect */
    public SoundEffect(String son) {
        this.son=son;
        File temp=new File(son);
           temp=temp.getAbsoluteFile();
           
           try{
                sound=JApplet.newAudioClip(temp.toURI().toURL());   
           }
           catch(Exception e)
           {
				Util.error( e );
           }
    }
    
    public static void playSound(String son){
        if(son!=null && son!="none" && son!=""){
           
           AudioClip sound; 
           File temp=new File(son);
           temp=temp.getAbsoluteFile();
           try{
                sound=JApplet.newAudioClip(temp.toURI().toURL());
                sound.play();
           }
           catch(Exception e)
           {
				Util.error( e );
           }
        }
    }
    public void play(){
        sound.play();
    }
    
}
