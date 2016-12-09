/*
 * AnalyseString.java
 *
 * Created on 8 novembre 2005, 10:01
 */
import java.util.Vector;

/**
 *
 * @author etudiant
 */
public class AnalyseString {
    
    /** Creates a new instance of AnalyseString */
    public AnalyseString() {
    }
    
    public static Vector<String> analyse(String chaine){
    	
        Vector<String> chaine2=new Vector<String>();
        int posDebut=0;
        int posFin=chaine.indexOf('(');
        int posFinale=chaine.indexOf(')');
        String temp=new String(chaine);
        
        chaine2.addElement(chaine.substring(posDebut,posFin));
        if(posFinale-1!=posFin){
            do{
                temp=new String(temp.substring(posFin+1));
                posDebut=0;
                posFin=temp.indexOf(',');
                posFinale=temp.indexOf(')');
                
                if(posFin<0)
                    chaine2.addElement(temp.substring(posDebut,posFinale).trim());
                else
                    chaine2.addElement(temp.substring(posDebut,posFin).trim());
            }
            while(posFin>0);
        }
        
        return chaine2;
    }

    public static Vector separate(String chaine){
        Vector<String> chaine2=new Vector<String>();
        int posDebut=0;
        int posFin;
        
        do{
            //posDebut=0;
            posFin=chaine.indexOf("\n");
            if(posFin==-1){
                chaine2.addElement(chaine.substring(posDebut).trim());
            }
            else{
                chaine2.addElement(chaine.substring(posDebut,posFin).trim());
                chaine=chaine.substring(posFin+1);
            }
        }
        while(posFin!=-1);
            
        
        return chaine2;
    }
    
}
