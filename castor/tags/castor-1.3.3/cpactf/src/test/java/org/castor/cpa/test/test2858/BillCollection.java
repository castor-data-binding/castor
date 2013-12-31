package org.castor.cpa.test.test2858;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class BillCollection extends FinanceTitle {
    private String _billCollectionType;
    
    public String getBillCollectionType() { return _billCollectionType; }
    public void setBillCollectionType(final String billCollectionType) { 
        _billCollectionType = billCollectionType;
    }
}
