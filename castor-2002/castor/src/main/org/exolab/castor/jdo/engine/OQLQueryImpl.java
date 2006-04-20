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


import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.QueryResults;
import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.PersistenceExceptionImpl;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.Logger;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class OQLQueryImpl
    implements OQLQuery
{


    private PersistenceEngine  _dbEngine;


    private DatabaseImpl       _dbImpl;


    private Class              _objClass;


    private QueryExpression    _expr;


    private Class[]            _bindTypes;


    private Object[]           _bindValues;


    private int                _fieldNum;


    OQLQueryImpl( DatabaseImpl dbImpl )
    {
        _dbImpl = dbImpl;
    }


    public void bind( Object value )
    {
        if ( _expr == null )
            throw new IllegalStateException( "Must create query before using it" );
        if ( _fieldNum == _bindTypes.length )
            throw new IllegalArgumentException( "Only " + _bindTypes.length +
                                                " fields in this query" );
        try {
            if ( value != null && ! _bindTypes[ _fieldNum ].isAssignableFrom( value.getClass() ) )
                throw new IllegalArgumentException( "Query paramter " + _fieldNum + " is not of the expected type " + 
                                                    _bindTypes[ _fieldNum ].getName() );
            if ( _bindValues == null )
                _bindValues = new Object[ _bindTypes.length ];
            _bindValues[ _fieldNum ] = value;
        } catch ( IllegalArgumentException except ) {
            throw except;
        }
        ++_fieldNum;
    }


    public void bind( boolean value )
    {
        bind( new Boolean( value ) );
    }


    public void bind( short value )
    {
        bind( new Short( value ) );
    }


    public void bind( int value )
    {
        bind( new Integer( value ) );
    }


    public void bind( long value )
    {
        bind( new Long( value ) );
    }


    public void bind( String value )
    {
        bind( (Object) value );
    }


    public void bind( float value )
    {
        bind( new Float( value ) );
    }


    public void bind( double value )
    {
        bind( new Double( value ) );
    }


    public void create( String oql )
        throws QueryException
    {
        StringTokenizer    token;
        String             objType;
        String             objName;
        StringBuffer       sql;
        JDOClassDescriptor clsDesc;
        SQLEngine          engine;
        Vector             types;
        Class[]            array;
        
        _fieldNum = 0;
        _expr = null;
        types = new Vector();
        sql = new StringBuffer();
        token = new StringTokenizer( oql );
        if ( ! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase( "SELECT" ) )
            throw new QueryException( "Query must start with SELECT" );
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing object name" );
        objName = token.nextToken();
        if ( ! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase( "FROM" ) )
            throw new QueryException( "Object must be followed by FROM" );
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing object type" );
        objType = token.nextToken();
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing object name" );
        if ( ! objName.equals( token.nextToken() ) )
            throw new QueryException( "Object name not same in SELECT and FROM" );
        
        try {
            _objClass = Class.forName( objType );
        } catch ( ClassNotFoundException except ) {
            throw new QueryException( "Could not find class " + objType );
        }
        _dbEngine = _dbImpl.getPersistenceEngine(); 
        if ( _dbEngine == null )
            throw new QueryException( "Cold not find an engine supporting class " + objType );
        engine = (SQLEngine) _dbEngine.getPersistence( _objClass );
        clsDesc = engine.getDescriptor();

        _expr = engine.getFinder();
        if ( token.hasMoreTokens() ) {
            if ( ! token.nextToken().equalsIgnoreCase( "WHERE" ) )
                throw new QueryException( "Missing WHERE clause" );
            parseField( clsDesc, token, _expr, types );
            while ( token.hasMoreTokens() ) {
                if ( ! token.nextToken().equals( "AND" ) )
                    throw new QueryException( "Only AND supported in WHERE clause" );
                parseField( clsDesc, token, _expr, types );
            }
        }

        _bindTypes = new Class[ types.size() ];
        types.copyInto( _bindTypes );
    }
    
    
    private void parseField( JDOClassDescriptor clsDesc, StringTokenizer token,
                             QueryExpression expr, Vector types )
        throws QueryException
    {
        String               name;
        String               op;
        String               value;
        FieldDescriptor[]    fields;
        JDOFieldDescriptor   field;
        
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing field name" );
        name = token.nextToken();
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing operator" );
        op = token.nextToken();
        if ( ! token.hasMoreTokens() )
            throw new QueryException( "Missing field value" );
        
        value = token.nextToken();
        if ( name.indexOf( "." ) > 0 )
            name = name.substring( name.indexOf( "." ) + 1 );
        fields = clsDesc.getFields();
        field = null;
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDescriptor &&
                 fields[ i ].getFieldName().equals( name ) ) {
                field = (JDOFieldDescriptor) fields[ i ];
                break;
            }
        }
        
        if ( field == null ) {
            if ( clsDesc.getIdentity() instanceof JDOFieldDescriptor &&
                 clsDesc.getIdentity().getFieldName().equals( name ) ) {
                field = (JDOFieldDescriptor) clsDesc.getIdentity();
            }
        }
        
        if ( field == null )
            throw new QueryException( "The field " + name + " was not found" );
        if ( value.startsWith( "$" ) ) {
            expr.addParameter( clsDesc.getTableName(), field.getSQLName(), op );
            types.addElement( field.getFieldType() );
        } else {
            expr.addCondition( clsDesc.getTableName(), field.getSQLName(), op, value );
        }
    }
    
    
    public Enumeration execute()
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        return execute( null );
    }


    public Enumeration execute( short accessMode )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        switch ( accessMode ) {
        case Database.ReadOnly:
            return execute( AccessMode.ReadOnly );
        case Database.Shared:
            return execute( AccessMode.Shared );
        case Database.Exclusive:
            return execute( AccessMode.Exclusive );
        case Database.DbLocked:
            return execute( AccessMode.DbLocked );
        default:
            throw new IllegalArgumentException( "Value for 'accessMode' is invalid" );
        }
    }


    private Enumeration execute( AccessMode accessMode )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        QueryResults      results;
        PersistenceQuery  query;
        SQLEngine         engine;
        
        if ( _expr == null )
            throw new IllegalStateException( "Must create query before using it" );
        try {
            try {
                engine = (SQLEngine) _dbEngine.getPersistence( _objClass );
                query = engine.createQuery( _expr, _bindTypes );
                if ( _bindValues != null ) {
                    for ( int i = 0 ; i < _bindValues.length ; ++i )
                        query.setParameter( i, _bindValues[ i ] );
                }
            } catch ( QueryException except ) {
                throw new QueryException( except.getMessage() );
            }
            results = _dbImpl.getTransaction().query( _dbEngine, query, accessMode );
            _fieldNum = 0;
            return new OQLEnumeration( results );
        } catch ( PersistenceException except ) {
            throw except;
        }
    }


    static class OQLEnumeration
        implements Enumeration
    {

        
        private Object       _lastObject;


        private QueryResults _results;


        OQLEnumeration( QueryResults results )
        {
            _results = results;
        }


        public boolean hasMoreElements()
        {
            Object identity;

            if ( _lastObject != null )
                return true;
            if ( _results == null )
                return false;
            try {
                identity = _results.nextIdentity();
                while ( identity != null ) {
                    try {
                        _lastObject = _results.fetch();
                        if ( _lastObject != null )
                            break;
                    } catch ( PersistenceException except ) {
                        identity = _results.nextIdentity();
                    }
                }
                if ( identity == null ) {
                    _results.close();
                    _results = null;
                }
            } catch ( PersistenceException except ) {
                _results.close();
                _results = null;
            }
            return ( _lastObject != null );
        }


        public Object nextElement()
        {
            Object identity;

            if ( _lastObject != null ) {
                Object result;
                
                result = _lastObject;
                _lastObject = null;
                return result;
            }
            if ( _results == null )
                throw new NoSuchElementException();
            try {
                identity = _results.nextIdentity();
                while ( identity != null ) {
                    try {
                        Object result;
                        
                        result = _results.fetch();
                        if ( result != null )
                            return result;
                    } catch ( PersistenceException except ) {
                    }
                    identity = _results.nextIdentity();
                }
                if ( identity == null ) {
                    _results.close();
                    _results = null;
                }
            } catch ( PersistenceException except ) { 
                _results.close();
                _results = null;
            }
            throw new NoSuchElementException();
        }


        protected void finalize()
            throws Throwable
        {
            if ( _results != null ) {
                _results.close();
                _results = null;
            }
        }


    }
    

}
