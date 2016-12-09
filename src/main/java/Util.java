///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Util.java
// Singleton class containing static utility functions.
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class Util
{
	public static class ItemType
	{
		public Icon icon;
		public int order;
		public String name;
		
		public ItemType( String icon, String name, int order )
		{
			this.icon = Util.getIcon( icon );
			this.name = name;
			this.order = order;
		}
	};
	
	public static class WeaponType
	{
		public String name;
		
		public WeaponType( String name )
		{
			this.name = name;
		}
	};
	
	public static class DamageType
	{
		public Icon icon;
		public String name;
		
		public DamageType( String icon, String name )
		{
			this.icon = Util.getIcon( icon );
			this.name = name;
		}
	};
	
	public static class StatType
	{
		public Icon icon;
		public String name;
		public String abbr;
		public String desc;
		public String effect;
		
		public StatType( String icon, String name, String abbr, String desc, String effect )
		{
			this.icon = Util.getIcon( icon );
			this.name = name;
			this.abbr = abbr;
			this.desc = desc;
			this.effect = effect;
		}
	};
	
	public static class ClassType
	{
		public String name;
		
		public ClassType( String name )
		{
			this.name = name;
		}
	};
	
	public static class SpellType
	{
		public Icon icon;
		public String name;
		
		public SpellType( String icon, String name )
		{
			this.icon = Util.getIcon( icon );
			this.name = name;
		}
	};
	
	private static Util instance = new Util();
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public static final int ITEM_WEAPON=0;
	public static final int ITEM_ARMOR=1;
	public static final int ITEM_POTION=2;
	public static final int ITEM_USABLE=3;
	public static final int ITEM_FOOD=4;
	public static final int ITEM_RING=5;
	public static final int ITEM_AMULET=6;
	public static final int ITEM_QUEST=7;
	public static final int ITEM_BOOK=8;
	public static final int NUM_ITEM_TYPES=9;
	public static final ItemType[] ITEM_TYPES = new ItemType[NUM_ITEM_TYPES];
	
	public static final int WEAPON_SWORD=0;
	public static final int WEAPON_AXE=1;
	public static final int WEAPON_MACE=2;
	public static final int WEAPON_SPEAR=3;
	public static final int WEAPON_DAGGER=4;
	public static final int WEAPON_STAFF=5;
	public static final int WEAPON_BOW=6;
	public static final int WEAPON_HAND=7;
	public static final int NUM_WEAPON_TYPES=8;
	public static final WeaponType[] WEAPON_TYPES = new WeaponType[NUM_WEAPON_TYPES];
	
	public static final int DAMAGE_PHYSICAL=0;
	public static final int DAMAGE_FIRE=1;
	public static final int DAMAGE_ICE=2;
	public static final int DAMAGE_ACID=3;
	public static final int DAMAGE_LIGHTNING=4;
	public static final int DAMAGE_POSITIVE=5;
	public static final int DAMAGE_NEGATIVE=6;
	public static final int DAMAGE_NEUTRAL=7;
	public static final int DAMAGE_CHARM=8;
	public static final int DAMAGE_PARALYSIS=9;
	public static final int DAMAGE_SLEEP=10;
	public static final int DAMAGE_CURSE=11;
	public static final int NUM_DAMAGE_TYPES=12;
	public static final DamageType[] DAMAGE_TYPES = new DamageType[NUM_DAMAGE_TYPES];
	
	public static final int STAT_STR=0;
	public static final int STAT_DEX=1;
	public static final int STAT_END=2;
	public static final int STAT_INT=3;
	public static final int STAT_CHR=4;
	public static final int NUM_STAT_TYPES=5;
	public static final StatType[] STAT_TYPES = new StatType[NUM_STAT_TYPES];
	
	public static final int CLASS_FIGHTER=0;
	public static final int CLASS_MAGE=1;
	public static final int CLASS_THIEF=2;
	public static final int CLASS_ARCHER=3;
	public static final int CLASS_SHAMAN=4;
	public static final int CLASS_RANGER=5;
	public static final int CLASS_MARCHANT=6;
	public static final int NUM_CLASS_TYPES=7;
	public static final ClassType[] CLASS_TYPES = new ClassType[NUM_CLASS_TYPES];
	
	public static final int SPELL_PHYSICAL=0; // Spells which use one of the standard damage/resist types.
	public static final int SPELL_FIRE=1;
	public static final int SPELL_ICE=2;
	public static final int SPELL_ACID=3;
	public static final int SPELL_LIGHTNING=4;
	public static final int SPELL_POSITIVE=5;
	public static final int SPELL_NEGATIVE=6;
	public static final int SPELL_NEUTRAL=7;
	public static final int SPELL_CHARM=8;
	public static final int SPELL_PARALYSIS=9;
	public static final int SPELL_SLEEP=10;
	public static final int SPELL_CURSE=11;
	public static final int SPELL_ENCHANT=12; // Spells with effect durations
	public static final int SPELL_FORBIDDEN=13; // Spells that have a permanent cost
	public static final int SPELL_ARCANE=14; // Spells that don't meet other criteria
	public static final int NUM_SPELL_TYPES=15;
	public static final SpellType[] SPELL_TYPES = new SpellType[NUM_SPELL_TYPES];
	
	public static final Color[] COLOR_DAMAGE_FORE = new Color[] {
		new Color( 0xe5625c ), // Red
		new Color( 0xE1E33B ), // Yellow
		new Color( 0x79F17F ), // Green
	};
	
	public static final Color[] COLOR_DAMAGE_BACK = new Color[] {
		new Color( 0x4b0501 ), // Red
		new Color( 0x3E3E16 ), // Yellow
		new Color( 0x083809 ), // Green
	};
	
	public static final Color[] COLOR_TIME_FORE = new Color[] {
		new Color( 0x486687 ), // Midnight
		new Color( 0x6C91BB ), // Predawn
		new Color( 0xB5FAFB ), // Dawn
		new Color( 0xF6FB95 ), // Morning
		new Color( 0xF6FB95 ), // Noon
		new Color( 0xF6FB95 ), // Afternoon
		new Color( 0xB5FAFB ), // Dusk
		new Color( 0x6C91BB ), // Late Night
		new Color( 0x486687 ), // Midnight
	};
	
	public static final Color[] COLOR_TIME_BACK = new Color[] {
		new Color( 0x091420 ), // Midnight
		new Color( 0x12273E ), // Predawn
		new Color( 0x133256 ), // Dawn
		new Color( 0x717601 ), // Morning
		new Color( 0x717601 ), // Noon
		new Color( 0x717601 ), // Afternoon
		new Color( 0x180D4C ), // Dusk
		new Color( 0x12273E ), // Late Night
		new Color( 0x091420 ), // Midnight
	};
	
	public final static Color COLOR_HP_FORE = new Color( 0xE5625C );
	public final static Color COLOR_HP_BACK = new Color( 0x4B0501 );
	
	public final static Color COLOR_MP_FORE = new Color( 0x5C86E5 );
	public final static Color COLOR_MP_BACK = new Color( 0x01184B );
	
	public final static Color COLOR_XP_FORE = new Color( 0xD5D5D5 );
	public final static Color COLOR_XP_BACK = new Color( 0x444444 );
	
	private static boolean ENEMY_MAP[][] = null;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private Util() {}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Initializer
	public static void initialize()
	{
		// Initialize tables etc here.
		try
		{
			// Item types
			ITEM_TYPES[ITEM_WEAPON] = new ItemType( "item_weapon", "Weapon", 0 );
			ITEM_TYPES[ITEM_ARMOR] = new ItemType( "item_armor", "Armor", 1 );
			ITEM_TYPES[ITEM_POTION] = new ItemType( "item_potion", "Potion", 6 );
			ITEM_TYPES[ITEM_USABLE] = new ItemType( "item_generic", "Item", 7 );
			ITEM_TYPES[ITEM_FOOD] = new ItemType( "item_food", "Food", 5 );
			ITEM_TYPES[ITEM_RING] = new ItemType( "item_ring", "Ring", 2 );
			ITEM_TYPES[ITEM_AMULET] = new ItemType( "item_amulet", "Amulet", 3 );
			ITEM_TYPES[ITEM_QUEST] = new ItemType( "item_quest", "Quest Item", 8 );
			ITEM_TYPES[ITEM_BOOK] = new ItemType( "item_book", "Book", 4 );
			
			// Weapon types
			WEAPON_TYPES[WEAPON_SWORD] = new WeaponType( "Sword" );
			WEAPON_TYPES[WEAPON_AXE] = new WeaponType( "Axe" );
			WEAPON_TYPES[WEAPON_MACE] = new WeaponType( "Mace" );
			WEAPON_TYPES[WEAPON_SPEAR] = new WeaponType( "Spear" );
			WEAPON_TYPES[WEAPON_DAGGER] = new WeaponType( "Dagger" );
			WEAPON_TYPES[WEAPON_STAFF] = new WeaponType( "Staff" );
			WEAPON_TYPES[WEAPON_BOW] = new WeaponType( "Bow" );
			WEAPON_TYPES[WEAPON_HAND] = new WeaponType( "Unarmed" );
			
			// Damage types
			DAMAGE_TYPES[DAMAGE_PHYSICAL] = new DamageType( "damage_physical", "Physical" );
			DAMAGE_TYPES[DAMAGE_FIRE] = new DamageType( "damage_fire", "Fire" );
			DAMAGE_TYPES[DAMAGE_ICE] = new DamageType( "damage_ice", "Ice" );
			DAMAGE_TYPES[DAMAGE_ACID] = new DamageType( "damage_acid", "Acid" );
			DAMAGE_TYPES[DAMAGE_LIGHTNING] = new DamageType( "damage_lightning", "Lightning" );
			DAMAGE_TYPES[DAMAGE_POSITIVE] = new DamageType( "damage_positive", "Light" );
			DAMAGE_TYPES[DAMAGE_NEGATIVE] = new DamageType( "damage_negative", "Shadow" );
			DAMAGE_TYPES[DAMAGE_NEUTRAL] = new DamageType( "damage_neutral", "Force" );
			DAMAGE_TYPES[DAMAGE_CHARM] = new DamageType( "damage_charm", "Charm" );
			DAMAGE_TYPES[DAMAGE_PARALYSIS] = new DamageType( "damage_paralysis", "Paralysis" );
			DAMAGE_TYPES[DAMAGE_SLEEP] = new DamageType( "damage_sleep", "Sleep" );
			DAMAGE_TYPES[DAMAGE_CURSE] = new DamageType( "damage_curse", "Curse" );
			
			// Stat types
			STAT_TYPES[STAT_STR] = new StatType( "stat_str", "Strength", "STR",
				"Physical prowess",
				"Increases damage, vore attack/defense" );
			STAT_TYPES[STAT_DEX] = new StatType( "stat_dex", "Dexterity", "DEX",
				"Physical quickness",
				"Increases defense, dodge, vore attack/defense" );
			STAT_TYPES[STAT_END] = new StatType( "stat_end", "Endurence", "END",
				"Overall toughness",
				"Increases health, vore defense, magic defense" );
			STAT_TYPES[STAT_INT] = new StatType( "stat_int", "Intelligence", "INT",
				"Mental alacrity/quickness",
				"Increases mana, magic attack/defense" );
			STAT_TYPES[STAT_CHR] = new StatType( "stat_chr", "Charisma", "CHR",
				"Physical/social attractiveness",
				"Increases magic attack, resale values" );
			
			// Class types
			CLASS_TYPES[CLASS_FIGHTER] = new ClassType( "Fighter" );
			CLASS_TYPES[CLASS_MAGE] = new ClassType( "Mage" );
			CLASS_TYPES[CLASS_THIEF] = new ClassType( "Thief" );
			CLASS_TYPES[CLASS_ARCHER] = new ClassType( "Archer" );
			CLASS_TYPES[CLASS_SHAMAN] = new ClassType( "Shaman" );
			CLASS_TYPES[CLASS_RANGER] = new ClassType( "Ranger" );
			CLASS_TYPES[CLASS_MARCHANT] = new ClassType( "Merchant" );
			
			// Spell types
			SPELL_TYPES[SPELL_PHYSICAL] = new SpellType( "damage_physical", "Attack Spell" );
			SPELL_TYPES[SPELL_FIRE] = new SpellType( "damage_fire", "Fire Magic" );
			SPELL_TYPES[SPELL_ICE] = new SpellType( "damage_ice", "Ice Magic" );
			SPELL_TYPES[SPELL_ACID] = new SpellType( "damage_acid", "Acid Magic" );
			SPELL_TYPES[SPELL_LIGHTNING] = new SpellType( "damage_lightning", "Lightning Magic" );
			SPELL_TYPES[SPELL_POSITIVE] = new SpellType( "damage_positive", "Light Magic" );
			SPELL_TYPES[SPELL_NEGATIVE] = new SpellType( "damage_negative", "Shadow Magic" );
			SPELL_TYPES[SPELL_NEUTRAL] = new SpellType( "damage_neutral", "Force Magic" );
			SPELL_TYPES[SPELL_CHARM] = new SpellType( "damage_charm", "Seducing Charm" );
			SPELL_TYPES[SPELL_PARALYSIS] = new SpellType( "damage_paralysis", "Paralyzing Curse" );
			SPELL_TYPES[SPELL_SLEEP] = new SpellType( "damage_sleep", "Sleep Enchantment" );
			SPELL_TYPES[SPELL_CURSE] = new SpellType( "damage_curse", "Fel Curse" );
			SPELL_TYPES[SPELL_ENCHANT] = new SpellType( "spell_enchantment", "Enchantment" );
			SPELL_TYPES[SPELL_FORBIDDEN] = new SpellType( "spell_forbidden", "Forbidden Ritual" );
			SPELL_TYPES[SPELL_ARCANE] = new SpellType( "spell_arcane", "Arcane Magic" );
			
			// Generate a trivial lookup map that describes all the enemy relationships.
			ENEMY_MAP = new boolean[13][13];
			
			//case 1:  4;6;7;9;10;11;12;13;
			addEnemy( 1, 4 );
			addEnemy( 1, 6 );
			addEnemy( 1, 7 );
			addEnemy( 1, 9 );
			addEnemy( 1, 10 );
			addEnemy( 1, 11 );
			addEnemy( 1, 12 );
			addEnemy( 1, 13 );
			
			//case 2:  3;5;6;7;10;11;12;13;
			addEnemy( 2, 3 );
			addEnemy( 2, 5 );
			addEnemy( 2, 6 );
			addEnemy( 2, 7 );
			addEnemy( 2, 10 );
			addEnemy( 2, 11 );
			addEnemy( 2, 12 );
			addEnemy( 2, 13 );
			
			//case 3:  4;5;6;7;9;10;11;12;13;
			addEnemy( 3, 4 );
			addEnemy( 3, 5 );
			addEnemy( 3, 6 );
			addEnemy( 3, 7 );
			addEnemy( 3, 9 );
			addEnemy( 3, 10 );
			addEnemy( 3, 11 );
			addEnemy( 3, 12 );
			addEnemy( 3, 13 );
			
			//case 4:  5;6;7;10;11;12;13;
			addEnemy( 4, 5 );
			addEnemy( 4, 6 );
			addEnemy( 4, 7 );
			addEnemy( 4, 10 );
			addEnemy( 4, 11 );
			addEnemy( 4, 12 );
			addEnemy( 4, 13 );
			
			//case 5:  7;9;10;11;12;13;
			addEnemy( 5, 7 );
			addEnemy( 5, 9 );
			addEnemy( 5, 10 );
			addEnemy( 5, 11 );
			addEnemy( 5, 12 );
			addEnemy( 5, 13 );
			
			//case 6:  7;9;10;11;12;13;
			addEnemy( 6, 7 );
			addEnemy( 6, 9 );
			addEnemy( 6, 10 );
			addEnemy( 6, 11 );
			addEnemy( 6, 12 );
			addEnemy( 6, 13 );
			
			//case 7:  7;9;10;11;12;13;
			addEnemy( 7, 7 ); // team 7 hates itself.  Try therapy.
			addEnemy( 7, 9 );
			addEnemy( 7, 10 );
			addEnemy( 7, 11 );
			addEnemy( 7, 12 );
			addEnemy( 7, 13 );
			
			//case 8:  7; // Special case... 8 will be an enemy of 7, but 7 will not be an enemy of 8.
			// I'm guessing 8 will never come in as a source... so it probably _shouldn't_ have been set
			//  in the initial map... so we're going to ignore it here.
			//addEnemy( 8,  );
			
			//case 9:  10;11;12;13;
			addEnemy( 9, 10 );
			addEnemy( 9, 11 );
			addEnemy( 9, 12 );
			addEnemy( 9, 13 );
			
			//case 10: 11;12;13;
			addEnemy( 10, 11 );
			addEnemy( 10, 12 );
			addEnemy( 10, 13 );
			
			//case 11: 12;13;
			addEnemy( 11, 12 );
			addEnemy( 11, 13 );
			
			//case 12: 12;13;
			addEnemy( 12, 12 ); // 12 is also self-hating, like 7.
			addEnemy( 12, 13 );
			
			//case 13: // 13's already been taken care off via the backfill from the other teams.
		}
		catch( Exception e )
		{
			Util.error( e );
		}
	}

	private static void addEnemy( int team_a, int team_b )
	{
		// Mark A and B as enemies in the map.  This is bidirectional (if A hates B, then B hates A).
		ENEMY_MAP[team_a-1][team_b-1] = true;
		ENEMY_MAP[team_b-1][team_a-1] = true;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Game utilities
	public static boolean isEnemy( int team_a, int team_b )
	{
		// Return true if A and B are enemies by team.  The table makes it trivial.
		return ENEMY_MAP[team_a-1][team_b-1];
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Resources
	public static ImageIcon getIcon( String str )
	{
		return new ImageIcon( instance.getClass().getResource("/system/icons/" + str + ".png") );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Error reporting
	protected static void error( String str, String detail )
	{
		System.out.println( detail );
		int opt = JOptionPane.showConfirmDialog( null,
				str + "\n\nWould you like to copy details to the clipboard?",
				"Error",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE );

		if( opt == 0 ) // Yes
		{
			StringSelection ss = new StringSelection( detail ); 
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents( ss, null );
		}
	}
	
	public static void error( String str, Exception e )
	{
		String detail = str + "\n" + stackString( e );
		error( str, detail );
	}
	
	
	public static void error( String str )
	{
		error( str, new Exception() );
	}
	
	public static void error( Exception e )
	{
		String detail = stackString( e );
		String str = detail.substring( 0, detail.indexOf( "\n" ) );
		error( str, detail );
	}

	public static String stackString( Exception e )
	{
		try
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace( pw );
			return sw.toString();
		}
		catch( Exception e2 )
		{
			return "Error retrieving stack trace";
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Calculators
	public static int lerpIndex( double l, int max )
	{
		if( l < 0.0 || max <= 1 )
		{
			return 0;
		}
		else if( l >= 1.0 )
		{
			return max - 1;
		}
		else
		{
			return (int)((l * (max - 1)) + 0.5);
		}
	}
	
	public static double applyCurve( double v, double c )
	{
		return v;
	}
	
	public static int levelCost( int n )
	{
		n++;
		return (n+1)*n/2 *1000 -1000;
	}
	
	public static double distSq( double ax, double ay, double bx, double by )
	{
		double dx = ax - bx;
		double dy = ay - by;
		return (dx * dx) + (dy * dy);
	}
	
	public static double distSq( Point a, Point b )
	{
		double dx = a.getX() - b.getX();
		double dy = a.getY() - b.getY();
		return (dx * dx) + (dy * dy);
	}
	
	public static double dist( double ax, double ay, double bx, double by )
	{
		return Math.sqrt( distSq( ax, ay, bx, by ) );
	}
	
	public static double dist( Point a, Point b )
	{
		return Math.sqrt( distSq( a.getX(), a.getY(), b.getX(), b.getY() ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// String processing
	public static String sanitizeHTML( String str )
	{
		// Handle database import changes.
		str = str.replace( '@', ',' );
		str = str.replace( '&', '\n' );
		
		// Pull out any existing html tags
		str = str.replace( "<", "&lt" );
		str = str.replace( ">", "&gt" );
		
		// Replace common notators with valid html tags
		str = str.replace( "\n", "<br>" );

		return str;
	}

	public static Vector<String> explode( String str, String sep, boolean trim )
	{
		Vector<String> ret = new Vector<String>();
		
		int slen = sep.length();
		int idx = str.indexOf( sep );
		while( idx >= 0 )
		{
			if( trim )
				ret.add( str.substring( 0, idx ).trim() );
			else
				ret.add( str.substring( 0, idx ) );
			
			str = str.substring( idx + slen );
			
			idx = str.indexOf( sep );
		}
		
		if( trim )
			ret.add( str.trim() );
		else
			ret.add( str );
		
		return ret;
	}

	public static Vector<String> explode( String str, String sep )
	{
		return explode( str, sep, false );
	}
	
	public static String implode( Vector<String> lines, String sep )
	{
		String ret = "";
		if( !lines.isEmpty() )
		{
			ret += lines.elementAt( 0 );
			for( int i = 1; i < lines.size(); ++i )
			{
				ret += sep + lines.elementAt( i );
			}
		}
		
		return ret;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Render tools
	public static Color mixColors( Color c0, Color c1, double l )
	{
		double dl = 1.0 - l;
		
		int r = (int)((double)c0.getRed() * dl + (double)c1.getRed() * l);
		int g = (int)((double)c0.getGreen() * dl + (double)c1.getGreen() * l);
		int b = (int)((double)c0.getBlue() * dl + (double)c1.getBlue() * l);
		int a = (int)((double)c0.getAlpha() * dl + (double)c1.getAlpha() * l);
		
		return new Color( r, g, b, a );
	}
	
	public static Color mixColors( Color[] c, double l )
	{
		if( l <= 0.0 || c.length == 1 )
		{
			return c[0];
		}
		else if( l >= 1.0 )
		{
			return c[c.length-1];
		}
		else
		{
			l *= c.length - 1;
			int i = (int)l;
			l -= (double)i;
			
			return mixColors( c[i], c[i+1], l );
		}
	}
	
	public static Color mixColors( Color[] c, double l, double curve )
	{
		if( curve != 0.0 )
		{
			double l3 = l * l * l;
			l = (curve * l3) + ((1.0 - curve) * l);
		}
		
		return mixColors( c, l );
	}
}
