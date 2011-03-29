package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class InvoiceItem extends BusinessObject {
    Invoice _invoice;
    Product _product;
    Integer _quantity;
    Double _price;
    Double _total;
    
    public Invoice getInvoice() { return _invoice;  }
    public void setInvoice(final Invoice invoice) {   _invoice = invoice; }
    
    public Product getProduct() { return _product;  }
    public void setProduct(final Product product) { _product = product; }
    
    public Integer getQuantity() { return _quantity; }
    public void setQuantity(final Integer quantity) { _quantity = quantity; }
    
    public Double getPrice() { return _price; }
    public void setPrice(final Double price) { _price = price; }
    
    public Double getTotal() { return _total; }
    public void setTotal(final Double total) { _total = total; }
}