package org.castor.cpa.test.test2861;

import java.util.Vector;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class Invoice extends BusinessObject {
    Integer _number;
    Person _emitter;
    Person _billTo;
    Vector<InvoiceItem> _invoiceItem;
    
    public Integer getNumber() { return _number; }
    public void setNumber(final Integer number) { _number = number; }
    
    public Person getEmitter() { return _emitter; }
    public void setEmitter(final Person emitter) { _emitter = emitter; }
    
    public Person getBillTo() { return _billTo; }
    public void setBillTo(final Person billTo) {    _billTo = billTo; }
    
    public Vector<InvoiceItem> getInvoiceItem() { return _invoiceItem;  }
    public void setInvoiceItem(final Vector<InvoiceItem> invoiceItem) {
        _invoiceItem = invoiceItem;
    }
}
