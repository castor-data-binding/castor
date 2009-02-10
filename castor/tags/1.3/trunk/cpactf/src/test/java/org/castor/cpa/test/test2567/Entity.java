package org.castor.cpa.test.test2567;

import java.text.DateFormat;
import java.util.Date;

public final class Entity {
    //------------------------------------------------------------------------------------

    private String _partName;
    
    private Date _loadDate;
    
    private Date _requestDate;
    
    private String _orderNumberText;
    
    private transient long _orderNumber;

    private transient String _areaName;
    
    //------------------------------------------------------------------------------------

    public String getPartName() { return _partName; }
    
    public void setPartName(final String name) {
        _partName = null;
        if (name != null) {
            String temp = name.trim();
            _partName = ("".equals(temp)) ? null : temp;
        }
    }
    
    public Date getLoadDate() { return _loadDate; }
    
    public void setLoadDate(final Date loadDate) {
        _loadDate = loadDate;
    }
    
    public Date getRequestDate() { return _requestDate; }
    
    public void setRequestDate(final Date requestDate) {
        _requestDate = requestDate;
    }

    public String getOrderNumberText() { return _orderNumberText; }
    
    public void setOrderNumberText(final String orderNumber) {
        _orderNumberText = null;
        if (orderNumber != null) {
            String temp = orderNumber.trim();
            _orderNumberText = ("".equals(temp)) ? null : temp;
        }
    }

    //------------------------------------------------------------------------------------

    public long getOrderNumber() { return _orderNumber; }
    
    public void setOrderNumber(final long orderNumber) {
        _orderNumber = orderNumber;
    }

    public String getAreaName() { return _areaName; }
    
    public void setAreaName(final String name) {
        _areaName = name;
    }
    
    //------------------------------------------------------------------------------------

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<RtSord");
        sb.append(" partName='").append(_partName).append("'");
        DateFormat df = DateFormat.getDateInstance();
        sb.append(" loadDate='").append(df.format(_loadDate)).append("'");
        sb.append(" requestDate='").append(df.format(_requestDate)).append("'");
        sb.append(" orderNumberText='").append(_orderNumberText).append("'");
        sb.append(" orderNumber='").append(_orderNumber).append("'");
        sb.append("/>");
        return sb.toString();
    }

    //------------------------------------------------------------------------------------
}
