package ctf.jdo.tc20x;

import java.io.Serializable;
import org.exolab.castor.jdo.TimeStampable;


public class SelfRelationFolderParent implements Serializable, TimeStampable {
    private Integer _id                     = null;
	private String _name                    = null;
	
	private long _timeStamp;
  
  	public Integer getId() {
        return _id;
    }
    public void setId( Integer id ) {
        _id = id;
    }
  
    public String getName() {
        return _name;
    }
    public void setName( String name ) {
        _name = name;
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
