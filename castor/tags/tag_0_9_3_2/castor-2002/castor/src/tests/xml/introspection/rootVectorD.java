//package xml.introspection;

import java.util.Vector;
import java.util.Enumeration;

public class rootVectorD {


    public Vector item = new Vector();

    public rootVectorD() {
        
    }

    
    public boolean equals( rootVectorD _rootvectord )
    {
        boolean result=true;
        
        if (item.size()!=_rootvectord.item.size())
        {
            System.out.println("different sizes");
            return false;
        }
         
        
        int i=0;
        while ((i<item.size())&&result)
        {
            result=((Item)item.get(i)).equals((Item)_rootvectord.item.get(i));
            i++;
        }
        
        return result;   
    }
    
    public void print() {

        for (int i=0; i<item.size(); i++)
            ((Item)item.get(i)).print();

    }
}
