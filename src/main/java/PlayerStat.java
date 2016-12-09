///////////////////////////////////////////////////////////////////////////////////////////////////
//
// PlayerStat.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

class PlayerStatData implements Serializable
{
	public int PlayerID;

	public String Nom;
	public String Picture;
	
	public int PVMax;
	public int PMMax;
	
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
	public String sonU;
	public String sonB;
	public String sonAnl;
	 
	public int Deplacement;
	public int digTimeMax;
	public int VOnly;
	public long Experience;
	public int Temps;
	public int Nourriture;
	public int Money;
	public int ArmeID;
	public int ArmureID;
	public int BagueID;
	public int AmuletID;
	public int DigTime;
	public int Famine;
	public int BonusArme;
	public int BonusArmure;
	public int BonusBague;
	public int BonusAmulet;
	public int PV;
	public int PM;
	public int Dung;
	public int Dung2;
	public int udung;
	public int VDefense;
	public String Family;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class PlayerStat extends StatChar
{
	private Vector<ObjInventaire> inventaire;
	public Vector<Evenement> listeEvent;
	public int nourriture;
	public Clock clock;
	public int money;
	
	public Arme arme;
	public Armure armure;
	public Bague bague;
	public Amulet amulet;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public PlayerStat(int carID, ObjetGraphique m, int side)
	{
		super(m);
		mere.setFootprint( 1 );
		
		bonus=new Vector<Bonus>();
		victimes=new Vector<VHold>();
		inventaire=new Vector<ObjInventaire>();
		listeEvent=new Vector<Evenement>();
		resistance=new int[12];
		
		this.side=side;
		actualSide=side;
		vOnly=0;
		status=0;
	 
		iD=carID;
		this.carID=carID;
		
		PlayerStatData data = DataUtil.getPlayerStatData( carID );
		
		if (data != null)
		{
			setName( data.Nom );
			setImage( data.Picture );
			
			pvMax=data.PVMax;
			pmMax=data.PMMax;
			
			strength=data.Strength;
			intelligence=data.Intelligence;
			dexterity=data.Dexterity;
			endurence=data.Endurence;
			charisma=data.Charisma;
			niveau=data.Niveau;
			nbAttack=data.nbAttaque;
			defense=data.Defense;
			resistance[Util.DAMAGE_FIRE]=data.ResFire;
			resistance[Util.DAMAGE_ICE]=data.ResIce;
			resistance[Util.DAMAGE_ACID]=data.ResAcid;
			resistance[Util.DAMAGE_LIGHTNING]=data.ResLightning;
			resistance[Util.DAMAGE_POSITIVE]=data.ResPositive;
			resistance[Util.DAMAGE_NEGATIVE]=data.ResNegative;
			resistance[Util.DAMAGE_NEUTRAL]=data.ResNeutral;
			resistance[Util.DAMAGE_PHYSICAL]=data.ResPhysical;
			resistance[Util.DAMAGE_CHARM]=data.ResCharm;
			resistance[Util.DAMAGE_PARALYSIS]=data.ResParalysis;
			resistance[Util.DAMAGE_SLEEP]=data.ResSleep;
			resistance[Util.DAMAGE_CURSE]=data.ResCurse;
			
			aDommageMin=data.AcidDamageMin;
			aDommageMax=data.AcidDamageMax;
			aDommageBonus=data.AcidDamageBonus;
			vDifficulte=data.VDifficulty;
			vEscapeBonus=data.VEscape;
			sonV=data.SonSw;
			sonMort=data.SonDeath;
			sonD1=data.SonD1;
			sonD2=data.SonD2;
			sonD3=data.SonD3;
			sonB1=data.SonB1;
			sonB2=data.SonB2;
			sonB3=data.SonB3;
			sonS1=data.SonS1;
			sonS2=data.SonS2;
			sonS3=data.SonS3;
			sonC=data.SonCrap;
			sonVo=data.SonVom;
			sonU=data.sonU;
			sonB=data.sonB;
			sonAnl=data.sonAnl;
			 
			//deplacement=data.Deplacement;
			digTimeMax=data.digTimeMax;
			vOnly=data.VOnly;
			experience=data.Experience;
			int minutes = data.Temps;
			nourriture=data.Nourriture;
			money=data.Money;
			int armeID=data.ArmeID;
			int armureID=data.ArmureID;
			int bagueID=data.BagueID;
			int amuletID=data.AmuletID;
			//digTime=data.DigTime;
			famine=data.Famine;
			int bonusArmeID=data.BonusArme;
			int bonusArmureID=data.BonusArmure;
			int bonusBagueID=data.BonusBague;
			int bonusAmuletID=data.BonusAmulet;
			pv=data.PV;
			pm=data.PM;
			dung=data.Dung;
			dung2=data.Dung2;
			udung=data.udung;
			vDefense=data.VDefense;
			family=data.Family;

			if(armeID!=0)
				setArme(armeID,bonusArmeID);
			if(armureID!=0)
				setArmure(armureID,bonusArmureID);
			if(bagueID!=0)
				setBague(bagueID,bonusBagueID);
			if(amuletID!=0)
				setAmulet(amuletID,bonusAmuletID);
		   
			clock = new Clock( 1, 1, 1, 0, minutes );
		}

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
	}
	
	public PlayerStat( SaveCharacter saveChar )
	{
		bonus=new Vector<Bonus>();
		victimes=new Vector<VHold>();
		inventaire=new Vector<ObjInventaire>();
		listeEvent=new Vector<Evenement>();
		
		setName( saveChar.Nom );
		
		iD = saveChar.StatID;
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
		damageBonus= saveChar.DamageBonus;
		actualSide= saveChar.ActualSide;
		vBonus= saveChar.vBonus;
		absorption= saveChar.Absorption;
		vDefense= saveChar.VDefense;
		size= saveChar.Size;
		polymorph= saveChar.Polymorph;
		side= saveChar.Side;
		regeneration= saveChar.Regeneration;
		
		clock= new Clock( saveChar.Temps );
		money= saveChar.MoneyP;
		nourriture= saveChar.Nourriture;
		famine= saveChar.famine;
		
		PlayerStatData data = DataUtil.getPlayerStatData( iD );
		
		if (data != null)
		{
			setImage( data.Picture );

			sonV=data.SonSw;
			sonMort=data.SonDeath;
			sonD1=data.SonD1;
			sonD2=data.SonD2;
			sonD3=data.SonD3;
			sonB1=data.SonB1;
			sonB2=data.SonB2;
			sonB3=data.SonB3;
			sonS1=data.SonS1;
			sonS2=data.SonS2;
			sonS3=data.SonS3;
			sonC=data.SonCrap;
			sonVo=data.SonVom;
			dung=data.Dung;
			vOnly=data.VOnly;
			family=data.Family;
			dung2=data.Dung2;
			sonU=data.sonU;
			sonB=data.sonB;
			udung=data.udung;
			sonAnl=data.sonAnl;
		}
		
		this.carID=iD;

		for (int i = 0; i < saveChar.Bonus.size(); ++i)
		{
			bonus.addElement( new Bonus( saveChar.Bonus.elementAt( i ), this ) );
		}
	}
	
	public PlayerStat( SaveCharacter saveChar, ObjetGraphique m )
	{
		this( saveChar );
		mere = m;
		mere.setFootprint( 1 );
		
		setArme(1, 0);
		reload();
	}
	
	public void loadInventory( Vector<SaveItem> items )
	{
		inventaire=new Vector<ObjInventaire>();
		Iterator<SaveItem> it = items.iterator();
		while (it.hasNext())
		{
			ObjInventaire newItem = ObjInventaire.load( it.next(), this );

			newItem.setNew( false );
			addObjInventaire( newItem, false, false );
		}
	}
	
	public void loadEvents( Vector<SaveEvent> events )
	{
		listeEvent=new Vector<Evenement>();
		Iterator<SaveEvent> it = events.iterator();
		while (it.hasNext())
		{
			listeEvent.add( new Evenement( it.next() ) );
		}
	}
	
	@Override
	public SaveCharacter save()
	{
		SaveCharacter ret = super.save();
		
		ret.Temps = clock.getTime();
		ret.MoneyP = money;
		ret.Nourriture = nourriture;
		ret.famine = famine;
		
		return ret;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setArme(int armeId, int bonusId){
		arme=new Arme(armeId, bonusId, this);
	}
	public void setArmure(int armureId, int bonusId){
		armure=new Armure(armureId, bonusId,this);
	}
	public void setBague(int bagueId, int bonusId){
		bague=new Bague(bagueId, bonusId,this);
	}
	public void setAmulet(int amuletId, int bonusId){
		amulet=new Amulet(amuletId, bonusId,this);
	}

	public void addObjInventaire(int objetID, int bonusID, int typeObj, boolean use){
		ObjInventaire obj;
		switch(typeObj){
			case Util.ITEM_AMULET:
				obj=new Amulet(objetID,bonusID,this);
				break;
			case Util.ITEM_ARMOR:
				obj=new Armure(objetID,bonusID,this);
				break;
			case Util.ITEM_WEAPON:
				obj=new Arme(objetID,bonusID,this);
				break;
			case Util.ITEM_RING:
				obj=new Bague(objetID,bonusID,this);
				break;
			case Util.ITEM_QUEST:
				obj=new Quest(objetID,bonusID,this);
				break;
			case Util.ITEM_POTION:
				obj=new Potion(objetID,this);
				break;
			case Util.ITEM_BOOK:
				obj=new Livre(objetID,this);
				break;
			case Util.ITEM_USABLE:
				obj=new Usable(objetID,this);
				break;
			default:
				obj=new Food(objetID,this);
				break;
		}
		
		addObjInventaire( obj, use, true );
	}
	
	public void addObjInventaire( ObjInventaire ob, boolean use, boolean announce )
	{
		if(use)
		{
			ob.use();
		}

		if( announce )
		{
			FenetreTest.instance.printMessage("You find a " + ob);
		}
		
		// Insert into our inventory.
		boolean stacked = false;
		for( int i = 0; i < inventaire.size(); ++i )
		{
			ObjInventaire temp = inventaire.elementAt( i );
			if( temp.canStack( ob ) )
			{
				temp.stack( ob );
				stacked = true;
				break;
			}
		}

		if( !stacked )
		{
			// If it's a new item, add it to the inventory window (It might get stacked above).
			inventaire.add(ob);
			FenetreTest.instance.inventaire.addElement(ob);
		}
	}
	
	public void removeObjInventaire(ObjInventaire ob)
	{
		ObjInventaire next = ob.getStack();
		if( ob.unStack() )
		{
			// If we're currently selected, we may need to deal with that below.
			int idx = FenetreTest.instance.inventaire.findElement( ob );
			
			if( idx >= 0 )
			{
				boolean selected = FenetreTest.instance.inventaire.getSelectedIndex() == idx;
				
				// This object was the first one in the stack, so remove it from our lists.
				inventaire.remove(ob);
				FenetreTest.instance.inventaire.removeElement(ob);
				ob.dispose();
			
				if( next != null )
				{
					// If we've just removed the first item, and there is more to the stack, re-insert
					//  the 'next' item on the stack (which is now the first).
					inventaire.add(next);
					FenetreTest.instance.inventaire.addElementAt(next,idx);
					
					// If the object we just removed was the selected item in the inventory list, we
					//  need to re-select it.
					if( selected )
					{
						FenetreTest.instance.inventaire.setSelectedIndex( idx );
					}
				}
			}
		}
	}
	
	public void removeObjInventaire(int objetID, int typeObj)
	{
		ObjInventaire temp;
		for(int i=0;i<inventaire.size();i++)
		{
			temp=(ObjInventaire)inventaire.elementAt(i);
			if(temp.objetID==objetID && temp.type==typeObj)
			{
				removeObjInventaire( temp );
				i--;
			}
		}
	}
	public void removeOneObjInventaire(int objetID, int typeObj)
	{
		ObjInventaire temp;
		for(int i=0;i<inventaire.size();i++)
		{
			temp=(ObjInventaire)inventaire.elementAt(i);
			if(temp.objetID==objetID && temp.type==typeObj)
			{
				removeObjInventaire( temp );
				break;
			}
		}
	}
	
	public void giveMoney(int mo){
		money+=mo;
		if(money<0)
			money=0;
		if(mo>0)
			FenetreTest.instance.printMessage("You find "+mo+" gold");
	}
	public void passeTemps(int t){
		super.passeTemps(t);
		clock.addTime( t );
		
		// Decrement food based on hunger and time
		if(famine>0){
			nourriture-=famine*t;
		}
		
		// Cap min-food at -10 times the player level
		if(nourriture<-10 * niveau)
			nourriture=-10 * niveau;
			
		// If you're below 0 food, deal damage to the player, down to 1 health.
		
		if(nourriture<0 && pv>0)
			if( pv > -nourriture )
				pv+=nourriture;
			else
				pv = 1;
		FenetreTest.instance.repaint();
	}
	
	public void nouveauEvent(String nom,int compteur){
		listeEvent.add(new Evenement(clock,compteur,nom));
	}
	
	public void removeEvent(String nom){
		int i;
		Evenement temp=null;
		for(i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				break;
			}
		}
		if(temp==null)
			return;
		if(i==listeEvent.size())
			return;
		listeEvent.removeElementAt(i);
	}
	
	public boolean testEventCompteur(String nom, int valeur, int typeTest){
		boolean res=false;
		int i;
		Evenement temp=null;
		for(i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				break;
			}
		}
		if(temp==null)
			return false;
		if(i==listeEvent.size())
			return false;
		switch(typeTest){
			case 0:
				  if(temp.compteur==valeur)
					  res=true;
				  break;
			case 1:
				if(temp.compteur>valeur)
					res=true;
				break;
			case 2:
				if(temp.compteur<valeur)
					res=true;
				break;
		}
		
		return res;
	}
	
