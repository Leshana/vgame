///////////////////////////////////////////////////////////////////////////////////////////////////
//
// ImagePanel.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class ImagePanel extends JPanel
{
	Image m_img = null;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public ImagePanel()
	{
		super();
		
		setBackground( Color.DARK_GRAY );
		setBorder( BorderFactory.createLoweredBevelBorder() );
	}
	
	public ImagePanel( Image img )
	{
		this();
		
		setImage( img );
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setImage( Image img )
	{
		m_img = img;
		repaint();
	}
	
	public Image getImage()
	{
		return m_img;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // Component
	public void paintComponent( Graphics g )
	{
		super.paintComponent(g);
		
		if( m_img == null )
		{
			return;
		}
		
		Rectangle r = getBounds();
		
		int iw = m_img.getWidth( null );
		int ih = m_img.getHeight( null );
		
		int cx = (int)r.getWidth() / 2;
		int cy = (int)r.getHeight() / 2;
		
		int hw, hh;
		
		if( r.getWidth() / r.getHeight() > (double)(iw) / (double)(ih) )
		{
			// r is 'wider' than the image, so bound on height.
			hh = cy;
			hw = (iw * hh) / ih;
		}
		else
		{
			// r is 'taller' than the image, so bound on width.
			hw = cx;
			hh = (ih * hw) / iw;
		}

		g.drawImage(m_img,cx-hw,cy-hh,hw+hw,hh+hh,this);
	}
}
