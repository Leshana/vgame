///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Arme.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class ArmeData implements Serializable
{
	public int ArmeID; // Key field (must come first)
	
	public String Nom;
	public String Description;
	public String ArmeImage; // Left as a string - it gets parsed on load.
	public String SonAttaque;
	
	public String ArmeIcon;
	
	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int QualiteMax;
	public int DommageMin;
	public int DommageMax;
	public int DommageBonus;
	public int DommageType;
	public int TypeArme;
	public int Price;
	public int Range;
	public int ProjectileID;
	public int EventID;
	public int TransformID;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Arme extends ObjEquipment
{	
	public int qualite;
	public int range;
	public int qualiteMax;
	public int dommageMin;
	public int dommageMax;
	public int dommageBonus;
	public int dommageType;
	public String armeImage;
	public int typeArme;
	public String sonAttaque;
	public AudioClip isonAttaque;
	public int projectileID;
	public Image imageA, imageS, imageSw;
	public int eventID;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Arme(int armeID, int bonusID, boolean equ, int val, PlayerStat m)
	{
		this(armeID,bonusID,m);
		qualite=val;
		
		equipped = equ;
		if(equipped)
		{
			mere.arme=this;
			loadImage();
			if(bonusID>0)
			{
				bonus.appliquerTempBonus();
			}
			//mere.changerEquipement();
		}
	}
	
	public Arme(int armeID, int bonusID, PlayerStat m)
	{
		super(m,armeID);
		type=Util.ITEM_WEAPON;
		if(bonusID!=0)
		{
			bonus=new Bonus(bonusID,m);
		}
		this.bonusID=bonusID;

		loadFromData( DataUtil.getArmeData( armeID ) );
	}
	
	public void loadFromData( ArmeData armeData )
	{	
		setName( armeData.Nom );
		setImage( armeData.ArmeIcon );
		
		niveauR = armeData.NiveauR;
		strengthR = armeData.StrengthR;
		intelligenceR = armeData.IntelligenceR;
		endurenceR = armeData.EndurenceR;
		charismaR = armeData.CharismaR;
		dexterityR = armeData.DexterityR;
		qualiteMax = armeData.QualiteMax;
		dommageMin = armeData.DommageMin;
		dommageMax = armeData.DommageMax;
		dommageBonus = armeData.DommageBonus;
		dommageType = armeData.DommageType;
		armeImage = armeData.ArmeImage;
		typeArme = armeData.TypeArme;
		sonAttaque = armeData.SonAttaque;
		price = armeData.Price;
		range = armeData.Range;
		description = armeData.Description;
		projectileID = armeData.ProjectileID;
		eventID = armeData.EventID;
		transformID = armeData.TransformID;
		
		loadImage();
	
		qualite=qualiteMax;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int calculDommage()
	{
		Random hasard=new Random();
		return hasard.nextInt(dommageMax-dommageMin)+dommageMin+dommageBonus;
	}

	public int getValeur()
	{
		return qualite;
	}
	
	public void loadImage()
	{
		if(!armeImage.equalsIgnoreCase("_f"))
		{
			int firstDot = armeImage.indexOf(".");
			String chaine=armeImage.substring(0, firstDot);
			String fin=armeImage.substring(firstDot, armeImage.length());
			
			imageA=ImageLibrairies.getImage(chaine+"A"+fin);
			imageS=ImageLibrairies.getImage(chaine+"S"+fin);
			imageSw=ImageLibrairies.getImage(chaine+"Sw"+fin);
		}
		
		isonAttaque=AudioLibrairie.getClip(sonAttaque);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjetInventaire
	public String getTypeString()
	{
		return Util.WEAPON_TYPES[typeArme].name;
	}
	
	public String getDamageString()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		return nf.format(dommageMin + dommageBonus)+" to " + nf.format(dommageMax + dommageBonus) +
				" " + Util.DAMAGE_TYPES[dommageType].name;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private IconBar m_qualBar = null;
	private JLabel m_dmgLbl = null;
	
	@Override // ObjetInventaire
	public void initializeView( JPanel vitals, JPanel details )
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		super.initializeView( vitals, details );
		
		m_dmgLbl = new JLabel();
		m_dmgLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		m_dmgLbl.setText( getDamageString() + " Damage" );
		m_dmgLbl.setIcon( Util.DAMAGE_TYPES[dommageType].icon );
		vitals.add( m_dmgLbl );
		
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
			m_qualBar = null;
			JLabel qualLbl = new JLabel();
			qualLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			qualLbl.setText( "Indestructable" );
			qualLbl.setIcon( Util.getIcon("stat_durability") );
			vitals.add( qualLbl );
		}
	}
	
	@Override // ObjetInventaire
	public void cleanupView()
	{
		super.cleanupView();
		
		m_qualBar = null;
		m_dmgLbl = null;
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
		
		if( m_dmgLbl != null )
		{
			// Can 
			m_dmgLbl.setText( getDamageString() + " Damage" );
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
			if(mere.arme!=null)
				mere.arme.unequip();
			if(mere.arme.objetID==1)
			{
				mere.arme=this;
				equipped=true;
				//mere.changerEquipement();
				loadImage();
				if(bonus!=null)
					bonus.appliquerBonus();
			}
		}
	}

	@Override // ObjetInventaire
	public void unequip()
	{
		if( mere.arme == this )
		{
			mere.arme=new Arme(1,0,mere);
			if(bonus!=null)
				bonus.enleverBonus();
		}

		loadImage();			
		equipped = false;
	}
}
