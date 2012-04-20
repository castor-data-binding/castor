import java.util.Iterator;

public class IteratorDelegator implements Iterator {
	public Iterator i;
	
	public IteratorDelegator(Iterator i) {
		this.i = i;
	}

	public boolean hasNext() {
		return i.hasNext();
	}

	public Object next() {
		return i.next();
	}

	public void remove() {
		i.remove();
	}
	
}
