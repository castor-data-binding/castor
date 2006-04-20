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
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 */

package org.exolab.castor.xml.schema;

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
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
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

