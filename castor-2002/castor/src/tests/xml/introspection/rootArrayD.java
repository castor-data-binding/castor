//package xml.introspection;

import java.util.Vector;
import java.util.Enumeration;

public class rootArrayD {

    public Item[] item;

    public rootArrayD() {
       
    }



    public void print() {
        System.out.println("content:");
        for (int i=0; i<item.length; i++)
            item[i].print();
    }
}
