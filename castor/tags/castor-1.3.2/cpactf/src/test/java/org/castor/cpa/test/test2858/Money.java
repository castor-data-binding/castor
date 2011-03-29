package org.castor.cpa.test.test2858;

import org.junit.Ignore;


/**
 * @author cwichoski
 */
@Ignore
public class Money extends PaymentTitle {
    private Double _bankNoteValue;
    private Integer _quantity;
    
    public Double getBankNoteValue() { return _bankNoteValue; }
    public void setBankNoteValue(final Double bankNoteValue) { 
        _bankNoteValue = bankNoteValue; 
    }
    
    public Integer getQuantity() { return _quantity; }
    public void setQuantity(final Integer quantity) { _quantity = quantity; }
    
    public void calculate() {
        double value = _quantity.intValue() * _bankNoteValue.doubleValue();
        this.setValue(new Double(value));
    }
}
