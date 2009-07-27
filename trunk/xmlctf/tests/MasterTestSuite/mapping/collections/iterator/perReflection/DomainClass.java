import java.util.Iterator;
import java.util.Vector;

public class DomainClass {

    private Vector items = new Vector();
    
    public DomainClass() {
        items.add("blah1");
    }

    /**
     * @return Iterator
     */
     public Iterator getItems() {
        return items.iterator();
    }

}
