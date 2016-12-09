///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Amulet.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class AmuletData implements Serializable
{
	public int AmuletID; // Key field (must come first)

	public String Nom;
	public String Description;
	
	public String AmuletIcon;
	
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
public class Amulet extends ObjEquipment
{
	public Amulet(int amuletID, int bonusID, boolean equ, PlayerStat m)
	{
		this(amuletID,bonusID,m);

		equipped = equ;
		if(equipped)
		{
			mere.amulet=this;
			if(bonusID>0)
				bonus.appliquerTempBonus();
		}
	}
	
	public Amulet(int amuletID, int bonusID, PlayerStat m)
	{
		super(m,amuletID);
		type = Util.ITEM_AMULET;

		if( bonusID != 0 )
		{
			bonus = new Bonus( bonusID, m );
		}
		
		this.bonusID = bonusID;
		
		loadFromData( DataUtil.getAmuletData( amuletID ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( AmuletData amuletData )
	{
		setName( amuletData.Nom );
		setImage( amuletData.AmuletIcon );

		niveauR = amuletData.NiveauR;
		strengthR = amuletData.StrengthR;
		intelligenceR = amuletData.IntelligenceR;
		endurenceR = amuletData.EndurenceR;
		charismaR = amuletData.CharismaR;
		dexterityR = amuletData.DexterityR;
		price = amuletData.Price;
		description = amuletData.Description;
		transformID = amuletData.TransformID;
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
			if(mere.amulet!=null)
				mere.amulet.unequip();
			if(mere.amulet==null){
				mere.amulet=this;
				equipped=true;
				if(bonus!=null)
					bonus.appliquerBonus();
			}
		}
	}
	
	public void unequip(){
		if( mere.amulet == this )
		{
			mere.amulet=null;
			if(bonus!=null)
				bonus.enleverBonus();
		}
		equipped=false;
	}
}