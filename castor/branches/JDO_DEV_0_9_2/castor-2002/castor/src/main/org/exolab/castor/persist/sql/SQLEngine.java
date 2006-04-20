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


import java.util.Vector;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.BitSet;
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
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.persist.AccessMode;
import org.exolab.castor.persist.Key;
import org.exolab.castor.persist.LogInterceptor;
import org.exolab.castor.persist.Entity;
import org.exolab.castor.persist.EntityInfo;
import org.exolab.castor.persist.EntityFieldInfo;
import org.exolab.castor.persist.Relation;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.session.OID;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.types.Complex;
import org.exolab.castor.persist.types.SQLTypes;
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
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 *
 * @version $Revision$ $Date$
 */
public final class SQLEngine implements Persistence, SQLConnector.ConnectorListener, SQLQueryKinds {

    private final static boolean ID_TYPE = true;


    private final static boolean FIELD_TYPE = false;


    private SQLQueryExecutor    _sqlLock;


    private SQLQueryExecutor    _sqlCreate;


    private SQLQueryExecutor    _sqlRemove;


    /**
     * Maps BitSets of "null" positions in the array of fields to SQLQueryExecutors
     */
    private HashMap             _sqlRemoveDirtyCheckMap;


    private SQLQueryExecutor    _sqlStore;


    /**
     * Maps BitSets of "null" positions in the array of fields to SQLQueryExecutors
     */
    private HashMap             _sqlStoreDirtyCheckMap;


    private SQLQueryExecutor    _sqlLoad;


    private SQLQueryExecutor    _sqlLoadLock;


    /**
     * Maps Relation.fieldInfo to SQLQueryExecutor
     */
    private HashMap             _sqlRelatedMap;


    /**
     * Maps Relation.fieldInfo to SQLQueryExecutor
     */
    private HashMap             _sqlRelatedLockMap;


    private SQLEntityInfo       _info;


    private QueryExpression     _sqlFinder;


    private BaseFactory  _factory;


    private String              _stampField;


    private LogInterceptor       _log;


    //private ClassMolder          _mold;


    private LockEngine           _lockEngine;


    private SQLConnector         _connector;

    /**
     * The HashMap maps Key to the set of LoadedRelations.
     */
    private HashMap _loadedRelations = new HashMap();


    SQLEngine( EntityInfo info, LockEngine lockEngine, LogInterceptor log,
            BaseFactory factory, SQLConnector connector, String stampField )
            throws PersistenceException {

        _info = SQLEntityInfo.getInstance(info);
        _lockEngine = lockEngine;
        _connector = connector;
        _stampField = stampField;
        _factory = factory;
        _log = log;

        /* TODO
        try {
            buildFinder();
        } catch ( QueryException except ) {
            except.printStackTrace();
            throw new MappingException( except );
        }
        */
    }

    /**
     * Used by {@link OQLQuery} to retrieve the class descriptor.
     * TODO: get rid of this method.
     */
    public JDOClassDescriptor getDescriptor()
    {
        return null;
    }


    /**
     * Used by ParseTreeWalker to quote names in WHERE clause
     */
    public String quoteName( String name )
    {
        return _factory.quoteName( name );
    }


    public PersistenceQuery createQuery( QueryExpression query, Class[] types, AccessMode accessMode )
        throws QueryException
    {
        String sql;

        //if ( accessMode == null )
        //    accessMode = _clsDesc.getAccessMode();
        sql = query.getStatement( accessMode == AccessMode.DbLocked);
        if ( _log != null )
            _log.queryStatement( sql );
        /* return new SQLQuery( this, sql, types ); TODO: SQLQuery implementation */
        return null;
    }


    public PersistenceQuery createCall( String spCall, Class[] types )
    {
        FieldDescriptor[] fields;
        String[] jdoFields0;
        String[] jdoFields;
        int[] sqlTypes0;
        int[] sqlTypes;
        int count;

        if ( _log != null )
            _log.queryStatement( spCall );

/* TODO
        fields = _clsDesc.getFields();
        jdoFields0 = new String[ fields.length + 1 ];
        sqlTypes0 = new int[ fields.length + 1 ];
        // the first field is the identity

        // | need some work here
        count = 1;
        jdoFields0[ 0 ] = _clsDesc.getIdentity().getFieldName();
        sqlTypes0[ 0 ] = ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLType()[0];
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDescriptor ) {
                jdoFields0[ count ] = ((JDOFieldDescriptor) fields[ i ]).getSQLName()[0];
                sqlTypes0[ count ] = ((JDOFieldDescriptor) fields[ i ]).getSQLType()[0];
                ++count;
            }
        }
        jdoFields = new String[ count ];
        sqlTypes = new int[ count ];
        System.arraycopy( jdoFields0, 0, jdoFields, 0, count );
        System.arraycopy( sqlTypes0, 0, sqlTypes, 0, count );
        return _factory.getCallQuery( spCall, types, _clsDesc.getJavaClass(), jdoFields, sqlTypes );
*/      return null;
    }

