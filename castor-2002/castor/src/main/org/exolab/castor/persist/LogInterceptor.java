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


package org.exolab.castor.persist;


import java.io.PrintWriter;


/**
 * A log interceptor recieves notifications on various events that
 * occur in the persistence layer is responsible for reporting them.
 * The interceptor cannot affect the outcome of any operation.
 * <p>
 * The log interceptor is used for tracing persistence activity for
 * performance tuning, viewing the generated SQL statements, and
 * tracing messages that are not bubbled up to the application layer.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface LogInterceptor {


    /**
     * Called to indicate that an object of the given type and identity
     * is about to be loaded into memory.
     * <p>
     * This method is called when the cache engine decides to
     * explicitly load the specified object from persistent storage and
     * not use a cached copy. It is called prior to the retrieval.
     *
     * @param objClass The type of the object
     * @param identity The object identity
     */
    public void loading( Object objClass, Object identity );
    
    /**
     * Called to indicate that an object of the given type and identity
     * is about to be created in persistent storage.
     * <p>
     * This method is called when the cache engine decides to
     * explicitly create the specified object in persistent storage,
     * either in response to a create method or upon transaction
     * commit. It is called prior to the creation.
     *
     * @param objClass The type of the object
     * @param identity The object identity
     */
    public void creating( Object objClass, Object identity );


    /**
     * Called to indicate that an object of the given type and identity
     * is about to be deleted from persistent storage.
     * <p>
     * This method is called when the cache engine decides to
     * explicitly delete the specified object from persistent storage,
     * either in response to a delete method or upon transaction
     * commit. It is called prior to the deletion.
     *
     * @param objClass The type of the object
     * @param identity The object identity
     */
    public void removing( Object objClass, Object identity );


    /**
     * Called to indicate that an object of the given type and identity
     * is about to be stored in persistent storage.
     * <p>
     * This method is called when the cache engine decides to
     * explicitly store the specified object in persistent storage,
     * after detecting a modification in this object. It is called
     * prior to storage.
     *
     * @param objClass The type of the object
     * @param identity The object identity
     */
    public void storing( Object objClass, Object identity );


    /**
     * Reports a statement that will be used with the persistent
     * engine.
     * <p>
     * The SQL engine uses this method to report all the select,
     * update, insert statements it creates upon initialization.
     *
     * @param message The storage statement
     */
    public void storeStatement( String statement );


    /**
     * Reports a statement that will be used with the persistent
     * engine to conduct a query.
     * <p>
     * The SQL engine uses this method to report select statements
     * when running new queries.
     *
     * @param message The query statement
     */
    public void queryStatement( String statement );


    /**
     * Reports a message of some sort that is not delivered to the
     * application. Only the interceptor will be notified of this
     * message.
     *
     * @param message The reported message
     */
    public void message( String message );


    /**
     * Reports an exception of some sort that is not delivered to the
     * application. Only the interceptor will be notified of this
     * exception.
     *
     * @param exception The exception
     */
    public void exception( Exception except );


    /**
     * Returns the PrintWriter for this LogInterceptor
     */
    public PrintWriter getPrintWriter();


}

