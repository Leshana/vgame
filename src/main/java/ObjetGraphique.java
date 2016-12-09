/*
 * ObjetGraphique.java
 *
 * Created on 30 septembre 2005, 13:30
 */
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.text.*;
/**
 *
 * @author etudiant
 */
class ObjetGraphiqueData implements Serializable
{
	public int CaracterID;
	
	public int TailleX;
	public int TailleY;
	public String ImageNom1;
	public int NombreImage1;
	public int Delai1;
	public String ImageNom2;
	public int NombreImage2;
	public int Delai2;
	public String ImageNom3;
	public int NombreImage3;
	public int Delai3;
	public String ImageNom4;
	public int NombreImage4;
	public int Delai4;
	public String ImageNom5;
	public int NombreImage5;
	public int Delai5;
	public String ImageNom6;
	public int NombreImage6;
	public int Delai6;
	public String ImageNom7;
	public int NombreImage7;
	public int Delai7;
	public String ImageNom8;
	public int NombreImage8;
	public int Delai8;
	public String ImageNom9;
	public int NombreImage9;
	public int Delai9;
	public String ImageNom10;
	public int NombreImage10;
	public int Delai10;
}

public class ObjetGraphique extends GameSprite {
	public int tailleX;
	public int tailleY;

	public int side;
	public int eventID;
	public int deathEventID;
	public int deplacement;
	public int caracterID;
	public int enable;
	public int statID;
	public int varX;
	public int varY;
	public int frame;
	public int type;
	public int fx;
	public int fy;
	public int modeAffichage;
	public StatChar statistique;
	public int carID;
	public int attackID;
	
	private static int compteur=0;
	
	public static final int PLAYER=0;
	public static final int MONSTER=1;
	public static final int NPC=2;
	public static final int SPELL=3;
	
	class ImageSet
	{
		public int delai;
		public String nom;
		public Image[] pos;

		public ImageSet( String nom, int nombre, int delai )
		{
			this.delai = delai;
			this.nom = nom;
			this.pos = new Image[nombre];

			reload();
		}

		public int size()
		{
			return pos.length;
		}

		public Image image( int i )
		{
			if( i >= pos.length )
			{
				return pos[0];
			}

			return pos[i];
		}

		public void reload()
		{
			NumberFormat nf = NumberFormat.getInstance(); 
			nf.setMinimumIntegerDigits(3);

			int ind=nom.indexOf("001");
			String chaine=nom.substring(0,ind);
			String fin=nom.substring(ind+3,nom.length());

			for( int i=0; i<pos.length; i++ )
			{
				pos[i] = ImageLibrairies.getImage( chaine + nf.format(i+1) + fin );
			}
		}
	  
		public void changerChaine(String arme, String armure)
		{
			int ind=nom.indexOf("001");
			String chaine=nom.substring(0,nom.indexOf("_"));
			String fin=nom.substring(ind+3,nom.length());

			nom=chaine+armure+arme+"001"+fin;

			reload();
		}
	}

	public ImageSet[] images = new ImageSet[10];

	public ObjetGraphique(
		int pX, int pY,
		int sideC, int event, int modeDep, int car, int statID, int t,
		int death, int carID, int attackID )
	{
		deathEventID=death;
		modeAffichage=1;
		type=t;
		frame=0;
		varX=0;
		varY=0;
		side=sideC;
		eventID=event;
		deplacement=modeDep;
		enable=1;
		this.statID=statID;
		this.carID=carID;
		this.attackID=attackID;
		//mere=FenetreTest.instance;

		setPosition( pX, pY );
		setVisible(true);
		
		readImages( carID );

		if(type==FenetreTest.PLAYER)
		{
			statistique=new PlayerStat(statID,this, side);
		}
		else
		{
			statistique=new StatChar(compteur,statID,side,this);
		}

		compteur++;
		if(compteur>=Integer.MAX_VALUE)
			compteur=1;
		
		FenetreTest.instance.addTarget(statistique);
		setPanel( FenetreTest.instance.gamePanel );

	}

	public ObjetGraphique( SaveCharacter saveChar )
	{
		carID = saveChar.CarID;
		eventID = saveChar.EventID;
		deathEventID = saveChar.DeathEventID;
		enable = saveChar.Enable;
		deplacement = saveChar.Deplacement;
		modeAffichage = saveChar.ModeAffichage;
		attackID = saveChar.AttackID;
		//caracterID = ??
		type = saveChar.Type;
		
		setPosition( saveChar.PositionX, saveChar.PositionY );
		setVisible( saveChar.Visible );
		
		readImages( carID );
		
		if (type == PLAYER)
		{
			statistique = new PlayerStat( saveChar, this );
		}
		else
		{
			statistique = new StatChar( saveChar, this );
		}
		
		FenetreTest.instance.addTarget(statistique);
		setPanel( FenetreTest.instance.gamePanel );
		
		repaint();
	}
	
