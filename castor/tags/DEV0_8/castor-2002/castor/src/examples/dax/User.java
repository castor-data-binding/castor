package dax;


import java.util.Hashtable;
import java.util.Enumeration;


public class User
{

    // The identity of the user is contained in uid
    public String   uid;


    public String   full;


    public String   first;


    public String   last;


    public String[] email;


    public String   ou;


    public Hashtable attrSet;


    public String toString()
    {
	StringBuffer str;

	str = new StringBuffer();
	if ( uid != null )
	    str.append( "uid: " ).append( uid ).append( '\n' );
	if ( full != null )
	    str.append( "cn: " ).append( full ).append( '\n' );
	if ( first != null )
	    str.append( "sn: " ).append( first ).append( '\n' );
	if ( last != null )
	    str.append( "givenname: " ).append( last ).append( '\n' );
	if ( email != null ) {
	    for ( int i = 0 ; i < email.length ; ++i ) {
		str.append( "mail: " ).append( email[ i ]  ).append( '\n' );
	    }
	}
	if ( attrSet != null ) {
	    str.append( attrSet.toString() );
	}
	return str.toString();
    }


}
