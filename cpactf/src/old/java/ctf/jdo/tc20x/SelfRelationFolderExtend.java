package ctf.jdo.tc20x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;

public final class SelfRelationFolderExtend
extends SelfRelationFolderParent
implements Serializable, TimeStampable {
    private static final long serialVersionUID = -4455086340402957100L;

    private Collection<SelfRelationFolderExtend> _children =
        new ArrayList<SelfRelationFolderExtend>();
    
    private SelfRelationFolderExtend _parent  = null;
  
    public Collection<SelfRelationFolderExtend> getChildren() {
        return _children;
    }
    
    public void setChildren(final Collection<SelfRelationFolderExtend> children) {
        _children = children;
    }
    
    public void addChild(final SelfRelationFolderExtend child) {
        child.setParent(this);
        _children.add(child);
    }
    
    public SelfRelationFolderExtend getParent() { return _parent; }
    public void setParent(final SelfRelationFolderExtend parent) { _parent = parent; }
}
