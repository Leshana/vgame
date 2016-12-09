///////////////////////////////////////////////////////////////////////////////////////////////////
//
// GamePanel.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Vector;
import java.awt.event.*;
import java.awt.image.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Panel that handles drawing game environment each frame.  Parses the map grid and manages the
//  sprite list, handles updating images each frame.
class GamePanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener
{
	protected Map m_map = null;
	protected int m_mapX = 0;
	protected int m_mapY = 0;
	protected Vector<GameSprite> m_sprites;
	protected int m_cursorX = -1;
	protected int m_cursorY = -1;
	GameSprite m_selected = null;
	
	protected boolean m_showPaths = false;
	
	protected Vector<Map.Tile> m_tilePath = null;
	protected Vector<Map.Tile> m_spritePath = null;
	protected Vector<Map.Tile> m_eventPath = null;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public GamePanel()
	{
		setDoubleBuffered( true );
		setBackground( Color.DARK_GRAY );
		setBorder( BorderFactory.createLoweredBevelBorder() );
		addComponentListener( this );
		addMouseListener( this );
		addMouseMotionListener( this );

		m_sprites = new Vector<GameSprite>();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getMapX()
	{
		return m_mapX;
	}
	
	public int getMapY()
	{
		return m_mapY;
	}
	
	public int getMapX( int x )
	{
		// Return the pixel within the panel that is x pixels horizontally from the map origin.
		return m_mapX + x;
	}
	
	public int getMapY( int y )
	{
		// Return the pixel within the panel that is y pixels vertically from the map origin.
		return m_mapY + y;
	}
	
	public int getMapTileX( int x )
	{
		// Return the pixel offset of the center of tile column x.
		return m_mapX + m_map.getTileX( x );
	}
	
	public int getMapTileY( int y )
	{
		// Return the pixel offset of the center of tile row y.
		return m_mapY + m_map.getTileY( y );
	}
	
	public int getColAtX( int x )
	{
		// Return column index that passes through pixel offset x
		return m_map.getColAtX( x - m_mapX );
	}
	
	public int getRowAtY( int y )
	{
		// Return the row index that passes through pixel offset y
		return m_map.getRowAtY( y - m_mapY );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setMap( Map map )
	{
		if( m_map != map )
		{
			m_map = map;

			clearCursor();
			setSelectedSprite( null );
			m_sprites.removeAllElements();
			
			if( m_map != null )
			{
				// Add the map's props to our sprite list.
				for( int i = 0; i < m_map.m_props.size(); ++i )
				{
					// The prop will automatically register itself on this panel.
					m_map.m_props.elementAt( i ).setPanel( this );
				}
			}
			
			updateOrigin();
		}
	}
	
	public void setMap( int id )
	{
		setMap( new Map( id ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void findTestPaths()
	{
		if( !m_showPaths || !isCursorOnscreen() )
		{
			return;
		}
		
		GameSprite plr = VGame.instance.getPlayer();
		Map.Tile dest = m_map.getTile( m_cursorX, m_cursorY );
		
		if( plr != null )
		{
			{
				Vector<Map.Tile> path = new Vector<Map.Tile>();
				if( m_map.as_findPathToTile( plr.getX(), plr.getY(), dest, plr, path ) )
				{
					m_tilePath = path;
				}
			}
			
			if( dest.m_sprite != null )
			{
				Vector<Map.Tile> path = new Vector<Map.Tile>();
				if( m_map.as_findPathToSprite( plr.getX(), plr.getY(), dest.m_sprite, plr, path ) )
				{
					m_spritePath = path;
				}
			}
			
			if( dest.m_event > 0 && !dest.m_landmine )
			{
				Vector<Map.Tile> path = new Vector<Map.Tile>();
				if( m_map.as_findPathToEvent( plr.getX(), plr.getY(), dest.m_event, plr, path ) )
				{
					m_eventPath = path;
				}
			}
		}

		repaint();
	}
	
	protected void clearTestPaths()
	{
		m_tilePath = null;
		m_spritePath = null;
		m_eventPath = null;
	}
	
	public void setCursor( int x, int y )
	{
		x = Math.max( Math.min( x, m_map.m_w - 1 ), 0 );
		y = Math.max( Math.min( y, m_map.m_h - 1 ), 0 );
		
		if( x != m_cursorX || y != m_cursorY )
		{
			m_cursorX = x;
			m_cursorY = y;
			
			clearTestPaths();
			
			if( m_selected != null )
			{
				// Re-place the selected sprite in case we're moving away from it in order to make
				//  it take up all nine of its squares, visually.
				m_selected.place();
			}
			
			if( isCursorOnscreen() )
			{
				Map.Tile t = m_map.getTile( m_cursorX, m_cursorY );
				if( t.m_sprite != null )
				{
					// Re-place the sprite that occupies the square we just selected. so it will
					//  take up all nine of its squares, visually.  This is like crowd hysteresis.
					t.m_sprite.place();
				}
				
				findTestPaths();
			}

			repaint();
		}
	}
	
	public void clearCursor()
	{
		if( isCursorOnscreen() )
		{
			m_cursorX = -1;
			m_cursorY = -1;
			
			clearTestPaths();
	
			repaint();
		}
	}
	
	public int getCursorX()
	{
		return m_cursorX;
	}
	
	public int getCursorY()
	{
		return m_cursorY;
	}
	
	public boolean isCursorOnscreen()
	{
		return m_cursorX >= 0 && m_cursorY >= 0;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setShowPaths( boolean s )
	{
		if( s != m_showPaths )
		{
			clearTestPaths();
			
			m_showPaths = s;
			
			findTestPaths();
		}
	}
	
	public boolean getShowPaths()
	{
		return m_showPaths;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setSelectedSprite( GameSprite spr )
	{
		if( spr != m_selected )
		{
			m_selected = spr;
			
			if( m_selected != null )
			{
				m_selected.place();
			}

			repaint();
		}
	}
	
	public GameSprite getSelectedSprite()
	{
		return m_selected;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void removeSprite( GameSprite spr )
	{
		// Remove the sprite from the list.
		if( !m_sprites.contains( spr ) )
		{
			// The sprite isn't in our list, so removing it wouldn't really accomplish much.  Might
			//  want to throw here?
			return;
		}

		m_sprites.remove( spr );
		
		if( spr.isVisible() )
		{
			repaint();
		}
	}
	
	public void addSprite( GameSprite spr )
	{
		// Add the sprite into the list in a valid location.
		if( m_sprites.contains( spr ) )
		{
			// The sprite is already in our list.  Likely not a good thing, but you could argue
			//  against throwing.
			return;
		}
		
		int i = m_sprites.size();
		
		for( ; i > 0; --i )
		{
			// Work backwards through the list to find the place where this item goes.
			if( spr.compareTo( m_sprites.elementAt( i - 1 ) ) >= 0 )
			{
				// spr comes after the sprite currently before it, so this is a good place.
				break;
			}
		}
	
		m_sprites.add( i, spr );
		
		if( spr.isVisible() )
		{
			repaint();
		}
	}
	
	public void resortSprite( GameSprite spr )
	{
		// Called from GameSprite when a sprite assigned to this panel may need resorting.
		
		// If there's only one sprite in the list, we don't need to resort (whether or not the
		//  sprite is one of ours).
		if( m_sprites.size() == 1 )
		{
			return;
		}
		
		int idx = m_sprites.indexOf( spr );
		
		if( idx < 0 )
		{
			// The sprite isn't in our list.  Should really throw an exception here.
			return;
		}

		// Check the sprites before and after this one.  If we compare favorably to each one, no
		//  need to do anything.
		if( (idx == 0 || spr.compareTo( m_sprites.elementAt( idx - 1 ) ) >= 0) &&
			(idx == m_sprites.size() - 1 || spr.compareTo( m_sprites.elementAt( idx + 1 ) ) <= 0) )
		{
			return;
		}
		
		// A resort is easy.  Just remove then re-add it.  The sort will happen during the add.
		removeSprite( spr );
		addSprite( spr );
	}
	
	public void liftSprite( GameSprite spr )
	{
		int fp = spr.getFootprint();
		int x = spr.getX() - 1;
		int y = spr.getY() - 1;
		
		for( int ox = -(fp / 2); ox <= (fp / 2); ++ox )
		{
			int px = x + ox;
			
			if( px < 0 || px >= m_map.m_w )
			{
				continue;
			}

			for( int oy = -(fp / 2); oy <= (fp / 2); ++oy )
			{
				int py = y + oy;
				
				if( py < 0 || py >= m_map.m_h )
				{
					continue;
				}

				Map.Tile t = m_map.getTile( px, py );
				
				if( t.m_sprite == spr )
				{
					t.m_sprite = null;
				}
			}
		}
	}
	
	public void placeSprite( GameSprite spr )
	{
		int fp = spr.getFootprint();

		if( !spr.isPlacable() || !spr.isVisible() || fp <= 0 )
		{
			return;
		}

		int x = spr.getX() - 1;
		int y = spr.getY() - 1;
		
		for( int ox = -(fp / 2); ox <= (fp / 2); ++ox )
		{
			int px = x + ox;
			
			if( px < 0 || px >= m_map.m_w )
			{
				continue;
			}

			for( int oy = -(fp / 2); oy <= (fp / 2); ++oy )
			{
				int py = y + oy;
				
				if( py < 0 || py >= m_map.m_h )
				{
					continue;
				}

				Map.Tile t = m_map.getTile( px, py );
				
				if( t.m_sprite == null
					|| t.m_sprite.getX() - 1 != px
					|| t.m_sprite.getY() - 1 != py )
				{
					t.m_sprite = spr;
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void updateOrigin()
	{
		// Start with the center of the panel.
		m_mapX = (getWidth() / 2);
		m_mapY = (getHeight() / 2);
		
		// Offset to a below-center point (to address the height of the characters)
		int offsY = Map.TILE_HEIGHT * 4;
		m_mapY += offsY;

		// Factor in the map size.
		if( m_map != null )
		{
			int w = m_map.m_w * Map.TILE_WIDTH;
			int h = m_map.m_h * Map.TILE_HEIGHT;
			
			m_mapX -= w / 2;
			m_mapY -= h / 2;
		
			// Figure out if we've pushed the map too low.
			int over = (m_mapY + h) - getHeight();
			if( over > 0 )
			{
				// We don't correct by more than the initial offset so on a way-too-small screen
				//  the map will still appear 'centered' (but cut off on all sides).
				m_mapY -= Math.min( over, offsY );
			}
		}
		
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Component
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		if( m_map == null )
		{
			return;
		}

		Map.Tile cursor = null;
		if( isCursorOnscreen() )
		{
			cursor = m_map.getTile( m_cursorX, m_cursorY );
		}
	  
		// Paint the map tiles.
		int idx = 0;
		for( int y = 0, py = m_mapY; y < m_map.m_h; ++y, py += Map.TILE_HEIGHT )
		{
			for( int x = 0, px = m_mapX; x < m_map.m_w; ++x, px += Map.TILE_WIDTH )
			{
				Map.Tile t = m_map.getTile( idx );
				g.drawImage( t.m_img, px, py, this );

				if( cursor != null && cursor.m_event > 0 && !cursor.m_landmine && (t.m_event == cursor.m_event) )
				{
					Color oc = g.getColor();
					g.setColor( new Color( 255, 255, 0, 64 ) );
					g.fillRect( px, py, Map.TILE_WIDTH, Map.TILE_HEIGHT );
					g.setColor( oc );
				}

				++idx;
			}
		}

		if( cursor != null && cursor.m_sprite == null && cursor.m_event <= 0 )
		{
			dot( g,	m_cursorX, m_cursorY,
					Map.TILE_WIDTH, Map.TILE_HEIGHT,
					new Color( 255, 255, 255, 128 ) );
		}
		
		if( cursor != null && cursor.m_sprite != null )
		{
			dot( g,	cursor.m_sprite.getX() - 1, cursor.m_sprite.getY() - 1,
					Map.TILE_WIDTH * 3, Map.TILE_HEIGHT * 3,
					new Color( 0, 0, 255, 64 ) );
		}
		
		if( m_selected != null )
		{
			dot( g,	m_selected.getX() - 1, m_selected.getY() - 1,
					Map.TILE_WIDTH * 3, Map.TILE_HEIGHT * 3,
					new Color( 0, 255, 0, 64 ) );
		}
		
		tracePath( g, m_tilePath, new Color( 255, 255, 255 ) );
		tracePath( g, m_spritePath, new Color( 0, 0, 255 ) );
		tracePath( g, m_eventPath, new Color( 255, 255, 0 ) );

		// Paint any Sprites.  Sprites consist of props and characters - they're all sorted
		//  when placed into the list.
		int ox = m_mapX - (Map.TILE_WIDTH / 2);
		int oy = m_mapY - (Map.TILE_HEIGHT / 2);

		for( int i = 0; i < m_sprites.size(); i++ )
		{
			GameSprite s = m_sprites.elementAt( i );

			if( s.isVisible() )
			{
				// Find the center of the tile.
				int px = ox + (s.getX() * Map.TILE_WIDTH);
				int py = oy + (s.getY() * Map.TILE_HEIGHT);

				// Tell the sprite to paint.
				s.paint( g, px, py, this );
			}
		}
	}
	
	protected void tracePath( Graphics g, Vector<Map.Tile> path, Color c )
	{
		if( path != null )
		{
			for( int i = 0; i < path.size(); ++i )
			{
				Map.Tile t = path.elementAt( i );
				dot( g, t.m_loc.x - 1, t.m_loc.y - 1, 8, 8, c );
			}
		}
	}
	
	protected void dot( Graphics g, int x, int y, int sx, int sy, Color c )
	{
		x *= Map.TILE_WIDTH;
		x += Map.TILE_WIDTH / 2;
		x += m_mapX;
		x -= sx / 2;
		
		y *= Map.TILE_HEIGHT;
		y += Map.TILE_HEIGHT / 2;
		y += m_mapY;
		y -= sy / 2;

		Color oc = g.getColor();

		g.setColor( c );
		g.fillOval( x, y, sx, sy );
		
		g.setColor( new Color( 0, 0, 0, 128 ) );
		g.drawOval( x, y, sx, sy );

		g.setColor( oc );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // MouseListener
	public void mousePressed(MouseEvent e) {}
	@Override // MouseListener
	public void mouseReleased(MouseEvent e) {}
	@Override // MouseListener
	public void mouseEntered(MouseEvent e)
	{
		setCursor( getColAtX( e.getX() ), getRowAtY( e.getY() ) );
	}
	@Override // MouseListener
	public void mouseExited(MouseEvent e)
	{
		clearCursor();
	}
	@Override // MouseListener
	public void mouseClicked(MouseEvent e) {}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // MouseMotionListener
	public void mouseMoved( MouseEvent e )
	{
		setCursor( getColAtX( e.getX() ), getRowAtY( e.getY() ) );
    }

	@Override // MouseMotionListener
    public void mouseDragged( MouseEvent e )
	{
		setCursor( getColAtX( e.getX() ), getRowAtY( e.getY() ) );
    }

	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ComponentListener
	public void componentResized(ComponentEvent e)
	{
		updateOrigin();
	}
	
	@Override // ComponentListener
	public void componentHidden(ComponentEvent e) {}

	@Override // ComponentListener
	public void componentMoved(ComponentEvent e) {}
	
	@Override // ComponentListener
	public void componentShown(ComponentEvent e) {}
}

