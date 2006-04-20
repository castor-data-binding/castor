//package xml.introspection;

import java.util.Vector;
import java.util.Enumeration;

public class rootArray {

    private Item[] _items;

    public rootArray() {
        
    }


    public void setItem(Item[] items) {
        _items = items;
    }

    public Item[] getItem() {
        return _items;
    }

    public boolean equals(rootArray _rootarray) {
        
        boolean result=true;
        
        if (_items.length!=_rootarray._items.length)
        {
            System.out.println("different lengths");
            return false;
        }
            
        int i=0;
        while ((i<_items.length)&&result)
        {
            result=_items[i].equals(_rootarray._items[i]);
            i++;
        }
        
        return result; 
    }
      

    public void print() {
        System.out.println("content:");
        for (int i=0; i<_items.length; i++)
            _items[i].print();
    }
}
