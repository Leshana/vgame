///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Card.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
// ViewDialog - a generic information dialog pertaining to a game object
class ViewDialog extends JDialog implements FocusListener
{
	private GameObject m_obj;
	private ImagePanel m_imgPanel;

	private JPanel m_vitals;
	private JPanel m_details;
	
	private static Vector<ViewDialog> s_ViewDialogs = new Vector<ViewDialog>();
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public ViewDialog( GameObject obj )
	{
		super( VGame.instance );
		addFocusListener( this );

		Rectangle b = VGame.instance.getBounds();
		
		int w = 260;
		int h = 400;

		b.setLocation( (int)(b.getX() + (b.getWidth() - w) / 2),
					   (int)(b.getY() + (b.getHeight() - h) / 2) );
		b.setSize( w, h );
		
		setBounds( b );
		setTitle( "Info" );
		setAlwaysOnTop( true );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setResizable( false );

		initializeComponents();
		
		setObject( obj );
		
		s_ViewDialogs.add( this );
		setVisible( true );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setObject( GameObject obj )
	{
		if( obj != m_obj )
		{
			if( m_obj != null )
			{
				m_obj.setView( null );

				m_vitals.removeAll();
				m_details.removeAll();
				m_obj.cleanupView();
			}
			
			m_obj = obj;
			
			if( m_obj != null )
			{
				m_obj.setView( this );
				m_obj.initializeView( m_vitals, m_details );

				updateView();

				m_details.scrollRectToVisible( m_details.getBounds( null ) );
			}
			
			repaint();
		}
	}
	
	public void setName( String name )
	{
		setTitle( name );
		repaint();
	}
	
	public void setImage( Image img )
	{
		m_imgPanel.setImage( img );
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void updateView()
	{
		if( m_obj != null )
		{
			setName( m_obj.getName() );
			setImage( m_obj.getImage() );

			m_obj.updateView();
			
			m_vitals.revalidate();
			m_details.revalidate();

			repaint();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void initializeComponents()
	{
		// image/description pane
		m_imgPanel = new ImagePanel();
		m_imgPanel.setPreferredSize( new Dimension( 5000, 160 ) );
		m_imgPanel.setMinimumSize( new Dimension( 160, 160 ) );
		m_imgPanel.setMaximumSize( new Dimension( 5000, 160 ) );
		
		// 'vitals' pane - filled by user, always visible
		m_vitals = new JPanel();
		m_vitals.setLayout( new BoxLayout( m_vitals, BoxLayout.Y_AXIS ) );
		m_vitals.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		m_vitals.setMaximumSize( new Dimension( 5000, 5000 ) );
		m_vitals.setOpaque( false );
		
		// 'details' pane - filled by user, scrolls
		JPanel detailsPane = new JPanel();
		detailsPane.setLayout( new BoxLayout( detailsPane, BoxLayout.Y_AXIS ) );
		detailsPane.setBorder( BorderFactory.createLoweredBevelBorder() );
		detailsPane.setBackground( Color.LIGHT_GRAY );
		{
			m_details = new JPanel();
			m_details.setLayout( new BoxLayout( m_details, BoxLayout.Y_AXIS ) );
			m_details.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
			m_details.setMaximumSize( new Dimension( 5000, 5000 ) );
			m_details.setBackground( Color.LIGHT_GRAY );
		
			JScrollPane scroller = new JScrollPane( m_details,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
			scroller.getVerticalScrollBar().setUnitIncrement(16);
			
			detailsPane.add( scroller );
		}
		
		// Set up the page layout
		Container pane = getContentPane();
		pane.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		// Image
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		pane.add( m_imgPanel, c );
		
		// Vitals
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		pane.add( m_vitals, c );
		
		// Details
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 2;
		c.gridwidth = 1; c.gridheight = 1;
		pane.add( detailsPane, c );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // JDialog
	public void dispose()
	{
		s_ViewDialogs.remove( this );
		setObject( null );
		
		super.dispose();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // FocusListener
	public void focusGained(FocusEvent e)
	{
		// Move us to the 'end' of the view dialog list so the list is in bottom-up order.
		s_ViewDialogs.remove( this );
		s_ViewDialogs.add( this );
		
		if( e.getOppositeComponent() == null )
		{
			setAllVisible( true );
		}
	}

	@Override // FocusListener
	public void focusLost(FocusEvent e)
	{
		if( e.getOppositeComponent() == null )
		{
			setAllVisible( false );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public static void closeAll()
	{
		while( !s_ViewDialogs.isEmpty() )
		{
			s_ViewDialogs.lastElement().dispose();
		}
	}
	
	public static void updateAll()
	{
		for( int i = 0; i < s_ViewDialogs.size(); ++i )
		{
			s_ViewDialogs.elementAt( i ).updateView();
		}
	}
	
	public static void setAllVisible( boolean v )
	{
		for( int i = 0; i < s_ViewDialogs.size(); ++i )
		{
			s_ViewDialogs.elementAt( i ).setVisible( v );
		}
	}
}
