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


import java.io.PrintWriter;
import java.util.Vector;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.odmg.ObjectNotPersistentException;
import org.exolab.castor.jdo.ODMGSQLException;
import org.exolab.castor.jdo.DuplicatePrimaryKeyException;
import org.exolab.castor.jdo.desc.PrimaryKeyDesc;
import org.exolab.castor.jdo.desc.FieldDesc;
import org.exolab.castor.jdo.desc.ObjectDesc;
import org.exolab.castor.jdo.desc.RelationDesc;
import org.exolab.castor.jdo.desc.Relation;
import org.exolab.castor.util.Logger;


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
class SQLEngine
{


    private ObjectDesc       _objDesc;


    private PrimaryKeyDesc  _primKey;


    private boolean         _createRequiresPK = true;


    private String          _pkLookup;


    private String          _sqlCreate;


    private String          _sqlRemove;


    private String          _sqlStore;


    private String          _sqlLoad;


    String          _sqlFinder;
    String          _sqlFinderJoin;


    private FieldDesc[]     _loadFields;


    private SQLRelated[]     _related;


    private SQLEngine        _extends;


    SQLEngine( ObjectDesc objDesc, PrintWriter logWriter )
    {
	if ( objDesc == null )
	    throw new IllegalArgumentException( "Argument 'objDesc' is null" );
	_objDesc = objDesc;
	_primKey = _objDesc.getPrimaryKey();
	if ( _primKey == null )
	    throw new ODMGRuntimeException( "Cannot persist a table that lacks a primary key descriptor" );
	if ( _objDesc.getPrimaryKeyField() == null )
	    throw new ODMGRuntimeException( "Cannot persist an object with an external primary key" );
	buildCreateSql();
	buildRemoveSql();
	buildStoreSql();
	buildLoadSql();
	buildRelated( logWriter );
	if ( logWriter != null ) {
	    logWriter.println( "SQL for " + _objDesc.getObjectClass().getName() +
			       ": " + _sqlLoad );
	    logWriter.println( "SQL for " + _objDesc.getObjectClass().getName() +
			       ": " + _sqlCreate );
	    logWriter.println( "SQL for " + _objDesc.getObjectClass().getName() +
			       ": " + _sqlStore );
	    logWriter.println( "SQL for " + _objDesc.getObjectClass().getName() +
			       ": " + _sqlRemove );
	}
	if ( _objDesc.getExtends() != null )
	    _extends = new SQLEngine( _objDesc.getExtends(), logWriter );
    }


    SQLEngine( ObjectDesc objDesc, PrimaryKeyDesc primKey, PrintWriter logWriter )
    {
	this( objDesc, logWriter );
	_primKey = primKey;
    }


    ObjectDesc getObjectDesc()
    {
	return _objDesc;
    }


