///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Usable.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class UsableData implements Serializable
{
	public int UsableID; // Key field (must come first)
		
	public String Nom;
	public String Description;
	
	public String UsableIcon;

	public int NiveauR;
	public int StrengthR;
	public int IntelligenceR;
	public int EndurenceR;
	public int CharismaR;
	public int DexterityR;
	public int Price;
	public int TransformID;

	public int SpellID;
	public int Use;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Usable extends ObjInventaire
{
	public int spellID;
	public int nbUse;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Usable(int usableID, int val, PlayerStat m)
	{
		this(usableID,m);
		nbUse=val;
	}
	
	public Usable(int usableID, PlayerStat m)
	{
		super(m, usableID);
		loadFromData( DataUtil.getUsableData( usableID ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public String getUsesString()
	{
		return nbUse + " Uses Remaining";
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( UsableData usableData )
	{
		type = Util.ITEM_USABLE;
		
		setName( usableData.Nom );
		setImage( usableData.UsableIcon );

		niveauR = usableData.NiveauR;
		strengthR = usableData.StrengthR;
		intelligenceR = usableData.IntelligenceR;
		endurenceR = usableData.EndurenceR;
		charismaR = usableData.CharismaR;
		dexterityR = usableData.DexterityR;
		price = usableData.Price;
		description = usableData.Description;
		transformID = usableData.TransformID;
		
		spellID = usableData.SpellID;
		nbUse = usableData.Use;
		
		Spell sort = new Spell( spellID );
		harmful = sort.harmful;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjInventaire
	public void initializeView( JPanel vitals, JPanel details )
	{
		super.initializeView( vitals, details );

		if( nbUse > 1 )
		{
			JLabel usesLbl = new JLabel();
			usesLbl.setText( getUsesString() );
			usesLbl.setIcon( Util.getIcon("item_generic") );
			usesLbl.setAlignmentX( Component.LEFT_ALIGNMENT );
			vitals.add( usesLbl );
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjInventaire
	public boolean canStack( ObjInventaire ob )
	{
		return ob.type == type && ob.objetID == objetID;
	}
	
	@Override // ObjInventaire
	public int getValeur()
	{
		return nbUse;
	}
	
	@Override // ObjInventaire
	public boolean isUsable()
	{
		return spellID >= 0;
	}

	@Override // ObjInventaire
	public void use(){
		super.use();
		if(testRequirements()){
			Spell sort=new Spell(spellID);
			Random hasard=new Random();
			ObjetGraphique temp=mere.mere;
			int valeur2;
			switch(sort.type){
				case 0:
				case 1:
					{
						StatChar cible = FenetreTest.instance.getSelectedTarget();
						if(cible != null && cible.maitre==null)
						{
							int time=hasard.nextInt(sort.timeMax-sort.timeMin)+sort.timeMin+sort.timeBonus+1;
							time+=sort.timeBonusInt*temp.statistique.intelligence/100;
							boolean test=true;
							AudioLibrairie.playClip(sort.isonCast);
							int tableau[]=FenetreTest.instance.cibleBloquee(cible.mere.getX(), cible.mere.getY(), temp.getX(),temp.getY());
							int poX;
							int poY;
							if (tableau==null){
								poX=cible.mere.getX();
								poY=cible.mere.getY();
							}
							else{
								poX=tableau[0];
								poY=tableau[1];
							}
							if(sort.type==1){
								FenetreTest.instance.ajouterProjectile(sort.projectileID,temp.getX(), temp.getY(), poX, poY,1,temp.tailleY/2, cible.mere.tailleY/2);

								FenetreTest.instance.animationProjectileMove();
								FenetreTest.instance.enleverProjectile();
							}

							AudioLibrairie.playClip(sort.isonCible);
							
							FenetreTest.instance.ajouterProjectile(sort.projectileID,poX, poY, poX, poY, 2, temp.tailleY/2, cible.mere.tailleY/2);

							FenetreTest.instance.animationProjectileStand();
							FenetreTest.instance.enleverProjectile();
							if(tableau!=null){
								break;
							}                
							if(sort.harmful==0){
								valeur2=hasard.nextInt(100)+1;
								switch(sort.typeResist){
									case 0:
										break;
									case 1:
										if(valeur2<cible.resistance[Util.DAMAGE_CURSE])
											test=false;
										break;
									case 2:
										if(valeur2<cible.resistance[Util.DAMAGE_PARALYSIS])
											 test=false;
										break;
									case 3:
										if(valeur2<cible.resistance[Util.DAMAGE_SLEEP])
											 test=false;
										break;
									case 4:
										if(valeur2<cible.resistance[Util.DAMAGE_CHARM])
											 test=false;
										break;
								 }
								 if(sort.save<=cible.getSave()){
									test=false;
								 }
							}
							if(test){
												
								valeur2=hasard.nextInt(sort.dommageMax-sort.dommageMin)+sort.dommageMin+sort.dommageBonus+1;
								valeur2+=sort.dommageBonusInt*temp.statistique.intelligence/100;
								if(sort.harmful==0){
									if(cible.appliquerDommage(valeur2,sort.dommageType))
										cible.mort(temp.statistique);
								}
								if(sort.vore>0){
									valeur2=hasard.nextInt(100)+1;
									if(valeur2<=sort.vore){
														
										cible.mere.setVisible(false);
										cible.maitre=temp.statistique;
										temp.statistique.ajouterVictime(cible);
										temp.modeAffichage=3;
									}
								}
								
								if(!FenetreTest.instance.determinationEnnemi(temp, FenetreTest.instance.getNoObj(cible.mere)) && sort.harmful==0){
									if(cible.mere.attackID!=0){
										FenetreTest.instance.src=temp;
										FenetreTest.instance.tgt=cible.mere;
										FenetreTest.instance.executeScript(cible.mere.attackID);
									}

								}
								if(sort.bonusID!=0){
									int bon=cible.isEnchanted(sort.bonusID);
									if(bon!=-1){
										Bonus tem=(Bonus)cible.bonus.elementAt(bon);
										if(tem.stack>0)
											tem.tempsMax+=time;
										else
											cible.bonus.addElement(new Bonus(sort.bonusID,cible,time));
									}
									else
										cible.bonus.addElement(new Bonus(sort.bonusID,cible,time));
								}
								if(sort.eventID!=0){
									FenetreTest.instance.src=temp;
									FenetreTest.instance.tgt=cible.mere;
									FenetreTest.instance.executeScript(sort.eventID);
								}
							}
											
						}
					}
					break;
				case 3:
					if(FenetreTest.instance.inventaire.getSelectedIndex()!=-1){
						ObjInventaire cible=(ObjInventaire)FenetreTest.instance.inventaire.getSelectedValue();
							if(cible.isEquippable()){
								if(cible.equipped)
									cible.unequip();
								cible.changerBonus(sort.bonusID);
								AudioLibrairie.playClip(sort.isonCast);
								FenetreTest.instance.ajouterProjectile(sort.projectileID,temp.getX(), temp.getY(), temp.getX(), temp.getY(),2, temp.tailleY/2, temp.tailleY/2);
										   
								FenetreTest.instance.animationProjectileStand();
								FenetreTest.instance.enleverProjectile();
							}
					 }
					 break;
			}
			
			
			
			
			
			
			
			nbUse--;
			if(nbUse==0)
				dispose();
		}
		
	}
}
