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


import java.util.Hashtable;
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
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.DuplicateIdentityExceptionImpl;
import org.exolab.castor.persist.PersistenceExceptionImpl;
import org.exolab.castor.persist.ObjectNotFoundExceptionImpl;
import org.exolab.castor.persist.ObjectDeletedExceptionImpl;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.util.Logger;
import org.exolab.castor.util.Messages;


/**
 * The SQL engine performs persistence of one object type against one
 * SQL database. It can only persist simple objects and extended
 * relationships. An SQL engine is created for each object type
 * represented by a database. When persisting, it requires a physical
 * connection that maps to the SQL database and the transaction
 * running on that database, through the {@link ConnectionProvider}
 * interface.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class SQLEngine
    implements Persistence
{


    /**
     * In order to support key generators using separate connections, 
     * we must hold a set of the Connections (keys - CacheEngines, values - Connections).
     */
    static private Hashtable    _separateConnections = new Hashtable();


    private String              _pkLookup;


    private String              _sqlCreate;


    private String              _sqlRemove;


    private String              _sqlStore;


    private String              _sqlStoreDirty;


    private String              _sqlLoad;


    private String              _sqlLoadLock;


    private FieldInfo[]         _fields;


    private SQLEngine           _extends;


    private QueryExpression     _sqlFinder;


    private PersistenceFactory  _factory;


    private String              _stampField;


    private LogInterceptor       _logInterceptor;


    private JDOClassDescriptor   _clsDesc;


    private KeyGenerator         _keyGen;


    SQLEngine( JDOClassDescriptor clsDesc, LogInterceptor logInterceptor,
               PersistenceFactory factory, String stampField )
        throws MappingException
    {
        _clsDesc = clsDesc;
        _stampField = stampField;
        _factory = factory;
        _logInterceptor = logInterceptor;
        _keyGen = null;
        if ( _clsDesc.getExtends() != null ) {
            _extends = new SQLEngine( (JDOClassDescriptor) _clsDesc.getExtends(), null,
                      _factory, _stampField );
        } else {
            KeyGeneratorDescriptor keyGenDesc;

            keyGenDesc = _clsDesc.getKeyGeneratorDescriptor();
            if ( keyGenDesc != null ) {
                _keyGen = keyGenDesc.getKeyGeneratorRegistry().getKeyGenerator(
                        _factory, keyGenDesc,
                        ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLType(),
                        _logInterceptor );
            }
        }
        try {
            buildSql( _clsDesc, _logInterceptor );
            buildFinder( _clsDesc, _logInterceptor );
        } catch ( QueryException except ) {
            throw new MappingException( except );
        }
    }


    private synchronized Connection getSeparateConnection()
        throws PersistenceException
    {
        PersistenceEngine engine;
        Connection conn;

        engine = DatabaseRegistry.getPersistenceEngine( _clsDesc.getJavaClass() );
        if ( engine == null ) {
            throw new PersistenceExceptionImpl( "persist.cannotCreateSeparateConn" );
        }

        conn = (Connection) _separateConnections.get( engine );
        if ( conn == null ) {
            try {
                conn = DatabaseRegistry.createConnection( engine );
                conn.setAutoCommit( false );
                _separateConnections.put( engine, conn );
            } catch ( SQLException except ) {
                throw new PersistenceExceptionImpl( "persist.cannotCreateSeparateConn" );
            }
        }
        return conn;        
    }


    /**
     * Used by {@link OQLQuery} to retrieve the class descriptor.
     */
    public JDOClassDescriptor getDescriptor()
    {
        return _clsDesc;
    }


    public PersistenceQuery createQuery( QueryExpression query, Class[] types, AccessMode accessMode )
        throws QueryException
    {
        String sql;

        if ( accessMode == null )
            accessMode = _clsDesc.getAccessMode();
        sql = query.getStatement( accessMode == AccessMode.DbLocked);
        if ( _logInterceptor != null )
            _logInterceptor.queryStatement( sql );
        return new SQLQuery( this, sql, types );
    }


    public PersistenceQuery createCall( String spCall, Class[] types )
    {
        FieldDescriptor[] fields;   
        String[] jdoFields0;
        String[] jdoFields;
        int[] sqlTypes0;
        int[] sqlTypes;
        int count; 

        if ( _logInterceptor != null )
            _logInterceptor.queryStatement( spCall );

        fields = _clsDesc.getFields();
        jdoFields0 = new String[ fields.length + 1 ];
        sqlTypes0 = new int[ fields.length + 1 ];
        // the first field is the identity
        count = 1;
        jdoFields0[ 0 ] = _clsDesc.getIdentity().getFieldName();
        sqlTypes0[ 0 ] = ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLType();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDescriptor ) {
                jdoFields0[ count ] = ((JDOFieldDescriptor) fields[ i ]).getSQLName();
                sqlTypes0[ count ] = ((JDOFieldDescriptor) fields[ i ]).getSQLType();
                ++count;
            }
        }
        jdoFields = new String[ count ];
        sqlTypes = new int[ count ];
        System.arraycopy( jdoFields0, 0, jdoFields, 0, count );
        System.arraycopy( sqlTypes0, 0, sqlTypes, 0, count );
        return ((BaseFactory) _factory).getCallQuery( spCall, types, _clsDesc.getJavaClass(), jdoFields, sqlTypes );
    }



    public QueryExpression getQueryExpression() {
        return _factory.getQueryExpression();
    }
    
    public QueryExpression getFinder()
    {
        return (QueryExpression) _sqlFinder.clone();
    }


    private Object generateKey( Object conn ) throws PersistenceException
    {
        Object identity;
        Connection connection;

        if ( _keyGen.isInSameConnection() )
            connection = (Connection) conn;
        else 
            connection = getSeparateConnection();
        synchronized ( connection ) {
            identity = _keyGen.generateKey( connection, _clsDesc.getTableName(),
                    ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLName(), null );
        }

        if ( identity == null ) 
            throw new PersistenceExceptionImpl( "persist.noIdentity" );

        return identity;
    }


    public Object create( Object conn, Object[] fields, Object identity )
        throws DuplicateIdentityException, PersistenceException
    {
        PreparedStatement stmt;
        int               count;

        stmt = null;
        try {
            // Must create record in parent table first.
            // All other dependents are created afterwards.
            if ( _extends != null )
                identity = _extends.create( conn, fields, identity );

            if ( _keyGen == null && identity == null )
                throw new PersistenceExceptionImpl( "persist.noIdentity" );

            // Generate key before INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT ) 
                identity = generateKey( conn );

            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.DURING_INSERT ) 
                stmt = ( (Connection) conn ).prepareCall( _sqlCreate );
            else
                stmt = ( (Connection) conn ).prepareStatement( _sqlCreate );

            // Must remember that SQL column index is base one
            count = 1;
            if ( _keyGen == null || _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT ) {
                stmt.setObject( 1, identity );
                ++count;
            }

            for ( int i = 0 ; i < _fields.length ; ++i ) 
                if ( _fields[ i ].store ) {
                    if ( fields[ i ] == null )
                        stmt.setNull( count, _fields[ i ].sqlType );
                    else
                        stmt.setObject( count, fields[ i ], _fields[ i ].sqlType );
                    ++count;
                }

            // Generate key during INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.DURING_INSERT ) {
                CallableStatement cstmt = (CallableStatement) stmt;
                int sqlType;

                sqlType = ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLType();
                cstmt.registerOutParameter( count, sqlType );
                cstmt.execute();

                // First skip all results "for maximum portability"
                // as proposed in CallableStatement javadocs.
                while ( cstmt.getMoreResults() || cstmt.getUpdateCount() != -1 );

                // Identity is returned in the last parameter
                // Workaround: for INTEGER type in Oracle getObject returns BigDecimal
                if ( sqlType == java.sql.Types.INTEGER )
                    identity = new Integer( cstmt.getInt( count ) );
                else 
                    identity = cstmt.getObject( count );
            } else 
                stmt.executeUpdate();
            
            stmt.close();

            // Generate key after INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.AFTER_INSERT )
                identity = generateKey( conn );
            return identity;
        } catch ( SQLException except ) {
            // [oleg] Check for duplicate key based on X/Open error code
            // Bad way: all validation exceptions are reported as DuplicateKey
            //if ( except.getSQLState() != null &&
            //     except.getSQLState().startsWith( "23" ) )
            //    throw new DuplicateIdentityExceptionImpl( _clsDesc.getJavaClass(), identity );

            // Good way: let PersistenceFactory try to determine
            Boolean isDupKey;

            isDupKey = _factory.isDuplicateKeyException( except );
            if ( Boolean.TRUE.equals( isDupKey ) ) {
                throw new DuplicateIdentityExceptionImpl( _clsDesc.getJavaClass(), identity );
            } else if ( Boolean.FALSE.equals( isDupKey ) ) {
                throw new PersistenceExceptionImpl( except );
            }
            // else unknown, let's check directly.

            // [oleg] Check for duplicate key the old fashioned way,
            //        after the INSERT failed to prevent race conditions
            //        and optimize INSERT times
            try {
                // Close the insert statement
                if ( stmt != null )
                    stmt.close();

                stmt = ( (Connection) conn ).prepareStatement( _pkLookup );
                stmt.setObject( 1, identity );
                if ( stmt.executeQuery().next() ) {
                    stmt.close();
                    throw new DuplicateIdentityExceptionImpl( _clsDesc.getJavaClass(), identity );
                }
            } catch ( SQLException except2 ) {
                // Error at the stage indicates it wasn't a duplicate
                // primary key problem. But best if the INSERT error is
                // reported, not the SELECT error.
            }

            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } catch ( SQLException except2 ) { }
            throw new PersistenceExceptionImpl( except );
        }
    }


    public Object store( Object conn, Object[] fields, Object identity,
                         Object[] original, Object stamp )
        throws ObjectModifiedException, ObjectDeletedException, PersistenceException
    {
        PreparedStatement stmt = null;
        int               count;

        try {
            // Must store record in parent table first.
            // All other dependents are stored independently.
            if ( _extends != null )
                _extends.store( conn, fields, identity, original, stamp );

            // In some cases update never happends
            if ( _sqlStore == null )
                return null;

            stmt = ( (Connection) conn ).prepareStatement( original == null ? _sqlStore : _sqlStoreDirty );
            count = 1;
            for ( int i = 0 ; i < _fields.length ; ++i )
                if ( _fields[ i ].store ) {
                    if ( fields[ i ] == null )
                        stmt.setNull( count, _fields[ i ].sqlType );
                    else
                        stmt.setObject( count, fields[ i ], _fields[ i ].sqlType );
                    ++count;
                }
            stmt.setObject( count, identity );
            ++count;
            
            if ( original != null ) {
                for ( int i = 0 ; i < _fields.length ; ++i ) {
                    if ( _fields[ i ].dirtyCheck ) {
                        if ( original[ i ] == null )
                            stmt.setNull( count, _fields[ i ].sqlType );
                        else
                            stmt.setObject( count, original[ i ], _fields[ i ].sqlType );
                        ++count;
                    }
                }
            }
            
            if ( stmt.executeUpdate() <= 0 ) { // SAP DB returns -1
                // If no update was performed, the object has been previously
                // removed from persistent storage or has been modified if
                // dirty checking. Determine which is which.
                stmt.close();
                if ( original != null ) {
                    stmt = ( (Connection) conn ).prepareStatement( _pkLookup );
                    stmt.setObject( 1, identity );
                    if ( stmt.executeQuery().next() ) {
                        stmt.close();
                        throw new ObjectModifiedException( Messages.format( "persist.objectModified", _clsDesc.getJavaClass().getName(), identity ) );
                    }
                    stmt.close();
                }
                
                throw new ObjectDeletedExceptionImpl( _clsDesc.getJavaClass(), identity );
            }
            stmt.close();
            return null;
        } catch ( SQLException except ) {
            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } catch ( SQLException except2 ) { }
            throw new PersistenceExceptionImpl( except );
        }
    }


    public void delete( Object conn, Object identity )
        throws PersistenceException
    {
        PreparedStatement stmt = null;

        try {
            stmt = ( (Connection) conn ).prepareStatement( _sqlRemove );
            stmt.setObject( 1, identity );
            stmt.execute();
            stmt.close();

            // Must delete record in parent table last.
            // All other dependents have been deleted before.
            if ( _extends != null )
                _extends.delete( conn, identity );
        } catch ( SQLException except ) {
            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } catch ( SQLException except2 ) { }
            throw new PersistenceExceptionImpl( except );
        }
    }


    public void writeLock( Object conn, Object identity )
        throws ObjectDeletedException, PersistenceException
    {
        PreparedStatement stmt = null;

        try {
            // Must obtain lock on record in parent table first.
            if ( _extends != null )
                _extends.writeLock( conn, identity );

            stmt = ( (Connection) conn ).prepareStatement( _pkLookup );
            stmt.setObject( 1, identity );
            // If no query was performed, the object has been previously
            // removed from persistent storage. Complain about this.
            if ( ! stmt.executeQuery().next() )
                throw new ObjectDeletedExceptionImpl( _clsDesc.getJavaClass(), identity );
            stmt.close();
        } catch ( SQLException except ) {
            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } catch ( SQLException except2 ) { }
            throw new PersistenceExceptionImpl( except );
        }
    }


    public Object load( Object conn, Object[] fields, Object identity, AccessMode accessMode )
        throws ObjectNotFoundException, PersistenceException
    {
        PreparedStatement stmt;
        ResultSet         rs;
        Object            stamp = null;
        int               count;

        try {
            stmt = ( (Connection) conn ).prepareStatement( ( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad );
            stmt.setObject( 1, identity );

            rs = stmt.executeQuery();
            if ( ! rs.next() )
                throw new ObjectNotFoundExceptionImpl( _clsDesc.getJavaClass(), identity );

            // Load all the fields of the object including one-one relations
            count = 1;
            for ( int i = 0 ; i < _fields.length ; ++i  ) {
                Object value;
                    
                fields[ i ] =  null;
                if ( ! _fields[ i ].load ) 
                    continue;
                if ( _fields[ i ].multi ) {
                    fields[ i ] = new Vector();
                    value = SQLTypes.getObject( rs, count, _fields[ i ].sqlType );
                    if ( value != null )
                        ( (Vector) fields[ i ] ).addElement( value );
                } else {
                    fields[ i ] =  SQLTypes.getObject( rs, count, _fields[ i ].sqlType );
                }
                ++count;
            }

            while ( rs.next() ) {
                count = 1;
                for ( int i = 0; i < _fields.length ; ++i  ) {
                    if ( ! _fields[ i ].load )
                        continue;
                    if ( _fields[ i ].multi ) {
                        Object value;
                        
                        value = SQLTypes.getObject( rs, count, _fields[ i ].sqlType );
                        if ( value != null && ! ( (Vector) fields[ i ] ).contains( value ) )
                            ( (Vector) fields[ i ] ).addElement( value );
                    }
                    ++count;
                }
            }

            rs.close();
            stmt.close();

        } catch ( SQLException except ) {
            throw new PersistenceExceptionImpl( except );
        }
        return stamp;
    }


    private void buildSql( JDOClassDescriptor clsDesc, LogInterceptor logInterceptor )
            throws QueryException
    {
        StringBuffer         sql;
        JDOFieldDescriptor[] jdoFields;
        FieldDescriptor[]    fields;
        int                  count;
        QueryExpression      query;
        String               wherePK;
        String               primKeyName;

        primKeyName = ( (JDOFieldDescriptor) clsDesc.getIdentity() ).getSQLName();
        query = _factory.getQueryExpression();
        query.addColumn( clsDesc.getTableName(), primKeyName );
        query.addParameter( clsDesc.getTableName(), primKeyName, QueryExpression.OpEquals );
        _pkLookup = query.getStatement( true );
        wherePK = JDBCSyntax.Where + _factory.quoteName( ( (JDOFieldDescriptor) clsDesc.getIdentity() ).getSQLName() ) +
            QueryExpression.OpEquals + JDBCSyntax.Parameter;

        fields = clsDesc.getFields();
        jdoFields = new JDOFieldDescriptor[ fields.length ];
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDescriptor )
                jdoFields[ i ] = (JDOFieldDescriptor) fields[ i ];
        }

        // Create statement to insert a new row into the table
        // using the specified primary key if one is required
        sql = new StringBuffer( "INSERT INTO " );
        sql.append( _factory.quoteName( clsDesc.getTableName() ) ).append( " (" );
        count = 0;
        if ( _keyGen == null || _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT ) {
            sql.append( _factory.quoteName( primKeyName ) );
            ++count;
        }
        for ( int i = 0 ; i < jdoFields.length ; ++i ) {
            if ( jdoFields[ i ] != null 
         // Don't try to insert many-many pseudo-fields
         && jdoFields[ i ].getManyTable() == null ) {
                if ( count > 0 ) {
                    sql.append( ',' );
                }
        sql.append( _factory.quoteName( jdoFields[ i ].getSQLName() ) );
        ++count;
            }
        }
        // it is possible to have no fields in INSERT statement:
        // only the primary key field in the table,
        // with KeyGenerator DURING_INSERT or BEFORE_INSERT
        if ( count == 0 ) 
            sql.setLength( sql.length() - 2 ); // cut " ("
        else 
            sql.append( ")" );
        sql.append( " VALUES (" );
        for ( int i = 0 ; i < count ; ++i ) {
            if ( i > 0 )
                sql.append( ',' );
            sql.append( '?' );
        }
        sql.append( ')' );
        _sqlCreate = sql.toString();
        if ( _keyGen != null ) {
            try {
                _sqlCreate = _keyGen.patchSQL( _sqlCreate, primKeyName );
            } catch ( MappingException except )  {
                Logger.getSystemLogger().println( except.toString() );

                // proceed without this stupid key generator
                _keyGen = null;
                buildSql( clsDesc, logInterceptor );
                return;
            }
            if ( _keyGen.getStyle() == KeyGenerator.DURING_INSERT )
                _sqlCreate = "{call " + _sqlCreate + "}";
        }
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for creating " + clsDesc.getJavaClass().getName() +
                                           ": " + _sqlCreate );


        sql = new StringBuffer( "DELETE FROM " ).append( _factory.quoteName( clsDesc.getTableName() ) );
        sql.append( wherePK );
        _sqlRemove = sql.toString();
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for deleting " + clsDesc.getJavaClass().getName() +
                                           ": " + _sqlRemove );


        sql = new StringBuffer( "UPDATE " );
        sql.append( _factory.quoteName( clsDesc.getTableName() ) ).append( " SET " );
        count = 0;
        for ( int i = 0 ; i < jdoFields.length ; ++i ) {
            if ( jdoFields[ i ] != null
         // Don't try to update many-many pseudo-fields
         && jdoFields[ i ].getManyTable() == null ) {
                if ( count > 0 )
                    sql.append( ',' );
                sql.append( _factory.quoteName( jdoFields[ i ].getSQLName() ) ).append( "=?" );
                ++count;
            }
        }
        // The table may contain only identity column, in this case UPDATE is never needed
        if ( count > 0 ) {
            sql.append( wherePK );
            _sqlStore = sql.toString();

            for ( int i = 0 ; i < jdoFields.length ; ++i ) {
                if ( jdoFields[ i ] != null
             // Don't try to update many-many pseudo-fields
             && jdoFields[ i ].getManyTable() == null ) {
                    if ( jdoFields[ i ].isDirtyCheck() )
                        sql.append( " AND " ).append( _factory.quoteName( jdoFields[ i ].getSQLName() ) ).append( "=?" );
                }
            }
            _sqlStoreDirty = sql.toString();
            if ( logInterceptor != null )
                logInterceptor.storeStatement( "SQL for updating " + clsDesc.getJavaClass().getName() +
                                               ": " + _sqlStoreDirty );
        }
    }


    private void buildFinder( JDOClassDescriptor clsDesc, LogInterceptor logInterceptor )
        throws MappingException, QueryException
    {
        Vector          fields;
        QueryExpression expr;

        fields = new Vector();
        expr = _factory.getQueryExpression();
        addLoadSql( clsDesc, expr, fields, false, true, true );

        _sqlLoad = expr.getStatement( false );
        _sqlLoadLock = expr.getStatement( true );
        _fields = new FieldInfo[ fields.size() ];
        fields.copyInto( _fields );
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for loading " + clsDesc.getJavaClass().getName() +
                                           ":  " + _sqlLoad );

        _sqlFinder = _factory.getQueryExpression();
        addLoadSql( clsDesc, _sqlFinder, fields, true, false, true );
    }


    private void addLoadSql( JDOClassDescriptor clsDesc, QueryExpression expr, Vector allFields,
                             boolean loadPk, boolean queryPk, boolean store )
        throws MappingException
    {
        FieldDescriptor[]    fields;
        JDOClassDescriptor   extend;
        FieldDescriptor      identity;
        String               identitySQL;

        identity = clsDesc.getIdentity();
        identitySQL = ( (JDOFieldDescriptor) identity ).getSQLName();

        // If this class extends another class, create a join with the parent table and
        // add the load fields of the parent class (but not the store fields)
        if ( clsDesc.getExtends() != null ) {
            expr.addInnerJoin( clsDesc.getTableName(), identitySQL,
                               ( (JDOClassDescriptor) clsDesc.getExtends() ).getTableName(),
                               ( (JDOFieldDescriptor) clsDesc.getExtends().getIdentity() ).getSQLName() );
            addLoadSql( (JDOClassDescriptor) clsDesc.getExtends(), expr, allFields,
            loadPk, queryPk, false );
            loadPk = false;
            queryPk = false;
        }

        if ( loadPk  )
            expr.addColumn( clsDesc.getTableName(), identitySQL );
        if ( queryPk )
            expr.addParameter( clsDesc.getTableName(), identitySQL, QueryExpression.OpEquals );

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDescriptor ) {
                JDOClassDescriptor relDesc;

                relDesc = (JDOClassDescriptor) fields[ i ].getClassDescriptor();
                if ( relDesc == null || ( (JDOFieldDescriptor) fields[ i ] ).getManyTable() == null ) {
                    expr.addColumn( clsDesc.getTableName(),
                                    ( (JDOFieldDescriptor) fields[ i ] ).getSQLName() );
                    if ( relDesc == null )
                        allFields.addElement( new FieldInfo( fields[ i ], store ) );
                    else
                        allFields.addElement( new FieldInfo( fields[ i ], store,
                                                             ( (JDOFieldDescriptor) relDesc.getIdentity() ).getSQLType() ) );
                } else {
                    expr.addColumn( ( (JDOFieldDescriptor) fields[ i ] ).getManyTable(),
                                    ( (JDOFieldDescriptor) fields[ i ] ).getSQLName() );
                    expr.addOuterJoin( clsDesc.getTableName(), identitySQL,
                                       ( (JDOFieldDescriptor) fields[ i ] ).getManyTable(),
                                       ( (JDOFieldDescriptor) fields[ i ] ).getManyKey() );
                    allFields.addElement( new FieldInfo( fields[ i ], false ) );
                }
            } else {
                JDOClassDescriptor relDesc;

                relDesc = (JDOClassDescriptor) fields[ i ].getClassDescriptor();
                if ( relDesc == null )
                    allFields.addElement( new FieldInfo( fields[ i ], false ) );
                else {
                    FieldDescriptor[] relFields;
                    String            foreKey = null;

                    relFields = relDesc.getFields();
                    for ( int j = 0 ; j < relFields.length ; ++j ) {
                        if ( relFields[ j ] instanceof JDOFieldDescriptor &&
                             relFields[ j ].getClassDescriptor() == clsDesc ) {
                                foreKey = ( (JDOFieldDescriptor) relFields[ j ] ).getSQLName();
                                break;
                             }
                    }
                    if ( foreKey == null ) 
                        throw new MappingException( "mapping.noRelation", relDesc.getTableName(), fields[ i ] );
                    else {
                        expr.addColumn( relDesc.getTableName(), ( (JDOFieldDescriptor) relDesc.getIdentity() ).getSQLName() );
                        expr.addOuterJoin( clsDesc.getTableName(), ( (JDOFieldDescriptor) identity ).getSQLName(),
                                           relDesc.getTableName(), foreKey );
                        allFields.addElement( new FieldInfo( fields[ i ], false ) );
                    }
                }
            }
        }
    }


    public String toString()
    {
        return _clsDesc.toString();
    }


    static final class FieldInfo
    {

        final boolean load;

        final boolean store;

        final String  name;

        final boolean multi;

        final boolean dirtyCheck;

        int           sqlType;

        FieldInfo( FieldDescriptor fieldDesc, boolean store )
        {
            this.name = fieldDesc.getFieldName();
            this.store = store;
            this.multi = fieldDesc.isMultivalued();
            this.load = (fieldDesc instanceof JDOFieldDescriptor) || this.multi;
            if ( store && fieldDesc instanceof JDOFieldDescriptor ) {
                this.dirtyCheck = ( (JDOFieldDescriptor) fieldDesc ).isDirtyCheck();
                this.sqlType = ( (JDOFieldDescriptor) fieldDesc ).getSQLType();
            } else {
                this.dirtyCheck = false;
                if ( this.multi )
                    this.sqlType = ( (JDOFieldDescriptor) fieldDesc.getClassDescriptor().getIdentity() ).getSQLType();
                else
                    this.sqlType = java.sql.Types.OTHER;
            }
        }

        FieldInfo( FieldDescriptor fieldDesc, boolean store, int sqlType )
        {
            this( fieldDesc, store );
            this.sqlType = sqlType;
        }

        public String toString()
        {
            return name;
        }

    }


    static final class SQLQuery
        implements PersistenceQuery
    {


        private PreparedStatement _stmt;


        private ResultSet         _rs;


        private final SQLEngine _engine;


        private final Class[]   _types;


        private final Object[]  _values;


        private final String    _sql;


        private Object         _lastIdentity;


        private int            _identSqlType;


        private boolean        _resultSetDone;

        SQLQuery( SQLEngine engine, String sql, Class[] types )
        {
            _engine = engine;
            _types = types;
            _values = new Object[ _types.length ];
            _sql = sql;
            _identSqlType = ( (JDOFieldDescriptor) _engine._clsDesc.getIdentity() ).getSQLType();
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
            return _engine._clsDesc.getJavaClass();
        }


        public void execute( Object conn, AccessMode accessMode )
            throws QueryException, PersistenceException
        {
            _lastIdentity = null;
            try {
                _stmt = ( (Connection) conn ).prepareStatement( _sql );
                for ( int i = 0 ; i < _values.length ; ++i ) {
                    _stmt.setObject( i + 1, _values[ i ] );
                    _values[ i ] = null;
                }
                _rs = _stmt.executeQuery();
                _resultSetDone = false;
            } catch ( SQLException except ) {
                if ( _stmt != null ) {
                    try {
                        _stmt.close();
                    } catch ( SQLException e2 ) { }
                }
                _resultSetDone = true;
                throw new PersistenceExceptionImpl( except );
            }
        }


        public Object nextIdentity( Object identity )
            throws PersistenceException
        {
            try {
                if ( _resultSetDone )
                    return null;

                if ( _lastIdentity == null ) {
                    if ( ! _rs.next() ) {
                        _resultSetDone = true;
                        return null;
                    }
                    _lastIdentity = SQLTypes.getObject( _rs, 1, _identSqlType );
                    return _lastIdentity;
                }

                while ( _lastIdentity.equals( identity ) ) {
                    if ( ! _rs.next() ) {
                        _lastIdentity = null;
                        _resultSetDone = true;
                        return null;
                    }
                    _lastIdentity = SQLTypes.getObject( _rs, 1, _identSqlType );
                }
                return _lastIdentity;
            } catch ( SQLException except ) {
                _lastIdentity = null;
                throw new PersistenceExceptionImpl( except );
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


        public Object fetch( Object[] fields, Object identity )
            throws ObjectNotFoundException, PersistenceException
        {
            int    count;
            Object stamp = null;

            try {

                // Load all the fields of the object including one-one relations
                count = 2;
                for ( int i = 0 ; i < _engine._fields.length ; ++i  ) {
                    Object value;
                  
                    fields[ i ] = null;
                    if ( ! _engine._fields[ i ].load ) 
                        continue;
                    value = SQLTypes.getObject( _rs, count, _engine._fields[ i ].sqlType );
                    if ( _engine._fields[ i ].multi ) {
                        fields[ i ] = new Vector();
                        if ( value != null )
                            ( (Vector) fields[ i ] ).addElement( value );
                    } else
                        fields[ i ] = value;
                    ++count;
                }

                if ( !_resultSetDone && _rs.next() ) {
                    _lastIdentity = SQLTypes.getObject( _rs, 1, _identSqlType );
                    while ( identity.equals( _lastIdentity ) ) {
                        count = 2;
                        for ( int i = 0; i < _engine._fields.length ; ++i  ) {
                            if ( ! _engine._fields[ i ].load )
                                continue;
                            if ( _engine._fields[ i ].multi ) {
                                Object value;

                                value = SQLTypes.getObject( _rs, count, _engine._fields[ i ].sqlType );
                                if ( value != null && ! ( (Vector) fields[ i ] ).contains( value ) )
                                    ( (Vector) fields[ i ] ).addElement( value );
                            }
                            ++count;
                        }
                        if ( _rs.next() ) {
                            _lastIdentity = SQLTypes.getObject( _rs, 1, _identSqlType );
                        } else {
                            _lastIdentity = null;
                            _resultSetDone = true;
                        }
                    }
                } else {
                    _lastIdentity = null;
                    _resultSetDone = true;
                }
            } catch ( SQLException except ) {
                throw new PersistenceExceptionImpl( except );
            }
            return stamp;
        }

    }


}
