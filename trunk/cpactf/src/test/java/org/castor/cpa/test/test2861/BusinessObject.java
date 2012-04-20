package org.castor.cpa.test.test2861;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class BusinessObject implements TimeStampable {
    private String _oid;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }    
}