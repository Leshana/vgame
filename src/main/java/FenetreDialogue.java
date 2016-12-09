///////////////////////////////////////////////////////////////////////////////////////////////////
//
// FenetreDialogue.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class FenetreDialogue extends JDialog implements MouseListener, MouseMotionListener
{
	FenetreTest mere;
	private ObjetGraphique src, tgt;
	
	private Image nIcon;
	private JEditorPane textPane;

	private Element lastHilight = null;
	
	public static final Color LINK_FORE = Color.BLUE;
	public static final Color LINK_BACK = Color.WHITE;
	
	public static final Color LINK_HOVER_FORE = Color.RED;
	public static final Color LINK_HOVER_BACK = new Color( 0xdddddd );
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public FenetreDialogue(String oIcon, String message)
	{
		super( VGame.instance );
		setAlwaysOnTop(true);

		mere = VGame.instance;
		src = mere.src;
		tgt = mere.tgt;

		Rectangle b = VGame.instance.getBounds();
		
		int w = 420;
		int h = (int)b.getHeight();

		b.setLocation( (int)(b.getX() + b.getWidth() - w),
					   (int)(b.getY()) );
		b.setSize( w, h );
		
		setBounds(b);
		setDefaultLookAndFeelDecorated( false );
		setTitle("Dialogue");

		nIcon = ImageLibrairies.getImage(oIcon);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		initializeComponents();
		initializeText( message );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void addChoix( String m, int ev )
	{
		String text = textPane.getText();
		
		int at = text.lastIndexOf( "</div>" );
		
		m = "<br><a href=\"" + ev + "\">" + Util.sanitizeHTML( m ) + "</a>";
		
		text = text.substring( 0, at ) + m + text.substring( at );
		
		textPane.setText( text );
		textPane.setCaretPosition( 0 );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	private void hilightLink( Element e )
	{
		if( e != lastHilight )
		{
			unhilightLink();
			lastHilight = e;
            changeColor( e, LINK_HOVER_FORE, LINK_HOVER_BACK );
        }
	}
	
	private void unhilightLink()
	{
        changeColor( lastHilight, LINK_FORE, LINK_BACK );
        lastHilight = null;
	}

    private void changeColor( Element e, Color fore, Color back )
    {
        if( e != null )
        {
            HTMLDocument doc = (HTMLDocument) textPane.getDocument();
            StyleContext ss = doc.getStyleSheet();

            Style style = ss.addStyle( "HighlightedHyperlink", null );
            style.addAttribute( StyleConstants.Foreground, fore );
            style.addAttribute( StyleConstants.Background, back );

            int start = e.getStartOffset();
            int end = e.getEndOffset();
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }


	///////////////////////////////////////////////////////////////////////////////////////////////
	private void initializeComponents()
	{
		// Image
		ImagePanel imgPanel=new ImagePanel( nIcon );
		imgPanel.setMinimumSize(new Dimension(400,400));
		imgPanel.setPreferredSize(new Dimension(400,400));
		
		// Text area
		JPanel textPanel = new JPanel();
		textPanel.setLayout( new BoxLayout( textPanel, BoxLayout.Y_AXIS ) );
		{
			textPane=new JEditorPane();
			textPane.addMouseMotionListener( this );
			textPane.addMouseListener( this );
			textPane.setContentType("text/html");
			textPane.setEditable(false);

			JScrollPane scroller = new JScrollPane( textPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

			textPanel.add( scroller );
		}

		// And finally the actual page layout.
		Container conteneur = getContentPane();
		conteneur.setLayout( new GridBagLayout() );
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2 );

		// Image panel - Top
		c.weightx = 1.0; c.weighty = 0.0;
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( imgPanel, c );

		// Text panel - Bottom
		c.weightx = 1.0; c.weighty = 1.0;
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = 1; c.gridheight = 1;
		conteneur.add( textPanel, c );
	}
	
	private void initializeText( String m )
	{
		String text = "<html>";
		text += "<head>";
		text += "<style type=\"text/css\">";
		text += "<!--";
		text += "#da {";
		text += "padding: 5px;";
		text += "font-family: " + textPane.getFont().getFamily() + ";";
		text += "font-size: " + (textPane.getFont().getSize()) + "pt;";
		text += "}";
		text += "a {";
		text += "font-size: " + (textPane.getFont().getSize() + 4) + "pt;";
		text += "}";
		text += "-->";
		text += "</style>";
		text += "</head>";
		text += "<body>";
		text += "<div id=\"da\">";
		text += Util.sanitizeHTML( m ) + "<br>";
		text += "</div>";
		text += "</body>";
		text += "</html>";
		
		textPane.setText( text );
		textPane.setCaretPosition( 0 );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void friendlyClicked()
	{
		int pos = textPane.viewToModel( new Point( (int)downAt.getX(), (int)downAt.getY() ) );

		if( pos >= 0 )
		{
			HTMLDocument hdoc = (HTMLDocument)textPane.getDocument();
			Element elem = hdoc.getCharacterElement( pos );

			if( elem != null )
			{
				AttributeSet a = elem.getAttributes();
				AttributeSet anchor = (AttributeSet)a.getAttribute( HTML.Tag.A );

				if( anchor != null )
				{
					int eid = Integer.parseInt( (String)(anchor.getAttribute( HTML.Attribute.HREF )) );
					
					dispose();
					mere.dialogue = null;

					if( eid != 0 )
					{
						mere.src = src;
						mere.tgt = tgt;
						mere.executeScript( eid );
					}

					mere.repaint();
				}
			}
		}
	}
	
	private Point downAt;

	@Override // MouseListener
    public void mousePressed( MouseEvent e )
    {
		downAt = new Point( e.getX(), e.getY() );
    }
    
	@Override // MouseListener
    public void mouseReleased( MouseEvent e )
    {
		if( downAt != null
			&& Math.abs( e.getX() - downAt.getX() ) < 5
			&& Math.abs( e.getY() - downAt.getY() ) < 5 )
		{
			friendlyClicked();
			downAt = null;
		}
    }

	@Override // MouseListener
    public void mouseClicked( MouseEvent e ) {}
	@Override // MouseListener
    public void mouseEntered( MouseEvent e ) {}
	@Override // MouseListener
    public void mouseExited( MouseEvent e )
    {
		downAt = null;
		unhilightLink();
    }

	@Override // MouseMotionListener
	public void mouseDragged( MouseEvent e ) {}
	@Override // MouseMotionListener
	public void mouseMoved( MouseEvent e )
	{
		int pos = textPane.viewToModel( new Point( e.getX(), e.getY() ) );

		if( pos >= 0 )
		{
			HTMLDocument hdoc = (HTMLDocument)textPane.getDocument();
			Element elem = hdoc.getCharacterElement( pos );

			if( elem != null )
			{
				AttributeSet a = elem.getAttributes();
				AttributeSet anchor = (AttributeSet)a.getAttribute( HTML.Tag.A );

				if( anchor != null )
				{
					hilightLink( elem );
				}
				else
				{
					downAt = null;
					unhilightLink();
				}
			}
		}
	}
}
