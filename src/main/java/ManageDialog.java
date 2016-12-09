///////////////////////////////////////////////////////////////////////////////////////////////////
//
// ManageDialog.java
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
class ManageDialog extends GameListDialog
{
	protected JButton m_rename;
	protected JButton m_delete;
	
	public static final String CMD_RENAME = "RENAME";
	public static final String CMD_DELETE = "DELETE";
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public ManageDialog()
	{
		super( "Manage Save Data" );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // GameListDialog
	protected void updateButtons()
	{
		String sn = getSelectedName();
		m_delete.setEnabled( sn != "" );
		m_rename.setEnabled( sn != "" );
	}
	
	@Override // GameListDialog
	protected void onButton( String cmd )
	{
		String sn = getSelectedName();
		if( cmd == CMD_RENAME && sn != "" )
		{
			while( true )
			{
				// Prompt for new name
				String tn = JOptionPane.showInputDialog( this,
						"Rename \"" + sn + "\" to...", sn );
				tn = tn.trim();
			
				// Notify save name is unchanged
				if( sn.equalsIgnoreCase( tn ) )
				{
					int opt = JOptionPane.showConfirmDialog( this,
							"The new name must be different from the old name.",
							"Error", JOptionPane.OK_CANCEL_OPTION );
					
					if( opt == 0 ) // Ok
					{
						continue;
					}
					else
					{
						break;
					}
				}
				
				// Notify name already in use (option to overwrite).
				if( doesNameExist( tn ) )
				{
					int opt = JOptionPane.showConfirmDialog( this,
							"Game \"" + tn + "\" already exists.  Would you like to overwrite it?",
							"Warning", JOptionPane.YES_NO_CANCEL_OPTION );

					if( opt == 0 ) // Yes
					{
						VGame.instance.deleteSaveGame( tn );
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
				
				VGame.instance.renameSaveGame( tn, sn );
				updateList();
				
				// Prompt confirming rename
				JOptionPane.showMessageDialog( this,
						"\"" + sn + "\" successfully renamed to \"" + tn + "\"." );
				break;
			}
		}
		else if( cmd == CMD_DELETE && sn != "" )
		{
			// Prompt about losing data
			int opt = JOptionPane.showConfirmDialog( this,
					"Really delete \"" + sn + "\"?",
					"Warning", JOptionPane.OK_CANCEL_OPTION );
					
			if( opt == 0 ) // Ok
			{		
				VGame.instance.deleteSaveGame( sn );
				updateList();
				
				// Prompt confirming delete
				JOptionPane.showMessageDialog( this,
						"\"" + sn + "\" has been deleted." );
			}
		}
	}
	
	@Override // GameListDialog
	protected void initializeComponents()
	{
		super.initializeComponents();
		
		m_rename = addButton( "Rename...", CMD_RENAME );
		m_delete = addButton( "Delete", CMD_DELETE );
	}
}
