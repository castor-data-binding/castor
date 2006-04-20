/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

/**
 * Implements a reference to an object that will be resolved at a later
 * time using some resolver mechanism. A resolvable reference can be
 * created in both resolved and unresolved states. Resolvable
 * references are immutable by definition.
 * <p>
 * A resolverable reference has two states: resolved and unresolved.
 * When in the resolved state, the reference will always return the
 * same resolved object. When in the unresolved state, the first time
 * the object is requested, it will be resolved and returned. At that
 * point the reference becomes resolved and the same object is returned
 * in subsequent requests.
 * <p>
 * The following example creates a resolved and unresolved objects
 * and then resolved the two:
 * <pre>
 * ResolvableReference resolved, unresolved;
 *
 * resolved = new ResolvableReference( myObject );
 * unresolved = new ResolvableReference( "id", resolver );
 * if ( resolved.get() == myObject )
 *   ; // This will always be true
 * if ( unresolved.get() == resolver.resolve( "id" ) )
 *   ; // This will always be true
 * </pre>
 * <p>
 *
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @see Resolver
 * @version $Revision$ $Date$
**/ 
public final class ResolvableReference
{

    /**
     * Determines whether or not the reference can be resolved at the
     * time this method is called. A call to this method does not guarantee
     * a null value will not be returned by a call to the get() method.
    **/
    public boolean resolvable() {
        //-- if resolver is null, the referent must be known, or
        //-- never will be known, so return true
        if ( _resolver == null) return true;
        //-- otherwise check resolver to see if it can resolve the id
        return (_resolver.resolve(_id) != null);
    } //-- resolvable

    /**
     * Called to resolve the object and return it. The returned object
     * must be cast to the proper class. If a referent was specified
     * in the constructor, that referent will be returned. If an
     * identifier was specified, the resolved will be called to resolve
     * the object. The resolver will be called only the first time
     * this method is called. Subsequent calls will return the same
     * object.
     * <p>
     * Null is returned if the object was resolved to null.
     *
     * @return The resolved object
     */
    public Referable get()
    {
        // If resolver is null, the referent is known.
        if ( _resolver == null )
            return _referent;

        // Must synchronize, resolving should only occur once.
        // If two get methods get to this point at once, the
        // first one will resolve, the second one will not.
        synchronized ( this ) {
            if ( _resolver != null ) {
                _referent = _resolver.resolve( _id );
                _resolver = null;
            }
            return _referent;
        }
    }

    /**
     * Constructs a resolvable reference for the named object.
     * This reference will be resolved on demand by calling the
     * resolver with the specified identifier. The object need
     * not be resolvable until the {@link #get} method is called.
     *
     * @param id The object's identifier
     * @param resolver The resolve to use
     */
    public ResolvableReference( String id, Resolver resolver )
    {
        _id = id;
        _resolver = resolver;
    }


    /**
     * Constructs a resolvable reference for the given object.
     * This reference will always resolve to the specified referent.
     *
     * @param referent The object to resolve to
     */
    public ResolvableReference(Referable referent)
    {
        _referent = referent;
    }


    /**
     * The resolver used to resolve the object. This variable will
     * only be held while the reference is unresolved. Once the
     * reference has been resolved, this variable will be set to
     * null. Unused resolvers will be garbage collected.
     */
    private Resolver _resolver;


    /**
     * References the resolved object if passed from the constructor
     * or has been resolved by a call to {@link #get}. Once an
     * object has been resolved, the same object will always be
     * returned.
     */
    private Referable _referent;
    

    /**
     * The identifier to use for resolving the reference.
     */
    private String _id;

}

