import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapOfArrays {

    private Map map;

    public MapOfArrays() {
        super();

        this.map = new HashMap();

        this.map.put("key1", new String[] { "key1_value1", "key1_Value2" });
        this.map.put("key2", new String[] { "key2_value1", "key2_Value2" });
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MapOfArrays)) {
            return false;
        }

        final MapOfArrays other = (MapOfArrays) obj;
        if (other.getMap().size() != this.getMap().size()) {
            return false;
        }

        final Set myKeys = this.getMap().keySet();
        final Set otherKeys = other.getMap().keySet();

        if (!myKeys.containsAll(otherKeys) && otherKeys.containsAll(myKeys)) {
            return false;
        }

        for (Iterator i = myKeys.iterator(); i.hasNext();) {
            final Object key = i.next();
            final String[] myValue = (String[]) this.getMap().get(key);
            final String[] otherValue = (String[]) other.getMap().get(key);

            if (!Arrays.equals(myValue, otherValue)) {
                return false;
            }
        }

        return true;
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
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.getClass().getName());
        buffer.append(": {");

        final Set myKeys = this.getMap().keySet();

        for (Iterator i = myKeys.iterator(); i.hasNext();) {
            final Object key = i.next();
            buffer.append(key);
            buffer.append("=");
            buffer.append(Arrays.asList(((String[]) this.getMap().get(key))).toString());
            buffer.append(", ");
        }
        if (this.getMap().size() > 0) {
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        buffer.append("}");

        return buffer.toString();
    }
}
