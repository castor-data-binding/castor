/**
 * Created on 24.11.2005 at 14:43:42
 *
 * TODO Insert class description here
 *
 * @author werner.guttmann
 *
 * Copyright (c) Vector SW DV GmbH, 2005
 */
package jdo;

import java.util.Comparator;

public class CustomComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        if (arg0 == null || arg1 == null) {
            throw new NullPointerException ("Objects to compare cannot be null");
        }
        
        if (!(arg0 instanceof SortedContainerItem)) {
            throw new ClassCastException ("Problem converting objects to compare to EntityTwo");
        }

        if (!(arg0 instanceof SortedContainerItem)) {
            throw new ClassCastException ("Problem converting objects to compare to EntityTwo");
        }
        
        return ((SortedContainerItem) arg0).getId().compareTo(((SortedContainerItem) arg1).getId());
    }

}
