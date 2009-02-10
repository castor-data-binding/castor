package myapp;

import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.mapping.AccessMode;

public class Product implements Persistent, TimeStampable {
    private int          _id;

    private String       _name;

    private float        _price;

    private ProductGroup _group;

    private long   _timeStamp;

    private Vector       _details = new Vector();

    private Vector       _categories = new Vector();

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    public float getPrice() {
        return _price;
    }

    public void setPrice(final float price) {
        _price = price;
    }

    public ProductGroup getGroup() {
        return _group;
    }

    public void setGroup(final ProductGroup group) {
        _group = group;
    }

    public ProductDetail createDetail() {
        return new ProductDetail();
    }

    public Vector getDetails() {
        return _details;
    }


    public void addDetail(final ProductDetail detail) {
        _details.add( detail );
        detail.setProduct( this );
    }

    public Vector getCategories() {
        return _categories;
    }

    public void addCategories(final Category category) {
        if ( ! _categories.contains( category ) ) {
            _categories.addElement( category );
            category.addProduct( this );
        }
    }

    public void jdoPersistent(final Database db) { }

    public void jdoTransient() { }

    public Class jdoLoad(final AccessMode accessMode) {
        if ( _name.indexOf("PC") >= 0 ) {
            return Computer.class;
        }
        return null;
    }

    public void jdoBeforeCreate(final Database db) { }

    public void jdoAfterCreate() { }

    public void jdoStore(final boolean modified) { }

    public void jdoBeforeRemove() { }

    public void jdoAfterRemove() { }

    public void jdoUpdate() { }

    public void jdoSetTimeStamp(final long timeStamp) {
        // System.out.println( "CHANGING TIMESTAMP FROM: " + _timeStamp + " TO: " + timeStamp );
        _timeStamp = timeStamp;
    }

    public long jdoGetTimeStamp() {
        // System.out.println( "GRABBING TIMESTAMP " + _timeStamp );
        return _timeStamp;
    }

    public String toString() {
        return "<id: " + _id + " name: " + _name + " price: " + _price + ">";
    }
}
