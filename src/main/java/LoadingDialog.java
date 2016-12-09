///////////////////////////////////////////////////////////////////////////////////////////////////
//
// LoadingDialog.java
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
class LoadingDialog extends GameListDialog
{
	protected JButton m_load;
	protected JButton m_new;
	
	public static final String CMD_NEW = "NEW";
	public static final String CMD_LOAD = "LOAD";

	///////////////////////////////////////////////////////////////////////////////////////////////
	public LoadingDialog()
	{
		super( "Load Game" );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // GameListDialog
	protected void updateButtons()
	{
		String sn = getSelectedName();
		m_load.setEnabled( sn != "" );
	}
	
	@Override // GameListDialog
	protected void onButton( String cmd )
	{
		String sn = getSelectedName();
		if( cmd == CMD_NEW )
		{
			// Prompt about losing unsaved progress
			// Prompt for new player name (?)
			dispose();
		}
		else if( cmd == CMD_LOAD && sn != "" )
		{
			// Prompt about losing unsaved progress
			VGame.instance.loadFrom( sn );
			dispose();
		}
	}
	
	@Override // GameListDialog
	protected void initializeComponents()
	{
		super.initializeComponents();
		
		//m_new = addButton( "New...", CMD_NEW );
		m_load = addButton( "Load", CMD_LOAD );
	}
}
