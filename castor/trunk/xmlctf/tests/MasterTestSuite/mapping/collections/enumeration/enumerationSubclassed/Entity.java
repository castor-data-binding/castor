import java.util.Vector;

public final class Entity {
    private Vector vector;
    
    public Entity() {
    	vector = new Vector();
    }
    
    public EnumerationDelegator enumVector() {
    	return new EnumerationDelegator(vector.elements());
    }
    
    public void setVector(Vector vector) {
    	this.vector = vector;
    }
    
    public void addString(String s) {
    	vector.add(s);
    }
}
