package ctf.jdo.tc20x;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import org.exolab.castor.jdo.TimeStampable;


public class SelfRelationFolderExtend 
    extends SelfRelationFolderParent 
    implements Serializable, TimeStampable 
{
    private Integer _id                     = null;
    private Collection _children            = new ArrayList();
	private SelfRelationFolderExtend _parent  = null;
	
	private long _timeStamp;
  
  	public Integer getId() {
        return _id;
    }
    public void setId( Integer id ) {
        _id = id;
    }
  
	public Collection getChildren() {
		return this._children;
	}
	
	public void setChildren( Collection children ) {
		this._children = children;
	}
	
	
	public void addChild( SelfRelationFolderExtend child ) {
		child.setParent( this );
		this._children.add( child );
	}
	
	
	public void setParent( SelfRelationFolderExtend parent ) {
		this._parent = parent;
	}
	
	public SelfRelationFolderExtend getParent() {
		return this._parent;
	}
	
	public void jdoSetTimeStamp( long timeStamp )
    {
        _timeStamp = timeStamp;
    }


    public long jdoGetTimeStamp()
    {
        return _timeStamp;
    }
	
}
