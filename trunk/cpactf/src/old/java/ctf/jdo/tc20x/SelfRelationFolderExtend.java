package ctf.jdo.tc20x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;

public final class SelfRelationFolderExtend
extends SelfRelationFolderParent
implements Serializable, TimeStampable {
    private static final long serialVersionUID = -4455086340402957100L;

//    private Integer _id = null;
    private Collection _children = new ArrayList();
    private SelfRelationFolderExtend _parent  = null;
//    private long _timeStamp;
  
//    public Integer getId() { return _id; }
//    public void setId(final Integer id) { _id = id; }
  
    public Collection getChildren() { return _children; }
    public void setChildren(final Collection children) { _children = children; }
    public void addChild(final SelfRelationFolderExtend child) {
        child.setParent(this);
        _children.add(child);
    }
    
    public SelfRelationFolderExtend getParent() { return _parent; }
    public void setParent(final SelfRelationFolderExtend parent) { _parent = parent; }
    
//    public long jdoGetTimeStamp() { return _timeStamp; }
//    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
}
