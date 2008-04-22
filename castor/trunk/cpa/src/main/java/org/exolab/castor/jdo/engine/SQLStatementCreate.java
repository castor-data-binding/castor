/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.castor.jdo.engine.ConnectionFactory;
import org.castor.jdo.engine.DatabaseRegistry;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.castor.util.Messages;
import org.exolab.castor.core.exceptions.CastorIllegalStateException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;

public class SQLStatementCreate {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementCreate.class);
    
    private final SQLEngine _engine;
    
    private final PersistenceFactory _factory;
    
    private final String _type;

    private final String _mapTo;

    private KeyGenerator _keyGen;

    private final boolean _useJDBC30;

    private SQLStatementLookup _lookupStatement;

    private String _statement;

    public SQLStatementCreate(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = engine.getDescriptor().getTableName();
        
        _keyGen = getKeyGenerator(engine, factory);

        Configuration config = CPAConfiguration.getInstance();
        _useJDBC30 = config.getBoolean(CPAConfiguration.USE_JDBC30, false);
        
        _lookupStatement = new SQLStatementLookup(engine, factory);
        
        buildStatement();
    }
    
    private KeyGenerator getKeyGenerator(final SQLEngine engine,
            final PersistenceFactory factory) throws MappingException {
        KeyGenerator keyGen = null;
        if (engine.getDescriptor().getExtends() == null) {
            KeyGeneratorDescriptor keyGenDesc = engine.getDescriptor().getKeyGeneratorDescriptor();
            if (keyGenDesc != null) {
                FieldDescriptor fldDesc = engine.getDescriptor().getIdentity();
                int[] tempType = ((JDOFieldDescriptor) fldDesc).getSQLType();
                keyGen = keyGenDesc.getKeyGeneratorRegistry().getKeyGenerator(
                        factory, keyGenDesc, (tempType == null) ? 0 : tempType[0]);

                // Does the key generator support the sql type specified in the mapping?
                keyGen.supportsSqlType(tempType[0]);
            }
        }
        return keyGen;
    }
    
    private void buildStatement() {
        if (_keyGen == null) {
            buildStatementWithIdentities();
        } else if (_keyGen.getStyle() == KeyGenerator.BEFORE_INSERT) {
            buildStatementWithIdentities();
        } else if (_keyGen.getStyle() == KeyGenerator.DURING_INSERT) {
            buildStatementWithoutIdentities();
            try {
                SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
                _statement = _keyGen.patchSQL(_statement, ids[0].getName());
                _statement = "{call " + _statement + "}";
            } catch (MappingException except)  {
                LOG.fatal(except);
                
                // proceed without this stupid key generator
                _keyGen = null;
                buildStatementWithIdentities();
            }
        } else if (_keyGen.getStyle() == KeyGenerator.AFTER_INSERT) {
            buildStatementWithoutIdentities();
            try {
                SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
                _statement = _keyGen.patchSQL(_statement, ids[0].getName());
            } catch (MappingException except)  {
                LOG.fatal(except);
                
                // proceed without this stupid key generator
                _keyGen = null;
                buildStatementWithIdentities();
            }
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.creating", _type, _statement));
        }
    }
    
    private void buildStatementWithIdentities() {
        StringBuffer insert = new StringBuffer();
        insert.append("INSERT INTO ");
        insert.append(_factory.quoteName(_mapTo));
        insert.append(" (");
        
        StringBuffer values = new StringBuffer();
        values.append(" VALUES (");
        
        int count = 0;

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        for (int i = 0; i < ids.length; i++) {
            if (count > 0) {
                insert.append(',');
                values.append(',');
            }
            insert.append(_factory.quoteName(ids[i].getName()));
            values.append('?');
            ++count;
        }
        
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].isStore()) {
                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    if (count > 0) {
                        insert.append(',');
                        values.append(',');
                    }
                    insert.append(_factory.quoteName(columns[j].getName()));
                    values.append('?');
                    ++count;
                }
            }
        }
        
        insert.append(')');
        values.append(')');
        
        _statement = insert.append(values).toString();
    }
    
    private void buildStatementWithoutIdentities() {
        StringBuffer insert = new StringBuffer();
        insert.append("INSERT INTO ");
        insert.append(_factory.quoteName(_mapTo));
        insert.append(" (");
        
        StringBuffer values = new StringBuffer();
        values.append(" VALUES (");
        
        int count = 0;

        // is it right to omit all identities in this case?
        // maybe we should support to define a separat keygen
        // for every identity or complex/custom keygen that
        // supports multiple columns.
        
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].isStore()) {
                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    if (count > 0) {
                        insert.append(',');
                        values.append(',');
                    }
                    insert.append(_factory.quoteName(columns[j].getName()));
                    values.append('?');
                    ++count;
                }
            }
        }
        
        // it is possible to have no fields in INSERT statement
        if (count == 0) {
            // is it neccessary to omit "()" after table name in case
            // the table holds only identities? maybe this depends on
            // the database engine.
            
            // cut " ("
            insert.setLength(insert.length() - 2);
        } else {
            insert.append(')');
        }
        values.append(')');
        
        _statement = insert.append(values).toString();
    }

    public Object executeStatement(final Database database, final Connection conn,
            final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        if (_keyGen == null) {
            return executeStatementNoKeygen(database, conn, identity, entity);
        } else if (_keyGen.getStyle() == KeyGenerator.BEFORE_INSERT) {
            return executeStatementBeforeInsert(database, conn, identity, entity);
        } else if (_keyGen.getStyle() == KeyGenerator.DURING_INSERT) {
            return executeStatementDuringInsert(database, conn, identity, entity);
        } else if (_keyGen.getStyle() == KeyGenerator.AFTER_INSERT) {
            return executeStatementAfterInsert(database, conn, identity, entity);
        }
        
        throw new PersistenceException("unknown key generator");
    }

    public Object executeStatementNoKeygen(final Database database,
            final Connection conn, final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();
        if ((extended == null) && (internalIdentity == null)) {
            throw new PersistenceException(Messages.format("persist.noIdentity", _type));
        }

        PreparedStatement stmt = null;
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if ((extended != null) && !extended.getDescriptor().getTableName().equals(_mapTo)) {
                internalIdentity = extended.create(database, conn, entity, internalIdentity);
            }
            
            // we only need to care on JDBC 3.0 at after INSERT.
            stmt = conn.prepareStatement(_statement);
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }
            
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            if (internalIdentity.size() != ids.length) {
                throw new PersistenceException("Size of identity field mismatched!");
            }

            // must remember that SQL column index is base one.
            int count = 1;
            for (int i = 0; i < ids.length; i++) {
                stmt.setObject(count++, ids[i].toSQL(internalIdentity.get(i)));
            }

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            count = bindFields(entity, stmt, count);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            stmt.executeUpdate();

            stmt.close();

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _type,  _statement), except);

            Boolean isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, internalIdentity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            _lookupStatement.executeStatement(conn, internalIdentity);

            try {
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }
    }

    public Object executeStatementBeforeInsert(final Database database,
            final Connection conn, final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();

        PreparedStatement stmt = null;
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if ((extended != null) && !extended.getDescriptor().getTableName().equals(_mapTo)) {
                internalIdentity = extended.create(database, conn, entity, internalIdentity);
            }
            
            // generate key before INSERT.
            internalIdentity = generateKey(database, conn, null);

            // we only need to care on JDBC 3.0 at after INSERT.
            stmt = conn.prepareStatement(_statement);
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }
            
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            if (internalIdentity.size() != ids.length) {
                throw new PersistenceException("Size of identity field mismatched!");
            }

            // must remember that SQL column index is base one.
            int count = 1;
            for (int i = 0; i < ids.length; i++) {
                stmt.setObject(count++, ids[i].toSQL(internalIdentity.get(i)));
            }

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            count = bindFields(entity, stmt, count);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            stmt.executeUpdate();

            stmt.close();

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _type,  _statement), except);

            Boolean isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, internalIdentity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            _lookupStatement.executeStatement(conn, internalIdentity);

            try {
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }
    }

    public Object executeStatementDuringInsert(final Database database,
            final Connection conn, final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();

        PreparedStatement stmt = null;
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if ((extended != null) && !extended.getDescriptor().getTableName().equals(_mapTo)) {
                internalIdentity = extended.create(database, conn, entity, internalIdentity);
            }
            
            stmt = conn.prepareCall(_statement);
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }
            
            // must remember that SQL column index is base one.
            int count = 1;
            count = bindFields(entity, stmt, count);

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();

            // generate key during INSERT.
            CallableStatement cstmt = (CallableStatement) stmt;

            int sqlType = ids[0].getSqlType();
            cstmt.registerOutParameter(count, sqlType);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _type, cstmt.toString()));
            }
            
            cstmt.execute();

            // first skip all results "for maximum portability"
            // as proposed in CallableStatement javadocs.
            while (cstmt.getMoreResults() || (cstmt.getUpdateCount() != -1)) {
                // no code to execute
            }

            // identity is returned in the last parameter.
            // workaround for INTEGER type in Oracle getObject returns BigDecimal.
            Object temp;
            if (sqlType == java.sql.Types.INTEGER) {
                temp = new Integer(cstmt.getInt(count));
            } else {
                temp = cstmt.getObject(count);
            }
            internalIdentity = new Identity(ids[0].toJava(temp));

            stmt.close();

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _type,  _statement), except);

            Boolean isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, internalIdentity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            _lookupStatement.executeStatement(conn, internalIdentity);

            try {
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }
    }

    public Object executeStatementAfterInsert(final Database database,
            final Connection conn, final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();

        PreparedStatement stmt = null;
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if ((extended != null) && !extended.getDescriptor().getTableName().equals(_mapTo)) {
                internalIdentity = extended.create(database, conn, entity, internalIdentity);
            }
            
            if ((internalIdentity == null) && _useJDBC30) {
                Field field = Statement.class.getField("RETURN_GENERATED_KEYS");
                Integer rgk = (Integer) field.get(_statement);
                
                Class[] types = new Class[] {String.class, int.class};
                Object[] args = new Object[] {_statement, rgk};
                Method method = Connection.class.getMethod("prepareStatement", types);
                stmt = (PreparedStatement) method.invoke(conn, args);
                    
                // stmt = conn.prepareStatement(_statement, Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = conn.prepareStatement(_statement);
            }
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }
            
            // must remember that SQL column index is base one.
            int count = 1;
            count = bindFields(entity, stmt, count);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            stmt.executeUpdate();

            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();

            if (internalIdentity == null) {
                if (_useJDBC30) {
                    // use key returned by INSERT statement.
                    Class cls = PreparedStatement.class;
                    Method method = cls.getMethod("getGeneratedKeys", (Class[]) null);
                    ResultSet keySet = (ResultSet) method.invoke(stmt, (Object[]) null);
                    // ResultSet keySet = stmt.getGeneratedKeys();
                    
                    int i = 1;
                    int sqlType;
                    List keys = new ArrayList();
                    while (keySet.next()) {
                        sqlType = ids[i - 1].getSqlType();
                        Object temp;
                        if (sqlType == java.sql.Types.INTEGER) {
                            temp = new Integer(keySet.getInt(i));
                        } else if (sqlType == java.sql.Types.NUMERIC) {
                            temp = keySet.getBigDecimal(i);
                        } else {
                            temp = keySet.getObject(i);
                        }

                        keys.add(ids[i - 1].toJava(temp));
                        i++;
                    }
                    internalIdentity = new Identity(keys.toArray());

                    stmt.close();
                } else {
                    stmt.close();
                    
                    // generate key after INSERT.
                    internalIdentity = generateKey(database, conn, stmt);
                }
            }

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _type,  _statement), except);

            Boolean isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, internalIdentity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            _lookupStatement.executeStatement(conn, internalIdentity);

            try {
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } catch (NoSuchMethodException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (NoSuchFieldException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new CastorIllegalStateException(ex);
        }
    }

    /**
     * Use the specified keygenerator to generate a key for this
     * row of object.
     *
     * Result key will be in java type.
     * @param database Database instance
     * @param conn JDBC Connection instance
     * @param stmt JDBC Statement instance
     * @return The generated key
     * @throws PersistenceException If no key can be generated 
     */
    private Identity generateKey(final Database database, final Connection conn,
                               final PreparedStatement stmt)
    throws PersistenceException {
        SQLColumnInfo id = _engine.getColumnInfoForIdentities()[0];

        // TODO [SMH]: Change KeyGenerator.isInSameConnection to KeyGenerator.useSeparateConnection?
        // TODO [SMH]: Move "if (_keyGen.isInSameConnection() == false)"
        //             out of SQLEngine and into key-generator?
        Connection connection = conn;
        if (!_keyGen.isInSameConnection()) {
            connection = getSeparateConnection(database);
        }

        Properties prop = null;
        if (stmt != null) {
            prop = new Properties();
            prop.put("insertStatement", stmt);
        }

        try {
            Object identity;
            synchronized (connection) {
                identity = _keyGen.generateKey(connection, _mapTo, id.getName(), prop);
            }

            // TODO [SMH]: Move "if (identity == null)" into keygenerator.
            if (identity == null) {
                throw new PersistenceException(
                    Messages.format("persist.noIdentity", _type));
            }

            return new Identity(id.toJava(identity));
        } finally {
            if (!_keyGen.isInSameConnection()) {
                closeSeparateConnection(connection);
            }
        }
    }

    /**
     * Bind non-identity fields to prepared statement.
     * 
     * @param values Field to bind.
     * @param stmt PreparedStatement instance.
     * @param internalCount Field counter
     * @throws SQLException If the fields cannot be bound successfully.
     * @throws PersistenceException
     */
    private int bindFields(final ProposedEntity entity, final PreparedStatement stmt,
            final int count) throws SQLException, PersistenceException {
        int internalCount = count;
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            SQLColumnInfo[] columns = fields[i].getColumnInfo();
            if (fields[i].isStore()) {
                Object value = entity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        stmt.setNull(internalCount++, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity identity = (Identity) value;
                    if (identity.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    for (int j = 0; j < columns.length; j++) {
                        SQLTypeInfos.setValue(stmt, internalCount++,
                                columns[j].toSQL(identity.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    SQLTypeInfos.setValue(stmt, internalCount++, columns[0].toSQL(value),
                            columns[0].getSqlType());
                }
            }
        }
        return internalCount;
    }

    private Connection getSeparateConnection(final Database database)
    throws PersistenceException {
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

    private void closeSeparateConnection(final Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
