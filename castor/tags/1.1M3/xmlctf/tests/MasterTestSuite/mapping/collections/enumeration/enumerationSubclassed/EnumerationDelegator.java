import java.util.Enumeration;

public class EnumerationDelegator implements Enumeration {
	public Enumeration e;
	
	public EnumerationDelegator(Enumeration e) {
		this.e = e;
	}

	public boolean hasMoreElements() {
		return e.hasMoreElements();
	}

	public Object nextElement() {
		return e.nextElement();
	}
	
}
