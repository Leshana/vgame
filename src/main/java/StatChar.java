///////////////////////////////////////////////////////////////////////////////////////////////////
//
// StatChar.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.applet.*;

class StatCharData implements Serializable
{
	public int StatID;
	
	public String Nom;
	public String Picture;

	public int PV;
	public int PM;

	public int Strength;
	public int Intelligence;
	public int Dexterity;
	public int Endurence;
	public int Charisma;
	public int Niveau;
	public int nbAttaque;
	public int Defense;
	public int ResFire;
	public int ResIce;
	public int ResAcid;
	public int ResLightning;
	public int ResPositive;
	public int ResNegative;
	public int ResNeutral;
	public int ResPhysical;
	public int ResCharm;
	public int ResParalysis;
	public int ResSleep;
	public int ResCurse;

	public int AcidDamageMin;
	public int AcidDamageMax;
	public int AcidDamageBonus;
	public int VDifficulty;
	public int VEscape;
	public String SonSw;
	public String SonDeath;
	public String SonD1;
	public String SonD2;
	public String SonD3;
	public String SonB1;
	public String SonB2;
	public String SonB3;
	public String SonS1;
	public String SonS2;
	public String SonS3;
	public String SonCrap;
	public String SonVom;

	public int Deplacement;
	public int digTimeMax;
	public int VOnly;
	public long Experience;
	public int TypeMonster;
	public int Dung;
	public int VDefense;
	public String Family;
	public int Dung2;
	public int Pooping;
	public String SonU;
	public String SonB;
	public int udung;
	public String SonAnl;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class StatChar extends GameObject
{
	public int side;
	public boolean enable;
	public int pv;
	public int pm;
	public int strength=1;
	public int intelligence=1;
	public int dexterity=1;
	public int endurence=1;
	public int charisma=1;
	public int niveau=1;
	public int nbAttack;
	public Attack attaque[];
	public int defense;
	public int resistance[];
	public int aDommageMin;
	public int aDommageMax;
	public int aDommageBonus;
	public int vDifficulte;
	public int vEscapeBonus;
	public int iD;
	public int carID;
	public String sonV;
	public String sonMort;
	public String sonD1, sonD2, sonD3;
	public String sonB1, sonB2, sonB3;
	public String sonS1, sonS2, sonS3;
	public String sonC, sonVo, sonU, sonB, sonAnl;
	public AudioClip isonD1, isonD2, isonD3;
	public AudioClip isonB1, isonB2, isonB3;
	public AudioClip isonS1, isonS2, isonS3;
	public AudioClip isonC, isonVo, isonV, isonU, isonB, isonAnl;
	public int status;
	public int digTimeMax;
	public int famine;
	public long experience;
	public int typePersonnage;
	public int vOnly=0;
	public int pvMax;
	public int pmMax;
	public int typeMonster;
	public int attackBonus=0;
	public int damageBonus=0;
	public ObjetGraphique mere;
	public StatChar maitre;
	public int actualSide;
	public int vBonus=0;
	public int absorption=0;
	public int dung=2;
	public int vDefense=0;
	public int size=0;
	public int polymorph=0;
	public String family;
	public int regeneration=0;
	public int dung2=2;
	public int udung;
	public int castingBonus=0;
	public int saveBonus=0;
	public int manaAbsorption=0;
	public int pooping=0;
	public int manaRegeneration=0;
	
	public Vector<Bonus> bonus;
	public Vector<VHold> victimes;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public static final int PLAYER=0;
	public static final int MONSTER=1;
	public static final int NPC=2;

	///////////////////////////////////////////////////////////////////////////////////////////////
	protected StatChar() {}

	protected StatChar(ObjetGraphique m)
	{
		mere=m;
		bonus=new Vector<Bonus>();
		victimes=new Vector<VHold>();
		maitre=null;
		this.resistance=new int[12];
	}
	
	public StatChar( SaveCharacter saveChar, ObjetGraphique m )
	{
		this( m );
		
		setName( saveChar.Nom );
		
		carID = saveChar.StatID;
		pv = saveChar.PV;
		pm = saveChar.PM;
		strength = saveChar.Strength;
		intelligence = saveChar.Intelligence;
		dexterity = saveChar.Dexterity;
		endurence = saveChar.Endurence;
		charisma = saveChar.Charisma;
		niveau = saveChar.Niveau;
		defense = saveChar.Defense;
		resistance = Arrays.copyOf( saveChar.Resistance, saveChar.Resistance.length );
		aDommageMin = saveChar.AcidDamageMin;
		aDommageMax = saveChar.AcidDamageMax;
		aDommageBonus = saveChar.AcidDamageBonus;
		vDifficulte = saveChar.VDifficulty;
		vEscapeBonus = saveChar.VEscape;
		status = saveChar.Status;
		digTimeMax = saveChar.digTimeMax;
		experience = saveChar.Experience;
		pvMax = saveChar.pvMax;
		pmMax = saveChar.pmMax;
		attackBonus = saveChar.attackBonus;
		damageBonus = saveChar.DamageBonus;
		actualSide = saveChar.ActualSide;
		vBonus = saveChar.vBonus;
		absorption = saveChar.Absorption;
		vDefense = saveChar.VDefense;
		size = saveChar.Size;
		polymorph = saveChar.Polymorph;
		side = saveChar.Side;
		regeneration = saveChar.Regeneration;

		StatCharData data = DataUtil.getStatCharData( carID );
		
		if (data != null)
		{
			setImage( data.Picture );

			nbAttack = data.nbAttaque;
			sonV = data.SonSw;
			sonMort = data.SonDeath;
			sonD1 = data.SonD1;
			sonD2 = data.SonD2;
			sonD3 = data.SonD3;
			sonB1 = data.SonB1;
			sonB2 = data.SonB2;
			sonB3 = data.SonB3;
			sonS1 = data.SonS1;
			sonS2 = data.SonS2;
			sonS3 = data.SonS3;
			sonC = data.SonCrap;
			sonVo = data.SonVom;
			typeMonster = data.TypeMonster;
			dung = data.Dung;
			vOnly = data.VOnly;
			family = data.Family;
			dung2 = data.Dung2;
			pooping = data.Pooping;
			sonU = data.SonU;
			sonB = data.SonB;
			udung = data.udung;
			sonAnl = data.SonAnl;
		}

		changeAttaque( carID );

		reload();
		
		for (int i = 0; i < saveChar.Bonus.size(); ++i)
		{
			bonus.addElement( new Bonus( saveChar.Bonus.elementAt( i ), this ) );
		}
		
		// Victim list has to wait until after the whole character list gets loaded.
	}

	public StatChar(int id,int carID, int side, ObjetGraphique m) {
		this(m);
		this.iD=id;
		this.carID=carID;
		this.side=side;
		this.actualSide = side;
		this.status=0;

		StatCharData data = DataUtil.getStatCharData( carID );
		
		if (data != null)
		{
			setName( data.Nom );
			setImage( data.Picture );

			this.pvMax = data.PV;
			this.pmMax = data.PM;

			this.strength = data.Strength;
			this.intelligence = data.Intelligence;
			this.dexterity = data.Dexterity;
			this.endurence = data.Endurence;
			this.charisma = data.Charisma;
			this.niveau = data.Niveau;
			this.defense = data.Defense;
			this.resistance[Util.DAMAGE_FIRE] = data.ResFire;
			this.resistance[Util.DAMAGE_ICE] = data.ResIce;
			this.resistance[Util.DAMAGE_ACID] = data.ResAcid;
			this.resistance[Util.DAMAGE_LIGHTNING] = data.ResLightning;
			this.resistance[Util.DAMAGE_POSITIVE] = data.ResPositive;
			this.resistance[Util.DAMAGE_NEGATIVE] = data.ResNegative;
			this.resistance[Util.DAMAGE_NEUTRAL] = data.ResNeutral;
			this.resistance[Util.DAMAGE_PHYSICAL] = data.ResPhysical;
			this.resistance[Util.DAMAGE_CHARM] = data.ResCharm;
			this.resistance[Util.DAMAGE_PARALYSIS] = data.ResParalysis;
			this.resistance[Util.DAMAGE_SLEEP] = data.ResSleep;
			this.resistance[Util.DAMAGE_CURSE] = data.ResCurse;

			this.aDommageMin = data.AcidDamageMin;
			this.aDommageMax = data.AcidDamageMax;
			this.aDommageBonus = data.AcidDamageBonus;
			this.vDifficulte = data.VDifficulty;
			this.vEscapeBonus = data.VEscape;
			this.sonV = data.SonSw;
			this.sonMort = data.SonDeath;
			this.sonD1 = data.SonD1;
			this.sonD2 = data.SonD2;
			this.sonD3 = data.SonD3;
			this.sonB1 = data.SonB1;
			this.sonB2 = data.SonB2;
			this.sonB3 = data.SonB3;
			this.sonS1 = data.SonS1;
			this.sonS2 = data.SonS2;
			this.sonS3 = data.SonS3;
			this.sonC = data.SonCrap;
			this.sonVo = data.SonVom;

			this.digTimeMax = data.digTimeMax;
			this.vOnly = data.VOnly;
			this.experience = data.Experience;
			this.typeMonster = data.TypeMonster;
			this.dung = data.Dung;
			this.vDefense = data.VDefense;
			this.family = data.Family;
			this.dung2 = data.Dung2;
			this.pooping = data.Pooping;
			this.sonU = data.SonU;
			this.sonB = data.SonB;
			this.udung = data.udung;
			this.sonAnl = data.SonAnl;
		}

		changeAttaque( carID );

		this.pv=pvMax;
		this.pm=pmMax;

		this.isonB1 = AudioLibrairie.getClip( this.sonB1 );
		this.isonB2 = AudioLibrairie.getClip( this.sonB2 );
		this.isonB3 = AudioLibrairie.getClip( this.sonB3 );
		this.isonD1 = AudioLibrairie.getClip( this.sonD1 );
		this.isonD2 = AudioLibrairie.getClip( this.sonD2 );
		this.isonD3 = AudioLibrairie.getClip( this.sonD3 );
		this.isonS1 = AudioLibrairie.getClip( this.sonS1 );
		this.isonS2 = AudioLibrairie.getClip( this.sonS2 );
		this.isonS3 = AudioLibrairie.getClip( this.sonS3 );
		this.isonV = AudioLibrairie.getClip( this.sonV );
		this.isonVo = AudioLibrairie.getClip( this.sonVo );
		this.isonC = AudioLibrairie.getClip( this.sonC );
		this.isonU = AudioLibrairie.getClip( this.sonU );
		this.isonB = AudioLibrairie.getClip( this.sonB );
		this.isonAnl = AudioLibrairie.getClip( this.sonAnl );
	}
	
	public SaveCharacter save()
	{
		SaveCharacter ret = new SaveCharacter();
		
		ret.StatID = carID;
		ret.Nom = getName();
		ret.PV = pv;
		ret.PM = pm;
		ret.Strength = strength;
		ret.Intelligence = intelligence;
		ret.Dexterity = dexterity;
		ret.Endurence = endurence;
		ret.Charisma = charisma;
		ret.Niveau = niveau;
		ret.Defense = defense;
		ret.Resistance = Arrays.copyOf( resistance, resistance.length );
		ret.AcidDamageMin = aDommageMin;
		ret.AcidDamageMax = aDommageMax;
		ret.AcidDamageBonus = aDommageBonus;
		ret.VDifficulty = vDifficulte;
		ret.VEscape = vEscapeBonus;
		ret.Status = status;
		ret.digTimeMax = digTimeMax;
		ret.Experience = experience;
		ret.pvMax = pvMax;
		ret.pmMax = pmMax;
		ret.attackBonus = attackBonus;
		ret.DamageBonus = damageBonus;
		ret.Side = side;
		ret.ActualSide = actualSide;
		ret.vBonus = vBonus;
		ret.Absorption = absorption;
		ret.VDefense = vDefense;
		ret.Polymorph = polymorph;
		ret.Size = size;
		ret.Regeneration = regeneration;
		
		ret.Bonus = new Vector<>();
		for (int i = 0; i < bonus.size(); ++i)
		{
			ret.Bonus.add( bonus.elementAt( i ).save() );
		}
		
		ret.Victimes = new Vector<>();
		for (int i = 0; i < victimes.size(); ++i)
		{
			ret.Victimes.add( victimes.elementAt( i ).save() );
		}
		
		return ret;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public int calculDommageA()
	{
		Random hasard=new Random();
		return hasard.nextInt(aDommageMax-aDommageMin)+aDommageMin+aDommageBonus;
	}

	public void changeAttaque(int altStatID)
	{
		attaque = Attack.createAttacksForStatChar( altStatID );
		nbAttack = attaque.length;
	}

	public void restoreFamily()
	{
		StatCharData data = DataUtil.getStatCharData( carID );
		
		if (data != null)
		{
			family = data.Family;
		}
	}

	public void restoreAttaque()
	{
		changeAttaque(carID);
	}

	public boolean appliquerDommage(int dommage, int typeDommage)
	{
		if(status>1)
			status-=2;

		pv-=(dommage*(100-resistance[typeDommage])/100);
		if(vOnly!=1||typeDommage==Util.DAMAGE_ACID){
			if(pv<1)
				return true;
			else
				return false;
		}
		return false;
	}

	public void gainHp(int hp)
	{
		pv+=hp;
		if(pv>pvMax)
			pv=pvMax;
	}

	public void gainMp(int mp)
	{
		pm+=mp;
		if(pm>pmMax)
			pm=pmMax;
		if(pm<0)
			pm=0;
	}

	public void giveExp(long exp)
	{
		experience += exp;
		while( experience > Util.levelCost( niveau ) )
		{
			monterNiveau();
		}
	}

	protected void monterNiveau()
	{
		niveau++;
		int choixStat;
		if(niveau%11==0)
			choixStat=5;
		else if(niveau%7==0)
			choixStat=4;
		else if(niveau%5==0)
			choixStat=3;
		else if(niveau%3==0)
			choixStat=2;
		else
			choixStat=1;
		if(niveau%5==0){
			aDommageBonus+=1;
			aDommageMax+=1;
			aDommageMin+=1;
			vDifficulte+=1;
		}
		if(niveau%8==0){
			digTimeMax++;
		}

		switch(typeMonster)
		{
		case Util.CLASS_FIGHTER:
			switch(choixStat)
			{
			case 1: strength++; break;
			case 2: endurence++; break;
			case 3: dexterity++; break;
			case 4: charisma++; break;
			case 5: intelligence++; break;
			}
			break;
		case Util.CLASS_MAGE:
			switch(choixStat)
			{
			case 1: intelligence++; break;
			case 2: dexterity++; break;
			case 3: charisma++; break;
			case 4: endurence++; break;
			case 5: strength++; break;
			}
			break;
		case Util.CLASS_THIEF:
			switch(choixStat)
			{
			case 1: dexterity++; break;
			case 2: charisma++; break;
			case 3: strength++; break;
			case 4: endurence++; break;
			case 5: intelligence++; break;
			}
			break;
		case Util.CLASS_ARCHER:
			switch(choixStat){
			case 1: dexterity++; break;
			case 2: strength++; break;
			case 3: endurence++; break;
			case 4: charisma++; break;
			case 5: intelligence++; break;
			}
			break;
		case Util.CLASS_SHAMAN:
			switch(choixStat)
			{
			case 1: intelligence++; break;
			case 2: strength++; break;
			case 3: dexterity++; break;
			case 4: endurence++; break;
			case 5: charisma++; break;
			}
			break;
		case Util.CLASS_RANGER:
			switch(choixStat)
			{
			case 1: strength++; break;
			case 2: dexterity++; break;
			case 3: endurence++; break;
			case 4: intelligence++; break;
			case 5: charisma++; break;
			}
			break;
		case Util.CLASS_MARCHANT:
			switch(choixStat)
			{
			case 1: charisma++; break;
			case 2: intelligence++; break;
			case 3: strength++; break;
			case 4: dexterity++; break;
			case 5: endurence++; break;
			}
			break;
		}
		pvMax+=endurence*10;
		pmMax+=intelligence*3;
		pv+=endurence*3;
		pm+=intelligence;
		giveExp(0);
	}

	public void passeTempsScript(int t)
	{
		int calcul;
		calcul=niveau/5;
		if(calcul<1)
			calcul=1;
		pm+= t/60 * calcul;
		pm+=manaRegeneration*t;

		if(pm>pmMax)
			pm=pmMax;
		if(pm<0)
			pm=0;

		if(pv>0){
			pv+=t*regeneration;
			if(pv<1)
				pv=1;
			else if(pv>pvMax)
				pv=pvMax;
		}

		for(int i=0;i<bonus.size();i++)
		{
			 if(((Bonus)bonus.elementAt(i)).passeTemp(t))
			 {
				 bonus.remove(i);
				 i--;
			 }
		}
	}

	public void passeTemps(int t)
	{
		if(pm<pmMax && FenetreTest.instance.getPlayer()!=null)
		{
			PlayerStat joueur=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
			if(joueur.clock.getMinutes()==0)
			{
				int valeur=niveau/5;
				if(valeur<1)
					valeur=1;
				pm+=valeur;
				if(pm>pmMax)
					pm=pmMax;
			}
		}
		pm+=manaRegeneration;
		if(pm>pmMax)
			pm=pmMax;
		if(pm<0)
			pm=0;

		if(pv>0)
		{
			pv+=t*regeneration;
			if(pv<1)
				pv=1;
			else if(pv>pvMax)
				pv=pvMax;
		}
		if(t>1 && victimes.size()>0)
		{
			for(;t>0;t--)
			{
				for(int i=0;i<bonus.size();i++)
				{
					if(((Bonus)bonus.elementAt(i)).passeTemp(1))
					{
						bonus.remove(i);
						i--;
					}
				}
			}
		}
		else
		{
			for(int i=0;i<bonus.size();i++)
			{
				if(((Bonus)bonus.elementAt(i)).passeTemp(t))
				{
					bonus.remove(i);
					i--;
				}
			}
			for(int i=0;i<victimes.size();i++)
			{
				((VHold)victimes.elementAt(i)).digTime+=t;
			}
		}

		for(int i=0;i<victimes.size();i++)
		{
			VHold temp=(VHold)victimes.elementAt(i);

			if(temp.victime!=null)
			{
				ObjetGraphique tempSource2=FenetreTest.instance.src;
				ObjetGraphique tempTarget2=FenetreTest.instance.tgt;
				FenetreTest.instance.tgt=temp.victime.mere;
				FenetreTest.instance.src=this.mere;
				digesting();
				if(temp.victime!=null && temp.victime.pv>-999)
				{
					FenetreTest.instance.tgt=this.mere;
					FenetreTest.instance.src=temp.victime.mere;
					temp.victime.digested();
				}
				FenetreTest.instance.tgt=tempTarget2;
				FenetreTest.instance.src=tempSource2;
			}

			if(temp.digTime>digTimeMax)
			{
				if(maitre==null)
				{
				   if(!temp.isUnbirth())
				   {
						if( FenetreTest.instance.isScatEnabled() )
						{
							AudioLibrairie.playClip(isonC);
							FenetreTest.instance.animationStand(FenetreTest.instance.getNoObj(mere),7, false);
							if(temp.victime==null || temp.victime.pv<=-999)
								FenetreTest.instance.AddProp(dung, mere.getX(),mere.getY(), 1);
							else
								FenetreTest.instance.AddProp(dung2, mere.getX(),mere.getY(), 1);
						}
				   }
				   else
				   {
						if(temp.victime==null || temp.victime.pv<=-999)
						{
							AudioLibrairie.playClip(isonB);
						   FenetreTest.instance.animationStand(FenetreTest.instance.getNoObj(mere),9, false);
						   FenetreTest.instance.AddProp(udung, mere.getX(),mere.getY(), 1);
						}
						else
						{
							AudioLibrairie.playClip(isonU);
							FenetreTest.instance.animationStand(FenetreTest.instance.getNoObj(mere),-8, false);
						}
				   }

				}

				if(temp.victime!=null && !(temp.victime.pv<=-999))
				{
					if(maitre==null)
					{
						temp.victime.mere.setVisible(true);

						FenetreTest.instance.reaparitionObjetGraphique(temp.victime.mere,mere.getX(), mere.getY());
						temp.victime.mere.setVisible(true);
						temp.victime.maitre=null;
					}
					else
					{
						VHold vmaitre;
						vmaitre=maitre.getVictime(this);
						if(!vmaitre.isUnbirth())
							maitre.victimes.add(new VHold(temp.victime,maitre));
						else
							maitre.unbirthVictime(temp.victime);
						temp.victime.maitre=maitre;
					}
					gainHp(absorption+niveau);
					giveFood(absorption+niveau);

				}
				else
				{
					gainHp((absorption+niveau)*10);
					giveFood((absorption+niveau+10)*10);
					if(manaAbsorption>0)
					{
						gainMp(manaAbsorption * (niveau/5 +1));
					}
				}

				if(temp.victime!=null && pooping!=0)
				{
					ObjetGraphique tempSource=FenetreTest.instance.src;
					ObjetGraphique tempTarget=FenetreTest.instance.tgt;
					FenetreTest.instance.tgt=temp.victime.mere;
					FenetreTest.instance.src=mere;
					FenetreTest.instance.executeScript(pooping);
					FenetreTest.instance.tgt=tempSource;
					FenetreTest.instance.src=tempTarget;
				}

				victimes.removeElementAt(i);
				i--;
			}
			else if(temp.digTime>digTimeMax/2)
			{
				gainHp((absorption+niveau)*2);
				giveFood((absorption+niveau+10));
				if(manaAbsorption>0)
					gainMp(manaAbsorption+niveau/10);
			}
			else
			{
				gainHp(absorption+niveau);
				giveFood(absorption+niveau+5);
				if(manaAbsorption>0)
					gainMp(manaAbsorption);
			}
		}

		if(victimes.size()<1)
			mere.modeAffichage=1;
	}

	public void giveMoney(int m){}
	public void addObjInventaire(int objetID, int bonusID, int typeObj, boolean use){}
	public boolean verifyObjet(int objetID, int typeObj){
		return false;
	}

	public void removeObjInventaire(int objetID, int typeObj){}
	public void removeOneObjInventaire(int objetID, int typeObj){}

	public int getDefense()
	{
		return defense+dexterity;
	}

	public void addBonus(int bonusID, int tempMax)
	{
		int bon=isEnchanted(bonusID);
		if(bon!=-1)
		{
			Bonus temp=(Bonus)bonus.elementAt(bon);
			if(temp.stack>0)
				temp.tempsMax+=tempMax;
			else
				bonus.addElement(new Bonus(bonusID,this,tempMax));
		}
		else
			bonus.addElement(new Bonus(bonusID,this,tempMax));
	}

	public String getMessageItem(){
		return "";
	}

	public int baseAttaque(){
		return niveau+strength;
	}

	public int faireDommage(int a){
		return attaque[a-1].calculDommage()+damageBonus;
	}
	public int typeDommage(int a){
		return attaque[a-1].typeDommage;
	}

	public int baseDommage(){return 0;}
	public int faireDommage(){return 0;}
	public int typeDommage(){return 0;}
	public int getMoney(){return 0;}

	public void nouveauEvent(String nom,int compteur){}
	public void modificationEvent(String nom, int compteur){}
	public void incrementationEvent(String nom,int compteur){}

	public void reajusterPoint()
	{
		if(pv>pvMax)
			pv=pvMax;
		if(pm>pmMax)
			pm=pmMax;
		if(pm<0)
			pm=0;
	}
	public boolean contient(StatChar vic)
	{
        for(int i=0;i<victimes.size();i++){
            if( ((VHold)victimes.elementAt(i)).victime==vic)
                return true;
        }
        return false;
	}

	public void mort(StatChar tueur)
	{
		if(victimes.size()>0)
		{
			// Handle our victims list.
			if( tueur !=null && tueur.contient( this ) )
			{
				// We were just killed by vore of some type, so empty our victims into our killer.
				VHold myvh = tueur.getVictime( this );

				for( int i = 0; i < victimes.size(); ++i )
				{
					VHold temp=(VHold)victimes.elementAt(i);
					temp.vore = myvh.vore;
					temp.digTime = 0;

					temp.mere = tueur;
					
					if (temp.victime != null)
					{
						temp.victime.maitre = tueur;
					}

					tueur.victimes.addElement(temp);
				}
			}
			else
			{
				// Killed by external sources, dump any living victims back into the world.  Non-
				// living victims die with us.
				for( int i = 0; i < victimes.size(); ++i )
				{
					VHold temp=(VHold)victimes.elementAt(i);
					if( temp.victime.pv > -999 )
					{
						FenetreTest.instance.reaparitionObjetGraphique(temp.victime.mere,mere.getX(),mere.getY());
						temp.victime.maitre=null;
						temp.victime.mere.setVisible(true);
					}
				}
			}

			// Empty our victims list, as they've all been dealt with one way or another.
			victimes.removeAllElements();
		}

		mere.enable=0;

		if(tueur!=null)
		{
			if( mere.deathEventID > 0 )
			{
				FenetreTest.instance.src = mere;
				FenetreTest.instance.tgt = tueur.mere;
				FenetreTest.instance.executeScript( mere.deathEventID );
			}

			if(tueur.niveau<niveau)
			{
				tueur.giveExp(experience/10);
			}
			else
			{
				int difference=tueur.niveau-niveau;
				long experienceDonne=experience / (10+ 2*difference);
				if(experienceDonne<1)
					experienceDonne=1;
				tueur.giveExp(experienceDonne);

			}
		}

		dispose();

		mere.setVisible(false);
		pv=-999;
	}

	public boolean testEventTemps(String nom, int temps,int jour, int mois, int annee, int typeTest){
		return false;
	}

	public boolean testEventCompteur(String nom, int valeur, int typeTest){
		return false;
	}

	public int getVAttaqueBase(int i){
		return strength+dexterity+niveau;
	}

	public int getVAttaqueBonus(int i){
		int bo=vBonus + attaque[i].vBonus;
		switch(size){
			case 1:
				bo-=60;
				break;
			case 2:
				bo+=40;
				break;
		}
		
		bo -= victimes.size() * 10;
		
		return bo;
	}

	public int getVAttaque(int i){
		return getVAttaqueBase(i) + getVAttaqueBonus(i);
	}

	public int getVDefenseBase(){
		return strength+dexterity+niveau+vDefense;
	}
	
	public int getVDefenseBonus(){
		int bo=(int)((double)pv/(double)pvMax*100)-80;
		switch(size){
			case 1:
				bo-=40;
				break;
			case 2:
				bo+=60;
				break;
		}
		
		if(victimes.size() > 0)
		  bo -= 40;
		
		return bo;
	}
	
	public int getVDefense(){
		return getVDefenseBase() + getVDefenseBonus();
	}

	public int getVEscapeBase(){
		return strength+endurence+vEscapeBonus+niveau;
	}

	public int getSpellSaveBase(){
		return endurence/2+intelligence/2+niveau;
	}

	public int getVResistance(){
		return strength*2+niveau+vDifficulte;
	}
	public VHold getVictime( StatChar vic )
	{
		for( int i = 0; i < victimes.size(); ++i )
		{
			VHold vh = (VHold)(victimes.elementAt( i ));
			if( vh.victime == vic )
			{
				return vh;
			}
		}

		return null;
	}

	public void ajouterVictime(StatChar vic){
		victimes.add(new VHold(vic, this));
	}

	public void unbirthVictime(StatChar vic){
		victimes.add(new VHold(vic, this,VHold.UNBIRTH,true));
	}

	public void analVoringVictime(StatChar vic){
		victimes.add(new VHold(vic, this,VHold.ANAL,true));
	}

	public void enleverVictime(StatChar vic)
	{
		for(int i=0;i<victimes.size();i++)
		{
			if( ((VHold)victimes.elementAt(i)).victime==vic )
			{
				victimes.removeElementAt(i);
				break;
			}
		}
		if(victimes.size()<1)
		{
			mere.modeAffichage=1;
		}
	}
	public int penalitePv(){
		if(pv>0)
			return 0;
		return 20;
	}
	public int penaliteV(){
		int temp;
		if(pv<1)
			return 100;
		temp=(int)((double)pv/(double)pvMax*100);
		if(100-temp >49)
			return 0;
		return 100-temp-30;
	}
	public int penaliteV2(){
		int temp;
		if(pv<1)
			return 50;
		temp=(int)((double)pv/(double)pvMax*100);
		if(100-temp >49)
			return 0;
		return 100-temp-50;
	}
	public void giveFood(int nut){}

	public String getSonAttaque(int i){
		return attaque[i].sonAttaque;
	}
	public AudioClip getSonAttaqueInt(int i){
		return attaque[i].isonAttaque;
	}
	public int getRangeAttaque(int i){
		return attaque[i].range;
	}
	public int getSortBase(){
		return intelligence*2/3+charisma/3 +niveau +castingBonus;
	}
	public int getSaveBase(){
		return endurence/2+intelligence/2 +niveau +saveBonus;
	}
	public int getSave(){
		Random hasard=new Random();
		return getSaveBase()+hasard.nextInt(20)+1;
	}
	public void reload(){
		isonB1=AudioLibrairie.getClip(sonB1);
		isonB2=AudioLibrairie.getClip(sonB2);
		isonB3=AudioLibrairie.getClip(sonB3);
		isonD1=AudioLibrairie.getClip(sonD1);
		isonD2=AudioLibrairie.getClip(sonD2);
		isonD3=AudioLibrairie.getClip(sonD3);
		isonS1=AudioLibrairie.getClip(sonS1);
		isonS2=AudioLibrairie.getClip(sonS2);
		isonS3=AudioLibrairie.getClip(sonS3);
		isonV=AudioLibrairie.getClip(sonV);
		isonVo=AudioLibrairie.getClip(sonVo);
		isonC=AudioLibrairie.getClip(sonC);
		isonU=AudioLibrairie.getClip(sonU);
		isonB=AudioLibrairie.getClip(sonB);
		isonAnl=AudioLibrairie.getClip(sonAnl);
		if(mere.type!=FenetreTest.instance.PLAYER){
			for(int i=0;i<attaque.length;i++){
				attaque[i].isonAttaque=AudioLibrairie.getClip(attaque[i].sonAttaque);
			}
		}
	}
	public void polymorph(int nombre){
		polymorph=nombre;
		mere.polymorph(nombre);
	}

	public void transform(int nombre){
		//polymorph=nombre;
		mere.transform(nombre);
	}
	public boolean testEventMasque(String nom,int valeur){return false;}
	public void incrementEventMasque(String nom,int valeur){}
	public void removeEvent(String nom){}
	public Evenement getEvenement(String ev){return null;}

	public int isEnchanted(int bonusId){
		for(int k=0;k<bonus.size();k++){
			Bonus temp=(Bonus)bonus.elementAt(k);
			if(temp.bonusID==bonusId){
				return k;
			}
		}
		return -1;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void beingHit(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.hitEvent>0)
				FenetreTest.instance.executeScript(temporaire.hitEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}
	public void swallowing(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.swallowEvent>0)
				FenetreTest.instance.executeScript(temporaire.swallowEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}
	public void swallowed(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.swallowedEvent>0)
				FenetreTest.instance.executeScript(temporaire.swallowedEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}
	public void digesting(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.digestingEvent>0)
				FenetreTest.instance.executeScript(temporaire.digestingEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}
	public void digested(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.digestedEvent>0)
				FenetreTest.instance.executeScript(temporaire.digestedEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	  }

	public void escape(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.escapeEvent>0)
				FenetreTest.instance.executeScript(temporaire.escapeEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}
	public void escaped(){
		for(int i=0;i<bonus.size();i++){
			Bonus temporaire=(Bonus)bonus.elementAt(i);
			if(temporaire.escapedEvent>0)
				FenetreTest.instance.executeScript(temporaire.escapedEvent, FenetreTest.instance.src, FenetreTest.instance.tgt);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Vector<String> getStatusList()
	{
		Vector<String> sl = new Vector<String>();

		if( (status & 0x1) == 0x1 )
		{
			sl.add( "Paralysed" );
		}
		if( (status & 0x2) == 0x2 )
		{
			sl.add( "Asleep" );
		}
		if( polymorph != 0 )
		{
			sl.add( "Polymorphed" );
		}
		if( size == 1 )
		{
			sl.add( "Shrunken" );
		}
		else if( size == 2 )
		{
			sl.add( "Enlarged" );
		}
		for( int i = 0; i < bonus.size(); ++i )
		{
			String desc = bonus.elementAt(i).description;
			if( !desc.isEmpty() )
			{
				sl.add( desc );
			}
		}

		return sl;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getLevelString()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		String ret = "Level " + nf.format(niveau) + " " + family;

		if( FenetreTest.instance.determinationEnnemi(mere) )
		{
			ret += " (Hostile)";
		}
		
		return ret;
	}

	public String getStatusString()
	{
		Vector<String> sl = getStatusList();
		if( !sl.isEmpty() )
		{
			return Util.implode( sl, ", " );
		}
		
		return "";
	}

	public String getPreyString()
	{
		String ret = "";

		if( !victimes.isEmpty() )
		{
			for( int i = 0; i < victimes.size(); ++i )
			{
				VHold te = (VHold)victimes.elementAt(i);

				if( i > 0 )
				{
					ret += "\n";
				}

				if( !te.isUnbirth() )
				{
					if( te.victime == null )
						ret+="Digesting someone's remains";
					else if( te.victime.pv > -999 )
						ret += "Digesting " + te.victime.getName();
					else
						ret += "Digesting " + te.victime.getName() + "'s remains";
				}
				else
				{
					if( te.victime == null )
						ret += "Absorbing someone";
					else if( te.victime.pv > -999 )
						ret += "Unbirthing " + te.victime.getName();
					else
						ret += "Absorbing " + te.victime.getName();
				}
			}
		}

		return ret;
	}

	public String getPredString()
	{
		String ret = "";

		if(maitre!=null)
		{
			VHold te=null;
			for(int i=0;i<maitre.victimes.size();i++)
			{
				te=(VHold)maitre.victimes.elementAt(i);
				if(te.victime==this)
					break;
			}

			if(!maitre.getVictime(this).isUnbirth())
			{
				if(te.digTime==maitre.digTimeMax/2-1)
					ret+="In "+ maitre.getName() +"'s Stomach (Being Squeezed into her intestines)";
				else if(te.digTime<maitre.digTimeMax/2)
					ret+="In "+ maitre.getName() +"'s Stomach (Being Digested)";
				else if(te.digTime==maitre.digTimeMax-1)
					ret+="In "+ maitre.getName() +"'s Intestines (Being Pushed into her rectum)";
				else if(te.digTime==maitre.digTimeMax)
					ret+="In "+ maitre.getName() +"'s Rectum (Ready to be excreted)";
				else
					ret+="In "+ maitre.getName() +"'s Intestines (Being Digested)";
			}
			else
			{
				if(te.digTime<maitre.digTimeMax-1)
					ret+="In "+ maitre.getName() +"'s Womb";
				else if (te.digTime==maitre.digTimeMax-1)
					ret+="In "+ maitre.getName() +"'s Womb (Being Pushed into her vagina)";
				else if(te.digTime==maitre.digTimeMax)
					ret+="In "+ maitre.getName() +"'s Vagina (Ready to be born again)";
			}
		}

		return ret;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	IconBar m_hpBar, m_mpBar, m_xpBar;
	JLabel m_typeLbl;
	VTextArea m_statusText, m_predText, m_preyText;
	JLabel m_strLbl, m_dexLbl, m_endLbl, m_intLbl, m_chrLbl;
	JLabel[] m_resLbl;
	
	Component m_statusTextSpacer, m_predTextSpacer, m_preyTextSpacer;
	
	@Override // GameObject
	public void initializeView( JPanel vitals, JPanel details )
	{
		JPanel levelPane = new JPanel();
		levelPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		levelPane.setLayout( new BoxLayout( levelPane, BoxLayout.X_AXIS ) );
		{
			m_xpBar = new IconBar();
			m_xpBar.setAlignmentX( Component.LEFT_ALIGNMENT );
			m_xpBar.setIcon( Util.getIcon("stat_level") );
			m_xpBar.setForeground( Util.COLOR_XP_FORE );
			m_xpBar.setBackground( Util.COLOR_XP_BACK );
			levelPane.add( m_xpBar );
			
			levelPane.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
			
			m_typeLbl = new JLabel();
			levelPane.add( m_typeLbl );
		}
		vitals.add( levelPane );
		
		m_hpBar = new IconBar();
		m_hpBar.setAlignmentX( Component.LEFT_ALIGNMENT );
		m_hpBar.setIcon( Util.getIcon("stat_hp") );
		m_hpBar.setForeground( Util.COLOR_HP_FORE );
		m_hpBar.setBackground( Util.COLOR_HP_BACK );
		vitals.add( m_hpBar );
		
		m_mpBar = new IconBar();
		m_mpBar.setAlignmentX( Component.LEFT_ALIGNMENT );
		m_mpBar.setIcon( Util.getIcon("stat_mp") );
		m_mpBar.setForeground( Util.COLOR_MP_FORE );
		m_mpBar.setBackground( Util.COLOR_MP_BACK );
		vitals.add( m_mpBar );
		

		m_statusText = new VTextArea( details );
		m_statusText.setForeground( Color.YELLOW );
		details.add( m_statusText );
		
		m_statusTextSpacer = Box.createRigidArea( new Dimension( 5, 5 ) );
		details.add( m_statusTextSpacer );

		m_predText = new VTextArea( details );
		m_predText.setForeground( Color.RED );
		details.add( m_predText );
		
		m_predTextSpacer = Box.createRigidArea( new Dimension( 5, 5 ) );
		details.add( m_predTextSpacer );

		m_preyText = new VTextArea( details );
		details.add( m_preyText );
		
		m_preyTextSpacer = Box.createRigidArea( new Dimension( 5, 5 ) );
		details.add( m_preyTextSpacer );

		JLabel statLbl = new JLabel();
		statLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		statLbl.setBackground( details.getBackground() );
		statLbl.setText( "Stats" );
		details.add( statLbl );
		
		JPanel statPane = new JPanel();
		statPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		statPane.setBackground( details.getBackground() );
		statPane.setLayout( new GridLayout( 1, 5 ) );
		statPane.setMaximumSize( new Dimension( 500, 16 ) );
		{
			JPanel strPane = new JPanel();
			strPane.setBackground( details.getBackground() );
			strPane.setLayout( new BoxLayout( strPane, BoxLayout.Y_AXIS ) );
			{
				m_strLbl = new JLabel( Util.STAT_TYPES[Util.STAT_STR].icon );
				m_strLbl.setToolTipText( Util.STAT_TYPES[Util.STAT_STR].name );
				m_strLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
				m_strLbl.setBackground( details.getBackground() );
				strPane.add( m_strLbl );
			}
			statPane.add( strPane );

			JPanel dexPane = new JPanel();
			dexPane.setBackground( details.getBackground() );
			dexPane.setLayout( new BoxLayout( dexPane, BoxLayout.Y_AXIS ) );
			{
				m_dexLbl = new JLabel( Util.STAT_TYPES[Util.STAT_DEX].icon );
				m_dexLbl.setToolTipText( Util.STAT_TYPES[Util.STAT_DEX].name );
				m_dexLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
				m_dexLbl.setBackground( details.getBackground() );
				dexPane.add( m_dexLbl );
			}
			statPane.add( dexPane );

			JPanel endPane = new JPanel();
			endPane.setBackground( details.getBackground() );
			endPane.setLayout( new BoxLayout( endPane, BoxLayout.Y_AXIS ) );
			{
				m_endLbl = new JLabel( Util.STAT_TYPES[Util.STAT_END].icon );
				m_endLbl.setToolTipText( Util.STAT_TYPES[Util.STAT_END].name );
				m_endLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
				m_endLbl.setBackground( details.getBackground() );
				endPane.add( m_endLbl );
			}
			statPane.add( endPane );

			JPanel intPane = new JPanel();
			intPane.setBackground( details.getBackground() );
			intPane.setLayout( new BoxLayout( intPane, BoxLayout.Y_AXIS ) );
			{
				m_intLbl = new JLabel( Util.STAT_TYPES[Util.STAT_INT].icon );
				m_intLbl.setToolTipText( Util.STAT_TYPES[Util.STAT_INT].name );
				m_intLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
				m_intLbl.setBackground( details.getBackground() );
				intPane.add( m_intLbl );
			}
			statPane.add( intPane );

			JPanel chrPane = new JPanel();
			chrPane.setBackground( details.getBackground() );
			chrPane.setLayout( new BoxLayout( chrPane, BoxLayout.Y_AXIS ) );
			{
				m_chrLbl = new JLabel( Util.STAT_TYPES[Util.STAT_CHR].icon );
				m_chrLbl.setToolTipText( Util.STAT_TYPES[Util.STAT_CHR].name );
				m_chrLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
				m_chrLbl.setBackground( details.getBackground() );
				chrPane.add( m_chrLbl );
			}
			statPane.add( chrPane );
		}
		details.add( statPane );
		
		details.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );

		// Resistances
		JLabel resLbl = new JLabel();
		resLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		resLbl.setBackground( details.getBackground() );
		resLbl.setText( "Resistances" );
		details.add( resLbl );

		JPanel resPane = new JPanel();
		resPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		resPane.setBackground( details.getBackground() );
		resPane.setLayout( new GridLayout( 3, 4 ) );
		resPane.setMaximumSize( new Dimension( 500, 48 ) );
		{
			JPanel cellPane[] = new JPanel[Util.NUM_DAMAGE_TYPES];
			m_resLbl = new JLabel[Util.NUM_DAMAGE_TYPES];

			for( int i = 0; i < Util.NUM_DAMAGE_TYPES; ++i )
			{
				cellPane[i] = new JPanel();
				cellPane[i].setBackground( details.getBackground() );
				cellPane[i].setLayout( new BoxLayout( cellPane[i], BoxLayout.Y_AXIS ) );
				{
					m_resLbl[i] = new JLabel( Util.DAMAGE_TYPES[i].icon );
					m_resLbl[i].setToolTipText( Util.DAMAGE_TYPES[i].name + " Resistance" );
					m_resLbl[i].setAlignmentX( Component.LEFT_ALIGNMENT );
					m_resLbl[i].setBackground( details.getBackground() );
					cellPane[i].add( m_resLbl[i] );
				}
			}
			
			// We display them in a funky order.
			resPane.add( cellPane[Util.DAMAGE_PHYSICAL] );
			resPane.add( cellPane[Util.DAMAGE_POSITIVE] );
			resPane.add( cellPane[Util.DAMAGE_NEGATIVE] );
			resPane.add( cellPane[Util.DAMAGE_NEUTRAL] );

			resPane.add( cellPane[Util.DAMAGE_FIRE] );
			resPane.add( cellPane[Util.DAMAGE_ICE] );
			resPane.add( cellPane[Util.DAMAGE_LIGHTNING] );
			resPane.add( cellPane[Util.DAMAGE_ACID] );
			
			resPane.add( cellPane[Util.DAMAGE_CHARM] );
			resPane.add( cellPane[Util.DAMAGE_PARALYSIS] );
			resPane.add( cellPane[Util.DAMAGE_SLEEP] );
			resPane.add( cellPane[Util.DAMAGE_CURSE] );
		}
		details.add( resPane );
		
		details.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
		
		// Combat Stats
		JLabel combatLbl = new JLabel();
		combatLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		combatLbl.setBackground( details.getBackground() );
		combatLbl.setText( "Combat" );
		details.add( combatLbl );
		
		JPanel combatStatPane = new JPanel();
		combatStatPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		combatStatPane.setBackground( details.getBackground() );
		combatStatPane.setLayout( new BoxLayout( combatStatPane, BoxLayout.Y_AXIS ) );
		{
			JLabel cAtkLbl = new JLabel();
			cAtkLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			cAtkLbl.setBackground( details.getBackground() );
			cAtkLbl.setText( "  Attack : " + (baseAttaque() + attackBonus) );
			combatStatPane.add( cAtkLbl );
			
			JLabel cDefLbl = new JLabel();
			cDefLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			cDefLbl.setBackground( details.getBackground() );
			cDefLbl.setText( "  Damage : " + (baseDommage() + damageBonus) );
			combatStatPane.add( cDefLbl );
			
			JLabel cSAtkLbl = new JLabel();
			cSAtkLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			cSAtkLbl.setBackground( details.getBackground() );
			cSAtkLbl.setText( "  Spell Casting : " + getSortBase() );
			combatStatPane.add( cSAtkLbl );
			
			JLabel cSDefLbl = new JLabel();
			cSDefLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			cSDefLbl.setBackground( details.getBackground() );
			cSDefLbl.setText( "  Spell Defense : " + getSaveBase() );
			combatStatPane.add( cSDefLbl );
		}
		details.add( combatStatPane );
		
		details.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
		

		// Vore Stats
		JLabel voreLbl = new JLabel();
		voreLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		voreLbl.setBackground( details.getBackground() );
		voreLbl.setText( "Vore" );
		details.add( voreLbl );
		
		JPanel voreStatPane = new JPanel();
		voreStatPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		voreStatPane.setBackground( details.getBackground() );
		voreStatPane.setLayout( new BoxLayout( voreStatPane, BoxLayout.Y_AXIS ) );
		{
			JLabel vAtkLbl = new JLabel();
			vAtkLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vAtkLbl.setBackground( details.getBackground() );
			int vab = getVAttaqueBonus(0);
			vAtkLbl.setText( "  Vore Attack : " + getVAttaqueBase(0) + (vab >= 0 ? " + " : " - ") + Math.abs( vab ) );
			voreStatPane.add( vAtkLbl );
			
			JLabel vDefLbl = new JLabel();
			vDefLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vDefLbl.setBackground( details.getBackground() );
			int vdb = getVDefenseBonus();
			vDefLbl.setText( "  Vore Defense : " + getVDefenseBase() + (vdb >= 0 ? " + " : " - ") + Math.abs( vdb ) );
			voreStatPane.add( vDefLbl );
			
			JLabel vEscLbl = new JLabel();
			vEscLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vEscLbl.setBackground( details.getBackground() );
			vEscLbl.setText( "  Vore Escape : " + getVEscapeBase() );
			voreStatPane.add( vEscLbl );
			
			
			JLabel vStrLbl = new JLabel();
			vStrLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vStrLbl.setBackground( details.getBackground() );
			vStrLbl.setText( "  Stomach Muscles : " + getVResistance() );
			voreStatPane.add( vStrLbl );
			
			JLabel vAcdLbl = new JLabel();
			vAcdLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vAcdLbl.setBackground( details.getBackground() );
			int adb = aDommageBonus;
			vAcdLbl.setText( "  Stomach Acids : " + aDommageMin + "-" + aDommageMax + (adb >= 0 ? " + " : " - ") + Math.abs( adb ) );
			voreStatPane.add( vAcdLbl );
			
			JLabel vSpdLbl = new JLabel();
			vSpdLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vSpdLbl.setBackground( details.getBackground() );
			vSpdLbl.setText( "  Stomach Time : " + digTimeMax );
			voreStatPane.add( vSpdLbl );
		}
		details.add( voreStatPane );
	}
	
	@Override // GameObject
	public void cleanupView()
	{
		m_hpBar = null;
		m_mpBar = null;
		m_xpBar = null;
		m_statusText = null;
		m_predText = null;
		m_preyText = null;
		m_strLbl = null;
		m_dexLbl = null;
		m_endLbl = null;
		m_intLbl = null;
		m_chrLbl = null;
		m_resLbl = null;
	}
	
	@Override // GameObject
	public void updateView()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();

		int lastXP = (int)Util.levelCost( niveau - 1 );
		int nextXP = (int)Util.levelCost( niveau );
		int curXP = (int)(experience);

		m_xpBar.setText( getLevelString() );
		m_xpBar.setMaximum( nextXP - lastXP );
		m_xpBar.setValue( curXP - lastXP );

		m_hpBar.setText( nf.format(pv) + " / " + nf.format(pvMax) + " Health" );
		m_hpBar.setMaximum( pvMax );
		m_hpBar.setValue( pv );
		
		m_mpBar.setVisible( pmMax > 0 );
		m_mpBar.setText( nf.format(pm) + " / " + nf.format(pmMax) + " Mana" );
		m_mpBar.setMaximum( pmMax );
		m_mpBar.setValue( pm );
			
		// Targets come in four types: Player, Ally, Friendly, and Hostile.
		if( VGame.instance.determinationEnnemi( mere ) )
		{
			m_typeLbl.setToolTipText( "Hostile" );
			m_typeLbl.setIcon( Util.getIcon("flag_red") );
		}
		else if( mere == VGame.instance.getPlayer() )
		{
			m_typeLbl.setToolTipText( "Player" );
			m_typeLbl.setIcon( Util.getIcon("banner_green") );
		}
		else if( mere.type == FenetreTest.NPC )
		{
			m_typeLbl.setToolTipText( "Ally" );
			m_typeLbl.setIcon( Util.getIcon("flag_green") );
		}
		else
		{
			m_typeLbl.setToolTipText( "Neutral" );
			m_typeLbl.setIcon( Util.getIcon("flag_white") );
		}
		
		String str;
		str = getStatusString();
		m_statusText.setVisible( !str.isEmpty() );
		m_statusTextSpacer.setVisible( !str.isEmpty() );
		m_statusText.setText( getStatusString() );
		
		str = getPredString();
		m_predText.setVisible( !str.isEmpty() );
		m_predTextSpacer.setVisible( !str.isEmpty() );
		m_predText.setText( getPredString() );
		
		str = getPreyString();
		m_preyText.setVisible( !str.isEmpty() );
		m_preyTextSpacer.setVisible( !str.isEmpty() );
		m_preyText.setText( getPreyString() );
		
		int r = m_preyText.getRows();
		
		m_strLbl.setText( nf.format(strength) );
		m_dexLbl.setText( nf.format(dexterity) );
		m_endLbl.setText( nf.format(endurence) );
		m_intLbl.setText( nf.format(intelligence) );
		m_chrLbl.setText( nf.format(charisma) );
		
		for( int i = 0; i < Util.NUM_DAMAGE_TYPES; ++i )
		{
			if( resistance[i] == 0 )
			{
				m_resLbl[i].setText( "--" );
				m_resLbl[i].setForeground( Color.GRAY );
			}
			else
			{
				m_resLbl[i].setText( nf.format(resistance[i]) );
				m_resLbl[i].setForeground( resistance[i] < 0 ? Color.RED : Color.BLUE );
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Object
	public String toString()
	{
		return getName() + " (Lv " + niveau + ")";
	}

	@Override // GameObject
	public void dispose()
	{
		FenetreTest.instance.removeTarget( this );
		super.dispose();
	}

	@Override // Comparable
	public int compareTo( Object o )
	{
		StatChar os = (StatChar)o;
		int t, ot;

		FenetreTest ft = FenetreTest.instance;
		ObjetGraphique plr = ft.getPlayer();

		if( plr != null )
		{
			// Primary sort on isPlayer, reversed (players come first)
			t = mere == plr ? 1 : 0;
			ot = os.mere == plr ? 1 : 0;
			if( t != ot )
			{
				return -(t - ot);
			}
		}

		// Secondary sort on isNPC, reversed (NPCs come first)
		t = mere.type == FenetreTest.NPC ? 1 : 0;
		ot = os.mere.type == FenetreTest.NPC ? 1 : 0;
		if( t != ot )
		{
			return -(t - ot);
		}

		// Tertiary sort on isHostile, reversed (hostiles come first)
		t = ft.determinationEnnemi( plr, mere ) ? 1 : 0;
		ot = ft.determinationEnnemi( plr, os.mere ) ? 1 : 0;
		if( t != ot )
		{
			return -(t - ot);
		}

		// Final sort on level, reversed (high levels come first)
		return -(niveau - os.niveau);
	}
}
