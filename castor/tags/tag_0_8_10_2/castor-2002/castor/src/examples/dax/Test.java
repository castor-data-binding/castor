package dax;


import java.util.Enumeration;
import java.net.URL;
import org.xml.sax.InputSource;
import org.exolab.castor.dax.engine.DirectorySourceImpl;
import org.exolab.castor.dax.Directory;
import org.exolab.castor.dax.DirectorySource;
import org.exolab.castor.dax.Search;


public class Test
{


    public static void main( String[] args )
    {
	try {
	    DirectorySourceImpl dirs;
	    Directory           dir;

	    DirectorySourceImpl.loadMapping( new InputSource( Test.class.getResource( "mapping.xml" ).toString() ) );

	    dirs = new DirectorySourceImpl();
	    dirs.setURL( "ldap://localhost/ou=people,dc=intalio,dc=com" );
	    dir = dirs.getDirectory( "dc=intalio,dc=com", "secret" );
	    dir.begin();

	    User                user;
	    Search              search;
	    Enumeration         results;

	    search = dir.createSearch( "(ou=people)" );
	    results = search.execute();
	    while ( results.hasMoreElements() ) {
                user = (User) results.nextElement();
                if ( user.getFull() == null )
                  user.setFull( user.getFirst() + " " + user.getLast() );
                System.out.println( user );
	    }
	    dir.commit();

            dir.begin();
      user = (User) dir.read( "arkin" );
	    if ( user == null || user != null ) {
		user = new User();
		user.setUid( "kvisco" );
		user.setFirst( "Keith" );
		user.setLast( "Visco" );
		user.setFull( "Keith Visco" );
		user.setEmail( new String[] { "kvisco@intalio.com" } );
		System.out.println( "Creating: " + user );
		dir.create( user );
	    } else {
		System.out.println( "Query: " + user );
		dir.delete( user );
	    }
	    dir.commit();


	    /*
	    Record              rec;

	    dirs = new DirectorySourceImpl();
	    dirs.setURL( "ldap://localhost/dc=intalio,dc=com" );
	    dir = dirs.getDirectory( "dc=intalio,dc=com", "secret" );
	    dir.begin();
      rec = (Record) dir.read( new RDN( "people", "arkin" ) );
	    System.out.println( rec );
	    dir.commit();
	    */


	} catch ( Exception except ) {
	    System.out.println( except );
	    except.printStackTrace();
	}
    }


}
