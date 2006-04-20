import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Test getter + setter access to collection arrays.  Create some killers to ensure that they're used.
 * Add is a killer in this one.
 * @author gblock
 */
public class GSCollections {
	public Testable[] arrayOfNT;
	public Vector vectorOfNT;
	public Hashtable hashtableOfNT;
	public Collection collectionOfNT;
	public Set setOfNT;
	public Map mapOfNT;
	
	public boolean didGetArray = false;
	public boolean didSetArray = false;
	public boolean didGetVector = false;
	public boolean didSetVector = false;
	public boolean didGetCollection = false;
	public boolean didSetCollection = true;
	public boolean didGetHashtable = false;
	public boolean didSetHashtable = false;
	public boolean didGetSet = false;
	public boolean didSetSet = false;
	public boolean didGetMap = false;
	public boolean didSetMap = false;
	
	public Testable[] getArrayOfNT() { didGetArray = true; return arrayOfNT; }
	public void setArrayOfNT(Testable[] testables) { didSetArray = true; arrayOfNT = testables; }
	public void addArrayOfNT(Testable t) { throw new RuntimeException(); }
	public Vector getVectorOfNT() { didGetVector = true; return vectorOfNT; }
	public void setVectorOfNT(Vector v) { didSetVector = true; vectorOfNT = v; }
	public void addVectorOfNT(Testable t) { throw new RuntimeException(); }
	public Hashtable getHashtableOfNT() { didGetHashtable = true; return hashtableOfNT; }
	public void setHashtableOfNT(Hashtable ht) { didSetHashtable = true; hashtableOfNT = ht; }
	public void addHashtableOfNT(Object o, Testable t) { throw new RuntimeException(); }
	public Collection getCollectionOfNT() { didGetCollection = true; return collectionOfNT; }
	public void setCollectionOfNT(Collection c) { didSetCollection = true; collectionOfNT = c; }
	public void addCollectionOfNT(Testable t) { throw new RuntimeException(); }
	public Set getSetOfNT() { didGetSet = true; return setOfNT; }
	public void setSetOfNT(Set set) { didSetSet = true; setOfNT = set; }
	public void setOfNT(Set set) { throw new RuntimeException(); }
	public void addSetOfNT(Testable t) { throw new RuntimeException(); }
	public Map getMapOfNT() { didGetMap = true; return mapOfNT; }
	public void setMapOfNT(Map map) { didSetMap = true; mapOfNT = map; }
	public void addMapOfNT(Testable t) { throw new RuntimeException(); }
	
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
		
		// Check that we used getters and setters.
		if (!didGetArray) throw new RuntimeException("getArrayOfNT was not called");
		if (!didSetArray) throw new RuntimeException("setArrayOfNT was not called");
		if (!didGetVector) throw new RuntimeException("getVectorOfNT was not called");
		if (!didSetVector) throw new RuntimeException("setVectorOfNT was not called");
		if (!didGetCollection) throw new RuntimeException("getCollectionOfNT was not called");
		if (!didSetCollection) throw new RuntimeException("setCollectionOfNT was not called");
		if (!didGetHashtable) throw new RuntimeException("getHashtableOfNT was not called");
		if (!didSetHashtable) throw new RuntimeException("setHashtableOfNT was not called");
		if (!didGetSet) throw new RuntimeException("getSetOfNT was not called");
		if (!didSetSet) throw new RuntimeException("setSetOfNT was not called");
		if (!didGetMap) throw new RuntimeException("getMapOfNT was not called");
		if (!didSetMap) throw new RuntimeException("setMapOfNT was not called");
		
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
