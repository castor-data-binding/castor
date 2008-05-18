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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * IDENTITY key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see IdentityKeyGeneratorFactory
 */
public final class IdentityKeyGenerator extends AbstractKeyGenerator {
    private abstract class IdentityKeyGenValueHandler extends AbstractKeyGenValueHandler {
        protected abstract Object getValue(Connection conn, String tableName) throws PersistenceException;
    }
        
    private class DefaultType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("SELECT @@identity", conn);
        }
    }

    private class DB2Type extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            // Statement worked with IBM UDB v7 and v8 but not with IBM DB2 v6.
            // StringBuffer buf = new StringBuffer("SELECT IDENTITY_VAL_LOCAL() FROM ");
            // buf.append(tableName).append(" FETCH FIRST ROW ONLY");
            
            // Statement works with IBM UDB and IBM DB2.
            StringBuffer buf = new StringBuffer();
            buf.append("SELECT IDENTITY_VAL_LOCAL() FROM sysibm.sysdummy1");           
            return getValue(buf.toString(), conn);
        }
    }

    private class HsqlType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            PreparedStatement stmt = null;
            Object v = null;
            try {
                stmt = conn.prepareCall("{call IDENTITY()}");
                v = getValue(stmt);
            } catch (SQLException e) {
                String msg = Messages.format("persist.keyGenSQL", getClass().getName(), e.toString());
                throw new PersistenceException(msg);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        _log.warn("Problem closing JDBCstatement", ex);
                    }
                }
            }
            return v;
        }
    }

    private class InformixType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("select dbinfo('sqlca.sqlerrd1') from systables where tabid = 1", conn);
        }
    }

    private class MySqlType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("SELECT LAST_INSERT_ID()", conn);
        }
    }

    private class SapDbType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("SELECT " + tableName + ".currval" + " FROM " + tableName, conn);
        }
    }
     
    private class DerbyType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("SELECT IDENTITY_VAL_LOCAL() FROM " + tableName, conn);
        }
    }
    
    private class PostgresqlType extends IdentityKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName)
        throws PersistenceException {
            return getValue("SELECT currval ('" +  tableName + "_id_seq')", conn);
        }
    }
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(IdentityKeyGenerator.class);
    
    private IdentityKeyGenValueHandler _type = null;

    private String _fName = null;

    /**
     * Initialize the IDENTITY key generator.
     * @param factory A PersistenceFactory instance.
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public IdentityKeyGenerator(final PersistenceFactory factory, final int sqlType)
    throws MappingException {
        _fName = factory.getFactoryName();
        
        checkSupportedFactory(factory);
        supportsSqlType(sqlType);
        initIdentityValue(sqlType);
        initType(_fName);
    }

    public String[] getSupportedFactoryNames() {
        return new String[] {"sybase", "sql-server", "hsql", "mysql", "informix", "sapdb",
                "db2", "derby", "postgresql", "pointbase" };
     }
         
    /**
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if (sqlType != Types.INTEGER && sqlType != Types.NUMERIC
                && sqlType != Types.DECIMAL && sqlType != Types.BIGINT) {
            throw new MappingException(Messages.format(
                    "mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
        }

        if (sqlType != Types.INTEGER && _fName.equals("hsql")) {
            throw new MappingException(Messages.format(
                    "mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
        }

        if (sqlType != Types.NUMERIC && _fName.equals("derby")) {
                throw new MappingException(Messages.format(
                        "mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
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
        _type.setGenerator(this);
        _type.setIdentityValue(_identityValue);
    }

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
            return _type.getValue(conn, tableName);
        } catch (Exception e) {
            _log.error("Problem generating new key", e);
            return null;
        }
    }
}
