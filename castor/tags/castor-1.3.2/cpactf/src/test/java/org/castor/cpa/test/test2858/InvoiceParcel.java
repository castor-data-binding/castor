package org.castor.cpa.test.test2858;

import java.util.Vector;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class InvoiceParcel implements TimeStampable {
    private String _oid;
    private Integer _parcelNumber;
    private Double _value;
    private Vector<FinanceTitleRelation> _myFinanceTitleRelation;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public Integer getParcelNumber() { return _parcelNumber; }
    public void setParcelNumber(final Integer parcelNumber) { _parcelNumber = parcelNumber; }
    
    public Double getValue() { return _value; }
    public void setValue(final Double value) { _value = value; }
    
    public Vector<FinanceTitleRelation> getMyFinanceTitleRelation() {
        return _myFinanceTitleRelation;
    }
    public void setMyFinanceTitleRelation(final Vector<FinanceTitleRelation> relation) { 
        _myFinanceTitleRelation = relation; 
    }
    public void addFinanceTitleRelation(final FinanceTitleRelation financeTitleRelation) {
        financeTitleRelation.setObjectType("InvoiceParcel");
        financeTitleRelation.setMyObject(this.getOid());
        if (_myFinanceTitleRelation == null) {
            _myFinanceTitleRelation = new java.util.Vector<FinanceTitleRelation>();
        }
        _myFinanceTitleRelation.addElement(financeTitleRelation);
    }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
}
