

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

    public void writeToFile(Object data, String fileName) throws IOException {
        Path outFile = gd.resolve(fileName);
        // Lets actually sort this shall we?  If its sortable that is.
        if (data instanceof Collection<?>) {
            final Collection<?> col = (Collection<?>)data;
            data = col.stream().sorted(Comparator.comparing(DataToJson::getIdFromInstance)).toArray();
        }
        try (BufferedWriter out = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
            mapper.writeValue(out, data);
        }
    }
   
    public <T> Map<Integer, T> loadStandardFile(String fileName, Class<T> type) throws IOException {
        Path inFile = gd.resolve(fileName);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Loading {0} from {1}", new Object[]{type.getSimpleName(), inFile});
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            CollectionType fileDataType = mapper.getTypeFactory().constructCollectionType(List.class, type);
            List<T> items = mapper.readValue(br, fileDataType);
            return items.stream().collect(Collectors.toConcurrentMap(DataToJson::getIdFromInstance, Function.identity()));
        }
    }
    
    public Map<Integer, MapData> loadMapDataFile(String fileName) throws IOException {
        Path inFile = gd.resolve(fileName);
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            MapType fileDataType = mapper.getTypeFactory().constructMapType(Map.class, Integer.class, MapData.class);
            Map<Integer, MapData> items = mapper.readValue(br, fileDataType);
            return items;
        }
    }
 
    public Map<Integer, Vector<AttackData>> loadAttackDataFile(String fileName) throws IOException {
        Path inFile = gd.resolve(fileName);
        try (BufferedReader br = Files.newBufferedReader(inFile, StandardCharsets.UTF_8)) {
            CollectionType fileDataType = mapper.getTypeFactory().constructCollectionType(List.class, AttackData.class);
            List<AttackData> items = mapper.readValue(br, fileDataType);
            return items.stream().collect(Collectors.groupingBy(AttackData::getStatCarID, Collectors.toCollection(Vector::new)));
        }
    }

    protected GameData loadGameData() throws IOException {
        GameData newGameData = new GameData();
        
        newGameData.amuletData = new Hashtable<>(loadStandardFile("amuletData.json", AmuletData.class));
        newGameData.armeData = new Hashtable<>(loadStandardFile("armeData.json", ArmeData.class));
        newGameData.armureData = new Hashtable<>(loadStandardFile("armureData.json", ArmureData.class));
        newGameData.bagueData = new Hashtable<>(loadStandardFile("bagueData.json", BagueData.class));
        newGameData.bonusData = new Hashtable<>(loadStandardFile("bonusData.json", BonusData.class));
        newGameData.eventData = new Hashtable<>(loadStandardFile("eventData.json", EventData.class));
        newGameData.foodData = new Hashtable<>(loadStandardFile("foodData.json", FoodData.class));
        newGameData.livreData = new Hashtable<>(loadStandardFile("livreData.json", LivreData.class));
        newGameData.objetGraphiqueData = new Hashtable<>(loadStandardFile("objetGraphiqueData.json", ObjetGraphiqueData.class));
        newGameData.playerStatData = new Hashtable<>(loadStandardFile("playerStatData.json", PlayerStatData.class));
        newGameData.potionData = new Hashtable<>(loadStandardFile("potionData.json", PotionData.class));
        newGameData.projectileData = new Hashtable<>(loadStandardFile("projectileData.json", ProjectileData.class));
        newGameData.questData = new Hashtable<>(loadStandardFile("questData.json", QuestData.class));
        newGameData.spellData = new Hashtable<>(loadStandardFile("spellData.json", SpellData.class));
        newGameData.statCharData = new Hashtable<>(loadStandardFile("statCharData.json", StatCharData.class));
        newGameData.usableData = new Hashtable<>(loadStandardFile("usableData.json", UsableData.class));
        newGameData.propData = new Hashtable<>(loadStandardFile("propData.json", PropData.class));
        
        newGameData.mapData = new Hashtable<>(loadMapDataFile("mapData.json"));
        
        newGameData.attackData = new Hashtable<>(loadAttackDataFile("attackData.json"));
        
        return newGameData;
    }

    void saveGameData(GameData gameData) throws IOException {
        
        writeToFile(gameData.amuletData.values(), "amuletData.json");
        writeToFile(gameData.armeData.values(), "armeData.json");
        writeToFile(gameData.armureData.values(), "armureData.json");
        writeToFile(gameData.bagueData.values(), "bagueData.json");
        writeToFile(gameData.bonusData.values(), "bonusData.json");
        writeToFile(gameData.eventData.values(), "eventData.json");
        writeToFile(gameData.foodData.values(), "foodData.json");
        writeToFile(gameData.livreData.values(), "livreData.json");
        writeToFile(gameData.objetGraphiqueData.values(), "objetGraphiqueData.json");
        writeToFile(gameData.playerStatData.values(), "playerStatData.json");
        writeToFile(gameData.potionData.values(), "potionData.json");
        writeToFile(gameData.projectileData.values(), "projectileData.json");
        writeToFile(gameData.questData.values(), "questData.json");
        writeToFile(gameData.spellData.values(), "spellData.json");
        writeToFile(gameData.statCharData.values(), "statCharData.json");
        writeToFile(gameData.usableData.values(), "usableData.json");
        writeToFile(gameData.propData.values(), "propData.json");

        // Map we want in order but its different
        Map<Integer, MapData> sortedMapData = new TreeMap<>(gameData.mapData);
        writeToFile(sortedMapData, "mapData.json");

        // Attack data is slightly special, we want to write them all and sort properly.
        //writeToFile(gameData.attackData.values(), "attackData.json");
        List<AttackData> attackData = gameData.attackData.values().stream().flatMap(Vector::stream).collect(Collectors.toList());
        writeToFile(attackData, "attackData.json");
        
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

    // Gets the name of the FIRST field in a class! This is the one (by convention) is the integer key field in the vgame database.
    private static String getKeyFieldName(Class<?> dataType) {
        final Field[] fields = dataType.getDeclaredFields();
        return fields[0].getName();
    }

}
