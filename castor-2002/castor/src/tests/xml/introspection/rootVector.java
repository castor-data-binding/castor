//package xml.introspection;

import java.util.Vector;
import java.util.Enumeration;

public class rootVector {


    private Vector _item = new Vector();

    public rootVector() {
        
    }


    public void setItem(Vector item) {
        _item = item;
    }

    public Vector getItem() {
        return _item;
    }
    
    public boolean equals( rootVector _rootvector )
    {
        boolean result=true;
        
        if (_item.size()!=_rootvector._item.size())
        {
            System.out.println("different sizes");
            return false;
        }
         
        
        int i=0;
        while ((i<_item.size())&&result)
        {
            result=((Item)_item.get(i)).equals((Item)_rootvector._item.get(i));
            i++;
        }
        
        return result;   
    }
    

    public void print() {

        for (int i=0; i<_item.size(); i++)
            ((Item)_item.get(i)).print();

    }
}
