///////////////////////////////////////////////////////////////////////////////////////////////////
//
// GameSprite.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.image.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
abstract class GameSprite implements Comparable
{
	private int m_x, m_y;
	private int m_layer;
	private boolean m_decal;
	private GamePanel m_panel;
	private boolean m_visible;
	private boolean m_placable;
	private int m_footprint;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public GameSprite()
	{
		m_x = m_y = 0;
		m_layer = 0;
		m_visible = true;
		m_placable = true;
		m_panel = null;
		m_footprint = 3;
	}
	
	public GameSprite( GamePanel panel )
	{
		this();
	
		setPanel( panel );
	}
	
	public GameSprite( GamePanel panel, int x, int y, int layer )
	{
		this();
		
		m_x = x;
		m_y = y;
		
		setPanel( panel );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setPosition( int x, int y )
	{
		if( m_x != x || m_y != y )
		{
			lift();
			
			m_x = x;
			m_y = y;
			resort();

			place();
		}
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setFootprint( int s )
	{
		if( s != m_footprint )
		{
			lift();

			m_footprint = s;

			place();
		}
	}
	
	public int getFootprint()
	{
		return m_footprint;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setLayer( int l )
	{
		if( m_layer != l )
		{
			m_layer = l;
			resort();
		}
	}
	
	public int getLayer()
	{
		return m_layer;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setDecal( boolean d )
	{
		// Decal sprites are painted 'under' everything else.
		if( m_decal != d )
		{
			lift();
			
			m_decal = d;
			resort();
			
			place();
		}
	}
	
	public boolean isDecal()
	{
		return m_decal;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setVisible( boolean v )
	{
		// Set our visibility.  If it changes, force a repaint (visibility isn't a sort factor, so
		//  a full resort isn't necessary).
		if( m_visible != v )
		{
			lift();
			
			m_visible = v;
			repaint();
			
			place();
		}
	}
	
	public boolean isVisible()
	{
		return m_visible;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setPlacable( boolean p )
	{
		if( p != m_placable )
		{
			lift();
			m_placable = p;
			place();
		}
	}
	
	public boolean isPlacable()
	{
		return m_placable;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void lift()
	{
		// Remove this sprite from its current location in the game panel.
		if( m_panel != null )
		{
			m_panel.liftSprite( this );
		}
	}
	
	public void place()
	{
		// Place this sprite into the game panel at its current location.
		if( m_panel != null )
		{
			m_panel.placeSprite( this );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void repaint()
	{
		// Force a repaint in the panel.  Should be called when this object is changed in a way
		//  that the panel can't detect.
		if( m_panel != null )
		{
			m_panel.repaint();
		}
	}
	
	public void resort()
	{
		// Force a re-sort of this sprite
		if( m_panel != null )
		{
			m_panel.resortSprite( this );
		}
	}
	
	public void setPanel( GamePanel panel )
	{
		// Set (or change) the panel this sprite is associated with.
		lift();

		if( m_panel != null )
		{
			m_panel.removeSprite( this );
		}
		
		m_panel = panel;
		
		if( m_panel != null )
		{
			m_panel.addSprite( this );
		}
		
		place();
	}
	
	public void remove()
	{
		// Disconnect us from our panel.  This must be done before this sprite is discarded,
		//  otherwise the panel will retain a handle.  Equivalent to setPanel( null ).
		if( m_panel != null )
		{
			lift();
			m_panel.removeSprite( this );
			m_panel = null;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Comparable
	public int compareTo( Object o )
	{
		GameSprite os = (GameSprite)o;
		
		int t = m_decal ? 1 : 0;
		int ot = os.m_decal ? 1 : 0;
		
		if( t != ot )
		{
			// Sort on decal status, reversed (decals draw first)
			return -(t - ot);
		}
		else if( m_y != os.m_y )
		{
			// Sort by y position (lower y draws first)
			return m_y - os.m_y;
		}
		else
		{
			// Sort by layer (lower layer draws first)
			return m_layer - os.m_layer;
		}
	}
	
	public abstract void paint( Graphics g, int px, int py, ImageObserver obs );
}

