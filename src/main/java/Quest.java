///////////////////////////////////////////////////////////////////////////////////////////////////
//
// Quest.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.awt.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
class QuestData implements Serializable
{
	public int QuestID; // Key field (must come first)
	
	public String Nom;
	public String Description;
	
	public String QuestIcon;
	
	public int price;
	public int TransformID;
	
	public int Use;
	public int EventID;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Quest extends ObjInventaire
{
	public int use;
	public int eventID;
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public Quest(int questID, int bonusID, int val, PlayerStat m){
		this(questID,0,m);
		use=val;
		if(bonusID!=0){
			bonus=new Bonus(bonusID,m);
			bonus.appliquerTempBonus();
		}
		this.bonusID=bonusID;
	}
	
	public Quest(int questID,int bonusID, PlayerStat m) {
		super(m,questID);
		type=Util.ITEM_QUEST;
		if(bonusID!=0){
			bonus=new Bonus(bonusID,m);
			bonus.appliquerBonus();
		}
		this.bonusID=bonusID;
		
		loadFromData( DataUtil.getQuestData( questID ) );
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFromData( QuestData questData )
	{
		setName( questData.Nom );
		setImage( questData.QuestIcon );

		description = questData.Description;
		price = questData.price;
		use = questData.Use;
		eventID = questData.EventID;
		transformID = questData.TransformID;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	@Override // ObjetInventaire
	public boolean isUsable()
	{
		return eventID != 0;
	}
	
	@Override // ObjetInventaire
	public void use(){
		super.use();
		if(eventID!=0 && (transformID==0 || mere.mere.carID==transformID) ){
			FenetreTest.instance.src=mere.mere;
			FenetreTest.instance.tgt=FenetreTest.instance.getSelectedTarget().mere;
			FenetreTest.instance.executeScript(eventID);
			if(use!=-1){
				use--;
				if(use==0){
					dispose();
				}
			}
		}
	}
	
	@Override // ObjetInventaire
	public void dispose()
	{
		if(bonus!=null)
		{
			bonus.enleverBonus();
			bonus = null;
		}

		super.dispose();
	}

	@Override // ObjetInventaire
	public int getValeur()
	{
		return use;
	}
	
}
