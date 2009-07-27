package org.castor.cpa.test.test1196;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author cwichoski
 */
public final class OrderItem implements TimeStampable  {
    private String _oid;
    private Integer _quantity;
    private Product _product;
    private Order _parent;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public Integer getQuantity() { return _quantity; }
    public void setQuantity(final Integer quantity) { _quantity = quantity; }
    
    public Product getProduct() { return _product; }
    public void setProduct(final Product product) { _product = product; }
    
    public Order getParent() { return _parent; }
    public void setParent(final Order parent) { _parent = parent; }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public String toString() {
        return super.toString() + " { oid: '" + getOid()
            + "', quantity: '" + getQuantity() + "', product: '" + getProduct()
            + "', timestamp: " + jdoGetTimeStamp() + " }";
    }
}
