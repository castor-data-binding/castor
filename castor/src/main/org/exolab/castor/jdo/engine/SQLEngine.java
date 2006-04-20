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
import org.exolab.castor.jdo.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.persist.spi.*;
import org.exolab.castor.util.Messages;

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


    static private Hashtable    _separateConnections = new Hashtable();


    private String              _pkLookup;


    private String              _sqlCreate;


    private String              _sqlRemove;


    private String              _sqlStore;


    private String              _sqlStoreDirty;


    private String              _sqlLoad;


    private String              _sqlLoadLock;


    private FieldInfo[]         _fields;


    private ColumnInfo[]         _ids;


    private SQLEngine           _extends;


    private QueryExpression     _sqlFinder;


    private PersistenceFactory  _factory;


    private String              _stampField;


    private String              _type;


    private String              _mapTo;


    private String              _extTable;


    private JDOClassDescriptor   _clsDesc;


    private KeyGenerator         _keyGen;




    SQLEngine( JDOClassDescriptor clsDesc, PersistenceFactory factory, String stampField )
        throws MappingException {

        _clsDesc = clsDesc;
        _stampField = stampField;
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
                // a JDOFieldDescriptor or has a ClassDescriptor
                if ((fieldDescriptors[i] instanceof JDOFieldDescriptor) ||
                    (fieldDescriptors[i].getClassDescriptor() != null)) {
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

        try {
            buildSql();
            buildFinder( clsDesc );
        } catch ( QueryException except ) {
            except.printStackTrace();
            throw new MappingException( except );
        }
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

    private synchronized Connection getSeparateConnection(Database database)
        throws PersistenceException
    {
        Connection conn;
        DatabaseRegistry dr;

        dr=DatabaseRegistry.getDatabaseRegistry(database.getDatabaseName());

        conn = (Connection) _separateConnections.get( dr );
        if ( conn == null ) {
            try {
                conn = dr.createConnection();
                conn.setAutoCommit( false );
                _separateConnections.put( dr, conn );
            } catch ( SQLException except ) {
                throw new PersistenceException( Messages.message("persist.cannotCreateSeparateConn"), except );
            }
        } //else
        return conn;
    }

    /**
     * Used by {@link org.exolab.castor.jdo.OQLQuery} to retrieve the class descriptor.
     */
    public JDOClassDescriptor getDescriptor()
    {
        return _clsDesc;
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

        if ( accessMode == null )
            accessMode = _clsDesc.getAccessMode();
        sql = query.getStatement( accessMode == AccessMode.DbLocked);
        
        if(_log.isDebugEnabled()){
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

        if(_log.isDebugEnabled()){
            _log.debug( Messages.format( "jdo.spCall", spCall ) );
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
        // changes for the SQL Direct interface begins here
        if(spCall.startsWith("SQL")){
            sql =spCall.substring(4);
            return new SQLQuery( this, sql, types );
        } else{
            return ((BaseFactory) _factory).getCallQuery( spCall, types,_clsDesc.getJavaClass(), jdoFields, sqlTypes );
        }

       
    }

    public QueryExpression getQueryExpression() {
        return _factory.getQueryExpression();
    }

    public QueryExpression getFinder()
    {
        return (QueryExpression) _sqlFinder.clone();
    }

    private Object idToSQL( int index, Object object )
            throws PersistenceException {

        if ( object == null || _ids[index].convertFrom == null )
            return object;
        return _ids[index].convertFrom.convert( object, _ids[index].convertParam );
    }

    private Object toSQL( int field, int column, Object object )
            throws PersistenceException {

        ColumnInfo col = _fields[field].columns[column];
        if ( object == null || col.convertFrom == null )
            return object;
        return col.convertFrom.convert( object, col.convertParam );
    }

    private Object idToJava( int index, Object object )
            throws PersistenceException {

        if ( object == null || _ids[index].convertTo == null )
            return object;
        return _ids[index].convertTo.convert( object, _ids[index].convertParam );
    }

    private Object toJava( int field, int column, Object object )
            throws PersistenceException {

        ColumnInfo col = _fields[field].columns[column];
        if ( object == null || col.convertTo == null )
            return object;
        return col.convertTo.convert( object, col.convertParam );
    }

    /**
     * Use the specified keygenerator to gengerate a key for this
     * row of object.
     *
     * Result key will be in java type.
     */
    private Object generateKey( Database database, Object conn, PreparedStatement stmt ) throws PersistenceException {
        Object identity;
        Connection connection;
        Properties prop = null;

        if ( _keyGen.isInSameConnection() ) {
            connection = (Connection) conn;
        } else {
            connection = getSeparateConnection( database );
        }

        if (stmt != null) {
            prop = new Properties();
            prop.put("insertStatement", stmt);
        }
        synchronized (connection) {
            identity = _keyGen.generateKey( connection, _clsDesc.getTableName(),
                _ids[0].name, prop );
        }

        if ( identity == null )
            throw new PersistenceException( Messages.format("persist.noIdentity", _clsDesc.getJavaClass().getName()) );

        return idToJava( 0, identity );
    }


    public Object create( Database database, Object conn, Object[] fields, Object identity )
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
                _log.debug( Messages.format( "jdo.create", _sqlCreate ) );
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

            // Generate key during INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.DURING_INSERT ) {
                CallableStatement cstmt = (CallableStatement) stmt;
                int sqlType;

                sqlType = _ids[0].sqlType;
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
                identity = idToJava( 0, identity );
            } else
                stmt.executeUpdate();

            stmt.close();

            // Generate key after INSERT
            if ( _keyGen != null && _keyGen.getStyle() == KeyGenerator.AFTER_INSERT ) {
                identity = generateKey( database, conn, stmt );
            }

            return identity;

        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.storeFatal",  _type,  _sqlCreate ) );

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

                if(_log.isDebugEnabled()){
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
     * if isNull, replace next "=?" with " IS NULL",
     * otherwise skip next "=?",
     * move "pos" to the left.
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

        try {
            // Must store record in parent table first.
            // All other dependents are stored independently.
            if ( _extends != null )
                // | quick and very dirty hack to try to make multiple class on the same table work
                if ( !_extends._mapTo.equals( _mapTo ) )
                    _extends.store( conn, fields, identity, original, stamp );

            storeStatement = getStoreStatement( original );
            stmt = ( (Connection) conn ).prepareStatement( storeStatement );
            
            if(_log.isDebugEnabled()){
                _log.debug( Messages.format( "jdo.storing", _clsDesc.getJavaClass().getName(), storeStatement ) );
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

                for ( int i=0; i<_ids.length; i++ )
                    stmt.setObject( count++, idToSQL( i, id.get(i) ) );

            } else {
                if ( _ids.length != 1 )
                    throw new PersistenceException( "Complex field expected!" );

                stmt.setObject( count++, idToSQL( 0, identity ) );
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
                            }
                        } else {
                            if ( _fields[i].columns.length != 1 )
                                throw new PersistenceException( "Complex field expected! ");

                            SQLTypes.setObject( stmt, count++, toSQL( i, 0, original[i]), _fields[i].columns[0].sqlType );
                        }
                    }
                }
            }

            if ( stmt.executeUpdate() <= 0 ) { // SAP DB returns -1 here
                // If no update was performed, the object has been previously
                // removed from persistent storage or has been modified if
                // dirty checking. Determine which is which.
                stmt.close();
                if ( original != null ) {
                    stmt = ( (Connection) conn ).prepareStatement( /*_pkLookup*/_sqlLoad );
                    
                    if(_log.isDebugEnabled()){
                        _log.debug( Messages.format( "jdo.storing", _clsDesc.getJavaClass().getName(), 
                        							_sqlLoad ) );
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

                        stmt.close();
                        throw new ObjectModifiedException( Messages.format("persist.objectModified", _clsDesc.getJavaClass().getName(), identity ) );
                    }
                    stmt.close();
                }

                throw new ObjectDeletedException( Messages.format("persist.objectDeleted", _clsDesc.getJavaClass().getName(), identity) );
            }
            stmt.close();
            return null;
        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.storeFatal", _type,  storeStatement ) );

            try {
                // Close the insert/select statement
                if ( stmt != null )
                    stmt.close();
            } 
            catch ( SQLException except2 ) {
                _log.warn ("Problem closing JDBC statement", except2);
            }
            throw new PersistenceException( Messages.format("persist.nested", except), except );
        }
    }


    public void delete( Object conn, Object identity )
            throws PersistenceException {

        PreparedStatement stmt = null;

        try {
            stmt = ( (Connection) conn ).prepareStatement( _sqlRemove );
            
            if(_log.isDebugEnabled()){
                _log.debug( Messages.format( "jdo.removing", _sqlRemove ) );
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

            int result = stmt.executeUpdate();
            if ( result < 1 )
                throw new PersistenceException("Object to be deleted does not exist! "+ identity );

            // Must delete record in parent table last.
            // All other dependents have been deleted before.
            if ( _extends != null )
                _extends.delete( conn, identity );
        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.deleteFatal", _type, _sqlRemove ) );

            throw new PersistenceException( Messages.format("persist.nested", except), except );
        } finally {
            try {
                if ( stmt != null ) {
                    stmt.close();
                }
            } catch ( Exception e ) {
            	_log.warn ("Problem closing JDBC statement");
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
            
            if(_log.isDebugEnabled()){
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


    public Object load( Object conn, Object[] fields, Object identity, AccessMode accessMode )
            throws ObjectNotFoundException, PersistenceException {

        PreparedStatement stmt  = null;
        ResultSet         rs    = null;
        Object            stamp = null;
        boolean           notNull;

        try {
            stmt = ( (Connection) conn ).prepareStatement( ( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad );
            
            if (_log.isDebugEnabled()) {
            	String generatedSQL = ( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad;
            	_log.debug( Messages.format( "jdo.loading", _clsDesc.getJavaClass().getName(), generatedSQL ) );
            }
            
            int count = 1;
            // bind the identity of the preparedStatement
            if ( identity instanceof Complex ) {
                Complex id = (Complex) identity;
                if ( id.size() != _ids.length || _ids.length <= 1 )
                    throw new PersistenceException( "Size of complex field mismatched! expected: "+_ids.length+" found: "+id.size() );

                for ( int i=0; i<_ids.length; i++ )
                    stmt.setObject( count++, idToSQL( i, id.get(i) ) );

            } else {
                if ( _ids.length != 1 )
                    throw new PersistenceException( "Complex field expected!" );
                stmt.setObject( count++, idToSQL( 0, identity ) );
            }

            // query the object
            rs = stmt.executeQuery();
            if ( ! rs.next() )
                throw new ObjectNotFoundException( Messages.format("persist.objectNotFound", _clsDesc.getJavaClass().getName(), identity) );

            // Load all the fields of the object including one-one relations
            count = 1;
            Object[] temp = new Object[10]; // assume complex field max at 10
            for ( int i = 0 ; i < _fields.length ; ++i  ) {
                if ( !_fields[i].load )
                    continue;

                if ( !_fields[i].multi ) {
                    notNull = false;
                    if ( _fields[i].columns.length == 1 ) {
                        fields[i] = toJava( i, 0, SQLTypes.getObject( rs, count++, _fields[i].columns[0].sqlType ) );
                    } else {
                        for ( int j=0; j<_fields[i].columns.length; j++ ) {
                            temp[j] = toJava( i, j, SQLTypes.getObject( rs, count++, _fields[i].columns[j].sqlType ) );
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
                        temp[j] = toJava( i, j, SQLTypes.getObject( rs, count, _fields[i].columns[j].sqlType ) );
                        if ( temp[j] != null ) {
                            notNull = true;
                        }
                        count++;
                    }
                    if ( notNull ) {
                        if ( _fields[i].columns.length == 1 )
                            res.add( temp[0] );
                        else
                            res.add( new Complex( _fields[i].columns.length, temp ) );
                    }
                    fields[i] = res;
                }
            }

            while ( rs.next() ) {
                count = 1;
                for ( int i = 0; i < _fields.length ; ++i  ) {
                    if ( !_fields[i].load )
                        continue;

                    if ( _fields[i].multi ) {
                        ArrayList res = (ArrayList)fields[i];
                        notNull = false;
                        for ( int j=0; j<_fields[i].columns.length; j++ ) {
                            temp[j] = toJava( i, j, SQLTypes.getObject( rs, count, _fields[i].columns[j].sqlType ) );
                            if ( temp[j] != null ) {
                                notNull = true;
                            }
                            count++;
                        }
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
                        count += _fields[i].columns.length;
                    }
                }
            }
        } catch ( SQLException except ) {
            _log.fatal( Messages.format( "jdo.loadFatal", _type, (( accessMode == AccessMode.DbLocked ) ? _sqlLoadLock : _sqlLoad ) ) );

            throw new PersistenceException( Messages.format("persist.nested", except), except );
        } finally {
            try {
                if ( rs != null ) rs.close();
            } catch ( SQLException sqle ) {
                _log.warn("Problem closing JDBC Connection instance", sqle);
            }
            try {
                if ( stmt != null ) stmt.close();
            } catch ( SQLException sqle ) {
                _log.warn("Problem closing JDBC statement", sqle);
            }
        }
        return stamp;
    }

    private void buildSql() throws QueryException {

        StringBuffer         sql;
        int                  count;
        QueryExpression      query;
        String               wherePK;
        String               tableName;
        boolean              keyGened = false;


        tableName = _mapTo;
        query = _factory.getQueryExpression();

        // initalize lookup query
        for ( int i=0; i<_ids.length; i++ ) {
            query.addParameter( tableName, _ids[i].name, QueryExpression.OpEquals );
        }
        _pkLookup = query.getStatement( true );

        // create sql statements
        StringBuffer sb = new StringBuffer();
        sb.append( JDBCSyntax.Where );
        for ( int i=0; i<_ids.length; i++ ) {
            if ( i > 0 ) sb.append( " AND " );
            sb.append( _factory.quoteName( _ids[i].name ) );
            sb.append( QueryExpression.OpEquals );
            sb.append( JDBCSyntax.Parameter );
        }
        wherePK = sb.toString();

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
                buildSql();
                return;
            }
            if ( _keyGen.getStyle() == KeyGenerator.DURING_INSERT )
                _sqlCreate = "{call " + _sqlCreate + "}";
        }

        if(_log.isDebugEnabled()){
            _log.debug( Messages.format( "jdo.creating", _type, _sqlCreate ) );
        }
        
        sql = new StringBuffer( "DELETE FROM " ).append( _factory.quoteName( tableName ) );
        sql.append( wherePK );
        _sqlRemove = sql.toString();
        
        if(_log.isDebugEnabled()){
            _log.debug( Messages.format( "jdo.removing", _type, _sqlRemove ) );
        }
        
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
        sql.append( wherePK );
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
        
        if(_log.isDebugEnabled()){
            _log.debug( Messages.format( "jdo.updating", _type, _sqlStoreDirty ) );
        }
    }


    private void buildFinder( JDOClassDescriptor clsDesc ) throws MappingException, QueryException {
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
        for ( int i=0; i<_ids.length; i++ ) {
            find.addColumn( _mapTo, idnames[i] );
        }

        // join all the related/depended table
        Vector joinTables = new Vector();
        for ( int i=0; i<_fields.length; i++ ) {
            String alias = _fields[i].tableName;
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
        _sqlLoad = expr.getStatement( false );
        _sqlLoadLock = expr.getStatement( true );

        _sqlFinder = find;

        if(_log.isDebugEnabled()){
            _log.debug( Messages.format( "jdo.loading", _type, _sqlLoad ) );
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
        //!TY fix this. joinFields should be in FieldInfo, not ColumnInfo

        //final boolean foreign;

        ColumnInfo[] columns;

        FieldInfo( JDOClassDescriptor clsDesc, FieldDescriptor fieldDesc, String classTable, boolean ext )
                throws MappingException{

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

    static final class ColumnInfo {

        final String  name;

        final int sqlType;

        final TypeConvertor convertTo;

        final TypeConvertor convertFrom;

        final String convertParam;

        ColumnInfo( String name, int type, TypeConvertor convertTo,
                TypeConvertor convertFrom, String convertParam ) {
            this.name = name;
            this.sqlType = type;
            this.convertTo = convertTo;
            this.convertFrom = convertFrom;
            this.convertParam = convertParam;
        }
    }

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


        private Object[]        _fields;


        SQLQuery( SQLEngine engine, String sql, Class[] types )
        {
            _engine = engine;
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
         * use the jdbc 2.0 method to move to an absolute position in the
         * resultset.
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
               throw new PersistenceException(e.getMessage());
            }
            return retval;
         }

         /**
          * Uses the underlying db's cursors to most to the last row in the
          * result set, get the row number via getRow(), then move back to
          * where ever the user was positioned in the resultset.
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
            _lastIdentity = null;
            try {
                if (scrollable)
                {
                  _stmt = ( (Connection) conn ).prepareStatement( _sql, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY );
                }
                else
                {
                _stmt = ( (Connection) conn ).prepareStatement( _sql );
                }

                for ( int i = 0 ; i < _values.length ; ++i ) {
                    _stmt.setObject( i + 1, _values[ i ] );
                    _values[ i ] = null;
                }
                
                if(_log.isDebugEnabled()){
                    _log.debug (Messages.format ("jdo.executingSql", _sql));
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


        private Object loadSingleField( int i, int count ) throws SQLException, PersistenceException
        {
            Object[] temp = new Object[10];  // bad practice, assume complex field smaller than 10
            boolean notNull = false;
            Object   field;

            if ( _engine._fields[i].columns.length == 1 ) {
                field = _engine.toJava( i, 0, SQLTypes.getObject( _rs, count++,
                        _engine._fields[i].columns[0].sqlType ) );
            } else {
                for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                    temp[j] = _engine.toJava( i, j, SQLTypes.getObject( _rs, count++,
                              _engine._fields[i].columns[j].sqlType ) );
                    if ( temp[j] != null ) {
                        notNull = true;
                    }
                }
                if ( notNull )
                    field = new Complex( _engine._fields[i].columns.length, temp );
                else
                    field = null;
            }
            return field;
        }


        private Object loadMultiField( int i, int count, Object field ) throws SQLException, PersistenceException
        {
            Object[]  temp = new Object[10];  // bad practice, assume complex field smaller than 10
            boolean notNull = false;
            ArrayList res;

            if( field == null )
                res = new ArrayList();
            else
                res = (ArrayList) field;

            for ( int j=0; j<_engine._fields[i].columns.length; j++ ) {
                temp[j] = _engine.toJava( i, j,
                          SQLTypes.getObject( _rs, count, _engine._fields[i].columns[j].sqlType ) );
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
            return res;
        }


        private int loadRow( Object[] fields, boolean isFirst ) throws SQLException, PersistenceException
        {
            int count = _engine._ids.length + 1;

            // Load all the fields.
            for ( int i = 0 ; i < _engine._fields.length ; ++i  ) {
                if ( !_engine._fields[i].load )
                    continue;

                if ( _engine._fields[i].multi ) {
                    fields[i] = loadMultiField( i, count, fields[i] );
                } else if( isFirst ) {
                    // Non-multi fields have to be done one only once, so this is skipped
                    // if we have already read the first row.
                    fields[i] = loadSingleField( i, count );
                }
                count += _engine._fields[i].columns.length;
            }
            return count;
        }


        private Object[] loadSQLIdentity() throws SQLException, PersistenceException
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


        // Fill the given fields[] with the "cached" stuff from our _fields[] .
        public Object fetch( Object[] fields, Object identity ) throws ObjectNotFoundException, PersistenceException
        {
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
            _fields = new Object[_engine._fields.length];

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
                loadRow( _fields, true );

                // We move forward in the ResultSet, until we see another identity or run out of rows.
                while ( _rs.next() ) {

                    // Load identity from current row.
                    currentIdentity = loadSQLIdentity();

                    // Compare with wantedIdentity and determine if it is a new one.
                    if( identitiesEqual( wantedIdentity, currentIdentity ) ) {

                        // Load next row of object data from _rs into <_fields> array.
                        loadRow( _fields, false );

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
