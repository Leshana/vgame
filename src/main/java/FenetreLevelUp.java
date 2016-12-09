///////////////////////////////////////////////////////////////////////////////////////////////////
//
// FenetreLevelUp.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class FenetreLevelUp extends JDialog implements ActionListener
{
	private StatButton m_str, m_dex, m_end, m_int, m_chr;
	private JLabel m_level;

	private PlayerStat m_plr;

	///////////////////////////////////////////////////////////////////////////////////////////////
	protected FenetreLevelUp( PlayerStat plr )
	{
		if( plr == null )
		{
			dispose();
		}

		Rectangle b = VGame.instance.getBounds();
		
		int w = 300;
		int h = 450;

		b.setLocation( (int)(b.getX() + (b.getWidth() - w) / 2),
					   (int)(b.getY() + (b.getHeight() - h) / 2) );
		b.setSize( w, h );
		
		setBounds( b );
		setModal( true );
		setTitle( "Level Up" );
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setResizable( false );

		initializeComponents();
		
		setPlayer( plr );
		
		setVisible( true );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	void setPlayer( PlayerStat plr )
	{
		if( plr != m_plr && plr != null )
		{
			m_plr = plr;
			updateComponents();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void initializeComponents()
	{
		Dimension spacer = new Dimension( 5, 5 );

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout( new BoxLayout( headerPanel, BoxLayout.Y_AXIS ) );
		{
			JLabel title = new JLabel( "Level Up!" );
			title.setFont( title.getFont().deriveFont( 20.0f ) );
			title.setAlignmentX( 0.5f );
			
			m_level = new JLabel( Util.getIcon("stat_level") );
			m_level.setAlignmentX( 0.5f );
			
			JLabel subtitle = new JLabel( "Select a stat to increase" );
			subtitle.setAlignmentX( 0.5f );
			
			headerPanel.add( title );
			headerPanel.add( m_level );
			headerPanel.add( Box.createRigidArea( spacer ) );
			headerPanel.add( subtitle );
			headerPanel.add( Box.createRigidArea( spacer ) );
		}
			
		// Stat panel
		GridLayout statLayout = new GridLayout( 5, 1 );
		statLayout.setHgap( 5 );
		statLayout.setVgap( 5 );
		JPanel statPanel = new JPanel();
		statPanel.setLayout( statLayout );
		//statPanel.setPreferredSize( new Dimension( 5000, 5000 ) );
		{
			m_str = new StatButton( this, Util.STAT_STR );
			m_dex = new StatButton( this, Util.STAT_DEX );
			m_end = new StatButton( this, Util.STAT_END );
			m_int = new StatButton( this, Util.STAT_INT );
			m_chr = new StatButton( this, Util.STAT_CHR );
			
			statPanel.add( m_str );
			statPanel.add( m_dex );
			statPanel.add( m_end );
			statPanel.add( m_int );
			statPanel.add( m_chr );
		}
		
		Container pane = getContentPane();
		pane.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2 );
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		// header panel
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		add( headerPanel, c );
		
		// stat panel
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		pane.add( statPanel, c );
	}
	
	private void updateComponents()
	{
		m_level.setText( "Level " + (m_plr.niveau-1) + " -> " + m_plr.niveau );
		
		m_str.setStat( m_plr.strength );
		m_dex.setStat( m_plr.dexterity );
		m_end.setStat( m_plr.endurence );
		m_int.setStat( m_plr.intelligence );
		m_chr.setStat( m_plr.charisma );

		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ActionListener
	public void actionPerformed( ActionEvent e )
	{
		try
		{
			String ac = e.getActionCommand();
			
			if( ac == Util.STAT_TYPES[Util.STAT_STR].abbr )
			{
				m_plr.strength += 1;
			}
			else if( ac == Util.STAT_TYPES[Util.STAT_DEX].abbr )
			{
				m_plr.dexterity += 1;
			}
			else if( ac == Util.STAT_TYPES[Util.STAT_END].abbr )
			{
				m_plr.endurence += 1;
			}
			else if( ac == Util.STAT_TYPES[Util.STAT_INT].abbr )
			{
				m_plr.intelligence += 1;
			}
			else if( ac == Util.STAT_TYPES[Util.STAT_CHR].abbr )
			{
				m_plr.charisma += 1;
			}
		
            m_plr.actualiserPV();
			dispose();
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private class StatButton extends JButton
	{
		JLabel m_amtLabel;

		public StatButton( FenetreLevelUp host, int type )
		{
			Util.StatType st = Util.STAT_TYPES[type];
			
			{
				JPanel headerPanel = new JPanel();
				headerPanel.setLayout( new BoxLayout( headerPanel, BoxLayout.X_AXIS ) );
				headerPanel.setOpaque( false );
				{
					JLabel titleLabel = new JLabel( st.icon );
					titleLabel.setText( st.name );
					titleLabel.setFont( titleLabel.getFont().deriveFont( 16.0f ) );
					
					m_amtLabel = new JLabel();
					m_amtLabel.setFont( m_amtLabel.getFont().deriveFont( 16.0f ) );
					
					headerPanel.add( titleLabel );
					headerPanel.add( Box.createHorizontalGlue() );
					headerPanel.add( m_amtLabel );
				}
				
				JPanel descPanel = new JPanel();
				descPanel.setLayout( new GridLayout( 2, 1 ) );
				descPanel.setOpaque( false );
				{
					JLabel descLabel = new JLabel();// Util.getIcon( "item_quest" ) );
					descLabel.setText( st.desc );
					descLabel.setForeground( Color.DARK_GRAY );
					descLabel.setAlignmentX( 0.0f );
					
					JLabel effectLabel = new JLabel();// Util.getIcon( "item_quest" ) );
					effectLabel.setText( st.effect );
					effectLabel.setForeground( Color.DARK_GRAY );
					effectLabel.setAlignmentX( 0.0f );
					
					descPanel.add( descLabel );
					descPanel.add( effectLabel );
				}

				setActionCommand( st.abbr );
				addActionListener( host );
				setMargin( new Insets( 2, 8, 2, 8 ) );
				setFocusable( false );
				
				// Panel layout
				setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
				
				add( headerPanel );
				add( Box.createVerticalGlue() );
				add( descPanel );
				
				repaint();
			}
		}
		
		public void setStat( int stat )
		{
			m_amtLabel.setText( stat + " -> " + (stat+1) );
		}
	}
}
