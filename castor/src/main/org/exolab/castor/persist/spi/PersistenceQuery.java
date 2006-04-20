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


package org.exolab.castor.persist.spi;


import org.castor.persist.ProposedObject;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;


/**
 * The persistence engine implements this interface in order to allow
 * queries to be performed and multiple objects to be returned. This is
 * an extension of {@link Persistence#load} for dealing with complex
 * queries.
 * <p>
 * The caller takes full responsibility to assure integrity of
 * transactions and object caching and only relies on the engine to
 * assist in assuring that through the mechanisms available to it.
 * <p>
 * A query may be created once and used multiple times by calling the
 * {@link #execute(Object, AccessMode)} method. Query parameters are set each time prior
 * to executing the query.
 * <p>
 * See {@link Persistence} for information about locks, loading
 * objects, identities and stamps.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Persistence
 */
public interface PersistenceQuery
{


    /**
     * Returns the number of parameters required for this query.
     *
     * @return Number of query parameters
     */
    public int getParameterCount();


    /**
     * Returns the type of a parameter required by this query. May
     * return null if the type is unknown.
     *
     * @param index The parameter index
     * @return Type of parameter, or null
     */
    public Class getParameterType( int index );


    /**
     * Sets the value of a paramter. Will complain if the parameter
     * is not of the specified type.
     *
     * @param index The parameter index
     * @param value The parameter value
     */
    public void setParameter( int index, Object value );


    /**
     * Returns the type of object returned by this query.
     *
     * @return The type of object returned by this query
     */
    public Class getResultType();


    /**
     * Execute the query with the give connection and lock type.
     * After a successful return the query results will be returned
     * by calling {@link #nextIdentity} and {@link #fetch}. The
     * query parameters will be reset. A new query may be issued by
     * providing new query parameters and calling {@link #execute(Object, AccessMode)}.
     *
     * @param conn An open connection
     * @param accessMode The access mode (null equals shared)
     * @throws QueryException An invalid query
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void execute( Object conn, AccessMode accessMode )
        throws QueryException, PersistenceException;

    /**
     * same as above, but parameterized for scrollable resultsets.
     * @param conn An open connection
     * @param accessMode The access mode (null equals shared)
     * @param scrollable The db cursor mode.
     * @throws QueryException An invalid query
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void execute( Object conn, AccessMode accessMode, boolean scrollable )
        throws QueryException, PersistenceException;


    /**
     * Returns the identity of the next object to be returned.
     * Calling this method multiple time will skip objects.
     * When the result set has been exhuasted, this method will
     * return null.
     *
     * @param identity The identity of the previous object,
     *  null if this method is called for the first time
     * @return The identity of the next object, null if the
     *  result set has been exhausted
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public Object nextIdentity( Object identity )
        throws PersistenceException;
    

    /**
     * Loades the object. This method must be called immediately
     * after {@link #nextIdentity} with the same identity.
     * <p>
     * If the object is locked by another transaction this method will
     * block until the lock is released, or a timeout occured. If a
     * timeout occurs or the object has been deleted by the other
     * transaction, this method will report an {@link
     * ObjectNotFoundException}. The query may proceed to the next
     * identity.
     * <p>
     * This method is equivalent to {@link Persistence#load} with a
     * known cache engine and access mode and acts on the query
     * results rather than issuing a new query to load the object.
     *
     * @param fields The fields to load into
     * @param identity The object's identity
     * @return The object's stamp, or null
     * @throws ObjectNotFoundException The object was not found in
     *   persistent storage
     * @throws PersistenceException A persistence error occured
     * @see Persistence#load
     */
    public Object fetch(ProposedObject proposedObject, Object identity)
    throws ObjectNotFoundException, PersistenceException;


    /**
     * Close the query and release all resources held by the query.
     */
    public void close();
    
    /**
     * moves the result of the query to the absolute position in the
     * resultset
     * @param row The row to move to
     * @throws PersistenceException A persistence error occured
     */
    public boolean absolute(int row)
      throws PersistenceException;

    /**
     * finds the size of the resulting resultset from the query.
     */
    public int size()
      throws PersistenceException;


}

