import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapOfCollections {

    private Map map;

    public MapOfCollections() {
        super();

        this.map = new HashMap();

        this.map.put("key1", Arrays.asList(new String[] { "key1_value1", "key1_value2" }));
        this.map.put("key2", Arrays.asList(new String[] { "key2_value1", "key2_value2" }));
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MapOfCollections)) {
            return false;
        }

        return this.getMap().equals(((MapOfCollections) obj).getMap());
    }

    public int hashCode() {
        return this.getMap().hashCode();
    }

    public String toString() {
        return this.getClass().getName() + ": " + this.getMap();
    }
}
