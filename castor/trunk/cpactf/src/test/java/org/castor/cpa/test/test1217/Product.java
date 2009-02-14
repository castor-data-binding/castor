package org.castor.cpa.test.test1217;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author cwichoski
 */
public final class Product implements TimeStampable  {
    private String _oid;
    private String _code;
    private Double _value;
    private Person _company;
    private BasePart _part;
    private long _timeStamp;

    public Product() { }
    public Product(final String oid, final String code, final Double value,
            final Person company, final BasePart part) {
        _oid = oid;
        _code = code;
        _value = value;
        _company = company;
        _part = part;
    }

    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public String getCode() { return _code; }
    public void setCode(final String code) { _code = code; }
    
    public Double getValue() { return _value; }
    public void setValue(final Double value) { _value = value; }
    
    public Person getCompany() { return _company; }
    public void setCompany(final Person company) { _company = company; }
    
    public BasePart getPart() { return _part; }
    public void setPart(final BasePart part) { _part = part; }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public String toString() {
        return super.toString() + " { oid: '" + getOid() + "', code: '" + getCode()
            + "', value: '" + getValue() + "', company: '" + getCompany()
            + "', part: '" + getPart() + "', timestamp: " + jdoGetTimeStamp() + " }";
    }
}
