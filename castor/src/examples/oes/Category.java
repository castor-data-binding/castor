package oes;


import java.util.Collection;
import java.util.ArrayList;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;


public class Category
    implements Persistent
{


    private Database  _db;


    private int       _categoryId;


    private String    _shortName;


    private String    _description;


    private Collection _products;


    public int getCategoryId()
    {
        return _categoryId;
    }


    public void setCategoryId( int id )
    {
        _categoryId = id;
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


    public Collection getProducts()
        throws PersistenceException
    {
        Query        qry;
        QueryResults res;

        if ( _products == null ) {
            _products = new ArrayList();
            qry = _db.getOQLQuery( "SELECT p FROM Product p WHERE p.category=$1" );
            qry.bind( _categoryId );
            res = qry.execute();
            while ( res.hasMore() )
                _products.add( res.next() );
        }
        return _products;
    }


    public void jdoPersistent( Database db )
    {
        _db = db;
    }


    public void jdoTransient()
    {
        _db = null;
        _products = null;
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

