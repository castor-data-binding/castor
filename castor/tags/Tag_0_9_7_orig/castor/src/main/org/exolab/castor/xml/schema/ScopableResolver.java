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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.util.Hashtable;

/**
 * An implementation of Resolver that can be "scoped". Which means you
 * can construct a ScopableResovler and give it another Resolver (which
 * can also be Scopable). If this Resolver cannot "resolve" the id, then
 * it will try to use the given Resolver to resolver it.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ScopableResolver implements Resolver {

    private Hashtable ids;

    private Resolver _resolver = null;

    public ScopableResolver() {
        ids = new Hashtable();
    } //-- ScopableResolver

    public ScopableResolver(Resolver resolver) {
        this();
        _resolver = resolver;
    } //-- ScopableResolver

    /**
     * Adds a resolvable object to this resolver identified by <tt>id</tt>.
     * Subsequent calls to {@link #resolve} with the same <tt>id</tt>
     * will return <tt>referent</tt>.
     *
     * @param id The referent's identifier
     * @param referent The referent object
     */
    public void addResolvable( String id, Referable referent ) {
        if (ids.get(id) != null) {
            //-- handle error
        }
        ids.put(id, referent);
    } //-- register

    /**
     * Removes a resolvable object from this resolver. Subsequent calls
     * to {@link #resolve} with the same <tt>id</tt> will return null.
     *
     * @param id The referent's identifier
     */
    public void removeResolvable( String id ) {
        Object obj = ids.get(id);
        if (obj != null) {
            ids.remove(id);
        }
    } //-- removeResolvable

    public Referable resolve(String id) {
        Referable referable = (Referable) ids.get(id);
        if ((referable == null) && (_resolver != null)) {
            referable = (Referable)_resolver.resolve(id);
        }
        return referable;
    } //-- resolve


} //-- ScopableResolver
