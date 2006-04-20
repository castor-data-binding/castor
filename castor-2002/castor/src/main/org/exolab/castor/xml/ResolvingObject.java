/**
 * Interface for an object that is associated with a resolver.
 * Typically the object will use the resolver to add and remove
 * objects that are added or removed to one of this object's
 * collection. The supplied resolver should implement a suitable
 * add/remove scheme. A resolving object need not necessarily make
 * use of the supplied resolver.
 * <p>
 * Not objects will implement add/remove on a resolver. Such objects
 * would not implement this interace and a simple <tt>instanceof</tt>
 * operation can be used to decide whether to call {@link
 * #useResolver}.
 *
 * @see Resolver
 */
 
package org.exolab.castor.xml;

public interface ResolvingObject
{

    public void useResolver( Resolver resolver );

}

