/*
 * Copyright 2010 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann,
 *                Ralf Joachim, Ahmad Hassan, Dennis Butterstein
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
 * $Id: SQLStatementUpdateCheck.java 2009-05-30 14:29:32 ahmad $
 */
package org.castor.cpa.persistence.sql.engine;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;

/** 
 * SQLStatementUpdatCheck class to check whether the new SQL update statement has failed because
 * entity has been removed previously from persistent storage or the object has been modified
 * before. If the object has been modified an ObjectModifiedException and if object has been
 * deleted an ObjectDeletedException is raised.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:tyip AT leafsoft DOT com">Thomas Yip</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8285 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SQLStatementUpdateCheck {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementUpdateCheck.class);

    //-----------------------------------------------------------------------------------    

    /** The name of engine descriptor. */
    private final String _type;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;

    /** Select SQL statement class hierarchy. */
    private Select _select;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.  
     */
    public SQLStatementUpdateCheck(final SQLEngine engine) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();

        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());
    }

    /**
     * Build SQL select statement to check weather the entity is still available in persistent
     * storage or not.
     *
     * @param mapTo Table name retrieved from Class Descriptor trough JDO Nature.
     */
    private void buildStatement(final String mapTo) {  
        // table to be checked
        Table table = new Table(mapTo);
        
        // define conditions for select statement
        Condition condition = new AndCondition();
        for (int i = 0; i < _ids.length; i++) {             
            condition.and(table.column(_ids[i].getName()).equal(new Parameter(_ids[i].getName())));
        }

        // initialize select statement returning only first identity column 
        _select = new Select(table);
        _select.addSelect(table.column(_ids[0].getName()));
        _select.setCondition(condition);

    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * This function checks whether the object specified in the statement has been previously
     * removed from the persistent storage or has been modified. If the object has been modified
     * an ObjectModifiedException and if object has been deleted an ObjectDeletedException is
     * raised.
     * 
     * @param conn CastorConnection holding connection and PersistenceFactory to be used to create
     *        statement.
     * @param identity Identity of the object to check for availability.
     * @throws PersistenceException If a database access error occurs, identity size mismatches,
     *         column length mismatches, ObjectDeletedException if object had been deleted or
     *         ObjectModifiedException if object had been modified before.
     */
    public void updateFailureCheck(final CastorConnection conn, final Identity identity)
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();

        try {
            stmt.prepareStatement(_select);

            // bind the identity of the row into the preparedStatement
            for (int i = 0; i < _ids.length; i++) {
                // bind value to prepared statement
                stmt.bindParameter(_ids[i].getName(), _ids[i].toSQL(identity.get(i)),
                        _ids[i].getSqlType());
                
                if (LOG.isTraceEnabled()) {
                    LOG.trace(Messages.format("jdo.bindingIdentity", _ids[i].getName(),
                            _ids[i].toSQL(identity.get(i))));
                }
            }

            //Load Data into resultset
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String msg = Messages.format("persist.objectModified", _type, identity);
                throw new ObjectModifiedException(msg);
            }
            String msg = Messages.format("persist.objectDeleted", _type, identity);
            throw new ObjectDeletedException(msg);
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.updateCheckFatal", _type,  stmt.toString()), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
         // close statement
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }

    //-----------------------------------------------------------------------------------    
}
