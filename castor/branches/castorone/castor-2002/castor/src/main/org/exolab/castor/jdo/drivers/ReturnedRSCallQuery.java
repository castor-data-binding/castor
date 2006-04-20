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


import java.util.Vector;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.SQLTypes;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;
import org.exolab.castor.persist.spi.Complex;

/**
 * PersistenceQuery implementation for use with CallableStatements that
 * return a ResultSet, like Oracle stored functions returning REF CURSOR.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
final class ReturnedRSCallQuery implements PersistenceQuery {


    private CallableStatement _stmt;


    private ResultSet         _rs;


    private final Class     _javaClass;


    private final Class[]   _types;


    private final Object[]  _values;


    private final String    _call;


    private Object         _lastIdentity;


    private int[]          _sqlTypes;


    ReturnedRSCallQuery( String call, Class[] types, Class javaClass,
                         String[] fields, int[] sqlTypes )
    {
        _call = "{ ? = call " + call + "}";
        _types = types;
        _javaClass = javaClass;
        _sqlTypes = sqlTypes;
        _values = new Object[ _types.length ];
    }


    public int getParameterCount()
    {
        return _types.length;
    }


    public Class getParameterType( int index )
        throws ArrayIndexOutOfBoundsException
    {
        return _types[ index ];
    }


    public void setParameter( int index, Object value )
        throws ArrayIndexOutOfBoundsException, IllegalArgumentException
    {
        _values[ index ] = value;
    }


    public Class getResultType()
    {
        return _javaClass;
    }


    public void execute( Object conn, AccessMode accessMode )
        throws QueryException, PersistenceException
    {
        _lastIdentity = null;
        try {
            _stmt = ( (Connection) conn ).prepareCall( _call );
            _stmt.registerOutParameter(1, -10); // -10 == OracleTypes.CURSOR
            for ( int i = 0 ; i < _values.length ; ++i ) {
                _stmt.setObject( i + 2, _values[ i ] );
                _values[ i ] = null;
            }
            _stmt.execute();
            _rs = (ResultSet) _stmt.getObject(1);
        } catch ( SQLException except ) {
            if ( _stmt != null ) {
                try {
                    _stmt.close();
                } catch ( SQLException e2 ) { }
            }
            throw new PersistenceException( Messages.format( "persist.nested", except ) );
        }
    }


    public Object nextIdentity(Object identity) throws PersistenceException
    {
        try {
            if ( _lastIdentity == null ) {
                if ( ! _rs.next() )
                    return null;
                _lastIdentity = SQLTypes.getObject( _rs, 1, _sqlTypes[ 0 ] );
                return new Complex( _lastIdentity );
            }

            while ( _lastIdentity.equals( identity ) ) {
                if ( ! _rs.next() ) {
                    _lastIdentity = null;
                    return null;
                }
                _lastIdentity = SQLTypes.getObject( _rs, 1, _sqlTypes[ 0 ] );
            }
            return new Complex( _lastIdentity );
        } catch ( SQLException except ) {
            _lastIdentity = null;
            throw new PersistenceException( Messages.format( "persist.nested", except ) );
        }
    }


    public void close()
    {
        if ( _rs != null ) {
            try {
                _rs.close();
            } catch ( SQLException except ) { }
            _rs = null;
        }
        if ( _stmt != null ) {
            try {
                _stmt.close();
            } catch ( SQLException except ) { }
            _stmt = null;
        }
    }


    public Object fetch(Object[] fields,Object identity) throws ObjectNotFoundException, PersistenceException
    {
        Object stamp = null;

        try {

            // Load all the fields of the object including one-one relations
            // index 0 belongs to the identity
            for ( int i = 1 ; i < _sqlTypes.length ; ++i  ) 
                fields[ i - 1 ] = SQLTypes.getObject( _rs, i + 1, _sqlTypes[ i ] );
            if ( _rs.next() ) 
                _lastIdentity = SQLTypes.getObject( _rs, 1, _sqlTypes[ 0 ] );
            else
                _lastIdentity = null;
        } catch ( SQLException except ) {
            throw new PersistenceException( Messages.format( "persist.nested", except ) );
        }
        return stamp;
    }

}
