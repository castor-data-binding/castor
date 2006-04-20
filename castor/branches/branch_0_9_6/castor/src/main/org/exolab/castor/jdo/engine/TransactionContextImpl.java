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


package org.exolab.castor.jdo.engine;


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.util.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * A transaction context is required in order to perform operations
 * against the database. The transaction context is mapped to {@link
 * javax.transaction.Transaction} for the ODMG API and into
 * {@link javax.transaction.xa.XAResource} for XA databases. The only
 * way to begin a new transaction is through the creation of a new
 * transaction context. All database access must be performed through
 * a transaction context.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
final class TransactionContextImpl
    extends TransactionContext
{

	/** 
	 * Log instance used for outputting debug statements
	 */
    private static Log _log = LogFactory.getFactory().getInstance( TransactionContextImpl.class );

    /**
     * Lists all the connections opened for particular database engines
     * used in the lifetime of this transaction. The database engine
     * is used as the key to an open/transactional connection.
     */
    private Hashtable   _conns = new Hashtable();


    /**
     * True if running inside a global transaction and should not
     * attempt to commit/rollback directly.
     */
    private boolean     _globalTx;

	/** 
	 * Meta-data related to the RDBMS used.
	 */
    private DbMetaInfo _dbInfo;

    /**
     * Create a new transaction context.
     */
    public TransactionContextImpl( Database db, boolean globalTx )
    {
        super( db );
        _globalTx = globalTx;
    }
	
	public TransactionContextImpl( Database db, boolean globalTx , javax.transaction.Transaction transaction )
    	throws javax.transaction.SystemException
	{
        super( db , transaction );
        _globalTx = globalTx;
    }
	

    protected void commitConnections()
        throws TransactionAbortedException
    {
        Enumeration enumeration;
        Connection  conn;

        if ( _globalTx ) {
            enumeration = _conns.elements();
            while ( enumeration.hasMoreElements() ) {
                try {
					( (Connection) enumeration.nextElement() ).close();
				} 
				catch ( SQLException except ) { 
					_log.warn( "SQLException occured when closing JDBC Connection instance.", except);
				} 
//				TODO: reenable below catch clause, see bug 1491 ????
//				catch ( Exception except ) { 
//					_log.error( "Exception occured in commitConnections", except );
//					throw new TransactionAbortedException( "Exception in commitConnections" , except );
//					
//				}
				
            }

            _conns.clear();
        } else {
            try {
                // Go through all the connections opened in this transaction,
                // commit and close them one by one.
                enumeration = _conns.elements();
                while ( enumeration.hasMoreElements() ) {
                    conn = (Connection) enumeration.nextElement();
                    // Checkpoint can only be done if transaction is not running
                    // under transaction monitor
                    conn.commit();
                }
            } catch ( SQLException except ) {
                throw new TransactionAbortedException( Messages.format("persist.nested", except), except );
            } finally {
                enumeration = _conns.elements();
                while ( enumeration.hasMoreElements() ) {
                    try {
                        ( (Connection) enumeration.nextElement() ).close();
                    } catch ( SQLException except ) { }
                }
                _conns.clear();
            }
        }
    }


    protected void closeConnections()
        throws TransactionAbortedException
    {
        Enumeration enumeration;
        Connection  conn;
        Exception   error = null;

        if ( ! _globalTx ) {
            return;
        }
        // Go through all the connections opened in this transaction,
        // close them one by one.
        // Close all that can be closed, after that report error if any.
        enumeration = _conns.elements();
        while ( enumeration.hasMoreElements() ) {
            conn = (Connection) enumeration.nextElement();
            try {
                conn.close();
            } catch ( SQLException except ) {
                error = except;
            }
        }
        _conns.clear();
        if ( error != null ) {
            throw new TransactionAbortedException( Messages.format("persist.nested", error ), error );
        }
    }


    protected void rollbackConnections()
    {
        Connection  conn;
        Enumeration enumeration;

        // Go through all the connections opened in this transaction,
        // rollback and close them one by one. Ignore errors.
        enumeration = _conns.elements();
        while ( enumeration.hasMoreElements() ) {
            conn = (Connection) enumeration.nextElement();
            try {
                // if not a global transaction, rollback each one
                if ( ! _globalTx )
                {
                    conn.rollback();
                    _log.debug( "Connection rolled back" );
                }
                conn.close();
                _log.debug( "Connection closed" );
            } catch ( SQLException except ) { }
        }
        _conns.clear();
    }


    public Object getConnection( LockEngine engine ) throws PersistenceException {
        Connection conn;

        conn = (Connection) _conns.get( engine );
        if ( conn == null ) {
            try {
                // Get a new connection from the engine. Since the
                // engine has no transaction association, we must do
                // this sort of round trip. An attempt to have the
                // transaction association in the engine inflates the
                // code size in other places.
                conn = DatabaseRegistry.createConnection( engine );
                if ( ! _globalTx ) {
                    conn.setAutoCommit( false );
                }
                _conns.put( engine, conn );
            } catch ( SQLException except ) {
                throw new PersistenceException( Messages.format("persist.nested", except), except );
            }
        }
        return conn;
    }

    /**
     * @return DbMetaInfo object with database connection information
     */
    public DbMetaInfo getConnectionInfo(LockEngine engine) throws PersistenceException
    {
        Connection conn = (Connection)getConnection(engine);

        if (_dbInfo == null)
            _dbInfo = new DbMetaInfo(conn);

        return _dbInfo;
    }
}
