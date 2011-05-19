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
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.cpa.persistence.sql.engine.CastorStatement;
import org.castor.cpa.persistence.sql.engine.SQLEngine;
import org.castor.cpa.persistence.sql.engine.SQLStatementInsertCheck;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLFieldInfo;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Abstract Class that implements the KeyGenerator Interface for BEFORE_INSERT style. The key
 * generator is used for producing identities for objects before they are created in the
 * database.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public abstract class AbstractBeforeKeyGenerator extends AbstractKeyGenerator {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractBeforeKeyGenerator.class);
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private PersistenceFactory _factory;
    
    /** SQL engine for all persistence operations at entities of the type this
     * class is responsible for. Holds all required information of the entity type. */
    private SQLEngine _engine;
    
    /** Represents the engine type obtained from clas descriptor. */
    private String _engineType = null;
    
    /** Name of the Table extracted from Class descriptor. */
    private String _mapTo;
    
    /** Variable to store built insert class hierarchy. */
    private Insert _insert;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param factory  Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement
     */
    public AbstractBeforeKeyGenerator(final PersistenceFactory factory) {
        _factory = factory;
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
      
        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        for (int i = 0; i < ids.length; i++) {
            String name = ids[i].getName();
            _insert.addAssignment(new Column(name), new Parameter(name));
        }
        
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
    }
    
    /**
     * {@inheritDoc}
     */
    public final Object executeStatement(final Database database, final CastorConnection conn, 
            final Identity identity, final ProposedEntity entity) throws PersistenceException {
        Identity internalIdentity = identity;
        
        CastorStatement stmt = conn.createStatement();
        try {
            // generate key before INSERT.
            internalIdentity = generateKey(database, conn);

            // we only need to care on JDBC 3.0 at after INSERT.
            stmt.prepareStatement(_insert);

            //bind Identities
            bindIdentity(internalIdentity, stmt);

            //bind  Fields
            bindFields(entity, stmt);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _engineType, stmt.toString()));
            }

            stmt.executeUpdate();

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _engineType, stmt.toString()), except);

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            SQLStatementInsertCheck lookupStatement = 
                new SQLStatementInsertCheck(_engine, _factory);    
            lookupStatement.insertDuplicateKeyCheck(conn, internalIdentity);

            throw new PersistenceException(Messages.format("persist.nested", except), except);
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
     * Binds the identity values.
     * 
     * @param internalIdentity Identity values.
     * @param stmt CastorStatement containing Connection and PersistenceFactory.
     * @throws SQLException If a database access error occurs.
     * @throws PersistenceException If identity size mismatches.
     */
    private void bindIdentity(final Identity internalIdentity, final CastorStatement stmt) 
    throws SQLException, PersistenceException {
        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        if (internalIdentity.size() != ids.length) {
            throw new PersistenceException("Size of identity field mismatched!");
        }

        for (int i = 0; i < ids.length; i++) {
            stmt.bindParameter(ids[i].getName(), ids[i].toSQL(internalIdentity.get(i)), 
                    ids[i].getSqlType());
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
    private void bindFields(final ProposedEntity entity, final CastorStatement stmt
            ) throws SQLException, PersistenceException {
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
                throw new PersistenceException(Messages.format("persist.noIdentity", _engineType));
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
