///////////////////////////////////////////////////////////////////////////////////////////////////
//
// TotalBonus.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class TotalBonus
{
	private Bonus total;
	private int events;
	private boolean any;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public TotalBonus()
	{
		total = new Bonus();
		events = 0;
		any = false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void add( Bonus b )
	{
		total.strength += b.strength;				any |= total.strength != 0;
		total.dexterity += b.dexterity;				any |= total.dexterity != 0;
		total.endurence += b.endurence;				any |= total.endurence != 0;
		total.intelligence += b.intelligence;		any |= total.intelligence != 0;
		total.charisma += b.charisma;				any |= total.charisma != 0;
		
		total.famine += b.famine;					any |= total.famine != 0;
		total.regeneration += b.regeneration;		any |= total.regeneration != 0;
		total.manaRegeneration += b.manaRegeneration;	any |= total.manaRegeneration != 0;
		
		total.defense += b.defense;					any |= total.defense != 0;
		
		total.attackBonus += b.attackBonus;			any |= total.attackBonus != 0;
		total.damageBonus += b.damageBonus;			any |= total.damageBonus != 0;
		
		total.saveBonus += b.saveBonus;				any |= total.saveBonus != 0;
		total.castingBonus += b.castingBonus;		any |= total.castingBonus != 0;
		
		total.vBonus += b.vBonus;					any |= total.vBonus != 0;
		total.vDifficulte += b.vDifficulte;			any |= total.vDifficulte != 0;
		total.aBonus += b.aBonus;					any |= total.aBonus != 0;
		total.digTimeMax += b.digTimeMax;			any |= total.digTimeMax != 0;
		total.absorption += b.absorption;			any |= total.absorption != 0;
		total.manaAbsorption += b.manaAbsorption;	any |= total.manaAbsorption != 0;
		
		total.vDefense += b.vDefense;				any |= total.vDefense != 0;
		total.vEscapeBonus += b.vEscapeBonus;		any |= total.vEscapeBonus != 0;
		
		events += b.eventStart > 0 ? 1 : 0;
		events += b.eventEnd > 0 ? 1 : 0;
		events += b.hitEvent > 0 ? 1 : 0;
		events += b.swallowEvent > 0 ? 1 : 0;
		events += b.swallowedEvent > 0 ? 1 : 0;
		events += b.digestingEvent > 0 ? 1 : 0;
		events += b.digestedEvent > 0 ? 1 : 0;
		events += b.escapeEvent > 0 ? 1 : 0;
		events += b.escapedEvent > 0 ? 1 : 0;
		
		if( !b.description.isEmpty() )
		{
			if( !total.description.isEmpty() )
			{
				total.description += ", ";
			}
			
			total.description += b.description;
		}

		add( b.resistance );
	}
	
	public void add( int[] res )
	{
		for( int i = 0; i < Util.NUM_DAMAGE_TYPES; ++i )
		{
			total.resistance[i] += res[i];
		}
		
		updateAny();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getDescriptionString()
	{
		return total.description;
	}
	
	public boolean getAnyVisible()
	{
		return any;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void addView( JPanel pane )
	{
		if( events > 0 )
		{
			addViewItem( pane, "Surrounded by a strange aura", Color.YELLOW );
		}
		
		addViewBonus( pane, total.strength, "Strength" );
		addViewBonus( pane, total.dexterity, "Dexterity" );
		addViewBonus( pane, total.endurence, "Endurence" );
		addViewBonus( pane, total.intelligence, "Intelligence" );
		addViewBonus( pane, total.charisma, "Charisma" );
		
		addViewBonus( pane, total.famine, "Hunger" );
		addViewBonus( pane, total.regeneration, "Regeneration" );
		addViewBonus( pane, total.manaRegeneration, "Mana Regeneration" );
		
		addViewBonus( pane, total.defense, "Defense" );
		
		addViewBonus( pane, total.attackBonus, "Attack Bonus" );
		addViewBonus( pane, total.damageBonus, "Damage Bonus" );
		
		addViewBonus( pane, total.saveBonus, "Spell Defense" );
		addViewBonus( pane, total.castingBonus, "Spellcasting" );
		
		addViewBonus( pane, total.vBonus, "Vore Bonus" );
		addViewBonus( pane, total.vDifficulte, "Stomach Muscles" );
		addViewBonus( pane, total.aBonus, "Stomach Acids" );
		addViewBonus( pane, total.digTimeMax, "Digestion Length" );
		addViewBonus( pane, total.absorption, "Body Digestion" );
		addViewBonus( pane, total.manaAbsorption, "Soul Digestion" );
		
		addViewBonus( pane, total.vDefense, "Vore Defense" );
		addViewBonus( pane, total.vEscapeBonus, "Escape Chance" );

		for( int i = 0; i < Util.NUM_DAMAGE_TYPES; ++i )
		{
			int amt = total.resistance[i];
			addViewBonus( pane, amt, Util.DAMAGE_TYPES[i].name + " Resistance" );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void addViewBonus( JPanel pane, int amt, String name )
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		if( amt > 0 )
		{
			addViewItem( pane, "+" + nf.format(amt) + " " + name, Color.BLUE );
		}
		else if( amt < 0 )
		{
			addViewItem( pane, nf.format(amt) + " " + name, Color.RED );
		}
	}
	
	private void addViewItem( JPanel pane, String text, Color col )
	{
		JLabel lbl = new JLabel();
		lbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		lbl.setText( text );
		lbl.setForeground( col );
		lbl.setBackground( pane.getBackground() );
		pane.add( lbl );
	}
	
	private void updateAny()
	{
		any = events > 0
			|| total.strength != 0
			|| total.intelligence != 0
			|| total.endurence != 0
			|| total.charisma != 0
			|| total.dexterity != 0
			|| total.famine != 0
			|| total.regeneration != 0
			|| total.manaRegeneration != 0
			|| total.defense != 0
			|| total.attackBonus != 0
			|| total.damageBonus != 0
			|| total.saveBonus != 0
			|| total.castingBonus != 0
			|| total.vBonus != 0
			|| total.vDifficulte != 0
			|| total.aBonus != 0
			|| total.digTimeMax != 0
			|| total.absorption != 0
			|| total.manaAbsorption != 0
			|| total.vDefense != 0
			|| total.vEscapeBonus != 0;
		
		for( int i = 0; i < Util.NUM_DAMAGE_TYPES && !any; ++i )
		{
			any |= total.resistance[i] != 0;
		}
	}
}
