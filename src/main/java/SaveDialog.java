///////////////////////////////////////////////////////////////////////////////////////////////////
//
// SaveDialog.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class SaveDialog extends GameListDialog
{
	protected JButton m_saveas;
	protected JButton m_save;
	
	public static final String CMD_SAVE_AS = "SAVE_AS";
	public static final String CMD_SAVE = "SAVE";
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public SaveDialog()
	{
		super( "Save Game" );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // GameListDialog
	protected void updateButtons()
	{
		String sn = getSelectedName();
		m_save.setEnabled( sn != "" );
	}
	
	@Override // GameListDialog
	protected void onButton( String cmd )
	{
		String sn = getSelectedName();
		if( cmd == CMD_SAVE_AS )
		{
			while( true )
			{
				// Prompt for new name
				String tn = JOptionPane.showInputDialog( this,
						"Save game as...", sn );
				tn = tn.trim();
				
				// Notify name already in use (option to overwrite).
				if( doesNameExist( tn ) )
				{
					int opt = JOptionPane.showConfirmDialog( this,
							"Game \"" + tn + "\" already exists.  Would you like to overwrite it?",
							"Warning", JOptionPane.YES_NO_CANCEL_OPTION );

					if( opt == 0 ) // Yes
					{
						// No need to do anything here.  Save will delete it.
					}
					else if( opt == 1 ) // No
					{
						continue;
					}
					else
					{
						break;
					}
				}
				
				FenetreTest.instance.saveTo( tn );
				updateList();
				
				// Prompt confirming save
				JOptionPane.showMessageDialog( this,
						"\"" + tn + "\" successfully saved." );
				dispose();
				break;
			}
		}
		else if( cmd == CMD_SAVE && sn != "" )
		{
			// Prompt about losing data
			int opt = JOptionPane.showConfirmDialog( this,
					"Overwrite existing data in \"" + sn + "\"?",
					"Warning", JOptionPane.OK_CANCEL_OPTION );
					
			if( opt == 0 ) // Ok
			{		
				FenetreTest.instance.saveTo( sn );
				
				// Prompt confirming save
				JOptionPane.showMessageDialog( this,
						"\"" + sn + "\" successfully saved." );
				dispose();
			}
		}
	}
	
	@Override // GameListDialog
	protected void initializeComponents()
	{
		super.initializeComponents();
		
		m_saveas = addButton( "Save as...", CMD_SAVE_AS );
		m_save = addButton( "Save", CMD_SAVE );
	}
}
