package ctf.jdo.tc20x;

import java.io.Serializable;

import org.exolab.castor.jdo.TimeStampable;

public class SelfRelationFolderParent implements Serializable, TimeStampable {
    private static final long serialVersionUID = 3809114535230964610L;

    private Integer _id = null;
    private String _name = null;
    private long _timeStamp;
  
    public final Integer getId() { return _id; }
    public final void setId(final Integer id) { _id = id; }
  
    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }
    
    public final long jdoGetTimeStamp() { return _timeStamp; }
    public final void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
}
