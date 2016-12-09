///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Potion.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class PotionData implements Serializable
{
	public int PotionID; // Key field (must come first)
		
	public String Nom;
	public String Description;
	
	public String PotionIcon;

	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int Price;
	public int TransformID;

	public int TypePotion;
	public int Minimum;
	public int Maximum;
	public int BonusID;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Potion extends ObjInventaire
{
	public int typePotion;
	public int minimum;
	public int maximum;
	
	public static final int HP=0;
	public static final int MP=1;
	public static final int PERMANENT=2;
	public static final int TEMPORAIRE=3;
	public static final int POISON=4;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Potion(int potionID, PlayerStat m)
	{
		super(m, potionID);
		loadFromData( DataUtil.getPotionData( potionID ) );
	}

	public void loadFromData( PotionData potionData )
	{
		type=Util.ITEM_POTION;
		
		setName( potionData.Nom );
		setImage( potionData.PotionIcon );
		
		niveauR = potionData.NiveauR;
		strengthR = potionData.StrengthR;
		intelligenceR = potionData.IntelligenceR;
		endurenceR = potionData.EndurenceR;
		charismaR = potionData.CharismaR;
		dexterityR = potionData.DexterityR;
		price = potionData.Price;
		description = potionData.Description;
		transformID = potionData.TransformID;

		typePotion = potionData.TypePotion;
		minimum = potionData.Minimum;
		maximum = potionData.Maximum;
		bonusID = potionData.BonusID;
		
		if (bonusID > 0)
		{
			bonus = new Bonus( bonusID, mere );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int calculVariation()
	{
		Random hasard=new Random();
		return hasard.nextInt(maximum-minimum)+minimum;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void addLabel( JPanel pane, String icon, String text )
	{
		JLabel lbl = new JLabel();
		lbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		lbl.setText( text );
		if( icon != null )
		{
			lbl.setIcon( Util.getIcon( icon ) );
		}
		lbl.setBackground( pane.getBackground() );
		pane.add( lbl );
	}
	
	@Override // ObjInventaire
	public void initializeView( JPanel vitals, JPanel details )
	{
		super.initializeView( vitals, details );
		
		switch( typePotion )
		{
		case HP:
			addLabel( vitals, "stat_hp", "Restores " + minimum + " to " + maximum + " Health" );
			break;
		case MP:
			addLabel( vitals, "stat_mp", "Restores " + minimum + " to " + maximum + " Mana" );
			break;
		case PERMANENT:
			addLabel( vitals, "item_quest", "Permanent Effect" );
			break;
		case TEMPORAIRE:
			addLabel( vitals, "item_quest", "Lasts " + minimum + " to " + maximum + " Rounds" );
			break;
		case POISON:
			addLabel( vitals, "stat_hp", "Restores " + minimum + " to " + maximum + " Health" );
			break;
		}
	}
	
	@Override // ObjInventaire
	public String getUseString()
	{
		return "Drink";
	}
	
	@Override // ObjInventaire
	public boolean canStack( ObjInventaire ob )
	{
		return ob.type == type && ob.objetID == objetID;
	}
	
	@Override // ObjInventaire
	public void use()
	{
		super.use();
		if(testRequirements())
		{
			switch(typePotion)
			{
			case HP:
				mere.pv+=calculVariation();
				if(mere.pv>mere.pvMax)
					mere.pv=mere.pvMax;
				break;
			case MP:
				mere.pm+=calculVariation();
				if(mere.pm>mere.pmMax)
					mere.pm=mere.pmMax;
				break;
			case PERMANENT:
				bonus.appliquerBonus();
				break;
			case TEMPORAIRE:
				int bon=mere.isEnchanted(bonusID);
				if(bon!=-1){
					Bonus tem=(Bonus)mere.bonus.elementAt(bon);
					if(tem.stack>0)
						tem.tempsMax+=calculVariation();
					else
						mere.bonus.add(new Bonus(bonusID,mere,calculVariation()));
				}
				else            
					mere.bonus.add(new Bonus(bonusID,mere,calculVariation()));
				break;
			case POISON:
				mere.pv-=calculVariation();
				if(mere.pv<1)
					mere.pv=1;
				break;
			}
			dispose();
		}
	}
}
