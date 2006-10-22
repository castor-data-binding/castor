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


import javax.transaction.Status;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.util.Messages;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceQuery;


/**
 * The result of a query in the context of a transaction. A query is
 * executed against the cache engine in the context of a transaction.
 * The result of a query is this object that can be used to obtain
 * the next object in the query.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-22 11:05:30 -0600 (Sat, 22 Apr 2006) $
 */
public final class QueryResults {
    /**
     * The transaction context in which this query was executed.
     */
    private final TransactionContext _tx;
    
    /**
     * The persistence engine against which this query was executed.
     */
    private final LockEngine   _engine;
    
    /**
     * The executed query.
     */
    private final PersistenceQuery _query;
    
    /**
     * The mode in which this query is running.
     */
    private final AccessMode _accessMode;
    
    /**
     * The last identity retrieved with a call to {@link #nextIdentity}.
     */
    private Identity _lastIdentity;

    public QueryResults(TransactionContext tx, 
            final LockEngine engine,
            final PersistenceQuery query, 
            final AccessMode accessMode, 
            final Database db) {
        _tx = tx;
        _engine = engine;
        _query = query;
        _accessMode = engine.getClassMolder(_query.getResultType()).getAccessMode(accessMode);
    }


    /**
     * Returns the transaction context in which this query was opened.
     * The transaction may be closed.
     *
     * @return The query's transaction context
     */
    public TransactionContext getTransaction() {
        return _tx;
    }
    
    
    /**
     * Returns the associated query.
     *
     * @return The query
     */
    public PersistenceQuery getQuery() {
        return _query;
    }
    
    /**
     * Returns the type of object returned by this query.
     *
     * @return The type of object returned by this query
     */
    public Class getResultType() {
        return _query.getResultType();
    }
    
    
    /**
     * Returns the identity of the next object to be returned.
     * Calling this method multiple time will skip objects.
     * When the result set has been exhuasted, this method will
     * return null.
     *
     * @return The identity of the next object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws TransactionNotInProgressException The transaction
     *  has been closed
     */
    public Object nextIdentity()
        throws TransactionNotInProgressException, PersistenceException {
        // Make sure transaction is still open.
        if (_tx.getStatus() != Status.STATUS_ACTIVE) {
            throw new TransactionNotInProgressException(
                    Messages.message("persist.noTransaction"));
        }
        try {
            _lastIdentity = _query.nextIdentity(_lastIdentity);
        } catch (PersistenceException except) {
            _lastIdentity = null;
            throw except;
        }
        return _lastIdentity;
    }


    /**
     * Loads the specified object with the identity. The identity must
     * have been retrieved with a call to {@link #nextIdentity}.
     * <p>
     * If the object is locked by another transaction this method will
     * block until the lock is released, or a timeout occured. If a
     * timeout occurs or the object has been deleted by the other
     * transaction, this method will report an {@link
     * ObjectNotFoundException}. The query may proceed to the next
     * identity.
     * <p>
     * If the object has been deleted in this transaction, this method
     * will report an {@link ObjectNotFoundException}. However, the
     * caller may iterate to and obtain the next object.
     * <p>
     * This method is equivalent to {@link TransactionContext#fetch}
     * with a know cache engine, identity and lock and acts on the query
     * results rather than issuing a new query to load the object.
     *
     * @return The loaded object
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Could not acquire a lock on
     *  the object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws TransactionNotInProgressException The transaction
     *  has been closed
     */
    public Object fetch()
        throws TransactionNotInProgressException, PersistenceException,
               ObjectNotFoundException, LockNotGrantedException {
        ClassMolder     handler;
        Object           object;
        
        // Make sure transaction is still open.
        if (_tx.getStatus() != Status.STATUS_ACTIVE) {
            throw new TransactionNotInProgressException(Messages
                    .message("persist.noTransaction"));
        }
        if (_lastIdentity == null) {
            throw new IllegalStateException(Messages.message("jdo.fetchNoNextIdentity"));
        }

        handler = _engine.getClassMolder(_query.getResultType());

        // load the object thur the transaction of the query
        ProposedEntity proposedValue = new ProposedEntity(handler);
        proposedValue.setProposedEntityClass(_query.getResultType());
        object = _tx.load(_lastIdentity, proposedValue, _accessMode, this);
        if (proposedValue.isExpanded()) { object = proposedValue.getEntity(); }
        
        return object;

    }


    /**
     * Close the query and release all resources held by the query.
     */
    public void close() {
        _query.close();
    }
    
    public boolean absolute(final int row) throws PersistenceException {
        return _query.absolute(row);
    }
    
    public int size() throws PersistenceException {
        return _query.size();
    }
}