	public boolean testEventTemps(String nom, int temps,int jour, int mois, int annee, int typeTest){
		boolean res=false;
		Evenement temp=null;
		int i;
		for(i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				break;
			}
		}
		if(temp==null)
			return false;
		if(i==listeEvent.size())
			return false;

		Clock dclock = new Clock( clock.getTime() - temp.clock.getTime() );
		
		switch(typeTest)
		{
		case 0:
			if( (temp.clock.getDayMinutes()==temps || temps<0)
				&& (temp.clock.getDays()==jour || jour<0)
				&& (temp.clock.getMonths()==mois || mois<0)
				&& (temp.clock.getYears()==annee || annee<0) )
			{
				res=true;
			}
			break;
		case 1:
			if( (temp.clock.getDayMinutes()>temps || temps<0)
				&& (temp.clock.getDays()>jour || jour<0)
				&& (temp.clock.getMonths()>mois || mois<0)
				&& (temp.clock.getYears()>annee || annee<0) )
				  res=true;
			break;
		case 2:
			if( (temp.clock.getDayMinutes()<temps || temps<0)
				&& (temp.clock.getDays()<jour || jour<0)
				&& (temp.clock.getMonths()<mois || mois<0)
				&& (temp.clock.getYears()<annee || annee<0) )
			{
				  res=true;
			}
			break;
		case 3:
			if( (dclock.getDayMinutes()<temps || temps<0)
				&& (dclock.getMonths()<mois || mois<0)
				&& (dclock.getDays()<jour || jour<0)
				&& (dclock.getYears()<annee || annee<0) )
			{
				res=true;
			}
			break;
		case 4:
			if( (dclock.getDayMinutes()>=temps || temps<0)
				&& (dclock.getMonths()>=mois || mois<0)
				&& (dclock.getDays()>=jour || jour<0)
				&& (dclock.getYears()>=annee || annee<0) )
			{
				res=true;
			}
		}
		
