
package org.exolab.castor.jdo;

/**
 * This exception is thrown when accessing an object that was deleted.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class ObjectDeletedException extends ObjectNotPersistentException {
    /** SerialVersionUID */
    private static final long serialVersionUID = -5294966338473275287L;

    public ObjectDeletedException(final String message) {
        super(message);
    }

    public ObjectDeletedException(final String message, final Throwable exception) {
        super(message, exception);
    }
} 
