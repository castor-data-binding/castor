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


package org.exolab.castor.persist.sql;

import java.util.NoSuchElementException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.engine.DatabaseImpl;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * A class to execute simple SQL queries generated from OQL.  If the query
 * only returns dependant values, or the results of SQL Functions or 
 * operations, then we don't need to use the whole persistence framework,
 * and this class will execute the query, and return results.
 *
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public class SimpleQueryExecutor 
{

  private DatabaseImpl _dbImpl;

  private Connection _conn = null;
  private PreparedStatement _stmt = null;
  private ResultSet _rset = null;

  /**
   * Constructor to create an object to execute a simple query.
   *
   * @param dbImpl the Database Implementation, used to get the connection
   */
  public SimpleQueryExecutor(DatabaseImpl dbImpl) {
    _dbImpl = dbImpl;
  }

  /**
   * Executes a simple query and returns the results.  The query must not
   * return any complex objects, becuse this method can only return simple
   * java objects.
   *
   * @param expr the Query Expression to be executed.
   * @param bindValues the values of the parameters
   * @throws QueryException if anything goes wrong.
   * @return the results of the query.
   * 
   */
  public QueryResults execute( QueryExpression expr, Object[] bindValues )
         throws QueryException {
    
    try {
    
      // 092: _conn = DatabaseRegistry.createConnection( _dbImpl.getLockEngine() );
      //_stmt = _conn.prepareStatement( expr.getStatement(false) );

      if ( bindValues != null ) {
        for ( int i = 0 ; i < bindValues.length ; ++i ) {
          _stmt.setObject( i + 1, bindValues[ i ] );
        }
      }
    
      _rset = _stmt.executeQuery();
      return new SimpleQueryResults();
    }
    catch (SQLException s) {
    
      if ( _rset != null )
        try { _rset.close(); } catch (SQLException e) {}
      if ( _stmt != null )
        try { _stmt.close(); } catch (SQLException e) {}
      if ( _conn != null )
        try { _conn.close(); } catch (SQLException e) {}
        
      _rset = null;
      _stmt = null;
      _conn = null;
      
      throw new QueryException( s.toString() );
    }
    
    
  }

  public class SimpleQueryResults implements QueryResults {

    private boolean _hasMore = false;

    public SimpleQueryResults() {
      //prime the resultset.
      try {
        _hasMore = _rset.next();
      }
      catch (SQLException e) {
        _hasMore = false;
      }
    }
  
    public boolean hasMoreElements() {
      return _hasMore;
    }

    public boolean hasMore() throws PersistenceException {
      return _hasMore;
    }

    public Object nextElement() throws NoSuchElementException {
      try {
        return next( true );
      } 
      catch ( PersistenceException except ) {
        // Will never happen
        return null;
      }
    }

    public Object next() throws PersistenceException, NoSuchElementException {
      return next( false );
    }
        
    private Object next( boolean skipError ) 
              throws PersistenceException, NoSuchElementException {
      
      Object retVal = null;
      
      if ( ! _hasMore )
        throw new NoSuchElementException();
      try {
        retVal = _rset.getObject(1);
        _hasMore = _rset.next();
      } 
      catch ( SQLException except ) { 
        if ( ! skipError )
          throw new PersistenceException(except.toString());
      }
      
      return retVal;
   }

    public void close() {
      if ( _rset != null ) 
        try { _rset.close(); } catch (SQLException s) {}
      if ( _stmt != null ) 
        try { _stmt.close(); } catch (SQLException s) {}
      if ( _conn != null ) 
        try { _conn.close(); } catch (SQLException s) {}
      _rset = null;
      _stmt = null;
      _conn = null;
      
    }

    protected void finalize() throws Throwable {
      close();
    }
   
  }

}