    /**
     * Must be called with object and primary key. Creates the row(s)
     * associated with the object and it's direct relationships. Complains
     * if a row with the same primary key is found in the database.
     */
    public void create( Connection conn, Object obj, Object primKey )
	throws DuplicatePrimaryKeyException, ODMGRuntimeException, ODMGSQLException
    {
	FieldDesc[]       descs;
	PreparedStatement stmt;
	int               i, j;
	int               count;
	Object            value;

	try {
	    if ( _related != null ) {
		for ( i = 0 ; i < _related.length ; ++i ) {
		    if ( _related[ i ].getRelationType() == Relation.OneToOne ) {
			_related[ i ].create( conn, obj, primKey );
		    }
		}
	    }
	    if ( _extends != null ) {
		_extends.create( conn, obj, primKey );
	    }

	    // If creation requires a primary key to be supplied, must check
	    // that no such primary key exists in the table. This call will
	    // also lock the table against creation of an object with such
	    // a primary key.
	    if ( _createRequiresPK ) {
		if ( primKey == null )
		    throw new ODMGRuntimeException( "This implementation requires a primary key to be set prior to object creation" );

		// Check that there is no duplicity in the table
		stmt = conn.prepareStatement( _pkLookup );
		if ( _primKey.isPrimitive() ) {
		    stmt.setObject( 1, primKey );
		} else {
		    descs = _primKey.getFieldDescs();
		    for ( i = 0 ; i < descs.length ; ++i ) {
			stmt.setObject( i + 1, descs[ i ].getValue( primKey ) );
		    }
		}
		if ( stmt.executeQuery().next() )
		    throw new DuplicatePrimaryKeyException( obj.getClass(), primKey );
		stmt.close();
	    }
	    
	    // Must remember that SQL column index is base one
	    count = 1;
	    stmt = conn.prepareStatement( _sqlCreate );
	    if ( _createRequiresPK ) {
		if ( _primKey.isPrimitive() ) {
		    stmt.setObject( count, primKey );
		    count += 1;
		} else {
		    primKey = _objDesc.getPrimaryKeyField().getValue( obj );
		    descs = _primKey.getFieldDescs();
		    for ( i = 0 ; i < descs.length ; ++i ) {
			stmt.setObject( count + i, descs[ i ].getValue( primKey ) );
		    }
		    count += i;
		}
	    }
	    
	    descs = _objDesc.getFieldDescs();
	    for ( i = 0 ; i < descs.length ; ++i ) {
		value = descs[ i ].getValue( obj );
		if ( value != null )
		    stmt.setObject( count + i, value );
	    }
	    count += i;
	    if ( _objDesc.getRelated() != null ) {
		for ( i = 0 ; i < _objDesc.getRelated().length ; ++i ) {
		    RelationDesc related;
		    
		    related = _objDesc.getRelated()[ i ];
		    if ( related.getRelationType() == Relation.ManyToOne ) {
			if ( related.getPrimaryKey().isPrimitive() ) {
			    value = related.getPrimaryKeyField().getValue( obj );
			    if ( value != null )
				stmt.setObject( count, value );
			    count += 1;
			} else {
			    primKey = related.getPrimaryKeyField().getValue( obj );
			    descs = related.getPrimaryKey().getFieldDescs();
			    for ( j = 0 ; j < descs.length ; ++j ) {
				value = descs[ j ].getValue( primKey );
				if ( value != null )
				    stmt.setObject( count + j, value );
			    }
			    count += j;
			}
		    }
		}
	    }

	    stmt.executeUpdate();
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
    }


    /**
     * Must be called with object and primary key. Loads the row(s)
     * associated with the object and it's direct relationships. Complains
     * if the object is not found in the database. Will not create the
     * object in order to load it.
     */
    public boolean load( Connection conn, Object obj, Object primKey )
	throws  ODMGSQLException
    {
	PreparedStatement stmt;
	ResultSet         rs;
	FieldDesc[]       pkDescs = null;
	Object            pk;
	Object            thisPk;

	try {
	    stmt = conn.prepareStatement( _sqlLoad );
	    if ( _primKey.isPrimitive() ) {
		stmt.setObject( 1, primKey );
	    } else {
		pkDescs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < pkDescs.length ; ++i ) {
		    stmt.setObject( 1 + i, pkDescs[ i ].getValue( primKey ) );
		}
	    }
	    
	    rs = stmt.executeQuery();
	    if ( ! rs.next() ) {
		return false;
	    }
	    _objDesc.getPrimaryKeyField().setValue( obj, primKey );
	    
	    do {
		// First iteration for a PK: traverse all the fields
		for ( int i = 0; i < _loadFields.length ; ++i  ) {
		    // Usinging typed setValue so float/double, int/long
		    // can be intermixed with automatic conversion, something
		    // that throws an exception in the untyped version
		    _loadFields[ i ].setValue( obj, rs, i + 1 );
		}
	    } while ( rs.next() );
	    rs.close();
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
	return true;
    }


    public Object query( Connection conn, String sql, Object[] values )
	throws  ODMGSQLException
    {
	PreparedStatement stmt;
	Object            obj;
	ResultSet         rs;
	int               count;
	Object            pk;
	FieldDesc[]       pkDescs;

	try {
	    obj = _objDesc.createNew();
	    stmt = conn.prepareStatement( sql );
	    for ( int i = 0 ; i < values.length ; ++i ) {
		stmt.setObject( i + 1, values[ i ] );
	    }

	    rs = stmt.executeQuery();
	    if ( ! rs.next() ) {
		return null;
	    }

	    if ( _primKey.isPrimitive() ) {
		pk = rs.getObject( 1 );
		count = 2;
	    } else {
		pk = _primKey.createNew();
		pkDescs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < pkDescs.length ; ++i ) {
		    pkDescs[ i ].setValue( pk, rs, 1 + i );
		}
		count = pkDescs.length + 1;
	    }
	    _objDesc.getPrimaryKeyField().setValue( obj, pk );
	    
	    do {
		// First iteration for a PK: traverse all the fields
		for ( int i = 0; i < _loadFields.length ; ++i  ) {
		    // Usinging typed setValue so float/double, int/long
		    // can be intermixed with automatic conversion, something
		    // that throws an exception in the untyped version
		    _loadFields[ i ].setValue( obj, rs, i + count );
		}
	    } while ( rs.next() );
	    rs.close();
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
	return obj;
    }


    /**
     * Must be called with object and primary key. Removes the row(s)
     * associated with the object and it's direct relationships. Complains
     * if the object is no longer in the database. If the primary key of
     * the object has been changed, it must be removed/created and not
     * stored.
     */
    public void store( Connection conn, Object obj, Object primKey )
	throws ObjectNotPersistentException, ODMGSQLException
    {
	PreparedStatement stmt;
	FieldDesc[]       descs;
	RelationDesc      related;
	int               i, j;
	int               count;
	Object            value;

	try {

	    if ( _related != null ) {
		for ( i = 0 ; i < _related.length ; ++i ) {
		    if ( _related[ i ].getRelationType() == Relation.OneToOne ) {
			_related[ i ].store( conn, obj, primKey );
		    }
		}
	    }
	    if ( _extends != null ) {
		_extends.store( conn, obj, primKey );
	    }

	    stmt = conn.prepareStatement( _sqlStore );

	    descs = _objDesc.getFieldDescs();
	    count = 1;
	    for ( i = 0 ; i < descs.length ; ++i ) {
		value = descs[ i ].getValue( obj );
		if ( value != null )
		    stmt.setObject( count + i, value );
	    }
	    count += i;
	    if ( _objDesc.getRelated() != null ) {
		for ( i = 0 ; i < _objDesc.getRelated().length ; ++i ) {
		    related = _objDesc.getRelated()[ i ];
		    
		    if ( related.getRelationType() == Relation.ManyToOne ) {
			if ( related.getPrimaryKey().isPrimitive() ) {
			    value = related.getPrimaryKeyField().getValue( obj );
			    if ( value != null )
				stmt.setObject( count, value );
			    count += 1;
			} else {
			    primKey = related.getPrimaryKeyField().getValue( obj );
			    descs = related.getPrimaryKey().getFieldDescs();
			    for ( j = 0 ; j < descs.length ; ++j ) {
				value = descs[ j ].getValue( primKey );
				if ( value != null )
				    stmt.setObject( count + j, value );
			    }
			    count += j;
			}
		    }
		}
	    }
	    
	    if ( _primKey.isPrimitive() ) {
		stmt.setObject( count, primKey );
	    } else {
		descs = _primKey.getFieldDescs();
		for ( i = 0 ; i < descs.length ; ++i ) {
		    stmt.setObject( count + i, descs[ i ].getValue( primKey ) );
		}
	    }
	    if ( stmt.executeUpdate() == 0 ) {
		// If no update was performed, the object has been previously
		// removed from persistent storage. Complain about this.
		stmt.close();
		throw new ObjectNotPersistentExceptionImpl( obj );
	    }
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
    }


    /**
     * Must be called with object and primary key. Removes the row(s)
     * associated with the object and it's direct relationships. Complains
     * if the object is no longer in the database.
     */
    public void delete( Connection conn, Object obj, Object primKey )
	throws ObjectNotPersistentException, ODMGSQLException
    {
	PreparedStatement stmt;
	FieldDesc[]       descs;

	try {
	    for ( int i = 0 ; i < _related.length ; ++i ) {
		if ( _related[ i ].getRelationType() == Relation.OneToOne ) {
		    _related[ i ].delete( conn, obj, primKey );
		}
	    }
	    if ( _extends != null ) {
		_extends.delete( conn, obj, primKey );
	    }

	    stmt = conn.prepareStatement( _sqlRemove );
	    if ( _primKey.isPrimitive() ) {
		stmt.setObject( 1, primKey );
	    } else {
		descs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    stmt.setObject( 1 + i, descs[ i ].getValue( primKey ) );
		}
	    }
	    if ( stmt.executeUpdate() == 0 ) {
		// If no remove was performed, the object has been previously
		// removed from persistent storage. Complain about this.
		stmt.close();
		throw new ObjectNotPersistentExceptionImpl( primKey );
	    }
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
    }


    /**
     * Must be called with object, primary key and either write or read.
     * Only write locks are implemented by locking the database row.
     * No need to call for read locks. Should not be called for an
     * object that was created, only if loaded/deleted.
     */
    public void writeLock( Connection conn, Object obj, Object primKey )
	throws ObjectNotPersistentException, ODMGSQLException
    {
	PreparedStatement stmt;
	FieldDesc[]       descs;

	try {
	    if ( _extends != null ) {
		_extends.writeLock( conn, obj, primKey );
	    }
	    // Only write locks are implemented by locking the row.
	    stmt = conn.prepareStatement( _pkLookup );
	    if ( _primKey.isPrimitive() ) {
		stmt.setObject( 1, primKey );
	    } else {
		descs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    stmt.setObject( i + 1, descs[ i ].getValue( primKey ) );
		}
	    }
	    if ( ! stmt.executeQuery().next() )
		throw new ObjectNotPersistentExceptionImpl( obj );
	    stmt.close();
	} catch ( SQLException except ) {
	    throw new ODMGSQLException( except );
	}
    }



    protected void buildRelated( PrintWriter logWriter )
    {
	RelationDesc[] related;
	Vector         engines;

	engines = new Vector();
	related = _objDesc.getRelated();
	if ( related != null ) {
	    for ( int i = 0 ; i < related.length ; ++i ) {
		if ( related[ i ].getRelationType() == Relation.OneToOne ) {
		    engines.addElement( new SQLRelated( related[ i ], _objDesc,
							related[ i ].getParentField(), logWriter ) );
		}
	    }
	    _related = (SQLRelated[]) engines.toArray( new SQLRelated[ engines.size() ] );
	}
    }


    protected void buildCreateSql()
    {
	StringBuffer   sql;
	FieldDesc[]    descs;
	RelationDesc[] related;
	int            count;

	// Create statement to lookup primary key and determine
	// if object with same primary key already exists
	if ( _createRequiresPK ) {
	    sql = new StringBuffer( "SELECT 1 FROM " );
	    sql.append( _objDesc.getTableName() ).append( " WHERE " );
	    if ( _primKey.isPrimitive() ) {
		sql.append( _primKey.getSqlName() );
		sql.append( "=?" );
	    } else {
		descs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    if ( i > 0 )
			sql.append( " AND " );
		    sql.append( descs[ i ].getSqlName() );
		    sql.append( "=?" );
		}
	    }
	    sql.append( " FOR UPDATE" );
	    _pkLookup = sql.toString();
	}

	// Create statement to insert a new row into the table
	// using the specified primary key if one is required
	sql = new StringBuffer( "INSERT INTO " );
	sql.append( _objDesc.getTableName() ).append( " (" );
	if ( _createRequiresPK ) {
	    if ( _primKey.isPrimitive() ) {
		sql.append( _primKey.getSqlName() );
		count = 1;
	    } else {
		descs = _primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    if ( i > 0 )
			sql.append( ',' );
		    sql.append( descs[ i ].getSqlName() );
		}
		count = descs.length;
	    }
	} else {
	    count = 0;
	}

