package myapp;


import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;


public class Product implements Persistent
{


    private int          _id;


    private String       _name;


    private float        _price;


    private ProductGroup _group;


    private Database     _db;

    /** Java 1.2
    private java.util.ArrayList  _details = new java.util.ArrayList();
     */
    private Vector       _details = new Vector();


    private Vector       _categories = new Vector();


    public int getId()
    {
        return _id;
    }


    public void setId( int id )
    {
        _id = id;
    }


    public String getName()
    {
        return _name;
    }


    public void setName( String name )
    {
        _name = name;
    }


    public float getPrice()
    {
        return _price;
    }


    public void setPrice( float price )
    {
        _price = price;
    }


    public ProductGroup getGroup()
    {
        return _group;
    }


    public void setGroup( ProductGroup group )
    {
        _group = group;
    }


    public ProductDetail createDetail()
    {
        return new ProductDetail();
    }


    /** Java 1.2
    public java.util.ArrayList getDetails()
    {
        return _details;
    }
    */


    public Vector getDetails()
    {
        return _details;
    }


    public void addDetail( ProductDetail detail )
    {
        _details.add( detail );
        detail.setProduct( this );
    }


    public Vector getCategories()
    {
        return _categories;
    }


    public void addCategories( Category category )
    {
        if ( ! _categories.contains( category ) ) {
            _categories.addElement( category );
            category.addProduct( this );
        }
    }


    public void jdoPersistent( Database db )
    {
        _db = db;
    }


    public void jdoTransient()
    {
        _db = null;
    }


    public Class jdoLoad(short accessMode)
    {
        if ( _name.indexOf("PC") >= 0 ) {
            return Computer.class;
        }
        return null;
    }


    public void jdoBeforeCreate( Database db )
    {
    }


    public void jdoAfterCreate()
    {
    }


    public void jdoStore(boolean modified)
    {
    }


    public void jdoBeforeRemove()
    {
    }


    public void jdoAfterRemove()
    {
    }


    public void jdoUpdate()
    {
    }


    public String toString()
    {
        return "<id: " + _id + " name: " + _name + ">";
    }

}
