///////////////////////////////////////////////////////////////////////////////////////////////////
//
// FenetreTest.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.net.URL;
import java.io.*;
import java.util.*;
import java.awt.event.*;

class EventData implements Serializable
{
	public int EventID; // Key field (must come first)
	public String Script;
}

class Event
{
	private static Hashtable<Integer,Event> eventTable = new Hashtable<>();
	
	private EventData m_data;
	private ProcessedScript m_script;
	
	public static Event getEvent( int eventId )
	{
		Integer key = new Integer( eventId );
		Event ret = eventTable.get( key );
		
		if (ret == null)
		{
			ret = new Event( eventId );
			eventTable.put( key, ret );
		}
		
		return ret;
	}
	
	public Event( int eventId )
	{
		m_data = DataUtil.getEventData( eventId );
		m_script = new ProcessedScript( m_data.Script );
	}
	
	public ProcessedScript getScript()
	{
		return m_script;
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class FenetreTest extends JFrame implements KeyListener, ListSelectionListener, MouseListener, FocusListener
{
	public static FenetreTest instance = null;

	public Vector<ObjetGraphique> objetG;
	//private ModificationConteneur action;
	
	private boolean fini;
	public boolean teleport=true;
	public int voreEnabled = 0;
	private Projectile projectile = null;
	public ObjetGraphique src, tgt;
	private String current_save_file = null;
	
	// Options
	public int speed = 0;
	private boolean soundEnabled = true;
	private boolean scatEnabled = true;
	private boolean moveButtonsVisible = true;
	private boolean failChecks = false;
	
	private boolean itemsEnabled = true;
	private boolean spellsEnabled = true;
	private boolean saveEnabled = true;
	
	public Map map = null;
	
	public FenetreDialogue dialogue=null;
	
	// Menu
	private JMenuItem qsaveItem, qloadItem;
	private JCheckBoxMenuItem soundMenuItem, scatMenuItem, failChecksMenuItem, moveButtonsMenuItem;
	private JCheckBoxMenuItem testPathsMenuItem;
	
	// Gameboard
	public GamePanel gamePanel;
	
	// Inventory
	public OrderedScroller inventaire;
	private JButton boutonSell, boutonView, boutonUse;
	
	// Spellbook
	public OrderedScroller spellbook;
	private JButton boutonInfo, boutonCast;

	// Target
	private OrderedScroller target;
	private JButton boutonStats, boutonAttack;
	private JCheckBox chkVore, chkFriendly;
	
	public void addTarget( StatChar newTarget )
	{
		target.addElement( newTarget );
	}
	
	public void removeTarget( StatChar oldTarget )
	{
		target.removeElement( oldTarget );
	}
	
	public int getTargetCount()
	{
		return target.getElementCount();
	}
	
	public StatChar getTarget( int i )
	{
		return (StatChar)target.elementAt( i );
	}
	
	public StatChar getSelectedTarget()
	{
		return (StatChar)target.getSelectedValue();
	}
	
	// Messages
	private JTextArea messageJ;
	private JPanel warningPanel;
	private IconBar weaponDamage;
	private IconBar armorDamage;
	
	// Movement
	private JPanel movePanel;
	private JButton boutonUp;
	private JButton boutonDown;
	private JButton boutonLeft;
	private JButton boutonRight;
	private JButton boutonUpLeft;
	private JButton boutonUpRight;
	private JButton boutonDownRight;
	private JButton boutonDownLeft;
	private JButton boutonStand;
	
	// Status
	private CharacterPanel charPanel;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Constants
	public static final int PLAYER=0;
	public static final int MONSTER=1;
	public static final int NPC=2;
	public static final int SPELL=3;
	
	public static final int INITIAL_MAP=1;

	public static final int MAX_SPEED=4;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public FenetreTest( String nomFenetre )
	{
		super( nomFenetre );
		instance = this;
		
		// Set visiblilty to false to prevent draws until after the components are built.
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 1024, 768 );
		setVisible( false );

		objetG = new Vector<ObjetGraphique>();
		
		addKeyListener( this );
		addFocusListener( this );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void start()
	{
		// Load client config.
		loadClientConfig();
		
		// Run initialization.
		initializeIcons();
		initializeComponents();
		initializeShortcuts();
		
		// Load the initial map.
		changeBackGround( INITIAL_MAP );
		enterBackGround( true );
		
		if( dialogue != null )
		{
			dialogue.requestFocus();
		}
		
		// Unhide the window.
		setVisible( true );
	}
	
	private void loadClientConfig()
	{
		Properties defaultCfg = new Properties();
		defaultCfg.setProperty( "Speed", "0" );
		defaultCfg.setProperty( "SoundEnabled", "true" );
		defaultCfg.setProperty( "ScatEnabled", "true" );
		defaultCfg.setProperty( "MoveButtonsVisible", "true" );
		
		// Try to load vgame.cfg
		Properties cfg = new Properties( defaultCfg );
		try
		{
			FileInputStream cfgFile = new FileInputStream( "vgame.cfg" );
			cfg.load( cfgFile );
			cfgFile.close();
		}
		catch (Exception e)
		{
			
		}
			
		speed = Integer.parseInt( cfg.getProperty( "Speed" ) );
		soundEnabled = Boolean.parseBoolean( cfg.getProperty( "SoundEnabled" ) );
		scatEnabled = Boolean.parseBoolean( cfg.getProperty( "ScatEnabled" ) );
		moveButtonsVisible = Boolean.parseBoolean( cfg.getProperty( "MoveButtonsVisible" ) );
		
		saveClientConfig();
	}
	
	private void saveClientConfig()
	{
		// Update the settings file based on our current selections.
		Properties cfg = new Properties();
		cfg.setProperty( "Speed", Integer.toString( speed ) );
		cfg.setProperty( "SoundEnabled", Boolean.toString( soundEnabled ) );
		cfg.setProperty( "ScatEnabled", Boolean.toString( scatEnabled ) );
		cfg.setProperty( "MoveButtonsVisible", Boolean.toString( moveButtonsVisible ) );
		
		try
		{
			FileOutputStream cfgFile = new FileOutputStream( "vgame.cfg" );
			cfg.store( cfgFile, "VGame Load Options" );
			cfgFile.close();
		}
		catch (Exception e)
		{
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void changeBackGround( int mapID )
	{
		map = new Map( mapID );
		gamePanel.setMap( map );
		
		// Finally, execute the entry script associated with the map.
		repaint();
	}
	
	public void enterBackGround( boolean script )
	{
		FenetreTest.instance.executeScript( script ? map.m_script_event : map.m_load_event );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getBaseX(){
		return gamePanel.getMapX();
	}
	public int getBaseY(){
		return gamePanel.getMapY();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void printMessage(String msg){
	  if( msg != "" )
	  {
		messageJ.append( msg + "\n" );
	  }
	}
	
	public void clearmessages(){
		messageJ.setText("");
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setItemsEnabled( boolean en )
	{
		if( en != itemsEnabled )
		{
			itemsEnabled = en;
			inventaire.setBlank( !en );
			repaint();
		}
	}
	
	public void setSpellsEnabled( boolean en )
	{
		if( en != spellsEnabled )
		{
			spellsEnabled = en;
			spellbook.setBlank( !en );
			repaint();
		}
	}
	
	public void setSaveEnabled( boolean en )
	{
		saveEnabled = en;
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setSoundEnabled( boolean e )
	{
		if( e != soundEnabled )
		{
			soundEnabled = e;
			saveClientConfig();
			repaint();
		}
	}
	
	public boolean isSoundEnabled()
	{
		return soundEnabled;
	}
	
	public void setScatEnabled( boolean e )
	{
		if( e != scatEnabled )
		{
			scatEnabled = e;
			saveClientConfig();
			repaint();
		}
	}
	
	public boolean isScatEnabled()
	{
		return scatEnabled;
	}
	
	public void setMoveButtonsVisible( boolean v )
	{
		if( v != moveButtonsVisible )
		{
			moveButtonsVisible = v;
			movePanel.setVisible( v );
			saveClientConfig();
			repaint();
		}
	}
	
	public boolean getMoveButtonsVisible()
	{
		return moveButtonsVisible;
	}
	
	public void setFailChecks( boolean b )
	{
		failChecks = b;
	}
	
	public boolean getFailChecks()
	{
		return failChecks;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void repaintgame()
	{
	  gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
	}
	
	public void updateUIState()
	{
		ObjetGraphique temp = getPlayer();
		PlayerStat plr = null;
		NumberFormat nf = NumberFormat.getNumberInstance();

		if( temp != null ) plr = (PlayerStat)temp.statistique;

		boolean dialogClosed = dialogue == null;
		boolean playerAlive = dialogClosed && plr != null;
		boolean playerMobile = playerAlive && plr.status == 0;
		boolean playerFree = playerMobile && plr.maitre == null && plr.polymorph == 0;

		boolean itemSelected = !inventaire.isSelectionEmpty();
		int itemIdx = inventaire.getSelectedIndex();
		boolean itemFirst = itemIdx == 0;
		boolean itemLast = itemIdx == inventaire.getElementCount() - 1 && itemIdx >= 0;
		ObjInventaire selectedItem = null;
		if( itemSelected ) selectedItem = (ObjInventaire)inventaire.getSelectedValue();
		boolean itemHostile = itemSelected && selectedItem.harmful == 0;

		boolean spellSelected = !spellbook.isSelectionEmpty();
		int spellIdx = spellbook.getSelectedIndex();
		boolean spellFirst = spellIdx == 0;
		boolean spellLast = spellIdx == spellbook.getElementCount() - 1 && spellIdx >= 0;
		Spell selectedSpell = null;
		if( spellSelected ) selectedSpell = (Spell)spellbook.getSelectedValue();
		boolean spellHostile = spellSelected && selectedSpell.harmful == 0;

		boolean targetSelected = !target.isSelectionEmpty();
		StatChar selectedTarget = null;
		if( targetSelected ) selectedTarget = (StatChar)target.getSelectedValue();
		boolean targetHostile = playerAlive && targetSelected
			&& determinationEnnemi( getPlayer(), selectedTarget.mere );
		boolean targetEaten = targetSelected && selectedTarget.maitre != null;
		boolean canAttackTarget = (targetHostile && !targetEaten) || !chkFriendly.isSelected();

		// Update the state of the UI controls.
		soundMenuItem.setSelected( soundEnabled );
		scatMenuItem.setSelected( scatEnabled );
		failChecksMenuItem.setSelected( failChecks );
		moveButtonsMenuItem.setSelected( moveButtonsVisible );
		testPathsMenuItem.setSelected( gamePanel.getShowPaths() );
		
		qsaveItem.setEnabled( current_save_file != null );
		qloadItem.setEnabled( current_save_file != null );

		if( current_save_file != null )
		{
			qsaveItem.setText( "Quick Save (" + current_save_file + ")" );
			qloadItem.setText( "Quick Load (" + current_save_file + ")" );
		}
		else
		{
			qsaveItem.setText( "Quick Save" );
			qloadItem.setText( "Quick Load" );
		}

		// Inventory
		inventaire.setEnabled( dialogClosed );
		boutonView.setEnabled( dialogClosed && itemSelected ); // Any item selected
		boutonUse.setEnabled( playerFree
			&& itemSelected
			&& (selectedItem.equipped || selectedItem.canUse())
			&& (canAttackTarget || !itemHostile) );
	  
		if( itemSelected && selectedItem.price > 0 )
		{
			boutonSell.setEnabled( playerFree );
			boutonSell.setText( "Sell (" + nf.format(selectedItem.getSellValue()) + ")" );
		}
		else
		{
			boutonSell.setEnabled( false );
			boutonSell.setText( "Sell" );
		}

		// Change text on boutonUse based on whether or not the item is equipment or something else.
		boutonUse.setText( itemSelected ? selectedItem.getUseString() : "Use" );
	  
		// Spellbook
		spellbook.setEnabled( dialogClosed );
		boutonInfo.setEnabled( dialogClosed && spellSelected ); // Any spell selected
		boutonCast.setEnabled( playerFree
			&& spellSelected && targetSelected
			&& (canAttackTarget || !spellHostile)
			&& selectedSpell.mpCost <= plr.pm
			&& selectedSpell.permanentCost <= plr.pmMax ); // Castable spell and target selected

		// Target
		target.setEnabled( dialogClosed ); // Leave the target box enabled at all times so you can look at your killer
		boutonStats.setEnabled( dialogClosed && targetSelected ); // If there is a target selected
		boutonAttack.setEnabled( playerFree && targetSelected && selectedTarget != plr ); // If there is a non-player target selected
		chkVore.setEnabled( playerAlive && voreEnabled != 0 ); // If there is a vore-capable player
		chkFriendly.setEnabled( playerAlive ); // If there is a player
		
		gamePanel.setSelectedSprite( selectedTarget != null ? selectedTarget.mere : null );

		if( chkVore.isSelected() && voreEnabled == 0 )
		{
			chkVore.setSelected( false );
		}
		
		if( chkFriendly.isSelected() && !targetHostile )
		{
			boutonAttack.setText( "Talk" );
		}
		else if( chkVore.isSelected() )
		{
			boutonAttack.setText( "Eat" );
		}
		else
		{
			boutonAttack.setText( "Attack" );
		}
		

		// Movement
		boutonUp.setEnabled( playerMobile );
		boutonDown.setEnabled( playerMobile );
		boutonLeft.setEnabled( playerMobile );
		boutonRight.setEnabled( playerMobile );
		boutonUpLeft.setEnabled( playerMobile );
		boutonUpRight.setEnabled( playerMobile );
		boutonDownRight.setEnabled( playerMobile );
		boutonDownLeft.setEnabled( playerMobile );
		boutonStand.setEnabled( dialogClosed ); // Leave enabled at all times to push time forward

		// Character
		charPanel.setPlayer( plr );
		
		// Equipment damage warnings
		warningPanel.setVisible( false );
		weaponDamage.setVisible( false );
		armorDamage.setVisible( false );

		if( plr != null )
		{
			Arme wep = plr.arme;
			if( wep != null
				&& wep.qualite >= 0
				&& wep.qualiteMax > 0
				&& (wep.qualite * 4) < (wep.qualiteMax * 3) )
			{
				weaponDamage.setMaximum( wep.qualiteMax );
				weaponDamage.setValue( wep.qualite );
				weaponDamage.setToolTipText( wep.qualite + " / " + wep.qualiteMax + " Durability" );
				weaponDamage.setVisible( true );
				warningPanel.setVisible( true );
			}
			
			Armure armor = plr.armure;
			if( armor != null
				&& armor.qualite >= 0
				&& armor.qualiteMax > 0
				&& (armor.qualite * 4) < (armor.qualiteMax * 3) )
			{
				armorDamage.setMaximum( armor.qualiteMax );
				armorDamage.setValue( armor.qualite );
				armorDamage.setToolTipText( armor.qualite + " / " + armor.qualiteMax + " Durability" );
				armorDamage.setVisible( true );
				warningPanel.setVisible( true );
			}
		}
	}
	
	@Override // Component
	public void paint( Graphics g )
	{
		try
		{
			updateUIState();
			
			super.paint( g );
		}
		catch( Exception e )
		{
			Util.error( e );
		}
	}

	private int playerIdx = -1;	
	public ObjetGraphique getPlayer()
	{
		int i = getNoPlayer();
		
		if( i < objetG.size() )
		{
			return objetG.elementAt( i );
		}
		
		return null;
	}

	public int getNoPlayer()
	{
		if( playerIdx >= 0 && playerIdx < objetG.size() && objetG.elementAt( playerIdx ).type == PLAYER )
		{
			return playerIdx;
		}

		playerIdx = -1;
		for( int i = 0; i < objetG.size(); ++i )
		{
			if( objetG.elementAt(i).type == PLAYER )
			{
				playerIdx = i;
				return i;
			}
		}

		return objetG.size();
	}
	
	protected void initializeComponents()
	{
		Dimension spacer = new Dimension( 5, 5 );
		Insets margin = new Insets(2, 8, 2, 8);
		Insets nomargin = new Insets(0, 0, 0, 0);
		
		int right_w = 300;
		int bottom_h = 150;
		
		ActionListener action = new ActionListener()
		{
			public void actionPerformed(ActionEvent action)
			{
				doCommand( action.getActionCommand() );
			}
		};

		// Menu Panel
		JMenuBar menuBar = new JMenuBar();
		{
			JMenu fileMenu = new JMenu( "File" )
			{
				@Override
				public void paintComponent( Graphics g )
				{
					setEnabled( dialogue == null );
					super.paintComponent( g );
				}
			};
			fileMenu.setMnemonic( KeyEvent.VK_F );
			{
				JMenuItem newItem = new JMenuItem( "New Game", KeyEvent.VK_N )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};

				JMenuItem saveItem = new JMenuItem( "Save Game", KeyEvent.VK_S )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( saveEnabled && dialogue == null && getPlayer() != null );
						super.paintComponent( g );
					}
				};
				saveItem.setActionCommand( "MENU_SAVE" );
				saveItem.addActionListener( action );
				
				JMenuItem loadItem = new JMenuItem( "Load Game", KeyEvent.VK_L )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				loadItem.setActionCommand( "MENU_LOAD" );
				loadItem.addActionListener( action );
				
				qsaveItem = new JMenuItem( "Quicksave", KeyEvent.VK_A )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( saveEnabled && dialogue == null && getPlayer() != null && current_save_file != null );
						super.paintComponent( g );
					}
				};
				qsaveItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
				qsaveItem.setActionCommand( "MENU_QUICK_SAVE" );
				qsaveItem.addActionListener( action );
				
				qloadItem = new JMenuItem( "Quickload", KeyEvent.VK_O )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null && current_save_file != null );
						super.paintComponent( g );
					}
				};
				qloadItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, ActionEvent.CTRL_MASK ) );
				qloadItem.setActionCommand( "MENU_QUICK_LOAD" );
				qloadItem.addActionListener( action );
				
				JMenuItem quitItem = new JMenuItem( "Exit", KeyEvent.VK_X )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				quitItem.setActionCommand( "MENU_QUIT" );
				quitItem.addActionListener( action );
				
				//fileMenu.add( newItem );
				//fileMenu.addSeparator();
				fileMenu.add( loadItem );
				fileMenu.add( saveItem );
				fileMenu.addSeparator();
				fileMenu.add( qloadItem );
				fileMenu.add( qsaveItem );
				fileMenu.addSeparator();
				fileMenu.add( quitItem );
			}
			
			JMenu optionMenu = new JMenu( "Options" )
			{
				@Override
				public void paintComponent( Graphics g )
				{
					setEnabled( dialogue == null );
					super.paintComponent( g );
				}
			};
			optionMenu.setMnemonic( KeyEvent.VK_O );
			{
				// Toggle item
				soundMenuItem = new JCheckBoxMenuItem( "Sound" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				soundMenuItem.setMnemonic( KeyEvent.VK_S );
				soundMenuItem.setSelected( soundEnabled );
				soundMenuItem.setActionCommand( "MENU_SOUND_TOGGLE" );
				soundMenuItem.addActionListener( action );
				optionMenu.add( soundMenuItem );
				
				scatMenuItem = new JCheckBoxMenuItem( "Scat" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				scatMenuItem.setMnemonic( KeyEvent.VK_C );
				scatMenuItem.setSelected( scatEnabled );
				scatMenuItem.setActionCommand( "MENU_SCAT_TOGGLE" );
				scatMenuItem.addActionListener( action );
				optionMenu.add( scatMenuItem );
				
				failChecksMenuItem = new JCheckBoxMenuItem( "Fail Checks" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				failChecksMenuItem.setMnemonic( KeyEvent.VK_F );
				failChecksMenuItem.setSelected( failChecks );
				failChecksMenuItem.setActionCommand( "MENU_FAIL_CHECKS_TOGGLE" );
				failChecksMenuItem.addActionListener( action );
				optionMenu.add( failChecksMenuItem );
				
				moveButtonsMenuItem = new JCheckBoxMenuItem( "Movement Buttons" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				moveButtonsMenuItem.setMnemonic( KeyEvent.VK_M );
				moveButtonsMenuItem.setSelected( moveButtonsVisible );
				moveButtonsMenuItem.setActionCommand( "MENU_MOVE_BTNS_TOGGLE" );
				moveButtonsMenuItem.addActionListener( action );
				optionMenu.add( moveButtonsMenuItem );
				
				optionMenu.addSeparator();
				
				JMenuItem viewCloseMenuItem = new JMenuItem( "Close all info cards" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				viewCloseMenuItem.setMnemonic( KeyEvent.VK_M );
				viewCloseMenuItem.setActionCommand( "MENU_CLOSE_VIEWS" );
				viewCloseMenuItem.addActionListener( action );
				optionMenu.add( viewCloseMenuItem );
				
				optionMenu.addSeparator();
				
				ButtonGroup speedGroup = new ButtonGroup();
				for( int i = 0; i < MAX_SPEED; ++i )
				{
					JRadioButtonMenuItem speedItem = new JRadioButtonMenuItem( "Speed " + (i+1) )
					{
						@Override
						public void paintComponent( Graphics g )
						{
							setEnabled( dialogue == null );
							super.paintComponent( g );
						}
					};
					
					if( i == speed )
					{
						speedItem.setSelected( true );
					}
					if( i < 10 )
					{
						speedItem.setMnemonic( KeyEvent.VK_1 + i );
					}

					speedItem.setActionCommand("MENU_SPEED_SET_" + i);
					speedItem.addActionListener( action );
					
					speedGroup.add( speedItem );
					optionMenu.add( speedItem );
				}
			}
			
			JMenu extrasMenu = new JMenu( "Extras" )
			{
				@Override
				public void paintComponent( Graphics g )
				{
					setEnabled( dialogue == null );
					super.paintComponent( g );
				}
			};
			extrasMenu.setMnemonic( KeyEvent.VK_X );
			{
				JMenuItem cheatItem = new JMenuItem( "Cheats" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				cheatItem.setMnemonic( KeyEvent.VK_C );
				cheatItem.setActionCommand( "MENU_CHEATS" );
				cheatItem.addActionListener( action );
				extrasMenu.add( cheatItem );
				
				JMenuItem bonusesItem = new JMenuItem( "Bonuses" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				bonusesItem.setMnemonic( KeyEvent.VK_B );
				bonusesItem.setActionCommand( "MENU_BONUSES" );
				bonusesItem.addActionListener( action );
				extrasMenu.add( bonusesItem );
				
				extrasMenu.addSeparator();
				
				testPathsMenuItem = new JCheckBoxMenuItem( "Pathing Markers" )
				{
					@Override
					public void paintComponent( Graphics g )
					{
						setEnabled( dialogue == null );
						super.paintComponent( g );
					}
				};
				testPathsMenuItem.setMnemonic( KeyEvent.VK_S );
				testPathsMenuItem.setSelected( false );
				testPathsMenuItem.setActionCommand( "MENU_TEST_PATHS_TOGGLE" );
				testPathsMenuItem.addActionListener( action );
				extrasMenu.add( testPathsMenuItem );
			}
			
			menuBar.add( fileMenu );
			menuBar.add( optionMenu );
			menuBar.add( extrasMenu );
		}

        setJMenuBar( menuBar );

		// Game panel
		gamePanel = new GamePanel();
		gamePanel.addMouseListener( this );
		  
		// Inventory panel
		JPanel invPanel = new JPanel();
		invPanel.setLayout( new BoxLayout( invPanel, BoxLayout.Y_AXIS ) );
		invPanel.setPreferredSize( new Dimension( right_w, 0 ) );
		invPanel.setMinimumSize( new Dimension( right_w, 0 ) );
		invPanel.setBorder( BorderFactory.createTitledBorder("Inventory") );
		{
			inventaire = new OrderedScroller();
			inventaire.setCellRenderer( new ItemCellRenderer() );
			inventaire.setSortedInsert( true );
			inventaire.addListSelectionListener(this);
		  
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
			{ 
				boutonView=new JButton(Util.getIcon("button_info"));
				boutonView.setMargin(margin);
				boutonView.setActionCommand("INV_INFO");
				boutonView.addActionListener(action);
				boutonView.setFocusable( false );

				boutonUse=new JButton("Use");
				boutonUse.setMargin(margin);
				boutonUse.setActionCommand("INV_USE");
				boutonUse.addActionListener(action);
				boutonUse.setFocusable( false );

				boutonSell=new JButton("Sell");
				boutonSell.setMargin(margin);
				boutonSell.setActionCommand("INV_SELL");
				boutonSell.addActionListener(action);
				boutonSell.setFocusable( false );

				buttonPanel.add( boutonView );
				buttonPanel.add( Box.createHorizontalGlue() );
				buttonPanel.add( boutonUse );
				buttonPanel.add( boutonSell );
			}
			
			inventaire.setDoubleclickButton( boutonUse );
		  
			invPanel.add( inventaire );
			invPanel.add( Box.createRigidArea(spacer) );
			invPanel.add( buttonPanel );
		}
			
		// Spellbook panel
		JPanel spellPanel = new JPanel();
		spellPanel.setLayout( new BoxLayout( spellPanel, BoxLayout.Y_AXIS ) );
		spellPanel.setPreferredSize( new Dimension( right_w, 0 ) );
		spellPanel.setMinimumSize( new Dimension( right_w, 0 ) );
		spellPanel.setBorder( BorderFactory.createTitledBorder("Spellbook") );
		{
			spellbook = new OrderedScroller();
			spellbook.setCellRenderer( new SpellCellRenderer() );
			spellbook.setSortedInsert( true );
			spellbook.addListSelectionListener(this);

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
			{ 
				boutonInfo=new JButton(Util.getIcon("button_info"));
				boutonInfo.setMargin(margin);
				boutonInfo.setActionCommand("SPELL_INFO");
				boutonInfo.addActionListener(action);
				boutonInfo.setFocusable( false );

				boutonCast=new JButton("Cast");
				boutonCast.setMargin(margin);
				boutonCast.setActionCommand("SPELL_CAST");
				boutonCast.addActionListener(action);
				boutonCast.setFocusable( false );

				buttonPanel.add(boutonInfo);
				buttonPanel.add(Box.createHorizontalGlue() );
				buttonPanel.add(boutonCast);
			}
			
			spellbook.setDoubleclickButton( boutonCast );

			spellPanel.add(spellbook);
			spellPanel.add(Box.createRigidArea(spacer));
			spellPanel.add(buttonPanel);
		}
		
		// Target Panel
		JPanel targetPanel = new JPanel();
		targetPanel.setPreferredSize( new Dimension( 0, bottom_h ) );
		targetPanel.setMinimumSize( new Dimension( 0, bottom_h ) );
		targetPanel.setLayout( new BoxLayout( targetPanel, BoxLayout.Y_AXIS ) );
		targetPanel.setBorder( BorderFactory.createTitledBorder("Target") );
		{
			target = new OrderedScroller();
			target.setCellRenderer( new TargetCellRenderer() );
			target.setButtonsVisible( false );
			target.setSortedInsert( true );
			target.addListSelectionListener(this);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
			{
				boutonStats = new JButton(Util.getIcon("button_info"));
				boutonStats.setMargin(margin);
				boutonStats.setActionCommand("TARGET_INFO");
				boutonStats.addActionListener(action);
				boutonStats.setFocusable( false );

				boutonAttack = new JButton("Attack");
				boutonAttack.setMargin(margin);
				boutonAttack.setActionCommand("TARGET_INTERACT");
				boutonAttack.addActionListener(action);
				boutonAttack.setFocusable( false );

				chkVore = new JCheckBox("Vore");
				chkVore.addActionListener(action);
				chkVore.setFocusable( false );

				chkFriendly = new JCheckBox("Friendly");
				chkFriendly.addActionListener(action);
				chkFriendly.setSelected( true );
				chkFriendly.setFocusable( false );

				buttonPanel.add(boutonStats);
				buttonPanel.add(Box.createHorizontalGlue() );
				buttonPanel.add(boutonAttack);
				buttonPanel.add(Box.createRigidArea(spacer));
				buttonPanel.add(chkVore);
				buttonPanel.add(chkFriendly);
			}

			target.setDoubleclickButton( boutonStats );

			targetPanel.add(target);
			targetPanel.add(Box.createRigidArea(spacer));
			targetPanel.add(buttonPanel);
		}
			
		// Move panel
		movePanel = new JPanel();
		movePanel.setLayout( new GridLayout( 3, 3, 5, 5 ) );
		movePanel.setVisible( moveButtonsVisible );
		movePanel.setPreferredSize( new Dimension( 130, bottom_h ) );
		movePanel.setMinimumSize( new Dimension( 130, bottom_h ) );
		movePanel.setBorder( BorderFactory.createTitledBorder("Move") );
		{
			
			boutonUpLeft = new JButton(Util.getIcon("arrow_nw"));
			boutonUpLeft.setMargin(nomargin);
			boutonUpLeft.setActionCommand("WALK_NW");
			boutonUpLeft.addActionListener(action);
			boutonUpLeft.setFocusable( false );

			boutonUp = new JButton(Util.getIcon("arrow_n"));
			boutonUp.setMargin(nomargin);
			boutonUp.setActionCommand("WALK_N");
			boutonUp.addActionListener(action);
			boutonUp.setFocusable( false );

			boutonUpRight = new JButton(Util.getIcon("arrow_ne"));
			boutonUpRight.setMargin(nomargin);
			boutonUpRight.setActionCommand("WALK_NE");
			boutonUpRight.addActionListener(action);
			boutonUpRight.setFocusable( false );

			boutonLeft = new JButton(Util.getIcon("arrow_w"));
			boutonLeft.setMargin(nomargin);
			boutonLeft.setActionCommand("WALK_W");
			boutonLeft.addActionListener(action);
			boutonLeft.setFocusable( false );

			boutonStand = new JButton(Util.getIcon("arrow_none"));
			boutonStand.setMargin(nomargin);
			boutonStand.setActionCommand("WALK_STAND");
			boutonStand.addActionListener(action);
			boutonStand.setFocusable( false );

			boutonRight = new JButton(Util.getIcon("arrow_e"));
			boutonRight.setMargin(nomargin);
			boutonRight.setActionCommand("WALK_E");
			boutonRight.addActionListener(action);
			boutonRight.setFocusable( false );

			boutonDownLeft = new JButton(Util.getIcon("arrow_sw"));
			boutonDownLeft.setMargin(nomargin);
			boutonDownLeft.setActionCommand("WALK_SW");
			boutonDownLeft.addActionListener(action);
			boutonDownLeft.setFocusable( false );

			boutonDown = new JButton(Util.getIcon("arrow_s"));
			boutonDown.setMargin(nomargin);
			boutonDown.setActionCommand("WALK_S");
			boutonDown.addActionListener(action);
			boutonDown.setFocusable( false );

			boutonDownRight = new JButton(Util.getIcon("arrow_se"));
			boutonDownRight.setMargin(nomargin);
			boutonDownRight.setActionCommand("WALK_SE");
			boutonDownRight.addActionListener(action);
			boutonDownRight.setFocusable( false );

			//boutonStand.addKeyListener(this);
			//boutonStand.grabFocus();  

			movePanel.add( boutonUpLeft );
			movePanel.add( boutonUp );
			movePanel.add( boutonUpRight );
			movePanel.add( boutonLeft );
			movePanel.add( boutonStand );
			movePanel.add( boutonRight );
			movePanel.add( boutonDownLeft );
			movePanel.add( boutonDown );
			movePanel.add( boutonDownRight );
		}
		
		// Message panel
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout( new BoxLayout( messagePanel, BoxLayout.Y_AXIS ) );
		messagePanel.setPreferredSize( new Dimension( 0, bottom_h ) );
		messagePanel.setMinimumSize( new Dimension( 0, bottom_h ) );
		messagePanel.setBorder( BorderFactory.createTitledBorder("Messages") );
		{
			warningPanel = new JPanel();
			warningPanel.setLayout( new BoxLayout( warningPanel, BoxLayout.Y_AXIS ) );
			warningPanel.setVisible( false );
			{
				weaponDamage = new IconBar( Util.getIcon( "item_weapon" ) );
				weaponDamage.setForegroundList( Util.COLOR_DAMAGE_FORE );
				weaponDamage.setBackgroundList( Util.COLOR_DAMAGE_BACK );
				weaponDamage.setText( "Damaged!" );
				weaponDamage.setVisible( false );
				
				armorDamage = new IconBar( Util.getIcon( "item_armor" ) );
				armorDamage.setForegroundList( Util.COLOR_DAMAGE_FORE );
				armorDamage.setBackgroundList( Util.COLOR_DAMAGE_BACK );
				armorDamage.setText( "Damaged!" );
				armorDamage.setVisible( false );
				
				warningPanel.add( weaponDamage );
				warningPanel.add( armorDamage );
				warningPanel.add( Box.createRigidArea( spacer ) );
			}
			
			messageJ = new JTextArea();
			messageJ.setMargin( new Insets(5,5,5,5) );
			messageJ.setEditable(false);
			messageJ.setBackground(this.getBackground());
			messageJ.setFocusable( false );

			JScrollPane scroller = new JScrollPane(messageJ, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			messagePanel.add(warningPanel);
			messagePanel.add(scroller);
		}
		
		// Status panel
		charPanel = new CharacterPanel();

		// And finally the actual page layout.
		Container conteneur = getContentPane();
		conteneur.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		// Game panel - 3x4
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 3; c.gridheight = 2;
		conteneur.add( gamePanel, c );
		
		// Inventory - 1 cell
		c.weightx = 0.0; c.weighty = 0.6;
		c.gridx = 3; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( invPanel, c );
		
		// Spellbook - 1 cell
		c.weightx = 0.0; c.weighty = 0.4;
		c.gridx = 3; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( spellPanel, c );
		
		// Bottom row, one cell each
		c.weightx = 0.55; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 2;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( targetPanel, c );
		
		c.gridx = 1; c.gridy = 2;
		c.weightx = 0.45; c.weighty = 0.0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( messagePanel, c );
		
		c.gridx = 2; c.gridy = 2;
		c.weightx = 0.0; c.weighty = 0.0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( movePanel, c );
		
		c.gridx = 3; c.gridy = 2;
		c.weightx = 0.0; c.weighty = 0.0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( charPanel, c );
	}

	public void ajouterObjetGraphique(int carID, int postX, int postY, int side, int eventID, int deplacement, int t, int deathEventID, int statID, int attackID)
	{
		ObjetGraphique ob = new ObjetGraphique(
			postX, postY,
			side,eventID,deplacement,carID,statID,t,
			deathEventID,carID,attackID );

		objetG.addElement( ob );
	}
	
	public void ajouterObjetGraphiqueVored(int carID, int postX, int postY, int side, int eventID, int deplacement, int t, int deathEventID, int statID, int attackID, StatChar maitre, int voreType)
	{
		ajouterObjetGraphique( carID, postX, postY, side, eventID, deplacement, t, deathEventID, statID, attackID );
		
		ObjetGraphique ob = objetG.lastElement();

		if( voreType == VHold.ORAL )
		{
			maitre.ajouterVictime( ob.statistique );
		}
		else if( voreType == VHold.UNBIRTH )
		{
			maitre.unbirthVictime( ob.statistique );
		}
		else
		{
			maitre.analVoringVictime( ob.statistique );
		}

		ob.statistique.maitre=maitre;
		ob.setVisible(false);
	}
	
	public void removePanneau(){
		for(int i=0;i<objetG.size();i++){
		   objetG.elementAt(i).setVisible(false);
		}
		
	}
	public void ajouterPanneau(){
		for(int i=0;i<objetG.size();i++){
			if( objetG.elementAt(i).isVisible())
				objetG.elementAt(i).setVisible(true);
		}
	}
	
	public void animationStand(int perso, int mode, boolean pla){
		ObjetGraphique temp=null;
		boolean direction=true;
			if(pla){
				for(int i=0;i<objetG.size();i++){
					if(objetG.elementAt(i).type==PLAYER){
						temp=objetG.elementAt(i);
						break;
					}
						
				}
			}
			else
			   temp=objetG.elementAt(perso);
			int modeGraph=1;
			int delai=1;
			if(mode<0){
				mode=-mode;
				direction=false;
			}
			
			modeGraph = temp.images[mode-1].size();
			delai = temp.images[mode-1].delai;
			temp.modeAffichage = mode;
			
			if(direction){
				temp.frame=0;
				for(int i=0;i<modeGraph;i++){
					if(temp.frame==modeGraph)
						temp.frame=0;
					repaintgame();
					
					temp.frame++;    
					pauseFor( delai );
				}
			}
			else{
				temp.frame=modeGraph-1;
				for(int i=modeGraph;i>0;i--){
					repaintgame();
					temp.frame--;
					pauseFor( delai );
				}
			}
			temp.frame=0;
			if(temp.modeAffichage==5)
				temp.modeAffichage=3;
			if(temp.modeAffichage==7 &&temp.statistique.victimes.size()>0)
			   temp.modeAffichage=3;
			if(temp.modeAffichage==7 &&temp.statistique.victimes.size()<1)
			   temp.modeAffichage=1;
			if(temp.modeAffichage==9 &&temp.statistique.victimes.size()>0)
			   temp.modeAffichage=3;
			if(temp.modeAffichage==9 &&temp.statistique.victimes.size()<1)
			   temp.modeAffichage=1;
			if(temp.modeAffichage==8 &&temp.statistique.victimes.size()<1)
			   temp.modeAffichage=1;
			if(temp.modeAffichage==8 &&temp.statistique.victimes.size()>0)
			   temp.modeAffichage=3;
			if(temp.modeAffichage==10 &&temp.statistique.victimes.size()<1)
			   temp.modeAffichage=1;
			if(temp.modeAffichage==10 &&temp.statistique.victimes.size()>0)
			   temp.modeAffichage=3;
	}
	
	public void animationScript(ObjetGraphique temp,int mode,int modeFin, boolean normal, boolean pla){
			boolean direction=true;
			if(pla){
				for(int i=0;i<objetG.size();i++){
					if(objetG.elementAt(i).type==PLAYER){
						temp=objetG.elementAt(i);
						break;
					}
						
				}
			}
			int modeGraph=1;
			int delai=1;
			if(mode<0){
				mode=-mode;
				direction=false;
			}

			modeGraph = temp.images[mode-1].size();
			delai = temp.images[mode-1].delai;
			temp.modeAffichage = mode;

			if(direction){
				temp.frame=0;
				for(int i=0;i<modeGraph;i++){
					if(temp.frame==modeGraph)
						temp.frame=0;
					repaintgame();
					temp.frame++;
					pauseFor( delai );
				}
			}
			else{
				temp.frame=modeGraph;
				for(int i=modeGraph;i>0;i--){
					repaintgame();
					temp.frame--;
					pauseFor( delai );
				}
			}
			temp.frame=0;
			if(normal){
			   if(temp.modeAffichage==2)
				   temp.modeAffichage=1;
			   if(temp.modeAffichage==4)
				   temp.modeAffichage=3;
			   if(temp.modeAffichage==5)
				   temp.modeAffichage=3;
			   if(temp.modeAffichage==6)
				   temp.modeAffichage=3;
			   if(temp.modeAffichage==8)
				   temp.modeAffichage=3;
			   if(temp.modeAffichage==7 &&temp.statistique.victimes.size()>0)
				   temp.modeAffichage=3;
			   if(temp.modeAffichage==7 &&temp.statistique.victimes.size()<1)
				   temp.modeAffichage=1;
			   if(temp.modeAffichage==9 &&temp.statistique.victimes.size()>0)
					temp.modeAffichage=3;
			   if(temp.modeAffichage==9 &&temp.statistique.victimes.size()<1)
					temp.modeAffichage=1;
			   if(temp.modeAffichage==10 &&temp.statistique.victimes.size()>0)
					temp.modeAffichage=3;
			   if(temp.modeAffichage==10 &&temp.statistique.victimes.size()<1)
					temp.modeAffichage=1;
			else
				temp.modeAffichage=modeFin;
		  }
	}
	
	public void animationProjectileMove(){
		
		int difX=projectile.cibleX-projectile.getX();
		int difY=projectile.cibleY-projectile.getY();
		difX*=Map.TILE_WIDTH;
		difY*=Map.TILE_HEIGHT;
		difX/=projectile.deplacement;
		difY/=projectile.deplacement;
		for(int i=0;i<projectile.deplacement;i++){
			repaintgame();
			pauseFor( projectile.delai1 );
			
			projectile.varX+=difX;
			projectile.varY+=difY;
			projectile.frame++;
			if(projectile.frame>=projectile.nombreImage1)
				projectile.frame=0;
		}
		
		
	}
	public void animationProjectileStand(){
		for(int i=0;i<projectile.nombreImage2;i++){
			repaintgame();
			pauseFor( projectile.delai2 );
			projectile.frame++;
			if(projectile.frame>=projectile.nombreImage2)
				projectile.frame=0;
		}
	}
	
	public void pauseFor( int msec )
	{
		int fac = 1;
		if( speed > 0 )
		{
			// 0==1, 1==4, 2==7, 3==10
			fac = 1 + (speed * 3);
		}
		
		msec = Math.max( msec / fac, 1 );

		try
		{
			Thread.sleep( msec );
		}
		catch( Exception e )
		{
			Util.error( e );
		}

		repaint();
	}

	public void deplacementPlayer(int dX,int dY, int perso, int mode, boolean pla){
		if(dX!=0 || dY!=0){
			ObjetGraphique temp=null;
			if(pla){
				for(int i=0;i<objetG.size();i++){
					if(objetG.elementAt(i).type==PLAYER){
						temp=objetG.elementAt(i);
						break;
					}
						
				}
			}
			else
			   temp=objetG.elementAt(perso);
			temp.fx=temp.getX()+dX;
			temp.fy=temp.getY()+dY;
			int modeGraph=1;
			int delai=1;
			
			modeGraph = temp.images[mode-1].size();
			delai = temp.images[mode-1].delai;
			
			temp.frame=0;
			temp.varX=0;
			temp.varY=0;
			int l;
			int sautX=(getPixelX(temp.fx,temp.tailleX)-getPixelX(temp.getX(),temp.tailleX))/modeGraph;
			int sautY=(getPixelY(temp.fy,temp.tailleY)-getPixelY(temp.getY(),temp.tailleY))/modeGraph;
			for(int i=0;i<modeGraph;i++){
				temp.varX+=sautX;
				temp.varY+=sautY;

				temp.frame++;
				if(temp.frame==modeGraph)
					temp.frame=0;
				repaintgame();
				
				pauseFor( delai );

			}
			temp.varX=0;
			temp.varY=0;
			temp.frame=0;
			temp.setPosition( temp.getX() + dX, temp.getY() + dY );
		}
	}
	public void finTour(){
		deplacementAutre();
		enleverMorts();
	}
	public void enleverMorts(){
		ObjetGraphique temp;
		for(int i=0;i<objetG.size();i++){
			temp=objetG.elementAt(i);
			if(temp.enable==0 && !temp.isVisible() && temp.statistique.pv<=-999){
				objetG.elementAt(i).remove();
				objetG.removeElementAt(i);
				
				i--;
				
			}
				
		}
	}
	public void ajouterProjectile(int projectileID,int positionX, int positionY, int cibleX, int cibleY, int modeAffichage, int base1Y, int base2Y){
		projectile=new Projectile(projectileID, positionX, positionY, cibleX, cibleY, base1Y, base2Y);
		projectile.modeAffichage=modeAffichage;
		projectile.panneau=new PanneauProjectile(projectile, this);
		gamePanel.add(projectile.panneau,0);
		
	}
	public void enleverProjectile(){
	   projectile.panneau.getGraphics().dispose();
	   gamePanel.remove(projectile.panneau);
	   projectile=null;
	}
	
	private int getPixelX(int positionX, int tailleX){
		return getBaseX() + (positionX-1)*Map.TILE_WIDTH;
	}
	private int getPixelY(int positionY, int tailleY){
		return getBaseY() + (positionY)*Map.TILE_HEIGHT-tailleY;
	}
	
	// starting north, going clockwise
	static int[] s_move_x = new int[]
	{
		0, 1, 1, 1, 0, -1, -1, -1
	};
	
	static int[] s_move_y = new int[]
	{
		-1, -1, 0, 1, 1, 1, 0, -1
	};
	
	public void deplacementAutre2(int i)
	{
		
		Random hasard=new Random();
		int h1=0;
		int h2=0;
		
		ObjetGraphique mover = objetG.elementAt(i);
		switch (mover.deplacement)
		{
		case 1:
			// Remain stationary
			break;
		case 2:
			// Wander aimlessly
			if( hasard.nextInt(100) < 30 )
			{
				// Potential infinite loop here if a creature is completely surrounded and wants to move.
				int s = hasard.nextInt( s_move_x.length );
				boolean blocked = true;
				for( int j = 0; j < 8 && blocked; ++j )
				{
					h1 = s_move_x[(s+j)%s_move_x.length];
					h2 = s_move_y[(s+j)%s_move_y.length];
					
					blocked = tiltLibre(mover.getX()+h1, mover.getY()+h2,mover);
				}
				mover.fx=mover.getX();
				mover.fy=mover.getY();
				
				deplacementPlayer(h1,h2,i,1,false);
			}
			break;
		case 3:
			// Hunt (wander, attack if there's a target)
			int cible=determinationCible(i);
			if(cible==-1){
				if( hasard.nextInt(100) < 40 )
				{
					int s = hasard.nextInt( s_move_x.length );
					boolean blocked = true;
					for( int j = 0; j < 8 && blocked; ++j )
					{
						h1 = s_move_x[(s+j)%s_move_x.length];
						h2 = s_move_y[(s+j)%s_move_y.length];
						
						blocked = tiltLibre(mover.getX()+h1, mover.getY()+h2,mover);
					}
					mover.fx=mover.getX();
					mover.fy=mover.getY();
				}
			}
			else
			{
				int cibleX=objetG.elementAt(cible).getX();
				int cibleY=objetG.elementAt(cible).getY();
				int difX=cibleX-mover.getX();
				int difY=cibleY-mover.getY();
				boolean blocked = true;
				while(blocked)
				{
					if(difX>5){
						h1=hasard.nextInt(4);
						if(h1>0)
							h1=1;
					}
					else if(difX<-5){
						h1=hasard.nextInt(4);
						if(h1>0)
							h1=-1;
					}
					else if(difX>0){
						h1=hasard.nextInt(4);
						if(h1==2)
							h1=1;
						if(h1==3)
							h1=1;
					}
					else if(difX<0){
						h1=hasard.nextInt(4);
						if(h1==2)
							h1=-1;
						if(h1==3)
							h1=-1;
						if(h1==1)
							h1=-1;
					}
					else{
						h1=hasard.nextInt(3);
						if(h1==2)
							h1=-1;
					}
					if(difY>5){
						h2=hasard.nextInt(4);
						if(h2>0)
							h2=1;
					}
					else if(difY<-5){
						h2=hasard.nextInt(4);
						if(h2>0)
							h2=-1;
					}
					else if(difY>0){
						h2=hasard.nextInt(4);
						if(h2==2)
							h2=1;
						if(h2==3)
							h2=1;
					}
					else if(difY<0){
						h2=hasard.nextInt(4);
						if(h2==1)
							h2=-1;
						if(h2==2)
							h2=-1;
						if(h2==3)
							h2=-1;
					}
					else{
						h2=hasard.nextInt(3);
						if(h2==2)
							h2=-1;
					}
					
					blocked = tiltLibre(mover.getX()+h1, mover.getY()+h2,mover);
				}
				
			}
			if( mover.modeAffichage==1 ){
				deplacementPlayer(h1,h2,i,1,false);
			}
			else{
				deplacementPlayer(h1,h2,i,2,false);
			}
			break;
		}
	}
	
	public void deplacementAutre(){
		for(int i=0;i<objetG.size();i++){
			ObjetGraphique ini=objetG.elementAt(i);
			if(objetG.elementAt(i).type==PLAYER)
				continue;
			if(objetG.elementAt(i).statistique.status!=0){
				if(objetG.elementAt(i).statistique.maitre!=null){
					//objetG.elementAt(i).statistique.passeTemps(1);
					evasionV(objetG.elementAt(i),false,false);
				}
				objetG.elementAt(i).statistique.passeTemps(1);
				continue;
			}
			if(objetG.elementAt(i).statistique.maitre!=null){
				objetG.elementAt(i).statistique.passeTemps(1);
				evasionV(objetG.elementAt(i),true,false);
				continue;
			}
			if( objetG.elementAt(i).enable==1){
				ObjetGraphique courrant=objetG.elementAt(i);
				int totalChance=0;
				int res=0;
				double distance=0;
				int cible=determinationCible(i);
				if(cible!=-1){
					distance= calculDistance(courrant, objetG.elementAt(cible));
					
					for(int j=0;j<courrant.statistique.attaque.length;j++){
						if(courrant.statistique.attaque[j].range>=(int)distance && courrant.statistique.attaque[j].mpCost<=courrant.statistique.pm)
							if(cibleBloquee( objetG.elementAt(cible).getX(), objetG.elementAt(cible).getY(), courrant.getX(),courrant.getY())==null)
								totalChance+=courrant.statistique.attaque[j].chance;
					}
				}
				if(totalChance==0){
					deplacementAutre2(i);
				}
				else{
					Random hasard=new Random();
					res=hasard.nextInt(100)+1;
					int j=0;
					for(;j<courrant.statistique.attaque.length && res>0;j++){
						if(courrant.statistique.attaque[j].range>=(int)distance && courrant.statistique.attaque[j].mpCost<=courrant.statistique.pm)
							res-=courrant.statistique.attaque[j].chance;
							
					}
					j--;
					
					if(res<1){
						if(courrant.modeAffichage==1){
							animationStand(i,2,false);
							courrant.modeAffichage=1;
						}
						else{
							animationStand(i,4,false);
							courrant.modeAffichage=3;
						}
						courrant.statistique.pm-=courrant.statistique.attaque[j].mpCost;
						if(executerAttaqueEnnemi(j,cible,courrant)){
							objetG.elementAt(cible).statistique.mort(courrant.statistique);
						}
					}
					else{
						deplacementAutre2(i);
					}
				}
				
			}
		 
			if(i<objetG.size())
                            objetG.elementAt(i).statistique.passeTemps(1);
		}
	}

	public void AddProp( int propID, int pX, int pY )
	{
		PropSprite p = new PropSprite( propID, pX, pY );
		p.setPanel( gamePanel );
	}
	
	public void AddProp( int propID, int pX, int pY, int layer )
	{
		PropSprite p = new PropSprite( propID, pX, pY );
		p.setLayer( layer );
		p.setPanel( gamePanel );
	}
	
	public void jouerSon(int type, ObjetGraphique source)
	{
		Random hasard=new Random();
		int nSon=hasard.nextInt(3)+1;
		switch(type)
		{
		case 1:
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(source.statistique.isonD1); break;
			case 2: AudioLibrairie.playClip(source.statistique.isonD2); break;
			case 3: AudioLibrairie.playClip(source.statistique.isonD3); break;
			}
			break;
		case 2:
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(source.statistique.isonS1); break;
			case 2: AudioLibrairie.playClip(source.statistique.isonS2); break;
			case 3: AudioLibrairie.playClip(source.statistique.isonS3); break;
			}
			break;
		case 3:
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(source.statistique.isonB1); break;
			case 2: AudioLibrairie.playClip(source.statistique.isonB2); break;
			case 3: AudioLibrairie.playClip(source.statistique.isonB3); break;
			}
			break;
		case 4: AudioLibrairie.playClip(source.statistique.isonC); break;
		case 5: AudioLibrairie.playClip(source.statistique.isonVo); break;
		case 6: AudioLibrairie.playClip(source.statistique.isonU); break;
		case 7: AudioLibrairie.playClip(source.statistique.isonB); break;
		case 8: AudioLibrairie.playClip(source.statistique.isonAnl); break;
		}
	}
	
	public void evasionV(ObjetGraphique creature, boolean essai, boolean player)
	{
		Random hasard=new Random();
		boolean unbirth=creature.statistique.maitre.getVictime(creature.statistique).isUnbirth();
		boolean analVore=creature.statistique.maitre.getVictime(creature.statistique).isAnal();
		int nSon=hasard.nextInt(3)+1;
		SoundEffect so;
		SoundEffect so2;
		SoundEffect so3;
		if(creature.statistique.maitre.maitre==null)
		{
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(creature.statistique.maitre.isonD1); break;
			case 2: AudioLibrairie.playClip(creature.statistique.maitre.isonD2); break;
			case 3: AudioLibrairie.playClip(creature.statistique.maitre.isonD3); break;
			}
		}
		
		if(creature.statistique.maitre.maitre==null)
		{
			nSon=hasard.nextInt(3)+1;
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(creature.statistique.isonS1); break;
			case 2: AudioLibrairie.playClip(creature.statistique.isonS2); break;
			case 3: AudioLibrairie.playClip(creature.statistique.isonS3); break;
			}
		}
		
		if(creature.statistique.maitre.getVictime(creature.statistique).digTime<creature.statistique.maitre.digTimeMax/2 && creature.statistique.maitre.maitre==null && !unbirth)
		{
			nSon=hasard.nextInt(6)+1;
			switch(nSon)
			{
			case 1: AudioLibrairie.playClip(creature.statistique.maitre.isonB1); break;
			case 2: AudioLibrairie.playClip(creature.statistique.maitre.isonB2); break;
			case 3: AudioLibrairie.playClip(creature.statistique.maitre.isonB3); break;
			}
		}
		
		if(creature.statistique.pv<1 && creature.statistique.vOnly==1)
		{
			if(creature.statistique.appliquerDommage(2,Util.DAMAGE_ACID))
			{
					creature.statistique.mort(creature.statistique.maitre);
				}
			else
			{
				if(creature.statistique.maitre.manaAbsorption>0)
				{
					int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
					creature.statistique.maitre.gainMp(manaDamage/2);
					creature.statistique.gainMp(-manaDamage/2);
				}
			}
			animationStand(getNoObj(creature.statistique.maitre.mere),5,player);
			return;
		}

		if(creature.statistique.pv<1)
			return;

		int dom =creature.statistique.maitre.calculDommageA();
		VHold temp=creature.statistique.maitre.getVictime(creature.statistique);
		if(!essai)
		{
			if(creature.statistique.maitre.manaAbsorption>0)
			{
				int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
				creature.statistique.maitre.gainMp(manaDamage);
				creature.statistique.gainMp(-manaDamage);
			}
			if(!unbirth)
			{
				if(temp.digTime<temp.mere.digTimeMax/2)
				{

					if(creature.statistique.appliquerDommage(dom/2,Util.DAMAGE_ACID))
					{
						creature.statistique.mort(creature.statistique.maitre);
					}
					if(creature.statistique.maitre.manaAbsorption>0)
					{
						int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
						creature.statistique.maitre.gainMp(manaDamage/2);
						creature.statistique.gainMp(-manaDamage/2);
					}
				}
				else
				{
					if(creature.statistique.appliquerDommage(dom*2,Util.DAMAGE_ACID))
					{
						creature.statistique.mort(creature.statistique.maitre);
					}
					if(creature.statistique.maitre.manaAbsorption>0)
					{
						int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
						creature.statistique.maitre.gainMp(manaDamage/2);
						creature.statistique.gainMp(-manaDamage/2);
					}
				}
			}
			else
			{
				if(creature.statistique.appliquerDommage(dom,Util.DAMAGE_ACID))
				{
					creature.statistique.mort(creature.statistique.maitre);
				}
				if(creature.statistique.maitre.manaAbsorption>0)
				{
					int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
					creature.statistique.maitre.gainMp(manaDamage/2);
					creature.statistique.gainMp(-manaDamage/2);
				}
			}
			animationStand(getNoObj(creature.statistique.maitre.mere),5,player);
			return;
		}

		if(temp.digTime>=temp.mere.digTimeMax/2)
		{
			if(creature.statistique.maitre.manaAbsorption>0)
			{
				int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
				creature.statistique.maitre.gainMp(manaDamage);
				creature.statistique.gainMp(-manaDamage);
			}
			
			if(creature.statistique.appliquerDommage(dom*2,Util.DAMAGE_ACID))
			{
				creature.statistique.mort(creature.statistique.maitre);
			}
			animationStand(getNoObj(creature.statistique.maitre.mere),5,player);
			return;
		}
		
		if(creature.statistique.appliquerDommage(dom*2,Util.DAMAGE_ACID))
		{
			creature.statistique.mort(creature.statistique.maitre);
			animationStand(getNoObj(creature.statistique.maitre.mere),5,player);  
			return;
		}
		
		if(creature.statistique.maitre.manaAbsorption>0)
		{
			int manaDamage=hasard.nextInt(creature.statistique.maitre.manaAbsorption*creature.statistique.maitre.niveau)+1;
			creature.statistique.maitre.gainMp(manaDamage/2);
			creature.statistique.gainMp(-manaDamage/2);
		}
		
		int base=creature.statistique.getVEscapeBase()+hasard.nextInt(20)+1;
		if(base>=creature.statistique.maitre.getVResistance()+hasard.nextInt(20)+1)
		{
			if(creature.statistique.maitre.maitre==null){
				if(!unbirth){
					if(analVore){
						AudioLibrairie.playClip(creature.statistique.maitre.isonAnl);
						animationStand(getNoObj(creature.statistique.maitre.mere),-10,player);
					}
					else
						AudioLibrairie.playClip(creature.statistique.maitre.isonVo);
				}    
				else{
					AudioLibrairie.playClip(creature.statistique.maitre.isonU);
					animationStand(getNoObj(creature.statistique.maitre.mere),-8,player);
				}
			}
			
			ObjetGraphique tempSource=src;
			ObjetGraphique tempTarget=tgt;
			tgt=creature.statistique.maitre.mere;
			src=creature;
			creature.statistique.escaped();
			tgt=creature;
			src=creature.statistique.maitre.mere;
			creature.statistique.maitre.escape();
			tgt=tempTarget;
			src=tempSource;
		   
			StatChar te=creature.statistique.maitre;
			if(creature.statistique.maitre.maitre!=null){				
				te.maitre.victimes.add(new VHold(creature.statistique,te.maitre,creature.statistique.maitre.maitre.getVictime(creature.statistique.maitre).getVore(),true));
				
				creature.statistique.maitre.enleverVictime(creature.statistique);
				creature.statistique.maitre=te.maitre;
			}
			else{
				animationStand(getNoObj(creature.statistique.maitre.mere),5,player);
				reaparitionObjetGraphique(creature, temp.mere.mere.getX(),temp.mere.mere.getY());
				creature.statistique.maitre=null;
				creature.enable=1;
				//creature.changerPosition();
				creature.setVisible(true);
				temp.mere.enleverVictime(creature.statistique);
				
			}   
			
			
		}
		else{
			animationStand(getNoObj(creature.statistique.maitre.mere),5,player);
		}
	}
	
	public int getNoObj(ObjetGraphique ob){
		for(int i=0;i<objetG.size();i++){
			if(objetG.elementAt(i)==ob)
				return i;
		}
		return 0;
	}
	
	public void enleverObjetGraphique(StatChar car){
		int i;
		for(i=0;i<objetG.size();i++){
			if( objetG.elementAt(i).statistique==car){
				objetG.removeElementAt(i);
				break;
			}
		}
	}
	
	public void deleteSaveGame( String saveName )
	{
	}
	
	public void renameSaveGame( String oldName, String newName )
	{
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFrom( String saveName )
	{
		if (saveName == null)
		{
			return;
		}

		SaveData saveData = readState( saveName );
		
		if (saveData != null)
		{
			restoreState( saveData );
			
			current_save_file = saveName;
		
			clearmessages();
			printMessage( saveName + " loaded." );
			repaint();
		}
	}
	
	public void saveTo( String saveName )
	{
		if( !saveEnabled || saveName == null )
		{
			return;
		}
		
		SaveData saveData = captureState();
		
		writeState( saveName, saveData );
		printMessage( saveName + " saved." );
		
		current_save_file = saveName;
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public SaveData readState( String saveName )
	{
		String fileName = "Save/" + saveName + ".sav";
		SaveData saveData = DataUtil.readObject( fileName );
		
		return saveData;
	}
	
	public void writeState( String saveName, SaveData saveData )
	{
		String fileName = "Save/" + saveName + ".sav";
		DataUtil.writeObject( fileName, saveData );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private SaveData captureState()
	{	
		SaveData saveData = new SaveData();

		saveData.MapID = map.m_id;
		saveData.Vore = voreEnabled;
		
		{
			saveData.Spells = new Vector<>();
			for (int i = 0; i < spellbook.getElementCount(); ++i)
			{
				Spell spell = (Spell)spellbook.elementAt( i );
				saveData.Spells.add( spell.save() );
			}
		}
		
		{
			saveData.Items = new Vector<>();
			for (int i = 0; i < inventaire.getElementCount(); ++i)
			{
				ObjInventaire item = (ObjInventaire)inventaire.elementAt( i );
				do
				{
					saveData.Items.add( item.save() );
					item = item.getStack();
				} while (item != null);
			}
		}
		
		{
			saveData.StatChars = new Vector<>();
			for (int i = 0; i < objetG.size(); ++i)
			{
				ObjetGraphique obG = objetG.elementAt( i );
				saveData.StatChars.add( obG.save() );
				
				if (obG.type == PLAYER)
				{
					PlayerStat ps = (PlayerStat)obG.statistique;
					
					saveData.Events = new Vector<>();
					Iterator<Evenement> eventIter = ps.listeEvent.iterator();
					while (eventIter.hasNext())
					{
						saveData.Events.add( eventIter.next().save() );
					}
				}
			}
		}
		
		return saveData;
	}
	
	private void restoreState( SaveData saveData )
	{
		// Disable sorted inserts for the inventory and spell lists so we get everything in the
		//  correct order out of the save file
		inventaire.setSortedInsert( false );
		spellbook.setSortedInsert( false );
		
		voreEnabled = saveData.Vore;
		
		chkVore.setSelected( false );
		chkFriendly.setSelected( true );
		ViewDialog.closeAll();

		newLoad();
		
		for (int i = 0; i < objetG.size(); i++)
		{
			objetG.elementAt(i).remove();
		}
		
		objetG.removeAllElements();
		target.removeAllElements();
		spellbook.removeAllElements();
		inventaire.removeAllElements();

		changeBackGround( saveData.MapID );
		
		teleport=true;
		
		for (int i = 0; i < saveData.StatChars.size(); ++i)
		{
			SaveCharacter saveChar = saveData.StatChars.elementAt( i );
			ObjetGraphique newChar = new ObjetGraphique( saveChar );
			
			if (newChar.type == PLAYER)
			{
				// Load the player's inventory and events.
				PlayerStat playerStat = (PlayerStat)newChar.statistique;
				playerStat.loadInventory( saveData.Items );
				playerStat.loadEvents( saveData.Events );
			}
			
			objetG.add( newChar );
		}
		
		// Load spells
		for (int i = 0; i < saveData.Spells.size(); ++i)
		{
			SaveSpell saveSpell = saveData.Spells.elementAt( i );
			Spell newSpell = new Spell( saveSpell );
			newSpell.setNew( false );
			
			spellbook.addElement( newSpell );
		}
		
		// Resolve vhold entries
		for (int i = 0; i < objetG.size(); ++i)
		{
			StatChar statChar = objetG.elementAt( i ).statistique;
			SaveCharacter saveChar = saveData.StatChars.elementAt( i );
			
			if (saveChar.Victimes.size() > 0)
			{
				Iterator<SaveVHold> it = saveChar.Victimes.iterator();
				while (it.hasNext())
				{
					SaveVHold saveVHold = it.next();
					VHold vHold = new VHold( statChar, saveVHold );
					
					statChar.victimes.add( vHold );
					if (vHold.victime != null)
					{
						vHold.victime.maitre = statChar;
					}
				}
			}
		}
		
		enterBackGround( false );

		inventaire.setSortedInsert( true );
		spellbook.setSortedInsert( true );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	/*
	public void loadSave(String nomSave)
	{
		// Open both databases, since they're both used throughout the load process.
		BaseDonnee save_db = BaseDonnee.open_save();

		// Disable sorted inserts for the inventory and spell lists so we get everything in the
		//  correct order out of the save file
		inventaire.setSortedInsert( false );
		spellbook.setSortedInsert( false );
		
		String req;
		ResultSet res;
		
		int saveID=0, mapID=0, playerID=0;
		
		req = "SELECT *";
		req += " FROM Save";
		req += " WHERE Nom = '" + nomSave + "'";
		
		res=save_db.calculRequete(req);
		
		try
		{
			
			if(res.first())
			{
				saveID=res.getInt("SaveID");
				mapID=res.getInt("MapID");
				playerID=res.getInt("Player");
				voreEnabled=res.getInt("Vore");
			}
		}
		catch(Exception e)
		{
			Util.error( e );
		}
		
		save_db.finRequete(res);
		
		chkVore.setSelected(false);
		chkFriendly.setSelected(true);

		loadMap(mapID, saveID, playerID);
		
		//current_save_file = nomSave;
		
		// Re-enable sorted inserts.
		inventaire.setSortedInsert( true );
		spellbook.setSortedInsert( true );

		clearmessages();
		printMessage( nomSave + " loaded." );

		repaint();
		
		save_db.close();
	}
	
	public void loadMap(int mapID, int saveID, int playerID)
	{
		newLoad();

		for(int i=0;i<objetG.size();i++)
		{
			objetG.elementAt(i).remove();
		}

		ViewDialog.closeAll();
		
		objetG.removeAllElements();
		target.removeAllElements();
		spellbook.removeAllElements();
		inventaire.removeAllElements();

		changeBackGround(mapID);

		loadCharacter(saveID, playerID);
		
		enterBackGround( false );

		repaint();
	}
	
	public void loadCharacter(int saveID, int playerID)
	{
		setItemsEnabled( true );
		setSpellsEnabled( true );
		setSaveEnabled( true );
		
		teleport=true;

		BaseDonnee save_db = BaseDonnee.open_save();

		String req;
		ResultSet res;
		
		req="SELECT CarID, PositionX, PositionY, EventID, DeathEventID, Visible, Enable, Deplacement, ModeAffichage, AttackID, LinkID, Type";
		req+=" FROM Caracter";
		req+=" WHERE SaveID="+saveID;
		
		res=save_db.calculRequete(req);
		
		try
		{
			if( res.first() )
			{
				do
				{
					objetG.add(new ObjetGraphique(res, this));
				} while (res.next());
			}
		}
		catch(Exception e)
		{
			Util.error( e );
		}

		save_db.finRequete(res);
		
		for(int i=0;i<objetG.size();i++)
		{
			objetG.elementAt(i).loadStatistique(saveID, playerID);
		}
		
		req = "SELECT SpellID";
		req += " FROM Spell";
		req += " WHERE SaveID = " + saveID;
		
		res=save_db.calculRequete(req);
		
		try
		{
		
			if( res.first() )
			{
				for( ; !res.isAfterLast(); res.next() )
				{
					Spell sp = new Spell( res.getInt(1) );
					sp.setNew( false );
					spellbook.addElement( sp );
				}
			}
		}
		catch(Exception e)
		{
			Util.error( e );
		}

		save_db.finRequete(res);
		
		for(int i=0;i<objetG.size();i++)
		{
			ObjetGraphique temp=objetG.elementAt(i);

			req ="SELECT BonusID, tempMax, temp";
			req +=" FROM Bonus";
			req +=" WHERE SaveID = " + saveID + " AND LinkID = " + temp.caracterID;

			res=save_db.calculRequete(req);
			
			try
			{
				
				if( res.first() )
				{
					for( ; !res.isAfterLast(); res.next() )
					{
						temp.statistique.bonus.addElement(new Bonus(
							res.getInt(1),
							temp.statistique,
							res.getInt(2),
							res.getInt(3) ));
					}
				}
			}
			catch(Exception e)
			{
				Util.error( e );
			}

			save_db.finRequete(res);
		}

		for(int i=0;i<objetG.size();i++)
		{
			ObjetGraphique temp = objetG.elementAt(i);

			req = "SELECT digTime, victime";
			req += " FROM VHold";
			req += " WHERE SaveID = " + saveID + " AND LinkID = " + temp.caracterID;

			res=save_db.calculRequete(req);
			
			try
			{

				if( res.first() )
				{
					for( ; !res.isAfterLast(); res.next() )
					{
						int digTime=res.getInt(1);
						int victime=res.getInt(2);

						if(victime==9999 || victime==-9999 || victime==0)
						{
							if(victime<0)
							{
								temp.statistique.victimes.addElement(new VHold(null,temp.statistique,digTime,VHold.UNBIRTH));
							}
							else
							{
								if(digTime<0)
								{
									temp.statistique.victimes.addElement(new VHold(null,temp.statistique,-digTime,VHold.ANAL));
								}
								else
								{
									temp.statistique.victimes.addElement(new VHold(null,temp.statistique,digTime));
								}
							}
						}
						else
						{
							ObjetGraphique target = null;
							boolean unbirth;

							if(victime<0)
							{
								victime=-victime;
								unbirth=true;
							}
							else
							{
								unbirth=false;
							}

							for(int j=0;j<objetG.size();j++)
							{
								target=objetG.elementAt(j);
								if( target.caracterID==victime )
								{
									break;
								}
							}

							if(!unbirth)
							{
								if(digTime>0)
								{
									temp.statistique.victimes.addElement(new VHold(target.statistique,temp.statistique,digTime));
								}
								else
								{
									temp.statistique.victimes.addElement(new VHold(target.statistique,temp.statistique,-digTime,VHold.ANAL));
								}
							}
							else
							{
								temp.statistique.victimes.addElement(new VHold(target.statistique,temp.statistique,digTime, VHold.UNBIRTH));
							}

							target.statistique.maitre=temp.statistique;
						}
					}
				}
			}
			catch(Exception e)
			{
				Util.error( e );
			}

			save_db.finRequete(res);
		}
		
		save_db.close();
	}
	*/
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void changerMap(int mapID, int positionX, int positionY, boolean script)
	{
		newLoad();
		
		// Preserve the players, npcs, and their victims.
		Vector<ObjetGraphique> save = new Vector<ObjetGraphique>();
		Vector<ObjetGraphique> trash = new Vector<ObjetGraphique>();
		for(int i=0;i<objetG.size();i++)
		{
			ObjetGraphique temp=objetG.elementAt(i);
			if( temp.type == PLAYER || temp.type == NPC || estDansPlayerNpc( temp.statistique ) )
			{
				save.addElement( temp );
			}
			else
			{
				// We're dumping everything else.
				trash.addElement( temp );
			}
		}
		
		// Trash any elements that didn't pass the above filter.
		for( int i = 0; i < trash.size(); ++i )
		{
			trash.elementAt(i).statistique.dispose();
		}
		trash.removeAllElements();
		
		// Clear the entity lists.
		target.removeAllElements();
		objetG.removeAllElements();
		
		// Load the new map.
		changeBackGround(mapID);
		
		// Replace the stored-off objects.
		for( int i = 0; i < save.size(); ++i )
		{
			ObjetGraphique temp = save.elementAt( i );
			
			objetG.addElement( temp );
			target.addElement( temp.statistique );
			temp.setPanel( gamePanel );
		}
	
		// Move the player to their entry point.
		getPlayer().setPosition( positionX, positionY );

		for(int i=0;i<objetG.size();i++)
		{
			ObjetGraphique temp=objetG.elementAt(i);

			if(temp.type==NPC && temp.statistique.maitre==null)
			{
				if(positionY==1 || positionY==2 || positionY==3)
					reaparitionObjetGraphique(temp, positionX, positionY+4);
				else if(positionX==map.m_w || positionX==map.m_w-1 || positionX==map.m_w-2)
					reaparitionObjetGraphique(temp, positionX-4, positionY);
				else if(positionX==1 || positionX==2 || positionX==3)
					reaparitionObjetGraphique(temp, positionX+4, positionY);
				else if(positionY==map.m_h || positionY==map.m_h-1 || positionY==map.m_h-2)
					reaparitionObjetGraphique(temp, positionX, positionY-4);
				else
					reaparitionObjetGraphique(temp, positionX, positionY);
			}
		}

		for(int i=0;i<objetG.size();i++)
		{
			objetG.elementAt(i).reload();
		}
		
		enterBackGround( script );
		
		repaintgame();
	}
	
	public void newLoad()
	{
	}

	public boolean estDansPlayerNpc(StatChar courrant){
		if(courrant.maitre==null)
			return false;
		if(courrant.maitre.mere.type==NPC || courrant.maitre.mere.type==PLAYER)
			return true;
		return estDansPlayerNpc(courrant.maitre);
	}
	
	public void reaparitionObjetGraphique(ObjetGraphique ob,int posX, int posY){
		boolean fini=false;
		int i=0;
		
		if(!tiltLibre(posX,posY)){
			fini=true;
		}
		else{
			while(!fini){
				for(int j=0;j<8+8*i;j++){
					if(j<1)
						posX++;
					else if(j<2+2*i)
						posY--;
					else if(j<4+4*i)
						posX--;
					else if(j<6+6*i)
						posY++;
					else
						posX++;
					if(!tiltLibre(posX,posY, ob)){
						fini=true;
						break;
					}
				}
				i++;
			}
		}
		ob.setPosition( posX, posY );
		repaint();
	}
	
	public boolean executerAttaquePlayer(int cible, ObjetGraphique courrant)
	{
		int jetBase=courrant.statistique.baseAttaque()+courrant.statistique.attackBonus;
		int defense=objetG.elementAt(cible).statistique.getDefense();
		
		SoundEffect so;
		Random hasard=new Random();
		int res=jetBase+hasard.nextInt(20)+1;

		if(courrant.modeAffichage==1)
		{
			animationStand(getNoPlayer(),2,false);
			courrant.modeAffichage=1;
		}
		else
		{
			animationStand(getNoPlayer(),4,false);
			courrant.modeAffichage=3;
		}
		
		PlayerStat te=(PlayerStat)getPlayer().statistique;

		if(res>=defense)
		{
			if(chkVore.isSelected()&& te.arme.projectileID==0)
			{
				jetBase=courrant.statistique.getVAttaque(0)+hasard.nextInt(20)+1;
				defense=objetG.elementAt(cible).statistique.getVDefense()+hasard.nextInt(20)+1;

				if(jetBase>=defense)
				{
					AudioLibrairie.playClip(courrant.statistique.isonV);
					ObjetGraphique target=objetG.elementAt(cible);
					target.setVisible(false);
					repaint();
					target.statistique.maitre=courrant.statistique;
					courrant.statistique.ajouterVictime(target.statistique);
					animationStand(getNoObj(courrant),6,false);
					courrant.modeAffichage=3;
					ObjetGraphique tempSource=src;
					ObjetGraphique tempTarget=tgt;
					tgt=target;
					src=te.mere;
					te.swallowing();
					tgt=te.mere;
					src=target;
					target.statistique.swallowed();
					tgt=tempTarget;
					src=tempSource;
				}
			}
			else
			{
				AudioLibrairie.playClip(courrant.statistique.getSonAttaqueInt(0));
				if(te.arme.projectileID>0)
				{
					int tableau[]=cibleBloquee(objetG.elementAt(cible).getX(), objetG.elementAt(cible).getY(), te.mere.getX(),te.mere.getY());
					int poX, poY;

					if (tableau==null)
					{
						poX=objetG.elementAt(cible).getX();
						poY=objetG.elementAt(cible).getY();
					}
					else
					{
						poX=tableau[0];
						poY=tableau[1];
					}

					ajouterProjectile(te.arme.projectileID, te.mere.getX(), te.mere.getY(),poX,poY, 1, te.mere.tailleY/2, objetG.elementAt(cible).tailleY);
					animationProjectileMove();
					enleverProjectile();

					if(tableau!=null)
					{
						// The projectile was blocked, so there's no effect.
						return false;
					}
				}

				int dommage=courrant.statistique.damageBonus+courrant.statistique.faireDommage()+courrant.statistique.baseDommage();

				if(courrant.statistique.size==1)
				{
					dommage/=2;
				}
				else if(courrant.statistique.size==2)
				{
					dommage*=2;
				}

				if(objetG.elementAt(cible).statistique.appliquerDommage(dommage, courrant.statistique.typeDommage()))
				{
					return true;
				}

				src=getPlayer();
				tgt=objetG.elementAt(cible);
				FenetreTest.instance.executeScript(te.arme.eventID);

				src=getPlayer();
				tgt=objetG.elementAt(cible);
				objetG.elementAt(cible).statistique.beingHit();

				return false;
			}
		}
		
		return false;
	}
	
	public boolean executerAttaqueEnnemi(int attaque, int cible,ObjetGraphique courrant){
		int jetBase=courrant.statistique.attaque[attaque].toucheBonus+courrant.statistique.attackBonus+courrant.statistique.strength+objetG.elementAt(cible).statistique.penalitePv();
		int defense=objetG.elementAt(cible).statistique.getDefense();
		Random hasard=new Random();
		boolean vRate=true;
		AudioLibrairie.playClip(courrant.statistique.getSonAttaqueInt(attaque));
		if(courrant.statistique.attaque[attaque].projectileID>0){
			ajouterProjectile(courrant.statistique.attaque[attaque].projectileID, courrant.getX(), courrant.getY(),objetG.elementAt(cible).getX(), objetG.elementAt(cible).getY(),1,courrant.tailleY/2,objetG.elementAt(cible).tailleY/2);
			animationProjectileMove();
			enleverProjectile();
		}
		
		int res=jetBase+hasard.nextInt(20)+1;
		if(res>=defense){
			if(courrant.statistique.attaque[attaque].vChance>-1){
				if(objetG.elementAt(cible).statistique.vOnly!=2){
					res=hasard.nextInt(100)+1-objetG.elementAt(cible).statistique.penaliteV();
					if(res<=courrant.statistique.attaque[attaque].vChance){
						jetBase=courrant.statistique.getVAttaque(attaque)+hasard.nextInt(20)+1; 
						defense=objetG.elementAt(cible).statistique.getVDefense()+hasard.nextInt(20)+1;
						if(jetBase>=defense){
							ObjetGraphique target=objetG.elementAt(cible);
							if(courrant.statistique.attaque[attaque].typeVore==1){
								AudioLibrairie.playClip(courrant.statistique.isonV);
								
								target.setVisible(false);
								repaintgame();
								target.statistique.maitre=courrant.statistique;
								courrant.statistique.ajouterVictime(target.statistique);
								vRate=false;
								animationStand(getNoObj(courrant),6,false);
								courrant.modeAffichage=3;
							}
							else{
								if(courrant.statistique.attaque[attaque].typeVore==2){
									AudioLibrairie.playClip(courrant.statistique.isonU);
									target.setVisible(false);
									repaintgame();
									target.statistique.maitre=courrant.statistique;
									courrant.statistique.unbirthVictime(target.statistique);
									vRate=false;
									animationStand(getNoObj(courrant),8,false);
									courrant.modeAffichage=3;
								}
								else{
									AudioLibrairie.playClip(courrant.statistique.isonAnl);
									target.setVisible(false);
									repaintgame();
									target.statistique.maitre=courrant.statistique;
									courrant.statistique.analVoringVictime(target.statistique);
									vRate=false;
									animationStand(getNoObj(courrant),10,false);
									courrant.modeAffichage=3;
								}
							}
							ObjetGraphique tempSource=src;
							ObjetGraphique tempTarget=tgt;
							tgt=target;
							src=courrant;
							courrant.statistique.swallowing();
							tgt=courrant;
							src=target;
							target.statistique.swallowed();
							tgt=tempTarget;
							src=tempSource;
							
						}
					}
				}
			}
			if(vRate){
				int dommage=courrant.statistique.damageBonus+courrant.statistique.attaque[attaque].calculDommage();
				if(dommage>0)
					dommage+=courrant.statistique.strength/2;
				if(courrant.statistique.size==1)
					dommage/=2;
				if(courrant.statistique.size==2)
					dommage*=2;
				
				boolean mort=objetG.elementAt(cible).statistique.appliquerDommage(dommage, courrant.statistique.attaque[attaque].typeDommage);
				if(mort)
					return true;
				if(hasard.nextInt(100)+1<=courrant.statistique.attaque[attaque].chanceEffect){
					int v=0;
					switch(courrant.statistique.attaque[attaque].typeResist){
						case 1:
							v=Util.DAMAGE_CURSE;
							break;
						case 2:
							v=Util.DAMAGE_PARALYSIS;
							break;
						case 3:
							v=Util.DAMAGE_SLEEP;
							break;
						case 4:
							v=Util.DAMAGE_CHARM;
							break;
						case 5:
							v=-1;
							break;
					}
					if(v==-1 || hasard.nextInt(100)+1> objetG.elementAt(cible).statistique.resistance[v]){
						if(courrant.statistique.attaque[attaque].save>objetG.elementAt(cible).statistique.getSave()){
							if(courrant.statistique.attaque[attaque].bonusID>0){
								v=hasard.nextInt(courrant.statistique.attaque[attaque].timeMax-courrant.statistique.attaque[attaque].timeMin)+courrant.statistique.attaque[attaque].timeMin+1+courrant.statistique.attaque[attaque].timeBonus;
								objetG.elementAt(cible).statistique.bonus.addElement(new Bonus(courrant.statistique.attaque[attaque].bonusID,objetG.elementAt(cible).statistique,v  ));
							}
							if(courrant.statistique.attaque[attaque].eventID>0){
								src=courrant;
								tgt=objetG.elementAt(cible);
								FenetreTest.instance.executeScript(courrant.statistique.attaque[attaque].eventID);
							}
						}
					}
				}
				src=courrant;
                                if(cible<objetG.size()){
                                    tgt=objetG.elementAt(cible);
                                    objetG.elementAt(cible).statistique.beingHit();
                                }
				
				return false;
			}
		}
		return false;
	}
	
	public boolean tiltLibre( int x, int y )
	{
		if( map.isTileOutside(x-1,y-1) )
		{
			return true;
		}
		
		if( map.getTile( x-1, y-1 ).m_block != 0 )
		{
			return true;
		}

		for( int i=0;i<objetG.size();i++ )
		{
			if( objetG.elementAt(i).getX()==x
				&& objetG.elementAt(i).getY()==y
				&& objetG.elementAt(i).statistique.maitre==null )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean tiltLibre(int x, int y,ObjetGraphique courrant)
	{
		if( map.isTileOutside( x-1, y-1 ) )
		{
			return true;
		}

		if( map.getTile( x-1, y-1 ).m_block != 0 )
		{
			return true;
		}
		
		if( creaturePosition( x, y, courrant) != -1 )
		{
			return true;
		}
		
		return false;
	}
	
	public int creaturePosition( int x, int y, ObjetGraphique courrant )
	{
		if( map.isTileOutside( x-1, y-1 ) )
		{
			return -1;
		}
		
		ObjetGraphique spr = (ObjetGraphique)(map.getTile( x-1, y-1 ).m_sprite);
		
		if( spr != null && spr != courrant )
		{
			for( int i = 0; i < objetG.size(); ++i )
			{
				if( objetG.elementAt(i) == spr )
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	/*
		int best = -1;
		double bestdistsq = 0.0;
		for(int i=0;i<objetG.size();i++)
		{
			ObjetGraphique temp=objetG.elementAt(i);

			if(temp==courrant || temp.statistique.maitre!=null)
				continue;
				
			double distsq=Util.distSq( x, y, temp.getX(), temp.getY() );
			
			if(distsq < 4 && (distsq < bestdistsq || best < 0))
			{
				best = i;
				bestdistsq = distsq;
			}
		}
		return best;
	}
	*/
	
	public int getScriptTilt(int x, int y)
	{
		--x; --y;
		if( map.isTileOutside(x,y) )
		{
			return 0;
		}
		Map.Tile tile = map.getTile(x,y);
		return tile.m_event;
	}
	
	public void executeScriptTilt(int x, int y)
	{
		int evt = getScriptTilt(x,y);
		if( evt > 0 )
		{
			FenetreTest.instance.executeScript(evt);
		}
	}
	
	public ObjetGraphique getObjetG(int p)
	{
		return objetG.elementAt(p);
	}

	public boolean determinationEnnemi(ObjetGraphique test)
	{
		return determinationEnnemi( getPlayer(), test );
	}
	
	public boolean determinationEnnemi(ObjetGraphique courrant, int cible)
	{
		return determinationEnnemi(courrant,objetG.elementAt(cible));
	}
	
	public boolean determinationEnnemi(ObjetGraphique courrant, ObjetGraphique test)
	{
		if( courrant != null && test != null && courrant.statistique != null && test.statistique != null )
		{
			return Util.isEnemy( courrant.statistique.actualSide, test.statistique.actualSide );
		}
		
		// Philosophically, can one hate that which does not exist?  Conversely, can an uncaring
		//  universe be a source of directed hatred?
		return false;
	}

	public int[] cibleBloquee(int cibleX, int cibleY, int sourceX, int sourceY)
	{
		int tableau[]=null;
	   
		float positionY=sourceY;
		float distanceX=cibleX-sourceX;
		float distanceY=cibleY-sourceY;
		if(distanceX==0 && distanceY==0)
			return null;
		boolean arret=false;
		
		short signeX=1;
		short signeY=1;
		
		if(distanceX<0)
			signeX=-1;
		else if(distanceX==0)
			signeX=0;
		
		if(distanceY<0)
			signeY=-1;
		else if(distanceY==0)
			signeY=0;
		
		int position;
		if(distanceX!=0){
			distanceY/=distanceX;
			if(distanceY<0)
				distanceY*=-1;
			
			for(int i=sourceX;i!=cibleX;i+=signeX)
			{
				for(int k=0;k<Math.round(distanceY)+1;k++)
				{
					position = (i-1) +map.m_w*(k*signeY+Math.round(positionY)-1);
					if(map.getTile(position).m_block==1)
					{
						tableau=new int[2];
						tableau[0]=i;
						tableau[1]=k*signeY+Math.round(positionY);
						arret=true;
						break;
						
					}
				}
				
				if(arret)
					break;
				positionY+=(distanceY*signeY);
			}
			
		}
		else{
			for(int i=sourceY;i==cibleY;i+=signeY){
				position = (cibleX-1) +map.m_w*(i-1);
				if(map.getTile(position).m_block==1){
						tableau=new int[2];
						tableau[0]=cibleX;
						tableau[1]=i;
						arret=true;
						break;
				}
			}
		}
		
		return tableau;
	}
	
	public int determinationCible(int perso)
	{
		double distance = 0;
		double temp = 0;
		int cible = -1;

		ObjetGraphique courrant=objetG.elementAt(perso);
		ObjetGraphique test;

		for(int i=0;i<objetG.size();i++)
		{
			if(i==perso)
				continue;

			test=objetG.elementAt(i);
			if(test.enable==0 || !test.isVisible() || !determinationEnnemi(courrant,test))
			{
				continue;
			}
			
			temp = calculDistanceSq(courrant, test);
			if( cible < 0 || temp < distance )
			{
				cible=i;
				distance=temp;
			}
		}
		return cible;
	}

	public double calculDistanceSq(ObjetGraphique x, ObjetGraphique y)
	{
		return Util.distSq( x.getX(), x.getY(), y.getX(), y.getY() );
	}
	
	public double calculDistance(ObjetGraphique x, ObjetGraphique y)
	{
		return Util.dist( x.getX(), x.getY(), y.getX(), y.getY() );
	}
	
	public void nextTurn()
	{
		ObjetGraphique plr = getPlayer();

		finTour();
		
		if( plr != null && plr.statistique != null )
		{
			plr.statistique.passeTemps( 1 );
		}
		
		ViewDialog.updateAll();
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean canMove( ObjetGraphique plr )
	{
		if( plr == null
			||	plr.statistique == null
			||	plr.statistique.status != 0
			||	plr.statistique.maitre != null
			||	dialogue != null )
		{
			return false;
		}
		
		return true;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void doCommand( String ac )
	{
		// All button responses go through here, so this try should catch most frame to frame
		//  errors.
		try
		{
			if( ac == null || dialogue != null )
			{
				// No commands allowed while a dialog is up.
				return;
			}
			
			ObjetGraphique plr = getPlayer();
			
			boolean fin = false;
			
			// These commands are available at any time.
			if(ac == "MENU_SPEED_INC")
			{
				speed = (speed + 1) % FenetreTest.MAX_SPEED;
				saveClientConfig();
				printMessage( "Speed " + (speed+1) );
			}
			else if(ac == "MENU_SPEED_DEC")
			{
				speed = (speed + (FenetreTest.MAX_SPEED - 1)) % FenetreTest.MAX_SPEED;
				saveClientConfig();
				printMessage( "Speed " + (speed+1) );
			}
			else if(ac.startsWith("MENU_SPEED_SET_"))
			{
				speed = Integer.parseInt( ac.substring("MENU_SPEED_SET_".length()) );
				saveClientConfig();
				printMessage( "Speed " + (speed+1) );
			}
			else if(ac == "MENU_SOUND_TOGGLE")
			{
				setSoundEnabled( !isSoundEnabled() );
				printMessage( "Sound " + (isSoundEnabled() ? "ON" : "OFF") );
			}
			else if(ac == "MENU_MOVE_BTNS_TOGGLE")
			{
				setMoveButtonsVisible( !getMoveButtonsVisible() );
				printMessage( "Move Buttons " + (getMoveButtonsVisible() ? "ON" : "OFF") );
			}
			else if(ac == "MENU_SCAT_TOGGLE")
			{
				setScatEnabled( !isScatEnabled() );
				printMessage( "Scat " + (isScatEnabled() ? "ON" : "OFF") );
			}
			else if(ac == "MENU_FAIL_CHECKS_TOGGLE")
			{
				setFailChecks( !getFailChecks() );
				printMessage( (getFailChecks() ? "Now" : "No longer") + " intentionally failing stat checks" );
			}
			else if(ac == "MENU_TEST_PATHS_TOGGLE")
			{
				gamePanel.setShowPaths( !gamePanel.getShowPaths() );
				printMessage( "Test Paths " + (gamePanel.getShowPaths() ? "ON" : "OFF") );
			}
			else if(ac == "MENU_CLOSE_VIEWS")
			{
				ViewDialog.closeAll();
			}
			else if(ac == "INV_INFO")
			{
				if( !inventaire.isSelectionEmpty() )
				{
					((ObjInventaire)inventaire.getSelectedValue()).view();
				}
			}
			else if(ac == "INV_NEXT")
			{
				inventaire.moveSelectedIndex( 1 );
			}
			else if(ac == "INV_PREV")
			{
				inventaire.moveSelectedIndex( -1 );
			}
			else if(ac == "SPELL_INFO")
			{
				if( !spellbook.isSelectionEmpty() )
				{
					((Spell)spellbook.getSelectedValue()).view();
				}
			}
			else if(ac == "SPELL_NEXT")
			{
				spellbook.moveSelectedIndex( 1 );
			}
			else if(ac == "SPELL_PREV")
			{
				spellbook.moveSelectedIndex( -1 );
			}
			else if(ac == "TARGET_INFO")
			{
				if( !target.isSelectionEmpty() )
				{
					((StatChar)target.getSelectedValue()).view();
				}
			}
			else if(ac == "TARGET_NEXT")
			{
				target.moveSelectedIndex( 1 );
			}
			else if(ac == "TARGET_PREV")
			{
				target.moveSelectedIndex( -1 );
			}
			else if(ac == "MENU_LOAD")
			{
				new LoadingDialog();
			}
			else if(ac == "MENU_QUICK_LOAD")
			{
				loadFrom( current_save_file );
			}
			else if(ac == "MENU_QUIT")
			{
				dispose();
				System.exit( 0 );
			}
			else if(ac == "MENU_CHEATS")
			{
				new CheatDialog(this);
			}
			else if(ac == "MENU_BONUSES" )
			{
				if( plr != null && plr.statistique != null )
				{
					Vector<Bonus> bv = plr.statistique.bonus;
					
					printMessage( "Dumping " + bv.size() + " active bonuses:" );
					
					for( int i = 0; i < bv.size(); ++i )
					{
						Bonus b = bv.elementAt( i );
						printMessage( "  [" + b.bonusID + "]: " + b.nom );
					}
					
					printMessage( "Done!" );
				}
			}
			else if(ac == "WALK_STAND")
			{
				// Special case - this really should be in the 'player alive' area, but we want to
				//  be able to advance time with it even when the player is dead.
				if( plr != null && plr.statistique != null )
				{
					if( plr.statistique.maitre != null )
					{
						evasionV( plr, false, false );
					}
					else
					{
						FenetreTest.instance.executeScriptTilt( plr.getX(), plr.getY() );
						
						if( !tiltLibre( plr.getX(), plr.getY(), plr ) )
						{
							deplacementPlayer( 0, 0, 0, plr.statistique.victimes.isEmpty() ? 1 : 3, true );
						}
					}
				}

				fin = true;
			}
			else if( plr == null || plr.statistique == null )
			{
				// Commands below are only available when there is a living player.
			}
			else if(ac == "MENU_SAVE"){
				new SaveDialog();
			}
			else if(ac == "MENU_QUICK_SAVE"){
				saveTo( current_save_file );
			}
			else if( plr.statistique.status != 0 )
			{
				// Commands below are only available if the player is mobile (not paralyzed).
			}
			else if(ac == "WALK_N")
			{
				fin = doMove( plr, 0, -1 );
			}
			else if(ac == "WALK_NW")
			{
				fin = doMove( plr, -1, -1 );
			}
			else if(ac == "WALK_NE")
			{
				fin = doMove( plr, 1, -1 );
			}
			else if(ac == "WALK_W")
			{
				fin = doMove( plr, -1, 0 );
			}
			else if(ac == "WALK_E")
			{
				fin = doMove( plr, 1, 0 );
			}
			else if(ac == "WALK_SW")
			{
				fin = doMove( plr, -1, 1 );
			}
			else if(ac == "WALK_SE")
			{
				fin = doMove( plr, 1, 1 );
			}
			else if(ac == "WALK_S")
			{
				fin = doMove( plr, 0, 1 );
			}
			else if(ac == "TOGGLE_VORE")
			{
				if( chkVore.isEnabled() )
					chkVore.setSelected(!chkVore.isSelected());
			}
			else if(ac == "TOGGLE_FRIENDLY")
			{
				if( chkFriendly.isEnabled() )
					chkFriendly.setSelected(!chkFriendly.isSelected());
			}
			else if( plr.statistique.maitre != null || plr.statistique.polymorph != 0 )
			{
				// Commands below are only available if the player hasn't been eaten or polymorphed.
			}
			else if(ac == "INV_USE")
			{
				if( !inventaire.isSelectionEmpty() )
				{
					((ObjInventaire)inventaire.getSelectedValue()).use();
					fin = true;
				}
			}
			else if(ac == "INV_SELL")
			{
				if( !inventaire.isSelectionEmpty() )
				{
					int idx = inventaire.getSelectedIndex();

					((ObjInventaire)inventaire.getSelectedValue()).sell();
					idx -= idx >= inventaire.getElementCount() ? 1 : 0;

					if( idx >= 0 )
					{
						inventaire.setSelectedIndex( idx );
					}
				}
			}
			else if(ac == "SPELL_CAST")
			{
				if( !spellbook.isSelectionEmpty() )
				{
					fin = doCast( plr, (Spell)spellbook.getSelectedValue() );
				}
			}
			else if(ac == "TARGET_INTERACT")
			{
				StatChar targ = (StatChar)target.getSelectedValue();

				if( !target.isSelectionEmpty() )
				{
					fin = doInteract( plr, targ.mere );
				}
			}
			
			// Run next-turn processing if marked.
			if( fin )
			{
				nextTurn();
			}
			
			// Force a repaint after any command
			repaint();
		}
		catch( Exception e )
		{
			Util.error( e );
		}
	}
	
	public boolean doMove( ObjetGraphique plr, int dx, int dy )
	{
		if( dialogue != null || plr.statistique.status != 0 )
		{
			return false;
		}
		
		if( plr.statistique.maitre != null )
		{
			// If we've been eaten, the only thing we can do is try to escape.
			evasionV( plr, true, false );
			return true;
		}
		
		src = plr;
		FenetreTest.instance.executeScriptTilt( plr.getX() + dx, plr.getY() + dy );
		
		if( !tiltLibre( plr.getX() + dx, plr.getY() + dy, plr ) )
		{
			if( plr.statistique.victimes.isEmpty() )
			{
				deplacementPlayer( dx, dy, 0, 1, true );
			}
			else
			{
				deplacementPlayer( dx, dy, 0, 3, true );
			}
			
			return true;
		}
		else
		{
			int cible = creaturePosition( plr.getX() + dx, plr.getY() + dy, plr );

			if( cible != -1 )
			{
				ObjetGraphique temp2 = getObjetG( cible );
				
				target.setSelectedIndex( target.findElement( temp2.statistique ) );

				if( determinationEnnemi( plr, cible ) )
				{
					if( executerAttaquePlayer( cible, plr ) )
					{
						temp2.statistique.mort( plr.statistique );
					}
				}
				else
				{
					tgt = getObjetG( cible );

					if( tgt.eventID != 0 )
					{
						FenetreTest.instance.executeScript( getObjetG( cible ).eventID );
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public void doInteraction( ObjetGraphique targ )
	{
		ObjetGraphique plr = getPlayer();

		boolean friendly = chkFriendly.isSelected();
		boolean enemy = determinationEnnemi(plr, targ);
		boolean attack = enemy;
		
		if( !enemy )
		{
			if( friendly )
			{
				// Talk
				if(targ.eventID!=0)
				{
					src=plr;
					tgt=targ;
					FenetreTest.instance.executeScript(targ.eventID);
				}
			}
			else
			{
				// Hostile action
				if(targ.attackID!=0)
				{
					src=plr;
					tgt=targ;
					FenetreTest.instance.executeScript(targ.attackID);
				}
				attack = true;
			}
		}
		
		if( attack )
		{
			// Hostile action (attack or vore
			if(executerAttaquePlayer(getNoObj(targ), plr))
			{
				targ.statistique.mort(plr.statistique);
			}
		}
	}
	
	public boolean doInteract( ObjetGraphique plr, ObjetGraphique targ )
	{
		if( targ == plr || targ.statistique.maitre != null )
		{
			// Can't attack ourselves or a target that's been eaten.
			return false;
		}

		if( plr.statistique.getRangeAttaque(0) + 1 > calculDistance( plr, targ ) )
		{
			doInteraction( targ );
			
			return true;
		}
		else
		{
			walkTo( targ.getX(), targ.getY(), false );
		}
		
		return false;
	}
	
	public boolean doCast( ObjetGraphique plr, Spell sort )
	{
		sort.reload();

		Random hasard=new Random();
		int valeur2=hasard.nextInt(sort.castDifficulte)+1;
		if( plr.statistique.pm >= sort.mpCost
			&& valeur2 <= plr.statistique.getSortBase()
			&& (sort.transformID==0 || sort.transformID==plr.carID) )
		{
			plr.statistique.pm-=sort.mpCost;
			plr.statistique.pmMax-=sort.permanentCost;
			
			switch(sort.type)
			{
			case 0:
			case 1:
				if( target.getSelectedIndex() != -1
					&& ((StatChar)target.getSelectedValue()).maitre == null)
				{
					StatChar cible=(StatChar)target.getSelectedValue();
					int time=hasard.nextInt(sort.timeMax-sort.timeMin)+sort.timeMin+sort.timeBonus+1;
					time+=sort.timeBonusInt*plr.statistique.intelligence/100;
					boolean test=true;
					AudioLibrairie.playClip(sort.isonCast);
					int tableau[]=cibleBloquee(cible.mere.getX(), cible.mere.getY(), plr.getX(),plr.getY());
					int poX;
					int poY;
					if (tableau==null)
					{
						poX=cible.mere.getX();
						poY=cible.mere.getY();
					}
					else
					{
						poX=tableau[0];
						poY=tableau[1];
					}
					   
					if(sort.type==1)
					{
						ajouterProjectile(sort.projectileID,plr.getX(), plr.getY(), poX, poY,1,plr.tailleY/2, cible.mere.tailleY/2);
						animationProjectileMove();
						enleverProjectile();
					}
					
					AudioLibrairie.playClip(sort.isonCible);
					ajouterProjectile(sort.projectileID,poX, poY, poX, poY, 2, plr.tailleY/2, cible.mere.tailleY/2);
					animationProjectileStand();
					enleverProjectile();
					
					if(tableau!=null)
					{
						break;
					}
					
					if(sort.harmful==0)
					{
						valeur2=hasard.nextInt(100)+1;
						switch(sort.typeResist)
						{
						case 0:
							break;
						case 1:
							if(valeur2<cible.resistance[Util.DAMAGE_CURSE])
								test=false;
							break;
						case 2:
							if(valeur2<cible.resistance[Util.DAMAGE_PARALYSIS])
								test=false;
							break;
						case 3:
							if(valeur2<cible.resistance[Util.DAMAGE_SLEEP])
								test=false;
							break;
						case 4:
							if(valeur2<cible.resistance[Util.DAMAGE_CHARM])
								test=false;
							break;
						}
						if(sort.save<=cible.getSave())
						{
							test=false;
						}
					}
					if(test)
					{	
						valeur2=hasard.nextInt(sort.dommageMax-sort.dommageMin)+sort.dommageMin+sort.dommageBonus+1;
						valeur2+=sort.dommageBonusInt*plr.statistique.intelligence/100;
						if(sort.harmful==0)
						{
							if(cible.appliquerDommage(valeur2,sort.dommageType))
								cible.mort(plr.statistique);
						}
						if(sort.vore>0)
						{
							valeur2=hasard.nextInt(100)+1;
							if(valeur2<=sort.vore)
							{
								//vore réussit
								cible.mere.setVisible(false);
								cible.maitre=plr.statistique;
								plr.statistique.ajouterVictime(cible);
								plr.modeAffichage=3;
							}
						}
						
						if(!determinationEnnemi(plr, getNoObj(cible.mere))&& sort.harmful==0)
						{
							if(cible.mere.attackID!=0)
							{
								src=plr;
								tgt=cible.mere;
								FenetreTest.instance.executeScript(cible.mere.attackID);
							}
						}
						if(sort.bonusID!=0)
						{
							int bon=cible.isEnchanted(sort.bonusID);
							if(bon!=-1)
							{
								Bonus tem=(Bonus)cible.bonus.elementAt(bon);
								if(tem.stack>0)
									tem.tempsMax+=time;
								else
									cible.bonus.addElement(new Bonus(sort.bonusID,cible,time));
							}
							else
								cible.bonus.addElement(new Bonus(sort.bonusID,cible,time));
						}
						if(sort.eventID!=0)
						{
							src=plr;
							tgt=cible.mere;
							FenetreTest.instance.executeScript(sort.eventID);
						}
					}
				}
				break;
			case 3:
				if(inventaire.getSelectedIndex()!=-1)
				{
					ObjInventaire cible=(ObjInventaire)inventaire.getSelectedValue();
					if(cible.isEquippable() && cible.bonusID==0)
					{
						if(cible.equipped)
							cible.unequip();

						cible.changerBonus(sort.bonusID);
						AudioLibrairie.playClip(sort.isonCast);
						ajouterProjectile(sort.projectileID,plr.getX(), plr.getY(), plr.getX(), plr.getY(),2, plr.tailleY/2, plr.tailleY/2);
						animationProjectileStand();
						enleverProjectile();
					}
				}
				break;
			}
			
			return true;
		}
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private abstract class Walker
	{
		public GameSprite m_mover;

		public Walker( GameSprite mover )
		{
			m_mover = mover;
		}

		public abstract boolean findPath( Vector<Map.Tile> out );
		public abstract boolean isBlocked( Map.Tile check );

		public boolean needsRepath( Vector<Map.Tile> path, int step )
		{
			for( int i = 1; step + i < path.size() && i < 4; ++i )
			{
				Map.Tile t = path.elementAt( step+i );
				
				if( isBlocked( t ) )
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	private class TileWalker extends Walker
	{
		public Map.Tile m_dest;
		
		public TileWalker( GameSprite mover, Map.Tile dest )
		{
			super( mover );
			m_dest = dest;
		}
		
		public boolean findPath( Vector<Map.Tile> out )
		{
			return map.as_findPathToTile( m_mover.getX(), m_mover.getY(), m_dest, m_mover, out );
		}
		
		public boolean isBlocked( Map.Tile check )
		{
			return (check.m_block != 0)
				|| (check.m_event > 0)
				|| (check.m_sprite != null && check.m_sprite != m_mover);
		}
	}
	
	private class SpriteWalker extends Walker
	{
		GameSprite m_toSpr;
		int m_sprX, m_sprY;
		
		public SpriteWalker( GameSprite mover, GameSprite dest )
		{
			super( mover );
			m_toSpr = dest;
			m_sprX = m_toSpr.getX();
			m_sprY = m_toSpr.getY();
		}
		
		public boolean findPath( Vector<Map.Tile> out )
		{
			m_sprX = m_toSpr.getX();
			m_sprY = m_toSpr.getY();
			return map.as_findPathToSprite( m_mover.getX(), m_mover.getY(), m_toSpr, m_mover, out );
		}
		
		public boolean isBlocked( Map.Tile check )
		{
			return (check.m_block != 0)
				|| (check.m_event > 0)
				|| (check.m_sprite != null && check.m_sprite != m_mover && check.m_sprite != m_toSpr);
		}
		
		public boolean needsRepath( Vector<Map.Tile> path, int step )
		{
			// If our target sprite moves, we need to do a repath.
			return m_toSpr.getX() != m_sprX
				|| m_toSpr.getY() != m_sprY
				|| super.needsRepath( path, step );
		}
	}
	
	private class EventWalker extends Walker
	{
		int m_event;

		public EventWalker( GameSprite mover, int id )
		{
			super( mover );
			m_event = id;
		}
		
		public boolean findPath( Vector<Map.Tile> out )
		{
			return map.as_findPathToEvent( m_mover.getX(), m_mover.getY(), m_event, m_mover, out );
		}
		
		public boolean isBlocked( Map.Tile check )
		{
			return (check.m_block != 0 && check.m_event != m_event)
					|| (check.m_event > 0 && check.m_event != m_event && !check.m_landmine)
					|| (check.m_sprite != null && check.m_sprite != m_mover);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private boolean walkingTo = false;
	private boolean walkingToIgnore = false;
	
	public void cancelWalkTo()
	{
		walkingTo = false;
	}
	
	public void cancelWalkTo( boolean ignorable )
	{
		if( !ignorable || !walkingToIgnore )
		{
			cancelWalkTo();
		}
	}
	
	public void cancelWalkTo( ObjetGraphique plr )
	{
		if( plr == getPlayer() )
		{
			cancelWalkTo();
		}
	}
	
	public void cancelWalkTo( ObjetGraphique plr, boolean ignorable )
	{
		if( plr == getPlayer() )
		{
			cancelWalkTo( ignorable );
		}
	}
	
	public void walkTo( int x, int y, boolean ignore )
	{
		// Walk to a target position.  Why we're walking there and how we behave on the way will
		//  differ based on what is in that position.
		ObjetGraphique plr = getPlayer();
		if( !canMove( plr ) )
		{
			if( dialogue == null )
			{
				if( plr != null && plr.statistique != null && plr.statistique.maitre != null && plr.statistique.status == 0 )
				{
					evasionV( plr, true, false );
				}

				nextTurn();
			}

			return;
		}

		if( map.isTileOutside( x-1, y-1 ) )
		{
			return;
		}
		
		Map.Tile to = map.getTile( x-1, y-1 );
		
		Walker walker = null;
		
		// Determine which walker to use and create one.
		if( to.m_sprite != null && to.m_sprite != plr )
		{
			walker = new SpriteWalker( plr, to.m_sprite );
		}
		else if( to.m_event > 0 && !to.m_landmine )
		{
			walker = new EventWalker( plr, to.m_event );
		}
		else
		{
			walker = new TileWalker( plr, to );
		}
		
		Vector<Map.Tile> path = new Vector<Map.Tile>();

		if( !walker.findPath( path ) )
		{
			return;
		}
		
		// Now walk the path one step at a time.
		walkingTo = true;
		walkingToIgnore = ignore;

		for( int i = 1; i < path.size() && walkingTo; ++i )
		{
			Map.Tile a = path.elementAt( i-1 );
			Map.Tile b = path.elementAt( i );
			
			// Attempt the official move and advance the turn ticker.
			int lasthp = plr.statistique.pv;
			boolean moved = doMove( plr, b.m_loc.x - a.m_loc.x, b.m_loc.y - a.m_loc.y );
			
			// If the move failed, we bail before we can do the 'next turn' call.
			if( !moved )
			{
				// Something kept us from moving - abort.
				break;
			}
			
			nextTurn();
			
			// Re-fetch the player in case something caused it to go away.
			plr = getPlayer();
			
			// If something has changed and the player can no longer continue, stop the walk.
			if( !canMove( plr ) )
			{
				// No player, or player is 'inconvenienced'.
				break;
			}
			else if( plr.statistique.pv < lasthp && !ignore )
			{
				// Player was attacked (or took damage... Either way, they may want to react)
				break;
			}
			
			// Test to see if we need to repath.
			if( walker.needsRepath( path, i ) )
			{
				Vector<Map.Tile> newpath = new Vector<Map.Tile>();
				if( !walker.findPath( newpath )
					|| newpath.size() > Math.max( 20, (path.size() - i) * 2 ) )
				{
					break;
				}
				
				path = newpath;
				i = 0;
			}

			pauseFor( 50 );
		}

		walkingTo = false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private Hashtable keyCommands;
	
	private class KeyCombo
	{
		int key, mods;
		public KeyCombo( int key, int mods )
		{
			// Control and alt are used for menu shortcuts
			this.mods = mods;
			this.key = key;
		}
		
		@Override // Object
		public boolean equals( Object o )
		{
			KeyCombo oo = (KeyCombo)o;
			return oo.key == key && oo.mods == mods;
		}
		
		@Override // Object
		public int hashCode()
		{
			return key | (mods << 16);
		}
		
		@Override // Object
		public String toString()
		{
			return "KeyCombo[key=" + KeyEvent.getKeyText( key ) + ";mods=" + InputEvent.getModifiersExText( mods ) + "]";
		}
	}
	
	public void bindKey( int key, int mods, String cmd )
	{
		mods = mods & ~(InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.ALT_MASK);
		KeyCombo kc = new KeyCombo( key, mods );
		keyCommands.put( kc, cmd );
	}
	
	public void bindKey( int key, String cmd )
	{
		bindKey( key, 0, cmd );
	}
	
	public void unbindKey( int key, int mods )
	{
		mods = mods & ~(InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.ALT_MASK);
		KeyCombo kc = new KeyCombo( key, mods );
		keyCommands.remove( kc );
	}
	
	public String getKeyCommand( int key, int mods )
	{
		KeyCombo kc = new KeyCombo( key, mods );
		return (String)(keyCommands.get( kc ));
	}
	
	private void initializeShortcuts()
	{
		// Init the keyboard shortcuts
		keyCommands = new Hashtable();
		
		bindKey( 97, "WALK_SW" );
		bindKey( 98, "WALK_S" );
		bindKey( 99, "WALK_SE" );
		
		bindKey( 100, "WALK_W" );
		bindKey( 101, "WALK_STAND" );
		bindKey( 102, "WALK_E" );
		
		bindKey( 103, "WALK_NW" );
		bindKey( 104, "WALK_N" );
		bindKey( 105, "WALK_NE" );
		
		bindKey( KeyEvent.VK_M, "WALK_SW" );
		bindKey( KeyEvent.VK_COMMA, "WALK_S" );
		bindKey( KeyEvent.VK_PERIOD, "WALK_SE" );
		
		bindKey( KeyEvent.VK_K, "WALK_W" );
		bindKey( KeyEvent.VK_L, "WALK_STAND" );
		bindKey( KeyEvent.VK_SEMICOLON, "WALK_E" );
		
		bindKey( KeyEvent.VK_I, "WALK_NW" );
		bindKey( KeyEvent.VK_O, "WALK_N" );
		bindKey( KeyEvent.VK_P, "WALK_NE" );
		
		bindKey( KeyEvent.VK_V, "TOGGLE_VORE" );
		bindKey( KeyEvent.VK_F, "TOGGLE_FRIENDLY" );
		
		bindKey( KeyEvent.VK_C, "SPELL_CAST" );
		bindKey( KeyEvent.VK_U, "INV_USE" );
		bindKey( KeyEvent.VK_A, "TARGET_INFO" );
		
		bindKey( KeyEvent.VK_S, "SPELL_NEXT" );
		bindKey( KeyEvent.VK_R, "INV_NEXT" );
		bindKey( KeyEvent.VK_T, "TARGET_NEXT" );
		
		bindKey( KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK, "SPELL_PREV" );
		bindKey( KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK, "INV_PREV" );
		bindKey( KeyEvent.VK_T, InputEvent.SHIFT_DOWN_MASK, "TARGET_PREV" );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // FocusListener
	public void focusGained(FocusEvent e)
	{
		if( e.getOppositeComponent() == null )
		{
			ViewDialog.setAllVisible( true );
		}
	}

	@Override // FocusListener
	public void focusLost(FocusEvent e)
	{
		if( e.getOppositeComponent() == null )
		{
			ViewDialog.setAllVisible( false );
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // KeyListener
	public void keyTyped(KeyEvent e) {}
	@Override // KeyListener
	public void keyReleased(KeyEvent e) {}
	@Override // KeyListener
	public void keyPressed(KeyEvent e)
	{
		String cmd = getKeyCommand( e.getKeyCode(), e.getModifiersEx() );
		if( cmd != null )
		{
			doCommand( cmd );
			repaint();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ListSelectionListener
	public void valueChanged( ListSelectionEvent e )
	{
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private static int s_mouseDownMods = 0;
	private static boolean s_mouseDown = false;
	
	public static final int IGNORE_ATTACKS_MASK = InputEvent.BUTTON3_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK;

	@Override // MouseListener
	public void mousePressed(MouseEvent e)
	{
		if( e.getButton() == 1 )
		{
			s_mouseDownMods = e.getModifiersEx();
			s_mouseDown = true;
		}
	}
	@Override // MouseListener
	public void mouseReleased(MouseEvent e)
	{
		if( s_mouseDown && e.getButton() == 1 )
		{
			// +1,+1 due to base 1 map coordinate override.
			int x = gamePanel.getCursorX() + 1;
			int y = gamePanel.getCursorY() + 1;
			
			s_mouseDownMods &= e.getModifiersEx();
			
			boolean ignore = (s_mouseDownMods & IGNORE_ATTACKS_MASK) != 0;

			walkTo( x, y, ignore );
			
			s_mouseDownMods = 0;
			s_mouseDown = false;
		}
	}
	@Override // MouseListener
	public void mouseEntered(MouseEvent e)
	{
		s_mouseDownMods = 0;
		s_mouseDown = false;
	}
	@Override // MouseListener
	public void mouseExited(MouseEvent e)
	{
		s_mouseDownMods = 0;
		s_mouseDown = false;
	}
	@Override // MouseListener
	public void mouseClicked(MouseEvent e) {}

	///////////////////////////////////////////////////////////////////////////////////////////////
	static Icon banner_green;
	static Icon banner_white;
	static Icon banner_red;
	
	static Icon flag_green;
	static Icon flag_white;
	static Icon flag_red;
	
	static Icon spell_red;
	static Icon spell_blue;

	static Color itemUnusableFore = Color.RED;
	static Color itemEquippedFore = Color.BLUE;
	static Color itemNewBack = new Color( 0xfff8aa );
	static Color itemEquippedBack = new Color( 0xf0f0f0 );

	static Color spellUnusableFore = Color.RED;
	static Color spellNewBack = new Color( 0xfff8aa );
	
	protected void initializeIcons()
	{
		banner_green = Util.getIcon("banner_green"); // Ally
		banner_white = Util.getIcon("banner_white"); // Friend
		banner_red   = Util.getIcon("banner_red"); // Hostile

		flag_green = Util.getIcon("flag_green"); // Ally
		flag_white = Util.getIcon("flag_white"); // Friend
		flag_red   = Util.getIcon("flag_red"); // Hostile

		spell_blue = Util.getIcon("spell_blue"); // Good
		spell_red  = Util.getIcon("spell_red"); // Bad
	}
	
	class ItemCellRenderer extends JLabel implements ListCellRenderer
	{
		public ItemCellRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			ObjInventaire item = (ObjInventaire)value;

			if( item.equipped || isSelected )
			{
				item.setNew( false );
			}

			// Set the foreground color (text color).
			if( item.equipped )
			{
				setForeground( itemEquippedFore );
			}
			else if( !item.canUse() )
			{
				setForeground( itemUnusableFore );
			}
			else if( isSelected )
			{
				setForeground( list.getSelectionForeground() );
			}
			else
			{
				setForeground( list.getForeground() );
			}

			// Set the background color.
			if( isSelected )
			{
				setBackground( list.getSelectionBackground() );
			}
			else if( item.equipped )
			{
				setBackground( itemEquippedBack );
			}
			else if( item.isNew() )
			{
				setBackground( itemNewBack );
			}
			else
			{
				setBackground( list.getBackground() );
			}

			// Set the text.
			setText(item.toString());
			setFont(list.getFont());
			
			setToolTipText( item.getLevelString() );
			
			// There are several kinds of items, each one has its own icon.
			setIcon( Util.ITEM_TYPES[item.type].icon );

			return this;
		}
	}
	
	class SpellCellRenderer extends JLabel implements ListCellRenderer
	{
		public SpellCellRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			Spell sp = (Spell)value;
			
			ObjetGraphique temp = getPlayer();
			PlayerStat plr = null;
			if( temp != null ) plr = (PlayerStat)temp.statistique;

			if( isSelected )
			{
				sp.setNew( false );
			}

			// Set the foreground color (text color).
			if( plr != null && (sp.mpCost > plr.pm || sp.permanentCost > plr.pmMax) )
			{
				setForeground( spellUnusableFore );
			}
			else if( isSelected )
			{
				setForeground( list.getSelectionForeground() );
			}
			else
			{
				setForeground( list.getForeground() );
			}

			// Set the background color.
			if( isSelected )
			{
				setBackground( list.getSelectionBackground() );
			}
			else if( sp.isNew() )
			{
				setBackground( spellNewBack );
			}
			else
			{
				setBackground( list.getBackground() );
			}

			// Set the text.
			setText(sp.toString());
			setFont(list.getFont());
			
			Util.SpellType st = Util.SPELL_TYPES[sp.getSpellType()];
			setToolTipText( "Difficulty " + sp.castDifficulte + " " + st.name );
			
			// Set the icon.
			if( sp.harmful == 0 )
			{
				// Yes, a 'harmful' value of 0 means it _is_ harmful.
				setIcon( spell_red );
			}
			else
			{
				setIcon( spell_blue );
			}

			return this;
		}
	}

	public static final Color TARGET_ENEMY_BACK = new Color( 0x4b0501 );
	public static final Color TARGET_ENEMY_FORE = new Color( 0xe5625c );
	public static final Color TARGET_ALLY_BACK = new Color( 0x083809 );
	public static final Color TARGET_ALLY_FORE = new Color( 0x79F17F );
	public static final Color TARGET_BACK = new Color( 0x444444 );
	public static final Color TARGET_FORE = new Color( 0xd5d5d5 );
	
	public static final Color TARGET_EATEN_BACK = Color.RED;
	
	class TargetCellRenderer extends IconBar implements ListCellRenderer
	{
		private Color m_backdrop;

		public TargetCellRenderer()
		{
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			StatChar st = (StatChar)value;

			// Set the text.
			setText(st.toString());
			setFont(list.getFont());
			
			setMaximum(st.pvMax);
			setValue(st.pv);
			
			String tip = st.getLevelString() + ", " + st.pv + " / " + st.pvMax + " Health";
			
			setPreferredSize( new Dimension( list.getWidth(), IconBar.BAR_HEIGHT ) );
			setMaximumSize( new Dimension( list.getWidth(), IconBar.BAR_HEIGHT ) );
			
			
			Color backdrop = list.getBackground();
			Color fore = TARGET_FORE;
			Color back = TARGET_BACK;
			Icon icon = flag_white;
			
			// Targets come in four types: Player, Ally, Friendly, and Hostile.
			if( getPlayer() != null && determinationEnnemi( st.mere ) )
			{
				back = TARGET_ENEMY_BACK;
				fore = TARGET_ENEMY_FORE;
				icon = flag_red;
				tip += ", Hostile";
			}
			else if( st.mere == getPlayer() )
			{
				back = TARGET_ALLY_BACK;
				fore = TARGET_ALLY_FORE;
				icon = banner_green;
			}
			else if( st.mere.type == FenetreTest.NPC )
			{
				back = TARGET_ALLY_BACK;
				fore = TARGET_ALLY_FORE;
				icon = flag_green;
				tip += ", Ally";
			}
			
			// Tint the bar colors if we're selected.
			if( isSelected )
			{
				backdrop = list.getSelectionBackground();
				fore = Util.mixColors( fore, backdrop, 0.5 );
			}
			
			if( st.maitre != null )
			{
				backdrop = TARGET_EATEN_BACK;
				tip += ", Eaten";
			}
			
			m_backdrop = backdrop;
			setBackground( back );
			setForeground( fore );
			setIcon( icon );
			
			setToolTipText( tip );

			return this;
		}
		
		@Override // IconBar
		public void paintComponent( Graphics g )
		{
			g.setColor( m_backdrop );
			g.fillRect( 0, 0, getWidth(), getHeight() );
			super.paintComponent( g );
		}
	}
	
	public void executeScript( int even )
	{
		executeScript( even, src, tgt );
	}
	
	public void executeScript( String script )
	{
		executeScript( script, src, tgt );
	}
	
	public void executeScript( ProcessedScript pscript )
	{
		executeScript( pscript, src, tgt );
	}
	
	public void executeScript( int even, ObjetGraphique in_src, ObjetGraphique in_tgt )
	{
		if (even > 0)
		{
			Event event = Event.getEvent( even );
			
			if (event != null)
			{
				executeScript( event.getScript(), in_src, in_tgt );
			}
		}
	}
	
	public void executeScript( String script, ObjetGraphique in_src, ObjetGraphique in_tgt )
	{
		// Pass-through to support cheat codes and any literal 'next step' execution I might've
		//  missed.
		ProcessedScript pscript = new ProcessedScript( script );
		executeScript( pscript, in_src, in_tgt );
	}
	
	public void executeScript( ProcessedScript pscript, ObjetGraphique in_src, ObjetGraphique in_tgt )
	{
		ObjetGraphique tempsrc = src;
		ObjetGraphique temptgt = tgt;
		
		src = in_src;
		tgt = in_tgt;

		pscript.execute();

		src = tempsrc;
		tgt = temptgt;
		
		// In the case of a chain of dialog boxes, we want to update our UI state between them,
		//  since no game time may be passing.
		
		repaint();
		ViewDialog.updateAll();
	}
}
