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
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.castor.persist.TransactionContext;
import org.exolab.castor.jdo.TransactionAbortedException;

/**
 * An {@link XAResource} implementation for an {@link XAResourceSource}.
 * Provides the XA interface for starting transactions, participating
 * in two phase commit and suspending/resuming transactions against
 * the XA source. Transactions are shared across all XA sources that
 * map to the same cache engine.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see TransactionContext
 * @see XAResourceSource
 */
public final class XAResourceImpl implements XAResource {
    /** The cache engine to which this XA resource belongs.
     *  Will be used to associate Xid with a transaction context. */
    private final LockEngine       _engine;
    
    /** The XA source which this XA resource manages. Will be
     *  used to associate/dissociate the source with the
     *  transaction context. */
    private final XAResourceSource  _xaSource;

    public XAResourceImpl(final LockEngine engine, final XAResourceSource xaSource) {
        if ( engine == null || xaSource == null )
            throw new IllegalArgumentException( "Argument 'engine' or xaSource' is null" );
        _xaSource = xaSource;
        _engine = engine;
    }
    
    
    public synchronized void start(final Xid xid, final int flags) throws XAException {
        // General checks.
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        switch ( flags ) {
        case TMNOFLAGS:
            TransactionContext tx1;
            
            // Must assure transaction context is created only once
            // for a given Xid
            synchronized ( _engine.getXATransactions() ) {
                tx1 = (TransactionContext) _engine.getXATransactions().get( xid );
                if ( tx1 == null ) {
                    tx1 = _xaSource.createTransactionContext( xid );
                    _engine.getXATransactions().put( xid, tx1 );
                }
            }
            // Associate XAResource with transaction
            _xaSource.setTransactionContext( tx1 );
            break;
        case TMJOIN:
        case TMRESUME:
            TransactionContext tx2;
            
            tx2 = (TransactionContext) _engine.getXATransactions().get( xid );
            if ( tx2 == null || ! tx2.isOpen() )
                throw new XAException( XAException.XAER_NOTA );
            // Associate XAResource with transaction
            _xaSource.setTransactionContext( tx2 );
            break;
        default:
            // No other flags supported in start().
            throw new XAException( XAException.XAER_INVAL );
        }
    }
	
	
    public synchronized void end(final Xid xid, final int flags) throws XAException {
        // General checks.
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        TransactionContext tx;
        
        tx = (TransactionContext) _engine.getXATransactions().get( xid );
        if ( tx == null )
            throw new XAException( XAException.XAER_NOTA );
        
        // Make sure the XA source is associated with any
        // transaction otherwise this is an invalid Xid
        if ( _xaSource.getTransactionContext() == null )
            throw new XAException( XAException.XAER_INVAL );
        switch ( flags ) {
        case TMSUCCESS:
            // Expect prepare/rollback next
            break;
        case TMFAIL:
            // Failure implies rolling back transaction,
            // and terminating XA source
            _xaSource.xaFailed();
            _xaSource.setTransactionContext( null );
            if ( tx.isOpen() ) {
                try {
                    tx.rollback();
                } catch ( Exception except ) { }
            }
            break;
        case TMSUSPEND:
            // Dissociate source from transaction
            _xaSource.setTransactionContext( null );
            break;
        default:
            throw new XAException( XAException.XAER_INVAL );
        }
    }


    public synchronized void forget(final Xid xid) throws XAException {
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        synchronized ( _engine.getXATransactions() ) {
            TransactionContext tx;
            
            tx = (TransactionContext) _engine.getXATransactions().remove( xid );
            if ( tx == null )
                throw new XAException( XAException.XAER_NOTA );
            // Dissociate XA source from transaction
            if ( _xaSource.getTransactionContext() == tx )
                _xaSource.setTransactionContext( null );
            
            // Forget is never called on an open transaction, but one
            // can never tell.
            if ( tx.isOpen() ) {
                try {
                    tx.rollback();
                } catch ( Exception except ) { }
                throw new XAException( XAException.XAER_PROTO );
            }
        }
    }


