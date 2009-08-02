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

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.keygen.KeyGenerator;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

public class SQLStatementCreate {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementCreate.class);
    
    /** SQL engine for all persistence operations at entities of the type this
     * class is responsible for. Holds all required information of the entity type. */
    private final SQLEngine _engine;
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private final PersistenceFactory _factory;
    
    /** Name of engine descriptor. */
    private final String _type;

    /** Name of the table extracted from class descriptor. */
    private final String _mapTo;

    /** A particular KeyGenerator instance from the list of key generators supported. */
    private KeyGenerator _keyGen;

    /** An sql statement. */
    private String _statement;

    //-----------------------------------------------------------------------------------    

    /**
    * Constructor.
    * 
    * @param engine SQL engine for all persistence operations at entities of the type this
    *        class is responsible for. Holds all required information of the entity type.
    * @param factory Persistence factory for the database engine the entity is persisted in.
    *        Used to format the SQL statement.
    * @throws MappingException
    */
    public SQLStatementCreate(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        ClassDescriptor clsDesc = engine.getDescriptor();
        
        _engine = engine;
        _factory = factory;
        _type = clsDesc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(clsDesc).getTableName();        
        _keyGen = factory.getKeyGenerator(clsDesc);
        
        buildStatement();
    }
    
    private void buildStatement() {
        if (_keyGen.getStyle() == KeyGenerator.NOGEN_INSERT) {
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

    /**
     * Executes the SQL statement after preparing the PreparedStatement.
     * 
     * @param database
     * @param conn An Open JDBC connection.
     * @param identity Identity of the object to insert.
     * @param entity
     * @return Identity
     * @throws PersistenceException If failed to insert record into database. This could happen
     *         if a database access error occurs, If identity size mismatches, unable to retrieve
     *         Identity, If provided Identity is null, If Extended engine is null.
     */
    public final Object executeStatement(final Database database, final Connection conn,
            final Identity identity, final ProposedEntity entity)
    throws PersistenceException {
        return _keyGen.executeStatement(_engine, _statement, database, conn, identity, entity);
    }
}
