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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandler;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerBigDecimal;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerInteger;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerLong;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerString;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Extends AbstractBeforeKeyGenerator and implements additional methods specific
 * to Sequence Key generator. It invovles the fetching the Table ID before the 
 * record is inserted into the table. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class SequenceBeforeKeyGenerator extends AbstractBeforeKeyGenerator {    
    //-----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SequenceBeforeKeyGenerator.class);
     
    /** Key length used for KeyGeneratorTypeHandlerString. */
    private static final int STRING_KEY_LENGTH = 8;
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private PersistenceFactory _factory;
    
    /** Sequence name associated with the table. */
    private String _seqName;
    
    /** Value used in fetching identity from Interbase database. */
    private int _increment;
    
    /** Particular type handler instance. */
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator for BEFORE_INSERT style 
     * {@link #generateKey} is called before INSERT. 
     * 
     * @param factory A PersistenceFactory instance.
     * @param params Database specific properties. 
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public SequenceBeforeKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        super(factory);
        _factory = factory;        
        _seqName = params.getProperty("sequence", "{0}_seq");

        try {
            _increment = Integer.parseInt(params.getProperty("increment", "1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
        }
        
        initSqlTypeHandler(sqlType);
    }

    /**
     * Initialize the Handler based on SQL Type.
     * 
     * @param sqlType A SQLTypidentifier.
     */
    protected void initSqlTypeHandler(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _typeHandler = new KeyGeneratorTypeHandlerInteger(true);
        } else if (sqlType == Types.BIGINT) {
            _typeHandler = new KeyGeneratorTypeHandlerLong(true);
        } else if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            _typeHandler = new KeyGeneratorTypeHandlerString(true, STRING_KEY_LENGTH);
        } else {
            _typeHandler = new KeyGeneratorTypeHandlerBigDecimal(true);
        }
    }
    
    /**
     * Formats the sequence name using name of the table and ID.
     * 
     * @param tableName Name of the table.
     * @param primKeyName ID of the table.
     * @return String representing formatted sequence name.
     */
    private String getSeqName(final String tableName, final String primKeyName) {
        return MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * @param conn An open connection within the given transaction.
     * @param tableName The table name.
     * @param primKeyName The primary key name.
     * @return A new key.
     * @throws PersistenceException An error occured talking to persistent storage.
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName) 
            throws PersistenceException {           
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                // prepares the statement
                String sql = _factory.getSequenceBeforeSelectString(
                     getSeqName(tableName, primKeyName), tableName, _increment);
                stmt = conn.prepareStatement(sql);
                
                // execute the prepared statement
                rs = stmt.executeQuery();
                
                // process result set using appropriate handler and return its value
                return _typeHandler.getValue(rs);
            } catch (SQLException e) {
                String msg = Messages.format("persist.keyGenSQL", 
                        this.getClass().getName(), e.toString());
                throw new PersistenceException(msg);
            } finally {
                try {
                    if (rs != null) { rs.close(); }
                    if (stmt != null) { stmt.close(); }
                } catch (SQLException e) {
                    LOG.warn("Problem closing JDBC statement", e);
                }
            }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }
    
    //-----------------------------------------------------------------------------------
}
