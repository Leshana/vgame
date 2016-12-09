import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Flan
 */
public class DataToJson {

    private final ObjectMapper mapper;

    private final Path gd;

    public DataToJson(Path gd) {
        this.gd = gd;
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    protected void writeToFile(Object data, String fileName) throws IOException {
        Path outFile = gd.resolve(fileName);
        Files.createDirectories(outFile.getParent());
        // Lets actually sort this shall we?  If its sortable that is.
        if (data instanceof Collection<?>) {
            final Collection<?> col = (Collection<?>)data;
            data = col.stream().sorted(Comparator.comparing(DataToJson::getIdFromInstance)).toArray();
        }
        try (BufferedWriter out = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
            mapper.writeValue(out, data);
        }
    }
   
    protected <T> Map<Integer, T> loadStandardFile(Path inFile, Class<T> type) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading {0} from {1}", new Object[]{type.getSimpleName(), inFile});
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            CollectionType fileDataType = mapper.getTypeFactory().constructCollectionType(List.class, type);
            List<T> items = mapper.readValue(br, fileDataType);
            return items.stream().collect(Collectors.toConcurrentMap(DataToJson::getIdFromInstance, Function.identity()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected Map<Integer, MapData> loadMapDataFile(Path inFile) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading MapData from {1}", inFile);
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            MapType fileDataType = mapper.getTypeFactory().constructMapType(Map.class, Integer.class, MapData.class);
            Map<Integer, MapData> items = mapper.readValue(br, fileDataType);
            return items;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    protected Map<Integer, Vector<AttackData>> loadAttackDataFile(Path inFile) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading AttackData from {1}", inFile);
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            CollectionType fileDataType = mapper.getTypeFactory().constructCollectionType(List.class, AttackData.class);
            List<AttackData> items = mapper.readValue(br, fileDataType);
            return items.stream().collect(Collectors.groupingBy(AttackData::getStatCarID, Collectors.toCollection(Vector::new)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected <T> Map<Integer, T> loadAllInFolder(String folderName, Function<Path, Map<Integer, T>> loaderFunc) throws IOException {
        Map<Integer, T> result = new ConcurrentHashMap<>();
        Path folderPath = gd.resolve(folderName);
        Files.list(folderPath)
                .filter((path) -> path.toString().endsWith(".json"))
                .sorted(Comparator.comparing(Path::toString))
                .map(loaderFunc)
                .forEach(result::putAll);
        return result;
    }
    
    protected GameData loadGameData() throws IOException {
        GameData newGameData = new GameData();
        
        newGameData.amuletData = new Hashtable<>(loadAllInFolder("amulet", (path -> loadStandardFile(path, AmuletData.class))));
        newGameData.amuletData = new Hashtable<>(loadAllInFolder("amulet", (path -> loadStandardFile(path, AmuletData.class))));
        newGameData.armeData = new Hashtable<>(loadAllInFolder("arme", (path -> loadStandardFile(path, ArmeData.class))));
        newGameData.armureData = new Hashtable<>(loadAllInFolder("armure", (path -> loadStandardFile(path, ArmureData.class))));
        newGameData.bagueData = new Hashtable<>(loadAllInFolder("bague", (path -> loadStandardFile(path, BagueData.class))));
        newGameData.bonusData = new Hashtable<>(loadAllInFolder("bonus", (path -> loadStandardFile(path, BonusData.class))));
        newGameData.eventData = new Hashtable<>(loadAllInFolder("event", (path -> loadStandardFile(path, EventData.class))));
        newGameData.foodData = new Hashtable<>(loadAllInFolder("food", (path -> loadStandardFile(path, FoodData.class))));
        newGameData.livreData = new Hashtable<>(loadAllInFolder("livre", (path -> loadStandardFile(path, LivreData.class))));
        newGameData.objetGraphiqueData = new Hashtable<>(loadAllInFolder("objetGraphique", (path -> loadStandardFile(path, ObjetGraphiqueData.class))));
        newGameData.playerStatData = new Hashtable<>(loadAllInFolder("playerStat", (path -> loadStandardFile(path, PlayerStatData.class))));
        newGameData.potionData = new Hashtable<>(loadAllInFolder("potion", (path -> loadStandardFile(path, PotionData.class))));
        newGameData.projectileData = new Hashtable<>(loadAllInFolder("projectile", (path -> loadStandardFile(path, ProjectileData.class))));
        newGameData.questData = new Hashtable<>(loadAllInFolder("quest", (path -> loadStandardFile(path, QuestData.class))));
        newGameData.spellData = new Hashtable<>(loadAllInFolder("spell", (path -> loadStandardFile(path, SpellData.class))));
        newGameData.statCharData = new Hashtable<>(loadAllInFolder("statChar", (path -> loadStandardFile(path, StatCharData.class))));
        newGameData.usableData = new Hashtable<>(loadAllInFolder("usable", (path -> loadStandardFile(path, UsableData.class))));
        newGameData.propData = new Hashtable<>(loadAllInFolder("prop", (path -> loadStandardFile(path, PropData.class))));
        
        newGameData.mapData = new Hashtable<>(loadAllInFolder("map", this::loadMapDataFile));
        
        newGameData.attackData = new Hashtable<>(loadAllInFolder("attack", this::loadAttackDataFile));
        
        return newGameData;
    }

    void saveGameData(GameData gameData) throws IOException {
        
        writeToFile(gameData.amuletData.values(), "amulet/amuletData.json");
        writeToFile(gameData.armeData.values(), "arme/armeData.json");
        writeToFile(gameData.armureData.values(), "armure/armureData.json");
        writeToFile(gameData.bagueData.values(), "bague/bagueData.json");
        writeToFile(gameData.bonusData.values(), "bonus/bonusData.json");
        writeToFile(gameData.eventData.values(), "event/eventData.json");
        writeToFile(gameData.foodData.values(), "food/foodData.json");
        writeToFile(gameData.livreData.values(), "livre/livreData.json");
        writeToFile(gameData.objetGraphiqueData.values(), "objetGraphique/objetGraphiqueData.json");
        writeToFile(gameData.playerStatData.values(), "playerStat/playerStatData.json");
        writeToFile(gameData.potionData.values(), "potion/potionData.json");
        writeToFile(gameData.projectileData.values(), "projectile/projectileData.json");
        writeToFile(gameData.questData.values(), "quest/questData.json");
        writeToFile(gameData.spellData.values(), "spell/spellData.json");
        writeToFile(gameData.statCharData.values(), "statChar/statCharData.json");
        writeToFile(gameData.usableData.values(), "usable/usableData.json");
        writeToFile(gameData.propData.values(), "prop/propData.json");
        
        // Map we want in order but its different
        Map<Integer, MapData> sortedMapData = new TreeMap<>(gameData.mapData);
        writeToFile(sortedMapData, "map/mapData.json");

        // Attack data is slightly special, we want to write them all and sort properly.
        List<AttackData> attackData = gameData.attackData.values().stream().flatMap(Vector::stream).collect(Collectors.toList());
        writeToFile(attackData, "attack/attackData.json");
    }

    /**
     * Reads the object's Integer ID from the first field in declaration order. This is compatible with the same logic
     * used by DataUtil.
     *
     * @param instance
     * @return
     */
    private static Integer getIdFromInstance(Object instance) {
        try {
            final Field[] fields = instance.getClass().getDeclaredFields();
            final Field keyField = fields[0];
            return (Integer) keyField.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DataToJson.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
