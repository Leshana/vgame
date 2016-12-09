///////////////////////////////////////////////////////////////////////////////////////////////////
//
// GameObject.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import javax.swing.*;
import java.util.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
abstract class GameObject implements Comparable
{
	private String m_name = "Object";
	private Image m_img = null;

	private ViewDialog m_view = null;

	private JPanel m_viewVitals = null;
	private JPanel m_viewDetails = null;

	private boolean m_isNew = true;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public GameObject()
	{
	}
	
	public GameObject( String name, String image )
	{
		m_img = ImageLibrairies.getImage( image );
		
		m_name = name;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public abstract void initializeView( JPanel vitals, JPanel details );
	public abstract void cleanupView();
	public abstract void updateView();
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setName( String name )
	{
		m_name = name;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public void setImage( Image img )
	{
		m_img = img;
	}
	
	public void setImage( String img )
	{
		m_img = ImageLibrairies.getImage( img );
	}
	
	public Image getImage()
	{
		return m_img;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
    public void setNew( boolean n )
    {
		m_isNew = n;
	}

    public boolean isNew()
    {
		return m_isNew;
    }

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void view()
	{
		if( m_view != null )
		{
			m_view.toFront();
			m_view.repaint();
		}
		else
		{
			m_view = new ViewDialog( this );
		}
	}

	public void setView( ViewDialog view )
	{
		m_view = view;
	}
    
    public ViewDialog getView()
    {
		return m_view;
    }
	
	public void dispose()
	{
		if( m_view != null )
		{
			m_view.dispose();
		}
	}
}
