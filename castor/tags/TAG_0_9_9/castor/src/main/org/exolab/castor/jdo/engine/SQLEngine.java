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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.jdo.engine.ConnectionFactory;
import org.castor.jdo.engine.CounterRef;
import org.castor.jdo.engine.DatabaseRegistry;

import org.castor.persist.ProposedObject;
import org.exolab.castor.jdo.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.persist.spi.*;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.SqlBindParser;

import java.sql.*;
import java.util.*;

/**
 * The SQL engine performs persistence of one object type against one
 * SQL database. It can only persist simple objects and extended
 * relationships. An SQL engine is created for each object type
 * represented by a database. When persisting, it requires a physical
 * connection that maps to the SQL database and the transaction
 * running on that database
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 *
 * @version $Revision$ $Date$
 */
public final class SQLEngine implements Persistence {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance( SQLEngine.class );

    /**
     * SQL statements for PK lookup
     */
    private String              _pkLookup;

    /**
     * SQL statements for object creation
     */
    private String              _sqlCreate;

    /**
     * SQL statements for object removal
     */
    private String              _sqlRemove;

    /**
     * SQL statements for updating object instance
     */
    private String              _sqlStore;

    private String              _sqlStoreDirty;

    /**
     * SQL statements for loading object instance
     */
    private String              _sqlLoad;

    private String              _sqlLoadLock;

    private FieldInfo[]         _fields;

    private ColumnInfo[]         _ids;

    private SQLEngine           _extends;

    private QueryExpression     _sqlFinder;

    private PersistenceFactory  _factory;

    private String              log;

    private String              _type;

    private String              _mapTo;

    private String              _extTable;

    private JDOClassDescriptor   _clsDesc;

    private KeyGenerator         _keyGen;

    /**
     * Indicates whether there is a field to persist at all; in the case of 
     * EXTEND relationships where no additional attributes are defined in the 
     * extending class, this might NOT be the case; in general, a class has to have
     * at least one field that is to be persisted.
     */
    private boolean hasFieldsToPersist = false;

    /**
     * Number of ClassDescriptor that extend this one.
     */
    private int _numberOfExtendLevels;

    /**
     * Collection of all the ClassDescriptor that extend this one (closure)
     */
    private Collection _extendingClassDescriptors;

    SQLEngine( JDOClassDescriptor clsDesc, PersistenceFactory factory, String stampField )
        throws MappingException {

        _clsDesc = clsDesc;
        _factory = factory;
        _keyGen = null;
        _type = clsDesc.getJavaClass().getName();
        _mapTo = clsDesc.getTableName();
        
        if ( _clsDesc.getExtends() == null ) {
            KeyGeneratorDescriptor keyGenDesc = clsDesc.getKeyGeneratorDescriptor();
            if ( keyGenDesc != null ) {
                int[] tempType = ( (JDOFieldDescriptor) _clsDesc.getIdentity() ).getSQLType();
                _keyGen = keyGenDesc.getKeyGeneratorRegistry().getKeyGenerator(
                        _factory, keyGenDesc, tempType==null? 0: tempType[0] );

                // Does the key generator support the sql type specified in the mapping?
                _keyGen.supportsSqlType( tempType[0] );
            }
        }

        // construct field and id info
        Vector idsInfo = new Vector();
        Vector fieldsInfo = new Vector();

        /*
         * Implementation Note:
         * Extends and Depends has some special mutual exclusive
         * properties, which implementator should aware of.
         *
         * A Depended class may depends on another depended class
         * A class should either extends or depends on other class
         * A class should not depend on extending class.
         *  because, it is the same as depends on the base class
         * A class may be depended by zero or more classes
         * A class may be extended by zero or more classes
         * A class may extends only zero or one class
         * A class may depends only zero or one class
         * A class may depend on extended class
         * A class may extend a dependent class.
         * A class may extend a depended class.
         * No loop or circle should exist
         *
         * In other word,
         *
         */
        // then, we put depended class ids in the back
        JDOClassDescriptor base = clsDesc;

        // make sure there is no forbidded cases
        /*
        while ( base.getDepends() != null ) {
            if ( base.getExtends() != null )
                throw new MappingException("Class should not both depends on and extended other classes");

            base = (JDOClassDescriptor)base.getDepends();
            if ( base.getExtends() != null )
                throw new MappingException("Class should not depends on an extended class");
            // do we need to add loop detection?
        }*/

        // walk until the base class which this class extends
        base = clsDesc;
        Stack stack = new Stack();
        stack.push( base );
        while ( base.getExtends() != null ) {
            //if ( base.getDepends() != null )
            //    throw new MappingException("Class should not both depends on and extended other classes");
            base = (JDOClassDescriptor) base.getExtends();
            stack.push( base );
            // do we need to add loop detection?
        }
        if ( base != clsDesc ) {
            _extTable = base.getTableName();
        }

        // now base is either the base of extended class, or
        // clsDesc
        // we always put the original id info in front
        // [oleg] except for SQL name, it may differ.
        FieldDescriptor[] baseIdDescriptors = base.getIdentities();
        FieldDescriptor[] idDescriptors = clsDesc.getIdentities();

        for ( int i=0; i < baseIdDescriptors.length; i++ ) {
            if ( baseIdDescriptors[i] instanceof JDOFieldDescriptor ) {
                String name = baseIdDescriptors[i].getFieldName();
                String[] sqlName = ((JDOFieldDescriptor) baseIdDescriptors[i]).getSQLName();
                int[] sqlType = ((JDOFieldDescriptor) baseIdDescriptors[i]).getSQLType();
                FieldHandlerImpl fh = (FieldHandlerImpl) baseIdDescriptors[i].getHandler();

                // The extending class may have other SQL names for identity fields
                for (int j = 0; j < idDescriptors.length; j++) {
                    if (name.equals(idDescriptors[j].getFieldName()) &&
                            (idDescriptors[j] instanceof JDOFieldDescriptor)) {
                        sqlName = ((JDOFieldDescriptor) idDescriptors[j]).getSQLName();
                        break;
                    }
                }
                idsInfo.add( new ColumnInfo( sqlName[0], sqlType[0], fh.getConvertTo(), fh.getConvertFrom(), fh.getConvertParam() ) );
            } else
                throw new MappingException("Except JDOFieldDescriptor");
        }


        // if class or base class depend on other class,
        // depended class ids will be part of this ids and
        // will be added in the back. We don't need to take
        // care depended class which is depends on other class.
        // ClassMolder will take care of it.
        /*
        idDescriptors = null;
        if ( clsDesc.getDepends()!= null ) {
            idDescriptors = ((ClassDescriptorImpl)jdoBase.getDepends()).getIdentities();
            for ( int i=0; i<idDescriptors.length; i++ ) {
                if ( idDescriptors[i] instanceof JDOFieldDescriptor ) {
                    String name = ((JDOFieldDescriptor)idDescriptors[i]).getSQLName();
                    int type = ((JDOFieldDescriptor)idDescriptors[i]).getSQLType();
                    FieldHandlerImpl fh = (FieldHandlerImpl)idDescriptors[i].getHandler();
                    idsInfo.add( new ColumnInfo( name, type, fh.getConvertTo(), fh.getConvertFrom(), fh.getConvertParam() ) );
                } else
                    throw new MappingException("Except JDOFieldDescriptor");
            }
        } */

        // then do the fields
        boolean extendField = true;
        while ( !stack.empty() ) {
            base = (JDOClassDescriptor)stack.pop();
            FieldDescriptor[] fieldDescriptors = base.getFields();
            for ( int i=0; i<fieldDescriptors.length; i++ ) {
                // fieldDescriptors[i] is persistent in db if it is
                // a JDOFieldDescriptor or has a ClassDescriptor and is not 
            	// transient
                if (((fieldDescriptors[i] instanceof JDOFieldDescriptor) ||
                    (fieldDescriptors[i].getClassDescriptor() != null)) &&
                    !fieldDescriptors[i].isTransient())  {
                    if ( stack.empty() ) {
                        fieldsInfo.add( new FieldInfo( clsDesc, fieldDescriptors[i], clsDesc.getTableName(), !extendField ) );
                    } else {
                        fieldsInfo.add( new FieldInfo( clsDesc, fieldDescriptors[i], base.getTableName(), extendField ) );
                    }
                }
            }
        }

        _ids = new ColumnInfo[idsInfo.size()];
        idsInfo.copyInto( _ids );

        _fields = new FieldInfo[fieldsInfo.size()];
        fieldsInfo.copyInto( _fields );

        // obtain the number of ClassDescriptor that extend this one.
        _numberOfExtendLevels = numberOfExtendingClassDescriptors(getDescriptor());
        _extendingClassDescriptors = getDescriptor().getExtendedBy();

        // iterate through all fields to check whether there is a field
        // to persist at all; in the case of extend relationships where no 
        // additional attributes are defined in the extending class, this 
        // might NOT be the case
        for (int i = 0 ; i < _fields.length ; ++i ) {
            if (_fields[i].store) {
                hasFieldsToPersist = true;
                break;
            }
        }

        if(_log.isDebugEnabled()) {
            _log.debug ("hasFieldsToPersist = " + hasFieldsToPersist);
        }
        
        try {
            buildSqlPKLookup();
            // _log.debug ("pkLookup = " + _pkLookup);
            buildSqlCreate();
            // _log.debug ("sqlCreate = " + _sqlCreate);
            buildSqlRemove();
            // _log.debug ("sqlRemove = " + _sqlRemove);
            buildFinder( clsDesc );
            // _log.debug ("sqlLoad = " + _sqlLoad);
            // _log.debug ("sqlLoadLock = " + _sqlLoadLock);
            buildSqlUpdate();
            // _log.debug ("sqlStore = " + _sqlStore);
            // _log.debug ("sqlStoreDirty = " + _sqlStoreDirty);
        } catch ( QueryException except ) {
            _log.warn("Problem building SQL", except);
            throw new MappingException( except );
        }
    }

    public Persistence.ColumnInfo[] getColumnInfoForIdentities() {
    	return _ids;
    }
    
    public Persistence.FieldInfo[] getInfo() {
        return _fields;
    }

    /**
     * Mutator method for setting extends SQLEngine
     * @param engine
     */
    public void setExtends( SQLEngine engine ) {
        _extends = engine;
    }

