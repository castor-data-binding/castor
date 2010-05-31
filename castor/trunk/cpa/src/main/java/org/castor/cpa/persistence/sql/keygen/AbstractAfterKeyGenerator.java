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

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.exolab.castor.core.exceptions.CastorIllegalStateException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLFieldInfo;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.castor.core.util.AbstractProperties;
import org.castor.core.util.Messages;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.QueryContext;
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
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private PersistenceFactory _factory;
    
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
    
    /** QueryContext for SQL query building, specifying database specific quotations 
     *  and parameters binding. */
    private final QueryContext _ctx;
    
    /** Use a database trigger to generate key. */
    private boolean _triggerPresent;
    
    /** Name of the Sequence. */
    private String _seqName;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param factory  Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement
     * @param params Parameters for key generator.
     */
    public AbstractAfterKeyGenerator(final PersistenceFactory factory, final Properties params) {
        _factory = factory;
        _ctx = new QueryContext(_factory);
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
    public KeyGenerator buildStatement(final SQLEngine engine) {
        _engine = engine;
        ClassDescriptor clsDesc = _engine.getDescriptor();
        _engineType = clsDesc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(clsDesc).getTableName();
        Insert insert = new Insert(_mapTo);
        
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
                    insert.addAssignment(new Column(name), new Parameter(name));
                }
            }
        }    

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        if (_seqName != null && !_triggerPresent) {
            insert.addAssignment(new Column(ids[0].getName()), new NextVal(_seqName));
        }
        insert.toString(_ctx);

        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.creating", _engineType, _ctx.toString()));
        }
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object executeStatement(final Database database, final Connection conn, 
            final Identity identity, final ProposedEntity entity) throws PersistenceException {
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();
        PreparedStatement stmt = null;
        
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if (extended != null) {
                ClassDescriptor extDesc = extended.getDescriptor();
                if (!new ClassDescriptorJDONature(extDesc).getTableName().equals(_mapTo)) {
                    internalIdentity = extended.create(database, conn, entity, internalIdentity);
                }
            }
            
            if ((internalIdentity == null) && _useJDBC30) {
                Field field = Statement.class.getField("RETURN_GENERATED_KEYS");
                Integer rgk = (Integer) field.get(_ctx.toString());
                
                Class[] types = new Class[] {String.class, int.class};
                Object[] args = new Object[] {_ctx.toString(), rgk};
                Method method = Connection.class.getMethod("prepareStatement", types);
                stmt = (PreparedStatement) method.invoke(conn, args);                    
            } else {
                stmt = conn.prepareStatement(_ctx.toString());
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
                    ResultSet keySet = (ResultSet) method.invoke(stmt, (Object[]) null);
                    
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

                    stmt.close();
                } else {
                    // generate key after INSERT.
                    internalIdentity = generateKey(database, conn, stmt);

                    stmt.close();
                }
            }

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _engineType,  _ctx.toString()), except);

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
     * Binds parameters values to the PreparedStatement.
     * 
     * @param entity Entity instance from which field values to be fetached to
     *               bind with sql insert statement.
     * @param stmt PreparedStatement object containing sql staatement.
     * @throws SQLException If a database access error occurs.
     * @throws PersistenceException If identity size mismatches.
     */
    private void bindFields(final ProposedEntity entity, final PreparedStatement stmt
            ) throws SQLException, PersistenceException {
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            SQLColumnInfo[] columns = fields[i].getColumnInfo();
            if (fields[i].isStore()) {
                Object value = entity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        _ctx.bindParameter(stmt, columns[j].getName(), null, 
                                columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity identity = (Identity) value;
                    if (identity.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    for (int j = 0; j < columns.length; j++) {
                        _ctx.bindParameter(stmt, columns[j].getName(), 
                                columns[j].toSQL(identity.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    _ctx.bindParameter(stmt, columns[0].getName(), columns[0].toSQL(value), 
                            columns[0].getSqlType());
                }
            }
        }
    }
    
    /**
     * Generates the key.
     * 
     * @param database Particular Database instance.
     * @param conn An open JDBC Connection. 
     * @param stmt PreparedStatement containing the SQL statement.
     * @return Identity that is generated.
     * @throws PersistenceException If fails to Generate key.
     */
    private Identity generateKey(final Database database, final Connection conn,
            final PreparedStatement stmt)
    throws PersistenceException {
        SQLColumnInfo id = _engine.getColumnInfoForIdentities()[0];

        // TODO [SMH]: Change KeyGenerator.isInSameConnection to KeyGenerator.useSeparateConnection?
        // TODO [SMH]: Move "if (_keyGen.isInSameConnection() == false)"
        //                 out of SQLEngine and into key-generator?
        Connection connection = conn;
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

