/*
 * Livre.java
 *
 * Created on 4 février 2006, 12:47
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
/**
 *
 * @author Krishna3ca
 */

import java.io.*;
import java.awt.*;

class LivreData implements Serializable
{
	public int BookID; // Key field (must come first)
	
	public String Nom;
	public String Description;

	public String BookIcon;

	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int Price;
	public int SpellID;
	public int TransformID;
}

public class Livre extends ObjInventaire
{
	public int spellID;
	
	/** Creates a new instance of Livre */
	public Livre(int livreID, PlayerStat m)
	{
		super(m, livreID);
		type=Util.ITEM_BOOK;
		
		loadFromData( DataUtil.getLivreData( livreID ) );
	}
	
	public void loadFromData( LivreData livreData )
	{
		setName( livreData.Nom );
		setImage( livreData.BookIcon );
		
		niveauR = livreData.NiveauR;
		strengthR = livreData.StrengthR;
		intelligenceR = livreData.IntelligenceR;
		endurenceR = livreData.EndurenceR;
		charismaR = livreData.CharismaR;
		dexterityR = livreData.DexterityR;
		price = livreData.Price;
		spellID = livreData.SpellID;
		description = livreData.Description;
		transformID = livreData.TransformID;
	}
	
	@Override // ObjetInventaire
	public void use()
	{
		super.use();
		if(testRequirements())
		{
			FenetreTest.instance.spellbook.addElement(new Spell(spellID));
			dispose();
		}
	}
	@Override // ObjetInventaire
	public String getUseString()
	{
		return "Read";
	}
}
