package xml;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Date;


public class Types
{


    private boolean  _boolean;


    private byte[]   _bytes;


    private int      _integer;


    private float    _float;


    private String   _string;


    private Date     _date;


    private Vector   _vector = new Vector();


    private Hashtable  _hashtable = new Hashtable();


    public Types( boolean set )
    {
        if ( set ) {
            _boolean = true;
            _bytes = new byte[] { 5, 6, 7, 8, 9, 10 };
            _integer = 5678;
            _float = 1234.5678F;
            _string = "the quick brown fox";
            _date = new Date();
            _vector = new Vector();
            _vector.addElement( "first" );
            _vector.addElement( "second" );
            _vector.addElement( "third" );
            _hashtable = new Hashtable();
            _hashtable.put( "1", "first" );
            _hashtable.put( "2", "second" );
            _hashtable.put( "3", "third" );
        }
    }


    public Types()
    {
    }


    public boolean getBoolean()
    {
        return _boolean;
    }


    public void setBoolean( boolean value )
    {
        _boolean = value;
    }


    public byte[] getBytes()
    {
        return _bytes;
    }


    public void setBytes( byte[] value )
    {
        _bytes = (byte[]) value.clone();
    }


    public int getInteger()
    {
        return _integer;
    }


    public void setInteger( int value )
    {
        _integer = value;
    }


    public float getFloat()
    {
        return _float;
    }


    public void setFloat( float value )
    {
        _float = value;
    }


    public String getString()
    {
        return _string;
    }


    public void setString( String value )
    {
        _string = value;
    }


    public Date getDate()
    {
        return _date;
    }


    public void setDate( Date value )
    {
        _date = value;
    }


    public Vector getVector()
    {
        return _vector;
    }


    public Hashtable getHashtable()
    {
        return _hashtable;
    }


    public boolean equals( Object other )
    {
        if ( this == other )
            return true;
        if ( other == null || ! ( other instanceof Types ) )
            return false;

        Types x;

        x = (Types) other;
        if ( x._boolean != _boolean )
            return false;
        if ( x._bytes != _bytes ) {
            if ( x._bytes == null || _bytes == null || x._bytes.length != _bytes.length )
                return false;
            for ( int i = 0 ; i < x._bytes.length ; ++i )
                if ( x._bytes[ i ] != _bytes[ i ] )
                    return false;
        }
        if ( x._integer != _integer || x._float != _float )
            return false;
        if ( x._string != _string && ( x._string == null || ! x._string.equals( _string) ) )
            return false;
        if ( x._date != _date ) {
            if ( x._date == null || ! x._date.equals( _date ) )
                return false;
        }
        if ( x._vector != _vector ) {
            if ( x._vector == null || _vector == null || x._vector.size() != _vector.size() )
                return false;
            for ( int i = 0 ; i < x._vector.size() ; ++i )
                if ( ! x._vector.elementAt( i ).equals( _vector.elementAt( i ) ) )
                    return false;
        }
        if ( x._hashtable != _hashtable ) {
            Enumeration enum;
            
            if ( x._hashtable == null || _hashtable == null || x._hashtable.size() != _hashtable.size() )
                return false;
            enum = x._hashtable.elements();
            while ( enum.hasMoreElements() )
                if ( ! _hashtable.contains( enum.nextElement() ) )
                    return false;
        }
        return true;
    }



}


