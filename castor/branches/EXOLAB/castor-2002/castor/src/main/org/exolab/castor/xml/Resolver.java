/*
 * Exoffice Code Header here
 */

package org.exolab.castor.xml;

/**
 * Defines an object resolver but does not specify any implementation.
 * <p>
 * A lazy resolver would resolve an object given its identifier.
 * The reference to the object will be created with a {@link
 * ResolvableReference} object using the resolved and the identifier.
 * When the object is requested, the {@link #resolve} method will be
 * called to obtain it.
 * <p>
 * Some implementation will add and remove resolvable objects, e.g.
 * a collection of objects that also acts as a resolver. These methods
 * should implement {@link #addResolvable} and {@link #removeResolvable}.
 * Resolvers that do not implement these methods are still considered
 * valid resolvers. For example, a database based resolver will operate
 * consistently without implementing add/remove not through the database
 * interface.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @see ResolvableReference
**/ 
public interface Resolver
{


    /**
     * Called to resolve a reference give the reference's identifier.
     * If the reference is known, this method should return the referenced
     * object. If the reference is unknown, this method should return
     * null.
     *
     * @param id The identifier to resolve
     * @return The resolved object
    **/
    public Referable resolve( String id );


    /**
     * Adds a resolvable object to this resolver identified by <tt>id</tt>.
     * Subsequent calls to {@link #resolve} with the same <tt>id</tt>
     * will return <tt>referent</tt>.
     *
     * @param id The referent's identifier
     * @param referent The referent object
     */
    public void addResolvable( String id, Referable referent );


    /**
     * Removes a resolvable object from this resolver. Subsequent calls
     * to {@link #resolve} with the same <tt>id</tt> will return null.
     *
     * @param id The referent's identifier
     */
    public void removeResolvable( String id );


}

