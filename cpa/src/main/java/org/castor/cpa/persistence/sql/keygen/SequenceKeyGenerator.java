/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.castor.cpa.persistence.sql.keygen;

import java.lang.reflect.Method;
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
import org.castor.cpa.persistence.sql.driver.DB2Factory;
import org.castor.cpa.persistence.sql.driver.InterbaseFactory;
import org.castor.cpa.persistence.sql.driver.OracleFactory;
import org.castor.cpa.persistence.sql.driver.PostgreSQLFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SEQUENCE key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 * @see SequenceKeyGeneratorFactory
 */
public final class SequenceKeyGenerator implements KeyGenerator {
    //-----------------------------------------------------------------------------------

    private abstract class SequenceKeyGenValueHandler {
        private KeyGenerator _keyGenerator;
        private KeyGeneratorTypeHandler < ? extends Object > _typeHandler;

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

        public void setTypeHandler(final KeyGeneratorTypeHandler < ? extends Object > typeHandler) {
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
    
    private class PostgresqlType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            // postgresql does before_insert and after_insert with trigger
            if (getStyle() == BEFORE_INSERT) {
                return getValue("SELECT nextval('" + getSeqName(tableName, primKeyName) + "')",
                        conn);
            } else if ((getStyle() == AFTER_INSERT) && _triggerPresent) {
                Object insStmt = props.get("insertStatement");
                Class<?> psqlStmtClass = Class.forName("org.postgresql.Statement");
                Method getInsertedOID = psqlStmtClass.getMethod("getInsertedOID", (Class[]) null);
                Integer insertedOID = (Integer) getInsertedOID.invoke(insStmt, (Object[]) null);
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT " + _factory.quoteName(primKeyName)
                        + " FROM " + _factory.quoteName(tableName) + " WHERE OID=?");
                stmt.setInt(1, insertedOID.intValue());
                ResultSet rs = stmt.executeQuery();
                return _typeHandler.getValue(rs);
            } else if ((getStyle() == AFTER_INSERT) && !_triggerPresent) {
                throw new SQLException("PostgrSQL needs a trigger for keygeneration after insert");
            }
            throw new SQLException("PostgrSQL does not support keygeneration during insert");
        }
    }
        
    private class DB2Type extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            // db2 only does before_insert, and does it its own way
            if (getStyle() == BEFORE_INSERT) {
                return getValue("SELECT nextval FOR " + getSeqName(tableName, primKeyName)
                        + " FROM SYSIBM.SYSDUMMY1", conn);
            }
            throw new SQLException("DB2 does only support keygeneration before insert");
        }
    }
        
    private class InterbaseType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            // interbase only does before_insert, and does it its own way
            if (getStyle() == BEFORE_INSERT) {
                return getValue("SELECT gen_id(" + getSeqName(tableName, primKeyName) + ","
                        + _increment + ") FROM rdb$database", conn);
            }
            throw new SQLException("Interbase does only support keygeneration before insert");
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SequenceKeyGenerator.class);
     
    private String _factoryName;

    private PersistenceFactory _factory;

    private KeyGeneratorTypeHandler < ? extends Object > _typeHandler;

    private byte _style;

    private int _increment;
     
    private String _seqName;
     
    private boolean _triggerPresent;
     
    private SequenceKeyGenValueHandler _type = null;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator.
     */
    public SequenceKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        checkSupportedFactory(factory);
        supportsSqlType(sqlType);
        initSqlTypeHandler(sqlType);
        initType();
        
        boolean returning = "true".equals(params.getProperty("returning"));
        _triggerPresent = "true".equals(params.getProperty("trigger", "false"));

        if (!_factoryName.equals(OracleFactory.FACTORY_NAME) && returning) {
            throw new MappingException(Messages.format("mapping.keyGenParamNotCompat",
                    "returning=\"true\"", getClass().getName(), _factoryName));
        }
        
        _factory = factory;
        
        _seqName = params.getProperty("sequence", "{0}_seq");

        _style = _factoryName.equals(PostgreSQLFactory.FACTORY_NAME)
                || _factoryName.equals(InterbaseFactory.FACTORY_NAME)
                || _factoryName.equals(DB2Factory.FACTORY_NAME)
                ? BEFORE_INSERT : (returning ? DURING_INSERT : AFTER_INSERT);
        if (_triggerPresent && !returning) {
            _style = AFTER_INSERT;
        }
        if (_triggerPresent && (_style == BEFORE_INSERT)) {
            throw new MappingException(Messages.format("mapping.keyGenParamNotCompat",
                    "trigger=\"true\"", getClass().getName(), _factoryName));
        }
        
        try {
            _increment = Integer.parseInt(params.getProperty("increment", "1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
        }
    }
    
    private void checkSupportedFactory(final PersistenceFactory factory)
    throws MappingException {
        _factoryName = factory.getFactoryName();
        String[] supportedFactoryNames = getSupportedFactoryNames();
        boolean supported = false;
        for (int i = 0; i < supportedFactoryNames.length; i++) {
            if (_factoryName.equals(supportedFactoryNames[i])) {
                supported = true;
            }
        }

        if (!supported) {
            String msg = Messages.format("mapping.keyGenNotCompatible",
                    getClass().getName(), _factoryName); 
            throw new MappingException(msg);
        }
    }

    private String[] getSupportedFactoryNames() {
        return new String[] {OracleFactory.FACTORY_NAME, PostgreSQLFactory.FACTORY_NAME,
                InterbaseFactory.FACTORY_NAME, "sapdb", DB2Factory.FACTORY_NAME};
    }

    protected void initSqlTypeHandler(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _typeHandler = new KeyGeneratorTypeHandlerInteger(true);
        } else if (sqlType == Types.BIGINT) {
            _typeHandler = new KeyGeneratorTypeHandlerLong(true);
        } else if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            _typeHandler = new KeyGeneratorTypeHandlerString(true, 8);
        } else {
            _typeHandler = new KeyGeneratorTypeHandlerBigDecimal(true);
        }
    }
    
    private String getSeqName(final String tableName, final String primKeyName) {
        return MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
    }
    
    private void initType() {
        if (_factoryName.equals("interbase")) {
            _type = new InterbaseType();
        } else if (_factoryName.equals("db2")) {
            _type = new DB2Type();
        } else if (_factoryName.equals("postgresql")) {
            _type = new PostgresqlType();
        } else {
            _type = new DefaultType();
        }
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
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if (sqlType != Types.INTEGER
                && sqlType != Types.NUMERIC
                && sqlType != Types.DECIMAL
                && sqlType != Types.BIGINT
                && sqlType != Types.CHAR
                && sqlType != Types.VARCHAR) {
            throw new MappingException(Messages.format("mapping.keyGenSQLType",
                    getClass().getName(), new Integer(sqlType)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public byte getStyle() {
        return _style;
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

        if (_style == BEFORE_INSERT) {
            return insert;
        }

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
        if (getStyle() == DURING_INSERT) {
            // append 'RETURNING primKeyName INTO ?'
            sb.append(" RETURNING ");
            sb.append(_factory.quoteName(primKeyName));
            sb.append(" INTO ?");
        }
        return sb.toString();
    }
    
    //-----------------------------------------------------------------------------------
}
