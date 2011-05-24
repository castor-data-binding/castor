import java.util.Enumeration;
import java.util.Vector;

public class ObjectWithContainer {

    protected Vector strings = new Vector();

    public void addString(String string) {
        strings.add(string);
    }

    public Enumeration enumStrings() {
        return strings.elements();
    }
}