	descs = _objDesc.getFieldDescs();
	for ( int i = 0 ; i < descs.length ; ++i ) {
	    if ( count > 0 )
		sql.append( ',' );
	    sql.append( descs[ i ].getSqlName() );
	    ++count;
	}
	related = _objDesc.getRelated();
	if ( related != null ) {
	    for ( int i = 0 ; i < related.length ; ++i ) {
		if ( related[ i ].getRelationType() == Relation.ManyToOne &&
		     related[ i ].getForeignKey() != null ) {
		    if ( count > 0 )
			sql.append( ',' );
		    sql.append( related[ i ].getForeignKey() );
		    ++count;
		}
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
    }


    protected void buildRemoveSql()
    {
	StringBuffer sql;

	sql = new StringBuffer( "DELETE FROM " );
	sql.append( _objDesc.getTableName() ).append( " WHERE " );
	sql.append( buildWherePK() );
	_sqlRemove = sql.toString();
    }


    protected void buildStoreSql()
    {
	StringBuffer sql;
	FieldDesc[]  descs;
	int          count;
	RelationDesc[] related;

	sql = new StringBuffer( "UPDATE " );
	sql.append( _objDesc.getTableName() ).append( " SET " );
	descs = _objDesc.getFieldDescs();
	for ( int i = 0 ; i < descs.length ; ++i ) {
	    if ( i > 0 )
		sql.append( ',' );
	    sql.append( descs[ i ].getSqlName() );
	    sql.append( "=?" );
	}
	count = descs.length;;
	related = _objDesc.getRelated();
	if ( related != null ) {
	    for ( int i = 0 ; i < related.length ; ++i ) {
		if ( related[ i ].getRelationType() == Relation.ManyToOne &&
		     related[ i ].getForeignKey() != null ) {
		    if ( count > 0 )
			sql.append( ',' );
		    sql.append( related[ i ].getForeignKey() );
		    sql.append( "=?" );
		    ++count;
		}
	    }
	}

	sql.append( " WHERE " ).append( buildWherePK() );
	_sqlStore = sql.toString();
    }


    protected StringBuffer buildWherePK()
    {
	StringBuffer sql;
	FieldDesc[]  descs;
	int          i;

	sql = new StringBuffer();
	if ( _objDesc.getPrimaryKey().isPrimitive() ) {
	    sql.append( _objDesc.getPrimaryKey().getSqlName() );
	    sql.append( "=?" );
	} else {
	    descs = _objDesc.getPrimaryKey().getFieldDescs();
	    for ( i = 0 ; i < descs.length ; ++i ) {
		if ( i > 0 )
		    sql.append( " AND " );
		sql.append( descs[ i ].getSqlName() );
		sql.append( "=?" );
	    }
	}
	return sql;
    }


    protected void buildLoadSql()
    {
	Vector         loadFields;
	StringBuffer   sqlFields;
	StringBuffer   sqlFrom;
	StringBuffer   sqlJoin;

	loadFields = new Vector();
	sqlFields = new StringBuffer( "SELECT " );
	sqlFrom = new StringBuffer( " FROM " );
	sqlJoin = new StringBuffer( " WHERE " );
	addLoadSql( _objDesc, sqlFields, sqlFrom,
		    sqlJoin, loadFields, 0, false );

	_sqlLoad = sqlFields.append( sqlFrom ).append( sqlJoin ).toString();
	_loadFields = (FieldDesc[]) loadFields.toArray( new FieldDesc[ loadFields.size() ] );

	sqlFields = new StringBuffer( "SELECT " );
	sqlFrom = new StringBuffer( " FROM " );
	sqlJoin = new StringBuffer( "" );
	addLoadSql( _objDesc, sqlFields, sqlFrom,
		    sqlJoin, loadFields, 0, true );
	_sqlFinder = sqlFields.append( sqlFrom ).append( " WHERE " ).toString();
	_sqlFinderJoin = sqlJoin.toString();
    }


    private int addLoadSql( ObjectDesc objDesc, StringBuffer sqlFields,
			    StringBuffer sqlFrom, StringBuffer sqlJoin,
			    Vector loadFields, int count, boolean loadPk )
    {
	FieldDesc[]    descs;
	RelationDesc[] related;
	ObjectDesc     extend;
	PrimaryKeyDesc primKey;

	primKey = objDesc.getPrimaryKey();
	extend = objDesc.getExtends();
	related = objDesc.getRelated();

	if ( count != 0 )
            sqlFrom.append( ',' );
        sqlFrom.append( objDesc.getTableName() );

	if ( ! loadPk ) {
	    if ( primKey.isPrimitive() ) {
		sqlJoin.append( objDesc.getSqlName( primKey.getSqlName() ) );
		sqlJoin.append( "=?" );
	    } else {
		descs = primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    if ( i > 0 )
			sqlJoin.append( " AND " );
		    sqlJoin.append( objDesc.getSqlName( descs[ i ] ) );
		    sqlJoin.append( "=?" );
		}
	    }
	}

	if ( extend != null ) {
	    if ( primKey.isPrimitive() ) {
		sqlJoin.append( " AND " );
		sqlJoin.append( objDesc.getSqlName( primKey.getSqlName() ) );
		sqlJoin.append( "=" );
		sqlJoin.append( extend.getSqlName( primKey.getSqlName() ) );
	    } else {
		descs = primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    sqlJoin.append( " AND " );
		    sqlJoin.append( objDesc.getSqlName( descs[ i ] ) );
		    sqlJoin.append( "=" );
		    sqlJoin.append( extend.getSqlName( descs[ i ] ) );
		}
	    }
	}

