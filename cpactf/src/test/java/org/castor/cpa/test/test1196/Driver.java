package org.castor.cpa.test.test1196;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author cwichoski
 */
public final class Driver implements TimeStampable  {
    private String _oid;
    private String _name;
    private Car _car;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Car getCar() { return _car; }
    public void setCar(final Car car) { _car = car; }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
}
