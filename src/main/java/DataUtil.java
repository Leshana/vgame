
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class GameData implements Serializable
{
	public Hashtable<Integer,AmuletData> amuletData;
	public Hashtable<Integer,ArmeData> armeData;
	public Hashtable<Integer,ArmureData> armureData;
	public Hashtable<Integer,BagueData> bagueData;
	public Hashtable<Integer,BonusData> bonusData;
	public Hashtable<Integer,EventData> eventData;
	public Hashtable<Integer,FoodData> foodData;
	public Hashtable<Integer,LivreData> livreData;
	public Hashtable<Integer,ObjetGraphiqueData> objetGraphiqueData;
	public Hashtable<Integer,PlayerStatData> playerStatData;
	public Hashtable<Integer,PotionData> potionData;
	public Hashtable<Integer,ProjectileData> projectileData;
	public Hashtable<Integer,QuestData> questData;
	public Hashtable<Integer,SpellData> spellData;
	public Hashtable<Integer,StatCharData> statCharData;
	public Hashtable<Integer,UsableData> usableData;
	
	// Attack is a mapped table, storing a list of attacks per statcar id
	public Hashtable<Integer,Vector<AttackData>> attackData;
	
	// Map data is highly complex and requires specialized export code.
	public Hashtable<Integer,MapData> mapData;
	
	// Prop data is a subset of the props table containing only the props which
	//  aren't tied to maps.
	public Hashtable<Integer,PropData> propData;
}

public class DataUtil
{
	private static GameData gameData;
	
