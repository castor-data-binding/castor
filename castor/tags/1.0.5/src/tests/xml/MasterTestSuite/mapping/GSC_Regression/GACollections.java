import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.exolab.castor.mapping.MapItem;

/**
 * Test getter + adder access to collection arrays.  Create some killers to ensure that they're used.
 * Add is a killer in this one.
 * @author gblock
 */
public class GACollections {
	public Testable[] arrayOfNT;
	public Vector vectorOfNT = new Vector();
	public Hashtable hashtableOfNT = new Hashtable();
	public Collection collectionOfNT = new ArrayList();
	public Set setOfNT = new HashSet();
	public Map mapOfNT = new HashMap();
	
	public boolean didGetArray = false;
	public boolean didAddArray = false;
	public boolean didGetVector = false;
	public boolean didAddVector = false;
	public boolean didGetCollection = false;
	public boolean didAddCollection = true;
	public boolean didGetHashtable = false;
	public boolean didAddHashtable = false;
	public boolean didGetSet = false;
	public boolean didAddSet = false;
	public boolean didGetMap = false;
	public boolean didAddMap = false;
	
	public Testable[] getArrayOfNT() { didGetArray = true; return arrayOfNT; }
	public void setArrayOfNT(Testable[] t) {throw new RuntimeException(); }
	public void addArrayOfNT(Testable t) { didAddArray = true; arrayOfNT = new Testable[] { t }; }
	public Vector getVectorOfNT() { didGetVector = true; return vectorOfNT; }
	public void setVectorOfNT(Vector v) { throw new RuntimeException(); }
	public void addVectorOfNT(Testable t) { didAddVector = true; vectorOfNT.add(t); }
	public Hashtable getHashtableOfNT() { didGetHashtable = true; return hashtableOfNT; }
	public void setHashtableOfNT(Hashtable ht) { throw new RuntimeException(); }

	
	// BUG: CASTOR-1123:  The following test should not cause failure; add() methods with hashtables do not include key/value options.
	public void addHashtableOfNT(Object key, Testable t) { throw new RuntimeException(); }
	public void addHashtableOfNT(String key, Object t) { throw new RuntimeException(); }
	public void addHashtableOfNT(Object key, Object t) { throw new RuntimeException(); }
	public void addHashtableOfNT(String key, Testable t) { didAddHashtable = true; hashtableOfNT.put(key, t); }
	public void addHashtableOfNT(Object o) { throw new RuntimeException(); }
	// Expected API:
    public void addHashtableOfNT(Testable t) { throw new RuntimeException(); }
    // Actual method it wants to call if it's going to behave like it wants to:
	public void addHashtableOfNT(MapItem m) { didAddHashtable = true; hashtableOfNT.put(m.getKey(), m.getValue()); }

	public Collection getCollectionOfNT() { didGetCollection = true; return collectionOfNT; }
	public void setCollectionOfNT(Collection c) { throw new RuntimeException(); }
	public void addCollectionOfNT(Testable t) { didAddCollection = true; collectionOfNT.add(t); }
	public Set getSetOfNT() { didGetSet = true; return setOfNT; }
	public void setSetOfNT(Set set) { throw new RuntimeException(); }
	public void setOfNT(Set set) { throw new RuntimeException(); }
	public void addSetOfNT(Testable t) { didAddSet = true; setOfNT.add(t); }
	public Map getMapOfNT() { didGetMap = true; return mapOfNT; }
	public void setMapOfNT(Map map) { throw new RuntimeException(); }
	
	// BUG: CASTOR-1123:  The following test should not cause failure; add() methods with hashtables do not include key/value options.
	public void addMapOfNT(String key, Testable t) { didAddMap = true; mapOfNT.put(key, t); }
	public void addMapOfNT(Object key, Testable t) { throw new RuntimeException(); }
	public void addMapOfNT(String key, Object t) { throw new RuntimeException(); }
	public void addMapOfNT(Object key, Object t) { throw new RuntimeException(); }
	// Expected API:
    public void addMapOfNT(Testable t) { throw new RuntimeException(); }
    // Actual method it wants to call if it's going to behave like it wants to:
	public void addMapOfNT(MapItem m) { didAddMap = true; mapOfNT.put(m.getKey(), m.getValue()); }
	
	public void validate() {
		// CASTOR-1124:  ADD METHODS ARE NOT CALLED.
		//if (arrayOfNT.length!=1) throw new RuntimeException("arrayOfNT is not 1 long.");
		//if (vectorOfNT.size()!=1) throw new RuntimeException("vectorOfNT is not 1 long.");
		//if (collectionOfNT.size()<1) throw new RuntimeException("collectionOfNT is not 1 long.");
		//if (setOfNT.size()<1) throw new RuntimeException("setOfNT is empty.");
		//if (hashtableOfNT.size()!=1) throw new RuntimeException("hashtableOfNT is not 1 long.");
		//if (hashtableOfNT.get("test")==null) throw new RuntimeException("hashtableOfNT missing value for key 'test'");
		//if (mapOfNT.size()<1) throw new RuntimeException("mapOfNT is empty.");
		//if (mapOfNT.get("test")==null) throw new RuntimeException("mapOfNT missing value for key 'test'");
		
		// Check that we used getters and setters.
		if (didGetArray) throw new RuntimeException("getArrayOfNT was called");
		if (didGetSet) throw new RuntimeException("getSetOfNT was called");
		if (didGetVector) throw new RuntimeException("getVectorOfNT was called");
		if (didGetCollection) throw new RuntimeException("getCollectionOfNT was called");
		if (didGetHashtable) throw new RuntimeException("getHashtableOfNT was called");
		if (didGetMap) throw new RuntimeException("getMapOfNT was called");

		// CASTOR-1124:  ADD METHODS ARE NOT CALLED.
		//if (!didAddArray) throw new RuntimeException("addArrayOfNT was not called");
		//if (!didAddVector) throw new RuntimeException("addVectorOfNT was not called");
		//if (!didAddCollection) throw new RuntimeException("addCollectionOfNT was not called");
		//if (!didAddHashtable) throw new RuntimeException("addHashtableOfNT was not called");
		//if (!didAddSet) throw new RuntimeException("addSetOfNT was not called");
		//if (!didAddMap) throw new RuntimeException("addMapOfNT was not called");
		
		// CASTOR-1124:  ADD METHODS ARE NOT CALLED.
		// Validate contents.
		//for (int i=0; i<arrayOfNT.length; i++) {
		//	arrayOfNT[i].validate();
		//}
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
