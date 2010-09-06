package org.castor.cpa.test.test2858;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class FinanceTitleRelation implements TimeStampable {
    private String _oid;
    private String _objectType;
    private String _myObject;
    private FinanceTitle _myFinanceTitle;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }
    
    public String getObjectType() { return _objectType; }
    public void setObjectType(final String objectType) { 
        _objectType = objectType; 
    }
    
    public String getMyObject() { return _myObject; }
    public void setMyObject(final String object) { 
        _myObject = object; 
    }
    
    public FinanceTitle getMyFinanceTitle() { return _myFinanceTitle; }
    public void setMyFinanceTitle(final FinanceTitle myFinanceTitle) { 
        _myFinanceTitle = myFinanceTitle; 
    }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
}