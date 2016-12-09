///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Armure.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.text.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class ArmureData implements Serializable
{
	public int ArmureID; // Key field (must come first)
	
	public String Nom;
	public String Description;
	public String ArmureImage; // Left as a string (parsed later)
	
	public String ArmureIcon;
	
	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int Price;
	public int TransformID;
	
	public int QualiteMax;
	public int Defense;
	public int ResPhysical;
	public int ResFire;
	public int ResIce;
	public int ResAcid;
	public int ResLightning;
	public int ResPositive;
	public int ResNegative;
	public int ResNeutral;
	public int ResCharm;
	public int ResParalysis;
	public int ResSleep;
	public int ResCurse;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Armure extends ObjEquipment
{
	public int qualite;
	public int qualiteMax;
	public int defense;
	public int[] resistance;
	public String armureImage;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Armure(int armureID, int bonusID, boolean equ, int val, PlayerStat m)
	{
		this(armureID,bonusID,m);
		qualite=val;
		equipped = equ;
		if(equipped)
		{
			mere.armure=this;
			mere.changerEquipement();
			if(bonusID>0)
				bonus.appliquerTempBonus();
		}
	}
	
	public Armure(int armureID, int bonusID,PlayerStat m)
	{
		super(m,armureID);
		resistance=new int[Util.NUM_DAMAGE_TYPES];
		type=Util.ITEM_ARMOR;
		if(bonusID!=0)
		{
			bonus=new Bonus(bonusID,m);
		}
		this.bonusID=bonusID;

		loadFromData( DataUtil.getArmureData( armureID ) );

		qualite=qualiteMax;
	}
	
	public void loadFromData( ArmureData armureData )
	{
		setName( armureData.Nom );
		setImage( armureData.ArmureIcon );
		
		niveauR = armureData.NiveauR;
		strengthR = armureData.StrengthR;
		intelligenceR = armureData.IntelligenceR;
		endurenceR = armureData.EndurenceR;
		charismaR = armureData.CharismaR;
		dexterityR = armureData.DexterityR;
		qualiteMax = armureData.QualiteMax;
		defense = armureData.Defense;
		armureImage = armureData.ArmureImage;
		resistance[Util.DAMAGE_PHYSICAL] = armureData.ResPhysical;
		resistance[Util.DAMAGE_FIRE] = armureData.ResFire;
		resistance[Util.DAMAGE_ICE] = armureData.ResIce;
		resistance[Util.DAMAGE_ACID] = armureData.ResAcid;
		resistance[Util.DAMAGE_LIGHTNING] = armureData.ResLightning;
		resistance[Util.DAMAGE_POSITIVE] = armureData.ResPositive;
		resistance[Util.DAMAGE_NEGATIVE] = armureData.ResNegative;
		resistance[Util.DAMAGE_NEUTRAL] = armureData.ResNeutral;
		resistance[Util.DAMAGE_CHARM] = armureData.ResCharm;
		resistance[Util.DAMAGE_PARALYSIS] = armureData.ResParalysis;
		resistance[Util.DAMAGE_SLEEP] = armureData.ResSleep;
		resistance[Util.DAMAGE_CURSE] = armureData.ResCurse;
		price = armureData.Price;
		description = armureData.Description;
		transformID = armureData.TransformID;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjetInventaire
	public TotalBonus getTotalBonus()
	{
		TotalBonus bt = super.getTotalBonus();
		
		bt.add( resistance );
		
		return bt;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private IconBar m_qualBar = null;
	private JLabel m_defLbl = null;
	
	@Override // ObjetInventaire
	public void initializeView( JPanel vitals, JPanel details )
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		super.initializeView( vitals, details );
		
		m_defLbl = new JLabel();
		m_defLbl.setText( nf.format(defense) + " Defense" );
		m_defLbl.setIcon( Util.getIcon("stat_defense") );
		m_defLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		vitals.add( m_defLbl );
		
		if( qualiteMax != -1000 )
		{
			m_qualBar = new IconBar();
			m_qualBar.setAlignmentX( Component.LEFT_ALIGNMENT );
			m_qualBar.setIcon( Util.getIcon("stat_durability") );
			m_qualBar.setForegroundList( Util.COLOR_DAMAGE_FORE );
			m_qualBar.setBackgroundList( Util.COLOR_DAMAGE_BACK );
			m_qualBar.setText( nf.format(qualite) + " / " + nf.format(qualiteMax) + " Durability" );
			m_qualBar.setMaximum( qualiteMax );
			m_qualBar.setValue( qualite );
			vitals.add( m_qualBar );
		}
		else
		{
			JLabel qualLbl = new JLabel();
			qualLbl.setText( "Indestructable" );
			qualLbl.setIcon( Util.getIcon("stat_durability") );
			qualLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vitals.add( qualLbl );
		}
	}
	
	@Override // ObjetInventaire
	public void cleanupView()
	{
		super.cleanupView();
		
		m_qualBar = null;
		m_defLbl = null;
	}
	
	@Override // ObjetInventaire
	public void updateView()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		super.updateView();
		
		if( m_qualBar != null )
		{
			m_qualBar.setText( nf.format(qualite) + " / " + nf.format(qualiteMax) + " Durability" );
			m_qualBar.setMaximum( qualiteMax );
			m_qualBar.setValue( qualite );
		}

		if( m_defLbl != null )
		{
			m_defLbl.setText( nf.format(defense) + " Defense" );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjetInventaire
	public void use()
	{
		super.use();
		if(equipped)
		{
		  unequip();
		}
		else if(testRequirements())
		{
			if(mere.armure!=null)
				mere.armure.unequip();
			if(mere.armure==null)
			{
				mere.armure=this;
				equipped=true;
				mere.changerEquipement();
				if(bonus!=null)
					bonus.appliquerBonus();
				for(int i=0;i<12;i++)
				{
						mere.resistance[i]+=resistance[i];
				}
			}
		}
	}
	
	@Override // ObjetInventaire
	public void unequip()
	{
		if( mere.armure == this )
		{
			mere.armure=null;
			if(bonus!=null)
				bonus.enleverBonus();
			
			for(int i=0;i<12;i++){
						mere.resistance[i]-=resistance[i];
				}
			
			mere.changerEquipement();
		}
		equipped=false;
	}

	@Override // ObjetInventaire
	public int getValeur()
	{
		return qualite;
	}
}
