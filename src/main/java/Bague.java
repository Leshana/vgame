///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Bague.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import javax.swing.*;
import java.awt.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class BagueData implements Serializable
{
	public int BagueID; // Key field (must come first)
		
	public String Nom;
	public String Description;
	
	public String BagueIcon;

	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int Price;
	public int TransformID;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Bague extends ObjEquipment
{
	public Bague(int bagueID, int bonusID, boolean equ, PlayerStat m)
	{
		this(bagueID,bonusID,m);

		equipped = equ;
		if(equipped)
		{
			mere.bague=this;
			if(bonusID>0)
				bonus.appliquerTempBonus();
		}
	}

	public Bague(int bagueID, int bonusID, PlayerStat m)
	{
		super(m, bagueID);

		type=Util.ITEM_RING;
		if(bonusID!=0){
			bonus=new Bonus(bonusID,m);
		}
		this.bonusID=bonusID;
		
		loadFromData( DataUtil.getBagueData( bagueID ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( BagueData bagueData )
	{
		setName( bagueData.Nom );
		setImage( bagueData.BagueIcon );
		
		niveauR = bagueData.NiveauR;
		strengthR = bagueData.StrengthR;
		intelligenceR = bagueData.IntelligenceR;
		endurenceR = bagueData.EndurenceR;
		charismaR = bagueData.CharismaR;
		dexterityR = bagueData.DexterityR;
		price = bagueData.Price;
		description = bagueData.Description;
		transformID = bagueData.TransformID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjetInventaire
	public void use(){
		super.use();
		if(equipped)
		{
		  unequip();
		}
		else if(testRequirements()){
			if(mere.bague!=null)
				mere.bague.unequip();
			if(mere.bague==null){
				mere.bague=this;
				equipped=true;
				if(bonus!=null)
					bonus.appliquerBonus();
			}
		}
	}
	
	@Override // ObjetInventaire
	public void unequip()
	{
		if( mere.bague == this )
		{
			mere.bague=null;
			if(bonus!=null)
				bonus.enleverBonus();
		}

		equipped=false;
	}
}
