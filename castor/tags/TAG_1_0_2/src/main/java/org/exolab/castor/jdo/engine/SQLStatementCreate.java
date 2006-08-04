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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.jdo.engine.ConnectionFactory;
import org.castor.jdo.engine.DatabaseRegistry;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;
import org.castor.util.Messages;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
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

        Configuration config = Configuration.getInstance();
        _useJDBC30 = config.getProperty(ConfigKeys.USE_JDBC30, false);
        
        _lookupStatement = new SQLStatementLookup(engine, factory);
        
        buildStatement();
    }
    
    private KeyGenerator getKeyGenerator(final SQLEngine engine,
            final PersistenceFactory factory) throws MappingException {
        KeyGenerator keyGen = null;
        if (engine.getDescriptor().getExtends() == null) {
            KeyGeneratorDescriptor keyGenDesc = engine.getDescriptor().getKeyGeneratorDescriptor();
            if (keyGenDesc != null) {
                int[] tempType = ((JDOFieldDescriptor) engine.getDescriptor().getIdentity()).getSQLType();
                keyGen = keyGenDesc.getKeyGeneratorRegistry().getKeyGenerator(
                        factory, keyGenDesc, (tempType == null) ? 0 : tempType[0]);

                // Does the key generator support the sql type specified in the mapping?
                keyGen.supportsSqlType(tempType[0]);
            }
        }
        return keyGen;
    }
    
    private void buildStatement() {
        // Create statement to insert a new row into the table
        // using the specified primary key if one is required
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        sql.append(_factory.quoteName(_mapTo)).append(" (");
        
        int count = 0;
        boolean keyGened = false;

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        for (int i = 0; i < ids.length; i++) {
            if ((_keyGen == null) || (_keyGen.getStyle() == KeyGenerator.BEFORE_INSERT)) {
                if (count > 0) { sql.append(','); }
                keyGened = true;
                sql.append(_factory.quoteName(ids[i].getName()));
                ++count;
            }
        }
        
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0 ; i < fields.length; ++i) {
            if (fields[i].isStore()) {
                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    if (count > 0) { sql.append(','); }
                    sql.append(_factory.quoteName(columns[j].getName()));
                    ++count;
                }
            }
        }
        
        // it is possible to have no fields in INSERT statement:
        // only the primary key field in the table,
        // with KeyGenerator DURING_INSERT or BEFORE_INSERT
        if (count == 0) {
            sql.setLength(sql.length() - 2); // cut " ("
        } else {
            sql.append(")");
        }
        sql.append(" VALUES (");
        for (int i = 0 ; i < count; ++i) {
            if (i > 0) { sql.append(','); }
            sql.append('?');
        }
        sql.append(')');
        
        _statement = sql.toString();

        if (!keyGened) {
            try {
                _statement = _keyGen.patchSQL(_statement, ids[0].getName());
            } catch (MappingException except)  {
                LOG.fatal(except);
                // proceed without this stupid key generator
                _keyGen = null;
                buildStatement();
                return;
            }
            if (_keyGen.getStyle() == KeyGenerator.DURING_INSERT) {
                _statement = "{call " + _statement + "}";
            }
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.creating", _type, _statement));
        }
    }
    
    public Object executeStatement(final Database database, final Connection conn,
                                   Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        SQLEngine extended = _engine.getExtends();
        if ((extended == null) && (_keyGen == null) && (identity == null)) {
            throw new PersistenceException(Messages.format("persist.noIdentity", _type));
        }

        PreparedStatement stmt = null;
        try {
            // Must create record in the parent table first.
            // All other dependents are created afterwards.
            if (extended != null) {
                // | quick and very dirty hack to try to make multiple class on the same table work
                if (!extended.getDescriptor().getTableName().equals(_mapTo)) {
                    identity = extended.create(database, conn, entity, identity);
                }
            }
            
            if ((_keyGen != null) && (_keyGen.getStyle() == KeyGenerator.BEFORE_INSERT)) {
                // Generate key before INSERT
                // genKey return identity in JDO type
                identity = generateKey(database, conn, null);
            }

            if ((_keyGen != null) && (_keyGen.getStyle() == KeyGenerator.DURING_INSERT)) {
                stmt = conn.prepareCall(_statement);
            } else {
                
                if (_useJDBC30) {
                    stmt = conn.prepareStatement(_statement, Statement.RETURN_GENERATED_KEYS);
                } else {
                    stmt = conn.prepareStatement(_statement);
                }
            }
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }
            
            // Must remember that SQL column index is base one
            int count = 1;
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            if ((_keyGen == null) || (_keyGen.getStyle() == KeyGenerator.BEFORE_INSERT)) {
                if (identity.size() != ids.length) {
                    throw new PersistenceException("Size of identity field mismatched!");
                }
                for (int i = 0; i < ids.length; i++) {
                    stmt.setObject(count++, ids[i].toSQL(identity.get(i)));
                }
            }

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            count = bindFields(entity, stmt, count);

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _type, stmt.toString()));
            }

            // Generate key during INSERT
            if ((_keyGen != null) && (_keyGen.getStyle() == KeyGenerator.DURING_INSERT)) {
                CallableStatement cstmt = (CallableStatement) stmt;
                int sqlType;

                sqlType = ids[0].getSqlType();
                cstmt.registerOutParameter(count, sqlType);
                
                // [WG]: TODO: Verify that this really works !!!
                if (LOG.isDebugEnabled()) {
                      LOG.debug(Messages.format("jdo.creating", _type, cstmt.toString()));
                }
                
                cstmt.execute();

                // First skip all results "for maximum portability"
                // as proposed in CallableStatement javadocs.
                while (cstmt.getMoreResults() || (cstmt.getUpdateCount() != -1)) {
                    // no code to execute
                }

                // Identity is returned in the last parameter
                // Workaround: for INTEGER type in Oracle getObject returns BigDecimal
                Object temp;
                if (sqlType == java.sql.Types.INTEGER) {
                    temp = new Integer(cstmt.getInt(count));
                } else {
                    temp = cstmt.getObject(count);
                }
                identity = new Identity(ids[0].toJava(temp));
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.format("jdo.creating", _type, stmt.toString()));
                }

                stmt.executeUpdate();

                if (_useJDBC30 && identity == null) {
                    ResultSet keySet = stmt.getGeneratedKeys();
                    int i = 1;
                    int sqlType;
                    List keys = new ArrayList();
                    while (keySet.next()) {
                        sqlType = ids[i-1].getSqlType();
                        Object temp;
                        if (sqlType == java.sql.Types.INTEGER) {
                            temp = new Integer(keySet.getInt(i));
                        } else if (sqlType == java.sql.Types.NUMERIC) {
                            temp = keySet.getBigDecimal(i);
                        } else {
                            temp = keySet.getObject(i);
                        }

                        keys.add(ids[i-1].toJava(temp));
                        i++;
                    }
                    identity = new Identity(keys.toArray());
                }
            }

            stmt.close();

            // Generate key after INSERT
            if ((_keyGen != null) && (_keyGen.getStyle() == KeyGenerator.AFTER_INSERT)) {
                if (!_useJDBC30) {
                    identity = generateKey(database, conn, stmt);
                }
            }

            return identity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _type,  _statement), except);

            // [oleg] Check for duplicate key based on X/Open error code
            // Bad way: all validation exceptions are reported as DuplicateKey
            //if ( except.getSQLState() != null &&
            //     except.getSQLState().startsWith( "23" ) )
            //    throw new DuplicateIdentityException( _clsDesc.getJavaClass(), identity );

            // Good way: let PersistenceFactory try to determine
            Boolean isDupKey;

            isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format("persist.duplicateIdentity", _type, identity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }
            // else unknown, let's check directly.

            // [oleg] Check for duplicate key the old fashioned way,
            //        after the INSERT failed to prevent race conditions
            //        and optimize INSERT times
            _lookupStatement.executeStatement(conn, identity);

            try {
                // Close the insert/select statement
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
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
        // TODO [SMH]: Move "if (_keyGen.isInSameConnection() == false)" out of SQLEngine and into key-generator?
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
            if (_keyGen.isInSameConnection() == false) {
                closeSeparateConnection(connection);
            }
        }
    }

    /**
     * Bind non-identity fields to prepared statement.
     * 
     * @param values Field to bind.
     * @param stmt PreparedStatement instance.
     * @param count Field counter
     * @throws SQLException If the fields cannot be bound successfully.
     * @throws PersistenceException
     */
    private int bindFields(final ProposedEntity entity, final PreparedStatement stmt, int count) 
    throws SQLException, PersistenceException {
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0 ; i < fields.length ; ++i) {
            SQLColumnInfo[] columns = fields[i].getColumnInfo();
            if (fields[i].isStore()) {
                Object value = entity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        stmt.setNull(count++, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity identity = (Identity) value;
                    if (identity.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    for (int j = 0; j < columns.length; j++) {
                        SQLTypeInfos.setValue(stmt, count++,
                                columns[j].toSQL(identity.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    SQLTypeInfos.setValue(stmt, count++, columns[0].toSQL(value), columns[0].getSqlType());
                }
            }
        }
        return count;
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
