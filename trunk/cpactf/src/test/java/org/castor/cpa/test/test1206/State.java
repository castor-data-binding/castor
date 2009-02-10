package org.castor.cpa.test.test1206;

import org.exolab.castor.jdo.TimeStampable;

public final class State implements TimeStampable {
    private String _oid;
    private String _name;
    private Country _country;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public Country getCountry() { return _country; }
    public void setCountry(final Country country) { _country = country; }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    public String toString() {
        return super.toString() + " { oid: '" + getOid() + "', name: '" + getName()
            + "', country: '" + getCountry().getOid()
            + "', timestamp: " + jdoGetTimeStamp() + " }";
    }
}
