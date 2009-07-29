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
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.castor.core.util.Messages;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SEQUENCE key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision: 8241 $ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 * @see SequenceKeyGeneratorFactory
 */
public final class SequenceDuringKeyGenerator extends AbstractDuringKeyGenerator {
    //-----------------------------------------------------------------------------------

    private abstract class SequenceKeyGenValueHandler {
        private KeyGenerator _keyGenerator;
        private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

        protected abstract Object getValue(Connection conn, String tableName,
                String primKeyName, Properties props) throws Exception;

        public Object getValue(final String sql, final Connection conn)
        throws PersistenceException {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                return _typeHandler.getValue(rs);
            } catch (SQLException e) {
                String msg = Messages.format("persist.keyGenSQL", 
                        _keyGenerator.getClass().getName(), e.toString());
                throw new PersistenceException(msg);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        public void setGenerator(final KeyGenerator generator) {
            _keyGenerator = generator;
        }

        public void setTypeHandler(final KeyGeneratorTypeHandler<? extends Object> typeHandler) {
            _typeHandler = typeHandler;
        }
    }

    private class DefaultType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            // used for orcale and sap after_insert
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".currval")
                    + " FROM " + _factory.quoteName(tableName), conn);
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SequenceDuringKeyGenerator.class);
     
    private static final int STRING_KEY_LENGTH = 8;
    
    private PersistenceFactory _factory;

    private boolean _triggerPresent;
    
    private String _seqName;
    
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

    private SequenceKeyGenValueHandler _type = null;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator for DURING_INSERT style 
     * {@link #generateKey} is never called, all work is done by {@link #patchSQL}.
     * 
     * @param factory A PersistenceFactory instance.
     * @param params
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public SequenceDuringKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        _factory = factory;        
        _triggerPresent = "true".equals(params.getProperty("trigger", "false"));
        _seqName = params.getProperty("sequence", "{0}_seq");
        
        initSqlTypeHandler(sqlType);
        initType();
    }

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
    
    private String getSeqName(final String tableName, final String primKeyName) {
        return MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
    }
    
    private void initType() {
        _type = new DefaultType();
        _type.setGenerator(this);
        _type.setTypeHandler(_typeHandler);
     }
    
    //-----------------------------------------------------------------------------------

    /**
     * @param conn An open connection within the given transaction.
     * @param tableName The table name.
     * @param primKeyName The primary key name.
     * @param props A temporary replacement for Principal object.
     * @return A new key.
     * @throws PersistenceException An error occured talking to persistent storage.
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        try {
            return _type.getValue(conn, tableName, primKeyName, props);
        } catch (Exception e) {
            LOG.error("Problem generating new key", e);
            throw new PersistenceException(Messages.format("persist.keyGenSQL", e));
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }
    
    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators).
     */
    public String patchSQL(final String insert, final String primKeyName)
    throws MappingException {
        StringTokenizer st;
        String tableName;
        String seqName;
        String nextval;
        StringBuffer sb;
        int lp1;  // the first left parenthesis, which starts fields list
        int lp2;  // the second left parenthesis, which starts values list

        // First find the table name
        st = new StringTokenizer(insert);
        if (!st.hasMoreTokens() || !st.nextToken().equalsIgnoreCase("INSERT")) {
            throw new MappingException(Messages.format("mapping.keyGenCannotParse", insert));
        }
        if (!st.hasMoreTokens() || !st.nextToken().equalsIgnoreCase("INTO")) {
            throw new MappingException(Messages.format("mapping.keyGenCannotParse", insert));
        }
        if (!st.hasMoreTokens()) {
            throw new MappingException(Messages.format("mapping.keyGenCannotParse", insert));
        }
        tableName = st.nextToken();

        // remove every double quote in the tablename
        int idxQuote = tableName.indexOf('"');
        if (idxQuote >= 0) {
            StringBuffer buffer2 = new StringBuffer();
            int pos = 0;

            do {
                buffer2.append(tableName.substring(pos, idxQuote));
                pos = idxQuote + 1;
                idxQuote = tableName.indexOf('"', pos);
            } while (idxQuote != -1);

            buffer2.append(tableName.substring(pos));

            tableName = buffer2.toString();
        }

        // due to varargs in 1.5, see CASTOR-1097
        seqName = MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
        nextval = _factory.quoteName(seqName + ".nextval");
        lp1 = insert.indexOf('(');
        lp2 = insert.indexOf('(', lp1 + 1);
        if (lp1 < 0) {
            throw new MappingException(Messages.format("mapping.keyGenCannotParse", insert));
        }
        sb = new StringBuffer(insert);
        // if no onInsert triggers in the DB, we have to supply the Key values manually
        if (!_triggerPresent) {
           if (lp2 < 0) {
                // Only one pk field in the table, the INSERT statement would be
                // INSERT INTO table VALUES ()
                lp2 = lp1;
                lp1 = insert.indexOf(" VALUES ");
                // don't change the order of lines below,
                // otherwise index becomes invalid
                sb.insert(lp2 + 1, nextval);
                sb.insert(lp1 + 1, "(" + _factory.quoteName(primKeyName) + ") ");
            } else {
                // don't change the order of lines below,
                // otherwise index becomes invalid
                sb.insert(lp2 + 1, nextval + ",");
                sb.insert(lp1 + 1, _factory.quoteName(primKeyName) + ",");
            }
        }

        // append 'RETURNING primKeyName INTO ?'
        sb.append(" RETURNING ");
        sb.append(_factory.quoteName(primKeyName));
        sb.append(" INTO ?");
        
        return sb.toString();
    }
    
    //-----------------------------------------------------------------------------------
}