	public void readImages( int car )
	{
		ObjetGraphiqueData data = DataUtil.getObjetGraphiqueData( car );
		
		if (data != null)
		{
			tailleX=data.TailleX;
			tailleY=data.TailleY;
			
			images[0] = new ImageSet( data.ImageNom1, data.NombreImage1, data.Delai1 );
			images[1] = new ImageSet( data.ImageNom2, data.NombreImage2, data.Delai2 );
			images[2] = new ImageSet( data.ImageNom3, data.NombreImage3, data.Delai3 );
			images[3] = new ImageSet( data.ImageNom4, data.NombreImage4, data.Delai4 );
			images[4] = new ImageSet( data.ImageNom5, data.NombreImage5, data.Delai5 );
			images[5] = new ImageSet( data.ImageNom6, data.NombreImage6, data.Delai6 );
			images[6] = new ImageSet( data.ImageNom7, data.NombreImage7, data.Delai7 );
			images[7] = new ImageSet( data.ImageNom8, data.NombreImage8, data.Delai8 );
			images[8] = new ImageSet( data.ImageNom9, data.NombreImage9, data.Delai9 );
			images[9] = new ImageSet( data.ImageNom10, data.NombreImage10, data.Delai10 );
		}

		repaint();
	}
	
	/*
	public void loadStatistique(int saveID, int playerID)
	{
		if(caracterID!=playerID)
		{
			statistique=new StatChar(saveID,caracterID,this);
		}
		else
		{
			loadStatistiquePlayer(saveID);
		}
		
		FenetreTest.instance.addTarget(statistique);
		setPanel( FenetreTest.instance.gamePanel );
		
		repaint();
	}
	
	public void loadStatistiquePlayer(int saveID)
	{
		PlayerStat p=new PlayerStat(saveID,caracterID,this);
		statistique=p;
		
		BaseDonnee save_db = BaseDonnee.open_save();
		
		String req;
		ResultSet res;
		
		req = "SELECT *";
		req += " FROM Evenement";
		req += " WHERE SaveID="+saveID;
		
		res = save_db.calculRequete(req);

		try
		{
			if( res.first() )
			{
				do
				{   
					int temps = res.getInt("temps");
					int jour = res.getInt("jour");
					int mois = res.getInt("mois");
					int annee = res.getInt("annee");
					
					p.listeEvent.add(new Evenement(
						new Clock( annee, mois, jour, 0, temps ),
						res.getInt("compteur"),
						res.getString("nom") ));
				} while (res.next());
			}
		}
		catch(Exception e)
		{
			Util.error( e );
		}
		
		save_db.finRequete(res);
			
		req = "SELECT *";
		req += " FROM Inventaire";
		req += " WHERE SaveID="+saveID;

		res = save_db.calculRequete(req);

		try
		{
			if( res.first() )
			{
				for( ; !res.isAfterLast(); res.next() )
				{
					int objetID=res.getInt("ObjetID");
					int bonusID=res.getInt("BonusID");
					int ty=res.getInt("Type");
					int equipped=res.getInt("Equipped");
					int valeur=res.getInt("Valeur");

					ObjInventaire obj = ObjInventaire.load(equipped, ty, objetID, bonusID, valeur, p);
					obj.setNew( false );

					p.addObjInventaire( obj, false, false );
				}
			}
		}
		catch(Exception e)
		{
			Util.error( e );
		}
		
		save_db.finRequete(res);
		
		save_db.close();
	}
	*/
	
	public void reload()
	{
		for( int i = 0; i < images.length; ++i )
		{
			images[i].reload();
		}

		if(statistique!=null)
			statistique.reload();
		
		repaint();
	}
	
	@Override // GameSprite
	public void remove()
	{
		super.remove();
		statistique.dispose();
	}
	
	public SaveCharacter save()
	{
		SaveCharacter ret = statistique.save();
		
		ret.CarID = carID;
		ret.PositionX = getX();
		ret.PositionY = getY();
		ret.EventID = eventID;
		ret.DeathEventID = deathEventID;
		ret.Visible = isVisible();
		ret.Enable = enable;
		ret.Deplacement = deplacement;
		ret.ModeAffichage = modeAffichage;
		ret.AttackID = attackID;
		ret.Type = type;
		
		return ret;
	}

	public void changerChaine(String arme, String armure)
	{
		for( int i = 0; i < images.length; ++i )
		{
			images[i].changerChaine( arme, armure );
		}
		
		repaint();
	}
	
	public void changerChaine(String armure)
	{
		for( int i = 0; i < images.length; ++i )
		{
			images[i].changerChaine( "", armure );
		}
		
		repaint();
	}

	public void transform(int nombre)
	{
		carID=nombre;
		polymorph(nombre);
	}
	
	public void polymorph(int nombre)
	{
		if(nombre<1)
			nombre=carID;

		readImages( nombre );

		repaint();
	}
	
	@Override // GameSprite
	public void paint( Graphics g, int px, int py, ImageObserver obs )
	{
		double size=1;
		switch(statistique.size)
		{
			case 1: size=0.5; break;
			case 2: size=1.25; break;
		}
		
		Image img = null;
		int w = (int)(tailleX * size);
		int h = (int)(tailleY * size);
		
		img = images[modeAffichage-1].image( frame );
		
		int x = px - w / 2;
		int y = py - h;
		
		if( img != null )
		{
		  g.drawImage(img,x,y,w,h,obs);
		}

		if(type==FenetreTest.PLAYER)
		{
			PlayerStat ti=(PlayerStat)statistique;
		   
			if(!ti.arme.armeImage.equalsIgnoreCase("_f"))
			{
				img = null;
				switch(modeAffichage)
				{
				case 1: img = ti.arme.imageS; break;
				case 2: img = ti.arme.imageA; break;
				case 3: img = ti.arme.imageS; break;
				case 4: img = ti.arme.imageA; break;
				case 5: img = ti.arme.imageS; break;
				case 6: img = ti.arme.imageSw; break;
				}
				
				if( img != null )
				{
				  g.drawImage(img,x,y,w,h,obs);
				}
			}
		}
	}
}
