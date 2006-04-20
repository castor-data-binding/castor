//package xml.introspection;

import java.util.Hashtable;
import java.util.Enumeration;

public class rootHashtable {


    private Hashtable _item = new Hashtable();

    public rootHashtable() {
        
    }


    public void setItem(Hashtable item) {
        _item = item;
    }

    public Hashtable getItem() {
        return _item;
    }


    public void print() {
        
        System.out.println(" hashtable: "+_item.toString());
        //for (int i=0; i<_item.size(); i++)
          //  ((Item)_item.get(i)).print();

    }
}
