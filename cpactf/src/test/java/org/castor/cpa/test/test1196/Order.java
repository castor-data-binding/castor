package org.castor.cpa.test.test1196;

import java.util.Vector;
import org.exolab.castor.jdo.TimeStampable;

/**
 * @author cwichoski
 */
public final class Order implements TimeStampable  {
    private String _oid;
    private Integer _number;
    private Vector < OrderItem > _orderItems;
    private long _timeStamp;
    
    public String getOid() { return _oid; }
    public void setOid(final String oid) { _oid = oid; }

    public Integer getNumber() { return _number; }
    public void setNumber(final Integer number) { _number = number; }
    
    public Vector < OrderItem > getOrderItems() { return _orderItems; }
    public void setOrderItems(final Vector < OrderItem > orderItem) {
        _orderItems = orderItem;
    }
    public void addOrderItem(final OrderItem orderItem) {
        orderItem.setParent(this);
        if (_orderItems == null) {
            _orderItems = new Vector < OrderItem > ();
        }
        _orderItems.add(orderItem);
    }
    public boolean removeOrderItem(final String oid) {
        boolean removed = false;
        if (_orderItems != null) {
            OrderItem orderItem = null;
            for (int n = 0; n < _orderItems.size(); n++) {
                orderItem = _orderItems.get(n);
                if (orderItem.getOid().equals(oid)) {
                    _orderItems.remove(n);
                    removed = true;
                    break;
                }
            }
        }
        return removed;
    }
    public OrderItem getOrderItem(final String oid) {
        OrderItem orderItem = null;
        if (_orderItems != null) {
            for (int n = 0; n < _orderItems.size(); n++) {
                orderItem = _orderItems.get(n);
                if (orderItem.getOid().equals(oid)) {
                    break;
                }
            }
        }
        return orderItem;
    }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public String toString() {
        return super.toString() + " { oid: '" + getOid()
            + "', number: '" + getNumber()
            + "', orderItems: '" + getOrderItems()
            + "', timestamp: " + jdoGetTimeStamp() + " }";
    }
}
