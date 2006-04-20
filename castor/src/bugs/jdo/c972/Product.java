package jdo.c972;

import org.exolab.castor.jdo.TimeStampable;

public class Product implements TimeStampable {
    private int          _id;
    private String       _name;
    private float        _price;
    private ProductGroup _group;
    private long         _timeStamp;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public float getPrice() { return _price; }
    public void setPrice(final float price) { _price = price; }

    public ProductGroup getGroup() { return _group; }
    public void setGroup(final ProductGroup group) { _group = group; }

    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
    public long jdoGetTimeStamp() { return _timeStamp; }

    public String toString() {
        return "<id: " + _id + " name: " + _name + " price: " + _price + ">";
    }
}
