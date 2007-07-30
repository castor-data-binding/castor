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


package org.exolab.castor.jdo.keygen;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * IDENTITY key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <mailto:dulci@start.no>Stein M. Hugubakken</mailto>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see IdentityKeyGeneratorFactory
 */
public final class IdentityKeyGenerator implements KeyGenerator {
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance (IdentityKeyGenerator.class);
    
    private DefaultIdentityValue _identityValue = null;
    private AbstractType _type = null;

    String _fName = null;

    /**
     * Initialize the IDENTITY key generator.
     * @param factory A PersistenceFactory instance.
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the persistance factory.
     */
    public IdentityKeyGenerator(final PersistenceFactory factory, final int sqlType) throws MappingException {
        _fName = factory.getFactoryName();
        if (!_fName.equals("sybase") &&
                !_fName.equals("sql-server") && 
                !_fName.equals("hsql") && 
                !_fName.equals("mysql") && 
                !_fName.equals("informix") &&
                !_fName.equals("sapdb") &&
                !_fName.equals("db2") && 
                !_fName.equals("derby") &&
                !_fName.equals("postgresql") &&
                !_fName.equals("pointbase")) {
            throw new MappingException(Messages.format("mapping.keyGenNotCompatible", getClass().getName(), _fName));
        }

        supportsSqlType(sqlType);

        initIdentityValue(sqlType);
        initType(_fName);
    }

    /**
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if (sqlType != Types.INTEGER &&
            sqlType != Types.NUMERIC &&
            sqlType != Types.DECIMAL &&
            sqlType != Types.BIGINT) {
            throw new MappingException(
                Messages.format("mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
        }

        if (sqlType != Types.INTEGER && _fName.equals("hsql")) {
            throw new MappingException(Messages.format("mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
        }

        if (sqlType != Types.NUMERIC && _fName.equals("derby")) {
                throw new MappingException(Messages.format("mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
            }
    }

    /**
     * @param conn An open connection within the given transaction
     * @param tableName The table name
     * @param primKeyName The primary key name
     * @param props A temporary replacement for Principal object
     * @return A new key
     * @throws PersistenceException An error occured talking to persistent
     *  storage
     */
    public Object generateKey(final Connection conn, final String tableName, final String primKeyName, final Properties props)
        throws PersistenceException {
        try {
            return _type.getValue(conn, tableName);
        } catch (Exception e) {
            _log.error("Problem generating new key", e);
            return null;
        }
    }

    /**
     * Style of key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT ?
     */
    public byte getStyle() {
        return AFTER_INSERT;
    }

    private void initIdentityValue(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _identityValue = new IntegerIdenityValue();
        } else if (sqlType == Types.BIGINT) {
            _identityValue = new LongIdenityValue();
        } else {
            _identityValue = new DefaultIdentityValue();
        }
    }

    private void initType(final String fName) {
        if (fName.equals("hsql")) {
            _type = new HsqlType();
        } else if (fName.equals("mysql")) {
            _type = new MySqlType();
        } else if (fName.equals("informix")) {
            _type = new InformixType();
        } else if (fName.equals("db2")) {
            _type = new DB2Type();
        } else if (fName.equals("sapdb")) {
            _type = new SapDbType();
        } else if (fName.equals("derby")) {
            _type = new DerbyType();
        } else if (fName.equals("postgresql")) {
            _type = new PostgresqlType();
        } else {
            _type = new DefaultType();
        }
    }

    /**
     * Is key generated in the same connection as INSERT?
     */
    public boolean isInSameConnection() {
        return true;
    }

    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators)
     */
    public String patchSQL(final String insert, final String primKeyName) throws MappingException {
        return insert;
    }
    
    private abstract class AbstractType {
        abstract Object getValue(Connection conn, String tableName) throws PersistenceException;

        Object getValue(final PreparedStatement stmt) throws PersistenceException, SQLException {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return _identityValue.getValue(rs.getInt(1));
            }
        throw new PersistenceException(Messages.format("persist.keyGenFailed", getClass().getName()));
        }

        Object getValue(final String sql, final Connection conn) throws PersistenceException {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                return getValue(stmt);
            } catch (SQLException e) {
                throw new PersistenceException(Messages.format("persist.keyGenSQL", getClass().getName(), e.toString()));
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
    }

    private class DB2Type extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            StringBuffer buf = new StringBuffer("SELECT IDENTITY_VAL_LOCAL() FROM sysibm.sysdummy1");           
            return getValue(buf.toString(), conn);
        }
    }

    // TODO: does Progress have support for retrieving key values for auto_increment columns
//    private class ProgressType extends AbstractType {
//        Object getValue(Connection conn, String tableName) throws PersistenceException {
//            StringBuffer buf = new StringBuffer("SELECT IDENTITY_VAL_LOCAL() FROM sysibm.sysdummy1");           
//            return getValue(buf.toString(), conn);
//        }
//    }
    
    private class DefaultType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("SELECT @@identity", conn);
        }
    }
    
    private class HsqlType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            PreparedStatement stmt = null;
            Object v = null;
            try {
                stmt = conn.prepareCall("{call IDENTITY()}");
                v = getValue(stmt);
            } catch (SQLException e) {
                throw new PersistenceException(Messages.format("persist.keyGenSQL", getClass().getName(), e.toString()));
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        _log.warn ("Problem closing JDBCstatement", ex);
                    }
                    stmt = null;
                }
            }
            return v;
        }
    }
    
    private class InformixType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("select dbinfo('sqlca.sqlerrd1') from systables where tabid = 1", conn);
        }
    }
    
    private class MySqlType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("SELECT LAST_INSERT_ID()", conn);
        }
    }

    private class SapDbType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("SELECT " +  tableName + ".currval" +  " FROM " + tableName, conn);
        }
    }
    
    private class DerbyType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("SELECT IDENTITY_VAL_LOCAL() FROM " + tableName, conn);
        }
    }
    
    private class PostgresqlType extends AbstractType {
        Object getValue(final Connection conn, final String tableName) throws PersistenceException {
            return getValue("SELECT currval ('" +  tableName + "_id_seq')", conn);
        }
    }
    
    private class DefaultIdentityValue {
        Object getValue(final int value) {
            return new BigDecimal(value);
        }
    }
    
    private class IntegerIdenityValue extends DefaultIdentityValue {
        Object getValue(final int value) {
            return new Integer(value);
        }
    }
    
    private class LongIdenityValue extends DefaultIdentityValue {
        Object getValue(final int value) {
            return new Long(value);
        }
    }
}