    public QueryExpression getQueryExpression() {
        return _factory.getQueryExpression();
    }

    public QueryExpression getFinder()
    {
        return (QueryExpression) _sqlFinder.clone();
    }


    /**
     * Creates a new object in persistence storage. Called for an
     * object that was created during the transaction when the identity
     * of that object is known. Creates a new record in persistence
     * storage. Must detect an attempt to create an object with the
     * same identity and must retain a lock on the object after creation.
     * If the identity is null, an identity might be created and returned
     * by this method.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param entity The entity to create
     * @throws DuplicateIdentityException An object with the same
     *   identity already exists in persistent storage
     * @throws PersistenceException A persistence error occured
     */
    public void create( Key key, Entity entity )
        throws PersistenceException {

        if (_sqlCreate == null) {
            _sqlCreate = SQLQueryBuilder.getCreateExecutor(_factory, _connector, _log, _info);
        }
        _sqlCreate.execute(key, _lockEngine, _connector.getConnection( key ), entity.identity, entity, null, null, null);
    }

    private BitSet getDirtyCheckNulls(Entity original) {
        BitSet dirtyCheckNulls = new BitSet();

        if (_factory.supportsSetNullInWhere()) {
            return dirtyCheckNulls;
        }
        for (int i = 0; i < _info.fieldInfo.length; i++) {
            if (original.values[_info.fieldOffset + i] == null &&
                    _info.fieldInfo[i] != null && _info.fieldInfo[i].info.dirtyCheck) {
                dirtyCheckNulls.set(i);
            }
        }
        return dirtyCheckNulls;
    }


    /**
     * Stores the object in persistent storage, given the object fields
     * and its identity. The object has been loaded before or has been
     * created through a call to {@link #create}. This method should
     * detect whether the object has been modified in persistent storage
     * since it was loaded. After this method returns all locks on the
     * object must be retained until the transaction has completed.
     * This method may return a new stamp to track further updates to
     * the object.
     * <p>
     * If the object was not retrieved for exclusive access, this
     * method will be asked to perform dirty checking prior to storing
     * the object. The <tt>original</tt> argument will contains the
     * object's original fields as retrieved in the transaction, and
     * <tt>stamp</tt> the object's stamp returned from a successful
     * call to {@link #load}. These arguments are null for objects
     * retrieved with an exclusive lock.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param entity The entity to store
     * @param original The original entity, or null
     * @throws ObjectModifiedException The object has been modified
     *  in persistence storage since it was last loaded
     * @throws ObjectDeletedException Indicates the object has been
     *  deleted from persistence storage
     * @throws PersistenceException A persistence error occured
     */
    public void store( Key key, Entity entity, Entity original )
        throws PersistenceException {

        BitSet dirtyCheckNulls;
        SQLQueryExecutor executor;

        if (original.locked) {
            // The object was loaded with a database lock, so dirty checking isn't needed.
            if (_sqlStore == null) {
                _sqlStore = SQLQueryBuilder.getStoreExecutor(_factory, _connector, _log,
                                                             _info, null);
            }
            executor = _sqlStore;
        } else {
            dirtyCheckNulls = getDirtyCheckNulls(original);
            if (_sqlStoreDirtyCheckMap == null) {
                _sqlStoreDirtyCheckMap = new HashMap();
            }
            executor = (SQLQueryExecutor) _sqlStoreDirtyCheckMap.get(dirtyCheckNulls);
            if (executor == null) {
                executor = SQLQueryBuilder.getStoreExecutor(_factory, _connector, _log,
                                                            _info, dirtyCheckNulls);
                _sqlStoreDirtyCheckMap.put(dirtyCheckNulls, executor);
            }
        }
        executor.execute(key, _lockEngine, _connector.getConnection(key), entity.identity, entity, original, null, null);
    }


