package org.castor.cpa.test.test1196;

/**
 * @author cwichoski
 */
public final class Computer extends Product {
    private String _number;
    private OrderItem _orderItem;
    
    public String getNumber() { return _number; }
    public void setNumber(final String number) { _number = number; }
    
    public OrderItem getOrderItem() { return _orderItem; }
    public void setOrderItem(final OrderItem orderItem) { _orderItem = orderItem; }
}
