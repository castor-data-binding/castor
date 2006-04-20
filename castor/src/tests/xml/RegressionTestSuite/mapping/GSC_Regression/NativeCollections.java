import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Test native access to collection arrays.  Create some killers to ensure that they're used.
 * @author gblock
 */
public class NativeCollections {
	public Testable[] arrayOfNT;
	public Vector vectorOfNT;
	public Hashtable hashtableOfNT;
	public Collection collectionOfNT;
	public Set setOfNT;
	public Map mapOfNT;
	
	public Testable[] getArrayOfNT() { throw new RuntimeException(); }
	public void setArrayOfNT(Testable[] arr) { throw new RuntimeException(); }
	public Vector getVectorOfNT() { throw new RuntimeException(); }
	public void setVectorOfNT(Vector v) { throw new RuntimeException(); }
	public Hashtable getHashtableOfNT() { throw new RuntimeException(); }
	public void setHashtableOfNT(Hashtable ht) { throw new RuntimeException(); }
	public Collection getCollectionOfNT() { throw new RuntimeException(); }
	public void setCollectionOfNT(Collection c) { throw new RuntimeException(); }
	public Set getSetOfNT() { throw new RuntimeException(); }
	public void setSetOfNT(Set set) { throw new RuntimeException(); }
	public void setOfNT(Set set) { throw new RuntimeException(); }
	public void getMapOfNT() { throw new RuntimeException(); }
	public void setMapOfNT(Map map) { throw new RuntimeException(); }
	
	public void validate() {
		if (arrayOfNT==null) throw new RuntimeException("arrayOfNT was not set");
		if (arrayOfNT.length!=1) throw new RuntimeException("arrayOfNT is not 1 long.");
		if (vectorOfNT==null||vectorOfNT.size()!=1) throw new RuntimeException("vectorOfNT is not 1 long.");
		if (collectionOfNT==null||collectionOfNT.size()<1) throw new RuntimeException("collectionOfNT is not 1 long.");
		if (setOfNT==null||setOfNT.size()<1) throw new RuntimeException("setOfNT is empty.");
		if (hashtableOfNT==null||hashtableOfNT.size()!=1) throw new RuntimeException("hashtableOfNT is not 1 long.");
		if (hashtableOfNT.get("test")==null) throw new RuntimeException("hashtableOfNT missing value for key 'test'");
		if (mapOfNT==null||mapOfNT.size()<1) throw new RuntimeException("mapOfNT is empty.");
		if (mapOfNT.get("test")==null) throw new RuntimeException("mapOfNT missing value for key 'test'");
		
		// Validate contents.
		for (int i=0; i<arrayOfNT.length; i++) {
			arrayOfNT[i].validate();
		}
		Iterator it = vectorOfNT.iterator();
		while (it.hasNext()) { ((Testable)it.next()).validate(); }
		it = collectionOfNT.iterator();
		while (it.hasNext()) { ((Testable)it.next()).validate(); }
		it = setOfNT.iterator();
		while (it.hasNext()) { ((Testable)it.next()).validate(); }
		it = hashtableOfNT.values().iterator();
		while (it.hasNext()) { ((Testable)it.next()).validate(); }
		it = mapOfNT.values().iterator();
		while (it.hasNext()) { ((Testable)it.next()).validate(); }
	}
}