    /**
     * Deletes the object from persistent storage, given the object'
     * identity. The object has been loaded before or has been created
     * through a call to {@link #create}. After this method returns all
     * locks on the object must be retained until the transaction has
     * completed.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param original The entity to delete
     * @throws PersistenceException A persistence error occured
     */
    public void delete( Key key, Entity original)
        throws PersistenceException {

        BitSet dirtyCheckNulls;
        SQLQueryExecutor executor;

        if (original.locked) {
            // The object was loaded with a database lock, so dirty checking isn't needed.
            if (_sqlRemove == null) {
                _sqlRemove = SQLQueryBuilder.getDeleteExecutor(_factory, _connector, _log,
                                                               _info, null);
            }
            executor = _sqlRemove;
        } else {
            dirtyCheckNulls = getDirtyCheckNulls(original);
            if (_sqlRemoveDirtyCheckMap == null) {
                _sqlRemoveDirtyCheckMap = new HashMap();
            }
            executor = (SQLQueryExecutor) _sqlRemoveDirtyCheckMap.get(dirtyCheckNulls);
            if (executor == null) {
                executor = SQLQueryBuilder.getDeleteExecutor(_factory, _connector, _log,
                                                             _info, dirtyCheckNulls);
                _sqlRemoveDirtyCheckMap.put(dirtyCheckNulls, executor);
            }
        }
        executor.execute(key, _lockEngine, _connector.getConnection(key), original.identity, null, original, null, null);
    }


    /**
     * Obtains a write lock on the object. This method is called in
     * order to lock the object and prevent concurrent access from
     * other transactions. The object is known to have been loaded
     * before either in this or another transaction. This method is
     * used to assure that updates or deletion of the object will
     * succeed when the transaction completes, without attempting to
     * reload the object.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param entity The entity to lock
     * @throws ObjectDeletedException Indicates the object has been
     *  deleted from persistence storage
     * @throws PersistenceException A persistence error occured
     */
    public void writeLock( Key key, Entity entity )
        throws PersistenceException {

        if (_sqlLock == null) {
            _sqlLock = SQLQueryBuilder.getLookupExecutor(_factory, _connector, _log, _info, true);
        }
        _sqlLock.execute(key, _lockEngine, _connector.getConnection(key), entity.identity, null, null, null, null);
    }


    /**
     * Loads the object from persistence storage. This method will load
     * the object fields from persistence storage based on the object's
     * identity. This method may return a stamp which can be used at a
     * later point to determine whether the copy of the object in
     * persistence storage is newer than the cached copy (see {@link
     * #store}). If <tt>lock</tt> is true the object must be
     * locked in persistence storage to prevent concurrent updates.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param entity The entity to load into
     * @param accessMode The access mode (null equals shared)
     * @return The object's stamp, or null
     * @throws ObjectNotFoundException The object was not found in
     *   persistent storage
     * @throws PersistenceException A persistence error occured
     */
    public void load( Key key, Entity entity, AccessMode accessMode )
            throws PersistenceException {
        SQLQueryExecutor executor;

        if (accessMode == AccessMode.DbLocked) {
            if (_sqlLoadLock == null) {
                _sqlLoadLock = SQLQueryBuilder.getSelectExecutor(_factory, _connector, _log, _info, true, null);
            }
            executor = _sqlLoadLock;
        } else {
            if (_sqlLoad == null) {
                _sqlLoad = SQLQueryBuilder.getSelectExecutor(_factory, _connector, _log, _info, false, null);
            }
            executor = _sqlLoad;
        }
        executor.execute(key, _lockEngine, _connector.getConnection(key), entity.identity, entity, null, null, null);
    }

