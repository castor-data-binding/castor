import java.util.Map;
import java.util.HashMap;

public class Root {

    Map _children = new HashMap();

    public Map getChildren() {
        return _children;
    }

    public void setChildren(Map m) {
        _children = (HashMap) m;
    }
}