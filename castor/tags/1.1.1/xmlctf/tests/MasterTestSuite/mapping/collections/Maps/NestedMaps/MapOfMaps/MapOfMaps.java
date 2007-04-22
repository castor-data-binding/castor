import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapOfMaps {

    private Map map;

    public MapOfMaps() {
        super();

        this.map = new HashMap();

        this.addEntry("key1", "nested_key1", "nested_value1");
        this.addEntry("key2", "nested_key2", "nested_value2");
    }

    private void addEntry(String key, String nestedKey, String nestedValue) {
        TreeMap entry = new TreeMap();
        entry.put(nestedKey, nestedValue);
        this.map.put(key, entry);
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MapOfMaps)) {
            return false;
        }
        return this.getMap().equals(((MapOfMaps) obj).getMap());
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.map;
    }
}
