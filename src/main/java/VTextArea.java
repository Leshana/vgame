///////////////////////////////////////////////////////////////////////////////////////////////////
//
// VTextArea.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class VTextArea extends JTextArea
{
	public VTextArea()
	{
		super();
		
		//setMinimumSize( new Dimension( 16, 16 ) );
		//setMaximumSize( new Dimension( 16, 16 ) );
		//setPreferredSize( new Dimension( 16, 16 ) );
		
		setAlignmentX( Component.LEFT_ALIGNMENT );
		setLineWrap( true );
		setWrapStyleWord( true );
		setEditable(false);
		setFocusable(false);
	}
	
	public VTextArea( Component parent )
	{
		this();
		
		setFont( parent.getFont().deriveFont( Font.BOLD ) );
		setBackground( parent.getBackground() );
	}
};
