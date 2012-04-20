package org.castor.cpa.test.test2858;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class Payment implements TimeStampable {
    private String _oid;
    private FinanceTitle _myFinanceTitle;
    private FinanceTitle _myFinanceTitlePaid;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }
    
    public FinanceTitle getMyFinanceTitle() { return _myFinanceTitle; }
    public void setMyFinanceTitle(final FinanceTitle myFinanceTitle) { 
        _myFinanceTitle = myFinanceTitle; 
    }
    
    public FinanceTitle getMyFinanceTitlePaid() { return _myFinanceTitlePaid; }
    public void setMyFinanceTitlePaid(final FinanceTitle myFinanceTitlePaid) { 
        _myFinanceTitlePaid = myFinanceTitlePaid; 
    }

    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
}
