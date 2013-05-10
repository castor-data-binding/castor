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
package org.castor.cpa.persistence.sql.engine;

import java.sql.Connection;

import org.castor.cpa.persistence.sql.keygen.KeyGenerator;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SQLStatementCreate class that makes use of KeyGenerators methods to build sql
 * insert statement and execute them. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public class SQLStatementInsert {
    
    /** SQL engine for all persistence operations at entities of the type this
     * class is responsible for. Holds all required information of the entity type. */
    private final SQLEngine _engine;

    /** A particular KeyGenerator instance from the list of key generators supported. */
    private KeyGenerator _keyGen;

    //-----------------------------------------------------------------------------------    

    /**
    * Constructor.
    * 
    * @param engine SQL engine for all persistence operations at entities of the type this
    *        class is responsible for. Holds all required information of the entity type.
    * @param factory Persistence factory for the database engine the entity is persisted in.
    *        Used to format the SQL statement.
    * @throws MappingException If fails to get the Key Generator instance.
    */
    public SQLStatementInsert(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        ClassDescriptor clsDesc = engine.getDescriptor();        
        _engine = engine;        
        _keyGen = factory.getKeyGenerator(clsDesc);
        
        buildStatement();
    }
    
    /**
     * Builds the SQL statement using KeyGenerators.
     */
    private void buildStatement() {
        _keyGen = _keyGen.buildStatement(_engine);
    }

    /**
     * Executes the SQL statement after preparing the PreparedStatement.
     * 
     * @param database A particular Database instance.
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
        return _keyGen.executeStatement(database, conn, identity, entity);
    }
}
