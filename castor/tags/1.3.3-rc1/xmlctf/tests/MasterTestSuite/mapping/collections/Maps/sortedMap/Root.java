import java.util.SortedMap;
import java.util.TreeMap;

public class Root {

    SortedMap _children = new TreeMap();

    public SortedMap getChildren() {
        return _children;
    }

    public void setChildren(SortedMap m) {
        _children = m;
    }
}