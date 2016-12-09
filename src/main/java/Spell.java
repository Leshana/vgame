///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Spell.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.applet.*;
import java.awt.*;
import java.text.*;
import javax.swing.*;

class SpellData implements Serializable
{
	public int SpellID;
	
	public String Nom;
	public String spellIcon;
	public int ProjectileID;
	public int Type;
	public int MpCost;
	public int PermanentCost;
	public int TimeMin;
	public int TimeMax;
	public int TimeBonus;
	public int TimeBonusInt;
	public int DamageMin;
	public int DamageMax;
	public int DamageBonus;
	public int DamageBonusInt;
	public int DamageType;
	public int CastDifficulty;
	public int BonusID;
	public int EventID;
	public int TypeResist;
	public int Harmful;
	public String SoundCast;
	public String SoundCible;
	public int Vore;
	public String Description;
	public int Save;
	public int TransformID;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Spell extends GameObject implements Comparable
{
	private SpellData spellData;
	
	public int spellID;
	public int projectileID;
	public int type;
	public int mpCost;
	public int permanentCost;
	public int timeMin;
	public int timeMax;
	public int timeBonus;
	public int timeBonusInt;
	public int dommageMin;
	public int dommageMax;
	public int dommageBonus;
	public int dommageBonusInt;
	public int dommageType;
	public int castDifficulte;
	public int bonusID;
	public int eventID;
	public int typeResist;
	public int harmful;
	public String sonCast;
	public String sonCible;
	public int vore;
	public String description;
	public int save;
	public int transformID;
	
	public AudioClip isonCast, isonCible;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Spell(int spellID)
	{
		this.spellID=spellID;
		
		spellData = DataUtil.getSpellData( spellID );
		
		setName( spellData.Nom );
		setImage( spellData.spellIcon );
		
		projectileID = spellData.ProjectileID;
		type = spellData.Type;
		mpCost = spellData.MpCost;
		permanentCost = spellData.PermanentCost;
		timeMin = spellData.TimeMin;
		timeMax = spellData.TimeMax;
		timeBonus = spellData.TimeBonus;
		timeBonusInt = spellData.TimeBonusInt;
		dommageMin = spellData.DamageMin;
		dommageMax = spellData.DamageMax;
		dommageBonus = spellData.DamageBonus;
		dommageBonusInt = spellData.DamageBonusInt;
		dommageType = spellData.DamageType;
		castDifficulte = spellData.CastDifficulty;
		bonusID = spellData.BonusID;
		eventID = spellData.EventID;
		typeResist = spellData.TypeResist;
		harmful = spellData.Harmful;
		sonCast = spellData.SoundCast;
		sonCible = spellData.SoundCible;
		vore = spellData.Vore;
		description = spellData.Description;
		save = spellData.Save;
		transformID = spellData.TransformID;
		
		isonCast = AudioLibrairie.getClip(spellData.SoundCast);
		isonCible = AudioLibrairie.getClip(spellData.SoundCible);
	}
	
	public Spell( SaveSpell saveData )
	{
		this( saveData.SpellID );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void reload()
	{
		isonCast = AudioLibrairie.getClip(spellData.SoundCast);
		isonCible = AudioLibrairie.getClip(spellData.SoundCible);
	}
	
	public int getSpellType()
	{
		if( spellData.PermanentCost > 0 )
		{
			return Util.SPELL_FORBIDDEN;
		}
		else if( spellData.TypeResist != 0 )
		{
			// These don't line up with their corresponding damage types.
			switch( spellData.TypeResist )
			{
			case 1: return Util.SPELL_CURSE;
			case 2: return Util.SPELL_PARALYSIS;
			case 3: return Util.SPELL_SLEEP;
			case 4: return Util.SPELL_CHARM;
			}
		}
		else if( spellData.DamageType != 0 )
		{
			return spellData.DamageType;
		}
		else if( spellData.TimeMax > 2 )
		{
			return Util.SPELL_ENCHANT;
		}

		return Util.SPELL_ARCANE;
	}
	
	public SaveSpell save()
	{
		SaveSpell ret = new SaveSpell();
		
		ret.SpellID = spellID;
		
		return ret;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	JPanel m_reqPane;
	JLabel m_costReqLbl, m_pcostReqLbl;

	@Override // GameObject
	public void initializeView( JPanel vitals, JPanel details )
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		Util.SpellType st = Util.SPELL_TYPES[getSpellType()];

		// Vitals
		JPanel typePane = new JPanel();
		typePane.setAlignmentX( Component.LEFT_ALIGNMENT );
		typePane.setLayout( new BoxLayout( typePane, BoxLayout.X_AXIS ) );
		{
			JLabel sumLbl = new JLabel();
			sumLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			sumLbl.setText( "Difficulty " + spellData.CastDifficulty + " " + st.name );
			sumLbl.setIcon( st.icon );
			typePane.add( sumLbl );
			
			typePane.add( Box.createHorizontalGlue() );
			
			JLabel typeLbl = new JLabel();
			typeLbl.setIcon( (spellData.Harmful == 0) ? Util.getIcon("spell_red") : Util.getIcon("spell_blue") );
			typePane.add( typeLbl );
		}
		vitals.add( typePane );
		
		if (spellData.MpCost > 0)
		{
			JLabel costLbl = new JLabel();
			costLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			costLbl.setIcon( Util.getIcon("stat_mp") );
			costLbl.setText( "Costs " + nf.format(spellData.MpCost) + " MP" );
			vitals.add( costLbl );
		}
		
		if (spellData.PermanentCost > 0)
		{
			JLabel pcostLbl = new JLabel();
			pcostLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			pcostLbl.setIcon( Util.getIcon("item_quest") );
			pcostLbl.setText( "Permanently Reduces MP by " + nf.format(spellData.PermanentCost) );
			vitals.add( pcostLbl );
		}
		
		// Details
		if (spellData.MpCost > 0 || spellData.PermanentCost > 0)
		{
			m_reqPane = new JPanel();
			m_reqPane.setAlignmentX( Component.LEFT_ALIGNMENT );
			m_reqPane.setBackground( details.getBackground() );
			m_reqPane.setLayout( new BoxLayout( m_reqPane, BoxLayout.Y_AXIS ) );
			{
				if (spellData.MpCost > 0)
				{
					m_costReqLbl = new JLabel( Util.getIcon("item_quest") );
					m_costReqLbl.setForeground( Color.RED );
					m_costReqLbl.setBackground( m_reqPane.getBackground() );
					m_costReqLbl.setText( "Requires " + nf.format(spellData.MpCost) + " MP" );
					m_reqPane.add( m_costReqLbl );
				}
			
				if (spellData.PermanentCost > 0)
				{
					m_pcostReqLbl = new JLabel( Util.getIcon("item_quest") );
					m_pcostReqLbl.setForeground( Color.RED );
					m_pcostReqLbl.setBackground( m_reqPane.getBackground() );
					m_pcostReqLbl.setText( "Requires " + nf.format(spellData.PermanentCost) + " Max MP" );
					m_reqPane.add( m_pcostReqLbl );
				}
				
				m_reqPane.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
			}
			details.add( m_reqPane );
		}
		
		VTextArea descText = new VTextArea();
		descText.setFont( details.getFont().deriveFont( Font.BOLD | Font.ITALIC ) );
		descText.setBackground( details.getBackground() );
		descText.setText( description );
		descText.setForeground( Color.GRAY );
		details.add( descText );
	}
	
	@Override // GameObject
	public void cleanupView()
	{
		m_reqPane = null;
		m_costReqLbl = null;
		m_pcostReqLbl = null;
	}
	
	@Override // GameObject
	public void updateView()
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		ObjetGraphique plr = VGame.instance.getPlayer();
		
		if (m_reqPane != null)
		{
			if( plr != null && plr.statistique != null )
			{
				PlayerStat ps = (PlayerStat)plr.statistique;
				
				if (m_costReqLbl != null)
				{
					m_costReqLbl.setVisible( spellData.MpCost > ps.pm );
				}
				
				if (m_pcostReqLbl != null)
				{
					m_pcostReqLbl.setVisible( spellData.PermanentCost > ps.pmMax );
				}
			}
			else
			{
				if (m_costReqLbl != null)
				{
					m_costReqLbl.setVisible( false );
				}
				
				if (m_pcostReqLbl != null)
				{
					m_pcostReqLbl.setVisible( false );
				}
			}
			
			m_reqPane.setVisible(
				(m_costReqLbl != null && m_costReqLbl.isVisible()) ||
				(m_pcostReqLbl != null && m_pcostReqLbl.isVisible()) );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Object
	public String toString()
	{
		String str = getName();
		
		if( spellData.MpCost>0 || spellData.PermanentCost<=0 )
		{
			str += " (" + spellData.MpCost + " MP)";
		}
		if( spellData.PermanentCost>0 )
		{
			str += " [-" + spellData.PermanentCost + " Max MP]";
		}

		return str;
	}

	@Override // Comparable
	public int compareTo( Object o )
	{
		Spell os = (Spell)o;
		
		if( spellData.Harmful != os.spellData.Harmful )
		{
			// Primary ordering by harmful/helpful
			return spellData.Harmful - os.spellData.Harmful;
		}
		else
		{
			// Secondary ordering by low-to-high mp cost
			return spellData.MpCost - os.spellData.MpCost;
		}
	}
}
