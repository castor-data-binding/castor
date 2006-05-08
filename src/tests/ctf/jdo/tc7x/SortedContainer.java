package ctf.jdo.tc7x;

import java.util.SortedSet;

public final class SortedContainer {
    private Integer _id;
    private String _name;
    private SortedSet twos;
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public SortedSet getTwos() {
        return this.twos;
    }

    public void setTwos(SortedSet twos) {
        this.twos = twos;
    }
}
