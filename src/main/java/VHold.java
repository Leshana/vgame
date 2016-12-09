/*
 * VHold.java
 *
 * Created on 13 janvier 2006, 10:18
 */

import java.util.*;
 
/**
 *
 * @author etudiant
 */
public class VHold {
	public StatChar victime;
	public int digTime;
	public StatChar mere;
	public int vore;
	
	public static final int ORAL=0;
	public static final int UNBIRTH=1;
	public static final int ANAL=2;
	
	/** Creates a new instance of VHold */
	public VHold(StatChar vic, StatChar m, int digTime, int type) {
		mere=m;
		victime=vic;
		this.digTime=digTime;
		vore=type;
	}
	
	public VHold(StatChar vic, StatChar m) {
		this(vic, m, 0, ORAL);
	}
	
	public VHold( StatChar pred, SaveVHold saveData )
	{
		mere = pred;
		
		if (saveData.victime >= 0)
		{
			victime = ((ObjetGraphique)FenetreTest.instance.objetG.elementAt( saveData.victime )).statistique;
		}
		else
		{
			victime = null;
		}
		
		digTime = saveData.digTime;
		vore = saveData.vore;
	}
	
	public VHold(StatChar vic, StatChar m, int digTime) {
		this(vic, m, digTime, ORAL);
	}
	
	public VHold(StatChar vic, StatChar m, int type, boolean diff) {
		this(vic, m, 0, type);
	}
	
	public SaveVHold save()
	{
		SaveVHold ret = new SaveVHold();
		
		ret.digTime = digTime;
		ret.vore = vore;
		
		ret.victime = -1;
		
		Vector<ObjetGraphique> objetG = FenetreTest.instance.objetG;
		for (int i = 0; i < objetG.size(); ++i)
		{
			ObjetGraphique obG = objetG.elementAt( i );
			if (obG.statistique == victime)
			{
				ret.victime = i;
				break;
			}
		}
		
		return ret;
	}
	
	public void unbirth(boolean valeur){
		if(valeur)
			vore=UNBIRTH;
		else
			vore=ORAL;
	}
	public void analVoring(boolean valeur){
		if(valeur)
			vore=ANAL;
		else
			vore=ORAL;
	}
	
	public boolean isUnbirth(){
		return vore == UNBIRTH;
	}
	
	public boolean isAnal(){
		return vore == ANAL;
	}
	
	public boolean isOral(){
		return vore == ORAL;
	}
	
	public int getVore(){
		return vore;
	}
	
	public boolean dig(){
		int amt = mere.calculDommageA();
		
		if(digTime>=mere.digTimeMax/2)
			amt *= 2;

		return victime.appliquerDommage(amt,Util.DAMAGE_ACID);
	}
	
}
