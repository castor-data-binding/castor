/*
 * Copyright 2009 Assaf Arkin, Oleg Nitz, Bruce Snyder
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
import java.util.Properties;

import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;

/**
 * Interface for a key generator. The key generator is used for
 * producing identities for objects before they are created in the
 * database.
 * <p>
 * All the key generators belonging to the same database share the
 * same non-transactional connection to the database.
 * <p>
 * The key generator is configured from the mapping file using
 * Bean-like accessor methods.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2005-04-25 15:33:21 -0600 (Mon, 25 Apr 2005) $
 */
public interface KeyGenerator {
    /** For the key generators of BEFORE_INSERT style {@link #generateKey}
     *  is called before INSERT. {@link #patchSQL} may be used but usually doesn't. */
    byte BEFORE_INSERT = -1;

    /** For the key generators of DURING_INSERT style {@link #generateKey}
     *  is never called, all work is done by {@link #patchSQL}. */
    byte DURING_INSERT = 0;

    /** For the key generators of AFTER_INSERT style {@link #generateKey}
     *  is called after INSERT. {@link #patchSQL} may be used but usually doesn't. */
    byte AFTER_INSERT = 1;
    
    /** For key generators that does not generate key before running insert query. */
    byte NOGEN_INSERT = 2;
    
    /**
     * Generate a new key for the specified table. This method is
     * called when a new object is about to be created. In some
     * environments the name of the owner of the object is known,
     * e.g. the principal in a J2EE server.
     * This method is never called for DURING_INSERT key generators.
     *
     * @param conn An open connection within the given transaction
     * @param tableName The table name
     * @param primKeyName The primary key name
     * @param props A temporary replacement for Principal object
     * @return A new key
     * @throws PersistenceException An error occured talking to persistent
     *  storage
     */
    Object generateKey(Connection conn, String tableName, String primKeyName, Properties props)
    throws PersistenceException;

    /**
     * Style of the key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT.
     */
    byte getStyle();

    /**
     * Is key generated in the same connection as INSERT?
     * For DURING_INSERT style this method is never called.
     */
    boolean isInSameConnection();

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
    Object executeStatement(final Database database, final Connection conn, 
            final Identity identity, final ProposedEntity entity) throws PersistenceException;
    
    /**
     * Builds the SQL insert statement.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
     * @return KeyGenerator 
     */
    KeyGenerator buildStatement(final SQLEngine engine);
}
