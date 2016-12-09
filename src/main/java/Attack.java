/*
 * Attack.java
 *
 * Created on 25 octobre 2005, 10:11
 */
 
import java.io.*;
import java.util.*;
import java.applet.*;
/**
 *
 * @author etudiant
 */

class AttackData implements Serializable
{
	int AttackID;
	int StatCarID;

	int ToucheBonus;
	int DommageMin;
	int DommageMax;
	int DommageBonus;
	int TypeDommage;
	int Range;
	int Vchance;
	int VBonus;
	int Chance;
	String SonAttaque;
	int ProjectileID;
	int ChanceEffect;
	int BonusID;
	int TimeMin;
	int TimeMax;
	int TimeBonus;
	int TypeResist;
	int EventID;
	int Save;
	int MPCost;
	int typeVore;
}

public class Attack
{
    public int toucheBonus;
    public int dommageMin;
    public int dommageMax;
    public int dommageBonus;
    public int typeDommage;
    public int range;
    public int vChance;
    public int vBonus;
    public int chance;
    public String sonAttaque;
    public AudioClip isonAttaque;
    public int projectileID;
    public int chanceEffect;
    public int bonusID;
    public int timeMin;
    public int timeMax;
    public int timeBonus;
    public int typeResist;
    public int eventID;
    public int save;
    public int mpCost;
    public int typeVore;
	
	public static Attack[] createAttacksForStatChar( int statCarID )
	{
		Vector<AttackData> data = DataUtil.getAttackData( statCarID );
		
		Attack[] ret = new Attack[data.size()];
		int idx = 0;
		
		Iterator<AttackData> it = data.iterator();
		while (it.hasNext())
		{
			ret[idx++] = new Attack( it.next() );
		}
		
		return ret;
	}
    
    /** Creates a new instance of Attack */
    public Attack(int toucheBonus, int dommageMin, int dommageMax, int dommageBonus, int typeDommage, int range, int vChance, int vBonus, int chance, String sonAttaque,int projectileID, int chanceEffect, int bonusID, int timeMin, int timeMax, int timeBonus, int typeResist, int eventID, int save, int mpCost, int typeVore) {
        this.toucheBonus=toucheBonus;
        this.dommageMin=dommageMin;
        this.dommageMax=dommageMax;
        this.dommageBonus=dommageBonus;
        this.typeDommage=typeDommage;
        this.range=range;
        this.vChance=vChance;
        this.vBonus=vBonus;
        this.chance=chance;
        this.sonAttaque=sonAttaque;
        this.projectileID=projectileID;
        this.chanceEffect=chanceEffect;
        this.bonusID=bonusID;
        this.timeMin=timeMin;
        this.timeMax=timeMax;
        this.timeBonus=timeBonus;
        this.typeResist=typeResist;
        this.eventID=eventID;
        this.save=save;
        this.mpCost=mpCost;
        this.typeVore=typeVore;
		
        this.isonAttaque=AudioLibrairie.getClip(sonAttaque);
    
    }
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Attack( AttackData data )
	{
		toucheBonus = data.ToucheBonus;
		dommageMin = data.DommageMin;
		dommageMax = data.DommageMax;
		dommageBonus = data.DommageBonus;
		typeDommage = data.TypeDommage;
		range = data.Range;
		vChance = data.Vchance;
		vBonus = data.VBonus;
		chance = data.Chance;
		sonAttaque = data.SonAttaque;
		projectileID = data.ProjectileID;
		chanceEffect = data.ChanceEffect;
		bonusID = data.BonusID;
		timeMin = data.TimeMin;
		timeMax = data.TimeMax;
		timeBonus = data.TimeBonus;
		typeResist = data.TypeResist;
		eventID = data.EventID;
		save = data.Save;
		mpCost = data.MPCost;
		typeVore = data.typeVore;
		
		isonAttaque=AudioLibrairie.getClip(sonAttaque);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
    public int calculDommage(){
        Random hasard=new Random();
        return hasard.nextInt(dommageMax-dommageMin)+dommageMin+dommageBonus;
    }
    
}
