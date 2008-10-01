import java.util.Map;
import java.util.TreeMap;

public class Root {

    Map _children = new TreeMap();

    public Map getChildren() {
        return _children;
    }

    public void setChildren(Map m) {
        _children = m;
    }
}