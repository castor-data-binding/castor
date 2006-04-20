package myapp;


import java.util.Vector;
import java.util.Enumeration;


public class Category
{


    private int      _id;


    private Vector   _products = new Vector();


    private String   _name;


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


    public Enumeration getProduct()
    {
        return _products.elements();
    }


    public void addProduct( Product product )
    {
        if ( ! _products.contains( product ) ) {
            _products.addElement( product );
            product.addCategory( this );
        }
    }


    public String toString()
    {
        return _id + " " + _name;
    }


}

