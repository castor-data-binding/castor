package org.castor.cpa.test.test1379;

import org.exolab.castor.jdo.TimeStampable;

public class Product implements TimeStampable {
    private int          _id;
    private String       _name;
    private float        _price;
    private long         _timeStamp;

    public final int getId() { return _id; }
    public final void setId(final int id) { _id = id; }

    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }

    public final float getPrice() { return _price; }
    public final void setPrice(final float price) { _price = price; }
    
    public final void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
    public final long jdoGetTimeStamp() { return _timeStamp; }

    public final String toString() {
        return "<id: " + _id + " name: " + _name + " price: " + _price + ">";
    }
}
