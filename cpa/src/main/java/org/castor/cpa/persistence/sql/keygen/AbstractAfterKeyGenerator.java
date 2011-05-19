/*
 * Copyright 2009 Ahmad Hassan, Ralf Joachim
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
 */
package org.castor.cpa.persistence.sql.keygen;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.core.exceptions.CastorIllegalStateException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Identity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLFieldInfo;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.castor.core.util.AbstractProperties;
import org.castor.core.util.Messages;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.cpa.persistence.sql.engine.CastorStatement;
import org.castor.cpa.persistence.sql.engine.SQLEngine;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.exolab.castor.mapping.ClassDescriptor;

/**
 * Abstract class that implements the KeyGenerator interface for AFTER_INSERT style. The key
 * generator is used for producing identities for objects before they are created in the
 * database.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public abstract class AbstractAfterKeyGenerator extends AbstractKeyGenerator {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractAfterKeyGenerator.class);
    
    /** SQL engine for all persistence operations at entities of the type this
     * class is responsible for. Holds all required information of the entity type. */
    private SQLEngine _engine;
    
    /** Represents the engine type obtained from clas descriptor. */
    private String _engineType = null;
    
    /** Boolean value specifies the Property whether JDBC 3.0-specific features 
     *  should be used. */
    private final boolean _useJDBC30;
    
    /** Name of the Table extracted from Class descriptor. */
    private String _mapTo;
    
    /** Use a database trigger to generate key. */
    private boolean _triggerPresent;
    
    /** Name of the Sequence. */
    private String _seqName;
    
    /** Variable to store built insert class hierarchy. */
    private Insert _insert;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param params Parameters for key generator.
     */
    public AbstractAfterKeyGenerator(final Properties params) {
        AbstractProperties properties = CPAProperties.getInstance();
        _useJDBC30 = properties.getBoolean(CPAProperties.USE_JDBC30, false);
        
        if (params != null) {
            _triggerPresent = "true".equals(params.getProperty("trigger", "false"));
            _seqName = params.getProperty("sequence", "{0}_seq");
        }
    }

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public final void buildStatement(final SQLEngine engine) {
        _engine = engine;
        ClassDescriptor clsDesc = _engine.getDescriptor();
        _engineType = clsDesc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(clsDesc).getTableName();
        _insert = new Insert(_mapTo);
        
        // is it right to omit all identities in this case?
        // maybe we should support to define a separat keygen
        // for every identity or complex/custom keygen that
        // supports multiple columns.
        
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].isStore()) {
                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    String name = columns[j].getName();
                    _insert.addAssignment(new Column(name), new Parameter(name));
                }
            }
        }    

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        if (_seqName != null && !_triggerPresent) {
            _insert.addAssignment(new Column(ids[0].getName()), new NextVal(_seqName));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public final Object executeStatement(final Database database, final CastorConnection conn, 
            final Identity identity, final ProposedEntity entity) throws PersistenceException {
        Identity internalIdentity = identity;
        
        CastorStatement stmt = conn.createStatement();
        try {
            if ((internalIdentity == null) && _useJDBC30) {
                Field field = Statement.class.getField("RETURN_GENERATED_KEYS");

                stmt.prepareStatement(_insert);
                String statement = stmt.toString();
                Integer rgk = (Integer) field.get(statement);

                Class[] types = new Class[] {String.class, int.class};
                Object[] args = new Object[] {statement, rgk};
                Method method = Connection.class.getMethod("prepareStatement", types);

                stmt.setStatement((PreparedStatement) method.invoke(conn.getConnection(), args));
            } else {
                stmt.prepareStatement(_insert);
            }
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _engineType, stmt.toString()));
            }

            bindFields(entity, stmt);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _engineType, stmt.toString()));
            }

            stmt.executeUpdate();

            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();

            if (internalIdentity == null) {
                if (_useJDBC30) {
                    // use key returned by INSERT statement.
                    Class cls = PreparedStatement.class;
                    Method method = cls.getMethod("getGeneratedKeys", (Class[]) null);
                    
                    ResultSet keySet 
                    = (ResultSet) method.invoke(stmt.getStatement(), (Object[]) null);
                    
                    int i = 1;
                    int sqlType;
                    List<Object> keys = new ArrayList<Object>();
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
                } else {
                    // generate key after INSERT.
                    internalIdentity = generateKey(database, conn);
                }
            }

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _engineType,  stmt.toString()), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } catch (NoSuchMethodException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (NoSuchFieldException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new CastorIllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new CastorIllegalStateException(ex);
        } finally {
            //close statement
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }
    
    /**
     * Binds parameters values to the PreparedStatement.
     * 
     * @param entity Entity instance from which field values to be fetached to
     *               bind with sql insert statement.
     * @param stmt CastorStatement containing Connection and PersistenceFactory.
     * @throws SQLException If a database access error occurs.
     * @throws PersistenceException If identity size mismatches.
     */
    private void bindFields(final ProposedEntity entity, final CastorStatement stmt)
    throws SQLException, PersistenceException {
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            SQLColumnInfo[] columns = fields[i].getColumnInfo();
            if (fields[i].isStore()) {
                Object value = entity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        stmt.bindParameter(columns[j].getName(), null, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity identity = (Identity) value;
                    if (identity.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    for (int j = 0; j < columns.length; j++) {
                        stmt.bindParameter(columns[j].getName(), columns[j].toSQL(identity.get(j)),
                                columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    stmt.bindParameter(columns[0].getName(), columns[0].toSQL(value),
                            columns[0].getSqlType());
                }
            }
        }
    }
    
    /**
     * Generates the key.
     * 
     * @param database Particular Database instance.
     * @param conn CastorConnection holding connection and PersistenceFactory to be used to create
     *        statement.
     * @return Identity that is generated.
     * @throws PersistenceException If fails to Generate key.
     */
    private Identity generateKey(final Database database, final CastorConnection conn)
    throws PersistenceException {
        SQLColumnInfo id = _engine.getColumnInfoForIdentities()[0];

        // TODO [SMH]: Change KeyGenerator.isInSameConnection to KeyGenerator.useSeparateConnection?
        // TODO [SMH]: Move "if (_keyGen.isInSameConnection() == false)"
        //                 out of SQLEngine and into key-generator?
        Connection connection = conn.getConnection();
        if (!this.isInSameConnection()) {
        connection = getSeparateConnection(database);
        }

        try {
            Object identity;
            synchronized (connection) {
            identity = this.generateKey(connection, _mapTo, id.getName());
            }

            // TODO [SMH]: Move "if (identity == null)" into keygenerator.
            if (identity == null) {
            throw new PersistenceException(
            Messages.format("persist.noIdentity", _engineType));
            }

            return new Identity(id.toJava(identity));
        } finally {
            if (!this.isInSameConnection()) {
                closeSeparateConnection(connection);
            }
        }
    }
    
    //-----------------------------------------------------------------------------------    
}

