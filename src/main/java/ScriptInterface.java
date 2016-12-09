
import java.util.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

public class ScriptInterface
{
	public static final ScriptInterface instance = new ScriptInterface();
	public static final Hashtable<String,MethodHandle> methods = new Hashtable<>();
	
	static
	{
		final Method[] declaredMethods = ScriptInterface.class.getDeclaredMethods();
		final MethodHandles.Lookup declaredMethodLookup = MethodHandles.lookup();

		for (int i = 0; i < declaredMethods.length; ++i)
		{
			try
			{
				methods.put( declaredMethods[i].getName().toLowerCase(), declaredMethodLookup.unreflect( declaredMethods[i] ) );
			}
			catch (Exception e)
			{
				Util.error( e );
			}
		}
	}

	public void iAddCaracter( String[] commande ){
		// Typo alias
		AddCaracter(commande);
	}
	public void AddCaracter( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphique(
				Integer.parseInt(commande[1]),
				Integer.parseInt(commande[3]),
				Integer.parseInt(commande[4]),
				Integer.parseInt(commande[5]),
				Integer.parseInt(commande[6]),
				Integer.parseInt(commande[8]),
				FenetreTest.MONSTER,
				Integer.parseInt(commande[7]),
				Integer.parseInt(commande[2]),
				Integer.parseInt(commande[9]) );
	}
	public void AddPlayer( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphique(
				Integer.parseInt(commande[1]),
				Integer.parseInt(commande[3]),
				Integer.parseInt(commande[4]),
				Integer.parseInt(commande[5]),
				Integer.parseInt(commande[6]),
				Integer.parseInt(commande[8]),
				FenetreTest.PLAYER,
				Integer.parseInt(commande[7]),
				Integer.parseInt(commande[2]),
				Integer.parseInt(commande[9]) );
	}
	public void AddNPC( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphique(
				Integer.parseInt(commande[1]),
				Integer.parseInt(commande[3]),
				Integer.parseInt(commande[4]),
				Integer.parseInt(commande[5]),
				Integer.parseInt(commande[6]),
				Integer.parseInt(commande[8]),
				FenetreTest.NPC,
				Integer.parseInt(commande[7]),
				Integer.parseInt(commande[2]),
				Integer.parseInt(commande[9]) );
	}
	public void GiveObject( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
		{
			int temp2=Integer.parseInt(commande[4]);
			boolean temp3=false;
			if(temp2==1)
				temp3=true;
			temp.statistique.addObjInventaire(Integer.parseInt(commande[1]), Integer.parseInt(commande[3]), Integer.parseInt(commande[2]), temp3);
		}
	}
	public void GiveGold( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.giveMoney(Integer.parseInt(commande[1]));
		}
	}
	public void GiveExp( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.giveExp(Long.parseLong(commande[1]));
		}
	}
	public void ChangeName( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			temp.statistique.setName( JOptionPane.showInputDialog(this,"Enter the name of your character") );
		}
	}
	public void RemoveGold( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			temp.statistique.giveMoney(-Integer.parseInt(commande[1]));
		}
	}
	public void DisableVore( String[] commande ){
		FenetreTest.instance.voreEnabled = 0;
	}
	public void EnableVore( String[] commande ){
		FenetreTest.instance.voreEnabled = 1;
	}
	public void RemoveObject( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			temp.statistique.removeObjInventaire(
					Integer.parseInt(commande[1]),
					Integer.parseInt(commande[2]) );
		}
	}
	public void Conversation( String[] commande ){
		FenetreTest.instance.dialogue=new FenetreDialogue(
				commande[1],
				commande[2] );
		FenetreTest.instance.repaint();
	}
	public void AddChocie( String[] commande ){
		// Typo Alias
		AddChoice(commande);
	}
	public void AddChoice( String[] commande ){
		FenetreTest.instance.dialogue.addChoix(
				commande[1],
				Integer.parseInt(commande[2]) );
	}
	public void ShowDialog( String[] commande ){
		FenetreTest.instance.dialogue.setVisible(true);
	}
	public void ToScript( String[] commande ){
		FenetreTest.instance.executeScript(Integer.parseInt(commande[1]));
	}
	public void VerifyGold( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.getMoney()>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyStrength( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.strength>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyDexterity( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.dexterity>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyIntelligence( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp.statistique.intelligence>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyEndurence( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp.statistique.endurence>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyCharisma( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.charisma>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyLevel( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp.statistique.niveau>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyHP( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.pv>=Integer.parseInt(commande[1]))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyMP( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.pm>=Integer.parseInt(commande[1]))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyObject( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.verifyObjet(Integer.parseInt(commande[1]), Integer.parseInt(commande[2])))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
		}
	}
	public void VerifyNPC( String[] commande ){
		ObjetGraphique pla=FenetreTest.instance.getPlayer();
		boolean resu=false;
		int recherche=Integer.parseInt(commande[1]);
		ObjetGraphique temp;
		for(int j=0;j<FenetreTest.instance.objetG.size();j++){
			temp=FenetreTest.instance.objetG.elementAt(j);
			if(temp==pla)
				continue;
			if(temp.statistique.carID==recherche && temp.enable!=0){
				resu=true;
				break;
			}
		}
		if(resu){
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		}
		else{
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void DamageHP( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			temp.statistique.appliquerDommage(Integer.parseInt(commande[1]),Integer.parseInt(commande[2]));
			temp.statistique.reajusterPoint();
		}
	}
	public void DamageMP( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			temp.statistique.pm-=Integer.parseInt(commande[1]);
			temp.statistique.reajusterPoint();
		}
	}
	public void MovePlayer( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, false );
			temp.setPosition(
				Integer.parseInt(commande[1]),
				Integer.parseInt(commande[2]) );
		}
	}
	public void CheckValueTarget( String[] commande ){
		// Typo alias
		CheckValue(commande);
	}
	public void CheckValue( String[] commande ){
		Random hasard=new Random();
		int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
		if(resul<=Integer.parseInt(commande[2]))
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
	}
	public void CheckStrength( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		Random hasard=new Random();
		int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
		if(resul<=temp.statistique.strength && !FenetreTest.instance.getFailChecks())
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void CheckIntelligence( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
			if(resul<=temp.statistique.intelligence && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void CheckDexterity( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int resul=hasard.nextInt(Integer.parseInt(commande[1]));
			if(resul<=temp.statistique.dexterity && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void CheckEndurence( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
			if(resul<=temp.statistique.endurence && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void CheckCharisma( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
			if(resul<=temp.statistique.charisma && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyDefense( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			if(temp.statistique.getDefense()>=Integer.parseInt(commande[1]) && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
	}
	public void VerifyEventComp( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp==null)
			FenetreTest.instance.executeScript(Integer.parseInt(commande[5]));
		else{
			if(temp.statistique.testEventCompteur(commande[1],Integer.parseInt(commande[2]),Integer.parseInt(commande[3]) ))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[5]));
		}
	}
	public void VerifyEventTime( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp==null)
			FenetreTest.instance.executeScript(Integer.parseInt(commande[8]));
		else{
			if(temp.statistique.testEventTemps(commande[1],Integer.parseInt(commande[2]),Integer.parseInt(commande[3]),Integer.parseInt(commande[4]),Integer.parseInt(commande[5]),Integer.parseInt(commande[6])    ))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[7]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[8]));
		}
	}
	public void CheckResistance( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int resul=hasard.nextInt(Integer.parseInt(commande[1]))+1;
			if(resul<=temp.statistique.resistance[Integer.parseInt(commande[2])] && !FenetreTest.instance.getFailChecks())
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
		}
	}
	public void AddEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
			temp.statistique.nouveauEvent(commande[1],Integer.parseInt(commande[2]));
	}
	public void ModifyEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
			temp.statistique.modificationEvent(commande[1],Integer.parseInt(commande[2]));
	}
	public void IncrementEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
			temp.statistique.incrementationEvent(commande[1],Integer.parseInt(commande[2]));
	}
	public void SwallowMe( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			ObjetGraphique autre;
			int statID=Integer.parseInt(commande[2]);
			int graphID=Integer.parseInt(commande[1]);
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
					FenetreTest.instance.objetG.elementAt(k).statistique.victimes.add(new VHold(temp.statistique,FenetreTest.instance.objetG.elementAt(k).statistique));
					temp.setVisible(false);
					temp.statistique.maitre=FenetreTest.instance.objetG.elementAt(k).statistique;
					FenetreTest.instance.objetG.elementAt(k).modeAffichage=3;
					AudioLibrairie.playClip( FenetreTest.instance.objetG.elementAt(k).statistique.isonV) ;
					break;
				}
			}
		}
		FenetreTest.instance.repaintgame();
	}
	public void ChangeSide( String[] commande )
	{
		ObjetGraphique autre;
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		int side=Integer.parseInt(commande[3]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++)
		{
			autre=FenetreTest.instance.objetG.elementAt(k);
			if(autre.carID==graphID && autre.statistique.carID==statID)
			{
				autre.statistique.side=side;
				autre.statistique.actualSide=side;
			}
		}
	}
	public void PlaySound( String[] commande ){
		if (FenetreTest.instance.isSoundEnabled())
		{
			AudioLibrairie.playClip(AudioLibrairie.getClip(commande[1]));
		}
	}
	public void PassTime( String[] commande ){
		ObjetGraphique temp;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp!=null){
				int val=Integer.parseInt(commande[1]);
				for(int l=0;l<val;l++){
					if(temp.type==FenetreTest.PLAYER)
						temp.statistique.passeTemps(1);
					else
						temp.statistique.passeTempsScript(1);
				}    
			}
		}  
	}
	public void Heal( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
			temp.statistique.gainHp(valeur);
		}
	}
	public void Rejuvenate( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			Random hasard=new Random();
			int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
			temp.statistique.gainMp(valeur);
		}
	}
	public void HealParty( String[] commande ){
		ObjetGraphique temp;
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
		   temp=FenetreTest.instance.objetG.elementAt(k);
		   if(temp.type==FenetreTest.PLAYER || temp.type==FenetreTest.NPC){
			   temp.statistique.gainHp(valeur);
		   }
		}
	}
	public void RejuvenateParty( String[] commande ){
		ObjetGraphique temp;
	   
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.type==FenetreTest.PLAYER || temp.type==FenetreTest.NPC){
				temp.statistique.gainMp(valeur);
			}
		}
	}
	public void HealTarget( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.tgt.statistique.gainHp(valeur);
	}
	public void RejuvenateTarget( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.tgt.statistique.gainMp(valeur);
	}
	public void HealSource( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.src.statistique.gainHp(valeur);
	}
	public void RejuvenateSource( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.src.statistique.gainMp(valeur);
	}
	public void ChangeMap( String[] commande ){
		FenetreTest.instance.changerMap(Integer.parseInt(commande[1]), Integer.parseInt(commande[2]),Integer.parseInt(commande[3]), true );
	}
	public void DamageHpSource( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.src.statistique.appliquerDommage(valeur, Integer.parseInt(commande[4]));
		if(FenetreTest.instance.src.statistique.pv<1)
			FenetreTest.instance.src.statistique.pv=1;
	}
	public void DamageHpTarget( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.tgt.statistique.appliquerDommage(valeur, Integer.parseInt(commande[4]));
		if(FenetreTest.instance.tgt.statistique.pv<1)
			FenetreTest.instance.tgt.statistique.pv=1;
	}
	 public void DamageMpSource( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.src.statistique.gainMp(-valeur);
		if(FenetreTest.instance.src.statistique.pm<0)
			FenetreTest.instance.src.statistique.pv=0;
	}
	public void DamageMpTarget( String[] commande ){
		Random hasard=new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.tgt.statistique.gainMp(-valeur);
		if(FenetreTest.instance.tgt.statistique.pm<0)
			FenetreTest.instance.tgt.statistique.pm=0;
	}
	public void ChangeSideTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.side=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.actualSide=Integer.parseInt(commande[1]);
	}
	public void ChangeSideSource( String[] commande ){
		FenetreTest.instance.src.statistique.side=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.actualSide=Integer.parseInt(commande[1]);
	}
	public void SwallowTarget( String[] commande ){
		FenetreTest.instance.tgt.setVisible(false);
		FenetreTest.instance.tgt.statistique.maitre=FenetreTest.instance.src.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonV) ;
		FenetreTest.instance.src.modeAffichage=3;
		FenetreTest.instance.src.statistique.victimes.add(new VHold(FenetreTest.instance.tgt.statistique, FenetreTest.instance.src.statistique));
		FenetreTest.instance.repaintgame();
	}
	public void SwallowSource( String[] commande ){
		FenetreTest.instance.src.setVisible(false);
		FenetreTest.instance.src.statistique.maitre=FenetreTest.instance.tgt.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonV) ;
		FenetreTest.instance.tgt.modeAffichage=3;
		FenetreTest.instance.tgt.statistique.victimes.add(new VHold(FenetreTest.instance.src.statistique, FenetreTest.instance.tgt.statistique));
		FenetreTest.instance.repaintgame();
	}
	public void Swallow( String[] commande ){
		ObjetGraphique temp=null, temp2=null, temp3=null;
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		int v3=Integer.parseInt(commande[3]);
		int v4=Integer.parseInt(commande[4]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==v1 && temp.statistique.carID==v2){
				temp2=temp;
			}
			if(temp.carID==v3 && temp.statistique.carID==v4){
				temp3=temp;
			}
		}
		if(temp2!=null && temp3!=null){
			temp3.setVisible(false);
			temp3.statistique.maitre=temp2.statistique;
			AudioLibrairie.playClip(temp2.statistique.isonV) ;
			temp2.modeAffichage=3;
			temp2.statistique.victimes.add(new VHold(temp3.statistique, temp2.statistique));
		}
		FenetreTest.instance.repaintgame();
	}
	public void SourceVomit( String[] commande ){
		VHold temp;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(temp.digTime<FenetreTest.instance.src.statistique.digTimeMax/2 && temp.victime.pv>0 && !temp.isUnbirth()){
				FenetreTest.instance.reaparitionObjetGraphique(temp.victime.mere,FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY());
				//temp.victime.mere.changerPosition();
				temp.victime.mere.setVisible(true);
				temp.victime.maitre=null;
				FenetreTest.instance.src.statistique.victimes.remove(k);
				k--;
				AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonVo) ;
			}
		}
		if(FenetreTest.instance.src.statistique.victimes.size()<1)
			FenetreTest.instance.src.modeAffichage=1;
		FenetreTest.instance.repaintgame();
	}
	public void TargetVomit( String[] commande ){
		VHold temp;
		for(int k=0;k<FenetreTest.instance.tgt.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.tgt.statistique.victimes.elementAt(k);
			if(temp.digTime<FenetreTest.instance.tgt.statistique.digTimeMax/2 && temp.victime.pv>0 && !temp.isUnbirth()){
				FenetreTest.instance.reaparitionObjetGraphique(temp.victime.mere,FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY());
				//temp.victime.mere.changerPosition();
				temp.victime.mere.setVisible(true);
				temp.victime.maitre=null;
				FenetreTest.instance.tgt.statistique.victimes.remove(k);
				k--;
				AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonVo) ;
			}
		}
		if(FenetreTest.instance.tgt.statistique.victimes.size()<1)
			FenetreTest.instance.tgt.modeAffichage=1;
		FenetreTest.instance.repaintgame();
	}
	public void LaxativeSource( String[] commande ){
		VHold temp;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(!temp.isUnbirth())
				temp.digTime+=FenetreTest.instance.src.statistique.digTimeMax/2;
		}
		FenetreTest.instance.repaintgame();
	}
	public void LaxativeTarget( String[] commande ){
		VHold temp;
		for(int k=0;k<FenetreTest.instance.tgt.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.tgt.statistique.victimes.elementAt(k);
			if(!temp.isUnbirth())
				temp.digTime+=FenetreTest.instance.tgt.statistique.digTimeMax/2;
		}
		FenetreTest.instance.repaintgame();
	}
	public void AddBonusTarget( String[] commande ){
		Random hasard= new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		int v=Integer.parseInt(commande[4]);
		
		int bon=FenetreTest.instance.tgt.statistique.isEnchanted(v);
		if(bon!=-1){
			Bonus temp=(Bonus)FenetreTest.instance.tgt.statistique.bonus.elementAt(bon);
			if(temp.stack>0)
				temp.tempsMax+=valeur;
			else
				FenetreTest.instance.tgt.statistique.bonus.add(new Bonus(v,FenetreTest.instance.tgt.statistique,valeur));
		}
		else
			FenetreTest.instance.tgt.statistique.bonus.add(new Bonus(v,FenetreTest.instance.tgt.statistique,valeur));
	}
	public void AddBonusSource( String[] commande ){
		Random hasard= new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		int v=Integer.parseInt(commande[4]);
		int bon=FenetreTest.instance.src.statistique.isEnchanted(v);
		if(bon!=-1){
			Bonus temp=(Bonus)FenetreTest.instance.src.statistique.bonus.elementAt(bon);
			if(temp.stack>0)
				temp.tempsMax+=valeur;
			else
				FenetreTest.instance.src.statistique.bonus.add(new Bonus(v,FenetreTest.instance.src.statistique,valeur));
		}
		else
			FenetreTest.instance.src.statistique.bonus.add(new Bonus(v,FenetreTest.instance.src.statistique,valeur));
	}
	public void AddCaracterSource( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphique(Integer.parseInt(commande[1]),FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]) );
		FenetreTest.instance.reaparitionObjetGraphique( FenetreTest.instance.objetG.lastElement(), FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY());
	}
	public void AddCaracterTarget( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphique(Integer.parseInt(commande[1]),FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]) );
		FenetreTest.instance.reaparitionObjetGraphique( FenetreTest.instance.objetG.lastElement(), FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY());
	}
	public void RemoveCaracter( String[] commande ){
		ObjetGraphique temp;
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==v1 && temp.statistique.carID==v2){
				temp.statistique.mort(null);
			}
		}
		FenetreTest.instance.repaintgame();
	}
	public void AddStats( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.getPlayer();
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			stat.strength += Integer.parseInt(commande[1]);
			stat.dexterity += Integer.parseInt(commande[2]);
			stat.endurence += Integer.parseInt(commande[3]);
			stat.intelligence += Integer.parseInt(commande[4]);
			stat.charisma += Integer.parseInt(commande[5]);
		}
	}
	public void AddStatsSource( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.src;
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			stat.strength += Integer.parseInt(commande[1]);
			stat.dexterity += Integer.parseInt(commande[2]);
			stat.endurence += Integer.parseInt(commande[3]);
			stat.intelligence += Integer.parseInt(commande[4]);
			stat.charisma += Integer.parseInt(commande[5]);
		}
	}
	public void AddStatsTarget( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.tgt;
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			stat.strength += Integer.parseInt(commande[1]);
			stat.dexterity += Integer.parseInt(commande[2]);
			stat.endurence += Integer.parseInt(commande[3]);
			stat.intelligence += Integer.parseInt(commande[4]);
			stat.charisma += Integer.parseInt(commande[5]);
		}
	}
	public void ExpireAllBonuses( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.getPlayer();
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			for (int i = 0; i < stat.bonus.size(); ++i)
			{
				stat.bonus.elementAt( i ).enleverBonus();
			}
			stat.bonus.removeAllElements();
		}
	}
	public void ExpireAllBonusesSource( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.src;
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			for (int i = 0; i < stat.bonus.size(); ++i)
			{
				stat.bonus.elementAt( i ).enleverBonus();
			}
			stat.bonus.removeAllElements();
		}
	}
	public void ExpireAllBonusesTarget( String[] commande ){
		ObjetGraphique objG = FenetreTest.instance.tgt;
		if (objG != null && objG.statistique != null)
		{
			StatChar stat = objG.statistique;
			for (int i = 0; i < stat.bonus.size(); ++i)
			{
				stat.bonus.elementAt( i ).enleverBonus();
			}
			stat.bonus.removeAllElements();
		}
	}
	public void AddSpell( String[] commande ){
		FenetreTest.instance.spellbook.addElement(new Spell(Integer.parseInt(commande[1])));
	}
	public void RemoveSpell( String[] commande ){
		Spell sort;
		int v1=Integer.parseInt(commande[1]);
		for(int k=0;k<FenetreTest.instance.spellbook.getElementCount();k++){
			sort=(Spell)FenetreTest.instance.spellbook.elementAt(k);
			if(sort.spellID==v1){
				FenetreTest.instance.spellbook.removeElement(sort);
				k--;
			}
		}
	}
	public void VerifySpell( String[] commande ){
		Spell sort=null;
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		int v3=Integer.parseInt(commande[3]);
		for(int k=0;k<FenetreTest.instance.spellbook.getElementCount();k++){
			sort=(Spell)FenetreTest.instance.spellbook.elementAt(k);
			if(sort.spellID==v1){
				break;
			}
		}
		if(sort!=null && sort.spellID==v1){
			FenetreTest.instance.executeScript(v2);
		}
		else{
			FenetreTest.instance.executeScript(v3);
		}
	}
	public void VerifyVoredSource( String[] commande ){
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.maitre!=null)
			FenetreTest.instance.executeScript(v1);
		else
			FenetreTest.instance.executeScript(v2);
	}
	public void VerifyVoredTarget( String[] commande ){
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.maitre!=null)
			FenetreTest.instance.executeScript(v1);
		else
			FenetreTest.instance.executeScript(v2);
	}
	public void Buy( String[] commande ){
		PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
		int objID=Integer.parseInt(commande[1]);
		int typ=Integer.parseInt(commande[2]);
		int bonusID=Integer.parseInt(commande[3]);
		int price=Integer.parseInt(commande[4]);
		int chVendeur=Integer.parseInt(commande[5]);
		int eventID=Integer.parseInt(commande[6]);
		int dif=chVendeur-pl.charisma;
		if(dif>20)
			dif=20;
		else if(dif<-10)
			dif=-10;
		dif=price * dif*5/100 +price;
		if(pl.money<dif){
			FenetreTest.instance.executeScript(eventID);
		}
		else{
			pl.money-=dif;
			pl.addObjInventaire(objID, bonusID, typ, false);
		}
	}
	public void AddProp( String[] commande ){
		int propID=Integer.parseInt(commande[1]);
		int pX=Integer.parseInt(commande[2]);
		int pY=Integer.parseInt(commande[3]);
		FenetreTest.instance.AddProp(propID, pX, pY);
	}
	public void BlockTilt( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int pY=Integer.parseInt(commande[2]);
		FenetreTest.instance.map.getTile(pX,pY).m_block = 1;
	}
	public void HalfBlockTilt( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int pY=Integer.parseInt(commande[2]);
		FenetreTest.instance.map.getTile(pX,pY).m_block = 2;
	}
	public void UnBlockTilt( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int pY=Integer.parseInt(commande[2]);
		FenetreTest.instance.map.getTile(pX,pY).m_block = 0;
	}
	public void BlockTiltAbs( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		FenetreTest.instance.map.getTile(pX).m_block = 1;
	}
	public void HalfBlockTiltAbs( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		FenetreTest.instance.map.getTile(pX).m_block = 2;
	}
	public void UnBlockTiltAbs( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		FenetreTest.instance.map.getTile(pX).m_block = 0;
	}
	public void ChangeTiltEvent( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int pY=Integer.parseInt(commande[2]);
		int eventID=Integer.parseInt(commande[3]);
		FenetreTest.instance.map.getTile(pX,pY).m_event = eventID;
	}
	public void ChangeTiltGraph( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int pY=Integer.parseInt(commande[2]);
		String ch=commande[3];
		FenetreTest.instance.map.getTile(pX,pY).m_img = ImageLibrairies.getImage(ch);
	}
	 public void ChangeTiltEventAbs( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		int eventID=Integer.parseInt(commande[2]);
		FenetreTest.instance.map.getTile(pX).m_event = eventID;
	}
	public void ChangeTiltGraphAbs( String[] commande ){
		int pX=Integer.parseInt(commande[1]);
		String ch=commande[2];
		FenetreTest.instance.map.getTile(pX).m_img = ImageLibrairies.getImage( ch );
	}
	public void Repaint( String[] commande ){
		FenetreTest.instance.repaintgame();
	   
	}
	public void GiveFood( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
			pl.giveFood(Integer.parseInt(commande[1]));
		}
	}
	public void RepairWeapon( String[] commande ){
		PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
		int prix=Integer.parseInt(commande[1])+pl.arme.price/10 - pl.charisma;
		if(prix<1)
			prix=1;
		if(pl.arme.price>0 && pl.money>=prix)
			if(pl.arme.qualiteMax>0){
				pl.arme.qualite=pl.arme.qualiteMax;
				pl.money-=prix;
			}
	}
	public void RepairArmor( String[] commande ){
		PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
		if(pl.armure!=null){
			int prix=Integer.parseInt(commande[1])+pl.armure.price/10 - pl.charisma;
			if(prix<1)
				prix=1;
			if(pl.armure.price>0 && pl.money>=prix)
				if(pl.armure.qualiteMax>0){
					pl.armure.qualite=pl.armure.qualiteMax;
					pl.money-=prix;
				}
		}
	}
	public void IsPlayerSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(FenetreTest.instance.src==temp)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsPlayerTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(FenetreTest.instance.tgt==temp)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void ChangeDeplacementSource( String[] commande ){
		int dep=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.deplacement=dep;
	}
	public void ChangeDeplacementTarget( String[] commande ){
		int dep=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.deplacement=dep;
	}
	public void ChangeDeplacement( String[] commande ){
		int dep=Integer.parseInt(commande[1]);
		int graph=Integer.parseInt(commande[2]);
		int stat=Integer.parseInt(commande[3]);
		ObjetGraphique temp;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==graph && temp.statistique.carID==stat){
				temp.deplacement=dep;
			}
		}  
	}
	public void IsNight( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			int ev1=Integer.parseInt(commande[1]);
			int ev2=Integer.parseInt(commande[2]);
			PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
			if(pl.clock.isNight())
				FenetreTest.instance.executeScript(ev1);
			else
				FenetreTest.instance.executeScript(ev2);
		}
	}
	public void GiveGoldTaget( String[] commande ){
		// Typo alias
		GiveGoldTarget(commande);
	}
	public void GiveGoldTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.giveMoney(ev1);
	}
	public void GiveGoldSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.giveMoney(ev1);
	}
	public void GiveObjectSource( String[] commande ){
		int temp2=Integer.parseInt(commande[4]);
		boolean temp3=false;
		if(temp2==1)
			temp3=true;
		FenetreTest.instance.src.statistique.addObjInventaire(Integer.parseInt(commande[1]), Integer.parseInt(commande[3]), Integer.parseInt(commande[2]), temp3);
	}
	public void GiveObjecTarget( String[] commande ){
		// Typo alias
		GiveObjectTarget(commande);
	}
	public void GiveObjectTarget( String[] commande ){
		int temp2=Integer.parseInt(commande[4]);
		boolean temp3=false;
		if(temp2==1)
			temp3=true;
		FenetreTest.instance.tgt.statistique.addObjInventaire(Integer.parseInt(commande[1]), Integer.parseInt(commande[3]), Integer.parseInt(commande[2]), temp3);
	}
	public void AskQuestion( String[] commande ){
		String question=commande[1];
		String reponse =commande[2];
		String rep2=null;
		int ev1=Integer.parseInt(commande[3]);
		int ev2=Integer.parseInt(commande[4]);
		rep2=JOptionPane.showInputDialog(this,question);
		if(rep2==null || rep2=="" || !rep2.equalsIgnoreCase(reponse)){
			FenetreTest.instance.executeScript(ev2);
		}
		else{
			FenetreTest.instance.executeScript(ev1);
		}
	}
	public void CheckSaveTarget( String[] commande ){
		int val=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		if(!(FenetreTest.instance.tgt.statistique.getSave()<=val))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
		
	}
	public void CheckSaveSource( String[] commande ){
		int val=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		if(!(FenetreTest.instance.src.statistique.getSave()<=val))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	 public void CheckResistanceSource( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		Random hasard= new Random();
		int val=hasard.nextInt(100)+1;
		
		if(!(FenetreTest.instance.src.statistique.resistance[res]<=val))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	 public void CheckResistanceTarget( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		Random hasard= new Random();
		int val=hasard.nextInt(100)+1;
		
		if(!(FenetreTest.instance.tgt.statistique.resistance[res]<=val))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void SetFood( String[] commande ){
		PlayerStat temp=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
		int val=Integer.parseInt(commande[1]);
		FenetreTest.instance.cancelWalkTo( true );
		temp.nourriture=val;
	}
	public void UnequipArmor( String[] commande ){
		PlayerStat temp=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
		if(temp.armure!=null)
		{
			FenetreTest.instance.cancelWalkTo( true );
			temp.armure.unequip();
		}
	}
	public void UnequipWeapon( String[] commande ){
		PlayerStat temp=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
		if(temp.arme!=null)
		{
			FenetreTest.instance.cancelWalkTo( true );
			temp.arme.unequip();
		}
	}
	public void UnequipRing( String[] commande ){
		PlayerStat temp=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
		if(temp.bague!=null)
		{
			FenetreTest.instance.cancelWalkTo( true );
			temp.bague.unequip();
		}
	}
	public void UnequipAmulet( String[] commande ){
		PlayerStat temp=(PlayerStat)FenetreTest.instance.getPlayer().statistique;
		if(temp.amulet!=null)
		{
			FenetreTest.instance.cancelWalkTo( true );
			temp.amulet.unequip();
		}
	}
	public void VerifyFamilySource( String[] commande ){
		String fam=commande[1];
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		if(FenetreTest.instance.src.statistique.family.equalsIgnoreCase(fam))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyFamilyTarget( String[] commande ){
		String fam=commande[1];
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		if(FenetreTest.instance.tgt.statistique.family.equalsIgnoreCase(fam))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifySideTarget( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		
		if(FenetreTest.instance.tgt.statistique.actualSide==res)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifySideSource( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		
		if(FenetreTest.instance.src.statistique.actualSide==res)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void AddBonus( String[] commande ){
		Random hasard= new Random();
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		int v=Integer.parseInt(commande[4]);
		int c=Integer.parseInt(commande[5]);
		int s=Integer.parseInt(commande[6]);
		ObjetGraphique temp;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==c && temp.statistique.carID==s){
				int bon=temp.statistique.isEnchanted(v);
				if(bon!=-1){
					Bonus tem=(Bonus)temp.statistique.bonus.elementAt(bon);
					if(tem.stack>0)
						tem.tempsMax+=valeur;
					else
						temp.statistique.bonus.add(new Bonus(v,temp.statistique,valeur));
				}
				else
					temp.statistique.bonus.add(new Bonus(v,temp.statistique,valeur));  
			}
		}  
	}
	public void VerifyPolymorphSource( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		
		if(FenetreTest.instance.src.statistique.polymorph==res)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyPolymorphTarget( String[] commande ){
		int res=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		
		if(FenetreTest.instance.tgt.statistique.polymorph==res)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void DisableTeleport( String[] commande ){
		FenetreTest.instance.teleport=false;
	}
	public void EnableTeleport( String[] commande ){
		FenetreTest.instance.teleport=false;
	}
	 public void VerifyTeleport( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		
		if(FenetreTest.instance.teleport)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void DisableItem( String[] commande ){
		FenetreTest.instance.setItemsEnabled( false );
	}
	public void EnableItem( String[] commande ){
		FenetreTest.instance.setItemsEnabled( true );
	}
	 public void DisableSpell( String[] commande ){
		FenetreTest.instance.setSpellsEnabled( false );
	}
	public void EnableSpell( String[] commande ){
		FenetreTest.instance.setSpellsEnabled( true );
	}
	public void DisableSave( String[] commande ){
		FenetreTest.instance.setSaveEnabled( false );
	}
	public void EnableSave( String[] commande ){
		FenetreTest.instance.setSaveEnabled( true );
	}
	public void VerifySide( String[] commande ){
		int cID=Integer.parseInt(commande[1]);
		int sID=Integer.parseInt(commande[2]);
		int si=Integer.parseInt(commande[3]);
		int ev1=Integer.parseInt(commande[4]);
		int ev2=Integer.parseInt(commande[5]);
		ObjetGraphique temp;
		boolean trouve=false;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==cID && temp.statistique.carID==sID && temp.statistique.actualSide==si){      
				trouve=true;
				break;
			}
		}  
		if(trouve)
			FenetreTest.instance.executeScript(ev1);
		else
		   FenetreTest.instance.executeScript(ev2);
	}
	public void ChangeTypeCharacter( String[] commande ){
		int cID=Integer.parseInt(commande[1]);
		int sID=Integer.parseInt(commande[2]);
		int typ=Integer.parseInt(commande[3]);
		ObjetGraphique temp;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==cID && temp.statistique.carID==sID ){      
				temp.type=typ;
				break;
			}
		}  
	}
	public void ChangeTypeCharacterTarget( String[] commande ){
		int typ=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.type=typ;
	}
	public void ChangeTypeCharacterSource( String[] commande ){
		int typ=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.type=typ;
	}
	public void BuyGroup( String[] commande ){
		PlayerStat pl = (PlayerStat)FenetreTest.instance.getPlayer().statistique;
		int objID=Integer.parseInt(commande[1]);
		int typ=Integer.parseInt(commande[2]);
		int bonusID=Integer.parseInt(commande[3]);
		int price=Integer.parseInt(commande[4]);
		int chVendeur=Integer.parseInt(commande[5]);
		int eventID=Integer.parseInt(commande[6]);
		int nb=Integer.parseInt(commande[7]);
		int dif=chVendeur-pl.charisma;
		if(dif>20)
			dif=20;
		else if(dif<-10)
			dif=-10;
		dif=price * dif*5/100 +price;
		if(pl.money<dif){
			FenetreTest.instance.executeScript(eventID);
		}
		else{
			pl.money-=dif;
			for(int h=0;h<nb;h++)
				pl.addObjInventaire(objID, bonusID, typ, false);
		}
	}
	public void SwallowYou( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			ObjetGraphique autre;
			int statID=Integer.parseInt(commande[2]);
			int graphID=Integer.parseInt(commande[1]);
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
					temp.statistique.victimes.add(new VHold(FenetreTest.instance.objetG.elementAt(k).statistique,temp.statistique));
					FenetreTest.instance.objetG.elementAt(k).setVisible(false);
					FenetreTest.instance.objetG.elementAt(k).statistique.maitre=temp.statistique;
					temp.modeAffichage=3;
					AudioLibrairie.playClip( temp.statistique.isonV) ;
					break;
				}
			}
		}
		FenetreTest.instance.repaint();
	}
	public void IsSourceShrinked( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		
		if(FenetreTest.instance.src.statistique.size==1)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetShrinked( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		
		if(FenetreTest.instance.tgt.statistique.size==1)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceEnlarged( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		
		if(FenetreTest.instance.src.statistique.size==2)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetEnlarged( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		
		if(FenetreTest.instance.tgt.statistique.size==2)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsPlayerShrinked( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp.statistique.size==1)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsPlayerEnlarged( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp.statistique.size==2)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyEventMask( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp==null)
			FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
		else{
			if(temp.statistique.testEventMasque(commande[1],Integer.parseInt(commande[2]) ))
				FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
			else
				FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
		}
	}
	public void IncrementEventMask( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
			temp.statistique.incrementEventMasque(commande[1],Integer.parseInt(commande[2]));
	}
	public void MoveNPC( String[] commande ){
		ObjetGraphique temp=null;
		int statID=Integer.parseInt(commande[4]);
		int graphID=Integer.parseInt(commande[3]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
		   }
		}
		if(temp!=null){
			temp.setPosition(
				Integer.parseInt(commande[1]),
				Integer.parseInt(commande[2]) );
		}
	}
	public void TransformSource( String[] commande ){
		FenetreTest.instance.src.statistique.transform(Integer.parseInt(commande[1]));
	}
	public void TransformTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.transform(Integer.parseInt(commande[1]));
	}
	public void Transform( String[] commande ){
		ObjetGraphique temp=null;
		int statID=Integer.parseInt(commande[3]);
		int graphID=Integer.parseInt(commande[2]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
		   }
		}
		if(temp!=null){
			temp.statistique.transform(Integer.parseInt(commande[1]));
		}
	}
	public void RemoveEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null)
			temp.statistique.removeEvent(commande[1]);
	}
	public void VerifyVored( String[] commande ){
		ObjetGraphique temp=null;
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
		   }
		}
		if(temp!=null && temp.statistique.maitre!=null){
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
		}
		else{
			 FenetreTest.instance.executeScript(Integer.parseInt(commande[4]));
		}
	}
	public void SaveNpcLevel( String[] commande ){
		String ev=commande[1];
		int graphID=Integer.parseInt(commande[2]);
		int statID=Integer.parseInt(commande[3]);
		int niveau=0;
		ObjetGraphique temp=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
		   }
		}
		if(temp!=null){
			niveau=temp.statistique.niveau;
		}
		temp=FenetreTest.instance.getPlayer();
		temp.statistique.removeEvent(ev);
		temp.statistique.nouveauEvent(ev,niveau);
	}
	public void RestoreNpcLevel( String[] commande ){
		String ev=commande[1];
		int graphID=Integer.parseInt(commande[2]);
		int statID=Integer.parseInt(commande[3]);
		int niveau=0;
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		Evenement temp2=temp.statistique.getEvenement(ev);
		if(temp2!=null)
			niveau=temp2.compteur;
		temp=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
		   }
		}
		if(temp!=null){
		   long exp=(niveau+1)*niveau/2 *1000 -1000+1;
		   exp-=temp.statistique.experience;
		   if(exp>0)
			   temp.statistique.giveExp(exp); 
		}
		
	}
	 public void FollowerHere( String[] commande ){
		ObjetGraphique temp=null;
		boolean valeur=false;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				valeur=true;
				break;
		   }
		}
		if(valeur){
			FenetreTest.instance.executeScript(Integer.parseInt(commande[1]));
		}
		else{
			 FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		}
	 }
	public void AddRandomEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		Random hasard=new Random();
		int resul=hasard.nextInt(Integer.parseInt(commande[2]));
		if(temp!=null)
			temp.statistique.nouveauEvent(commande[1],resul);
	}
	public void RemoveOneObject( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.removeOneObjInventaire(Integer.parseInt(commande[1]),Integer.parseInt(commande[2]));
		}
	}
	public void FollowerSwallowed( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		if(autre!=null){
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
					temp=FenetreTest.instance.objetG.elementAt(k);
					temp.statistique.victimes.add(new VHold(autre.statistique,temp.statistique));
					autre.setVisible(false);
					autre.statistique.maitre=temp.statistique;
					temp.modeAffichage=3;
					AudioLibrairie.playClip( temp.statistique.isonV) ;
					break;
				}
			}
			FenetreTest.instance.repaintgame();
		}
	}
	public void FollowerSwallow( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		if(autre!=null){
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
					temp=FenetreTest.instance.objetG.elementAt(k);
					autre.statistique.victimes.add(new VHold(temp.statistique,autre.statistique));
					temp.setVisible(false);
					temp.statistique.maitre=autre.statistique;
					autre.modeAffichage=3;
					AudioLibrairie.playClip(autre.statistique.isonV) ;
					break;
				}
			}
			FenetreTest.instance.repaintgame();
		}
	}
	public void FollowerSwallowSource( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		if(autre!=null){
			autre.statistique.victimes.add(new VHold(FenetreTest.instance.src.statistique,autre.statistique));
			FenetreTest.instance.src.setVisible(false);
			FenetreTest.instance.src.statistique.maitre=autre.statistique;
			autre.modeAffichage=3;
			AudioLibrairie.playClip(autre.statistique.isonV);

			FenetreTest.instance.repaintgame();
		}
	}
	public void FollowerSwallowTarget( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		if(autre!=null){
			autre.statistique.victimes.add(new VHold(FenetreTest.instance.tgt.statistique,autre.statistique));
			FenetreTest.instance.tgt.setVisible(false);
			FenetreTest.instance.tgt.statistique.maitre=autre.statistique;
			autre.modeAffichage=3;
			AudioLibrairie.playClip(autre.statistique.isonV) ;

			FenetreTest.instance.repaintgame();
		}
	}
	public void FollowerSwallowedSource( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		if(autre!=null){
			FenetreTest.instance.src.statistique.victimes.add(new VHold(autre.statistique,FenetreTest.instance.src.statistique));
			autre.setVisible(false);
			autre.statistique.maitre=FenetreTest.instance.src.statistique;
			FenetreTest.instance.src.modeAffichage=3;
			AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonV) ;

			FenetreTest.instance.repaintgame();
		}
	}
	public void FollowerSwallowedTarget( String[] commande ){
		ObjetGraphique temp,autre=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre=temp;
				break;
		   }
		}
		if(autre!=null){
			FenetreTest.instance.tgt.statistique.victimes.add(new VHold(autre.statistique,FenetreTest.instance.tgt.statistique));
			autre.setVisible(false);
			autre.statistique.maitre=FenetreTest.instance.tgt.statistique;
			FenetreTest.instance.tgt.modeAffichage=3;
			AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonV) ;

			FenetreTest.instance.repaintgame();
		}
	}
	public void SwallowAllFollowers( String[] commande ){
		ObjetGraphique autre=null, temp;
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				autre=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				autre.statistique.victimes.add(new VHold(temp.statistique,autre.statistique));
				temp.setVisible(false);
				temp.statistique.maitre=autre.statistique;
				autre.modeAffichage=3;
				AudioLibrairie.playClip(autre.statistique.isonV) ;
			}
		}

		FenetreTest.instance.repaintgame();
	}
	public void TargetSwallowAllFollowers( String[] commande ){
		ObjetGraphique temp;

		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.tgt, true );
			
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				FenetreTest.instance.tgt.statistique.victimes.add(new VHold(temp.statistique,FenetreTest.instance.tgt.statistique));
				temp.setVisible(false);
				temp.statistique.maitre=FenetreTest.instance.tgt.statistique;
				FenetreTest.instance.tgt.modeAffichage=3;
				AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonV) ;
			}
		}

		FenetreTest.instance.repaintgame();
	}
	public void SourceSwallowAllFollowers( String[] commande ){
		ObjetGraphique temp;
		
		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.src, true );

		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if( temp.type==FenetreTest.NPC && temp.statistique.pv>-999){
				FenetreTest.instance.src.statistique.victimes.add(new VHold(temp.statistique,FenetreTest.instance.src.statistique));
				temp.setVisible(false);
				temp.statistique.maitre=FenetreTest.instance.src.statistique;
				FenetreTest.instance.src.modeAffichage=3;
				AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonV) ;
			}
		}

		FenetreTest.instance.repaintgame();
	}
	public void SwallowFriend( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			ObjetGraphique autre;
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).type==FenetreTest.NPC && FenetreTest.instance.objetG.elementAt(k).statistique.pv>-999){
					temp.statistique.victimes.add(new VHold(FenetreTest.instance.objetG.elementAt(k).statistique,temp.statistique));
					FenetreTest.instance.objetG.elementAt(k).setVisible(false);
					FenetreTest.instance.objetG.elementAt(k).statistique.maitre=temp.statistique;
					temp.modeAffichage=3;
					AudioLibrairie.playClip( temp.statistique.isonV) ;
					break;
				}
			}
		}
		FenetreTest.instance.repaintgame();
	}
	public void FriendSwallow( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			FenetreTest.instance.cancelWalkTo( temp, true );
			ObjetGraphique autre;
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				if( FenetreTest.instance.objetG.elementAt(k).type==FenetreTest.NPC && FenetreTest.instance.objetG.elementAt(k).statistique.pv>-999){
					FenetreTest.instance.objetG.elementAt(k).statistique.victimes.add(new VHold(temp.statistique,FenetreTest.instance.objetG.elementAt(k).statistique));
					temp.setVisible(false);
					temp.statistique.maitre=FenetreTest.instance.objetG.elementAt(k).statistique;
					FenetreTest.instance.objetG.elementAt(k).modeAffichage=3;
					AudioLibrairie.playClip( FenetreTest.instance.objetG.elementAt(k).statistique.isonV) ;
					break;
				}
			}
		}
		FenetreTest.instance.repaintgame();
	}
	public void SetTarget( String[] commande ){
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				FenetreTest.instance.tgt=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
	}
	public void SetSource( String[] commande ){
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				FenetreTest.instance.src=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
	}
	public void SetTargetFollower( String[] commande ){
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).type==FenetreTest.NPC && FenetreTest.instance.objetG.elementAt(k).statistique.pv>-999){
				FenetreTest.instance.tgt=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
	}
	 public void SetSourceFollower( String[] commande ){
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).type==FenetreTest.NPC && FenetreTest.instance.objetG.elementAt(k).statistique.pv>-999){
				FenetreTest.instance.src=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
	}
	public void AddProjectile( String[] commande ){
		int projectileID=Integer.parseInt(commande[1]);
		int typeSort=Integer.parseInt(commande[2]);
		int event=Integer.parseInt(commande[3]);
		int event2=Integer.parseInt(commande[4]);
		
		int tableau[]=FenetreTest.instance.cibleBloquee(FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), FenetreTest.instance.src.getX(),FenetreTest.instance.src.getY());
		int poX;
		int poY;
		if (tableau==null){
			poX=FenetreTest.instance.tgt.getX();
			poY=FenetreTest.instance.tgt.getY();
		}
		else{
			poX=tableau[0];
			poY=tableau[1];
		}
								   
		if(typeSort==1){
			FenetreTest.instance.ajouterProjectile(projectileID,FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), poX, poY,1,FenetreTest.instance.src.tailleY/2, FenetreTest.instance.tgt.tailleY/2);
			FenetreTest.instance.animationProjectileMove();
			FenetreTest.instance.enleverProjectile();
		}
	   
		FenetreTest.instance.ajouterProjectile(projectileID,poX, poY, poX, poY, 2, FenetreTest.instance.src.tailleY/2, FenetreTest.instance.tgt.tailleY/2);
		FenetreTest.instance.animationProjectileStand();
		FenetreTest.instance.enleverProjectile();
		if(tableau==null){
		   FenetreTest.instance.executeScript(event);
		}
		else{
			FenetreTest.instance.executeScript(event2);
		}
	}
	public void AddProjectileBase( String[] commande ){
		int projectileID=Integer.parseInt(commande[1]);
		int typeSort=Integer.parseInt(commande[2]);
		int event=Integer.parseInt(commande[3]);
		int event2=Integer.parseInt(commande[4]);
		
		int tableau[]=FenetreTest.instance.cibleBloquee(FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), FenetreTest.instance.src.getX(),FenetreTest.instance.src.getY());
		int poX;
		int poY;
		if (tableau==null){
			poX=FenetreTest.instance.tgt.getX();
			poY=FenetreTest.instance.tgt.getY();
		}
		else{
			poX=tableau[0];
			poY=tableau[1];
		}
								   
		if(typeSort==1){
			FenetreTest.instance.ajouterProjectile(projectileID,FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), poX, poY,1,FenetreTest.instance.src.tailleY, FenetreTest.instance.tgt.tailleY*10);
			FenetreTest.instance.animationProjectileMove();
			FenetreTest.instance.enleverProjectile();
		}
	   
		FenetreTest.instance.ajouterProjectile(projectileID,poX, poY, poX, poY, 2, FenetreTest.instance.src.tailleY/2, FenetreTest.instance.tgt.tailleY/10);
		FenetreTest.instance.animationProjectileStand();
		FenetreTest.instance.enleverProjectile();
		if(tableau==null){
		   FenetreTest.instance.executeScript(event);
		}
		else{
			FenetreTest.instance.executeScript(event2);
		}
	}
	public void SetTargetSource( String[] commande ){
		FenetreTest.instance.tgt=FenetreTest.instance.src;
	}
	public void SetSourceTarget( String[] commande ){
		FenetreTest.instance.src=FenetreTest.instance.tgt;
	}
	public void ExchangeSourceTarget( String[] commande ){
		ObjetGraphique temp;
		temp=FenetreTest.instance.src;
		FenetreTest.instance.src=FenetreTest.instance.tgt;
		FenetreTest.instance.tgt=temp;
	}
	public void cAffectAll( String[] commande ){
		// Typo alias
		AffectAll(commande);
	}
	public void AffectAll( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(temp.statistique.maitre ==null && temp.statistique.pv>-999){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null ){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void AffectEnnemis( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
	   
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(FenetreTest.instance.determinationEnnemi(origine, k) && temp.statistique.maitre ==null && temp.statistique.pv>-999 && origine!=temp){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void AffectAllies( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(!FenetreTest.instance.determinationEnnemi(origine, k) && temp.statistique.maitre ==null && temp.statistique.pv>-999){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void AffectParty( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(temp.statistique.side==origine.statistique.side && temp.statistique.maitre ==null && temp.statistique.pv>-999){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void AffectNonParty( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(! (temp.statistique.side==origine.statistique.side) &&  temp.statistique.maitre ==null && temp.statistique.pv>-999){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void AffectAllCaster( String[] commande ){
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;              
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(temp!=origine && temp.statistique.maitre ==null && temp.statistique.pv>-999){
				if(range<1){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					FenetreTest.instance.tgt=temp;
					FenetreTest.instance.executeScript(scriptID);
				}


				if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
					if(originType.equalsIgnoreCase("target"))
						temp.statistique.mort(FenetreTest.instance.tgt.statistique);
					else if(originType.equalsIgnoreCase("source"))
						temp.statistique.mort(FenetreTest.instance.src.statistique);
					else if(originType.equalsIgnoreCase("firstSource"))
						temp.statistique.mort(source.statistique);
					else if(originType.equalsIgnoreCase("firstTarget"))
						temp.statistique.mort(target.statistique);
					else if(originType.equalsIgnoreCase("player"))
						temp.statistique.mort(origine.statistique);
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
	}
	public void LethalHpSource( String[] commande ){
		Random hasard=new Random();
		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.src, false );
			
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.src.statistique.appliquerDommage(valeur, Integer.parseInt(commande[4]));
	}
	public void LethalHpTarget( String[] commande ){
		Random hasard=new Random();
		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.tgt, false );
			
		int valeur= hasard.nextInt(Integer.parseInt(commande[2])-Integer.parseInt(commande[1]))+Integer.parseInt(commande[1])+Integer.parseInt(commande[3]);
		FenetreTest.instance.tgt.statistique.appliquerDommage(valeur, Integer.parseInt(commande[4]));
	}
	public void RemoveDead( String[] commande ){
		FenetreTest.instance.enleverMorts();
	}
	public void ExecuteAttackedScriptSource( String[] commande ){
		FenetreTest.instance.executeScript(FenetreTest.instance.src.attackID);
	}
	public void ExecuteAttackedScriptTarget( String[] commande ){
		FenetreTest.instance.executeScript(FenetreTest.instance.tgt.attackID);
	}
	public void GiveExpCreature( String[] commande ){
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		int exper=Integer.parseInt(commande[3]);
		ObjetGraphique temp;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				temp.statistique.giveExp(exper);
			}
		}
	}
	public void GiveExpTarget( String[] commande ){
		int exper=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.giveExp(exper);
	}
	public void GiveExpSource( String[] commande ){
		int exper=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.giveExp(exper);
	}
	public void DrainExp( String[] commande ){
		int min=Integer.parseInt(commande[1]);
		int max=Integer.parseInt(commande[2]);
		int factor=Integer.parseInt(commande[3]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(max-min)+min+1;
		
		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.tgt, true );
		
		if(factor<0)
			FenetreTest.instance.src.statistique.giveExp(valeur/(-factor));
		else
			FenetreTest.instance.src.statistique.giveExp(valeur*factor);
		FenetreTest.instance.tgt.statistique.giveExp(-valeur);
		if(FenetreTest.instance.tgt.statistique.experience<1)
			FenetreTest.instance.tgt.statistique.experience=1;
	}
	public void DrainHp( String[] commande ){
		int min=Integer.parseInt(commande[1]);
		int max=Integer.parseInt(commande[2]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(max-min)+min+1;
		
		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.tgt, true );
		
		if(FenetreTest.instance.tgt.statistique.pv+1<valeur){
			valeur=FenetreTest.instance.tgt.statistique.pv-1;
		}
		FenetreTest.instance.tgt.statistique.pv-=valeur;
		FenetreTest.instance.src.statistique.pv+=valeur;
		if(FenetreTest.instance.src.statistique.pv>FenetreTest.instance.src.statistique.pvMax)
			FenetreTest.instance.src.statistique.pv=FenetreTest.instance.src.statistique.pvMax;
	}
	public void DrainMp( String[] commande ){
		int min=Integer.parseInt(commande[1]);
		int max=Integer.parseInt(commande[2]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(max-min)+min+1;

		FenetreTest.instance.cancelWalkTo( FenetreTest.instance.tgt, true );
		
		if(FenetreTest.instance.tgt.statistique.pm+1<valeur){
			valeur=FenetreTest.instance.tgt.statistique.pm-1;
		}
		FenetreTest.instance.tgt.statistique.pm-=valeur;
		FenetreTest.instance.src.statistique.pm+=valeur;
		if(FenetreTest.instance.src.statistique.pm>FenetreTest.instance.src.statistique.pmMax)
			FenetreTest.instance.src.statistique.pm=FenetreTest.instance.src.statistique.pmMax;
	}
	public void IsTargetFull( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.victimes.size()>0)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void cIsSourceFull( String[] commande ){
		// Typo alias
		IsSourceFull(commande);
	}
	public void IsSourceFull( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.victimes.size()>0)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsFull( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		int statID=Integer.parseInt(commande[4]);
		int graphID=Integer.parseInt(commande[3]);
		ObjetGraphique temp=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
				break;
			}
		}
		if(temp!=null && temp.statistique.victimes.size()>0)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceDigestingTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.maitre==FenetreTest.instance.src.statistique)
			 FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetDigestingSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.maitre==FenetreTest.instance.src.statistique)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsDigesting( String[] commande ){
		int ev1=Integer.parseInt(commande[5]);
		int ev2=Integer.parseInt(commande[6]);
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		int stat2ID=Integer.parseInt(commande[4]);
		int graph2ID=Integer.parseInt(commande[3]);
		ObjetGraphique temp=null, temp2=null;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
			}
		}
		if(temp!=null){
			for(int k=0; k<temp.statistique.victimes.size();  k++  ){
				temp2=((VHold)temp.statistique.victimes.elementAt(k)).victime.mere;
				if(temp2!=null && temp2.carID==graph2ID && temp2.statistique.carID==stat2ID){
					FenetreTest.instance.executeScript(ev1);
					break;
				}
				temp2=null;
			}
			if(temp2==null)
				FenetreTest.instance.executeScript(ev2);
		}
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceGraphic( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[3]);
		if(FenetreTest.instance.src.carID==graphID)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
		
	}
	public void IsTargetGraphic( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[3]);
		if(FenetreTest.instance.tgt.carID==graphID)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
		
	}
	public void KillWoundedTarget( String[] commande ){
		if(FenetreTest.instance.tgt.statistique.pv<1 && FenetreTest.instance.tgt.statistique.vOnly!=1)
			FenetreTest.instance.tgt.statistique.mort(FenetreTest.instance.src.statistique);
	}
	public void VerifyHPTarget( String[] commande ){
		if(FenetreTest.instance.tgt.statistique.pv>=Integer.parseInt(commande[1]))
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyHPSource( String[] commande ){
		if(FenetreTest.instance.src.statistique.pv>=Integer.parseInt(commande[1]))
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyMPTarget( String[] commande ){
		if(FenetreTest.instance.tgt.statistique.pm>=Integer.parseInt(commande[1]))
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void VerifyMPSource( String[] commande ){
		if(FenetreTest.instance.src.statistique.pm>=Integer.parseInt(commande[1]))
			FenetreTest.instance.executeScript(Integer.parseInt(commande[2]));
		else
			FenetreTest.instance.executeScript(Integer.parseInt(commande[3]));
	}
	public void AddEventCharTarget( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.nouveauEvent(commande[1]+"X",FenetreTest.instance.tgt.getX());
			temp.statistique.nouveauEvent(commande[1]+"Y",FenetreTest.instance.tgt.getY());
		}
	}
	 public void AddEventCharSource( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.nouveauEvent(commande[1]+"X",FenetreTest.instance.src.getX());
			temp.statistique.nouveauEvent(commande[1]+"Y",FenetreTest.instance.src.getY());
		}
	}
	public void AddEventChar( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		ObjetGraphique temp2=null;
		int statID=Integer.parseInt(commande[3]);
		int graphID=Integer.parseInt(commande[2]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			if( FenetreTest.instance.objetG.elementAt(k).carID==graphID && FenetreTest.instance.objetG.elementAt(k).statistique.carID==statID){
				temp=FenetreTest.instance.objetG.elementAt(k);
			}
		}
		if(temp!=null && temp2!=null){
			temp.statistique.nouveauEvent(commande[1]+"X",temp2.getX());
			temp.statistique.nouveauEvent(commande[1]+"Y",temp2.getY());
		}
	}
	public void SetTargetEventChar( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		ObjetGraphique temp2=null;
		if(temp!=null){
			Evenement ev1=temp.statistique.getEvenement(commande[1]+"X");
			Evenement ev2=temp.statistique.getEvenement(commande[1]+"Y");
			if(ev1!=null && ev2!=null){
				for(int k=0;k<FenetreTest.instance.objetG.size();k++){
					temp2=FenetreTest.instance.objetG.elementAt(k);
					if(temp2.getX()==ev1.compteur && temp2.getY()==ev2.compteur){
						FenetreTest.instance.tgt=temp2;
						break;
					}
				}
			}
		}
	}
	public void SetSourceEventChar( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		ObjetGraphique temp2=null;
		if(temp!=null){
			Evenement ev1=temp.statistique.getEvenement(commande[1]+"X");
			Evenement ev2=temp.statistique.getEvenement(commande[1]+"Y");
			if(ev1!=null && ev2!=null){
				for(int k=0;k<FenetreTest.instance.objetG.size();k++){
					temp2=FenetreTest.instance.objetG.elementAt(k);
					if(temp2.getX()==ev1.compteur && temp2.getY()==ev2.compteur){
						FenetreTest.instance.src=temp2;
						break;
					}
				}
			}
		}
	}
	public void RemoveEventChar( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null){
			temp.statistique.removeEvent(commande[1]+"X");
			temp.statistique.removeEvent(commande[1]+"Y");
		}           
	}
	public void IsTargetSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt==FenetreTest.instance.src)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyMap( String[] commande ){
		int m=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		if(m==FenetreTest.instance.map.m_id)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void AddBonusTwilightTarget( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int bn1=Integer.parseInt(commande[1]);
		int bn2=Integer.parseInt(commande[2]);
		int t, to=0;
		if(temp!=null){
			PlayerStat pla=(PlayerStat)temp.statistique;
			if(pla.clock.isNight()){
				FenetreTest.instance.tgt.statistique.bonus.add(new Bonus(bn2,FenetreTest.instance.tgt.statistique,pla.clock.getTimeUntilDawn()));
			}
			else{
				FenetreTest.instance.tgt.statistique.bonus.add(new Bonus(bn1,FenetreTest.instance.tgt.statistique,pla.clock.getTimeUntilDusk()));
			}
		}
	}
	 public void AddBonusTwilightSource( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int bn1=Integer.parseInt(commande[1]);
		int bn2=Integer.parseInt(commande[2]);
		int t, to=0;
		if(temp!=null){
			PlayerStat pla=(PlayerStat)temp.statistique;
			if(pla.clock.isNight()){
				FenetreTest.instance.src.statistique.bonus.add(new Bonus(bn2,FenetreTest.instance.src.statistique,pla.clock.getTimeUntilDawn()));
			}
			else{
				FenetreTest.instance.src.statistique.bonus.add(new Bonus(bn1,FenetreTest.instance.src.statistique,pla.clock.getTimeUntilDusk()));
			}
		}
	}
	public void VerifyDistance( String[] commande ){
		int dis=Integer.parseInt(commande[1]);
		int typ=Integer.parseInt(commande[2]);
		int ev1=Integer.parseInt(commande[3]);
		int ev2=Integer.parseInt(commande[4]);
		double res=FenetreTest.instance.calculDistance(FenetreTest.instance.tgt,FenetreTest.instance.src);
		boolean test=false;
		switch(typ){
			case 0:
				if(res<=(double)dis)
					test=true;
				break;
			case 1:
				if(res>=(double)dis)
					test=true;
				break;
		}
		if(test)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void ChangePoopingScriptTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.pooping=Integer.parseInt(commande[1]);
	}
	public void ChangePoopingScriptSource( String[] commande ){
		FenetreTest.instance.src.statistique.pooping=Integer.parseInt(commande[1]);
	}
	public void ChangeDeathScriptTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.pooping=Integer.parseInt(commande[1]);
	}
	public void ChangeDeathScriptSource( String[] commande ){
		FenetreTest.instance.src.statistique.pooping=Integer.parseInt(commande[1]);
	}
	public void RemoveCharmSource( String[] commande ){
		FenetreTest.instance.src.statistique.actualSide=FenetreTest.instance.src.statistique.side;
	}
	public void RemoveCharmTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.actualSide=FenetreTest.instance.tgt.statistique.side;
	}
	public void IsSourcePlayer( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src!=null && FenetreTest.instance.src.type==FenetreTest.PLAYER)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetPlayer( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt!=null && FenetreTest.instance.tgt.type==FenetreTest.PLAYER)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceAlive( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src!=null && FenetreTest.instance.src.statistique.pv>-999)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetAlive( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt!=null && FenetreTest.instance.tgt.statistique.pv>-999)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	 public void IsSourceEnnemiTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.determinationEnnemi(FenetreTest.instance.src,FenetreTest.instance.tgt))
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void CheckAttackSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(20)+1+FenetreTest.instance.src.statistique.baseAttaque()+FenetreTest.instance.src.statistique.attackBonus;
		if(valeur>=FenetreTest.instance.tgt.statistique.getDefense())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void CheckAttackTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(20)+1+FenetreTest.instance.tgt.statistique.baseAttaque()+FenetreTest.instance.tgt.statistique.attackBonus;
		if(valeur>=FenetreTest.instance.src.statistique.getDefense())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void AttackDamageTarget( String[] commande ){
		int att=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.appliquerDommage(FenetreTest.instance.src.statistique.faireDommage(att), FenetreTest.instance.src.statistique.typeDommage(att));
		if(FenetreTest.instance.tgt.statistique.pv<1 && (FenetreTest.instance.tgt.statistique.vOnly!=1 || FenetreTest.instance.src.statistique.typeDommage(att)==Util.DAMAGE_ACID  ) ){
			FenetreTest.instance.tgt.statistique.mort(FenetreTest.instance.src.statistique);
		}
	}
	public void AttackDamageSource( String[] commande ){
		int att=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.appliquerDommage(FenetreTest.instance.tgt.statistique.faireDommage(att), FenetreTest.instance.tgt.statistique.typeDommage(att));
		if(FenetreTest.instance.src.statistique.pv<1 && (FenetreTest.instance.src.statistique.vOnly!=1 || FenetreTest.instance.tgt.statistique.typeDommage(att)==Util.DAMAGE_ACID  ) ){
			FenetreTest.instance.src.statistique.mort(FenetreTest.instance.src.statistique);
		}
	}
	public void VerifyMapRange( String[] commande ){
		int m1=Integer.parseInt(commande[1]);
		int m2=Integer.parseInt(commande[2]);
		int ev1=Integer.parseInt(commande[3]);
		int ev2=Integer.parseInt(commande[4]);
		if(FenetreTest.instance.map.m_id>=m1 && FenetreTest.instance.map.m_id<=m2)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetID( String[] commande ){
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[3]);
		int ev2=Integer.parseInt(commande[4]);
		if(FenetreTest.instance.tgt!=null && FenetreTest.instance.tgt.statistique.carID==statID && FenetreTest.instance.tgt.carID==graphID)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceID( String[] commande ){
		int statID=Integer.parseInt(commande[2]);
		int graphID=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[3]);
		int ev2=Integer.parseInt(commande[4]);
		if(FenetreTest.instance.src!=null && FenetreTest.instance.src.statistique.carID==statID && FenetreTest.instance.src.carID==graphID)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void RemoveTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.mort(null);
		FenetreTest.instance.repaintgame();
	}
	public void RemoveSource( String[] commande ){
		FenetreTest.instance.src.statistique.mort(null);
		FenetreTest.instance.repaintgame();
	}
	public void IsSourcePolymorphed( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.polymorph!=0)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetPolymorphed( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.polymorph!=0)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void SetTargetLast( String[] commande ){
		FenetreTest.instance.tgt=FenetreTest.instance.objetG.elementAt(FenetreTest.instance.objetG.size()-1);
	}
	public void SetSourceLast( String[] commande ){
		FenetreTest.instance.src=FenetreTest.instance.objetG.elementAt(FenetreTest.instance.objetG.size()-1);
	}
	public void SetSourePlayer( String[] commande ){
		// Typo alias
		SetSourcePlayer(commande);
	}
	public void SetSourcePlayer( String[] commande ){
		FenetreTest.instance.src=FenetreTest.instance.getPlayer();   
	}
	public void SetTargetPlayer( String[] commande ){
		FenetreTest.instance.tgt=FenetreTest.instance.getPlayer();   
	}
	public void SetHpTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.pv=ev1;   
	}
	public void SetHpSouce( String[] commande ){
		// Typo alias
		SetHpSource(commande);
	}
	public void SetHpSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.pv=ev1;   
	}
	public void AnimateTarget( String[] commande ){
		int modeDebut=Integer.parseInt(commande[1]);
		int modeFin=Integer.parseInt(commande[2]);
		int normal=Integer.parseInt(commande[3]);
		boolean norm=false;
		if(normal>0)
			norm=true;
		if(FenetreTest.instance.tgt!=null && FenetreTest.instance.tgt.type==FenetreTest.PLAYER){
			FenetreTest.instance.animationScript(FenetreTest.instance.tgt, modeDebut, modeFin, norm, true);
		}
		else{
			if(FenetreTest.instance.tgt!=null){
			  FenetreTest.instance.animationScript(FenetreTest.instance.tgt, modeDebut, modeFin, norm, false);  
			}
		}
	}
	public void AnimateSource( String[] commande ){
		int modeDebut=Integer.parseInt(commande[1]);
		int modeFin=Integer.parseInt(commande[2]);
		int normal=Integer.parseInt(commande[3]);
		boolean norm=false;
		if(normal>0)
			norm=true;
		if(FenetreTest.instance.src!=null && FenetreTest.instance.src.type==FenetreTest.PLAYER){
			FenetreTest.instance.animationScript(FenetreTest.instance.src, modeDebut, modeFin, norm, true);
		}
		else{
			if(FenetreTest.instance.src!=null){
			  FenetreTest.instance.animationScript(FenetreTest.instance.src, modeDebut, modeFin, norm, false);  
			}
		}
	}
	public void PlayCreatureSoundSource( String[] commande ){
		int ty=Integer.parseInt(commande[1]);
		FenetreTest.instance.jouerSon(ty, FenetreTest.instance.src);
	}
	public void PlayCreatureSoundTarget( String[] commande ){
		int ty=Integer.parseInt(commande[1]);
		FenetreTest.instance.jouerSon(ty, FenetreTest.instance.tgt);
	}
	public void VoreAttack( String[] commande ){
		int bon=Integer.parseInt(commande[2]);
		int att=Integer.parseInt(commande[1]);
		Random hasard=new Random();
		int natt=hasard.nextInt(20)+1;
		int ndef=hasard.nextInt(20)+1;
		if(natt+FenetreTest.instance.src.statistique.getVAttaque(att-1)+bon>= ndef+FenetreTest.instance.tgt.statistique.getVDefense()){
			FenetreTest.instance.tgt.setVisible(false);
			FenetreTest.instance.tgt.statistique.maitre=FenetreTest.instance.src.statistique;
			AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonV) ;
			FenetreTest.instance.src.modeAffichage=3;
			FenetreTest.instance.src.statistique.victimes.add(new VHold(FenetreTest.instance.tgt.statistique, FenetreTest.instance.src.statistique));
			if(FenetreTest.instance.src!=null && FenetreTest.instance.src.type==FenetreTest.PLAYER){
				FenetreTest.instance.animationScript(FenetreTest.instance.src,6, 3, true, true);
			}
			else{
				if(FenetreTest.instance.src!=null){
					FenetreTest.instance.animationScript(FenetreTest.instance.src, 6, 3, true, false);  
				}
			}
			FenetreTest.instance.repaint();
		}
	}
	public void Pause( String[] commande ){
		int del=Integer.parseInt(commande[1]);
		FenetreTest.instance.pauseFor( del );
	}
	public void MoveTargetToSource( String[] commande ){
		FenetreTest.instance.reaparitionObjetGraphique( FenetreTest.instance.tgt, FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY());
	}
	public void MoveSourceToTarget( String[] commande ){
		FenetreTest.instance.reaparitionObjetGraphique(FenetreTest.instance.src, FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY());
	}
	public void AffectRandomEnnemi( String[] commande ){
		int essai=0;
		int maxennemi=0;
		int ci;
		int compteur;
		boolean reussi=false;
		Random hasard=new Random();
		int scriptID=Integer.parseInt(commande[1]);
		int range=Integer.parseInt(commande[2]);
		String originType=commande[3];
		ObjetGraphique temp, source, target,origine;
		source=FenetreTest.instance.src;
		target=FenetreTest.instance.tgt;
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(originType.equalsIgnoreCase("target"))
				origine=FenetreTest.instance.tgt;
			else if(originType.equalsIgnoreCase("source"))
				origine=FenetreTest.instance.src;
			else if(originType.equalsIgnoreCase("firstSource"))
				origine=source;
			else if(originType.equalsIgnoreCase("firstTarget"))
				origine=target;
			else if(originType.equalsIgnoreCase("player"))
				origine=FenetreTest.instance.getPlayer();
			else
				origine=source;
			if(FenetreTest.instance.determinationEnnemi(origine, k) && temp.statistique.maitre ==null && temp.statistique.pv>-999 && origine!=temp){
				if(range<1){
					maxennemi++;
				}
				else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
					maxennemi++;
				}
			}
		}

		while(!reussi && essai<maxennemi){
			essai++;
			ci=hasard.nextInt(maxennemi)+1;
			compteur=1;
			source=FenetreTest.instance.src;
			target=FenetreTest.instance.tgt;
			for(int k=0;k<FenetreTest.instance.objetG.size();k++){
				temp=FenetreTest.instance.objetG.elementAt(k);

				if(originType.equalsIgnoreCase("target"))
					origine=FenetreTest.instance.tgt;
				else if(originType.equalsIgnoreCase("source"))
					origine=FenetreTest.instance.src;
				else if(originType.equalsIgnoreCase("firstSource"))
					origine=source;
				else if(originType.equalsIgnoreCase("firstTarget"))
					origine=target;
				else if(originType.equalsIgnoreCase("player"))
					origine=FenetreTest.instance.getPlayer();
				else
					origine=source;
				if(FenetreTest.instance.determinationEnnemi(origine, temp) && temp.statistique.maitre ==null && temp.statistique.pv>-999 && origine!=temp){
					if(range<1){
						FenetreTest.instance.tgt=temp;
						if(compteur==ci){
							FenetreTest.instance.executeScript(scriptID);
							reussi=true;
							break;
						}
						else
							compteur++;
					}
					else if(range>=FenetreTest.instance.calculDistance(temp, origine) && FenetreTest.instance.cibleBloquee(temp.getX(), temp.getY(), origine.getX(), temp.getY())==null){
						FenetreTest.instance.tgt=temp;
					   if(compteur==ci){
							FenetreTest.instance.executeScript(scriptID);
							reussi=true;
							break;
						}
						else
							compteur++;
					}

					if(temp.statistique.pv<1 && temp.statistique.vOnly!=1){
						if(originType.equalsIgnoreCase("target"))
							temp.statistique.mort(FenetreTest.instance.tgt.statistique);
						else if(originType.equalsIgnoreCase("source"))
							temp.statistique.mort(FenetreTest.instance.src.statistique);
						else if(originType.equalsIgnoreCase("firstSource"))
							temp.statistique.mort(source.statistique);
						else if(originType.equalsIgnoreCase("firstTarget"))
							temp.statistique.mort(target.statistique);
						else if(originType.equalsIgnoreCase("player"))
							temp.statistique.mort(origine.statistique);
					}
				}
			}
		}
		FenetreTest.instance.tgt=target;
		FenetreTest.instance.src=source;
   }
   public void RepeatScript( String[] commande ){
		int scriptID=Integer.parseInt(commande[2]);
		int nbr=Integer.parseInt(commande[1]);
		for(int k=0;k<nbr;k++){
			FenetreTest.instance.executeScript(scriptID);
		}
   }
   public void ChangeTriggerScriptSource( String[] commande ){
		int script2=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.eventID=script2;
   }
   public void ChangeTriggerScriptTarget( String[] commande ){
		int script2=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.eventID=script2;
   }
   public void IsSourceFollower( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.type==FenetreTest.NPC)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);        
   }
   public void IsTargetFollower( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.type==FenetreTest.NPC)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);        
   }
   public void IsPlayerAlive( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(temp!=null)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);        
   }
   public void IsStomachFull( String[] commande ){
		VHold temp;
		boolean trouve;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		trouve=false;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(!temp.isUnbirth() && temp.digTime<=FenetreTest.instance.src.statistique.digTimeMax/2){
				trouve=true;
				break;
			}
		}
		if(trouve)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2); 
	}
	public void IsIntestineFull( String[] commande ){
		VHold temp;
		boolean trouve;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		trouve=false;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(!temp.isUnbirth() && temp.digTime>FenetreTest.instance.src.statistique.digTimeMax/2){
				trouve=true;
				break;
			}
		}
		if(trouve)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2); 
	}
	public void IsUnbirth( String[] commande ){
		VHold temp;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		temp=FenetreTest.instance.src.statistique.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.isUnbirth())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsAnalVoring( String[] commande ){
		VHold temp;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		temp=FenetreTest.instance.src.statistique.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.getVore()==VHold.ANAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsOralVore( String[] commande ){
		VHold temp;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		temp=FenetreTest.instance.src.statistique.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.getVore()==VHold.ORAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void SetPoopPropTarget( String[] commande ){
		int poop=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.dung=poop;
	}
	public void SetPoopPropSource( String[] commande ){
		int poop=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.dung=poop;
	}
	 public void SetPoop2PropTarget( String[] commande ){
		int poop=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.dung2=poop;
	}
	public void SetPoopProp2Source( String[] commande ){
		// Typo alias
		SetPoop2PropSource(commande);
	}
	public void SetPoop2PropSource( String[] commande ){
		int poop=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.dung2=poop;
	}
	public void IsPlayerWearArmor( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null && ((PlayerStat)temp.statistique).armure!=null)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsTargetSwallowable( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.vOnly!=2)
			 FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void IsSourceSwallowable( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.vOnly!=2)
			 FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void SetTargetPred( String[] commande ){
		FenetreTest.instance.tgt=FenetreTest.instance.src.statistique.maitre.mere;
	}
	public void SetSourcePred( String[] commande ){
		FenetreTest.instance.src=FenetreTest.instance.tgt.statistique.maitre.mere;
	}
	public void AddCaracterSourceSwallowed( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]), FenetreTest.instance.src.statistique,VHold.ORAL );
	}
	public void AddCaracterTargetSwallowed( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]),FenetreTest.instance.tgt.statistique,VHold.ORAL ); 
	}
	public void AddCaracterSourceUnbirthed( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]), FenetreTest.instance.src.statistique,VHold.UNBIRTH );
	}
	public void AddCaracterTargetUnbirthed( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]),FenetreTest.instance.tgt.statistique,VHold.UNBIRTH ); 
	}
	public void AddCaracterSourceAnalVoring( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]), FenetreTest.instance.src.statistique,VHold.ANAL );
	}
	public void AddCaracterTargetAnalVoring( String[] commande ){
		FenetreTest.instance.ajouterObjetGraphiqueVored(Integer.parseInt(commande[1]),FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY(), Integer.parseInt(commande[3]), Integer.parseInt(commande[4]), Integer.parseInt(commande[6]), FenetreTest.MONSTER, Integer.parseInt(commande[5]), Integer.parseInt(commande[2]), Integer.parseInt(commande[7]),FenetreTest.instance.tgt.statistique,VHold.ANAL ); 
	}
	public void SetTargetRandom( String[] commande ){
		Random hasard=new Random();
		int nombre=hasard.nextInt(FenetreTest.instance.objetG.size());
		FenetreTest.instance.tgt=FenetreTest.instance.objetG.elementAt(nombre);
	}
	public void SetSourceRandom( String[] commande ){
		Random hasard=new Random();
		int nombre=hasard.nextInt(FenetreTest.instance.objetG.size());
		FenetreTest.instance.src=FenetreTest.instance.objetG.elementAt(nombre);
	}
	public void VerifyPlayerArmor( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null && ((PlayerStat)temp.statistique).armure!=null && ((PlayerStat)temp.statistique).armure.objetID==arm)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyPlayerWeapon( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null && ((PlayerStat)temp.statistique).arme!=null && ((PlayerStat)temp.statistique).arme.objetID==arm)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyPlayerRing( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null && ((PlayerStat)temp.statistique).bague!=null && ((PlayerStat)temp.statistique).bague.objetID==arm)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyPlayerAmulet( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		if(temp!=null && ((PlayerStat)temp.statistique).amulet!=null && ((PlayerStat)temp.statistique).amulet.objetID==arm)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyBonusTarget( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		Bonus  temp;
		boolean ok=false;
		for(int k=0;k<FenetreTest.instance.tgt.statistique.bonus.size();k++){
			temp=(Bonus)FenetreTest.instance.tgt.statistique.bonus.elementAt(k);
			if(temp.bonusID==arm){
				ok=true;
				break;
			}
		}
		if(ok)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void VerifyBonusSource( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int ev1=Integer.parseInt(commande[2]);
		int ev2=Integer.parseInt(commande[3]);
		Bonus  temp;
		boolean ok=false;
		for(int k=0;k<FenetreTest.instance.src.statistique.bonus.size();k++){
			temp=(Bonus)FenetreTest.instance.src.statistique.bonus.elementAt(k);
			if(temp.bonusID==arm){
				ok=true;
				break;
			}
		}
		if(ok)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void ChangeAttackSource( String[] commande ){
		int stat=Integer.parseInt(commande[1]);
		FenetreTest.instance.src.statistique.changeAttaque(stat);
	}
	public void ChangeAttackTarget( String[] commande ){
		int stat=Integer.parseInt(commande[1]);
		FenetreTest.instance.tgt.statistique.changeAttaque(stat);
	}
	public void RestoreAttackTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.restoreAttaque();
	}
	public void RestoreAttackSource( String[] commande ){
		FenetreTest.instance.src.statistique.restoreAttaque();
	}
	public void RemoveBonusSource( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int mode=Integer.parseInt(commande[2]);
		Bonus  temp;
		for(int k=0;k<FenetreTest.instance.src.statistique.bonus.size();k++){
			temp=(Bonus)FenetreTest.instance.src.statistique.bonus.elementAt(k);
			if(temp.bonusID==arm){
			   if(mode==0)
					temp.enleverBonus();
			   else
					temp.interruptBonus();
			   FenetreTest.instance.src.statistique.bonus.remove(k);
			   k--;
			}
		}
	}
	public void RemoveBonusTarget( String[] commande ){
		int arm=Integer.parseInt(commande[1]);
		int mode=Integer.parseInt(commande[2]);
		Bonus  temp;
		for(int k=0;k<FenetreTest.instance.tgt.statistique.bonus.size();k++){
			temp=(Bonus)FenetreTest.instance.tgt.statistique.bonus.elementAt(k);
			if(temp.bonusID==arm){
			   if(mode==0)
					temp.enleverBonus();
			   else
					temp.interruptBonus();
			   FenetreTest.instance.tgt.statistique.bonus.remove(k);
			   k--;
			}
		}
	}
	public void ChangeNameTarget( String[] commande ){
		FenetreTest.instance.tgt.statistique.setName( commande[1] );
	}
	public void ChangeNameSource( String[] commande ){
		FenetreTest.instance.src.statistique.setName( commande[1] );
	}
	public void AddCaracterPosition( String[] commande ){
		String positionX,positionY;
		positionX=commande[3];
		positionY=commande[4];
		int posX,posY;
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		posX=((PlayerStat)temp.statistique).getEvenement(positionX).compteur;
		posY=((PlayerStat)temp.statistique).getEvenement(positionY).compteur;
		FenetreTest.instance.ajouterObjetGraphique(Integer.parseInt(commande[1]), posX, posY, Integer.parseInt(commande[5]), Integer.parseInt(commande[6]), Integer.parseInt(commande[8]), FenetreTest.MONSTER, Integer.parseInt(commande[7]), Integer.parseInt(commande[2]), Integer.parseInt(commande[9]) );
	}
	public void ChangeTiltGraphRandom( String[] commande ){
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		String graph=commande[5];
		Random hasard=new Random();
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.map.getTile(posX,posY).m_img = ImageLibrairies.getImage( graph );
	}
	public void PickOne( String[] commande ){
		Random hasard=new Random();
		int nbChoix=commande.length-1;
		int choix=hasard.nextInt(nbChoix)+1;
		FenetreTest.instance.executeScript(Integer.parseInt(commande[choix]));  
	}
	public void AddPropRandom( String[] commande ){
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		int propID=Integer.parseInt(commande[5]);
		Random hasard=new Random();
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.AddProp(propID, posX, posY);
	}
	public void VerifyListMap( String[] commande ){
		boolean ok=false;
		for(int k=2;k<commande.length;k+=2){
			if(FenetreTest.instance.map.m_id==Integer.parseInt(commande[k])){
				FenetreTest.instance.executeScript(Integer.parseInt(commande[k+1]));
				ok=true;
				break;
			}
		}
		if(!ok){
			FenetreTest.instance.executeScript(Integer.parseInt(commande[1]));
		}   
	}
	public void PickOneEvent( String[] commande ){
		boolean ok=false;
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int nbChoix=commande.length/2-1;
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[1]).compteur;
		int defaultEvent=Integer.parseInt(commande[2]);
		for(int k=3;k<commande.length;k+=2){
			if(evt==Integer.parseInt(commande[k])){
				FenetreTest.instance.executeScript(Integer.parseInt(commande[k+1]));
				ok=true;
				break;
			}
		}
		if(!ok){
			FenetreTest.instance.executeScript(defaultEvent);
		}   
	}
	public void AddPropRandomMap( String[] commande ){
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		int propID=Integer.parseInt(commande[5]);
		int modif=Integer.parseInt(commande[6]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.AddProp(propID, posX, posY);
	}
	public void ChangeTiltGraphRandomMap( String[] commande ){
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		String graph=commande[5];
		int modif=Integer.parseInt(commande[6]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.map.getTile(posX,posY).m_img = ImageLibrairies.getImage( graph );
	}
	public void AddPropRandomEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		int propID=Integer.parseInt(commande[5]);
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		Random hasard=new Random(evt*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.AddProp(propID, posX, posY);
	}
	public void ChangeTiltGraphRandomEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		String graph=commande[5];
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		Random hasard=new Random(evt*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.map.getTile(posX,posY).m_img = ImageLibrairies.getImage(graph);
	}
	public void CRepeatScriptEvent( String[] commande ){
		// There's a typo in the data somewhere...
		RepeatScriptEvent( commande );
	}
	public void RepeatScriptEvent( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int scriptID=Integer.parseInt(commande[2]);
		int nbr=Integer.parseInt(commande[1]);
		Evenement evt=((PlayerStat)temp.statistique).getEvenement(commande[3]);
		for(int k=0;k<nbr;k++){
			FenetreTest.instance.executeScript(scriptID);
			evt.compteur++;
		}
	}
	public void AddPropRandomEventMap( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		int propID=Integer.parseInt(commande[5]);
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*evt*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.AddProp(propID, posX, posY);
	}
	public void ChangeTiltGraphRandomEventMap( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		String graph=commande[5];
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*evt*modif);
		int posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
		int posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
		FenetreTest.instance.map.getTile(posX,posY).m_img = ImageLibrairies.getImage(graph);
	}
	public void RepeatAddPropRandomEventMap( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		int propID=Integer.parseInt(commande[5]);
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		int nombre=Integer.parseInt(commande[8]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*evt*modif);
		int posX;
		int posY;
		for(int k=0;k<nombre;k++){
			posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
			posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
			FenetreTest.instance.AddProp(propID, posX, posY);
			evt++;
		}
	}
	public void RepeatChangeTiltGraphRandomEventMap( String[] commande ){
		ObjetGraphique temp=FenetreTest.instance.getPlayer();
		int minimumX=Integer.parseInt(commande[1]);
		int maximumX=Integer.parseInt(commande[2]);
		int minimumY=Integer.parseInt(commande[3]);
		int maximumY=Integer.parseInt(commande[4]);
		String graph=commande[5];
		int evt=((PlayerStat)temp.statistique).getEvenement(commande[6]).compteur;
		int modif=Integer.parseInt(commande[7]);
		int nombre=Integer.parseInt(commande[8]);
		Random hasard=new Random(FenetreTest.instance.map.m_id*evt*modif);
		int posX;
		int posY;
		for(int k=0;k<nombre;k++){
			posX=hasard.nextInt(maximumX-minimumX)+minimumX+1;
			posY=hasard.nextInt(maximumY-minimumY)+minimumY+1;
			FenetreTest.instance.map.getTile(posX,posY).m_img = ImageLibrairies.getImage(graph);
			evt++;
		}
	}
	public void UnbirthTarget( String[] commande ){
		FenetreTest.instance.tgt.setVisible(false);
		FenetreTest.instance.tgt.statistique.maitre=FenetreTest.instance.src.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonU) ;
		FenetreTest.instance.src.modeAffichage=3;
		FenetreTest.instance.src.statistique.victimes.add(new VHold(FenetreTest.instance.tgt.statistique, FenetreTest.instance.src.statistique,VHold.UNBIRTH,true));
		FenetreTest.instance.repaintgame();
	}
	public void UnbirthSource( String[] commande ){
		FenetreTest.instance.src.setVisible(false);
		FenetreTest.instance.src.statistique.maitre=FenetreTest.instance.tgt.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonU) ;
		FenetreTest.instance.tgt.modeAffichage=3;
		FenetreTest.instance.tgt.statistique.victimes.add(new VHold(FenetreTest.instance.src.statistique, FenetreTest.instance.tgt.statistique,VHold.UNBIRTH,true));
		FenetreTest.instance.repaintgame();
	}
	public void Unbirth( String[] commande ){
		ObjetGraphique temp=null, temp2=null, temp3=null;
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		int v3=Integer.parseInt(commande[3]);
		int v4=Integer.parseInt(commande[4]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==v1 && temp.statistique.carID==v2){
				temp2=temp;
			}
			if(temp.carID==v3 && temp.statistique.carID==v4){
				temp3=temp;
			}
		}
		if(temp2!=null && temp3!=null){
			temp3.setVisible(false);
			temp3.statistique.maitre=temp2.statistique;
			AudioLibrairie.playClip(temp2.statistique.isonU) ;
			temp2.modeAffichage=3;
			temp2.statistique.victimes.add(new VHold(temp3.statistique, temp2.statistique, VHold.UNBIRTH,true));
		}
		FenetreTest.instance.repaintgame();
	}
	public void AnalVoringTarget( String[] commande ){
		FenetreTest.instance.tgt.setVisible(false);
		FenetreTest.instance.tgt.statistique.maitre=FenetreTest.instance.src.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.src.statistique.isonAnl) ;
		FenetreTest.instance.src.modeAffichage=3;
		FenetreTest.instance.src.statistique.victimes.add(new VHold(FenetreTest.instance.tgt.statistique, FenetreTest.instance.src.statistique,VHold.ANAL,true));
		FenetreTest.instance.repaintgame();
	}
	public void AnalVoringSource( String[] commande ){
		FenetreTest.instance.src.setVisible(false);
		FenetreTest.instance.src.statistique.maitre=FenetreTest.instance.tgt.statistique;
		AudioLibrairie.playClip(FenetreTest.instance.tgt.statistique.isonAnl) ;
		FenetreTest.instance.tgt.modeAffichage=3;
		FenetreTest.instance.tgt.statistique.victimes.add(new VHold(FenetreTest.instance.src.statistique, FenetreTest.instance.tgt.statistique,VHold.ANAL,true));
		FenetreTest.instance.repaintgame();
	}
	public void AnalVoring( String[] commande ){
		ObjetGraphique temp=null, temp2=null, temp3=null;
		int v1=Integer.parseInt(commande[1]);
		int v2=Integer.parseInt(commande[2]);
		int v3=Integer.parseInt(commande[3]);
		int v4=Integer.parseInt(commande[4]);
		for(int k=0;k<FenetreTest.instance.objetG.size();k++){
			temp=FenetreTest.instance.objetG.elementAt(k);
			if(temp.carID==v1 && temp.statistique.carID==v2){
				temp2=temp;
			}
			if(temp.carID==v3 && temp.statistique.carID==v4){
				temp3=temp;
			}
		}
		if(temp2!=null && temp3!=null){
			temp3.setVisible(false);
			temp3.statistique.maitre=temp2.statistique;
			AudioLibrairie.playClip(temp2.statistique.isonAnl) ;
			temp2.modeAffichage=3;
			temp2.statistique.victimes.add(new VHold(temp3.statistique, temp2.statistique, VHold.ANAL,true));
		}
		FenetreTest.instance.repaintgame();
	}
	public void CheckVoreAttackSource( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		int att=Integer.parseInt(commande[3]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(20)+1+FenetreTest.instance.src.statistique.getVAttaque(att);
		if(valeur>=FenetreTest.instance.tgt.statistique.getVDefense())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	public void CheckVoreAttackTarget( String[] commande ){
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		int att=Integer.parseInt(commande[3]);
		Random hasard=new Random();
		int valeur=hasard.nextInt(20)+1+FenetreTest.instance.tgt.statistique.getVAttaque(att);
		if(valeur>=FenetreTest.instance.src.statistique.getVDefense())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void BirthSource( String[] commande )
	{
		VHold temp;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(temp.isUnbirth())
				temp.digTime+=FenetreTest.instance.src.statistique.digTimeMax;
		}
		FenetreTest.instance.repaintgame();
	}
	
	public void BirthTarget( String[] commande )
	{
		VHold temp;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			if(temp.isUnbirth())
				temp.digTime+=FenetreTest.instance.src.statistique.digTimeMax;
		}
		FenetreTest.instance.repaintgame();
	}
	
	public void EmptySource( String[] commande )
	{
		VHold temp;
		ObjetGraphique victimeTemporaire;
		for(int k=0;k<FenetreTest.instance.src.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.src.statistique.victimes.elementAt(k);
			victimeTemporaire=temp.victime.mere;
			if(FenetreTest.instance.src.statistique.maitre!=null){
				StatChar te=victimeTemporaire.statistique.maitre;

				te.maitre.victimes.add(new VHold(victimeTemporaire.statistique,te.maitre,victimeTemporaire.statistique.maitre.maitre.getVictime(victimeTemporaire.statistique.maitre).getVore(),true));
		
				victimeTemporaire.statistique.maitre.enleverVictime(victimeTemporaire.statistique);
				victimeTemporaire.statistique.maitre=te.maitre;
			}
			else{
				 FenetreTest.instance.reaparitionObjetGraphique(victimeTemporaire, FenetreTest.instance.src.getX(),FenetreTest.instance.src.getY());
				 victimeTemporaire.statistique.maitre=null;
				 victimeTemporaire.enable=1;
				 //victimeTemporaire.changerPosition();
				 victimeTemporaire.setVisible(true);
				 FenetreTest.instance.src.statistique.enleverVictime(victimeTemporaire.statistique);
			}
		}
		FenetreTest.instance.repaintgame();
	}
	
	public void EmptyTarget( String[] commande )
	{
		VHold temp;
		ObjetGraphique victimeTemporaire;
		for(int k=0;k<FenetreTest.instance.tgt.statistique.victimes.size();k++){
			temp=(VHold)FenetreTest.instance.tgt.statistique.victimes.elementAt(k);
			victimeTemporaire=temp.victime.mere;
			if(FenetreTest.instance.tgt.statistique.maitre!=null){
				StatChar te=victimeTemporaire.statistique.maitre;

				te.maitre.victimes.add(new VHold(victimeTemporaire.statistique,te.maitre,victimeTemporaire.statistique.maitre.maitre.getVictime(victimeTemporaire.statistique.maitre).getVore(),true));
		
				victimeTemporaire.statistique.maitre.enleverVictime(victimeTemporaire.statistique);
				victimeTemporaire.statistique.maitre=te.maitre;
			}
			else{
				 FenetreTest.instance.reaparitionObjetGraphique(victimeTemporaire, FenetreTest.instance.tgt.getX(),FenetreTest.instance.tgt.getY());
				 victimeTemporaire.statistique.maitre=null;
				 victimeTemporaire.enable=1;
				 //victimeTemporaire.changerPosition();
				 victimeTemporaire.setVisible(true);
				 FenetreTest.instance.tgt.statistique.enleverVictime(victimeTemporaire.statistique);
			}
		}
		FenetreTest.instance.repaintgame();
	}
	
	public void IsSourceUnbirthed( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.maitre!=null)
			temp=FenetreTest.instance.src.statistique.maitre.getVictime(FenetreTest.instance.src.statistique);
		if(temp!=null && temp.isUnbirth())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void IsSourceAnalVored( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.maitre!=null)
			temp=FenetreTest.instance.src.statistique.maitre.getVictime(FenetreTest.instance.src.statistique);
		if(temp!=null && temp.getVore()==VHold.ANAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void IsSourceSwallowed( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.src.statistique.maitre!=null)
			temp=FenetreTest.instance.src.statistique.maitre.getVictime(FenetreTest.instance.src.statistique);
		if(temp!=null && temp.getVore()==VHold.ORAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void IsTargetUnbirthed( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.maitre!=null)
			temp=FenetreTest.instance.tgt.statistique.maitre.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.isUnbirth())
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void IsTargetAnalVored( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.maitre!=null)
			temp=FenetreTest.instance.tgt.statistique.maitre.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.getVore()==VHold.ANAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void IsTargetSwallowed( String[] commande )
	{
		VHold temp=null;
		int ev1=Integer.parseInt(commande[1]);
		int ev2=Integer.parseInt(commande[2]);
		if(FenetreTest.instance.tgt.statistique.maitre!=null)
			temp=FenetreTest.instance.tgt.statistique.maitre.getVictime(FenetreTest.instance.tgt.statistique);
		if(temp!=null && temp.getVore()==VHold.ORAL)
			FenetreTest.instance.executeScript(ev1);
		else
			FenetreTest.instance.executeScript(ev2);
	}
	
	public void AddPropTarget( String[] commande )
	{
		int propID=Integer.parseInt(commande[1]);
		FenetreTest.instance.AddProp(propID, FenetreTest.instance.tgt.getX(), FenetreTest.instance.tgt.getY());
	}
	
	public void AddPropSource( String[] commande )
	{
		int propID=Integer.parseInt(commande[1]);
		FenetreTest.instance.AddProp(propID, FenetreTest.instance.src.getX(), FenetreTest.instance.src.getY());
	}
	
	public void SaveTargetCarID( String[] commande )
	{
		String ev=commande[1];
		ObjetGraphique temp=null;
		temp=FenetreTest.instance.getPlayer();
		temp.statistique.removeEvent(ev);
		temp.statistique.nouveauEvent(ev,FenetreTest.instance.tgt.carID);
	}
	
	public void SaveSourceCarID( String[] commande )
	{
		String ev=commande[1];
		ObjetGraphique temp=null;
		temp=FenetreTest.instance.getPlayer();
		temp.statistique.removeEvent(ev);
		temp.statistique.nouveauEvent(ev,FenetreTest.instance.src.carID);
	}
		
	public void TransformTargetEvent( String[] commande )
	{
		ObjetGraphique plr=FenetreTest.instance.getPlayer();
		int value=plr.statistique.getEvenement(commande[1]).compteur;
		FenetreTest.instance.tgt.statistique.transform(value);
	}
	
	public void TransformSourceEvent( String[] commande )
	{
		ObjetGraphique plr=FenetreTest.instance.getPlayer();
		int value=plr.statistique.getEvenement(commande[1]).compteur;
		FenetreTest.instance.src.statistique.transform(value);
	}
}