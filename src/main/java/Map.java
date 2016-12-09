///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Map.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;
import java.io.*;

class MapPropData implements Serializable
{
	public int width;
	public int height;
	public int art;
	public int x;
	public int y;
	
	public void setArt( String artName, Vector<String> workingPalette )
	{
		for (art = 0; art < workingPalette.size(); ++art)
		{
			if (artName.equals( workingPalette.get( art ) ))
			{
				break;
			}
		}
		
		if (art == workingPalette.size())
		{
			workingPalette.add( artName );
		}
	}
}

class PropData implements Serializable
{
	public int id;
	public int width;
	public int height;
	public String artName;
}

class MapData implements Serializable
{
	public int width;
	public int height;
	public int scriptEvent;
	public int loadEvent;
	public int northEvent, southEvent, eastEvent, westEvent;
	public String[] palette;
	public int[] tiles;
	public MapPropData[] props;
	
	public void setSize( int w, int h )
	{
		width = w;
		height = h;
		
		int size = w * h;
		tiles = new int[size];
		for (int i = 0; i < size; ++i)
		{
			tiles[i] = 0;
		}
	}
	
	public void setTile( int pos, String art, int event, int blocked, Vector<String> workingPalette )
	{
		int artIdx;
		for (artIdx = 0; artIdx < workingPalette.size(); ++artIdx)
		{
			if (art.equals( workingPalette.get( artIdx ) ))
			{
				break;
			}
		}
		
		if (artIdx == workingPalette.size())
		{
			workingPalette.add( art );
		}
		
		tiles[pos] = blocked | (artIdx << 4) | (event << 16);
	}
	
	public void setPalette( Vector<String> paletteVec )
	{
		palette = paletteVec.toArray( new String[0] );
		if (palette == null )
		{
			palette = new String[0];
		}
	}
	
	public void setProps( Vector<MapPropData> propVec )
	{
		props = propVec.toArray( new MapPropData[0] );
		if (props == null)
		{
			props = new MapPropData[0];
		}
	}
	
	public int getTileBlocked( int pos )
	{
		return tiles[pos] & 0xf;
	}
	
	public int getTileArt( int pos )
	{
		return (tiles[pos] >> 4) & 0xfff;
	}
	
