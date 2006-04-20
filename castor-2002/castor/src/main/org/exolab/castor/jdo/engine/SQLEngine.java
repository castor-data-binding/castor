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
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.persist.DuplicateIdentityExceptionImpl;
import org.exolab.castor.persist.PersistenceExceptionImpl;
import org.exolab.castor.persist.ObjectNotFoundExceptionImpl;
import org.exolab.castor.persist.ObjectDeletedExceptionImpl;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.LogInterceptor;
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
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
final class SQLEngine
    implements Persistence
{


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


    SQLEngine( JDOClassDescriptor clsDesc, LogInterceptor logInterceptor,
               PersistenceFactory factory, String stampField )
        throws MappingException
    {
        _clsDesc = clsDesc;
        _stampField = stampField;
        _factory = factory;
        _logInterceptor = logInterceptor;
        if ( _clsDesc.getExtends() != null )
            _extends = new SQLEngine( (JDOClassDescriptor) _clsDesc.getExtends(), null,
				      _factory, _stampField );
        try {
            buildSql( _clsDesc, _logInterceptor );
            buildFinder( _clsDesc, _logInterceptor );
        } catch ( QueryException except ) {
            throw new MappingException( except );
        }
    }


    /**
     * Used by {@link OQLQuery} to retrieve the class descriptor.
     */
    JDOClassDescriptor getDescriptor()
    {
	return _clsDesc;
    }


    public PersistenceQuery createQuery( QueryExpression query, Class[] types )
        throws QueryException
    {
        String sql;

        sql = query.getStatement( _clsDesc.getAccessMode() == AccessMode.DbLocked);
        if ( _logInterceptor != null )
            _logInterceptor.queryStatement( sql );
        return new SQLQuery( this, sql, types );
    }


    public QueryExpression getFinder()
    {
        return (QueryExpression) _sqlFinder.clone();
    }


    public Object create( Object conn, Object[] fields, Object identity )
        throws DuplicateIdentityException, PersistenceException
    {
        PreparedStatement stmt;
        int               count;

        stmt = null;
        try {
            // Must create record in parent table first.
            // All other relations have been created before hand.
            if ( _extends != null )
                _extends.create( conn, fields, identity );

            // Must remember that SQL column index is base one
            stmt = ( (Connection) conn ).prepareStatement( _sqlCreate );
            stmt.setObject( 1, identity );
            count = 2;
            for ( int i = 0 ; i < _fields.length ; ++i )
                if ( _fields[ i ].store ) {
                    stmt.setObject( count, fields[ i ] );
                    ++count;
                }
            stmt.executeUpdate();
            stmt.close();
            return null;
        } catch ( SQLException except ) {
            // [oleg] Check for duplicate key based on X/Open error code
            if ( except.getSQLState() != null &&
                 except.getSQLState().startsWith( "23" ) )
                throw new DuplicateIdentityExceptionImpl( _clsDesc.getJavaClass(), identity );

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
            // All other relations have been created before hand.
            if ( _extends != null )
                _extends.store( conn, fields, identity, original, stamp );

            stmt = ( (Connection) conn ).prepareStatement( original == null ? _sqlStore : _sqlStoreDirty );
            count = 1;
            for ( int i = 0 ; i < _fields.length ; ++i )
                if ( _fields[ i ].store ) {
                    stmt.setObject( count, fields[ i ] );
                    ++count;
                }
            stmt.setObject( count, identity );
            ++count;

	    if ( original != null ) {
		for ( int i = 0 ; i < _fields.length ; ++i )
		    if ( _fields[ i ].dirtyCheck ) {
			stmt.setObject( count, original[ i ] );
			++count;
		    }
	    }

            if ( stmt.executeUpdate() == 0 ) {
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
            // Must delete record in parent table first.
            // All other relations will be deleted after wards.
            if ( _extends != null )
                _extends.delete( conn, identity );

            stmt = ( (Connection) conn ).prepareStatement( _sqlRemove );
            stmt.setObject( 1, identity );
            stmt.execute();
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

        try {
            stmt = ( (Connection) conn ).prepareStatement( ( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad );
            stmt.setObject( 1, identity );

            rs = stmt.executeQuery();
            if ( ! rs.next() )
                throw new ObjectNotFoundExceptionImpl( _clsDesc.getJavaClass(), identity );

            // Load all the fields of the object including one-one relations
            for ( int i = 0 ; i < _fields.length ; ++i  ) {
                if ( _fields[ i ].multi ) {
                    Object value;
                    
                    fields[ i ] = new Vector();
                    value = rs.getObject( i + 1 );
                    if ( value != null )
                        ( (Vector) fields[ i ] ).addElement( value );
                } else
                    fields[ i ] = rs.getObject( i + 1 );
            }

            while ( rs.next() ) {
                for ( int i = 0; i < _fields.length ; ++i  )
                    if ( _fields[ i ].multi ) {
                        Object value;
                        
                        value = rs.getObject( i + 1 );
                        if ( value != null && ! ( (Vector) fields[ i ] ).contains( value ) )
                            ( (Vector) fields[ i ] ).addElement( value );
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

        query = _factory.getQueryExpression();
        query.addParameter( clsDesc.getTableName(), ( (JDOFieldDescriptor) clsDesc.getIdentity() ).getSQLName(),
                            QueryExpression.OpEquals );
        _pkLookup = query.getStatement( true );
        wherePK = JDBCSyntax.Where + ( (JDOFieldDescriptor) clsDesc.getIdentity() ).getSQLName() +
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
        sql.append( clsDesc.getTableName() ).append( " (" );
        sql.append( ( (JDOFieldDescriptor) clsDesc.getIdentity() ).getSQLName() );
        count = 1;
        for ( int i = 0 ; i < jdoFields.length ; ++i ) {
            if ( jdoFields[ i ] != null ) {
                sql.append( ',' );
                sql.append( jdoFields[ i ].getSQLName() );
                ++count;
            }
        }
        sql.append( ") VALUES (" );
        for ( int i = 0 ; i < count ; ++i ) {
            if ( i > 0 )
                sql.append( ',' );
            sql.append( '?' );
        }
        sql.append( ')' );
        _sqlCreate = sql.toString();
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for creating " + clsDesc.getJavaClass().getName() +
                                           ": " + _sqlCreate );


        sql = new StringBuffer( "DELETE FROM " ).append( clsDesc.getTableName() );
        sql.append( wherePK );
        _sqlRemove = sql.toString();
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for deleting " + clsDesc.getJavaClass().getName() +
                                           ": " + _sqlRemove );


        sql = new StringBuffer( "UPDATE " );
        sql.append( clsDesc.getTableName() ).append( " SET " );
        count = 0;
        for ( int i = 0 ; i < jdoFields.length ; ++i ) {
            if ( jdoFields[ i ] != null ) {
                if ( count > 0 )
                    sql.append( ',' );
                sql.append( jdoFields[ i ].getSQLName() ).append( "=?" );
                ++count;
            }
        }
        sql.append( wherePK );
        _sqlStore = sql.toString();

        for ( int i = 0 ; i < jdoFields.length ; ++i ) {
            if ( jdoFields[ i ] != null ) {
                if ( jdoFields[ i ].isDirtyCheck() )
                    sql.append( " AND " ).append( jdoFields[ i ].getSQLName() ).append( "=?" );
            }
        }
        _sqlStoreDirty = sql.toString();
        if ( logInterceptor != null )
            logInterceptor.storeStatement( "SQL for updating " + clsDesc.getJavaClass().getName() +
                                           ": " + _sqlStoreDirty );
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
                               ( (JDOClassDescriptor) clsDesc.getExtends() ).getTableName(), identitySQL );
            addLoadSql( (JDOClassDescriptor) clsDesc.getExtends(), expr, allFields,
			true, queryPk, false );
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
                    allFields.addElement( new FieldInfo( fields[ i ], store ) );
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
                    System.out.println( "No relation for " + fields[ i ] );
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
                    if ( foreKey != null ) {
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

        final boolean store;

        final String  name;

        final boolean multi;

        final boolean dirtyCheck;

        FieldInfo( FieldDescriptor fieldDesc, boolean store )
        {
            this.name = fieldDesc.getFieldName();
            this.store = store;
            this.multi = fieldDesc.isMultivalued();
            if ( store && fieldDesc instanceof JDOFieldDescriptor )
                this.dirtyCheck = ( (JDOFieldDescriptor) fieldDesc ).isDirtyCheck();
            else
                this.dirtyCheck = false;
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


        SQLQuery( SQLEngine engine, String sql, Class[] types )
        {
            _engine = engine;
            _types = types;
            _values = new Object[ _types.length ];
            _sql = sql;
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
            } catch ( SQLException except ) {
                if ( _stmt != null ) {
                    try {
                        _stmt.close();
                    } catch ( SQLException e2 ) { }
                }
                throw new PersistenceExceptionImpl( except );
            }
        }


        public Object nextIdentity( Object identity )
            throws PersistenceException
        {
            try {
                if ( _lastIdentity == null ) {
                    if ( ! _rs.next() )
                        return null;
                    _lastIdentity = _rs.getObject( 1 );
                    return _lastIdentity;
                }

                while ( _lastIdentity.equals( identity ) ) {
                    if ( ! _rs.next() ) {
                        _lastIdentity = null;
                        return null;
                    }
                    _lastIdentity = _rs.getObject( 1 );
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
                count = 2;

                // Load all the fields of the object including one-one relations
                for ( int i = 0 ; i < _engine._fields.length ; ++i  ) {
                    if ( _engine._fields[ i ].multi ) {
                        Object value;

                        fields[ i ] = new Vector();
                        value = _rs.getObject( i + count );
                        if ( value != null )
                            ( (Vector) fields[ i ] ).addElement( value );
                    } else
                        fields[ i ] = _rs.getObject( i + count );
                }

                if ( _rs.next() ) {
                    _lastIdentity = _rs.getObject( 1 );
                    while ( identity.equals( _lastIdentity ) ) {
                        for ( int i = 0; i < _engine._fields.length ; ++i  )
                            if ( _engine._fields[ i ].multi ) {
                                Object value;

                                value = _rs.getObject( i + count );
                                if ( value != null && ! ( (Vector) fields[ i ] ).contains( value ) )
                                    ( (Vector) fields[ i ] ).addElement( value );
                            }
                        if ( _rs.next() )
                            _lastIdentity = _rs.getObject( 1 );
                        else
                            _lastIdentity = null;
                    }
                } else
                    _lastIdentity = null;
            } catch ( SQLException except ) {
                throw new PersistenceExceptionImpl( except );
            }
            return stamp;
        }

    }


}
