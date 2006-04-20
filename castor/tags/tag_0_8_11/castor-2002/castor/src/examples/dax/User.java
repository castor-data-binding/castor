package dax;


import java.util.Hashtable;
import java.util.Enumeration;


public class User
{

    // The identity of the user is contained in uid
    private String   _uid;


    public String   _full;


    public String   _first;


    public String   _last;


    public String[] _email;


    public String   _ou;


    public Hashtable _attrSet;


    public String getUid()
    {
        return _uid;
    }


    public void setUid( String uid )
    {
        _uid = uid;
    }


    public String getFull()
    {
        return _full;
    }


    public void setFull( String full )
    {
        _full = full;
    }


    public String getFirst()
    {
        return _first;
    }


    public void setFirst( String first )
    {
        _first = first;
    }


    public String getLast()
    {
        return _last;
    }


    public void setLast( String last )
    {
        _last = last;
    }


    public String[] getEmail()
    {
        return _email;
    }


    public void setEmail( String[] email )
    {
        _email = email;
    }


    public String getOu()
    {
        return _ou;
    }


    public void setOu( String ou )
    {
        _ou = ou;
    }


    public Hashtable attrSet()
    {
        return _attrSet;
    }


    public void setAttrSet( Hashtable attrSet )
    {
        _attrSet = attrSet;
    }


    public String toString()
    {
	StringBuffer str;

	str = new StringBuffer();
	if ( _uid != null )
	    str.append( "uid: " ).append( _uid ).append( '\n' );
	if ( _full != null )
	    str.append( "cn: " ).append( _full ).append( '\n' );
	if ( _first != null )
	    str.append( "sn: " ).append( _first ).append( '\n' );
	if ( _last != null )
	    str.append( "givenname: " ).append( _last ).append( '\n' );
	if ( _email != null ) {
	    for ( int i = 0 ; i < _email.length ; ++i ) {
		str.append( "mail: " ).append( _email[ i ]  ).append( '\n' );
	    }
	}
	if ( _attrSet != null ) {
	    str.append( _attrSet.toString() );
	}
	return str.toString();
    }


}
