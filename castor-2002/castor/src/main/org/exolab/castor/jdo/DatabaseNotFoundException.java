package org.exolab.castor.jdo;


/**
 * This exception is thrown when attempting to open a database that
 * does not exist.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseNotFoundException
    extends PersistenceException
{
    

    public DatabaseNotFoundException( String message )
    {
        super( message );
    }


}

