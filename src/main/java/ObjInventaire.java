///////////////////////////////////////////////////////////////////////////////////////////////////
//
// ObjInventaire.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class ObjInventaire extends GameObject
{
	public int type;
	public int niveauR=1;
	public int dexterityR=0;
	public int intelligenceR=0;
	public int endurenceR=0;
	public int strengthR=0;
	public int charismaR=0;
	public int objetID;
	public boolean equipped=false;
	public String description;
	public int bonusID=0;
	public int transformID;
	public int price=0;
	public int harmful=1;
	
	public Bonus bonus = null;

	protected ObjInventaire stack_next = null;
	protected ObjInventaire stack_prev = null;

	PlayerStat mere;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public ObjInventaire( PlayerStat m, int obj )
	{
		super();

		mere=m;
		objetID=obj;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isUsable()
	{
		return true;
	}

	public boolean canUse()
	{
		return isUsable() && testRequirements();
	}

	public boolean isEquippable()
	{
		return false;
	}

	public void use(){}
	public String getUseString()
	{
		return "Use";
	}

	public boolean stack( ObjInventaire ob )
	{
		// Add ob to our stack.  We use a front push that doesn't move the top item, in case the
		//  top item is having its charges depleted or something.
		boolean ret = canStack( ob );

		if( ret )
		{
			setNew( isNew() || ob.isNew() );
			ob.setNew( false );

			if( stack_next != null )
			{
				stack_next.stack_prev = ob;
			}

			ob.stack_prev = this;
			ob.stack_next = stack_next;

			stack_next = ob;
		}

		return ret;
	}

	public boolean unStack()
	{
		boolean ret = false;

		if( stack_next != null )
		{
			stack_next.stack_prev = stack_prev;
		}

		if( stack_prev != null )
		{
			stack_prev.stack_next = stack_next;
		}
		else
		{
			// This is the first item in the stack, which entails some special handling.
			ret = true;
		}

		stack_prev = null;
		stack_next = null;

		return ret;
	}

	public boolean canStack( ObjInventaire ob )
	{
		return false;
	}

	public int getStackCount()
	{
		int ret = 1;

		if( stack_next != null )
		{
			ret += stack_next.getStackCount();
		}

		return ret;
	}

	public ObjInventaire getStack()
	{
		return stack_next;
	}

	public void unequip(){}
	public void sell()
	{
		if( price > 0 )
		{
			mere.money += getSellValue();
			dispose();
		}
	}

	protected boolean testRequirements(){
		if(transformID != 0 && mere.mere.carID != transformID )
			return false;
		if(niveauR > 0 && mere.niveau < niveauR)
			return false;
		if(dexterityR > 0 && mere.dexterity < dexterityR )
			return false;
		if( strengthR > 0 && mere.strength < strengthR )
			return false;
		if( intelligenceR > 0 && mere.intelligence < intelligenceR )
			return false;
		if( endurenceR > 0 && mere.endurence < endurenceR )
			return false;
		if( charismaR > 0 && mere.charisma < charismaR )
			return false;
		return true;
	}

	public int getSellValue()
	{
		return (price / 4) + ((price * mere.charisma) / 20);
	}

	public void changerBonus(int bonusID){}

	public int getValeur()
	{
		return 0;
	}

	public SaveItem save()
	{
		SaveItem ret = new SaveItem();
		
		ret.ObjectID = objetID;
		ret.BonusID = bonusID;
		ret.Type = type;
		ret.Equipped = equipped;
		ret.Valeur = getValeur();
		
		return ret;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Vector<String> getRequirementsList()
	{
		Vector<String> rl = new Vector<String>();
		
		// Ideally I'd look up the name of the required transformID here, but the names aren't
		//  'readable'... they're like 'Dwarf 4', 'captainGuard', etc.
		if( transformID != 0 && mere.mere.carID != transformID )
			rl.add( "Bound to someone else" );
		if( niveauR > 1 && mere.niveau < niveauR )
			rl.add( "Requires Level " + niveauR );
		if( strengthR > 1 && mere.strength < strengthR )
			rl.add( "Requires " + strengthR + " Strength" );
		if( dexterityR > 1 && mere.dexterity < dexterityR )
			rl.add( "Requires " + dexterityR + " Dexterity" );
		if( intelligenceR > 1 && mere.intelligence < intelligenceR )
			rl.add( "Requires " + intelligenceR + " Intelligence" );
		if( endurenceR > 1 && mere.endurence < endurenceR )
			rl.add( "Requires " + endurenceR + " Endurence" );
		if( charismaR > 1 && mere.charisma < charismaR )
			rl.add( "Requires " + charismaR + " Charisma" );

		return rl;
	}
	
	public TotalBonus getTotalBonus()
	{
		TotalBonus bt = new TotalBonus();
		
		if( bonus != null )
		{
			bt.add( bonus );
		}
		
		return bt;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getTypeString()
	{
		return Util.ITEM_TYPES[type].name;
	}

	public String getPriceString()
	{
		String ret;

		if( price > 0 )
			ret = "Value : " + getSellValue() + " (" + price + ")";
		else
			ret = "This item cannot be sold";

		return ret;
	}

	public String getLevelString()
	{
		int sc = getStackCount();
		String eq = equipped ? " (Equipped)" : "";
		String st = sc > 1 ? " (x" + sc + ")" : "";

		return "Level " + Math.max(niveauR, 1) + " " + getTypeString() + eq + st;
	}

	public String getDescString()
	{
		return Util.sanitizeHTML( description );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	private JLabel m_levelLbl = null;
	private JLabel m_priceLbl = null;
	private JPanel m_reqPane = null;

	@Override // GameObject
	public void initializeView( JPanel vitals, JPanel details )
	{
		// Vitals
		JPanel levelPane = new JPanel();
		levelPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		levelPane.setLayout( new BoxLayout( levelPane, BoxLayout.X_AXIS ) );
		{
			m_levelLbl = new JLabel();
			m_levelLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			m_levelLbl.setText( getLevelString() );
			m_levelLbl.setIcon( Util.getIcon( "stat_level" ) );//Util.ITEM_TYPES[type].icon );
			levelPane.add( m_levelLbl );
			
			levelPane.add( Box.createHorizontalGlue() );
			
			JLabel typeLbl = new JLabel();
			typeLbl.setIcon( Util.ITEM_TYPES[type].icon );
			levelPane.add( typeLbl );
		}
		vitals.add( levelPane );
		
		// Details
		m_reqPane = new JPanel();
		m_reqPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		m_reqPane.setBackground( details.getBackground() );
		m_reqPane.setLayout( new BoxLayout( m_reqPane, BoxLayout.Y_AXIS ) );
		{
			Vector<String> rl = getRequirementsList();
			
			if( !rl.isEmpty() )
			{
				for( int i = 0; i < rl.size(); ++i )
				{
					JLabel reqLbl = new JLabel();
					reqLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
					reqLbl.setText( rl.elementAt( i ) );
					reqLbl.setIcon( Util.getIcon("item_quest") );
					reqLbl.setForeground( Color.RED );
					reqLbl.setBackground( m_reqPane.getBackground() );
					m_reqPane.add( reqLbl );
				}
				
				m_reqPane.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
			}
		}
		details.add( m_reqPane );

		m_priceLbl = new JLabel();
		m_priceLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
		m_priceLbl.setText( getPriceString() );
		m_priceLbl.setIcon( Util.getIcon("item_gold") );
		details.add( m_priceLbl );
		
		details.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
		
		VTextArea descText = new VTextArea();
		descText.setFont( details.getFont().deriveFont( Font.BOLD | Font.ITALIC ) );
		descText.setBackground( details.getBackground() );
		descText.setText( getDescString() );
		descText.setForeground( Color.GRAY );
		details.add( descText );
		
		TotalBonus bt = getTotalBonus();
		if( bt.getAnyVisible() )
		{
			details.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
			bt.addView( details );
		}
	}
	
	@Override // GameObject
	public void cleanupView()
	{
		m_levelLbl = null;
		m_priceLbl = null;
		m_reqPane = null;
	}
	
	@Override // GameObject
	public void updateView()
	{	
		if( m_levelLbl != null )
		{
			m_levelLbl.setText( getLevelString() );
		}
		
		// Need to do requirements list somehow...
		if( m_reqPane != null )
		{
			m_reqPane.removeAll();
			Vector<String> rl = getRequirementsList();
			
			if( !rl.isEmpty() )
			{
				for( int i = 0; i < rl.size(); ++i )
				{
					JLabel reqLbl = new JLabel();
					reqLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
					reqLbl.setOpaque( false );
					reqLbl.setText( rl.elementAt( i ) );
					reqLbl.setIcon( Util.getIcon("item_quest") );
					reqLbl.setForeground( Color.RED );
					reqLbl.setBackground( m_reqPane.getBackground() );
					m_reqPane.add( reqLbl );
				}
				
				m_reqPane.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
			}

			m_reqPane.revalidate();
		}
		
		if( m_priceLbl != null )
		{
			m_priceLbl.setText( getPriceString() );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Object
	public String toString()
	{
		return getName();
	}

	@Override // GameObject
	public void dispose()
	{
		if( stack_next != null && getView() != null )
		{
			getView().setObject( stack_next );
			setView( null );
		}

		mere.removeObjInventaire( this );
		super.dispose();
	}

	@Override // GameObject
	public String getName()
	{
		String str = super.getName();
		int num = getStackCount();

		if( num > 1 )
		{
			str += " (x" + num + ")";
		}

		return str;
	}

	@Override
	public int compareTo( Object o )
	{
		// Return <0 if this object comes 'before' o, 0 if the same, and >0 if o comes first.
		ObjInventaire oo = (ObjInventaire)o;

		int t, ot;

		t = isUsable() ? 1 : 0;
		ot = oo.isUsable() ? 1 : 0;

		if( t != ot )
		{
			// Sort by usable, reversed (usable items come first)
			return -(t - ot);
		}

		if( type != oo.type )
		{
			// Sort by mapped type.
			return Util.ITEM_TYPES[type].order - Util.ITEM_TYPES[oo.type].order;
		}

		if( niveauR != oo.niveauR )
		{
			// Sort by required level (reversed)
			return -(niveauR - oo.niveauR);
		}

		// Sort by name
		return getName().compareTo( oo.getName() );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public static ObjInventaire load(SaveItem saveItem, PlayerStat p)
	{
		switch(saveItem.Type)
		{
			case Util.ITEM_WEAPON:
				return new Arme(saveItem.ObjectID,saveItem.BonusID,saveItem.Equipped, saveItem.Valeur, p);
			case Util.ITEM_ARMOR:
				return new Armure(saveItem.ObjectID,saveItem.BonusID, saveItem.Equipped, saveItem.Valeur, p);
			case Util.ITEM_AMULET:
				return new Amulet(saveItem.ObjectID,saveItem.BonusID,saveItem.Equipped,p);
			case Util.ITEM_BOOK:
				return new Livre(saveItem.ObjectID, p);
			case Util.ITEM_FOOD:
				return new Food(saveItem.ObjectID, p);
			case Util.ITEM_POTION:
				return new Potion(saveItem.ObjectID, p);
			case Util.ITEM_QUEST:
				return new Quest(saveItem.ObjectID,saveItem.BonusID,saveItem.Valeur,p);
			case Util.ITEM_RING:
				return new Bague(saveItem.ObjectID,saveItem.BonusID,saveItem.Equipped,p);
			case Util.ITEM_USABLE:
				return new Usable(saveItem.ObjectID,saveItem.Valeur,p);
		}
		return null;
	}
}
