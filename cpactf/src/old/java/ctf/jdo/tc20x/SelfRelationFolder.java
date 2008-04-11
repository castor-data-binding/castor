package ctf.jdo.tc20x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;

public final class SelfRelationFolder implements Serializable, TimeStampable {
    private static final long serialVersionUID = -625272206256195346L;

    private Integer _id = null;
    private String _name = null;
    private Collection _children = new ArrayList();
    private SelfRelationFolder _parent = null;
    private long _timeStamp;

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Collection getChildren() { return _children; }
    public void setChildren(final Collection children) { _children = children; }
    public void addChild(final SelfRelationFolder child) {
        child.setParent(this);
        _children.add(child);
    }

    public SelfRelationFolder getParent() { return _parent; }
    public void setParent(final SelfRelationFolder parent) { _parent = parent; }

    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
}
