package ctf.jdo.tc20x;

import java.util.Collection;
import java.util.ArrayList;
import java.io.Serializable;
import org.exolab.castor.jdo.TimeStampable;

public class SelfRelationFolder implements Serializable, TimeStampable {

    private Integer _id = null;

    private String _name = null;

    private Collection _children = new ArrayList();

    private SelfRelationFolder _parent = null;

    private long _timeStamp;

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

    public Collection getChildren() {
        return this._children;
    }

    public void setChildren(final Collection children) {
        this._children = children;
    }

    public void addChild(final SelfRelationFolder child) {
        child.setParent(this);
        this._children.add(child);
    }

    public void setParent(final SelfRelationFolder parent) {
        this._parent = parent;
    }

    public SelfRelationFolder getParent() {
        return this._parent;
    }

    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    public long jdoGetTimeStamp() {
        return _timeStamp;
    }

}
