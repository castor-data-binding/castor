package dax;


import java.util.Hashtable;
import java.util.Enumeration;


public class Record
{

    public RDN rdn;

    public Hashtable misc;


    public String toString()
    {
	StringBuffer str;
	Enumeration enum;
	String      name;

	str = new StringBuffer();
	if ( rdn != null ) {
	    str.append( "ou: " ).append( rdn.ou ).append( '\n' );
	    str.append( "uid: " ).append( rdn.uid ).append( '\n' );
	}
	if ( misc != null ) {
	    enum = misc.keys();
	    while ( enum.hasMoreElements() ) {
		name = (String) enum.nextElement();
		str.append( name ).append( ": " ).append( misc.get( name ) ).append( '\n' );
	    }
	}
	return str.toString();
    }


}


