///////////////////////////////////////////////////////////////////////////////////////////////////
//
// VGame.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class VGame
{
	public static FenetreTest instance;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public static void main( String[] args )
	{
                // First things first, try and load the game data!
                DataUtil.loadStaticGameData();
            
		try
		{
			// Sets the look and feel to whatever is appropriate for the user's OS... but will
			//  screw up some of the layout in the process - controls have different defaults as
			//  far as sizes/fonts/etc go.  Needs some more looking before it goes in.
			// If it goes in...
			//UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

			Util.initialize();
	
			instance = new FenetreTest( "VGame" );
			instance.start();
		}
		catch( Exception e )
		{
			Util.error( e );
		}
	}
}
