///////////////////////////////////////////////////////////////////////////////////////////////////
//
// IconBar.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class IconBar extends JComponent
{
	private Icon m_icon;
	private String m_text;
	private int m_max;
	private int m_value;
	private double m_curve;
	
	private Color[] m_fore;
	private Color[] m_back;
	private Icon[] m_icons;
	
	public static final int ICON_PAD = 4;
	public static final int BAR_HEIGHT = 16;

	///////////////////////////////////////////////////////////////////////////////////////////////
	public IconBar()
	{
		super();
		m_icon = null;
		m_max = 1;
		m_value = 0;
		m_text = "";
		m_curve = 0.0;
		
		m_fore = null;
		m_back = null;
		m_icons = null;
	}
	
	public IconBar( ImageIcon icon )
	{
		this();
		m_icon = icon;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void setIcon( Icon icon )
	{
		m_icon = icon;
		repaint();
	}
	
	public Icon getIcon()
	{
		return m_icon;
	}
	
	public void setMaximum( int max )
	{
		m_max = max;
		m_value = Math.min( m_value, m_max );
		repaint();
	}
	
	public int getMaximum()
	{
		return m_max;
	}
	
	public void setValue( int value )
	{
		m_value = Math.min( value, m_max );
		repaint();
	}
	
	public int getValue()
	{
		return m_value;
	}
	
	public void setText( String str )
	{
		m_text = str;

		repaint();
	}
	
	public String getText()
	{
		return m_text;
	}
	
	public void setBlendCurve( double c )
	{
		m_curve = c;
		repaint();
	}
	
	public double getBlendCurve()
	{
		return m_curve;
	}
	
	public void setIconList( Icon[] icons )
	{
		m_icons = icons;
		repaint();
	}

	public void setForegroundList( Color[] fore )
	{
		m_fore = fore;
		repaint();
	}
	
	public void setBackgroundList( Color[] back )
	{
		m_back = back;
		repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // JComponent
	public Dimension getPreferredSize()
	{
		return new Dimension( 0, BAR_HEIGHT );
	}
	
	@Override // JComponent
	public Dimension getMinimumSize()
	{
		return new Dimension( 0, BAR_HEIGHT );
	}
	
	@Override // JComponent
	public Dimension getMaximumSize()
	{
		return new Dimension( 5000, BAR_HEIGHT );
	}

	@Override // JComponent
	public void paintComponent( Graphics g )
	{
		Graphics2D g2d = (Graphics2D)g;
		Font fnt = getFont().deriveFont( Font.BOLD );
		double v = 0.0f;
		Color fore = getForeground();
		Color back = getBackground();
		Icon icon = m_icon;
		
		if( m_max > 0 && m_value > 0 )
		{
			v = Math.max( Math.min( (double)m_value / (double)m_max, 1.0 ), 0.0 );
		}
		
		if( m_fore != null )
		{
			fore = Util.mixColors( m_fore, v, m_curve );
		}

		if( m_back != null )
		{
			back = Util.mixColors( m_back, v, m_curve );
		}
		
		if( m_icons != null && m_icons.length > 0 )
		{
			icon = m_icons[Util.lerpIndex( v, m_icons.length )];
		}
		
		int l = 0;
		int t = 0;
		int w = getWidth() - 1;
		int h = getHeight() - 1;
		
		// Draw the icon if one is present.
		if( icon != null )
		{
			int offs = (getHeight() - icon.getIconHeight()) / 2;
			icon.paintIcon( this, g, l, offs );

			l += icon.getIconWidth() + ICON_PAD;
			w -= icon.getIconWidth() + ICON_PAD;
		}

		// Paint the background for the bar.
		g.setColor( back );
		g.fillRoundRect( l, t, w, h, h, h );
		
		// Figure out how big the string will be so we can center it vertically.
		FontRenderContext frc = g2d.getFontRenderContext();
		LineMetrics metrics = fnt.getLineMetrics( m_text, frc );

		int strX = l + 8;
		int strY = (int)((getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
		
		// Paint the bar's string in the foreground color (which should contrast with the bar's
		//  background color.
		g.setFont( fnt );
		g.setColor( fore );
		g.drawString( m_text, strX, strY );
		
		if( v > 0.0 )
		{
			Shape clip = g.getClip();

			// Draw a 'fill' bar based on value relative to max.  It uses a neat-o gradient effect.
			int vw = (int)((double)w * v);
			g.setClip( g.getClipBounds().intersection( new Rectangle( 0, 0, l + vw, getHeight() ) ) );
			g2d.setPaint( new GradientPaint(0, h / 4, fore, 0, h * 2, back, true) );
			g.fillRoundRect( l, t, w, h, h, h );
			
			// Re-draw the string using a darker color so it will show up on the foreground colored
			//  bar.
			g.setColor( back );
			g.drawString( m_text, strX, strY );

			// Reset the clipping size
			g.setClip( clip );
		}
		
		// Draw an outline around the bar
		g.setColor( Color.BLACK );
		g.drawRoundRect( l, t, w, h, h, h );
	}
}