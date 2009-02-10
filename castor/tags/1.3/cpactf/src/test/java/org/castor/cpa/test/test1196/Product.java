package org.castor.cpa.test.test1196;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author cwichoski
 */
public class Product implements TimeStampable  {
    private String _oid;
    private String _name;
    private long _timeStamp;
    
    public final String getOid() { return _oid; }
    public final void setOid(final String oid) { _oid = oid; }

    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }
    
    public final long jdoGetTimeStamp() { return _timeStamp; }
    public final void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
}
