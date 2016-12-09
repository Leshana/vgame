///////////////////////////////////////////////////////////////////////////////////////////////////
//
// OrderedScroller.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class OrderedScroller extends JPanel implements ActionListener, ListSelectionListener, MouseListener
{
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected DefaultListModel m_model;
	protected JList m_list;
	protected JScrollPane m_scroller;
	protected JPanel m_buttons;
	protected JButton m_top, m_up, m_down, m_bottom, m_sort;
	
	protected JButton m_dclick;

	protected int m_desired_selection;
	
	protected boolean m_sorted;
	protected boolean m_enabled;
	protected boolean m_sorted_insert;
	protected boolean m_force_selection;

    private boolean m_valueChangedGate = false;
	
	public static final String MOVE_TOP_ACTION		= "MOVE_TOP";
	public static final String MOVE_BOTTOM_ACTION	= "MOVE_BOTTOM";
	public static final String MOVE_UP_ACTION		= "MOVE_UP";
	public static final String MOVE_DOWN_ACTION		= "MOVE_DOWN";
	public static final String SORT_ACTION			= "SORT";

	///////////////////////////////////////////////////////////////////////////////////////////////
	public OrderedScroller()
	{
		super();
		
		Dimension spacer = new Dimension( 5, 5 );
		
		m_desired_selection = 0;
		m_sorted = true;
		m_enabled = true;
		m_sorted_insert = false;
		m_force_selection = true;

		setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
		{
			m_model = new DefaultListModel();
			m_list = new JList( m_model );
			m_list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			m_list.addListSelectionListener(this);
			m_list.addMouseListener(this);
			m_list.setFocusable( false );

			m_scroller = new JScrollPane(m_list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			m_scroller.setFocusable( false );

			m_buttons = new JPanel();
			m_buttons.setLayout( new BoxLayout( m_buttons, BoxLayout.Y_AXIS ) );
			{
				m_top = new JButton( Util.getIcon("arrow_top") );
				m_top.setActionCommand(MOVE_TOP_ACTION);
				m_top.addActionListener(this);
				m_top.setFocusable( false );
				m_top.setMinimumSize(new Dimension(0,25));
				m_top.setPreferredSize(new Dimension(20,25));
				m_top.setMaximumSize(new Dimension(20,25));

				m_up = new JButton( Util.getIcon("arrow_up") );
				m_up.setActionCommand(MOVE_UP_ACTION);
				m_up.addActionListener(this);
				m_up.setFocusable( false );
				m_up.setPreferredSize(new Dimension(20,5000));
				m_up.setMaximumSize(new Dimension(20,5000));

				m_down = new JButton( Util.getIcon("arrow_down") );
				m_down.setActionCommand(MOVE_DOWN_ACTION);
				m_down.addActionListener(this);
				m_down.setFocusable( false );
				m_down.setPreferredSize(new Dimension(20,5000));
				m_down.setMaximumSize(new Dimension(20,5000));

				m_bottom = new JButton( Util.getIcon("arrow_bottom") );
				m_bottom.setActionCommand(MOVE_BOTTOM_ACTION);
				m_bottom.addActionListener(this);
				m_bottom.setFocusable( false );
				m_bottom.setMinimumSize(new Dimension(0,25));
				m_bottom.setPreferredSize(new Dimension(20,25));
				m_bottom.setMaximumSize(new Dimension(20,25));
			
				m_sort=new JButton( Util.getIcon("cmd_sort") );
				m_sort.setActionCommand(SORT_ACTION);
				m_sort.addActionListener( this );
				m_sort.setFocusable( false );
				m_sort.setMinimumSize(new Dimension(0,25));
				m_sort.setPreferredSize(new Dimension(20,25));
				m_sort.setMaximumSize(new Dimension(20,25));

				m_buttons.add( m_top );
				m_buttons.add( m_up );
				m_buttons.add( Box.createRigidArea(spacer) );
				m_buttons.add( m_down );
				m_buttons.add( m_bottom );
				m_buttons.add( Box.createRigidArea(spacer) );
				m_buttons.add( m_sort );
			}

			add( m_scroller );
			add( m_buttons );
		}
		
		updateEnabled();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Interface
	public void setSortedInsert( boolean sort )
	{
		// If the list is sorted, insert new items in their proper place instead of at the end.
		m_sorted_insert = sort;
	}
	
	public boolean getSortedInsert()
	{
		return m_sorted_insert;
	}
	
	public void setForceSelection( boolean force )
	{
		// If selection is forced, and there are ever no items selected, select the item nearest
		//  to the last recorded selection.
		m_force_selection = force;
		checkSelection();
	}
	
	public boolean getForceSelection()
	{
		return m_force_selection;
	}
	
	public void setButtonsVisible( boolean v )
	{
		// This will hide and show the reorder control area.
		m_buttons.setVisible( v );
		repaint();
	}
	
	public void setBlank( boolean b )
	{
		if( b != isBlank() )
		{
			DefaultListModel m = m_model;
			
			if( b )
			{
				m = new DefaultListModel();
			}
			
			m_list.setModel( m );

			checkSelection();
			updateEnabled();
			repaint();
		}
	}
	
	public boolean isBlank()
	{
		return m_list.getModel() != m_model;
	}
	
	public void setDoubleclickButton( JButton btn )
	{
		m_dclick = btn;
	}
	
	public void moveSelectedIndex( int offs, boolean wrap )
	{
		if( offs != 0 && !m_list.isSelectionEmpty() && m_model.size() > 1 )
		{
			int idx = m_list.getSelectedIndex();
			int size = m_model.size();
			
			if( !wrap && offs > 0 && idx + offs >= size )
			{
				idx = size - 1;
			}
			else if( !wrap && offs < 0 && idx + offs < 0 )
			{
				idx = 0;
			}
			else
			{
				while( offs < -idx )
				{
					offs += size;
				}

				idx += offs;
				idx %= size;
			}
			
			if( idx != m_list.getSelectedIndex() )
			{
				m_list.setSelectedIndex( idx );
				m_list.ensureIndexIsVisible( idx );
				repaint();
			}
		}
	}
	
	public void moveSelectedIndex( int offs )
	{
		moveSelectedIndex( offs, false );
	}
	
	public boolean isSorted()
	{
		return m_sorted;
	}
	
	public int addElement( Object o )
	{
		// Add the passed element to the list.  We'll decide where it goes.
		int i = m_model.size();
		
		if( m_sorted_insert )
		{
			// For sorted inserts, we start at the bottom of the list and move up until we find
			//  what looks like a good spot for this item.  Note that this could be totally wrong.
			for( ; i > 0; --i )
			{
				// Work backwards through the list to find the place where this item goes.
				if( ((Comparable)o).compareTo( m_model.elementAt( i - 1 ) ) >= 0 )
				{
					// c comes after the item currently before it, so this is a good place.
					break;
				}
			}
			
			m_model.add( i, o );
			
			// This won't change the 'sorted' status... Either it was sorted and will remain so, or
			//  it wasn't and this won't fix anything.  But, we do want to update our enabled
			//  status for some rare edge cases.
			updateEnabled();
		}
		else
		{
			// The list is not using sorted inserts, so put the item at the end.
			m_model.addElement( o );
			
			// If we think we're sorted, verify that that is still the case after the insert
			//  (likely not).
			if( m_sorted )
			{
				checkSorted();
			}
		}
		
		checkSelection();
		
		repaint();
		return i;
	}
	
	public void addElementAt( Object o, int pos )
	{
		// Insert the passed object at the passed position.  Use with care, this may unsort a
		//  sorted list.
		m_model.add( pos, o );

		checkSorted();
		checkSelection();
		
		repaint();
	}
	
	public int removeElement( Object o )
	{
		int i = m_model.indexOf( o );

		if( i >= 0 )
		{
			m_model.remove( i );
			checkSorted();
			checkSelection();
		}
		
		repaint();
		return i;
	}
	
	public Object removeElementAt( int i )
	{
		Object o = m_model.remove( i );
		checkSorted();
		checkSelection();
		
		repaint();
		return o;
	}
	
	public void removeAllElements()
	{
		m_model.removeAllElements();
		m_sorted = true;
		
		updateEnabled();
	}
	
	public int getElementCount()
	{
		return m_model.size();
	}
	
	public int findElement( Object o )
	{
		for( int i = 0; i < m_model.size(); ++i )
		{
			if( m_model.elementAt( i ) == o )
			{
				return i;
			}
		}

		return -1;
	}

	public void sort()
	{
		// Make sure the list is sorted.
		checkSorted();
		if( m_sorted )
		{
			// In the trivial case we can get out right away.
			return;
		}
		
		Object selected = getSelectedValue();
		int size = m_model.size();
		Object obs[] = new Object[size];
		
		// Move the objects into an array.
		for( int i = 0; i < size; ++i )
		{
			obs[i] = m_model.elementAt( i );
		}

		m_model.removeAllElements();
		m_desired_selection = -1;
		
		// Sort the array.
		Arrays.sort( obs );
		
		// Put the sorted array back into the model.
		for( int i = 0; i < size; ++i )
		{
			m_model.addElement( obs[i] );
			
			if( obs[i] == selected )
			{
				m_desired_selection = i;
			}
		}
		
		if( m_desired_selection != -1 )
		{
			m_valueChangedGate = true;
			m_list.setSelectedIndex( m_desired_selection );
			m_valueChangedGate = false;
		}

		m_sorted = true;

		updateEnabled();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Helpers
	protected void checkSorted()
	{
		// Go through the list and make sure it's sorted.  Sets the sorted flag if so, clears it if
		//  not.
		m_sorted = true;
		
		for( int i = 1; i < m_model.size(); ++i )
		{
			if( ((Comparable)m_model.elementAt(i-1)).compareTo( elementAt(i) ) > 0 )
			{
				m_sorted = false;
				break;
			}
		}
		
		updateEnabled();
	}
	
	protected void checkSelection()
	{
		if( m_list.isSelectionEmpty() && m_force_selection )
		{
			m_list.setSelectedIndex( 0 );
			m_list.ensureIndexIsVisible( 0 );
			repaint();
		}
	}
	
	protected void updateEnabled()
	{
		// Make sure that only appropriate controls are enabled.
		int size = m_model.size();
		int idx = m_list.getSelectedIndex();
		boolean blank = isBlank();
		
		m_scroller.setEnabled( m_enabled );
		m_list.setEnabled( m_enabled );

		m_up.setEnabled( !blank && m_enabled && size > 0 && idx > 0 );
		m_top.setEnabled( !blank && m_enabled && size > 0 && idx > 0 );
		m_down.setEnabled( !blank && m_enabled && size > 0 && idx < size - 1 );
		m_bottom.setEnabled( !blank && m_enabled && size > 0 && idx < size - 1 );
		
		m_sort.setEnabled( !blank && m_enabled && !m_sorted );
		
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Pass-throughs
	public Object elementAt( int i )
	{
		return m_model.elementAt( i );
	}
	
	public int getSelectedIndex()
	{
		return m_list.getSelectedIndex();
	}
	
	public Object getSelectedValue()
	{
		return m_list.getSelectedValue();
	}
	
	public void setSelectedIndex( int i )
	{
		m_list.setSelectedIndex( i );
		repaint();
	}
	
	public boolean isSelectionEmpty()
	{
		return m_list.isSelectionEmpty();
	}
	
	public void setCellRenderer( ListCellRenderer renderer )
	{
		m_list.setCellRenderer( renderer );
		repaint();
	}
	
	public void addListSelectionListener( ListSelectionListener l )
	{
		m_list.addListSelectionListener( l );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// Overrides
	@Override // JPanel
	public void setEnabled( boolean e )
	{
		super.setEnabled( e );

		if( m_enabled != e )
		{
			m_enabled = e;
			updateEnabled();
		}
	}
	
	@Override // JPanel
	public boolean isEnabled()
	{
		return m_enabled;
	}
	
	@Override // ActionListener
    public void actionPerformed( ActionEvent e )
    {
		try
		{
			int i = m_list.getSelectedIndex();
			int b = m_model.size() - 1;
			Object o = getSelectedValue();
			
			if( e.getActionCommand().equals( MOVE_TOP_ACTION )
				&& (m_enabled && i > 0) )
			{
				m_model.remove( i );
				m_model.add( 0, o );
				m_list.setSelectedIndex( 0 );
				checkSorted();
			}
			else if( e.getActionCommand().equals( MOVE_UP_ACTION )
				&& (m_enabled && i > 0) )
			{
				m_model.remove( i );
				m_model.add( i - 1, o );
				m_list.setSelectedIndex( i - 1 );
				checkSorted();
			}
			else if( e.getActionCommand().equals( MOVE_BOTTOM_ACTION )
				&& (m_enabled && i < b) )
			{
				m_model.remove( i );
				m_model.add( b, o );
				m_list.setSelectedIndex( b );
				checkSorted();
			}
			else if( e.getActionCommand().equals( MOVE_DOWN_ACTION )
				&& (m_enabled && i < b) )
			{
				m_model.remove( i );
				m_model.add( i + 1, o );
				m_list.setSelectedIndex( i + 1 );
				checkSorted();
			}
			else if( e.getActionCommand().equals( SORT_ACTION )
				&& (m_enabled && !m_sorted) )
			{
				sort();
			}
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
    }

    @Override // ListSelectionListener
	public void valueChanged( ListSelectionEvent e )
	{
		try
		{
			if( !m_valueChangedGate && !e.getValueIsAdjusting() )
			{
				m_valueChangedGate = true;
				
				int size = m_model.size();
				int idx = m_list.getSelectedIndex();

				if( m_desired_selection >= size )
				{
					m_desired_selection = size - 1;
				}
				
				// Enforce the selection rules if there are any and they are enforcable.
				if( idx != m_desired_selection )
				{
					if( idx >= 0 )
					{
						m_desired_selection = idx;
					}
					else if( m_force_selection && m_desired_selection >= 0 )
					{
						// Hopefully this won't fire another event...
						m_list.setSelectedIndex( m_desired_selection );
					}
				}
				
				// Enable/disable buttons that may or may not apply any more.
				updateEnabled();
				
				m_valueChangedGate = false;
			}
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
	}
	
	@Override // MouseListener
    public void mousePressed(MouseEvent e) {}
	@Override // MouseListener
    public void mouseReleased(MouseEvent e) {}
	@Override // MouseListener
    public void mouseEntered(MouseEvent e) {}
	@Override // MouseListener
    public void mouseExited(MouseEvent e) {}
	@Override // MouseListener
    public void mouseClicked(MouseEvent e)
    {
		try
		{
			if( m_dclick != null && m_dclick.isEnabled() && e.getClickCount() == 2 && !m_list.isSelectionEmpty() )
			{
				m_dclick.doClick();
			}
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
    }
}
