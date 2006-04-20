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


package org.exolab.castor.dax.engine;


import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.dax.Search;
import org.exolab.castor.dax.InvalidSearchException;
import org.exolab.castor.dax.DirectoryException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.QueryResults;
import org.exolab.castor.persist.spi.PersistenceQuery;



/**
 * An implementation of the {@link Search} interface.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
class SearchImpl
    implements Search
{


    private PersistenceQuery  _query;


    private DirectoryImpl     _dir;
    
    
    private int               _paramIndex;


    SearchImpl( DirectoryImpl dir, PersistenceQuery query )
    {
        _dir = dir;
        _query = query;
        _paramIndex = 0;
    }
    
    
    public void setParameter( String value )
        throws IndexOutOfBoundsException
    {
        if ( _paramIndex == _query.getParameterCount() )
            throw new IndexOutOfBoundsException( "Query only specifies " + _query.getParameterCount() +
                                                 " parameters" );
        _query.setParameter( _paramIndex, value );
        ++_paramIndex;
    }
    
    
    public int getParameterCount()
    {
        return _query.getParameterCount();
    }
    
    
    public Enumeration execute()
        throws InvalidSearchException, DirectoryException
    {
        TransactionContext tx;
        PersistenceEngine  dirEngine;
        
        dirEngine = _dir.getPersistenceEngine();
        tx = _dir.getTransactionContext();
        try {
            if ( tx == null ) {
                tx = _dir.newTransactionContext();
                _paramIndex = 0;
                return new SearchResults( dirEngine, tx.query( dirEngine, _query, AccessMode.ReadOnly ) );
            } else {
                if ( ! tx.isOpen() )
                    throw new DirectoryException( "Transaction closed" );
                _paramIndex = 0;
                return new SearchResults( dirEngine, tx.query( dirEngine, _query, AccessMode.Shared ) );
            }
        } catch ( QueryException except ) {
            throw new InvalidSearchException( except.getMessage() );
        } catch ( PersistenceException except ) {
            throw new DirectoryException( except );
        }
    }
    
    
    static class SearchResults
        implements Enumeration
    {
        
        
        private QueryResults      _results;
        
        
        private Object            _lastIdentity;
        
        
        private PersistenceEngine _dirEngine;
        
        
        SearchResults( PersistenceEngine dirEngine, QueryResults results )
            throws DirectoryException
        {
            _results = results;
            _dirEngine = dirEngine;
            try {
                _lastIdentity = _results.nextIdentity();
            } catch ( TransactionNotInProgressException except ) {
                throw new DirectoryException( except.getMessage() );
            } catch ( PersistenceException except ) {
                throw new DirectoryException( except );
            }
        }
	
	
        public boolean hasMoreElements()
        {
            if ( ! _results.getTransaction().isOpen() )
                return false;
            return ( _lastIdentity != null );
        }
        

        public Object nextElement()
        {
            Object obj;
            
            if ( _lastIdentity == null )
                throw new NoSuchElementException( "No more elements in query result" );
            obj = _dirEngine.getClassHandler( _results.getResultType() ).newInstance();
            try {
                obj = _results.fetch();
                _lastIdentity = _results.nextIdentity();
            } catch ( ObjectNotFoundException except ) {
                return nextElement();
            } catch ( LockNotGrantedException except ) {
                return nextElement();
            } catch ( TransactionNotInProgressException except ) {
                throw new IllegalStateException( except.getMessage() );
            } catch ( PersistenceException except ) {
                throw new NoSuchElementException( except.getMessage() );
            }
            return obj;
        }
        
        
    }


}