    public synchronized int prepare(final Xid xid) throws XAException {
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        TransactionContext tx;
        
        tx = (TransactionContext) _engine.getXATransactions().get( xid );
        if ( tx == null )
            throw new XAException( XAException.XAER_NOTA );
        
        switch ( tx.getStatus() ) {
        case Status.STATUS_PREPARED:
        case Status.STATUS_ACTIVE:
            // Can only prepare an active transaction. And error
            // is reported as vote to rollback the transaction.
            try {
              return tx.prepare() ? XA_OK : XA_RDONLY;
                
            } catch ( TransactionAbortedException except ) {
                throw new XAException( XAException.XA_RBROLLBACK );
            } catch ( IllegalStateException except ) {
                throw new XAException( XAException.XAER_PROTO );
            }
        case Status.STATUS_MARKED_ROLLBACK:
            // Report transaction marked for rollback.
            throw new XAException( XAException.XA_RBROLLBACK );
        default:
            throw new XAException( XAException.XAER_PROTO );
        }
    }


    public synchronized void commit(final Xid xid, final boolean onePhase) throws XAException {
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        TransactionContext tx;
        
        tx = (TransactionContext) _engine.getXATransactions().get( xid );
        if ( tx == null )
            throw new XAException( XAException.XAER_NOTA );
        switch ( tx.getStatus() ) {
        case Status.STATUS_COMMITTED:
            // Allowed to make multiple commit attempts.
            return;
        case Status.STATUS_ROLLEDBACK:
            // This should not happen unless someone interfered
            // by calling rollback directly or failing a commit,
            // but is still a valid heuristic condition on our behalf.
            throw new XAException( XAException.XA_HEURRB );
        case Status.STATUS_PREPARED:
            // Commit can only occur after a prepare, so must be
            // in prepared state first. Any ODMG error is reported
            // as a heuristic decision to rollback.
            try {
                tx.commit();
            } catch ( TransactionAbortedException except ) {
                // Transaction cannot commit and was rolledback
                throw new XAException( XAException.XA_HEURRB );
            } catch ( IllegalStateException except ) {
                throw new XAException( XAException.XAER_PROTO );
            }
        default:
            throw new XAException( XAException.XAER_PROTO );
        }
    }
    

    public synchronized void rollback(final Xid xid) throws XAException {
        if ( xid == null )
            throw new XAException( XAException.XAER_INVAL );
        
        TransactionContext tx;
        
        tx = (TransactionContext) _engine.getXATransactions().get( xid );
        if ( tx == null )
            throw new XAException( XAException.XAER_NOTA );
        switch ( tx.getStatus() ) {
        case Status.STATUS_COMMITTED:
            // This should not happen unless someone interfered
            // by calling commit directly, but is still a valid
            // heuristic condition on our behalf.
            throw new XAException( XAException.XA_HEURCOM );
        case Status.STATUS_ROLLEDBACK:
            // Allowed to make multiple rollback attempts.
            return;
        case Status.STATUS_ACTIVE:
        case Status.STATUS_MARKED_ROLLBACK:
            // Rollback never fails with an application exception.
            try {
                tx.rollback();
            } catch ( IllegalStateException except ) {
                throw new XAException( XAException.XAER_PROTO );
            }
            return;
        default:
            throw new XAException( XAException.XAER_PROTO );
        }
    }


    public Xid[] recover(final int flags) throws XAException {
        // Recovery is not implemented. This XAResource only deals
        // with in-memory objects, so recovery has no meaning.
        // Actual recovery is provided by underlying persistence
        // mechanism (i.e. XADataSource, etc).
        return null;
    }


    public synchronized boolean isSameRM(final XAResource xaRes) throws XAException {
        // Two resource managers are equal if they produce equivalent
        // connection (i.e. same database, same user). If the two are
        // equivalent they would share a transaction by joining.
        if ( xaRes == null || ! ( xaRes instanceof XAResourceImpl ) )
            return false;
        if ( _engine == ( (XAResourceImpl) xaRes )._engine )
            return true;
        return false;
    }
    
    
    public boolean setTransactionTimeout(final int timeout) {
        TransactionContext tx;
        
        tx = _xaSource.getTransactionContext();
        if ( tx != null && tx.isOpen() ) {
            tx.setTransactionTimeout( timeout );
            return true;
        }
        return false;
    }


    public int getTransactionTimeout() {
        TransactionContext tx;
        
        tx = _xaSource.getTransactionContext();
        if ( tx != null && tx.isOpen() )
            return tx.getTransactionTimeout();
        return 0;
    }
}
