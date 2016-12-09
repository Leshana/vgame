///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Bonus.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class BonusData implements Serializable
{
	public int BonusID; // Key field (must come first)
	
	public String Nom, description;
	public String family;
	
	public int Strength, Intelligence, Endurence, Charisma, Dexterity;
	public int aBonus, VEscapeBonus, DigTimeMax, Famine, VBonus, Absorption, VDefense, VDifficulty, manaAbsorption;
	public int Defense, AttackBonus, DamageBonus, attackStat;
	public int ResPhysical, ResFire, ResIce, ResAcid, ResLightning, ResPositive, ResNegative, ResNeutral, ResCharm, ResParalysis, ResSleep, ResCurse;
	public int Side, Status, size, polymorph, Regeneration, Stack;
	public int SaveBonus, CastingBonus, manaRegeneration;
	public int EventStart, EventEnd, HitEvent, swallowEvent, swallowedEvent, digestingEvent, digestedEvent, escapeEvent, escapedEvent;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Bonus
{
	public StatChar mere = null;
	
	public int strength = 0;
	public int intelligence = 0;
	public int endurence = 0;
	public int charisma = 0;
	public int dexterity = 0;
	public int aBonus = 0;
	public int vEscapeBonus = 0;
	public int digTimeMax = 0;
	public int famine = 0;
	public int defense = 0;
	public int typeBonus = 0;
	public int temps = 0;
	public int tempsMax = 0;
	public int attackBonus = 0;
	public int damageBonus = 0;
	public int side = 0;
	public int vBonus = 0;
	public int absorption = 0;
	public int status = 0;
	public int bonusID = 0;
	public int vDefense = 0;
	public int size = 0;
	public int polymorph = 0;
	public int vDifficulte = 0;
	public int regeneration = 0;
	public int stack = 0;
	public int saveBonus = 0;
	public int castingBonus = 0;
	public int manaAbsorption = 0;
	public int manaRegeneration = 0;
	public int attackStat = 0;
	
	public int eventStart = 0;
	public int eventEnd = 0;
	public int hitEvent = 0;
	public int swallowEvent = 0;
	public int swallowedEvent = 0;
	public int digestingEvent = 0;
	public int digestedEvent = 0;
	public int escapeEvent = 0;
	public int escapedEvent = 0;
	
	public String nom = "";
	public String description = "";
	public String family = "";
	
	public int[] resistance = null;
	
	public static final int PERMANENT=0;
	public static final int TEMPORAIRE=1;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Bonus()
	{
		resistance = new int[Util.NUM_DAMAGE_TYPES];
	}
	
	public Bonus(int bonusID, StatChar m)
	{
		this.bonusID=bonusID;
		typeBonus=PERMANENT;
		mere=m;
		resistance=new int[Util.NUM_DAMAGE_TYPES];
		
		loadFromData( DataUtil.getBonusData( bonusID ) );
	}

	public Bonus(int bonusID, StatChar m,int tempsMax) {
		this(bonusID, m);
		typeBonus=TEMPORAIRE;
		this.tempsMax=tempsMax;
		temps=0;
		appliquerBonus();
	}
	public Bonus(int bonusID, StatChar m,int tempsMax, int temps) {
		this(bonusID, m);
		typeBonus=TEMPORAIRE;
		this.tempsMax=tempsMax;
		this.temps=temps;
		appliquerTempBonus();
	}
	
	public Bonus( SaveBonus saveBonus, StatChar m )
	{
		this( saveBonus.BonusID, m, saveBonus.tempsMax, saveBonus.temps );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( BonusData bonusData )
	{
		nom = bonusData.Nom;
		strength = bonusData.Strength;
		intelligence = bonusData.Intelligence;
		endurence = bonusData.Endurence;
		charisma = bonusData.Charisma;
		dexterity = bonusData.Dexterity;
		aBonus = bonusData.aBonus;
		vEscapeBonus = bonusData.VEscapeBonus;
		digTimeMax = bonusData.DigTimeMax;
		famine = bonusData.Famine;
		defense = bonusData.Defense;
		resistance[Util.DAMAGE_PHYSICAL] = bonusData.ResPhysical;
		resistance[Util.DAMAGE_FIRE] = bonusData.ResFire;
		resistance[Util.DAMAGE_ICE] = bonusData.ResIce;
		resistance[Util.DAMAGE_ACID] = bonusData.ResAcid;
		resistance[Util.DAMAGE_LIGHTNING] = bonusData.ResLightning;
		resistance[Util.DAMAGE_POSITIVE] = bonusData.ResPositive;
		resistance[Util.DAMAGE_NEGATIVE] = bonusData.ResNegative;
		resistance[Util.DAMAGE_NEUTRAL] = bonusData.ResNeutral;
		resistance[Util.DAMAGE_CHARM] = bonusData.ResCharm;
		resistance[Util.DAMAGE_PARALYSIS] = bonusData.ResParalysis;
		resistance[Util.DAMAGE_SLEEP] = bonusData.ResSleep;
		resistance[Util.DAMAGE_CURSE] = bonusData.ResCurse;
		attackBonus = bonusData.AttackBonus;
		damageBonus = bonusData.DamageBonus;
		side = bonusData.Side;
		vBonus = bonusData.VBonus;
		absorption = bonusData.Absorption;
		status = bonusData.Status;
		vDefense = bonusData.VDefense;
		size = bonusData.size;
		polymorph = bonusData.polymorph;
		vDifficulte = bonusData.VDifficulty;
		regeneration = bonusData.Regeneration;
		stack = bonusData.Stack;
		saveBonus = bonusData.SaveBonus;
		castingBonus = bonusData.CastingBonus;
		description = bonusData.description;
		manaAbsorption = bonusData.manaAbsorption;
		manaRegeneration = bonusData.manaRegeneration;
		family = bonusData.family;
		attackStat = bonusData.attackStat;
		
		eventStart = bonusData.EventStart;
		eventEnd = bonusData.EventEnd;
		hitEvent = bonusData.HitEvent;
		swallowEvent = bonusData.swallowEvent;
		swallowedEvent = bonusData.swallowedEvent;
		digestingEvent = bonusData.digestingEvent;
		digestedEvent = bonusData.digestedEvent;
		escapeEvent = bonusData.escapeEvent;
		escapedEvent = bonusData.escapedEvent;
		
		if( nom == null )
		{
			nom = "<Unnamed Bonus>";
		}
		
		if( description == null )
		{
			description = "";
		}
		
		description = description.trim();
	}
	
	public SaveBonus save()
	{
		SaveBonus ret = new SaveBonus();
		
		ret.BonusID = bonusID;
		ret.temps = temps;
		ret.tempsMax = tempsMax;
		
		return ret;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean passeTemp(int t){
		temps+=t;
		if(temps>tempsMax){
			enleverBonus();
			return true;
		}
		return false;
	}
	
	public void appliquerBonus()
	{
		mere.strength+=strength;
		mere.intelligence+=intelligence;
		mere.endurence+=endurence;
		mere.charisma+=charisma;
		mere.dexterity+=dexterity;
		mere.aDommageBonus+=aBonus;
		mere.vEscapeBonus+=vEscapeBonus;
		mere.digTimeMax+=digTimeMax;
		mere.famine+=famine;
		mere.defense+=defense;
		for(int i=0;i<12;i++){
			mere.resistance[i]+=resistance[i];
		}
		mere.attackBonus+=attackBonus;
		mere.damageBonus+=damageBonus;
		if(side>0){
			if(mere.actualSide!=12)
				mere.actualSide=side;
		}
		mere.vBonus+=vBonus;
		mere.absorption+=absorption;
		switch(mere.status){
			case 0:
				if(status>-1)
					mere.status=status;
				break;
			case 1:
				switch(status){
					case 0:
						mere.status=status;
						break;
					case 2:
						mere.status+=status;
						break;
					case 3:
						mere.status=3;
						break;
				}
				break;
			 case 2:
				switch(status){
					case 0:
						mere.status=status;
						break;
					case 1:
						mere.status+=status;
						break;
					case 3:
						mere.status=3;
						break;
				}
				break;
			 case 3:
				 if(status==0)
					 mere.status=0;
				 break;
		}
		mere.vDefense+=vDefense;
		switch(size){
			case 0:
				  mere.size=0;  
			break;
			case 1:
				switch(mere.size){
					case 0:
						mere.size=1;
					   break;
					case 2:
						mere.size=0;
				}
			 break;
			 case 2:
				switch(mere.size){
					case 0:
						mere.size=2;
					   break;
					case 1:
						mere.size=0;
				}
			 break;
		}
		if(polymorph>-1){
			mere.polymorph(polymorph);
		}

		mere.vDifficulte+=vDifficulte;
		mere.regeneration+=regeneration;

		if(eventStart>0)
		{
			ObjetGraphique srcTemp=FenetreTest.instance.src;
			FenetreTest.instance.src=mere.mere;
			FenetreTest.instance.executeScript( eventStart );
			FenetreTest.instance.src=srcTemp;
		}
		appliquerTempBonus();
	}

	public void interruptBonus()
	{
		boolean trouve=false;
		mere.strength-=strength;
		mere.intelligence-=intelligence;
		mere.endurence-=endurence;
		mere.charisma-=charisma;
		mere.dexterity-=dexterity;
		mere.aDommageBonus-=aBonus;
		mere.vEscapeBonus-=vEscapeBonus;
		mere.digTimeMax-=digTimeMax;
		mere.famine-=famine;
		mere.defense-=defense;
		for(int i=0;i<12;i++)
		{
			mere.resistance[i]-=resistance[i];
		}
		mere.attackBonus-=attackBonus;
		mere.damageBonus-=damageBonus;
		if(side>0){
			if(mere.actualSide!=12 || side==12)
			mere.actualSide=mere.side;
		}
		mere.vBonus-=vBonus;
		mere.absorption-=absorption;
		if(status==1 || status==3){
			if(mere.status==1 || mere.status==3)
				mere.status--;
		}
		mere.vDefense-=vDefense;
		if(mere.size==size)
			mere.size=0;
		if(polymorph==mere.polymorph){
			mere.polymorph(0);
		}
		mere.vDifficulte-=vDifficulte;
		mere.regeneration-=regeneration;
		mere.castingBonus-=castingBonus;
		mere.saveBonus-=saveBonus;
		mere.manaAbsorption-=manaAbsorption;
		mere.manaRegeneration-=manaRegeneration;
		if(!family.equalsIgnoreCase("-"))
			mere.restoreFamily();
		if(attackStat!=0){
			for(int i=0;i<mere.bonus.size();i++){
				Bonus temp=(Bonus)mere.bonus.elementAt(i);
				if(temp!=this && temp.attackStat!=0){
					trouve=true;
					break;
				}
			}
			if(trouve)
				mere.restoreAttaque();
		}    
	}
	
	public void enleverBonus(){
		interruptBonus();
		if(eventEnd>0){
			ObjetGraphique srcTemp=FenetreTest.instance.src;
			FenetreTest.instance.src=mere.mere;
			FenetreTest.instance.executeScript(eventEnd);
			FenetreTest.instance.src=srcTemp;
		}
		
	}
	public void appliquerTempBonus(){
		mere.castingBonus+=castingBonus;
		mere.saveBonus+=saveBonus;
		mere.manaAbsorption+=manaAbsorption;
		mere.manaRegeneration+=manaRegeneration;
		if(!family.equalsIgnoreCase("-"))
			mere.family=family;
		if(attackStat!=0)
			mere.changeAttaque(attackStat);
	}
}
