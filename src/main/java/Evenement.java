/*
 * Evenement.java
 *
 * Created on 19 janvier 2006, 12:39
 */

/**
 *
 * @author etudiant
 */
public class Evenement {
    public Clock clock;
    public int compteur;
    public String nom;
    
    /** Creates a new instance of Evenement */
    public Evenement(Clock clock, int compteur, String nom) {
		this.clock = new Clock( clock );
        this.compteur=compteur;
        this.nom=nom;
    }
	
	public Evenement(SaveEvent saveEvent)
	{
		clock = new Clock( saveEvent.temps );
		compteur = saveEvent.compteur;
		nom = saveEvent.nom;
	}
    
	public SaveEvent save()
	{
		SaveEvent ret = new SaveEvent();
		
		ret.temps = clock.getTime();
		ret.compteur = compteur;
		ret.nom = nom;
		
		return ret;
	}
}
