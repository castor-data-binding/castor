//package xml.introspection;

import java.util.ArrayList;
//import java.util.Enumeration;

public class rootArrayList {


    private ArrayList _item = new ArrayList();

    public rootArrayList() {
        
    }


    public void setItem(ArrayList item) {
        _item = item;
    }

    public ArrayList getItem() {
        return _item;
    }


    public void print() {
        
        System.out.println(" Arraylist: "+_item.toString());
        //for (int i=0; i<_item.size(); i++)
          //  ((Item)_item.get(i)).print();

    }
}
