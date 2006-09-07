package ctf.jdo.tc7x;

import java.util.SortedSet;

public final class SortedContainer {
    private Integer _id;
    private String _name;
    private SortedSet _twos;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public SortedSet getTwos() { return _twos; }
    public void setTwos(final SortedSet twos) { _twos = twos; }
}
