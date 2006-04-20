
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
    

    /**
     * Construct an instance of the exception.
     *
     * @param msg A string providing a description of the exception.
     */
    public ObjectDeletedException( String message )
    {
        super( message );
    }


} 
