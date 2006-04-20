
package org.exolab.castor.jdo;

/**
 * This exception is thrown when accessing an object that was deleted.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class ObjectDeletedException
    extends ObjectNotPersistentException
{
    
    public ObjectDeletedException( String message )
    {
        super( message );
    }

    public ObjectDeletedException( String message, Throwable exception )
    {
        super( message, exception );
    }

    
} 
