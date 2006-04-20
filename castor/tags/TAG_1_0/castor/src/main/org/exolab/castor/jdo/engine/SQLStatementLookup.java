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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.util.Messages;

public final class SQLStatementLookup {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementLookup.class);
    
    private final SQLEngine _engine;
    
    private final PersistenceFactory _factory;
    
    private final String _type;

    private final String _mapTo;

    private String _statement;

    public SQLStatementLookup(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = engine.getDescriptor().getTableName();
        
        buildStatement();
    }

    private void buildStatement() throws MappingException {
        try {
            SQLColumnInfo[] _ids = _engine.getColumnInfoForIdentities();
            QueryExpression query = _factory.getQueryExpression();

            // initalize lookup query
            for (int i = 0; i < _ids.length; i++) {
                query.addParameter(_mapTo, _ids[i].getName(), QueryExpression.OpEquals);
            }
            _statement = query.getStatement(true);
      } catch (QueryException except) {
          LOG.warn("Problem building SQL", except);
          throw new MappingException(except);
      }
    }
    
    public Object executeStatement(final Object conn, Object identity)
    throws PersistenceException {
        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        PreparedStatement stmt = null;
        try {
            stmt = ((Connection) conn).prepareStatement(_statement);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.duplicateKeyCheck", _statement));
            }
            
            // bind the identity to the preparedStatement
            int count = 1;
            if (identity instanceof Complex) {
                Complex id = (Complex) identity;
                if ((id.size() != ids.length) || (ids.length <= 1)) {
                    throw new PersistenceException("Size of complex field mismatched!");
                }

                for (int i = 0; i < ids.length; i++) {
                    stmt.setObject(count++, ids[i].toSQL(id.get(i)));
                }
            } else {
                if (ids.length != 1) {
                    throw new PersistenceException("Complex field expected!");
                }

                stmt.setObject(count++, ids[0].toSQL(identity));
            }

            if (stmt.executeQuery().next()) {
                stmt.close();
                throw new DuplicateIdentityException(Messages.format("persist.duplicateIdentity", _type, identity));
            }
        } catch (SQLException except2) {
            // Error at the stage indicates it wasn't a duplicate
            // primary key problem. But best if the INSERT error is
            // reported, not the SELECT error.
        }

        try {
            // Close the insert/select statement
            if (stmt != null) { stmt.close(); }
        } catch (SQLException except2) {
            LOG.warn("Problem closing JDBC statement", except2);
        }

        return null;
    }
}