	if ( objDesc instanceof RelationDesc )
	if ( objDesc instanceof RelationDesc &&
	     ( (RelationDesc) objDesc ).getRelationType() == Relation.OneToOne )
	    loadPk = false;
	
	if ( loadPk  ) {
	    if ( primKey.isPrimitive() ) {
		if ( count > 0 )
		    sqlFields.append( ',' );
		sqlFields.append( objDesc.getSqlName( primKey.getSqlName() ) );
		loadFields.addElement( objDesc.getPrimaryKeyField() );
		++count;
	    } else {
		descs = primKey.getFieldDescs();
		for ( int i = 0 ; i < descs.length ; ++i ) {
		    if ( count > 0 )
			sqlFields.append( ',' );
		    sqlFields.append( objDesc.getSqlName( descs[ i ] ) );
		    loadFields.addElement( descs[ i ] );
		    ++count;
		}
	    }
	}
	descs = objDesc.getFieldDescs();
	for ( int i = 0 ; i < descs.length ; ++i ) {
	    if ( count > 0 )
		sqlFields.append( ',' );
	    sqlFields.append( objDesc.getSqlName( descs[ i ] ) );
	    loadFields.addElement( descs[ i ] );
	    ++count;
	}

	related = objDesc.getRelated();
	if ( related != null ) {
	    for ( int i = 0 ; i < related.length ; ++i ) {
		sqlJoin.append( " AND " );
		if ( related[ i ].getForeignKeyField() == null )
		    sqlJoin.append( objDesc.getSqlName( related[ i ].getForeignKey() ) );
		else
		    sqlJoin.append( objDesc.getSqlName( related[ i ].getForeignKeyField().getSqlName() ) );
		sqlJoin.append( '=' );
		if ( related[ i ].getPrimaryKey().isPrimitive() )
		    sqlJoin.append( related[ i ].getSqlName( related[ i ].getPrimaryKey().getSqlName() ) );
		else
		    sqlJoin.append( related[ i ].getSqlName( related[ i ].getPrimaryKey().getFieldDescs()[ 0 ].getSqlName() ) );
	    }
	}

	if ( extend != null ) {
	    count = addLoadSql( extend, sqlFields, sqlFrom,
				sqlJoin, loadFields, count, true );
	}
	if ( related != null ) {
	    for ( int i = 0 ; i < related.length ; ++i )
		count = addLoadSql( related[ i ], sqlFields, sqlFrom,
				    sqlJoin, loadFields, count, true );
	}
	return count;
    }


}
