package myapp;


public class ProductDetail
{


    private int      _id;


    private Product  _product;


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


    public Product getProduct()
    {
        return _product;
    }


    public void setProduct( Product product )
    {
        _product = product;
    }


    public String toString()
    {
        return _id + " " + _name;
    }


}

