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



import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.util.JDOUtils;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.drivers.DB2Factory;
import org.exolab.castor.jdo.drivers.InterbaseFactory;
import org.exolab.castor.jdo.drivers.OracleFactory;
import org.exolab.castor.jdo.drivers.PostgreSQLFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SEQUENCE key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 * @see SequenceKeyGeneratorFactory
 */
public final class SequenceKeyGenerator implements KeyGenerator {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log LOG = LogFactory.getFactory().getInstance (SequenceKeyGenerator.class);

    protected final PersistenceFactory _factory;

    protected final String _factoryName;

    protected final String _seqName;

    private byte _style;

    private final int _sqlType;

    private int _increment;

    private boolean _triggerPresent;

    /**
     * Initialize the SEQUENCE key generator.
     */
    public SequenceKeyGenerator( PersistenceFactory factory, Properties params, int sqlType ) throws MappingException {
        boolean returning;

        _factoryName = factory.getFactoryName();
        returning = "true".equals( params.getProperty("returning") );
        _triggerPresent = "true".equals( params.getProperty("trigger","false") );


        if (!_factoryName.equals(OracleFactory.FACTORY_NAME)
                && !_factoryName.equals(PostgreSQLFactory.FACTORY_NAME)
                && !_factoryName.equals(InterbaseFactory.FACTORY_NAME)
                && !_factoryName.equals("sapdb")
                && !_factoryName.equals(DB2Factory.FACTORY_NAME) ) {
            throw new MappingException( Messages.format( "mapping.keyGenNotCompatible",
                                        getClass().getName(), _factoryName ) );
        }
        if (! _factoryName.equals(OracleFactory.FACTORY_NAME) && returning) {
            throw new MappingException( Messages.format( "mapping.keyGenParamNotCompat",
                                        "returning=\"true\"", getClass().getName(), _factoryName ));
        }
        _factory = factory;
        _seqName = params.getProperty("sequence", "{0}_seq");

        _style = (_factoryName.equals(PostgreSQLFactory.FACTORY_NAME) || _factoryName.equals(InterbaseFactory.FACTORY_NAME) || _factoryName.equals(DB2Factory.FACTORY_NAME)
                ? BEFORE_INSERT : ( returning  ? DURING_INSERT : AFTER_INSERT) );
        if (_triggerPresent && !returning) {
            _style = AFTER_INSERT;
        }
        if (_triggerPresent && _style == BEFORE_INSERT)
            throw new MappingException(Messages.format( "mapping.keyGenParamNotCompat",
                                        "trigger=\"true\"", getClass().getName(), _factoryName ) );

        _sqlType = sqlType;
        supportsSqlType( sqlType );

        try {
            _increment = Integer.parseInt(params.getProperty("increment","1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
        }
    }

    /**
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType( int sqlType ) throws MappingException {
        if (sqlType != Types.INTEGER
                && sqlType != Types.NUMERIC
                && sqlType != Types.DECIMAL
                && sqlType != Types.BIGINT
                && sqlType != Types.CHAR
                && sqlType != Types.VARCHAR) {
            throw new MappingException( Messages.format( "mapping.keyGenSQLType",
                    getClass().getName(), new Integer( sqlType ) ) );
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
    public Object generateKey( Connection conn, String tableName, String primKeyName, Properties props ) throws PersistenceException {
        PreparedStatement stmt = null;
        ResultSet rs;
        String seqName;
        String table;

        seqName = MessageFormat.format( _seqName, new Object[] {tableName,primKeyName});// due to varargs in 1.5, see CASTOR-1097
        table = _factory.quoteName(tableName);
        try {
            if (_factory.getFactoryName().equals(InterbaseFactory.FACTORY_NAME)) {
                //interbase only does before_insert, and does it its own way
                stmt = conn.prepareStatement("SELECT gen_id(" + seqName +
                        "," + _increment + ") FROM rdb$database");
                rs = stmt.executeQuery();
            } else if (_factory.getFactoryName().equals(DB2Factory.FACTORY_NAME)) {
                stmt = conn.prepareStatement("SELECT nextval FOR " + seqName + " FROM SYSIBM.SYSDUMMY1" );
                rs = stmt.executeQuery();
            } else {
                if ( _style == BEFORE_INSERT ) {
                    stmt = conn.prepareStatement("SELECT nextval('" + seqName + "')" );
                    rs = stmt.executeQuery();
                } else if (_triggerPresent && _factoryName.equals(PostgreSQLFactory.FACTORY_NAME)) {
                    Object insStmt = props.get("insertStatement");
                    Class psqlStmtClass = Class.forName("org.postgresql.Statement");
                    Method getInsertedOID = psqlStmtClass.getMethod("getInsertedOID", (Class[])null);
                    int insertedOID = ((Integer) getInsertedOID.invoke(insStmt, (Object[])null)).intValue();
                    stmt = conn.prepareStatement("SELECT " + _factory.quoteName( primKeyName ) +
                            " FROM " + table + " WHERE OID=?");
                    stmt.setInt(1, insertedOID);
                    rs = stmt.executeQuery();

                } else {
                    stmt = conn.prepareStatement("SELECT " + _factory.quoteName(seqName + ".currval") + " FROM " + table);
                    rs = stmt.executeQuery();
                }
            }

            if ( !rs.next() ) {
                throw new PersistenceException( Messages.format( "persist.keyGenFailed", getClass().getName() ) );
            }

            Object resultKey = null;
            int resultValue = rs.getInt( 1 );
            String resultColName = rs.getMetaData().getColumnName( 1 ) ;
            int resultColType = rs.getMetaData().getColumnType( 1 ) ;
            if ( LOG.isDebugEnabled() ) {
                LOG.debug("JDBC query returned value " + resultValue + " from column " + resultColName + "/" + resultColType);
            }
            if (_sqlType == Types.INTEGER) {
                resultKey = new Integer(resultValue);
            } else if (_sqlType == Types.BIGINT) {
                resultKey = new Long(resultValue);
            } else if (_sqlType == Types.CHAR || _sqlType == Types.VARCHAR) {
                resultKey = String.valueOf(resultValue);
            } else {
                resultKey = new BigDecimal(resultValue);
            }

            if (LOG.isDebugEnabled()) {
                if (resultKey != null) {
                    LOG.debug("Returning value " + resultKey + " of type " + resultKey.getClass().getName() + " as key.");
                }
            }
            return resultKey;
        } catch ( Exception ex ) {
            throw new PersistenceException( Messages.format(
                    "persist.keyGenSQL", getClass().getName(), ex.toString() ) );
        } finally {
            JDOUtils.closeStatement(stmt);
        }
    }

    /**
     * Style of key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT ?
     */
    public byte getStyle() {
        return _style;
    }


    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators)
     */
    public String patchSQL( String insert, String primKeyName )
            throws MappingException {
        StringTokenizer st;
        String tableName;
        String seqName;
        String nextval;
        StringBuffer sb;
        int lp1;  // the first left parenthesis, which starts fields list
        int lp2;  // the second left parenthesis, which starts values list

        if ( _style == BEFORE_INSERT ) {
            return insert;
        }

        // First find the table name
        st = new StringTokenizer( insert );
        if ( !st.hasMoreTokens() || !st.nextToken().equalsIgnoreCase("INSERT") ) {
            throw new MappingException( Messages.format( "mapping.keyGenCannotParse",
                                                         insert ) );
        }
        if ( !st.hasMoreTokens() || !st.nextToken().equalsIgnoreCase("INTO") ) {
            throw new MappingException( Messages.format( "mapping.keyGenCannotParse",
                                                         insert ) );
        }
        if ( !st.hasMoreTokens() ) {
            throw new MappingException( Messages.format( "mapping.keyGenCannotParse",
                                                         insert ) );
        }
        tableName = st.nextToken();

        // remove every double quote in the tablename
        int idxQuote = tableName.indexOf('"');
        if (idxQuote >= 0) {
            StringBuffer buffer2 = new StringBuffer();
            int pos = 0;

            do {
                buffer2.append(tableName.substring(pos, idxQuote));
                pos = idxQuote+1;
            } while((idxQuote=tableName.indexOf('"', pos)) != -1);

            buffer2.append(tableName.substring(pos));

            tableName = buffer2.toString();
        }

        seqName = MessageFormat.format( _seqName, new Object[] {tableName,primKeyName}); // due to varargs in 1.5, see CASTOR-1097
        nextval = _factory.quoteName(seqName + ".nextval");
        lp1 = insert.indexOf( '(' );
        lp2 = insert.indexOf( '(', lp1 + 1 );
        if ( lp1 < 0 ) {
            throw new MappingException( Messages.format( "mapping.keyGenCannotParse",
                                                         insert ) );
        }
        sb = new StringBuffer( insert );
        if (!_triggerPresent) { // if no onInsert triggers in the DB, we have to supply the Key values manually
           if ( lp2 < 0 ) {
                // Only one pk field in the table, the INSERT statement would be
                // INSERT INTO table VALUES ()
                lp2 = lp1;
                lp1 = insert.indexOf( " VALUES " );
                // don't change the order of lines below,
                // otherwise index becomes invalid
                sb.insert( lp2 + 1, nextval);
                sb.insert( lp1 + 1, "(" + _factory.quoteName( primKeyName ) + ") " );
            } else {
                // don't change the order of lines below,
                // otherwise index becomes invalid
                sb.insert( lp2 + 1, nextval + ",");
                sb.insert( lp1 + 1, _factory.quoteName( primKeyName ) + "," );
            }
        }
        if ( _style == DURING_INSERT ) {
            // append 'RETURNING primKeyName INTO ?'
            sb.append( " RETURNING " );
            sb.append( _factory.quoteName( primKeyName ) );
            sb.append( " INTO ?" );
        }
        return sb.toString();
    }


    /**
     * Is key generated in the same connection as INSERT?
     */
    public boolean isInSameConnection() {
        return true;
    }

}
