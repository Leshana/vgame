///////////////////////////////////////////////////////////////////////////////////////////////////
//
// GameListDialog.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
abstract class GameListDialog extends JDialog implements ListSelectionListener, ActionListener
{
	protected DefaultListModel<String> m_model;
	protected JList m_list;
	private JScrollPane m_scroller;

	//private JLabel m_nom, m_exp, m_hp, m_mp, m_food, m_money;
	
	protected CharacterPanel m_charPanel = null;
	
	protected JPanel m_buttonPanel = null;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public GameListDialog( String title )
	{
		Rectangle b = VGame.instance.getBounds();
		
		int w = 500;
		int h = 300;

		b.setLocation( (int)(b.getX() + (b.getWidth() - w) / 2),
					   (int)(b.getY() + (b.getHeight() - h) / 2) );
		b.setSize( w, h );
		
		setBounds( b );
		setTitle( title );
		setModal( true );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setResizable( false );

		setVisible( false );

		initializeComponents();
		initializeGameList();
		
		if( m_model.size() > 0 )
		{
			m_list.setSelectedIndex( 0 );
		}
		
		setVisible( true );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getSelectedName()
	{
		String sn = "";
		if( !m_list.isSelectionEmpty() )
		{
			sn = (String)m_list.getSelectedValue();
		}
		
		return sn;
	}
	
	public boolean doesNameExist( String name )
	{
		// Return true if the pased name is already in our list.
		for( int i = 0; i < m_model.size(); ++i )
		{
			if( name.equalsIgnoreCase( (String)(m_model.elementAt( i )) ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected JButton addButton( String name, String cmd )
	{
		if( m_buttonPanel != null )
		{
			JButton btn = new JButton( name );
			btn.setActionCommand( cmd );
			btn.addActionListener( this );

			m_buttonPanel.add( btn );
			
			return btn;
		}
		
		return null;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ListSelectionListener
	public void valueChanged( ListSelectionEvent e )
	{
		try
		{
			if( !e.getValueIsAdjusting() )
			{
				updateStats();
				updateButtons();
			}
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
	}
	
	@Override // ActionListener
	public void actionPerformed( ActionEvent e )
	{
		try
		{
			onButton( e.getActionCommand() );
		}
		catch( Exception ex )
		{
			Util.error( ex );
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	protected abstract void updateButtons();
	protected abstract void onButton( String cmd );
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void updateStats()
	{
		// Read character stats for the currently selected save file from the database.
		if( m_list.isSelectionEmpty() )
		{
			m_charPanel.setPlayer( null );
		}
		else
		{
			String saveName = (String)m_list.getSelectedValue();
			SaveData saveData = FenetreTest.instance.readState( saveName );
			
			if (saveData != null)
			{
				for (int i = 0; i < saveData.StatChars.size(); ++i)
				{
					SaveCharacter saveChar = saveData.StatChars.elementAt( i );
					if (saveChar.Type == StatChar.PLAYER)
					{
						PlayerStat player = new PlayerStat( saveChar );
						m_charPanel.setPlayer( player );
						break;
					}
				}
			}
		}
	}
	
	protected void updateList()
	{
		int selIdx = m_list.getSelectedIndex();
		String selName = (String)m_list.getSelectedValue();
		
		m_model.removeAllElements();
		
		initializeGameList();
		
		if( selIdx >= m_model.size() )
		{
			selIdx = m_model.size() - 1;
		}
		
		// If we had an entry selected, make sure it stays selected.
		for( int i = 0; i < m_model.size(); ++i )
		{
			if( ((String)m_model.elementAt( i )).equals( selName ) )
			{
				selIdx = i;
				break;
			}
		}
		
		if( selIdx >= 0 )
		{
			m_list.setSelectedIndex( selIdx );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	protected void initializeComponents()
	{
		Dimension spacer = new Dimension( 5, 5 );

		// List panel
		JPanel listPanel = new JPanel();
		listPanel.setLayout( new BoxLayout( listPanel, BoxLayout.Y_AXIS ) );
		listPanel.setBorder( BorderFactory.createTitledBorder("Saved Games") );
		{
			m_model = new DefaultListModel();
			m_list = new JList( m_model );
			m_list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
			m_list.addListSelectionListener(this);

			m_scroller = new JScrollPane( m_list,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
			
			listPanel.add( m_scroller );
		}
		
		// Stats panel
		m_charPanel = new CharacterPanel();
		
		// Button panel
		m_buttonPanel = new JPanel();
		m_buttonPanel.setLayout( new BoxLayout( m_buttonPanel, BoxLayout.X_AXIS ) );
		{
			m_buttonPanel.add( Box.createHorizontalGlue() );
		}

		// Set up the page layout
		Container conteneur = getContentPane();
		conteneur.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		// List panel - left side.
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 2;
		conteneur.add( listPanel, c );
		
		// Stats panel - top right
		c.weightx = 0.0; c.weighty = 1.0;
		c.gridx = 1; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( m_charPanel, c );
		
		// Button panel - bottom right
		c.weightx = 0.0; c.weighty = 0.0;
		c.gridx = 1; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( m_buttonPanel, c );
	}
	
	protected void initializeGameList()
	{
		File saveDir = new File( "./Save" );
		
		if (!saveDir.exists())
		{
			saveDir.mkdir();
		}
		
		if (saveDir.exists() && saveDir.isDirectory())
		{
			File[] fileList = saveDir.listFiles( new FilenameFilter(){
				public boolean accept( File dir, String name ){
					return name.endsWith(".sav");
				}
			});
			
			Arrays.sort(fileList, new Comparator<File>(){
				public int compare(File f1, File f2)
				{
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				} });
			
			for (int i = 0; i < fileList.length; ++i)
			{
				String name = fileList[i].getName();
				m_model.addElement( name.substring( 0, name.length() - 4 ) );
			}
		}
	}
}
