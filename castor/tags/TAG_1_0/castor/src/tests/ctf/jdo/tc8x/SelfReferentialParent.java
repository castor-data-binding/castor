package ctf.jdo.tc8x;

import java.util.List;

public class SelfReferentialParent {
    
    private Integer _id;
    private String _name;
    private SelfReferentialParent _parent;
    private List _entities;
    
    public final Integer getId() {
        return _id;
    }
    
    public final void setId(final Integer id) {
        _id = id;
    }
    
    public final String getName() {
        return _name;
    }
    
    public final void setName(final String name) {
        _name = name;
    }

    public final List getEntities() {
        return _entities;
    }

    public final void setEntities(final List entities) {
        _entities = entities;
    }

    public final SelfReferentialParent getParent() {
        return _parent;
    }

    public final void setParent(final SelfReferentialParent parent) {
        _parent = parent;
    }
}
