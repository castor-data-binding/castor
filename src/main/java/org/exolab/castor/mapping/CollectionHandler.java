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


package org.exolab.castor.mapping;


import java.util.Enumeration;


/**
 * Collection handler for adding/listing elements of a collection.
 * A collection field will use this handler to add elements when it's
 * value is set, and to enumerate then when it's value is retrieved.
 * A collection handler is instantiated only once, must be thread
 * safe and not use any synchronization.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public interface CollectionHandler
{


    /**
     * Add an object to the collection. A collection may not allow the
     * same object to be added more than once. The collection is provided
     * as a parameter and is returned as the return value if the returned
     * collection is a different object. That way the handler can create
     * a new collection or change the collection as necessary (e.g. when
     * resizing an array).
     *
     * @param collection The collection, null if no collection has
     *  been created yet
     * @param object The object to add to the collection
     * @return The collection with the new object if a different
     *  instance than the <tt>collection</tt> parameter, null otherwise
     * @throws ClassCastException The collection handler does not
     *  support collections of this type
     */
    public Object add( Object collection, Object object )
        throws ClassCastException;


    /**
     * Returns an enumeration of all the elements in the collection.
     *
     * @param collection The collection
     * @return An enumeration of all the elements in the collection
     * @throws ClassCastException The collection handler does not
     *  support collections of this type
     */
    public Enumeration elements( Object collection )
        throws ClassCastException;


    /**
     * Returns the number of elements in the collection.
     *
     * @param collection The collection
     * @return Number of elements in the collection
     * @throws ClassCastException The collection handler does not
     *  support collections of this type
     */
    public int size( Object collection )
        throws ClassCastException;


    /**
     * Clears the collection of any objects. The collection is provided
     * as a parameter and is returned as the return value if the returned
     * collection is a different object. That way the handler can create
     * a new collection or change the collection as necessary (e.g. when
     * resizing an array).
     *
     * @param collection The collection, null if no collection has
     *  been created yet
     * @return The empty collection if a different
     *  instance than the <tt>collection</tt> parameter, null otherwise
     * @throws ClassCastException The collection handler does not
     *  support collections of this type
     */
    public Object clear( Object collection )
        throws ClassCastException;


}



