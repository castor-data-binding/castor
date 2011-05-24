package org.castor.cpa.test.test2858;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class PaymentTitle extends FinanceTitle {
    private String _paymentType;
    
    public String getPaymentType() { return _paymentType; }
    public void setPaymentType(final String paymentType) { 
        _paymentType = paymentType; 
    }
}
