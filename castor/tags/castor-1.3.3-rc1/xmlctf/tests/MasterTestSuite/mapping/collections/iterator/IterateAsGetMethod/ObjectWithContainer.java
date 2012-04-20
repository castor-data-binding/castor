import java.util.Iterator;
import java.util.Vector;

public class ObjectWithContainer {
	protected Vector	strings;
	
	public ObjectWithContainer() {
		strings	= new Vector();
	}
	
	public void addString(String string) {
		strings.add(string);
	}
	
	public Iterator iterateStrings() {
		return strings.iterator();
	}
}
