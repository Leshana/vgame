///////////////////////////////////////////////////////////////////////////////////////////////////
//
// CharacterPanel.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class CharacterPanel extends JPanel
{
	private PlayerStat m_player = null;

	TitledBorder m_border;
	private IconBar m_exp, m_hp, m_mp, m_time;
	private JLabel m_food, m_money;
	
	private JLabel m_str, m_dex, m_end, m_int, m_chr;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public CharacterPanel()
	{
		initializeComponents();
		updateStats();
	}
	
	public CharacterPanel( PlayerStat plr )
	{
		initializeComponents();
		setPlayer( plr );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setPlayer( PlayerStat plr )
	{
		m_player = plr;
		updateStats();
	}
	
	public void update()
	{
		updateStats();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void updateStats()
	{
		if( m_player != null )
		{
			m_border.setTitle( m_player.getName() );
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			
			int lastXP = (int)Util.levelCost( m_player.niveau - 1 );
			int nextXP = (int)Util.levelCost( m_player.niveau );
			int curXP = (int)(m_player.experience);
			
			// Exp
			m_exp.setMaximum( nextXP - lastXP );
			m_exp.setValue( curXP - lastXP );
			m_exp.setText( nf.format(m_player.niveau) + " " + m_player.family );
			
			// Gameplay
			m_hp.setMaximum( m_player.pvMax );
			m_hp.setValue( m_player.pv );
			m_hp.setText( nf.format(m_player.pv) + " / " + nf.format(m_player.pvMax) );
			
			m_mp.setMaximum( m_player.pmMax );
			m_mp.setValue( m_player.pm );
			m_mp.setText( nf.format(m_player.pm) + " / " + nf.format(m_player.pmMax) );

			m_food.setText( nf.format(m_player.nourriture) + " (" +
					(m_player.famine > 0 && m_player.nourriture >= m_player.famine ? nf.format(m_player.nourriture / m_player.famine) : "--") + ")" );
			m_money.setText( nf.format(m_player.money) + "" );
			
			// Stats
			m_str.setText( "" + m_player.strength );
			m_dex.setText( "" + m_player.dexterity );
			m_end.setText( "" + m_player.endurence );
			m_int.setText( "" + m_player.intelligence );
			m_chr.setText( "" + m_player.charisma );
			
			// Time
			Clock clock = m_player.clock;
			m_time.setValue( clock.getDisplayMinutes() );
			m_time.setText( clock.getTimeString() );
			
			String ttt = "Current Time is " + clock.getTimeString();

			if( m_player.clock.isDay() )
			{
				m_time.setIcon( Util.getIcon( "time_day" ) );
				ttt += " during the day";
			}
			else
			{
				m_time.setIcon( Util.getIcon( "time_night" ) );
				ttt += " at night";
			}
			
			ttt += " on " + clock.getDateString();
			m_time.setToolTipText( ttt );
		}
		else
		{
			// No selection, blank out the fields.
			m_border.setTitle( "Character" );
			
			// Exp
			m_exp.setMaximum( 1 );
			m_exp.setValue( 0 );
			m_exp.setText( "" );
			
			// Gameplay
			m_hp.setMaximum( 1 );
			m_hp.setValue( 0 );
			m_hp.setText( "" );
			
			m_mp.setMaximum( 1 );
			m_mp.setValue( 0 );
			m_mp.setText( "" );
			
			m_food.setText("");
			m_money.setText("");
			
			// Stats
			m_str.setText( "" );
			m_dex.setText( "" );
			m_end.setText( "" );
			m_int.setText( "" );
			m_chr.setText( "" );
			
			// Time
			m_time.setValue( 0 );
			m_time.setText( "" );
			m_time.setIcon( Util.getIcon("time_night") );
			m_time.setToolTipText( "Current Time" );
		}
		
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void initializeComponents()
	{
		Dimension spacer = new Dimension( 5, 5 );
		
		m_border = BorderFactory.createTitledBorder( "" );
		setBorder( m_border );
		setPreferredSize( new Dimension( 250, 0 ) );
		setMinimumSize( new Dimension( 250, 0 ) );
		
		JPanel charPanel = new JPanel();
		//charPanel.setPreferredSize( new Dimension( 200, 18 ) );
		//charPanel.setMinimumSize( new Dimension( 200, 18 ) );
		charPanel.setLayout( new BoxLayout( charPanel, BoxLayout.Y_AXIS ) );
		{
			m_exp = new IconBar( Util.getIcon("stat_level") );
			m_exp.setForeground( Util.COLOR_XP_FORE );
			m_exp.setBackground( Util.COLOR_XP_BACK );
			
			charPanel.add( m_exp );
		}
		
		// Bar panel
		JPanel barPanel = new JPanel();
		//barPanel.setMinimumSize( new Dimension( 100, 36 ) );
		barPanel.setLayout( new BoxLayout( barPanel, BoxLayout.Y_AXIS ) );
		{
			m_hp = new IconBar( Util.getIcon("stat_hp") );
			m_hp.setForeground( Util.COLOR_HP_FORE );
			m_hp.setBackground( Util.COLOR_HP_BACK );
			m_hp.setToolTipText( "Health" );
			
			m_mp = new IconBar( Util.getIcon("stat_mp") );
			m_mp.setForeground( Util.COLOR_MP_FORE );
			m_mp.setBackground( Util.COLOR_MP_BACK );
			m_mp.setToolTipText( "Mana" );
			
			m_food = new JLabel( Util.getIcon("item_food") );
			m_food.setToolTipText( "Food (Turns until Empty)" );
			m_money = new JLabel( Util.getIcon("item_gold") );
			m_money.setToolTipText( "Gold" );
			
			barPanel.add( m_hp );
			barPanel.add( m_mp );
			barPanel.add( Box.createVerticalGlue() );
			barPanel.add( m_food );
			barPanel.add( m_money );
		}
		
		// Stat panel
		JPanel statPanel = new JPanel();
		statPanel.setPreferredSize( new Dimension( 60, 80 ) );
		statPanel.setMinimumSize( new Dimension( 60, 80 ) );
		statPanel.setLayout( new BoxLayout( statPanel, BoxLayout.Y_AXIS ) );
		{
			m_str = new JLabel( Util.STAT_TYPES[Util.STAT_STR].icon );
			m_dex = new JLabel( Util.STAT_TYPES[Util.STAT_DEX].icon );
			m_end = new JLabel( Util.STAT_TYPES[Util.STAT_END].icon );
			m_int = new JLabel( Util.STAT_TYPES[Util.STAT_INT].icon );
			m_chr = new JLabel( Util.STAT_TYPES[Util.STAT_CHR].icon );
			
			m_str.setToolTipText( Util.STAT_TYPES[Util.STAT_STR].name );
			m_dex.setToolTipText( Util.STAT_TYPES[Util.STAT_DEX].name );
			m_end.setToolTipText( Util.STAT_TYPES[Util.STAT_END].name );
			m_int.setToolTipText( Util.STAT_TYPES[Util.STAT_INT].name );
			m_chr.setToolTipText( Util.STAT_TYPES[Util.STAT_CHR].name );
			
			statPanel.add( m_str );
			statPanel.add( m_dex );
			statPanel.add( m_end );
			statPanel.add( m_int );
			statPanel.add( m_chr );
		}
		
		JPanel timePanel = new JPanel();
		timePanel.setLayout( new BoxLayout( timePanel, BoxLayout.Y_AXIS ) );
		{
			m_time = new IconBar( Util.getIcon("time_night") );
			m_time.setMaximum( Clock.MINUTES_PER_DAY );
			m_time.setForegroundList( Util.COLOR_TIME_FORE );
			m_time.setBackgroundList( Util.COLOR_TIME_BACK );
			
			timePanel.add( m_time );
		}

		// And finally the actual page layout.
		setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2 );
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		// XP bar - top row
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 1;
		add( charPanel, c );
		
		// Bar panel (hp, mp, food, money)
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		add( barPanel, c );
		
		// Stat panel (char stats)
		c.weightx = 0.0; c.weighty = 0.0;
		c.gridx = 1; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		add( statPanel, c );
		
		// Time panel
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 2;
		c.gridwidth = 2; c.gridheight = 1;
		add( timePanel, c );
	}
}
