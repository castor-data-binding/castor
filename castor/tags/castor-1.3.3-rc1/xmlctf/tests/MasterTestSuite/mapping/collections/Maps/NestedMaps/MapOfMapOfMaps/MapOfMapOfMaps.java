import java.util.HashMap;
import java.util.Map;

public class MapOfMapOfMaps {

    private Map map;

    public MapOfMapOfMaps() {
        super();

        this.map = new HashMap();

        this.map.put("key1", new HashMap());
        this.map.put("key2", new HashMap());

        this.addLevel2("key1");
        this.addLevel3("key1", "key1");
        this.addLevel3("key1", "key2");

        this.addLevel2("key2");
        this.addLevel3("key2", "key1");
        this.addLevel3("key2", "key2");

    }

    private void addLevel3(String level1Key, String level2Key) {
        final Map level2Map = (Map) this.getMap().get(level1Key);
        final Map level3Map = (Map) level2Map.get(level2Key);
        level3Map.put("key1", "value1");
        level3Map.put("key2", "value2");
    }

    private void addLevel2(String level1Key) {
        Map level2Map = (Map) this.map.get(level1Key);
        level2Map.put("key1", new HashMap());
        level2Map.put("key2", new HashMap());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MapOfMapOfMaps)) {
            return false;
        }

        MapOfMapOfMaps other = (MapOfMapOfMaps) obj;
        return this.getMap().equals(other.getMap());
    }

    public Map getMap() {
        return this.map;
    }

    public int hashCode() {
        return this.getMap().hashCode();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.getMap();
    }
}
