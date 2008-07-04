package org.castor.core.nature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyHolderTest implements PropertyHolder
{
    private List natures = new ArrayList();
    private Map properties = new HashMap();
    

    public Object getProperty(final String name) {
        return properties.get(name);
    }

    public void setProperty(final String name, final Object value) {
        properties.put(name, value);
    }

    public void addNature(String nature) {
        this.natures.add(nature);
    }

    public boolean hasNature(final String nature) {
        return natures.contains(nature);
    }

}