		return res;
	}
	
	public void modificationEvent(String nom, int compteur){
		Evenement temp;
		for(int i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				temp.compteur=compteur;
				break;
			}
		}
	}
	public void incrementationEvent(String nom,int compteur){
		Evenement temp;
		for(int i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				temp.compteur+=compteur;
				break;
			}
		}
	}

	public int getDefense(){
		if(armure!=null){
			return super.getDefense()+armure.defense;
		}
		return super.getDefense();
	}
	
	protected void actualiserPV()
	{
		pvMax+=endurence*10;
		pmMax+=intelligence*3;
		pv+=endurence*10;
		pm+=intelligence*3;
	}
	public int baseAttaque()
	{
		switch(arme.typeArme){
			case Util.WEAPON_SWORD:
				return (strength+dexterity+niveau)/2;
			case Util.WEAPON_AXE:    
				return (2*strength+niveau)/3;
			case Util.WEAPON_MACE:    
				return (strength+endurence+niveau)/2;
			case Util.WEAPON_SPEAR:    
				return dexterity+niveau/2;
			case Util.WEAPON_DAGGER:    
				return dexterity+niveau/2;
			case Util.WEAPON_STAFF:    
				return (strength+dexterity+niveau)/2;
			case Util.WEAPON_BOW:
				return dexterity+niveau/2;
			case Util.WEAPON_HAND:
				return strength+niveau/2;
		}
		return 0;
	}
	public int baseDommage(){
		switch(arme.typeArme){
			case Util.WEAPON_SWORD:
				return (strength+dexterity)/4;
			case Util.WEAPON_AXE:    
				return 2*strength/3;
			case Util.WEAPON_MACE:    
				return strength/2;
			case Util.WEAPON_SPEAR:    
				return dexterity/2;
			case Util.WEAPON_DAGGER:    
				return dexterity/4;
			case Util.WEAPON_STAFF:    
				return strength/4;
			case Util.WEAPON_BOW:    
				return dexterity/2;
			case Util.WEAPON_HAND:    
				return strength/4;
		}
		return 0;
	}
	public int faireDommage(){
		return arme.calculDommage();
	}
	public int faireDommage(int a){
		return arme.calculDommage()+damageBonus;
	}
	public int typeDommage(){
		return arme.dommageType;
	}
	public int typeDommage(int a){
		return arme.dommageType;
	}
	public int getMoney(){
		return money;
	}
	public boolean verifyObjet(int objetID, int typeObj){
		ObjInventaire temp;
		for(int i=0;i<inventaire.size();i++){
			temp=(ObjInventaire)inventaire.elementAt(i);
			if(temp.objetID==objetID && temp.type==typeObj)
				return true;
		}
		return false;
	}
		
	public int getVAttaqueBonus(int i){
		int bo = vBonus;
		switch(size){
			case 1:
				bo-=60;
				break;
			case 2:
				bo+=40;
				break;
		}
		bo -= victimes.size() * 10;
		bo -= nourriture / 100;
		if(nourriture<500)
			bo+=10;
		if(nourriture<100)
			bo+=20;
		
		return bo;
	}
	
		
	public void giveFood(int nut){
		nourriture+=nut;
	}
	public String getSonAttaque(int i){
		return arme.sonAttaque;
	}
	public AudioClip getSonAttaqueInt(int i){
		return arme.isonAttaque;
	}
	public int getRangeAttaque(int i){
		return arme.range;
	}
	public void reload(){
		super.reload();
		arme.isonAttaque=AudioLibrairie.getClip(arme.sonAttaque);
		arme.loadImage();
	}
	public boolean appliquerDommage(int dommage, int typeDommage){
		boolean reponse=super.appliquerDommage(dommage, typeDommage);
		if(typeDommage==Util.DAMAGE_ACID){
			if(arme.qualiteMax>0){
				arme.qualite-=dommage/5;
				if(arme.qualite<1)
					arme.dispose();
			}
			if(armure!=null && armure.qualiteMax>0){
				armure.qualite-=dommage/5;
				if(armure.qualite<1)
					armure.dispose();
			}
				   
		}
		return reponse;
	}
	public void changerEquipement(){
		String arm="_";
		if(armure!=null)
			arm=armure.armureImage;
		mere.changerChaine(arm);
	}
	public void polymorph(int nombre){
		if(armure!=null)
			armure.unequip();
		arme.unequip();
		super.polymorph(nombre);
	}
	
	public void transform(int nombre){
		if(armure!=null)
			armure.unequip();
		arme.unequip();
		if(amulet!=null)
			amulet.unequip();
		if(bague!=null)
			bague.unequip();
		super.transform(nombre);
	}
	
	public int getVDefenseBonus(){
		int b = super.getVDefenseBonus();
		if(armure == null)
		  b -= 20;
		
		return b;
	}
	
	public void giveExp(long exp){
		super.giveExp(exp);
		FenetreTest.instance.printMessage("You gain "+exp+" XP");
	}
	public boolean testEventMasque(String nom,int valeur){
		boolean res=false;
		int i;
		Evenement temp=null;
		for(i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				break;
			}
		}
		if(temp==null)
			return false;
		if(i==listeEvent.size())
			return false;
		if(temp.compteur % Math.pow(2, valeur)>=Math.pow(2, valeur-1))
			   res=true;
		
		return res;
	}
	public void incrementEventMasque(String nom,int valeur){
		Evenement temp;
		boolean enleve=false;
		if(valeur<0){
			valeur=-valeur;
			enleve=true;
		}
		for(int i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(nom)){
				if(!testEventMasque(nom, valeur))
					temp.compteur+=Math.pow(2,valeur-1);
				else if(enleve){
					temp.compteur-=Math.pow(2,valeur-1);
				}
				break;
			}
		}
	}
	public Evenement getEvenement(String ev){
		Evenement temp;
		for(int i=0;i<listeEvent.size();i++){
			temp=(Evenement)listeEvent.elementAt(i);
			if(temp.nom.equalsIgnoreCase(ev)){
				return temp;
			}
		}
		return null;
	}
	
	public void restoreFamily()
	{
		PlayerStatData data = DataUtil.getPlayerStatData( carID );
		
		if (data != null)
		{
			family = data.Family;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getDescription()
	{
		String message="";

		message+="\n";
		message+="Attack : "+(baseAttaque()+attackBonus)+"\n";
		message+="Damage : "+(baseDommage()+damageBonus)+"\n";
		message+="Digestion : " + (aDommageMin + aDommageBonus) + " to " + (aDommageMax + aDommageBonus) + " Acid for " + digTimeMax + " rounds\n";
		message+="Vore Defense : "+getVDefenseBase()+"\n";
		message+="Vore Attack : "+getVAttaqueBase(0)+"\n";
		message+="Escape Bonus : "+getVEscapeBase()+"\n";
		message+="Stomach Muscles : "+getVResistance()+"\n";
		message+="Saving Throws : "+getSaveBase()+"\n";
		message+="Spellcasting : "+getSortBase()+"\n";
		message+="\n";
		message+="Food : "+nourriture+"\n";
		message+="Experience : "+experience+" ,Next Level : "+Util.levelCost(niveau)+"\n";
		message+="Gold : "+money+"\n";

		return message;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // StatChar
	protected void monterNiveau()
	{
		++niveau;

		if( niveau % 3 == 0 )
		{
			aDommageBonus++;
		}

		FenetreLevelUp fen= new FenetreLevelUp(this);
	}
	
	@Override // StatChar
	public Vector<String> getStatusList()
	{
		Vector<String> sl = super.getStatusList();

		if(arme!=null && arme.bonus!=null && arme.bonus.description!=null && !arme.bonus.description.trim().equalsIgnoreCase("")){
			sl.add( arme.bonus.description );
		}
		if(armure!=null && armure.bonus!=null && armure.bonus.description!=null && !armure.bonus.description.trim().equalsIgnoreCase("")){
			sl.add( armure.bonus.description );
		}
		if(bague!=null && bague.bonus!=null && bague.bonus.description!=null && !bague.bonus.description.trim().equalsIgnoreCase("")){
			sl.add( bague.bonus.description );
		}
		if(amulet!=null && amulet.bonus!=null && amulet.bonus.description!=null && !amulet.bonus.description.trim().equalsIgnoreCase("")){
			sl.add( amulet.bonus.description );
		}
		for(int i=0;i<inventaire.size();i++)
		{
			ObjInventaire temp=(ObjInventaire)inventaire.elementAt(i);
			if(temp.bonusID!=0 && temp.type==Util.ITEM_QUEST)
			{
				Quest temp2=(Quest)temp;
				if(temp2.bonus.description!=null && !temp2.bonus.description.trim().equalsIgnoreCase(""))
					sl.add( temp2.bonus.description );
			}
		}

		return sl;
	}
}