    private Connection getSeparateConnection(Database database) throws PersistenceException  {
        ConnectionFactory factory = null;
        try {
            factory = DatabaseRegistry.getConnectionFactory(database.getDatabaseName());
        } catch (MappingException e) {
            throw new PersistenceException(Messages.message("persist.cannotCreateSeparateConn"), e);
        }
        
        try {
            Connection conn = factory.createConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new PersistenceException(Messages.message("persist.cannotCreateSeparateConn"), e);
        }
    }

    private void closeSeparateConnection(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            _log.error(e.getMessage(), e);
        }
    }
    
    /**
     * Used by {@link org.exolab.castor.jdo.OQLQuery} to retrieve the class descriptor.
     * @return the JDO class descriptor.
     */
    public JDOClassDescriptor getDescriptor()
    {
        return _clsDesc;
    }


    /**
     * Used by ParseTreeWalker to quote names in WHERE clause
     * @param name A name to be quoted 
     * @return a quoted name.
     */
    public String quoteName( String name )
    {
        return _factory.quoteName( name );
    }


    public PersistenceQuery createQuery( QueryExpression query, Class[] types, AccessMode accessMode )
        throws QueryException
    {
        String sql;

        if ( accessMode == null )
            accessMode = _clsDesc.getAccessMode();
        sql = query.getStatement( accessMode == AccessMode.DbLocked);
        
        if(_log.isDebugEnabled()) {
            _log.debug( Messages.format( "jdo.createSql", sql ) );
        }
        
        return new SQLQuery( this, sql, types );
    }


    public PersistenceQuery createCall( String spCall, Class[] types )
    {
        FieldDescriptor[] fields;
        String[] jdoFields0;
        String[] jdoFields;
        String sql;
        int[] sqlTypes0;
        int[] sqlTypes;
        int count;

        // changes for the SQL Direct interface begins here
        if (spCall.startsWith("SQL")) {
            sql = spCall.substring(4);
        	
            if (_log.isDebugEnabled()) {
                _log.debug (Messages.format ("jdo.directSQL", sql));
            }
            
            return new SQLQuery( this, sql, types );
		}

        if (_log.isDebugEnabled()) {
            _log.debug (Messages.format ("jdo.spCall", spCall));
        }

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

        return ((BaseFactory) _factory).getCallQuery( spCall, types,_clsDesc.getJavaClass(), jdoFields, sqlTypes );
    }

    public QueryExpression getQueryExpression() {
        return _factory.getQueryExpression();
    }

    public QueryExpression getFinder()
    {
        return (QueryExpression) _sqlFinder.clone();
    }

    private Object idToSQL(int index, Object object) {
        if (object == null || _ids[index].convertFrom == null)
            return object;
        return _ids[index].convertFrom.convert(object, _ids[index].convertParam);
    }

    private Object toSQL(int field, int column, Object object) {
        ColumnInfo col = _fields[field].columns[column];
        if (object == null || col.convertFrom == null)
            return object;
        return col.convertFrom.convert(object, col.convertParam);
    }

    private Object idToJava(int index, Object object) {
        if (object == null || _ids[index].convertTo == null)
            return object;
        return _ids[index].convertTo.convert(object, _ids[index].convertParam);
    }

    private Object toJava(int field, int column, Object object) {
        ColumnInfo col = _fields[field].columns[column];
        if (object == null || col.convertTo == null)
            return object;
        return col.convertTo.convert(object, col.convertParam);
    }

    /**
     * Use the specified keygenerator to gengerate a key for this
     * row of object.
     *
     * Result key will be in java type.
     * @param database Database instance
     * @param conn JDBC Connection instance
     * @param stmt JDBC Statement instance
     * @return The generated key
     * @throws PersistenceException If no key can be generated 
     */
    private Object generateKey(Database database, Object conn, PreparedStatement stmt)
    throws PersistenceException {
        Object identity;
        Connection connection;
        Properties prop = null;

        // TODO [SMH]: Change KeyGenerator.isInSameConnection to KeyGenerator.useSeparateConnection?
        // TODO [SMH]: Move "if (_keyGen.isInSameConnection() == false)" out of SQLEngine and into key-generator?
        if (_keyGen.isInSameConnection() == false) {
            connection = getSeparateConnection(database);
        } else {
            connection = (Connection) conn;
        }

        if (stmt != null) {
            prop = new Properties();
            prop.put("insertStatement", stmt);
        }

        try {
            synchronized (connection) {
                identity = _keyGen.generateKey(connection, _clsDesc.getTableName(), _ids[0].name, prop);
            }

            // TODO [SMH]: Move "if (identity == null)" into keygenerator.
            if (identity == null) {
                throw new PersistenceException(
                    Messages.format("persist.noIdentity", _clsDesc.getJavaClass().getName()));
            }

            return idToJava(0, identity);
        } finally {
            if (_keyGen.isInSameConnection() == false) {
                closeSeparateConnection(connection);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.spi.Persistence#create(org.exolab.castor.jdo.Database, java.lang.Object, java.lang.Object[], java.lang.Object)
     */
    public Object create(final Database database, final Object conn, final Object[] fields, Object identity)
    throws DuplicateIdentityException, PersistenceException {

        PreparedStatement stmt = null;
        int               count;

        if ( _extends == null && _keyGen == null && identity == null )
            throw new PersistenceException( Messages.format("persist.noIdentity", _clsDesc.getJavaClass().getName()) );

        try {
            // Must create record in the parent table first.
            // All other dependents are created afterwards.
            if ( _extends != null ) {
                // | quick and very dirty hack to try to make multiple class on the same table work
                if ( !_extends._mapTo.equals( _mapTo ) )
                    identity = _extends.create( database, conn, fields, identity );
            }

            // Generate key before INSERT
            else if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT )
                identity = generateKey( database, conn, null );   // genKey return identity in JDO type


            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.DURING_INSERT )
                stmt = ( (Connection) conn ).prepareCall( _sqlCreate );
            else
                stmt = ( (Connection) conn ).prepareStatement( _sqlCreate );
             
            if(_log.isDebugEnabled()){
//                 _log.debug( Messages.format( "jdo.creating", _clsDesc.getJavaClass().getName(), _sqlCreate) );
                _log.debug( Messages.format( "jdo.creating", _clsDesc.getJavaClass().getName(), stmt.toString()) );
            }
            
            // Must remember that SQL column index is base one
            count = 1;
            if ( _keyGen == null || _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT ) {
                if ( _ids.length > 1 && !(identity instanceof Complex) )
                    throw new PersistenceException( "Multiple identities expected!" );

                if ( identity instanceof Complex ) {
                    Complex id = (Complex) identity;
                    if ( id.size() != _ids.length || _ids.length <= 1 )
                        throw new PersistenceException( "Size of complex field mismatched!");

                    for ( int i=0; i<_ids.length; i++ )
                        stmt.setObject( count++, idToSQL( i, id.get(i) ) );

                } else {
                    if ( _ids.length != 1 )
                        throw new PersistenceException( "Complex field expected!" );

                    stmt.setObject( count++, idToSQL( 0, identity ) );
                }
            }

            if(_log.isDebugEnabled()){
             _log.debug( Messages.format( "jdo.creating", _clsDesc.getJavaClass().getName(), stmt.toString()) );
            }

            bindFields(fields, stmt, count);

            if(_log.isDebugEnabled()){
                _log.debug( Messages.format( "jdo.creating", _clsDesc.getJavaClass().getName(), stmt.toString()) );
            }

            // Generate key during INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.DURING_INSERT ) {
                CallableStatement cstmt = (CallableStatement) stmt;
                int sqlType;

                sqlType = _ids[0].sqlType;
                cstmt.registerOutParameter( count, sqlType );
                
                // [WG]: TODO: Verify that this really works !!!
                if (_log.isDebugEnabled()) {
                 	  _log.debug (Messages.format ("jdo.creating.bound", _clsDesc.getJavaClass().getName(), cstmt));
                }
                
                cstmt.execute();

                // First skip all results "for maximum portability"
                // as proposed in CallableStatement javadocs.
                while ( cstmt.getMoreResults() || cstmt.getUpdateCount() != -1 ) {
                	// no code to execute
                }

                // Identity is returned in the last parameter
                // Workaround: for INTEGER type in Oracle getObject returns BigDecimal
                if ( sqlType == java.sql.Types.INTEGER )
                    identity = new Integer( cstmt.getInt( count ) );
                else
                    identity = cstmt.getObject( count );
                identity = idToJava( 0, identity );
            } else {
                if(_log.isDebugEnabled()){
                    _log.debug( Messages.format( "jdo.creating", _clsDesc.getJavaClass().getName(), stmt.toString()) );
                }
            	stmt.executeUpdate();
            }

            stmt.close();

            // Generate key after INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.AFTER_INSERT ) {
                identity = generateKey( database, conn, stmt );
            }

            return identity;

        } catch (SQLException except) {
        	if (_log.isInfoEnabled()) {
        		_log.info( Messages.format( "jdo.storeFatal",  _type,  _sqlCreate ), except );
        	}

            // [oleg] Check for duplicate key based on X/Open error code
            // Bad way: all validation exceptions are reported as DuplicateKey
            //if ( except.getSQLState() != null &&
            //     except.getSQLState().startsWith( "23" ) )
            //    throw new DuplicateIdentityException( _clsDesc.getJavaClass(), identity );

            // Good way: let PersistenceFactory try to determine
            Boolean isDupKey;

            isDupKey = _factory.isDuplicateKeyException( except );
            if ( Boolean.TRUE.equals( isDupKey ) ) {
                throw new DuplicateIdentityException( Messages.format("persist.duplicateIdentity", _clsDesc.getJavaClass().getName(), identity ) );
            } else if ( Boolean.FALSE.equals( isDupKey ) ) {
                throw new PersistenceException( Messages.format("persist.nested", except), except );
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

                if(_log.isDebugEnabled()) {
                    _log.debug( Messages.format( "jdo.duplicateKeyCheck", _pkLookup ) );
                }
                
                // bind the identity to the preparedStatement
                count = 1;
                if ( identity instanceof Complex ) {
                    Complex id = (Complex) identity;
                    if ( id.size() != _ids.length || _ids.length <= 1 )
                        throw new PersistenceException( "Size of complex field mismatched!");

                    for ( int i=0; i<_ids.length; i++ )
                        stmt.setObject( count++, idToSQL( i, id.get(i) ) );

                } else {
                    if ( _ids.length != 1 )
                        throw new PersistenceException( "Complex field expected!" );

                    stmt.setObject( count++, idToSQL( 0, identity ) );
                }

                if ( stmt.executeQuery().next() ) {
                    stmt.close();
                    throw new DuplicateIdentityException( Messages.format("persist.duplicateIdentity", _clsDesc.getJavaClass().getName(), identity ) );
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
            } catch ( SQLException except2 ) {
                _log.warn("Problem closing JDBC statement", except2);
            }
            throw new PersistenceException( Messages.format("persist.nested", except), except );
        }
    }

	/**
	 * Bind non-identity fields to prepared statement.
	 * @param fields Field to bind.
	 * @param stmt PreparedStatement instance.
	 * @param count Field counter
	 * @throws SQLException If the fields cannot be bound successfully.
	 * @throws PersistenceException
	 */
	private void bindFields(final Object[] fields, final PreparedStatement stmt, int count) 
	throws SQLException, PersistenceException {
		for ( int i = 0 ; i < _fields.length ; ++i ) {
		    if ( _fields[ i ].store ) {
		        if ( fields[i] == null ) {
		            for ( int j=0; j < _fields[i].columns.length; j++ )
		                stmt.setNull( count++, _fields[i].columns[j].sqlType );

		        } else if ( fields[i] instanceof Complex ) {
		            Complex complex = (Complex)fields[i];
		            if ( complex.size() != _fields[i].columns.length )
		                throw new PersistenceException( "Size of complex field mismatch!" );

		            for ( int j=0; j<_fields[i].columns.length; j++ ) {
		                Object value = ( complex == null ? null : complex.get(j) );
		                SQLTypes.setObject( stmt, count++, toSQL( i, j, value), _fields[i].columns[j].sqlType );
		            }
		        } else {
		            if ( _fields[i].columns.length != 1 )
		                throw new PersistenceException( "Complex field expected! ");

		            SQLTypes.setObject( stmt, count++, toSQL( i, 0, fields[i]), _fields[i].columns[0].sqlType );
		        }
		    }
		}
	}


    /**
     * if isNull, replace next "=?" with " IS NULL",
     * otherwise skip next "=?",
     * move "pos" to the left.
     * @param isNull True if =? should be replaced with 'IS NULL'
     * @param sb StringBUffer holding the SQL statement to be modified 
     * @param pos The current position (where to apply the replacement).
     * @return The next position.
     */
    private int nextParameter(boolean isNull, StringBuffer sb, int pos) {
        for ( ; pos > 0; pos--) {
            if (sb.charAt(pos - 1) == '=' && sb.charAt(pos) == '?') {
                break;
            }
        }
        if (pos > 0) {
            pos--;
            if (isNull) {
                sb.delete(pos, pos + 2);
                sb.insert(pos, " IS NULL");
            }
        }
        return pos;
    }

    /**
     * If the RDBMS doesn't support setNull for "WHERE fld=?" and requires "WHERE fld IS NULL",
     * we need to modify the statement.
     */
    private String getStoreStatement( Object[] original ) throws PersistenceException {
        StringBuffer sb = null;
        int pos = 0;

        if (original == null) {
            return _sqlStore;
        }
        if (((BaseFactory) _factory).supportsSetNullInWhere()) {
            return _sqlStoreDirty;
        }
        pos = _sqlStoreDirty.length() - 1;
        sb = new StringBuffer(pos * 4);
        sb.append(_sqlStoreDirty);
        for (int i = _fields.length - 1; i >= 0; i--) {
            if (_fields[i].store && _fields[i].dirtyCheck) {
                if (original[i] == null) {
                    for (int j = _fields[i].columns.length - 1; j >= 0; j--) {
                        pos = nextParameter(true, sb, pos);
                    }
                } else if ( original[i] instanceof Complex ) {
                    Complex complex = (Complex) original[i];
                    if ( complex.size() != _fields[i].columns.length )
                        throw new PersistenceException( "Size of complex field mismatch!" );

                    for (int j = _fields[i].columns.length - 1; j >= 0; j--) {
                        pos = nextParameter((complex.get(j) == null), sb, pos);
                    }
                } else {
                    if (_fields[i].columns.length != 1)
                        throw new PersistenceException( "Complex field expected! ");

                    pos = nextParameter(false, sb, pos);
                }
            }
        }
        return sb.toString();
    }

    public Object store( Object conn, Object[] fields, Object identity,
                         Object[] original, Object stamp )
        throws ObjectModifiedException, ObjectDeletedException, PersistenceException {

        PreparedStatement stmt = null;
        int               count;
        String            storeStatement = null;

        // Must store record in parent table first.
        // All other dependents are stored independently.
        if ( _extends != null )
            // | quick and very dirty hack to try to make multiple class on the same table work
            if ( !_extends._mapTo.equals( _mapTo ) )
                _extends.store( conn, fields, identity, original, stamp );

        // Only build and execute an UPDATE statement if the class to be updated has 
        // fields to persist.
        if (hasFieldsToPersist) {
            try {
                
                storeStatement = getStoreStatement( original );
                stmt = ( (Connection) conn ).prepareStatement( storeStatement );
                
                if(_log.isDebugEnabled()){
                    _log.debug( Messages.format( "jdo.storing", _clsDesc.getJavaClass().getName(), stmt.toString()) );
                }
                
                count = 1;
                
                // bind fields of the row to be stored into the preparedStatement
                for ( int i = 0 ; i < _fields.length ; ++i ) {
                    if ( _fields[ i ].store ) {
                        if ( fields[i] == null ) {
                            for ( int j=0; j < _fields[i].columns.length; j++ )
                                stmt.setNull( count++, _fields[i].columns[j].sqlType );
                            
                        } else if ( fields[i] instanceof Complex ) {
                            Complex complex = (Complex) fields[i];
                            if ( complex.size() != _fields[i].columns.length )
                                throw new PersistenceException( "Size of complex field mismatch!" );
                            
                            for ( int j=0; j<_fields[i].columns.length; j++ ) {
                                SQLTypes.setObject( stmt, count++, toSQL( i, j, complex.get(j)), _fields[i].columns[j].sqlType );
                            }
                        } else {
                            if ( _fields[i].columns.length != 1 )
                                throw new PersistenceException( "Complex field expected! ");
                            
                            SQLTypes.setObject( stmt, count++, toSQL( i, 0, fields[i]), _fields[i].columns[0].sqlType );
                        }
                    }
                }
                
                // bind the identity of the row to be stored into the preparedStatement
                if ( identity instanceof Complex ) {
                    Complex id = (Complex) identity;
                    if ( id.size() != _ids.length || _ids.length <= 1 )
                        throw new PersistenceException( "Size of complex field mismatched!");
                    
                    for ( int i=0; i<_ids.length; i++ ){
                        stmt.setObject( count++, idToSQL( i, id.get(i) ) );
                        if (_log.isDebugEnabled()) {
                            _log.debug ( Messages.format("jdo.bindingIdentity", _ids[i].name, idToSQL( i, id.get(i) ) ) );
                        }
                    }                    
                } else {
                    if ( _ids.length != 1 )
                        throw new PersistenceException( "Complex field expected!" );
                    
                    stmt.setObject( count++, idToSQL( 0, identity ) );
                    if (_log.isDebugEnabled()) {
                        _log.debug ( Messages.format("jdo.bindingIdentity", _ids[0].name, idToSQL( 0, identity ) ) );
                    }
                }
                
                // bind the old fields of the row to be stored into the preparedStatement
                if ( original != null ) {
                    boolean supportsSetNull = ((BaseFactory) _factory).supportsSetNullInWhere();
                    
                    for ( int i = 0 ; i < _fields.length ; ++i ) {
                        if ( _fields[ i ].store && _fields[i].dirtyCheck ) {
                            if ( original[i] == null ) {
                                if (supportsSetNull) {
                                    for ( int j=0; j < _fields[i].columns.length; j++ )
                                        stmt.setNull( count++, _fields[i].columns[j].sqlType );
                                }
                            } else if ( original[i] instanceof Complex ) {
                                Complex complex = (Complex) original[i];
                                if ( complex.size() != _fields[i].columns.length )
                                    throw new PersistenceException( "Size of complex field mismatch!" );
                                
                                for ( int j=0; j<_fields[i].columns.length; j++ ) {
                                    SQLTypes.setObject( stmt, count++, toSQL( i, j, complex.get(j)), _fields[i].columns[j].sqlType );
                                    
                                    if (_log.isDebugEnabled()) {
                                        _log.debug (Messages.format( "jdo.bindingField", _fields[i].columns[j].name, toSQL( i, j, complex.get(j) ) ) );
                                    }
                                    
                                }
                            } else {
                                if ( _fields[i].columns.length != 1 )
                                    throw new PersistenceException( "Complex field expected! ");
                                
                                SQLTypes.setObject( stmt, count++, toSQL( i, 0, original[i]), _fields[i].columns[0].sqlType );
                            
                                if (_log.isDebugEnabled()) {
                                    _log.debug (Messages.format( "jdo.bindingField", _fields[i].columns[0].name, toSQL( i, 0, original[i] ) ) );
                                }
                            }
                        }
                    }
                }
                
                if (_log.isDebugEnabled()) {
                    _log.debug( Messages.format( "jdo.storing", _clsDesc.getJavaClass().getName(), stmt.toString()) );
                }

                if ( stmt.executeUpdate() <= 0 ) { // SAP DB returns -1 here
                    // If no update was performed, the object has been previously
                    // removed from persistent storage or has been modified if
                    // dirty checking. Determine which is which.
                    stmt.close();
                    if ( original != null ) {
                        stmt = ( (Connection) conn ).prepareStatement( /*_pkLookup*/_sqlLoad );
                        
                        if(_log.isDebugEnabled()) {
                            _log.debug( Messages.format( "jdo.storing", _clsDesc.getJavaClass().getName(), 
                                    stmt.toString() ) );
                        }
                        
                        // bind the identity to the prepareStatement
                        count = 1;
                        if ( identity instanceof Complex ) {
                            Complex id = (Complex) identity;
                            for ( int i=0; i<_ids.length; i++ )
                                stmt.setObject( count++, idToSQL( i, id.get(i) ) );
                            
                        } else {
                            stmt.setObject( count++, idToSQL( 0, identity ) );
                        }
                        
                        ResultSet res = stmt.executeQuery();
                        if ( res.next() ) {                     
                            StringBuffer enlistFieldsNotMatching = new StringBuffer();
                            
                            Object currentField = null;
                            int numberOfFieldsNotMatching = 0;
                            for(int i = 0; i < _fields.length; i++){
                                currentField = toJava(i, 0, res.getObject(_fields[i].columns[0].name));
                                if (_fields[i].tableName.compareTo(_mapTo) == 0) {
                                    if ((original[i] == null && currentField != null) ||
                                        (currentField == null && original[i] != null) ||
                                        (original[i] == null && currentField == null)) {
                                        enlistFieldsNotMatching.append ("(" + _clsDesc.getJavaClass().getName() + ")." + _fields[i].columns[0].name + ": ");
                                        enlistFieldsNotMatching.append ("[" + original[i] + "/" + currentField + "]"); 
                                    } else if (!original[i].equals(currentField) ) {
                                        if (numberOfFieldsNotMatching >= 1) {
                                            enlistFieldsNotMatching.append (", ");
                                        }
                                        enlistFieldsNotMatching.append ("(" + _clsDesc.getJavaClass().getName() + ")." + _fields[i].columns[0].name + ": ");
                                        enlistFieldsNotMatching.append ("[" + original[i] + "/" + currentField + "]"); 
                                        numberOfFieldsNotMatching++;
                                    }
                                }
                            }
                            throw new ObjectModifiedException( Messages.format("persist.objectModified", _clsDesc.getJavaClass().getName(), identity, enlistFieldsNotMatching.toString()) );
                        }
                    }
                    throw new ObjectDeletedException( Messages.format("persist.objectDeleted", _clsDesc.getJavaClass().getName(), identity) );
                }                
            } catch ( SQLException except ) {
                _log.fatal( Messages.format( "jdo.storeFatal", _type,  storeStatement ), except );
                throw new PersistenceException( Messages.format("persist.nested", except), except );
            } finally {
                try {
                    // Close the insert/select statement
                    if ( stmt != null )
                        stmt.close();
                } 
                catch ( SQLException except2 ) {
                    _log.warn ("Problem closing JDBC statement", except2);
                }
            }
        }

        return null;
    }


    public void delete( Object conn, Object identity )
            throws PersistenceException {

        PreparedStatement stmt = null;

        try {
            stmt = ( (Connection) conn ).prepareStatement( _sqlRemove );
            
            if(_log.isDebugEnabled()){
//                _log.debug( Messages.format( "jdo.removing", _clsDesc.getJavaClass().getName(), _sqlRemove ) );
                _log.debug( Messages.format( "jdo.removing", _clsDesc.getJavaClass().getName(), stmt.toString()) );
            }

            int count = 1;
            // bind the identity of the preparedStatement
            if ( identity instanceof Complex ) {
                Complex id = (Complex) identity;
                if ( id.size() != _ids.length || _ids.length <= 1 )
                    throw new PersistenceException( "Size of complex field mismatched!");

                for ( int i=0; i<_ids.length; i++ )
                    stmt.setObject( count++, idToSQL( i, id.get(i) ) );

            } else {
                if ( _ids.length != 1 )
                    throw new PersistenceException( "Complex field expected!" );
                stmt.setObject( count++, idToSQL( 0, identity ) );
            }

            if(_log.isDebugEnabled()){
              _log.debug( Messages.format( "jdo.removing", _clsDesc.getJavaClass().getName(), stmt.toString()) );
          }

            int result = stmt.executeUpdate();
            if ( result < 1 )
                throw new PersistenceException("Object to be deleted does not exist! "+ identity );

            // Must delete record in parent table last.
            // All other dependents have been deleted before.
            if ( _extends != null )
                _extends.delete( conn, identity );
        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.deleteFatal", _type, _sqlRemove ), except );

            throw new PersistenceException( Messages.format("persist.nested", except), except );
        } finally {
            try {
                if ( stmt != null ) {
                    stmt.close();
                }
            } catch ( Exception e ) {
                _log.warn ("Problem closing JDBC statement", e);
            }
        }
    }


    public void writeLock( Object conn, Object identity )
            throws ObjectDeletedException, PersistenceException {

        PreparedStatement stmt = null;
        try {
            // Must obtain lock on record in parent table first.
            if ( _extends != null )
                _extends.writeLock( conn, identity );

            stmt = ( (Connection) conn ).prepareStatement( _pkLookup );
            
            if(_log.isDebugEnabled()) {
                _log.debug( Messages.format( "jdo.acquireWriteLock", _pkLookup ) );
            }
            
            int count = 1;
            // bind the identity of the preparedStatement
            if ( identity instanceof Complex ) {
                Complex id = (Complex) identity;
                if ( id.size() != _ids.length || _ids.length <= 1 )
                    throw new PersistenceException( "Size of complex field mismatched!");

                for ( int i=0; i<_ids.length; i++ )
                    stmt.setObject( count++, idToSQL( i, id.get(i) ) );

            } else {
                if ( _ids.length != 1 )
                    throw new PersistenceException( "Complex field expected!" );

                stmt.setObject( count++, idToSQL( 0, identity ) );
            }

            // If no query was performed, the object has been previously
            // removed from persistent storage. Complain about this.
            if ( ! stmt.executeQuery().next() )
                throw new ObjectDeletedException( Messages.format("persist.objectDeleted", _clsDesc.getJavaClass().getName(), identity ) );
            stmt.close();
        } catch ( SQLException except ) {
            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } catch ( SQLException except2 ) {
                _log.warn("Problem closing JDBC statement", except2);
            }
            throw new PersistenceException( Messages.format("persist.nested", except), except );
        }
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
     * @param conn An open connection
     * @param fields An Object[] to load field values into
     * @param identity Identity of the object to load.
     * @param accessMode The access mode (null equals shared)
     * @return The object's stamp, or null
     * @throws ObjectNotFoundException The object was not found in persistent storage
     * @throws PersistenceException A persistence error occured
     */
    public Object load( Object conn, ProposedObject proposedObject, Object identity, AccessMode accessMode )
    throws ObjectNotFoundException, PersistenceException {
        PreparedStatement stmt  = null;
        ResultSet         rs    = null;
        Object            stamp = null;
        boolean           notNull;

                
        Object[] fields = proposedObject.getFields();
        
        try {
            String sqlString = (accessMode == AccessMode.DbLocked) ? _sqlLoadLock : _sqlLoad; 
            stmt = ((Connection) conn).prepareStatement(sqlString);
                        
            if (_log.isDebugEnabled()) {
                _log.debug( Messages.format("jdo.loading", _clsDesc.getJavaClass().getName(), stmt.toString()));
            }
            
            int fieldIndex = 1;
            // bind the identity of the preparedStatement
            if ( identity instanceof Complex ) {
                Complex id = (Complex) identity;
                if ( id.size() != _ids.length || _ids.length <= 1 )
                    throw new PersistenceException( "Size of complex field mismatched! expected: "+_ids.length+" found: "+id.size() );

                for ( int i=0; i<_ids.length; i++ )
                    stmt.setObject(fieldIndex++, idToSQL(i, id.get(i)));
            } else {
                if ( _ids.length != 1 )
                    throw new PersistenceException( "Complex field expected!" );
                stmt.setObject(fieldIndex++, idToSQL(0, identity));
            }

            if (_log.isDebugEnabled()) {
                _log.debug( Messages.format( "jdo.loading", _clsDesc.getJavaClass().getName(), stmt.toString()) );
            }

            // execute the SQL query 
            rs = stmt.executeQuery();
            if ( ! rs.next() )
                throw new ObjectNotFoundException( Messages.format("persist.objectNotFound", _clsDesc.getJavaClass().getName(), identity) );

            if (_extendingClassDescriptors.size() > 0) {
                Object[] returnValues = 
                    calculateNumberOfFields(_extendingClassDescriptors, 
                            _ids.length, _fields.length, _numberOfExtendLevels, rs);
                JDOClassDescriptor potentialLeafDescriptor = (JDOClassDescriptor) returnValues[0];
            	
                if (potentialLeafDescriptor != null &&
                    !potentialLeafDescriptor.getJavaClass().getName().equals (getDescriptor().getJavaClass().getName())) {
                    Object[] expandedFields = new Object[potentialLeafDescriptor.getFields().length];
                    
                    fields = expandedFields;
                    proposedObject.setFields (expandedFields);
                    proposedObject.setActualClass (potentialLeafDescriptor.getJavaClass());
                    proposedObject.setExpanded(true);
                }

            	return null;
            }
            
            // Load all the fields of the object including one-one relations
            // index to use during ResultSet.getXXX()
            int columnIndex = 1;
            // index in fields[] for storing result of SQLTypes.getObject()
            fieldIndex = 1;
            String tableName = null;
            String tableNameOld = tableName;
            Object[] temp = new Object[10]; // assume complex field max at 10
            for (int i = 0 ; i < _fields.length ; ++i ) {
            	tableName = _fields[i].tableName;
            	if (!tableName.equals (tableNameOld) && !_fields[i].joined) {
            		columnIndex = columnIndex + _ids.length; 
            	}
            	
                if ( !_fields[i].load )
                    continue;

                if ( !_fields[i].multi ) {
                    notNull = false;
                    if ( _fields[i].columns.length == 1 ) {
                        fields[i] = toJava(i, 0, SQLTypes.getObject(rs, columnIndex++, _fields[i].columns[0].sqlType));
                        fieldIndex++;
                    } else {
                        for (int j = 0; j < _fields[i].columns.length; j++) {
                            temp[j] = toJava(i, j, SQLTypes.getObject(rs, columnIndex++, _fields[i].columns[j].sqlType));
                            fieldIndex++;
                            if ( temp[j] != null ) {
                                notNull = true;
                            }
                        }
                        if ( notNull )
                            fields[i] = new Complex( _fields[i].columns.length, temp );
                        else
                            fields[i] = null;
                    }
                } else {
                    ArrayList res = new ArrayList();
                    notNull = false;
                    for ( int j=0; j<_fields[i].columns.length; j++ ) {
                        temp[j] = toJava(i, j, SQLTypes.getObject(rs, columnIndex, _fields[i].columns[j].sqlType));
                        if ( temp[j] != null ) {
                            notNull = true;
                        }
                        fieldIndex++;
                        columnIndex++;
                    }
                    if ( notNull ) {
                        if ( _fields[i].columns.length == 1 )
                            res.add( temp[0] );
                        else
                            res.add( new Complex( _fields[i].columns.length, temp ) );
                    }
                    fields[i] = res;
                }
                
                tableNameOld = tableName;
            }

            while (rs.next()) {
                fieldIndex = 1;
                columnIndex = 1;

                tableName = null;
                tableNameOld = tableName;

                for (int i = 0; i < _fields.length ; ++i) {

                	tableName = _fields[i].tableName;
                	if (!tableName.equals (tableNameOld) && !_fields[i].joined) {
                	    columnIndex = columnIndex + _ids.length;
                	}
                	
                	if ( !_fields[i].load )
                        continue;

                    if ( _fields[i].multi ) {
                        ArrayList res = (ArrayList)fields[i];
                        notNull = false;
                        for (int j = 0; j < _fields[i].columns.length; j++) {
                            temp[j] = toJava(i, j, SQLTypes.getObject(rs, columnIndex, _fields[i].columns[j].sqlType));
                            if (temp[j] != null) { notNull = true; }
                            columnIndex++;
                        }
                        fieldIndex++;
                        if ( notNull ) {
                            if ( _fields[i].columns.length == 1 ) {
                                if ( !res.contains( temp[0] ) )
                                    res.add( temp[0] );
                            } else {
                                Complex com = new Complex( _fields[i].columns.length, temp );
                                if ( !res.contains( com ) )
                                    res.add( new Complex( _fields[i].columns.length, temp ) );
                            }
                        }
                    } else {
                        fieldIndex++;
                        columnIndex += _fields[i].columns.length;
                    }
                    tableNameOld = tableName;
                }
                
                proposedObject.setFields(fields);
            }
        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.loadFatal", _type, (( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad ) ), except );
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            Utils.closeResultSet(rs);
            Utils.closeStatement(stmt);
        }
        return stamp;
    }
    
    private int numberOfExtendingClassDescriptors (JDOClassDescriptor classDescriptor) {
        int numberOfExtendLevels = 1;
        JDOClassDescriptor currentClassDescriptor = getDescriptor();
        while (currentClassDescriptor.getExtends() != null) {
            currentClassDescriptor = (JDOClassDescriptor) currentClassDescriptor.getExtends();
            numberOfExtendLevels++;
        }
        return numberOfExtendLevels;
    }

    private Object[] calculateNumberOfFields (Collection extendingClassDescriptors, 
            int numberOfIdentityColumns,
            int numberOfFields,
            int numberOfExtendLevels, 
            ResultSet rs)
    throws SQLException
    {
        JDOClassDescriptor potentialLeafDescriptor = null;
        int suggestedNumberOfFields = numberOfFields;
        Collection potentialActualClassDescriptor = new LinkedList();
        int numberOfIdentitiesToAnalyze = 0;
        addExtendingClassDescriptors(potentialActualClassDescriptor, extendingClassDescriptors);
        
        JDOClassDescriptor potentialClassDescriptor = null;
        JDOClassDescriptor potentialClassDescriptorPrevious = null;
        int initialColumnIndex = numberOfFields + numberOfIdentityColumns * numberOfExtendLevels + 1;
        int columnIndex = initialColumnIndex;
        int numberOfExtendingClassDescriptors = 0;
        for (Iterator iter = potentialActualClassDescriptor.iterator(); iter.hasNext(); ) {
            potentialClassDescriptor = (JDOClassDescriptor) iter.next();
            numberOfExtendingClassDescriptors += 1;
            _log.debug ("Potential extending class descriptor: " + potentialClassDescriptor.getJavaClass().getName());
            FieldDescriptor[] identityDescriptors = potentialClassDescriptor.getIdentities();
            boolean isNull = true;
            
            for (int i = 0; i < identityDescriptors.length; i++) {
                Object temp;
                Object[] temps;
                JDOFieldDescriptor jdoFieldDescriptor = (JDOFieldDescriptor) identityDescriptors[i];
                if (jdoFieldDescriptor.getSQLName().length == 1 ) {
                    temp = SQLTypes.getObject( rs, columnIndex++, java.sql.Types.JAVA_OBJECT);
                    isNull = (temp == null);
                } else {
                    temps = new Object[jdoFieldDescriptor.getSQLName().length];
                    for ( int j=0; j<jdoFieldDescriptor.getSQLName().length; j++ ) {
                        temps[j] = SQLTypes.getObject( rs, columnIndex++, java.sql.Types.JAVA_OBJECT);
                        isNull = (temps[j] == null);
                    }
                    temp = new Complex (jdoFieldDescriptor.getSQLName().length, temps);
                }
                
                _log.debug ("Obtained value " + temp + " for additional (extending) identity " + 
                        potentialClassDescriptor.getJavaClass().getName() + "/" + 
                        identityDescriptors[i].getFieldName() + " at position " + 
                        columnIndex);
                isNull = (temp == null);
                if (temp != null) {
                    numberOfIdentitiesToAnalyze += 1;
                    potentialClassDescriptorPrevious = potentialClassDescriptor;
                }
                
            }
            
            if (!iter.hasNext() && !isNull && numberOfIdentitiesToAnalyze > 0) {
                potentialLeafDescriptor = potentialClassDescriptor;
                suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
            } else if (!iter.hasNext() && isNull && numberOfIdentitiesToAnalyze > 0){
                potentialLeafDescriptor = potentialClassDescriptorPrevious; 
                // suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
            } else {
                FieldDescriptor[] potentialFields = 
                    (FieldDescriptor[]) potentialClassDescriptor.getFields();
                for (int i = 0; i < potentialFields.length; i++) {
                    JDOFieldDescriptor jdoFieldDescriptor = (JDOFieldDescriptor) potentialFields[i];
                    String[] columnNames = jdoFieldDescriptor.getSQLName();
                    columnIndex = columnIndex + columnNames.length;
                }
                
                // the JDOClassDescriptor we just looked at is definitely part of the extends hierarchy,
                // and as such we need to increase the number of potential fields
                if (!isNull) {
                    suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
                }
            }
        }
        
        _log.debug ("In total " + numberOfIdentitiesToAnalyze + " (extending) identities analyzed.");
        
        if ((potentialLeafDescriptor != null) && _log.isDebugEnabled()) {
            _log.debug ("Most likely of type " + potentialLeafDescriptor.getJavaClass().getName());
            _log.debug ("After analysis, " + suggestedNumberOfFields + " fields need to be loaded.");
        }
        
        return new Object[] {potentialLeafDescriptor, new Integer (suggestedNumberOfFields) };
        
    }
    
    private void buildSqlCreate () throws QueryException {
        StringBuffer sql;
        int count;
        boolean keyGened = false;
        
        String tableName = _mapTo;
        
        // Create statement to insert a new row into the table
        // using the specified primary key if one is required
        sql = new StringBuffer( "INSERT INTO " );
        sql.append( _factory.quoteName( tableName ) ).append( " (" );
        count = 0;
        for ( int i=0; i<_ids.length; i++ ) {
            if ( _keyGen == null || _keyGen.getStyle() == KeyGenerator.BEFORE_INSERT ) {
                if ( count > 0 ) sql.append( ',' );
                keyGened = true;
                sql.append( _factory.quoteName( _ids[i].name ) );
                ++count;
            }
        }
        for ( int i = 0 ; i < _fields.length; ++i ) {
            if ( _fields[i].store ) {
                for ( int j=0; j<_fields[i].columns.length; j++ ) {
                    if ( count > 0 )
                        sql.append( ',' );
                    sql.append( _factory.quoteName( _fields[i].columns[j].name ) );
                    ++count;
                }
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
        for ( int i = 0 ; i < count; ++i ) {
            if ( i > 0 )
                sql.append( ',' );
            sql.append( '?' );
        }
        sql.append( ')' );
        _sqlCreate = sql.toString();

        if ( ! keyGened ) {
            try {
                _sqlCreate = _keyGen.patchSQL( _sqlCreate, _ids[0].name /*primKeyName*/ );
            } catch ( MappingException except )  {
                _log.fatal( except );
                // proceed without this stupid key generator
                _keyGen = null;
                buildSqlCreate();
                return;
            }
            if ( _keyGen.getStyle() == KeyGenerator.DURING_INSERT )
                _sqlCreate = "{call " + _sqlCreate + "}";
        }

        if(_log.isDebugEnabled()) {
            _log.debug( Messages.format( "jdo.creating", _type, _sqlCreate ) );
        }
        
    }
    private void buildSqlUpdate () {
        StringBuffer         sql;
        int                  count;
        
        // append the SET clause only if there are any fields that need to be 
        //persisted.
        if (hasFieldsToPersist) {
            
            sql = new StringBuffer( "UPDATE " );
            sql.append( _factory.quoteName( _mapTo ) );
            sql.append( " SET " );
            
            count = 0;
            for ( int i = 0 ; i < _fields.length ; ++i ) {
                if ( _fields[ i ].store ) {
                    for ( int j=0; j<_fields[i].columns.length; j++ ) {
                        if ( count > 0 )
                            sql.append( ',' );
                        sql.append( _factory.quoteName( _fields[i].columns[j].name ) );
                        sql.append( "=?" );
                        ++count;
                    }
                }
            }
            
            sql.append (buildWherePK());
            _sqlStore = sql.toString();

            for ( int i = 0 ; i < _fields.length ; ++i ) {
                if ( _fields[i].store && _fields[i].dirtyCheck ) {
                    for ( int j=0; j<_fields[i].columns.length; j++ ) {
                        sql.append( " AND " );
                        sql.append( _factory.quoteName( _fields[i].columns[j].name ) );
                        sql.append( "=?" );
                    }
                }
            }
            _sqlStoreDirty = sql.toString();
            
            if(_log.isDebugEnabled()) {
                _log.debug( Messages.format( "jdo.updating", _type, _sqlStoreDirty ) );
            }
        } 
//        else
//            throw new QueryException ("Class needs at least one field to persist.");
//        }
        
    }
    
    private void buildSqlRemove () {
        StringBuffer         sql;

        String tableName = _mapTo;
        
        sql = new StringBuffer( "DELETE FROM " ).append( _factory.quoteName( tableName ) );
        sql.append (buildWherePK());
        _sqlRemove = sql.toString();
        
        if(_log.isDebugEnabled()) {
            _log.debug( Messages.format( "jdo.removing", _type, _sqlRemove ) );
        }
        
    }
    
    private String buildWherePK () {
        // create sql statements
        StringBuffer sb = new StringBuffer();
        sb.append( JDBCSyntax.Where );
        for ( int i=0; i<_ids.length; i++ ) {
            if ( i > 0 ) sb.append( " AND " );
            sb.append( _factory.quoteName( _ids[i].name ) );
            sb.append( QueryExpression.OpEquals );
            sb.append( JDBCSyntax.Parameter );
        }
        return sb.toString();
        
    }
    
    private void buildSqlPKLookup () throws QueryException {
        
        String tableName = _mapTo;
        QueryExpression query = _factory.getQueryExpression();

        // initalize lookup query
        for ( int i=0; i<_ids.length; i++ ) {
            query.addParameter( tableName, _ids[i].name, QueryExpression.OpEquals );
        }
        _pkLookup = query.getStatement( true );
        
    }
    
    private void buildFinder(JDOClassDescriptor clsDesc) throws QueryException {
        QueryExpression expr;
        QueryExpression find;

        expr = _factory.getQueryExpression();
        find = _factory.getQueryExpression();
        //addLoadSql( expr, fields, false, true, true );

        //_fields = new FieldInfo[ fields.size() ];
        //fields.copyInto( _fields );

        // get id columns' names
        String[] idnames = _clsDesc.getIdentityColumnNames();
        for ( int i=0; i<_ids.length; i++ ) {
            expr.addParameter( _mapTo, _ids[i].name, QueryExpression.OpEquals );
        }

        // join all the extended table
        JDOClassDescriptor curDesc = clsDesc;
        JDOClassDescriptor baseDesc;
        while ( curDesc.getExtends() != null ) {
            baseDesc = (JDOClassDescriptor) curDesc.getExtends();
            expr.addInnerJoin(curDesc.getTableName(), curDesc.getIdentityColumnNames(),
                              baseDesc.getTableName(), baseDesc.getIdentityColumnNames());
            find.addInnerJoin(curDesc.getTableName(), curDesc.getIdentityColumnNames(),
                              baseDesc.getTableName(), baseDesc.getIdentityColumnNames());
            curDesc = baseDesc;
        }
        
        // join all the related/depended table
        Vector joinTables = new Vector();
        String aliasOld = null;
        String alias = null;
        
        for (int i = 0; i < _fields.length; i++) {
        	if (i > 0) { aliasOld = alias; }
            alias = _fields[i].tableName;
            
            // add id columns to select statement
            if (!alias.equals(aliasOld) && !_fields[i].joined) {
                JDOClassDescriptor classDescriptor = (JDOClassDescriptor) 
                    _fields[i].fieldDescriptor.getContainingClassDescriptor();
                String[] ids = classDescriptor.getIdentityColumnNames();
            	for (int j = 0; j < ids.length; j++) {
                	expr.addColumn(alias, ids[j]);
                    find.addColumn(alias, ids[j]);
            	}
            }
            
            if ( _fields[i].load ) {
                if ( _fields[i].joined /*&& !joinTables.contains( _fields[i].tableName )*/ ) {
                    int offset = 0;
                    String[] rightCol = _fields[i].joinFields;
                    String[] leftCol = new String[_ids.length-offset];
                    for ( int j=0; j<leftCol.length; j++ ) {
                        leftCol[j] = _ids[j+offset].name;
                    }
                    if (joinTables.contains( _fields[i].tableName ) || clsDesc.getTableName().equals( _fields[i].tableName )) {
                        alias = alias.replace('.', '_') + "_f" + i; // should not mix with aliases in ParseTreeWalker
                        expr.addOuterJoin( _mapTo, leftCol, _fields[i].tableName, rightCol, alias );
                        find.addOuterJoin( _mapTo, leftCol, _fields[i].tableName, rightCol, alias );
                    } else {
                        expr.addOuterJoin( _mapTo, leftCol, _fields[i].tableName, rightCol );
                        find.addOuterJoin( _mapTo, leftCol, _fields[i].tableName, rightCol );
                        joinTables.add( _fields[i].tableName );
                    }
                }

                for ( int j=0; j<_fields[i].columns.length; j++ ) {
                    expr.addColumn( alias, _fields[i].columns[j].name );
                    find.addColumn( alias, _fields[i].columns[j].name );
                }
                
                expr.addTable(_fields[i].tableName, alias);
                find.addTable(_fields[i].tableName, alias);
            }
        }

        // 'join' all the extending tables 
        curDesc = clsDesc;
        List classDescriptorsToAdd = new LinkedList();
        JDOClassDescriptor classDescriptor = null;
        addExtendingClassDescriptors(classDescriptorsToAdd, curDesc.getExtendedBy());
        
        if (classDescriptorsToAdd.size() > 0) {
        	for (Iterator iter = classDescriptorsToAdd.iterator(); iter.hasNext(); ) {
        		classDescriptor = (JDOClassDescriptor) iter.next();
        		
        		if (_log.isDebugEnabled()) {
                    _log.debug("Adding outer left join for " + classDescriptor.getJavaClass().getName() + 
        				       " on table " + classDescriptor.getTableName());
                }
                
                expr.addOuterJoin( _mapTo, 
                		curDesc.getIdentityColumnNames(), 
                		classDescriptor.getTableName(), 
                		classDescriptor.getIdentityColumnNames());
                find.addOuterJoin( _mapTo, 
                		curDesc.getIdentityColumnNames(), 
                		classDescriptor.getTableName(), 
                		classDescriptor.getIdentityColumnNames());

                Persistence persistenceEngine;
				try {
					persistenceEngine = _factory.getPersistence (classDescriptor);
				} catch (MappingException e) {
					throw new QueryException("Problem obtaining persistence engine for ClassDescriptor " + classDescriptor.getJavaClass().getName(), e);
				}

				SQLEngine.ColumnInfo[] idInfos = 
					(SQLEngine.ColumnInfo[]) persistenceEngine.getColumnInfoForIdentities();
                for (int i = 0; i < idInfos.length; i++) {
                	expr.addColumn (classDescriptor.getTableName(), idInfos[i].name);
                	find.addColumn (classDescriptor.getTableName(), idInfos[i].name);
                }
                
                SQLEngine.FieldInfo[] fieldInfos = (SQLEngine.FieldInfo[]) persistenceEngine.getInfo();
                for (int i = 0; i < fieldInfos.length; i++) {
                	boolean hasFieldToAdd = false;
                	SQLEngine.ColumnInfo[] columnInfos = fieldInfos[i].columns;
                	if (classDescriptor.getTableName().equals(fieldInfos[i].tableName)) {
                		for ( int j = 0; j < columnInfos.length; j++ ) {
                    		expr.addColumn (classDescriptor.getTableName(), fieldInfos[i].columns[j].name);
                    		find.addColumn (classDescriptor.getTableName(), fieldInfos[i].columns[j].name);
                		}
                		hasFieldToAdd = true;
                	}
                    
                    if (hasFieldToAdd) {
                    	expr.addTable(classDescriptor.getTableName());
                    	find.addTable(classDescriptor.getTableName());
                    }
                }
        	}
        }


        _sqlLoad = expr.getStatement( false );
        _sqlLoadLock = expr.getStatement( true );

        _sqlFinder = find;

        if(_log.isDebugEnabled()) {
            _log.debug(Messages.format("jdo.loading", _type, _sqlLoad));
            _log.debug(Messages.format("jdo.loading.with.lock", _type, _sqlLoadLock));
            _log.debug(Messages.format("jdo.finding", _type, _sqlFinder));
        }
        
    }
    
    private void addExtendingClassDescriptors (Collection classDescriptorsToAdd, Collection extendingClassDescriptors) {
    	JDOClassDescriptor classDescriptor = null; 
        for (Iterator iter = extendingClassDescriptors.iterator(); iter.hasNext(); ) {
        	classDescriptor = (JDOClassDescriptor) iter.next(); 
        	classDescriptorsToAdd.add (classDescriptor);
        	addExtendingClassDescriptors(classDescriptorsToAdd, classDescriptor.getExtendedBy());
        }
    	
    }


    public String toString() {
        return _clsDesc.toString();
    }

    static final class FieldInfo implements Persistence.FieldInfo {

        final String  tableName;

        final String  jdoName;

        final boolean load;

        final boolean store;

        final boolean multi;

        final boolean joined;

        final boolean dirtyCheck;

        final String[] joinFields;

        ColumnInfo[] columns;
        
        final FieldDescriptor fieldDescriptor;
        
        final ClassDescriptor classDescriptor;

        FieldInfo( JDOClassDescriptor clsDesc, FieldDescriptor fieldDesc, String classTable, boolean ext )
                throws MappingException{

        	fieldDescriptor = fieldDesc;
        	
        	classDescriptor = clsDesc;
        	
            // for readability
            final int FIELD_TYPE = 0;

            final int REF_TYPE = 1;

            final int REL_TABLE_TYPE = 2;

            int type;

            FieldDescriptor[] classids = clsDesc.getIdentities();
            ClassDescriptor related = fieldDesc.getClassDescriptor();
            if ( related != null && !( related instanceof JDOClassDescriptor ) )
                    throw new MappingException("Related class is not JDOClassDescriptor");

            if ( fieldDesc.getClassDescriptor() != null ) {
                // !(fieldDesc instanceof JDOFieldDescriptor) ) {
                // no <sql> tag, treated as foreign key field of
                // PersistenceCapable

                // determine the type of field
                if ( !( fieldDesc instanceof JDOFieldDescriptor ) )
                    type = REF_TYPE;
                else if ( ((JDOFieldDescriptor)fieldDesc).getManyTable() != null )
                    type = REL_TABLE_TYPE;
                else if ( ((JDOFieldDescriptor)fieldDesc).getSQLName() != null )
                    type = FIELD_TYPE;
                else
                    type = REF_TYPE;

                // initalize the column names
                FieldDescriptor[] relids = ((JDOClassDescriptor)related).getIdentities();
                String[] names = null;
                if ( fieldDesc instanceof JDOFieldDescriptor )
                    names = ((JDOFieldDescriptor)fieldDesc).getSQLName();
                String[] relnames = new String[relids.length];
                for ( int i=0; i<relids.length; i++ ) {
                    relnames[i] = ((JDOFieldDescriptor)relids[i]).getSQLName()[0];
                    if ( relnames[i] == null )
                        throw new MappingException("Related class identities field does not contains sql information!");
                }
                String[] joins = null;
                if ( fieldDesc instanceof JDOFieldDescriptor )
                    joins = ((JDOFieldDescriptor)fieldDesc).getManyKey();
                String[] classnames = new String[classids.length];
                for ( int i=0; i<classids.length; i++ ) {
                    classnames[i] = ((JDOFieldDescriptor)classids[i]).getSQLName()[0];
                    if ( classnames[i] == null )
                        throw new MappingException("Related class identities field does not contains sql information!");
                }

                // basic check of column names
                if ( names != null && names.length != relids.length )
                    throw new MappingException("The number of column of foreign keys doesn't not match with what specified in manyKey");
                if ( joins != null && joins.length != classids.length )
                    throw new MappingException("The number of column of foreign keys doesn't not match with what specified in manyKey");

                // initalize the class
                switch (type) {
                case FIELD_TYPE:
                    this.tableName = classTable;//((JDOClassDescriptor)clsDesc).getTableName();;
                    this.jdoName = fieldDesc.getFieldName();
                    this.load = true;
                    this.store = !ext && !((JDOFieldDescriptor)fieldDesc).isReadonly();
                    this.multi = false;
                    this.joined = false;
                    this.dirtyCheck = ((JDOFieldDescriptor)fieldDesc).isDirtyCheck();
                    names = (names!=null?names:relnames);
                    this.joinFields = classnames;
                    break;
                case REF_TYPE:
                    this.tableName = ((JDOClassDescriptor)related).getTableName();
                    this.jdoName = fieldDesc.getFieldName();
                    this.load = true;
                    this.store = false;
                    this.multi = fieldDesc.isMultivalued();
                    this.joined = true;
                    this.dirtyCheck = (fieldDesc instanceof JDOFieldDescriptor)?((JDOFieldDescriptor)fieldDesc).isDirtyCheck():true;
                    names = (names!=null?names:relnames);
                    this.joinFields = (joins!=null?joins:classnames);
                    break;
                case REL_TABLE_TYPE:
                    this.tableName = ((JDOFieldDescriptor)fieldDesc).getManyTable();
                    this.jdoName = fieldDesc.getFieldName();
                    this.load = true;
                    this.store = false;
                    this.multi = fieldDesc.isMultivalued();
                    this.joined = true;
                    this.dirtyCheck = ((JDOFieldDescriptor)fieldDesc).isDirtyCheck();
                    names = (names!=null?names:relnames);
                    this.joinFields = (joins!=null?joins:classnames);
                    break;
                default:
                    throw new MappingException("Never happen! But, it won't compile without the exception");
                }

                this.columns = new ColumnInfo[relids.length];
                for ( int i=0; i<relids.length; i++ ) {
                    if ( !(relids[i] instanceof JDOFieldDescriptor) )
                        throw new MappingException("Related class identities field does not contains sql information!");

                    JDOFieldDescriptor relId = (JDOFieldDescriptor)relids[i];
                    FieldHandlerImpl fh = (FieldHandlerImpl) relId.getHandler();
                    columns[i] = new ColumnInfo( names[i], relId.getSQLType()[0],
                            fh.getConvertTo(), fh.getConvertFrom(), fh.getConvertParam() );
                }
            } else {
                // primitive field
                this.tableName = classTable;
                this.jdoName = fieldDesc.getFieldName();
                this.load = true;
                this.store = !ext && !((JDOFieldDescriptor)fieldDesc).isReadonly();
                this.multi = false;
                this.joined = false;
                this.joinFields = null;
                this.dirtyCheck = ((JDOFieldDescriptor)fieldDesc).isDirtyCheck();

                FieldHandlerImpl fh = (FieldHandlerImpl) fieldDesc.getHandler();
                this.columns = new ColumnInfo[1];
                String[] sqlNameArray = ((JDOFieldDescriptor)fieldDesc).getSQLName();
                String sqlName;
                if ( sqlNameArray == null ) {
                    sqlName = fieldDesc.getFieldName();
                } else {
                    sqlName = sqlNameArray[0];
                }
                this.columns[0] = new ColumnInfo( sqlName,
                        ((JDOFieldDescriptor)fieldDesc).getSQLType()[0], fh.getConvertTo(),
                        fh.getConvertFrom(), fh.getConvertParam() );
            }
        }
        public boolean isComplex() {
            return true;
        }
        public boolean isPersisted() {
            return store;
        }
        public String getFieldName() {
            return jdoName;
        }
        public String toString() {
            return tableName + "." + jdoName;
        }
    }

    static final class ColumnInfo implements Persistence.ColumnInfo{

        /**
         * Name of the column
         */
        final String  name;

        /**
         * SQL type of teh coplumn
         */
        final int sqlType;

        /**
         * TypeConvertor to use when converting to the SQLType of this column.
         */
        final TypeConvertor convertTo;

        /**
         * TypeConvertor to use when converting from the SQLType of this column.
         */
        final TypeConvertor convertFrom;

        /**
         * Type conversion parameters
         */
        final String convertParam;

        ColumnInfo( String name, int type, TypeConvertor convertTo,
                TypeConvertor convertFrom, String convertParam ) {
            this.name = name;
            this.sqlType = type;
            this.convertTo = convertTo;
            this.convertFrom = convertFrom;
            this.convertParam = convertParam;
        }

		/* (non-Javadoc)
		 * @see org.exolab.castor.persist.spi.Persistence.ColumnInfo#getName()
		 */
		public String getName() {
			return name;
		}

		/* (non-Javadoc)
		 * @see org.exolab.castor.persist.spi.Persistence.ColumnInfo#getSqlType()
		 */
		public int getSqlType() {
			return sqlType;
		}

		/* (non-Javadoc)
		 * @see org.exolab.castor.persist.spi.Persistence.ColumnInfo#getConvertTo()
		 */
		public TypeConvertor getConvertTo() {
			return convertTo;
		}

		/* (non-Javadoc)
		 * @see org.exolab.castor.persist.spi.Persistence.ColumnInfo#getConvertFrom()
		 */
		public TypeConvertor getConvertFrom() {
			return convertFrom;
		}

		/* (non-Javadoc)
		 * @see org.exolab.castor.persist.spi.Persistence.ColumnInfo#getConvertParam()
		 */
		public String getConvertParam() {
			return convertParam;
		}
    }

    static final class SQLQuery implements PersistenceQuery {

        private PreparedStatement _stmt;

        private ResultSet         _rs;

        private /*final*/ SQLEngine _engine;
        private SQLEngine _requestedEngine;

        private final Class[]   _types;

        private final Object[]  _values;

        private final String    _sql;

        private Object[]        _lastIdentity;

        private int[]           _identSqlType;

        private boolean         _resultSetDone;

        private Object[]        _fields;

        SQLQuery( SQLEngine engine, String sql, Class[] types )
        {
            _engine = engine;
            _requestedEngine = engine;
            _types = types;
            _values = new Object[ _types.length ];
            _sql = sql;
            _identSqlType = new int[_engine._clsDesc.getIdentities().length];
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

        /**
         * Move to an absolute position within a ResultSet. 
         * use the jdbc 2.0 method to move to an absolute position in the
         * resultset.
         * @param row The row to move to
         * @return True if the move was successful.
         * @throws PersistenceException Indicates a problem in moving to an absolute position.
         */
         public boolean absolute(int row)
            throws PersistenceException
         {
            boolean retval = false;
            try
            {
               if (_rs != null)
               {
                  retval = _rs.absolute(row);
               }
            }
            catch (SQLException e)
            {
               throw new PersistenceException(e.getMessage(), e);
            }
            return retval;
         }

         /**
          * Uses the underlying db's cursors to move to the last row in the
          * result set, get the row number via getRow(), then move back to
          * where ever the user was positioned in the resultset.
         * @return The size of the current result set. 
         * @throws PersistenceException If the excution of this method failed. 
          */
         public int size()
            throws PersistenceException
         {
            int whereIAm = 1; // first
            int retval = 0; // default size is 0;
            try
            {
               if (_rs != null)
               {
                  whereIAm = _rs.getRow();
                  if (_rs.last())
                  {
                     retval = _rs.getRow();
                  }
                  else
                  {
                     retval = 0;
                  }
                  // go back from whence I came.
                  if (whereIAm > 0)
                  {
                     _rs.absolute(whereIAm);
                  }
                  else
                  {
                     _rs.beforeFirst();
                  }
               }
            }
            catch (SQLException se)
            {
               throw new PersistenceException(se.getMessage());
            }
            return retval;
         }

        public void execute( Object conn, AccessMode accessMode )
            throws QueryException, PersistenceException
        {
            // backwards compatible, scrollable resultset is false;
            execute(conn, accessMode, false);
         }

        public void execute( Object conn, AccessMode accessMode, boolean scrollable )
            throws QueryException, PersistenceException
        {
             // create SQL statement from _sql, replacing bind expressions like "?1" by "?"
            String sql = SqlBindParser.getJdbcSql(_sql);

            _lastIdentity = null;

            try {
                if (scrollable)
                {
                    _stmt = ( (Connection) conn ).prepareStatement( sql, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY );
                }
                else
                {
                    _stmt = ( (Connection) conn ).prepareStatement( sql );
                }

                 // bind variable values on _values to the JDBC statement _stmt using the bind variable order in _sql 
                SqlBindParser.bindJdbcValues(_stmt, _sql, _values);

                 // erase bind values
                for(int i=0; i<_values.length; ++i)
                    _values[i] = null;

                if(_log.isDebugEnabled()) {
                    _log.debug (Messages.format ("jdo.executingSql", sql));
                }

                _rs = _stmt.executeQuery();
                _resultSetDone = false;
            } catch ( SQLException except ) {
                if ( _stmt != null ) {
                    try {
                        _stmt.close();
                    } 
                    catch ( SQLException e2 ) {
                        _log.warn("Problem closing JDBC statement", e2);
                    }
                }
                _resultSetDone = true;
                throw new PersistenceException( Messages.format("persist.nested", except) + " while executing "+ _sql, except );
            }
        }

        // Load a number of sql columns (from the current row of _rs) into an identity.
        private Object loadIdentity() throws SQLException, PersistenceException
        {
            // We can't retrieve a next identity if we have no rows of data left :-)
            if ( _resultSetDone )
                return null;

            boolean  empty = false;
            Object[] returnId = new Object[_engine._ids.length];
            Object   tmp;

            empty = true;
            for ( int i=0; i<_engine._ids.length; i++ ) {
                tmp = SQLTypes.getObject( _rs, 1+i, _identSqlType[i] );
                returnId[i] = _engine.idToJava( i, tmp );
                if ( tmp != null )
                    empty = false;
            }

            if ( ! empty ) {
                switch (_engine._ids.length) {
                case 1:
                    return returnId[0];
                case 2:
                    return new Complex( returnId[0], returnId[1] );
                default:
                    return new Complex( returnId );
                }
            }
            return null;
        }

        // Get the next identity that is different from <identity>.
        public Object nextIdentity( Object identity ) throws PersistenceException
        {
            try {
                if( _lastIdentity == null ) {
                    if ( _resultSetDone || !_rs.next() ) {
                        _resultSetDone = true;
                        return null;
                    }
                }

                // Look if the current row in our ResultSet already belongs to a different id.
                _lastIdentity = identityToSQL( identity );
                identity = loadIdentity();

                if( identitiesEqual( _lastIdentity, identityToSQL( identity ) ) ) {
                    // This will fetch the object data into our internal _fields[] and thus also
                    // "skip" all rows till the first one with a new identity.
                    fetchRaw( null );
                }

                identity = loadIdentity();

                // This will fetch the object data into our internal _fields[] and thus also
                // "skip" all rows till the first one with a new identity.
                fetchRaw( null );

            } catch ( SQLException except ) {
                _lastIdentity = null;
                throw new PersistenceException( Messages.format("persist.nested", except), except );
            }
            return identity;
        }


        public void close()
        {
            if ( _rs != null ) {
                try {
                    _rs.close();
                } catch ( SQLException except ) {
                    _log.warn("Problem closing JDBC ResultSet", except);
                }
                _rs = null;
            }
            if ( _stmt != null ) {
                try {
                    _stmt.close();
                } catch ( SQLException except ) {
                    _log.warn ("Problem closing JDBC statement", except);
                }
                _stmt = null;
            }
        }


        private Object[] identityToSQL( Object identity )
        {
            Object[] sqlIdentity = new Object[_engine._ids.length];

            if( identity != null ) {
                // Split complex identity into array of single objects.
                if ( _engine._ids.length > 1 ) {
                    Complex id = (Complex) identity;
                    for ( int i=0; i < _engine._ids.length; i++ ) {
                        sqlIdentity[i] = id.get(i);
                    }
                } else {
                    sqlIdentity[0] = identity;
                }
            }
            return sqlIdentity;
        }


        private Object loadSingleField(int i, CounterRef counterReference)
        throws SQLException, PersistenceException {
            String currentTableName = counterReference.getTableName();
            int count = counterReference.getCounter();
            Object[] temp = new Object[_engine._fields[i].columns.length];
            boolean notNull = false;
            Object   field;
            
            String fieldTableName = _engine._fields[i].tableName;
            String fieldColumnName = _engine._fields[i].columns[0].name;
            String fieldName = fieldTableName + "." + fieldColumnName;
            
            ResultSetMetaData metaData = _rs.getMetaData();
            while (true) {
                String metaTableName = metaData.getTableName(count);
                String metaColumnName = metaData.getColumnName(count);
               if (fieldColumnName.equalsIgnoreCase(metaColumnName)) {
                   if (fieldTableName.equalsIgnoreCase(metaTableName)) {
                       break;
                   } else if ("".equals(metaTableName)) {
                       break;
                   }
               } else if (fieldName.equalsIgnoreCase(metaColumnName)) {
                   break;
               }

               count++;
           }
            
            if ( _engine._fields[i].columns.length == 1 ) {
                field = _engine.toJava(i, 0, SQLTypes.getObject(_rs, count, _engine._fields[i].columns[0].sqlType));
                count++;
            } else {
                for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                    temp[j] = _engine.toJava(i, j, SQLTypes.getObject(_rs, count, _engine._fields[i].columns[j].sqlType));
                    count++;
                    if ( temp[j] != null ) {
                        notNull = true;
                    }
                }
                if ( notNull )
                    field = new Complex( _engine._fields[i].columns.length, temp );
                else
                    field = null;
            }
            counterReference.setCounter(count);
            counterReference.setTableName(currentTableName);
            return field;
        }


        private Object loadMultiField(int i, CounterRef counterReference, Object field)
        throws SQLException, PersistenceException {
            int count = counterReference.getCounter();
            Object[]  temp = new Object[_engine._fields[i].columns.length];
            boolean notNull = false;
            ArrayList res;

            String fieldTableName = _engine._fields[i].tableName;
            String firstColumnOfField = _engine._fields[i].columns[0].name;
            
            ResultSetMetaData metaData = _rs.getMetaData();
            while (!(firstColumnOfField.equalsIgnoreCase(metaData.getColumnName(count))
                     && (fieldTableName.equalsIgnoreCase(metaData.getTableName(count))
                         || "".equals(metaData.getTableName(count))))) {
            	count++;
            }

            if( field == null )
                res = new ArrayList();
            else
                res = (ArrayList) field;

            for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                temp[j] = _engine.toJava(i, j, SQLTypes.getObject(_rs, count, _engine._fields[i].columns[j].sqlType));
                if ( temp[j] != null ) {
                    notNull = true;
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
                        res.add( com );
                }
            }
            counterReference.setCounter(count);
            
            return res;
        }

        private int loadRow(Object[] fields, int numberOfFields, boolean isFirst)
        throws SQLException, PersistenceException {
            int count = _engine._ids.length + 1;

            String tableName = null;

            // TODO: wrong, as it could be that the first field is not part of the root class.
            if (numberOfFields > 0) {
                tableName = _engine._fields[0].tableName;
                
                // Load all the fields.
                CounterRef counterReference = new CounterRef ();
                counterReference.setCounter(count);
                counterReference.setTableName(tableName);
                
                for ( int i = 0 ; i < numberOfFields ; ++i  ) {
                    if (!_engine._fields[i].load) { continue; }
                    
                    if ( _engine._fields[i].multi ) {
                        counterReference.setCounter(count);
                        fields[i] = loadMultiField( i, counterReference, fields[i] );
                        count = counterReference.getCounter(); 
                    } else if( isFirst ) {
                        // Non-multi fields have to be done one only once, so this is skipped
                        // if we have already read the first row.
                        counterReference.setCounter (count);
                        fields[i] = loadSingleField( i, counterReference);
                        count = counterReference.getCounter();
                    }
                }
            }
            return count;
        }


        private Object[] loadSQLIdentity() throws SQLException
        {
            Object[] identity = new Object[_engine._ids.length];

            // Load the identity from the current row.
            for ( int i = 0; i < _engine._ids.length; i++ ) {
                identity[i] = SQLTypes.getObject( _rs, 1+i, _identSqlType[i] );
            }
            return identity;
        }


        private boolean identitiesEqual( Object[] wantedIdentity, Object[] currentIdentity )
        {
            // Check if the given identities differ.
            for ( int i = 0; i < wantedIdentity.length; i++ ) {
                if( wantedIdentity[i] == null || currentIdentity[i] == null ) {
                    if( wantedIdentity[i] != currentIdentity[i] )
                        return false;
                } else if ( ! wantedIdentity[i].toString().equals( currentIdentity[i].toString() ) ) {
                    return false;
                }
            }
            return true;
        }


        /**
         * @see org.exolab.castor.persist.spi.PersistenceQuery#fetch(org.exolab.castor.persist.ProposedObject, java.lang.Object)
         */
        public Object fetch(ProposedObject proposedObject, Object identity)
        throws ObjectNotFoundException, PersistenceException {
            Object[] fields = proposedObject.getFields();
            
            // Fill the given fields[] with the "cached" stuff from our _fields[] .
            for( int i = 0; i < _fields.length; i++ ) {
                fields[i] = _fields[i];
            }
            
            return null;
        }


        private Object fetchRaw( Object identity ) throws ObjectNotFoundException, PersistenceException
        {
            // maybe we can optimize a little bit here when we have time.
            // Instead of creating new Object[] and ArrayList for each 
            // "multi field" each fetchRaw is called, we might reuse them.

            SQLEngine oldEngine = null;
            int originalFieldNumber = _requestedEngine._fields.length;
            if (_requestedEngine.getDescriptor().isExtended()) {
                Collection extendingClassDescriptors = _requestedEngine.getDescriptor().getExtendedBy();
                int numberOfExtendLevels = _requestedEngine.numberOfExtendingClassDescriptors(_requestedEngine.getDescriptor());
                JDOClassDescriptor leafDescriptor = null;
                Object[] returnValues = null;
                try {
                    returnValues =_requestedEngine.calculateNumberOfFields(extendingClassDescriptors, _requestedEngine._ids.length, _requestedEngine._fields.length, numberOfExtendLevels, this._rs);
                } catch (SQLException e) {
                    _log.error ("Problem calculating number of concrete fields.", e);
                    throw new PersistenceException ("Problem calculating number of concrete fields.", e);
                }
                
                leafDescriptor = (JDOClassDescriptor) returnValues[0];
                
                if (leafDescriptor != null) {
                    if (!leafDescriptor.getJavaClass().getName().equals(_requestedEngine.getDescriptor().getJavaClass().getName())) {
                        originalFieldNumber = ((Integer) returnValues[1]).intValue();
                        
                        Persistence newEngine =null;
                        try {
                            newEngine = _requestedEngine._factory.getPersistence(leafDescriptor);
                        } catch (MappingException e) {
                            _log.error ("Problem obtaining persistence engine for " + leafDescriptor.getJavaClass().getName(), e);
                            throw new PersistenceException ("Problem obtaining persistence engine for " + leafDescriptor.getJavaClass().getName(), e);
                        } 
                        _engine = (SQLEngine) newEngine;
                    }
                }
            }
            
            _fields = new Object[originalFieldNumber];

            // It would prove a little difficult to fetch if we don't have any rows with data left :-)
            if ( _resultSetDone )
                return null;

            Object   stamp = null;

            Object[] wantedIdentity;
            Object[] currentIdentity;

            try {
                // If identity given, we want only to load data for this object.
                // Otherwise we just load the identity from the current row.
                if( identity != null )
                    wantedIdentity = identityToSQL( identity );
                else
                    wantedIdentity = loadSQLIdentity();

                // Load first (and perhaps only) row of object data from _rs into <_fields> array.
                // As we assume that we have called fetch() immediatly after nextIdentity(),
                // we can be sure that it belongs to the object we want. This is probably not the
                // safest programming style, but has to suffice currently :-)
                loadRow(_fields, originalFieldNumber, true);

                // We move forward in the ResultSet, until we see another identity or run out of rows.
                while ( _rs.next() ) {

                    // Load identity from current row.
                    currentIdentity = loadSQLIdentity();

                    // Compare with wantedIdentity and determine if it is a new one.
                    if( identitiesEqual( wantedIdentity, currentIdentity ) ) {

                        // Load next row of object data from _rs into <_fields> array.
                        loadRow(_fields, originalFieldNumber, false);

                    } else {
                        // We are done with all the rows for our obj. and still have rows left.
                        _lastIdentity = currentIdentity;

                        // As stamp is never set, this function always returns null ... ???
                        // (Don't ask me, it was like that before I modified the code! :-)
                        return stamp;
                    }
                }

                // We are done with all the rows for our obj. and don't have any rows left.
                _resultSetDone = true;
                _lastIdentity = null;
            } catch ( SQLException except ) {
                throw new PersistenceException( Messages.format("persist.nested", except), except );
            }

            return null;
        }
    }
}
