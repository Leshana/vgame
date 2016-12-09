/*
 * ImageLibrairies.java
 *
 * Created on 28 septembre 2005, 12:12
 */
import java.awt.*;
import java.lang.ref.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author etudiant
 */
public final class ImageLibrairies
{
	private static Hashtable<String,SoftReference<Image>> imageTable = new Hashtable<>();
	
	public static Image getImage( String name )
	{
		SoftReference<Image> tableEntry = imageTable.get( name );
		
		if (tableEntry == null || tableEntry.get() == null)
		{
			ImageIcon imageIcon = new ImageIcon( name );
			Image image = imageIcon.getImage();
			
			imageTable.put( name, new SoftReference<Image>( image ) );
			
			return image;
		}
		else
		{
			return tableEntry.get();
		}
	}
}
