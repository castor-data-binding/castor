package oes;


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.PersistenceException;


public class Product
    implements Persistent
{


    private Database  _db;


    private String    _sku;


    private String    _shortName;


    private String    _description;


    private int       _categoryId;


    private Category  _category;


    private int       _priceId;


    private Price     _price;


    public String getSku()
    {
        return _sku;
    }


    public void setSku( String sku )
    {
        if ( sku.length() != 14 )
            throw new IllegalArgumentException( "Argument 'sku' must be 14 characters" );
        _sku = sku;
    }


    public String getShortName()
    {
        return _shortName;
    }


    public void setShortName( String shortName )
    {
        shortName = shortName.trim();
        if ( shortName.length() == 0 )
            throw new IllegalArgumentException( "Argument 'shortName' is an empty string" );
        _shortName = shortName;
    }


    public String getDescription()
    {
        return _description;
    }


    public void setDescription( String description )
    {
        description = description.trim();
        if ( description.length() == 0 )
            throw new IllegalArgumentException( "Argument 'description' is an empty string" );
        _description = description;
    }


    public int getCategoryId()
    {
        if ( _category != null )
            return _category.getCategoryId();
        return _categoryId;
    }



    public void setCategoryId( int id )
    {
        _categoryId = id;
        _category = null;
    }


    public Category getCategory()
        throws PersistenceException
    {
        if ( _category != null )
            return _category;
        _category = (Category) _db.load( Category.class, new Integer( _categoryId ) );
        return _category;
    }


    public void setCategory( Category category )
    {
        _category = category;
        _categoryId = category.getCategoryId();
    }


    public int getPriceId()
    {
        if ( _price != null )
            return _price.getPriceId();
        return _priceId;
    }


    public void setPriceId( int id )
    {
        _priceId = id;
        _price = null;
    }


    public Price getPrice()
        throws PersistenceException
    {
        if ( _price != null )
            return _price;
        _price = (Price) _db.load( Price.class, new Integer( _priceId ) );
        return _price;
    }


    public void setPrice( Price price )
    {
        _price = price;
        _priceId = price.getPriceId();
    }


    public void jdoPersistent( Database db )
    {
        _db = db;
    }


    public void jdoTransient()
    {
        _db = null;
        _price = null;
        _category = null;
    }


    public Class jdoLoad(short accessMode)
    {
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


}















