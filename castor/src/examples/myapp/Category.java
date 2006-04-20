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


    //public Enumeration getProducts()
    public Vector getProducts()
    {
        return _products;
        // return _products.elements();
    }


    public void addProduct( Product product )
    {
        if ( ! _products.contains( product ) ) {
            System.out.println( "Adding product " + product + " to category " + this );
            _products.addElement( product );
            product.addCategories( this );
        }
    }


    public String toString()
    {
        return "<id: " + _id + " name: " + _name + ">";
    }


}