    /**
     * Loads all the identities of entity in which the specified field match
     * the supplied value. Conceptually, the specified field is a foreign key
     * field; the supplied values is the value the foreign key.
     *
     * @param key The transaction context
     * @param conn An open connection
     * @param field The field on the "many" side of the relation
     * @param value The value of the field
     * @param entityIds The list of loaded identities that should be filled in the method
     * @param accessMode The access mode (null equals shared)
     * @throws PersistenceException A persistence error occured
     */
    public void loadRelated( Key key, Relation relation, AccessMode accessMode )
            throws PersistenceException {
        SQLQueryExecutor executor;
        SQLRelationInfo[] oneToManyPath;
        HashMap map;
        LoadedRelation lr = null;
        Set lrSet;
        String oneTable;
        Object identity;
        boolean found;
        List list;

        found = false;
        lrSet = (Set) _loadedRelations.get(key);
        if (lrSet == null) {
            lrSet = new HashSet();
            _loadedRelations.put(key, lrSet);
            _connector.addListener(key, this);
        } else {
            // if the given identity on "one" side of the current Relation appeared in the list of identities
            // on "many" side of one of the loaded relations, then we pre-fetch relations.
            oneTable = relation.fieldInfo.entityClass.entityClass;
            for (Iterator it = lrSet.iterator(); it.hasNext(); ) {
                lr = (LoadedRelation) it.next();
                if (lr.manyTable.equals(oneTable) && lr.list.contains(relation.identity)) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            oneToManyPath = new SQLRelationInfo[lr.oneToManyPath.length + 1];
            System.arraycopy(lr.oneToManyPath, 0, oneToManyPath, 0, lr.oneToManyPath.length);
            identity = lr.identity;
            list = new ArrayList();
        } else {
            oneToManyPath = new SQLRelationInfo[1];
            identity = relation.identity;
            list = relation.list;
        }
        oneToManyPath[oneToManyPath.length - 1] = SQLRelationInfo.getInstance(relation.fieldInfo);

        if (accessMode == AccessMode.DbLocked) {
            if (_sqlRelatedLockMap == null) {
                _sqlRelatedLockMap = new HashMap();
            }
            map = _sqlRelatedLockMap;
        } else {
            if (_sqlRelatedMap == null) {
                _sqlRelatedMap = new HashMap();
            }
            map = _sqlRelatedMap;
        }
        executor = (SQLQueryExecutor) map.get(relation.fieldInfo);
        if (executor == null) {
            executor = SQLQueryBuilder.getSelectExecutor(_factory, _connector, _log, _info, true, oneToManyPath);
            map.put(relation.fieldInfo, executor);
        }
        executor.execute(key, _lockEngine, _connector.getConnection(key), identity, null, null, relation, list);
        lrSet.remove(lr);
        lrSet.add(new LoadedRelation(oneToManyPath, identity, list));
    }

    /**
     * Implementation of SQLConnector.ConnectorListener
     */
    public void connectionPrepare( Key key ) {
        _loadedRelations.remove(key);
        _connector.removeListener(key, this);
    }

    /**
     * Implementation of SQLConnector.ConnectorListener
     */
    public void connectionRelease( Key key ) {
    }

/* TODO: redesign
    private void buildFinder( JDOClassDescriptor clsDesc ) throws QueryException {
        Vector          fields;
        QueryExpression find;

        fields = new Vector();
        find = _factory.getQueryExpression();
        //addLoadSql( expr, fields, false, true, true );

        //_fields = new FieldInfo[ fields.size() ];
        //fields.copyInto( _fields );

        // join all the extended table
        JDOClassDescriptor curDesc = clsDesc;
        JDOClassDescriptor baseDesc;
        String[] curIds = idnames;
        String[] baseIds;
        while ( curDesc.getExtends() != null ) {
            baseDesc = (JDOClassDescriptor) curDesc.getExtends();
            baseIds = new String[_ids.length];
            for ( int i=0; i<_ids.length; i++ ) {
                baseIds[i] = ((JDOFieldDescriptor) (baseDesc.getIdentities()[i])).getSQLName()[0];
            }
            find.addInnerJoin( curDesc.getTableName(), curIds, baseDesc.getTableName(), baseIds );
            curDesc = baseDesc;
            curIds = baseIds;
        }
        for ( int i=0; i<_ids.length; i++ ) {
            find.addColumn( _mapTo, idnames[i] );
        }

        // join all the related/depended table
        Vector joinTables = new Vector();
        for ( int i=0; i<_fields.length; i++ ) {
            if ( _fields[i].load ) {
                if ( _fields[i].joined ) {
                    int offset = 0;
                    String[] rightCol = _fields[i].joinFields;
                    String[] leftCol = new String[_ids.length-offset];
                    for ( int j=0; j<leftCol.length; j++ ) {
                        leftCol[j] = _ids[j+offset].name;
                    }
                    find.addOuterJoin( _mapTo, leftCol, _fields[i].tableName, rightCol );
                    joinTables.add( _fields[i].tableName );
                }

                for ( int j=0; j<_fields[i].columns.length; j++ ) {
                    find.addColumn( _fields[i].tableName, _fields[i].columns[j].name );
                }
            }
        }
        _sqlFinder = find;

    }
*/

    public String toString() {
        return _info.toString();
    }


/*
    static final class SQLQuery implements PersistenceQuery {


        private PreparedStatement _stmt;


        private ResultSet         _rs;


        private final SQLEngine _engine;


        private final Class[]   _types;


        private final Object[]  _values;


        private final String    _sql;


        private Object[]        _lastIdentity;


        private int[]           _identSqlType;


        private boolean         _resultSetDone;


        SQLQuery( SQLEngine engine, String sql, Class[] types )
        {
            _engine = engine;
            _types = types;
            _values = new Object[ _types.length ];
            _sql = sql;
            _identSqlType = new int[_engine._info.getIdentities().length];
            for (int i = 0; i < _identSqlType.length; i++) {
                _identSqlType[i] = ((JDOFieldDescriptor) _engine._clsDesc.getIdentities()[i]).getSQLType()[0];
            }
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
                throw new PersistenceException( Messages.format("persist.nested", except) + " while executing "+ _sql, except );
            }
        }


        public Object nextIdentity( Object identity )
                throws PersistenceException {

            Object[] oldIdentity;
            Object[] returnId;
            boolean empty = false;
            boolean newId = false;

            try {
                if ( _resultSetDone )
                    return null;

                // _lastIdentity is null in the first call to this method
                if ( _lastIdentity == null ) {
                    // the query is empty
                    if ( ! _rs.next() ) {
                        _resultSetDone = true;
                        return null;
                    }

                    _lastIdentity = new Object[_engine._ids.length];
                    returnId = new Object[_engine._ids.length];
                    empty = true;
                    for ( int i=0; i<_engine._ids.length; i++ ) {
                        _lastIdentity[i] = SQLTypes.getObject( _rs, 1+i, _identSqlType[i] );
                        returnId[i] = _engine.idToJava( i, _lastIdentity[i] );
                        if ( _lastIdentity[i] != null )
                            empty = false;
                    }
                    if ( empty ) {
                        return null;
                    } else {
                        switch (_engine._ids.length) {
                        case 1:
                            return returnId[0];
                        default:
                            return new Complex( returnId );
                        }
                    }
                }

                // convert the identity from java type into sql
                // type for comparsion
                oldIdentity =  new Object[_engine._ids.length];
                returnId = new Object[_engine._ids.length];

                // determine if the id in the resultSet is a new one
                if ( _engine._ids.length > 1 ) {
                    Complex id = (Complex) identity;
                    for ( int i=0; i < _engine._ids.length; i++ ) {
                        returnId[i] = id.get(i);
                        oldIdentity[i] = _engine.idToSQL( i, id.get(i) );
                        if ( !oldIdentity[i].equals( _lastIdentity[i] ) ) {
                            newId = true;
                            _lastIdentity[i] = oldIdentity[i];
                            returnId[i] = _engine.idToJava( i, _lastIdentity[i] );
                        }
                    }
                } else {
                    returnId[0] = identity;
                    oldIdentity[0] = _engine.idToSQL( 0, identity );
                    if ( !oldIdentity[0].equals( _lastIdentity[0] ) ) {
                        newId = true;
                        _lastIdentity[0] = oldIdentity[0];
                        returnId[0] = _engine.idToJava( 0, _lastIdentity[0] );
                    }
                }

                // skip to the next id
                while ( !newId && !_resultSetDone ) {
                    if ( ! _rs.next() ) {
                        _lastIdentity = null;
                        _resultSetDone = true;
                        return null;
                    }

                    empty = true;
                    for ( int i=0; i<_engine._ids.length; i++ ) {
                        Object o = SQLTypes.getObject( _rs, 1+i, _identSqlType[i] );
                        if ( !oldIdentity[i].equals( o ) ) {
                            newId = true;
                            _lastIdentity[i] = o;
                            returnId[i] = _engine.idToJava( i, _lastIdentity[i] );
                        }
                        if ( o != null )
                            empty = false;
                    }
                }
                    if ( empty )
                    return null;
                else if ( _engine._ids.length > 1 )
                    return new Complex( returnId );
                else
                    return returnId[0];

            } catch ( SQLException except ) {
                _lastIdentity = null;
                throw new PersistenceException( Messages.format("persist.nested", except), except );
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


        public Object fetch(Object[] fields,Object identity)
                throws ObjectNotFoundException, PersistenceException {

            int    count;
            Object stamp = null;
            boolean newId = false;
            Object[] sqlIdentity = new Object[_engine._ids.length];

            try {
                // Load all the fields of the object including one-one relations
                Object[] temp = new Object[10];  // bad pratice, assume complex field smaller than 10

                count = _engine._ids.length;

                // load all the fields
                for ( int i = 0 ; i < _engine._fields.length ; ++i  ) {
                    if ( !_engine._fields[i].load )
                        continue;

                    if ( !_engine._fields[i].multi ) {
                        boolean notNull = false;
                        if ( _engine._fields[i].columns.length == 1 ) {
                            Object value = _engine.toJava( i, 0, SQLTypes.getObject( _rs, count++, _engine._fields[i].columns[0].sqlType ) );
                            if ( !_rs.wasNull() )
                                fields[i] = value;
                            else
                                fields[i] = null;
                        } else {
                            Complex inner = (Complex) fields[i];
                        for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                                Object value = _engine.toJava( i, j, SQLTypes.getObject( _rs, count++, _engine._fields[i].columns[j].sqlType ) );
                                if ( !_rs.wasNull() ) {
                                temp[j] = value ;
                                    notNull = true;
                                } else {
                                    temp[j] = null;
                                }
                            }
                            fields[i] = null;
                            if ( notNull )
                                fields[i] = new Complex( _engine._fields[i].columns.length, temp );
                        }
                    } else {
                        ArrayList res = new ArrayList();
                        boolean notNull = false;
                        for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                            Object value = _engine.toJava( i, j, SQLTypes.getObject( _rs, count, _engine._fields[i].columns[j].sqlType ) );
                            if ( !_rs.wasNull() ) {
                                temp[j] = value;
                                notNull = true;
                            } else {
                                temp[j] = null;
                            }
                            count++;
                        }
                        if ( notNull ) {
                            if ( _engine._fields[i].columns.length == 1 )
                                res.add( temp[0] );
                            else
                                res.add( new Complex( _engine._fields[i].columns.length, temp ) );
                        }
                        fields[i] = res;
                    }
                }

                // add other one-to-many fields
                if ( !_resultSetDone && _rs.next() ) {
                    count = 1;
                    if ( _lastIdentity == null )
                        _lastIdentity = new Object[_engine._ids.length];

                    // check if the table row consists data of the interested identity
                    for ( int i=0; i<_lastIdentity.length; i++ ) {
                        Object o = SQLTypes.getObject( _rs, count, _identSqlType[i] );
                        if ( !o.equals( sqlIdentity[i] ) ) {
                            newId = true;
                            _lastIdentity[i] = o;
                        }
                        count++;
                    }

                    // move forward in the ResultSet, until we see
                    // another identity
                    while ( !newId ) {
                        for ( int i = 0 ; i < _engine._fields.length ; ++i  ) {
                            if ( !_engine._fields[i].load )
                                continue;

                            if ( _engine._fields[i].multi ) {
                                ArrayList res = (ArrayList)fields[i];
                                boolean notNull = false;
                                for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                                    Object value = _engine.toJava( i, j, SQLTypes.getObject( _rs, count, _engine._fields[i].columns[j].sqlType ) );
                                    if ( !_rs.wasNull() ) {
                                        temp[j] = value ;
                                        notNull = true;
                                    } else {
                                       temp[j] = null;
                                }
                                count++;
                                }
                                if ( notNull ) {
                                    if ( _engine._fields[i].columns.length == 1 ) {
                                        if ( !res.contains( temp[0] ) )
                                            res.add( temp[0] );
                                    } else {
                                        Complex com = new Complex( _engine._fields[i].columns.length, temp );
                                        if ( !res.contains( com ) )
                                            res.add( new Complex( _engine._fields[i].columns.length, temp ) );
                                    }
                                }
                            } else {
                                // non-multi fields have to be done one only
                                // so, skip to next
                                count += _engine._fields[i].columns.length;
                            }
                        }

                        if ( _rs.next() ) {
                            // check if the table row consists data of the interested identity
                            for ( int i=0; i<_lastIdentity.length; i++ ) {
                                Object o = SQLTypes.getObject( _rs, count, _identSqlType[i] );
                                if ( !o.equals( sqlIdentity[i] ) ) {
                                    newId = true;
                                    _lastIdentity[i] = o;
                                }
                                count++;
                            }
                        } else {
                            _resultSetDone = true;
                            _lastIdentity = null;
                        }
                    }
                } else {
                    _lastIdentity = null;
                    _resultSetDone = true;
                }
            } catch ( SQLException except ) {
                throw new PersistenceException( Messages.format("persist.nested", except), except );
            }

            return stamp;
        }
    }
*/
}
