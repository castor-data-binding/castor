import java.util.Map;
import java.util.HashMap;

public class Root {

  HashMap _children = new HashMap();

  public HashMap getChildren() {
    System.out.println("#getChildren!");
      return _children;
  }
  public void setChildren(HashMap m) {
    System.out.println("#setChildren!");
      _children = (HashMap)m;
  }
}