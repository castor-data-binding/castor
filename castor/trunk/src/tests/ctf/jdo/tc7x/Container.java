package ctf.jdo.tc7x;

import java.util.ArrayList;

public final class Container {
    private Integer _id;
    private String _name;
    private ArrayList _prop;

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public ArrayList getProp() { return _prop; }
    public void setProp(final ArrayList prop) { _prop = prop; }
}
