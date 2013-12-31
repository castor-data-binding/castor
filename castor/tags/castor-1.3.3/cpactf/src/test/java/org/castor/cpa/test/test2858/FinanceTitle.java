package org.castor.cpa.test.test2858;

import java.util.Date;
import java.util.Vector;
import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class FinanceTitle implements TimeStampable {
    private String _oid;
    private Date _date;
    private Double _value;
    private long _timeStamp;
    private Vector<Payment> _myPayments;
    private Vector<Payment> _myTitlesPaid;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }
    
    public Date getDate() { return _date; }
    public void setDate(final Date date) { _date = date; }
    
    public Double getValue() { return _value; }
    public void setValue(final Double value) { _value = value; }

    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public Vector<Payment> getMyPayments() { return _myPayments; }
    public void setMyPayments(final Vector<Payment> myPayments) { 
        _myPayments = myPayments; 
    }
    
    public Vector<Payment> getMyTitlesPaid() { return _myTitlesPaid; }
    public void setMyTitlesPaid(final Vector<Payment> myTitlesPaid) {
        _myTitlesPaid = myTitlesPaid;
    }
    
    public void addPayment(final Payment payment) {
        payment.setMyFinanceTitlePaid(this);
        if (_myPayments == null) {
            _myPayments = new java.util.Vector<Payment>();
        }
        _myPayments.addElement(payment);
    }
    
    public void addTitlePaid(final Payment payment) {
        payment.setMyFinanceTitle(this);
        if (_myTitlesPaid == null) {
            _myTitlesPaid = new java.util.Vector<Payment>();
        }
        _myTitlesPaid.addElement(payment);
    }
}
