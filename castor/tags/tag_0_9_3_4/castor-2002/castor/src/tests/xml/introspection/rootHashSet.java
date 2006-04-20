//package xml.introspection;

import java.util.HashSet;
import java.util.Set;
import java.util.Enumeration;

public class rootHashSet {


    public HashSet _item = new HashSet();

    public rootHashSet() {
        
    }


    public void setItem(HashSet item) {
        _item = item;
    }

    public Set getItem() {
        return _item;
    }


    public void print() {
        
        System.out.println(" hashset: "+_item.toString());
        //for (int i=0; i<_item.size(); i++)
          //  ((Item)_item.get(i)).print();

    }
}
