/**
 * Created on 24.11.2005 at 14:43:42
 *
 * TODO Insert class description here
 *
 * @author werner.guttmann
 *
 * Copyright (c) Vector SW DV GmbH, 2005
 */
package ctf.jdo.tc7x;

import java.util.Comparator;

public final class CustomComparator implements Comparator<SortedContainerItem> {
    public int compare(final SortedContainerItem arg0, final SortedContainerItem arg1) {
        if (arg0 == null || arg1 == null) {
            throw new NullPointerException ("Objects to compare cannot be null");
        }
        
        return arg0.getId().compareTo(arg1.getId());
    }
}
