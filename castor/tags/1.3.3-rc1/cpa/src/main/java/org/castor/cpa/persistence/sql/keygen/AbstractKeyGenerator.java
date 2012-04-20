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
import org.castor.jdo.engine.DatabaseContext;
import org.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * Abstract Class that implements the KeyGenerator Interface and provide 
 * implementation for methods that are common in more than one subclass of this
 * AbstractKeyGenerator.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class AbstractKeyGenerator implements KeyGenerator {
    //-----------------------------------------------------------------------------------        

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractKeyGenerator.class);
    
    //-----------------------------------------------------------------------------------    

    /**
     * Operning new JDBC Connection. 
     * 
     * @param database The database on which it opens the JDBC connection.
     * @return A JDBC Connection
     * @throws PersistenceException If fails to open connection.
     */
    public final Connection getSeparateConnection(final Database database)
    throws PersistenceException {
        DatabaseContext context = null;
        try {
            context = DatabaseRegistry.getDatabaseContext(database.getDatabaseName());
        } catch (MappingException e) {
            throw new PersistenceException(Messages.message("persist.cannotCreateSeparateConn"), e);
        }
        
        try {
            Connection conn = context.getConnectionFactory().createConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new PersistenceException(Messages.message("persist.cannotCreateSeparateConn"), e);
        }
    }
    
    /**
     * Close the JDBC Connection.
     * 
     * @param conn A JDBC Connection.
     */

    public final void closeSeparateConnection(final Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    //-----------------------------------------------------------------------------------        
}
