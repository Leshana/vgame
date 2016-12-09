
import java.io.*;
import java.util.*;

class SaveVHold implements Serializable
{
	public int digTime;
	public int vore;
	public int victime; // Need an identifier for a specific savecharacter here.
}

class SaveBonus implements Serializable
{
	public int BonusID;
	public int temps;
	public int tempsMax;
}

class SaveCharacter implements Serializable
{
	// From Caracter
	public int CarID;
	public int PositionX;
	public int PositionY;
	public int EventID;
	public int DeathEventID;
	public boolean Visible;
	public int Enable;
	public int Deplacement;
	public int ModeAffichage;
	public int AttackID;
	public int Type;
	
	// From StatCaracter
	public int StatID;
	public String Nom;
	public int PV;
	public int PM;
	public int Strength;
	public int Intelligence;
	public int Dexterity;
	public int Endurence;
	public int Charisma;
	public int Niveau;
	public int Defense;
	public int[] Resistance;
	public int AcidDamageMin;
	public int AcidDamageMax;
	public int AcidDamageBonus;
	public int VDifficulty;
	public int VEscape;
	public int Status;
	public int digTimeMax;
	public long Experience;
	public int pvMax;
	public int pmMax;
	public int attackBonus;
	public int DamageBonus;
	public int Side;
	public int ActualSide;
	public int vBonus;
	public int Absorption;
	public int VDefense;
	public int Polymorph;
	public int Size;
	public int Regeneration;
	
	// Player specific StatCharacter fields
	public int Temps;
	public int MoneyP;
	public int Nourriture;
	public int famine;
	
	// From VHold
	public Vector<SaveVHold> Victimes;
	
	// From Bonus
	public Vector<SaveBonus> Bonus;
}

class SaveEvent implements Serializable
{
	public int temps;
	public int compteur;
	public String nom;
}

class SaveItem implements Serializable
{
	public int ObjectID;
	public int BonusID;
	public int Type;
	public boolean Equipped;
	public int Valeur;
}

class SaveSpell implements Serializable
{
	public int SpellID;
}

public class SaveData implements Serializable
{
	public int MapID;
	
	public Vector<SaveCharacter> StatChars;
	
	public Vector<SaveEvent> Events;
	public Vector<SaveItem> Items;
	public Vector<SaveSpell> Spells;
	
	public int Vore;
}