	public int getTileEvent( int pos )
	{
		return (int)(short)((tiles[pos] >> 16) & 0xffff);
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////
// GameSprite variant for simple map props.
class PropSprite extends GameSprite
{
	public int m_w, m_h;
	public Image m_img;
	
	public PropSprite( Image img, int x, int y, int w, int h )
	{
		super();
		
		m_w = w;
		m_h = h;
		m_img = img;

		setPlacable( false );
		setLayer( -1 );
		setPosition( x, y );
	}
	
	public PropSprite( int id, int x, int y )
	{
		super();

		PropData data = DataUtil.getPropData( id );
		
		if (data != null)
		{
			m_w = data.width;
			m_h = data.height;
			m_img = ImageLibrairies.getImage( data.artName );
			
			setPlacable( false );
			setLayer( -1 );
			setPosition( x, y );
		}
	}
	
	@Override // GameSprite
	public void paint( Graphics g, int px, int py, ImageObserver obs )
	{
		// px, py is the pixel center of the tile we should draw in.
		py += Map.TILE_HEIGHT / 2;
		py -= m_h;
		
		px -= m_w / 2;

		g.drawImage( m_img, px, py, obs );
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
class Map
{
	///////////////////////////////////////////////////////////////////////////////////////////////
	public class Tile implements Comparable
	{
		public Point m_loc;
		public Image m_img;
		public int m_block;
		public int m_event;
		public boolean m_landmine;
		public GameSprite m_sprite;
		
		public Tile m_as_from;
		public int m_as_state;
		public int m_as_cost;
		public int m_as_heuristic;
		
		public Tile( Point loc )
		{
			m_loc = loc;

			m_img = null;
			m_block = 0;
			m_event = 0;
			m_landmine = false;
			m_sprite = null;
			
			m_as_from = null;
			m_as_state = AS_STATE_BASE;
			m_as_cost = 0;
			m_as_heuristic = 0;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////
		public void as_open( Tile from, Tile dest )
		{
			// Mark a tile as initially open, record its parent and move costs.
			m_as_state = AS_STATE_BASE + AS_OPEN;
			
			if( from != null )
			{
				m_as_from = from;
				m_as_cost = from.m_as_cost + as_getCost( from.m_loc, m_loc );
			}
			else
			{
				m_as_from = null;
				m_as_cost = 0;
			}
			
			m_as_heuristic = as_getCost( m_loc, dest.m_loc );
		}
		
		public boolean as_reOpen( Tile from )
		{
			// Attempt to re-open a square based on a new parent.  Returns true if the new parent
			//  would result in a lower move cost, returns false and does nothing if not.
			if( from != m_as_from )
			{
				int newcost = from.m_as_cost + as_getCost( from.m_loc, m_loc );

				if( newcost < m_as_cost )
				{
					m_as_from = from;
					m_as_cost = newcost;

					return true;
				}
			}
			
			return false;
		}
		
		public void as_close()
		{
			// Closes a square, preventing it from being tested or having its parent changed.
			m_as_state = AS_STATE_BASE + AS_CLOSED;
		}
		
		public void as_clear()
		{
			// Reverts open or closed status.
			m_as_state = AS_STATE_BASE;
		}
		
		public boolean as_isOpen()
		{
			// Returns true if a square is 'open'.
			return m_as_state == AS_STATE_BASE + AS_OPEN;
		}
		
		public boolean as_isClosed()
		{
			// Returns true if a square is 'closed'.
			return m_as_state == AS_STATE_BASE + AS_CLOSED;
		}
		
		public boolean as_canOpen()
		{
			// Returns true if a square hasn't been opened or closed yet.
			return m_as_state <= AS_STATE_BASE;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////
		@Override // Comparable
		public int compareTo( Object o )
		{
			// Sort based on move cost.  Used by a BinaryHeap during a-star.
			Tile ot = (Tile)o;
			return (m_as_cost + m_as_heuristic) - (ot.m_as_cost + ot.m_as_heuristic);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Tile[] m_tiles;
	public Vector<PropSprite> m_props;
	
	public int m_id;
	
	public int m_w, m_h;
	public int m_script_event;
	public int m_load_event;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public static final int TILE_WIDTH	= 16;
	public static final int TILE_HEIGHT	= 16;
	
	public static final int AS_OPEN				= 1;
	public static final int AS_CLOSED			= 2;
	
	public static int AS_STATE_BASE				= 0;
	public static final int AS_STATE_BASE_STEP	= 5;

	public static Point[] AS_AROUND_OFFSETS = new Point[] {
		new Point( -1, -1 ),
		new Point(  0, -1 ),
		new Point(  1, -1 ),
		new Point( -1,  0 ),
		new Point(  1,  0 ),
		new Point( -1,  1 ),
		new Point(  0,  1 ),
		new Point(  1,  1 ),
	};

	///////////////////////////////////////////////////////////////////////////////////////////////
	public Map( int id )
	{
		m_id = id;
		
		MapData data = DataUtil.getMapData( m_id );
		
		if (data != null)
		{
			int paletteSize = data.palette.length;
			Image[] paletteImages = new Image[paletteSize];
			
			for (int i = 0; i < paletteSize; ++i)
			{
				paletteImages[i] = ImageLibrairies.getImage( data.palette[i] );
			}
			
			HashMap<Integer,Integer> counts = new HashMap<Integer,Integer>();
		
			m_script_event = data.scriptEvent;
			m_load_event = data.loadEvent;
			m_w = data.width;
			m_h = data.height;
			
			int north = data.northEvent;
			int south = data.southEvent;
			int east = data.eastEvent;
			int west = data.westEvent;
				
			// Mark the exits with 0 counts so they don't get flagged as landmines.
			counts.put( new Integer( north ), new Integer( 0 ) );
			counts.put( new Integer( south ), new Integer( 0 ) );
			counts.put( new Integer( east ), new Integer( 0 ) );
			counts.put( new Integer( west ), new Integer( 0 ) );
				
			// Initialize the tile map, default blocker, and event values.
			m_tiles = new Tile[m_w * m_h];
			for( int y = 0; y < m_h; ++y )
			{
				for( int x = 0; x < m_w; ++x )
				{
					Tile t = new Tile( new Point( x+1, y+1 ) );
					
					if( y == 0 )
					{
						t.m_block = 1;
						t.m_event = north;
					}
					else if( y == m_h - 1 )
					{
						t.m_block = 1;
						t.m_event = south;
					}
					else if( x == 0 )
					{
						t.m_block = 1;
						t.m_event = west;
					}
					else if( x == m_w - 1 )
					{
						t.m_block = 1;
						t.m_event = east;
					}

					int idx = (y * m_w) + x;
					m_tiles[idx] = t;
				}
			}
			
			int blockedcount = 0;
			int tilecount = data.tiles.length;
			
			for (int pos = 0; pos < tilecount; ++pos)
			{
				Tile t = m_tiles[pos];
				
				// Look up the image index from the resolved palette
				t.m_img = paletteImages[data.getTileArt( pos )];
				
				// We can only ever _set_ the blocker value, so we don't need to read it if
				//  there's already a block in place.
				if (t.m_block == 0)
				{
					t.m_block = data.getTileBlocked( pos );
					++blockedcount;
				}
				
				int event = data.getTileEvent( pos );
				if( event >= 0 )
				{
					// Only copy the event if there is one - we don't want to clear any of the
					//  edge tiles' directional events.
					t.m_event = event;

					Integer idx = new Integer( event );
					if( counts.containsKey( idx ) )
					{
						counts.put( idx, new Integer( counts.get( idx ).intValue() + 1 ) );
					}
					else
					{
						counts.put( idx, new Integer( 1 ) );
					}
				}
			}
				
			// Determine which scripts constitute 'landmines' and flag their tiles so we know
			//  which ones to ignore.
			int threshold = (tilecount - blockedcount) / 3;

			for( int i = 0; i < m_tiles.length; ++i )
			{
				Tile t = m_tiles[i];
				
				if( t.m_event != 0 )
				{
					if( counts.get( new Integer( t.m_event ) ).intValue() > threshold )
					{
						t.m_landmine = true;
					}
				}
			}
			
			int propCount = data.props.length;
			
			m_props = new Vector<PropSprite>();
			m_props.ensureCapacity( propCount );
			
			for (int i = 0; i < propCount; ++i)
			{
				MapPropData propData = data.props[i];
				
				int w = propData.width;
				int h = propData.height;
				int x = propData.x;
				int y = propData.y;
				
				PropSprite prop = new PropSprite( paletteImages[propData.art], x, y, w, h );
					
				if( !isTileOutside( x-1, y-1 ) && getTile( x-1, y-1 ).m_block == 0 )
				{
					// If the origin tile of the prop is not blocked, then it's a decal.  Kind
					//  of a hack, but gets the job done.
					prop.setDecal( true );
				}
				
				m_props.add( prop );
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// A-star
	public static int as_getCost( Point a, Point b )
	{
		// Calculate the heuristic - this is the move cost assuming no obstacles.
		// Make as much of the path diagonal as possible, walk a straight line for the rest.
		//  The following calculation takes all directionalities into consideration.
		int min = Math.abs( b.x - a.x );
		int max = Math.abs( b.y - a.y );
	
		if( max < min )
		{
			int tmp = max;
			max = min;
			min = tmp;
		}
		
		return (min * 14) + ((max - min) * 10);
	}
	
	public static boolean as_isPathBlocked( Tile check, Tile end, GameSprite mover )
	{
		if( check.m_block == 0 && check.m_event <= 0 && check.m_sprite == null )
		{
			// There's nothing in the tile which could block our movement.
			return false;
		}
		else if( check.m_block != 0 && (end.m_event <= 0 || end.m_event != check.m_event) )
		{
			// The tile is blocked.
			return true;
		}
		else if( check.m_event > 0 && check.m_event != end.m_event && !check.m_landmine )
		{
			// There is an event in the square which isn't our destination event.
			return true;
		}
		else if( check.m_sprite != null && check.m_sprite != mover && check.m_sprite != end.m_sprite )
		{
			// There's a sprite which isn't either the mover or the end sprite in the tile.
			return true;
		}
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean as_findPathToTile( int fx, int fy, Tile to, GameSprite mover, Vector<Tile> out )
	{
		if( isTileOutside( fx-1, fy-1 ) || (fx == to.m_loc.x && fy == to.m_loc.y) )
		{
			// Can't start or end off the map, or we're already there.
			return false;
		}
		
		if( to.m_block != 0
			|| (to.m_sprite != null && to.m_sprite != mover)
			|| (to.m_event > 0 && !to.m_landmine) )
		{
			// Finding a path to a tile requires an unoccupied/unblocked tile as a destination.
			return false;
		}
		
		Tile from = getTile( fx-1, fy-1 );
		
		Vector<Tile> endv = new Vector<Tile>();
		endv.addElement( to );
		
		return as_findPath( from, endv, mover, out );
	}
	
	public boolean as_findPathToSprite( int fx, int fy, GameSprite to, GameSprite mover, Vector<Tile> out )
	{
		// Find a path to a given sprite.  Used primarily when chasing.
		if( isTileOutside( fx-1, fy-1 ) || to == null )
		{
			// Can't originate from an off-map position or travel to a null sprite.
			return false;
		}
		
		int fp = to.getFootprint();

		if( !to.isPlacable() || !to.isVisible() || fp <= 0 )
		{
			return false;
		}
		
		Tile from = getTile( fx-1, fy-1 );
		
		if( from.m_sprite == to || to == mover )
		{
			// Nothing to do.
			return false;
		}
		
		Vector<Tile> endv = new Vector<Tile>();

		// We want to move to a sprite, so use any of that sprite's squares as a potential end.
		int x = to.getX() - 1;
		int y = to.getY() - 1;
		
		for( int ox = -(fp / 2); ox <= (fp / 2); ++ox )
		{
			int px = x + ox;
			
			if( px < 0 || px >= m_w )
			{
				continue;
			}

			for( int oy = -(fp / 2); oy <= (fp / 2); ++oy )
			{
				int py = y + oy;
				
				if( py < 0 || py >= m_h )
				{
					continue;
				}

				Map.Tile t = getTile( px, py );
				
				if( t.m_sprite == to )
				{
					endv.addElement( t );
				}
			}
		}
		
		return as_findPath( from, endv, mover, out );
	}
	
	public boolean as_findPathToEvent( int fx, int fy, int eventID, GameSprite mover, Vector<Tile> out )
	{
		// We want to move to (and execute) an event, so use any squares with that event as a
		//  potential endpoint.
		if( isTileOutside( fx-1, fy-1 ) || eventID <= 0 )
		{
			// Can't originate from an off-map position or go to a non-event with this function.
			return false;
		}
		
		Tile from = getTile( fx-1, fy-1 );
		
		if( from.m_event == eventID )
		{
			// Nothing to do.
			return false;
		}

		Vector<Tile> endv = new Vector<Tile>();

		for( int i = 0; i < m_tiles.length; ++i )
		{
			Tile t = m_tiles[i];
			
			if( t.m_event == eventID )
			{
				endv.addElement( t );
			}
		}
		
		return as_findPath( from, endv, mover, out );
	}
	
	private boolean as_findPath( Tile from, Vector<Tile> end, GameSprite mover, Vector<Tile> out )
	{
		// This is a reverse a-star.  We start with several 'end' points and find the shortest path
		//  back to the 'start'.  The end result is that we find the shortest path to any point
		//  which matches our desired end criteria.
		
		// Reset the a-star data in the map.
		AS_STATE_BASE += AS_STATE_BASE_STEP;
		
		// Close all occupied/blocked squares.
		for( int i = 0; i < m_tiles.length; ++i )
		{
			Tile t = m_tiles[i];
			
			if( (t.m_block != 0)
				|| (t.m_event > 0 && !t.m_landmine && t.m_event != from.m_event)
				|| (t.m_sprite != null && t.m_sprite != mover) )
			{
				t.as_close();
			}
		}
		
		// Create 'open' list
		BinaryHeap open = new BinaryHeap( m_tiles.length );

		for( int i = 0; i < end.size(); ++i )
		{
			Tile t = end.elementAt( i );
			if( from == t )
			{
				// One of the end points _is_ our starting point, so that's easy.
				out.add( t );
				return true;
			}

			// Reset each endpoint.
			t.as_clear();
			
			// open each 'end' pointing at nothing
			t.as_open( null, from );
			open.push( t );
		}

		// While there are open tiles...
		while( !open.isEmpty() )
		{
			// Set 'current' to lowest cost 'open' node.
			//System.out.println( "Fetching next open tile");
			Tile current = (Tile)open.pop();
			//System.out.println( "Current " + current.m_loc + ", " + open.size() + " remaining open" );
			
			//  Close 'current'
			current.as_close();

			// Once the current square is the start square, we're done.  It means there are no other
			//  squares which could possibly have a lower path cost.
			// We short cut in the case that we hit a square which triggers the same event or
			//  contains the same object, though, since stepping there will cause the same effect.
			if( current == from )
			{
				//System.out.println( "  Found route!" );
				// Assemble the route
				while( current != null )
				{
					//System.out.println( "    Adding " + current.m_loc + " to out path" );
					out.addElement( current );
					current = current.m_as_from;
				}
				
				return true;
			}
			
			//  For each space around current
			for( int i = 0; i < AS_AROUND_OFFSETS.length; ++i )
			{
				int x = current.m_loc.x + AS_AROUND_OFFSETS[i].x;
				int y = current.m_loc.y + AS_AROUND_OFFSETS[i].y;

				if( isTileOutside( x-1, y-1 ) )
				{
					//System.out.println( "  Skipping " + x + ", " + y + " (outside)" );
					continue;
				}
				
				Tile check = getTile( x-1, y-1 );
				//System.out.println( "  Checking " + check.m_loc );
				
				if( check.as_canOpen() )
				{
					//System.out.println( "    Can open");
					// The tile hasn't been opened yet, so try that.
					if( as_isPathBlocked( check, from, mover ) )
					{
						// The tile is blocked, or would cause undesired interaction, so ignore it.
						//System.out.println( "    Is Blocked");
						continue;
					}
					else
					{
						//System.out.println( "    Opening " + check.m_loc );
						check.as_open( current, from );
						open.push( check );
					}
				}
				else if( check.as_reOpen( current ) )
				{
					// We were able to re-open the tile, so repush it in the open list.
					//System.out.println( "    Re-opened " + check.m_loc );
					open.repush( check );
				}
				else
				{
					//System.out.println( "    Can't open or re-open");
				}
			}
		}

		// If we got this far, there was no valid route.
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getTileIdx( int x, int y )
	{
		return (y * m_w) + x;
	}
	
	public Tile getTile( int x, int y )
	{
		return m_tiles[(y * m_w) + x];
	}
	
	public Tile getTile( int idx )
	{
		return m_tiles[idx];
	}
	
	public boolean isTileOutside( int x, int y )
	{
		return x < 0 || x >= m_w || y < 0 || y >= m_h;
	}
	
	public int getTileX( int x )
	{
		// Return the pixel offset from the left map border to the center of the tile in column x.
		return (TILE_WIDTH * x) + (TILE_WIDTH / 2);
	}
	
	public int getTileY( int y )
	{
		// Return the pixel offset from the top map border to the center of the tile in row y.
		return (TILE_HEIGHT * y) + (TILE_HEIGHT / 2);
	}
	
	
	public int getColAtX( int x )
	{
		// Return column index that passes through pixel offset x
		return x / TILE_WIDTH;
	}
	
	public int getRowAtY( int y )
	{
		// Return the row index that passes through pixel offset y
		return y / TILE_HEIGHT;
	}
}