	public static void loadStaticGameData()
	{
                // Try 1 - Load from gamedata folder if it exists.
                Path gdFolder = Paths.get("gamedata");
                try {
                    if (Files.exists(gdFolder)) {
                        DataToJson exporter = new DataToJson(gdFolder);
                        gameData = exporter.loadGameData();
                        return;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
                
                // Try 2 - Load from VGame.dat if it exists
                Path vgameDatFile = Paths.get("VGame.dat");
                String dataFile = vgameDatFile.toString();
                if (Files.exists(vgameDatFile)) {
                    gameData = readObject( dataFile );
                }
		
		if (gameData == null)
		{
			Util.error( "Unable to load base game data - attempting re-export." );
			
			gameData = exportGameData();
			
			if (gameData == null)
			{
				Util.error( "Re-export failed" );
			}
			else
			{
				writeObject( dataFile, gameData );
			}
		}
                
                if (gameData != null) {
                    System.out.println("Exporting VGameDat to gamedata folder");
                    try {
                        Files.createDirectories(gdFolder);
                        DataToJson exporter = new DataToJson(gdFolder);
                        exporter.saveGameData(gameData);
                    } catch (IOException ex) {
                        Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex);
                        System.exit(1);
                    }
                }
		
		/*
		if (gameData != null)
		{
			System.out.println("Loaded game data with: ");
			System.out.println(" " + gameData.amuletData.size() + " Amulets");
			System.out.println(" " + gameData.armeData.size() + " Weapons");
			System.out.println(" " + gameData.armureData.size() + " Armors");
			System.out.println(" " + gameData.bagueData.size() + " Rings");
			System.out.println(" " + gameData.bonusData.size() + " Bonuses");
			System.out.println(" " + gameData.eventData.size() + " Events");
			System.out.println(" " + gameData.foodData.size() + " Food types");
			System.out.println(" " + gameData.livreData.size() + " Books");
			System.out.println(" " + gameData.objetGraphiqueData.size() + " Graphical object types");
			System.out.println(" " + gameData.playerStatData.size() + " Player stat sets");
			System.out.println(" " + gameData.potionData.size() + " Potions");
			System.out.println(" " + gameData.projectileData.size() + " Projectiles");
			System.out.println(" " + gameData.questData.size() + " Quest items");
			System.out.println(" " + gameData.spellData.size() + " Spells");
			System.out.println(" " + gameData.statCharData.size() + " StatChar types");
			System.out.println(" " + gameData.usableData.size() + " Usable items");
			System.out.println(" " + gameData.attackData.size() + " Attack sets");
			System.out.println(" " + gameData.mapData.size() + " Maps");
			System.out.println(" " + gameData.propData.size() + " Props");
		}
		*/
	}
	
	//===================================================================================
	public static AmuletData getAmuletData( int id )
	{
		return gameData.amuletData.get( new Integer( id ) );
	}
	
	public static ArmeData getArmeData( int id )
	{
		return gameData.armeData.get( new Integer( id ) );
	}
	
	public static ArmureData getArmureData( int id )
	{
		return gameData.armureData.get( new Integer( id ) );
	}
	
	public static BagueData getBagueData( int id )
	{
		return gameData.bagueData.get( new Integer( id ) );
	}
	
	public static BonusData getBonusData( int id )
	{
		return gameData.bonusData.get( new Integer( id ) );
	}
	
	public static EventData getEventData( int id )
	{
		return gameData.eventData.get( new Integer( id ) );
	}
	
	public static FoodData getFoodData( int id )
	{
		return gameData.foodData.get( new Integer( id ) );
	}
	
	public static LivreData getLivreData( int id )
	{
		return gameData.livreData.get( new Integer( id ) );
	}
	
	public static ObjetGraphiqueData getObjetGraphiqueData( int id )
	{
		return gameData.objetGraphiqueData.get( new Integer( id ) );
	}
	
	public static PlayerStatData getPlayerStatData( int id )
	{
		return gameData.playerStatData.get( new Integer( id ) );
	}
	
	public static PotionData getPotionData( int id )
	{
		return gameData.potionData.get( new Integer( id ) );
	}
	
	public static ProjectileData getProjectileData( int id )
	{
		return gameData.projectileData.get( new Integer( id ) );
	}
	
	public static QuestData getQuestData( int id )
	{
		return gameData.questData.get( new Integer( id ) );
	}
	
	public static SpellData getSpellData( int id )
	{
		return gameData.spellData.get( new Integer( id ) );
	}
	
	public static StatCharData getStatCharData( int id )
	{
		return gameData.statCharData.get( new Integer( id ) );
	}
	
	public static UsableData getUsableData( int id )
	{
		return gameData.usableData.get( new Integer( id ) );
	}
	
	public static Vector<AttackData> getAttackData( int id )
	{
		return gameData.attackData.get( new Integer( id ) );
	}
	
	public static MapData getMapData( int id )
	{
		return gameData.mapData.get( new Integer( id ) );
	}
	
	public static PropData getPropData( int id )
	{
		return gameData.propData.get( new Integer( id ) );
	}
	
	//===================================================================================
	private static String getSaveFileName( String saveName )
	{
		return "Save/" + saveName + ".sav";
	}
	
	private static GameData exportGameData()
	{
		GameData newData = new GameData();
		
		newData.amuletData = MDBReader.loadTable( "Amulet", AmuletData.class );
		newData.armeData = MDBReader.loadTable( "Arme", ArmeData.class );
		newData.armureData = MDBReader.loadTable( "Armure", ArmureData.class );
		newData.bagueData = MDBReader.loadTable( "Bague", BagueData.class );
		newData.bonusData = MDBReader.loadTable( "Bonus", BonusData.class );
		newData.eventData = MDBReader.loadTable( "Event", EventData.class );
		newData.foodData = MDBReader.loadTable( "Food", FoodData.class );
		newData.livreData = MDBReader.loadTable( "Book", LivreData.class );
		newData.objetGraphiqueData = MDBReader.loadTable( "Caracter", ObjetGraphiqueData.class );
		newData.playerStatData = MDBReader.loadTable( "Player", PlayerStatData.class );
		newData.potionData = MDBReader.loadTable( "Potion", PotionData.class );
		newData.projectileData = MDBReader.loadTable( "Projectile", ProjectileData.class );
		newData.questData = MDBReader.loadTable( "Quest", QuestData.class );
		newData.spellData = MDBReader.loadTable( "Spell", SpellData.class );
		newData.statCharData = MDBReader.loadTable( "StatCaracter", StatCharData.class );
		newData.usableData = MDBReader.loadTable( "Usable", UsableData.class );
		
		// Attack is a mapped table, storing a list of attacks per statcar id
		newData.attackData = MDBReader.loadMappedTable( "Attaque", AttackData.class );
		
		// Map data is highly complex and requires specialized export code.
		newData.mapData = exportMapTable();
		
		// Prop data is a subset of the props table containing only the props which
		//  aren't tied to maps.
		newData.propData = exportPropTable();
		
		return newData;
	}
	
	public static <T> T readObject( String fileName )
	{
		try
		{
			ObjectInputStream inStream = new ObjectInputStream( new FileInputStream( fileName ) );
			return (T)inStream.readObject();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static void writeObject( String fileName, Object ob )
	{
		try
		{
			ObjectOutputStream outStream = new ObjectOutputStream( new FileOutputStream( fileName ) );
			outStream.writeObject( ob );
		}
		catch (Exception e)
		{
			Util.error( "Error writing data file " + fileName, e );
		}
	}
	
	private static Hashtable<Integer,MapData> exportMapTable()
	{
		try
		{
			Hashtable<Integer,MapData> ret = new Hashtable<>();
			
			BaseDonnee dataDB = BaseDonnee.open_data();
			ResultSet mapRes = dataDB.calculRequete( "SELECT * FROM Map" );

			if( mapRes.first() )
			{
				do
				{
					int mapId = mapRes.getInt("MapID");
					
					MapData mapData = new MapData();
					Vector<String> paletteVec = new Vector<>();

					mapData.setSize( mapRes.getInt("TailleX"), mapRes.getInt("TailleY") );
					
					mapData.scriptEvent = mapRes.getInt("EventID");
					mapData.loadEvent = mapRes.getInt("Loading");
					mapData.northEvent = mapRes.getInt("North");
					mapData.southEvent = mapRes.getInt("South");
					mapData.eastEvent = mapRes.getInt("East");
					mapData.westEvent = mapRes.getInt("West");
					
					{
						ResultSet tileRes;

						tileRes = dataDB.calculRequete( "SELECT * FROM TiltSet WHERE MapID = " + mapId );
						
						if( tileRes.first() )
						{
							do
							{
								mapData.setTile(
									tileRes.getInt("Position"),
									tileRes.getString("NomImage"),
									tileRes.getInt("EventID"),
									tileRes.getInt("Blocked"),
									paletteVec );
							} while (tileRes.next());
						}
						
						dataDB.finRequete(tileRes);
					}
					
					{
						ResultSet propRes = dataDB.calculRequete( "SELECT * FROM Prop WHERE MapID = " + mapId );
						Vector<MapPropData> propVec = new Vector<>();

						if( propRes.first() )
						{
							
							do
							{
								MapPropData data = new MapPropData();
								data.width = propRes.getInt("TailleX");
								data.height = propRes.getInt("TailleY");
								data.x = propRes.getInt("PositionX");
								data.y = propRes.getInt("PositionY");
								
								data.setArt( propRes.getString("Nom"), paletteVec );
								
								propVec.add( data );
							} while (propRes.next());
						}
						
						mapData.setProps( propVec );
						dataDB.finRequete( propRes );
					}
					
					mapData.setPalette( paletteVec );
					
					ret.put( new Integer( mapId ), mapData );

				} while (mapRes.next());
			}

			dataDB.finRequete(mapRes);
			
			return ret;
		}
		catch( Exception e )
		{
			Util.error( e );
		}
		
		return null;
	}
	
	private static Hashtable<Integer,PropData> exportPropTable()
	{
		Hashtable<Integer,PropData> ret = new Hashtable<>();
		BaseDonnee data_db = BaseDonnee.open_data();

		try
		{
			ResultSet res = data_db.calculRequete( "SELECT * FROM Prop WHERE MapID = -1" );
			
			if (res.first())
			{
				do
				{
					PropData data = new PropData();
					data.id = res.getInt("PropID");
					data.width = res.getInt("TailleX");
					data.height = res.getInt("TailleY");
					data.artName = res.getString("Nom");
					
					ret.put( new Integer( data.id ), data );
				} while (res.next());
			}
			
			data_db.finRequete(res);
		}
		catch( Exception e )
		{
			Util.error( "Error exporting Prop.dat", e );
		}
		
		return ret;
	}
}