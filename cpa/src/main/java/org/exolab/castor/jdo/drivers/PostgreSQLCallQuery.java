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
package org.exolab.castor.jdo.drivers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceQuery;

/**
 * PersistenceQuery implementation for use with PostgreSQL stored functions
 * returning instance, which can be fetched only through SELECT of
 * fields of the instance.
 *                  
 * @author <a href="ros@domainforfree.com">Rostislav Beloff</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2006-04-11 15:26:07 -0600 (Tue, 11 Apr 2006) $
 */
final class PostgreSQLCallQuery implements PersistenceQuery {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log _log = LogFactory.getFactory().getInstance(PostgreSQLCallQuery.class);
    
    private PreparedStatement _stmt;

    private ResultSet         _rs;

    private final Class     _javaClass;

    private final Class[]   _types;

    private final Object[]  _values;

    private final String    _call;

    private Identity        _lastIdentity;

    private int[]           _sqlTypes;

    PostgreSQLCallQuery( String call, Class[] types, Class javaClass, String[] fields, int[] sqlTypes ) {
        StringBuffer query = new StringBuffer();

        query.append( JDBCSyntax.SELECT );
        for ( int i = 0; i < fields.length; i++ ) {
            if ( i > 0 ) 
                query.append( JDBCSyntax.COLUMN_SEPARATOR );
            query.append( fields[i] );
            query.append( "(" );
            query.append( call );
            query.append( ")" );
        }
        _call = query.toString();
        _types = types;
        _javaClass = javaClass;
        _sqlTypes = sqlTypes;
        _values = new Object[ _types.length ];
    }

    public boolean absolute(int row) throws PersistenceException {
      return false;
    }

    public int size() throws PersistenceException {
      return 0;
    }

    public int getParameterCount() {
        return _types.length;
    }


    public Class getParameterType( int index ) throws ArrayIndexOutOfBoundsException {
        return _types[ index ];
    }


    public void setParameter( int index, Object value )
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        _values[ index ] = value;
    }


    public Class getResultType() {
        return _javaClass;
    }

    public void execute( Object conn, AccessMode accessMode, boolean scrollable)
    throws QueryException, PersistenceException {
      execute(conn, accessMode);
    }

    private void execute( Object conn, AccessMode accessMode )
    throws QueryException, PersistenceException {
        _lastIdentity = null;
        try {
            int count;

            _stmt = ( (Connection) conn ).prepareStatement( _call );
            count = 1;
            for ( int f = 0 ; f < _sqlTypes.length ; ++f ) {
                for ( int i = 0 ; i < _values.length ; ++i ) {
                    _stmt.setObject( count, _values[ i ] );
                    ++count;
                }
            }
            for ( int i = 0 ; i < _values.length ; ++i ) 
                _values[ i ] = null;
            _stmt.execute();
            _rs = _stmt.executeQuery();
        } catch ( SQLException except ) {
            if ( _stmt != null ) {
                try {
                    _stmt.close();
                } catch ( SQLException e2 ) {
                    _log.warn (Messages.message ("persist.stClosingFailed"), e2);
                }
            }
            throw new PersistenceException( Messages.format( "persist.nested", except ) );
        }
    }


    public Identity nextIdentity(Identity identity) throws PersistenceException {
        try {
            if (_lastIdentity == null) {
                if (!_rs.next()) { return null; }
                _lastIdentity = new Identity(SQLTypeInfos.getValue(_rs, 1, _sqlTypes[0]));
                return _lastIdentity;
            }

            while (_lastIdentity.equals(identity)) {
                if (!_rs.next()) {
                    _lastIdentity = null;
                    return null;
                }
                _lastIdentity = new Identity(SQLTypeInfos.getValue(_rs, 1, _sqlTypes[0]));
            }
            return _lastIdentity;
        } catch (SQLException except) {
            _lastIdentity = null;
            throw new PersistenceException(Messages.format("persist.nested", except));
        }
    }

    public void close() {
        if ( _rs != null ) {
            try {
                _rs.close();
            } catch ( SQLException except ) {
            	_log.warn (Messages.message ("persist.rsClosingFailed"), except);
            }
            _rs = null;
        }
        if ( _stmt != null ) {
            try {
                _stmt.close();
            } catch ( SQLException except ) {
            	_log.warn (Messages.message ("persist.stClosingFailed"), except);
            }
            _stmt = null;
        }
    }

    public Object fetch(ProposedEntity proposedObject) throws PersistenceException {
        try {
            // Load all the fields of the object including one-one relations
            // index 0 belongs to the identity
            for (int i = 1; i < _sqlTypes.length; ++i) {
                proposedObject.setField(SQLTypeInfos.getValue(_rs, i + 1, _sqlTypes[i]), i - 1);
            }
            if (_rs.next()) {
                _lastIdentity = new Identity(SQLTypeInfos.getValue(_rs, 1, _sqlTypes[0]));
            } else {
                _lastIdentity = null;
            }
        } catch (SQLException except) {
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        }
        return null;
    }

}
