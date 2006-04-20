package org.exolab.castor.jdo;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * This exception is thrown when attempting to open a database that
 * does not exist.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseNotFoundException
    extends PersistenceException
{
    

    private Exception  _except;

    
    public DatabaseNotFoundException( String message )
    {
        super( message );
    }


    public DatabaseNotFoundException( Exception except )
    {
        super( except.getMessage() );
        _except = except;
    }


    public Exception getException()
    {
        return _except;
    }
    
    
    public void printStackTrace()
    {
        if ( _except == null )
            super.printStackTrace();
        else
            _except.printStackTrace();
    }
    
    
    public void printStackTrace( PrintStream print )
    {
        if ( _except == null )
            super.printStackTrace( print );
        else
            _except.printStackTrace( print );
    }
    
    
    public void printStackTrace( PrintWriter print )
    {
        if ( _except == null )
            super.printStackTrace( print );
        else
            _except.printStackTrace( print );
    }
    

}

