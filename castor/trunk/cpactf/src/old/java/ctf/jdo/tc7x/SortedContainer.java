package ctf.jdo.tc7x;

import java.util.SortedSet;

public final class SortedContainer {
    private Integer _id;
    private String _name;
    private SortedSet<SortedContainerItem> _twos;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public SortedSet<SortedContainerItem> getTwos() { return _twos; }
    public void setTwos(final SortedSet<SortedContainerItem> twos) { _twos = twos; }
}
