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


package org.exolab.castor.jdo.engine;


import org.odmg.Transaction;
import org.odmg.TransactionInProgressException;
import org.odmg.TransactionNotInProgressException;
import org.odmg.TransactionAbortedException;
import org.odmg.LockNotGrantedException;
import org.odmg.ODMGRuntimeException;
import org.exolab.castor.util.FastThreadLocal;
import org.exolab.castor.util.Messages;



/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class TransactionImpl
    implements org.odmg.Transaction
{


    private TransactionContext  _txContext;


    private int                 _threadCount;


    private static FastThreadLocal _txLocal = new FastThreadLocal();


    public TransactionImpl()
    {
	// Used by ODMG class which requires that thread be associated
	// with the new transaction.
	join();
    }


    public static TransactionImpl getCurrent()
    {
	return (TransactionImpl) _txLocal.get();
    }


    static TransactionContext getCurrentContext()
    {
	TransactionImpl tx;

	tx = (TransactionImpl) _txLocal.get();
	if ( tx == null )
	    return null;
	else
	    return tx._txContext;
    }


    public void join()
    {
	TransactionImpl tx;

	tx = (TransactionImpl) _txLocal.get();
	if ( tx == this )
	    return;
	if ( tx != null ) {
	    -- tx._threadCount;
	}
	_txLocal.set( this );
	++ _threadCount;
    }


    public void leave()
    {
	TransactionImpl tx;

	tx = (TransactionImpl) _txLocal.get();
	if ( tx == this ) {
	    _txLocal.set( null );
	    -- _threadCount;
	} else {
	    // Not inside transaction
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	}
    }


    public synchronized void begin()
    {
 	if ( _txLocal.get() != this )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	if ( _txContext != null && _txContext.getStatus() == TransactionContext.Status.Open )
	    throw new TransactionInProgressException( Messages.message( "castor.jdo.odmg.txInProgress" ) );
	_txContext = new TransactionContext();
    }


    public boolean isOpen()
    {
	return ( _txContext != null && _txContext.getStatus() == TransactionContext.Status.Open );
    }


    public void commit()
    {
	// Thread must be inside transaction, transaction must be owner,
	// thread must be only thread associated with transaction
 	if ( _txLocal.get() != this )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	if ( _txContext == null || _txContext.getStatus() == TransactionContext.Status.Committed )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.txNotInProgress" ) );
	if ( _txContext.getStatus() == TransactionContext.Status.Rolledback )
	    throw new TransactionAbortedException( Messages.message( "castor.jdo.odmg.txAborted" ) );
	if ( _threadCount != 1 )
	    throw new ODMGRuntimeException( Messages.message( "castor.jdo.odmg.threadNotSingleOwner" ) );
	try {
	    _txContext.commit();
	} finally {
	    _txContext = null;
	}
    }


    public void abort()
    {
	// Thread must be inside transaction, transaction must be owner,
	// thread must be only thread associated with transaction
 	if ( _txLocal.get() != this )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	if ( _txContext == null )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.txNotInProgress" ) );
	if ( _threadCount != 1 )
	    throw new ODMGRuntimeException( Messages.message( "castor.jdo.odmg.threadNotSingleOwner" ) );
	try {
	    if ( _txContext.getStatus() == TransactionContext.Status.Open )
		_txContext.rollback();
	} finally {
	    _txContext = null;
	}
    }


    public void checkpoint()
    {
	// Thread must be inside transaction, transaction must be open
 	if ( _txLocal.get() != this )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	if ( _txContext == null || _txContext.getStatus() == TransactionContext.Status.Committed )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.txNotInProgress" ) );
	if ( _txContext.getStatus() == TransactionContext.Status.Rolledback )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.txRolledback" ) );
	try {
	    _txContext.checkpoint();
	} catch ( TransactionAbortedException except ) {
	    _txContext = null;
	    throw except;
	}
    }


    public void lock( Object obj, int lockMode )
	throws LockNotGrantedException
    {
 	if ( _txLocal.get() != this )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.threadNotOwner" ) );
	if ( _txContext == null || _txContext.getStatus() == TransactionContext.Status.Committed )
	    throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.txNotInProgress" ) );
	if ( _txContext.getStatus() == TransactionContext.Status.Rolledback )
	    throw new TransactionAbortedException( Messages.message( "castor.jdo.odmg.txAborted" ) );
	_txContext.lock( obj, ( lockMode == WRITE ) );
    }


    public boolean tryLock( Object obj, int lockMode )
    {
	try {
	    lock( obj, lockMode );
	    return true;
	} catch ( LockNotGrantedException except ) {
	    return false;
	}
    }


    protected void finalize()
	throws Throwable
    {
	if ( _txContext != null && _txContext.getStatus() == TransactionContext.Status.Open )
	    _txContext.rollback();
    }


}
