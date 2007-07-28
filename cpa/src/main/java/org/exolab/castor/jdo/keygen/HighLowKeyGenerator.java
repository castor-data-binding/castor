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
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * The parent abstract class for HIGH-LOW key generators
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 * @see HighLowKeyGeneratorFactory
 */
public class HighLowKeyGenerator implements KeyGenerator {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(HighLowKeyGenerator.class);
    
    private final static BigDecimal ONE = new BigDecimal( 1 );

    private final static String SEQ_TABLE = "table";

    private final static String SEQ_KEY = "key-column";

    private final static String SEQ_VALUE = "value-column";

    private final static String GRAB_SIZE = "grab-size";

    private final static String SAME_CONNECTION = "same-connection";

    private final static String GLOBAL = "global";

    private final PersistenceFactory _factory;
    
    private final int _sqlType;

    // Sequence table name 
    private final String _seqTable;

    // Sequence table key column name
    private final String _seqKey;

    // Sequence table value column name
    private final String _seqValue;

    // grab size as int
    private int _grabSizeI;

    // flag of use of the same connection
    // (less efficient, but in EJB envirinment we have no choice for now) 
    private boolean _sameConnection;

    // grab size as BigDecimal
    private BigDecimal _grabSizeD;

    // last generated values
    private Hashtable _lastValues = new Hashtable();
    
    // maximum possible values after which database operation is needed
    private Hashtable _maxValues = new Hashtable();

    // to generate globally unique identities
    private boolean _global;


    /**
     * Initialize the HIGH-LOW key generator.
     */
    public HighLowKeyGenerator(PersistenceFactory factory,  Properties params, int sqlType)
    throws MappingException {
        String factorStr;

        _factory = factory;
        _sqlType = sqlType;
        supportsSqlType( sqlType );

        _seqTable = params.getProperty( SEQ_TABLE );
        if ( _seqTable == null ) 
            throw new MappingException( Messages.format( "mapping.KeyGenParamNotSet",
                                        SEQ_TABLE, getClass().getName() ) );

        _seqKey = params.getProperty( SEQ_KEY );
        if ( _seqKey == null ) 
            throw new MappingException( Messages.format( "mapping.KeyGenParamNotSet",
                                        SEQ_KEY, getClass().getName() ) );

        _seqValue = params.getProperty( SEQ_VALUE );
        if ( _seqValue == null ) 
            throw new MappingException( Messages.format( "mapping.KeyGenParamNotSet",
                                        SEQ_VALUE, getClass().getName() ) );

        factorStr = params.getProperty( GRAB_SIZE, "10" );
        try {
            _grabSizeI = Integer.parseInt( factorStr );
        } catch ( NumberFormatException except ) {
            _grabSizeI = 0;
        }
        if ( _grabSizeI <= 0 ) 
            throw new MappingException( Messages.format( "mapping.wrongKeyGenParam",
                                        factorStr, GRAB_SIZE, getClass().getName() ) );
        _grabSizeD = new BigDecimal( _grabSizeI );
        _sameConnection = "true".equals( params.getProperty( SAME_CONNECTION ) );
        _global = "true".equals( params.getProperty( GLOBAL ) );
    }

    /**
     * Determine if the key generator supports a given sql type.
     *
     * @param sqlType
     * @throws MappingException
     */
    public void supportsSqlType( int sqlType ) throws MappingException {
        if ( sqlType != Types.INTEGER && sqlType != Types.NUMERIC && sqlType != Types.DECIMAL && sqlType != Types.BIGINT) {
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
    public synchronized Object generateKey(Connection conn, String tableName,
            String primKeyName, Properties props) throws PersistenceException {
        Object last;
        Object max;
        boolean inRange;

        if ( _global ) {
            tableName = "<GLOBAL>";
        }
        last = _lastValues.get( tableName );
        max = _maxValues.get( tableName );
        if ( last != null ) {    
            if ( _sqlType == Types.INTEGER )
                last = new Integer( ( (Integer) last ).intValue() + 1 );
            else if ( _sqlType == Types.BIGINT )
                last = new Long( ( (Long) last ).longValue() + 1 );
            else
                last = ((BigDecimal) last).add( ONE );
        } else {
            QueryExpression query;
            String sql;
            String sql2;
            PreparedStatement stmt = null;
            PreparedStatement stmt2 = null;
            ResultSet rs;
            boolean success;

            try {
                // the separate connection should be committed/rolled back at this point
                if ( ! _sameConnection ) 
                    conn.rollback();

                // Create SQL sentence of the form
                // "SELECT seq_val FROM seq_table WHERE seq_key='table'"
                // with database-dependent keyword for lock
                // [george stewart] Note, that some databases (InstantDB, 
                // HypersonicSQL) don't support such locks.
                query = _factory.getQueryExpression();
                query.addColumn( _seqTable, _seqValue );
                query.addCondition( _seqTable, _seqKey, QueryExpression.OP_EQUALS, 
                                    JDBCSyntax.PARAMETER);

                // SELECT and put lock on the last record
                sql = query.getStatement( true );
                // For the case if the "SELECT FOR UPDATE" is not supported
                // we perform dirty checking
                sql2 = "UPDATE "+  _seqTable +
                    " SET " + _seqValue + "=" + JDBCSyntax.PARAMETER +
                    JDBCSyntax.WHERE + _seqKey + QueryExpression.OP_EQUALS +
                    JDBCSyntax.PARAMETER + JDBCSyntax.AND + 
                    _seqValue + "=" + JDBCSyntax.PARAMETER;

                stmt = conn.prepareStatement( sql );
                stmt.setString(1, tableName);
                stmt2 = conn.prepareStatement( sql2 );
                stmt2.setString(2, tableName);

                // Retry 7 times (lucky number)
                success = false;
                for ( int i = 0 ; ! success && i < 7 ; i++ ) {
                    rs = stmt.executeQuery();

                    if ( rs.next() ) {
                        if ( _sqlType == Types.INTEGER ) {
                            int value;
                            int maxVal;

                            value = rs.getInt( 1 );
                            stmt2.setInt(3, value);
                            last = new Integer( value + 1 );
                            maxVal = value + _grabSizeI;
                            max = new Integer( maxVal );
                            stmt2.setInt(1, maxVal);
                        } else if ( _sqlType == Types.BIGINT ) {
                            long value;
                            long maxVal;

                            value = rs.getLong( 1 );
                            stmt2.setLong(3, value);
                            last = new Long( value + 1 );
                            maxVal = value + _grabSizeI;
                            max = new Long( maxVal );
                            stmt2.setLong(1, maxVal);
                        } else {
                            BigDecimal value;
                            BigDecimal maxVal;

                            value = rs.getBigDecimal( 1 );
                            stmt2.setBigDecimal(3, value);
                            last = value.add( ONE );
                            maxVal = value.add( _grabSizeD );
                            max = maxVal;
                            stmt2.setBigDecimal(1, maxVal);
                        }
                        // For the case if the "SELECT FOR UPDATE" is not supported
                        // we perform dirty checking
                        success = (stmt2.executeUpdate() == 1);
                    } else {
                        // [Terry Child] Initialize the counter with MAX(pk) + 1
                        // for the case of switching from some other key generator 
                        // to HIGH-LOW
                        stmt.close();
                        if ( ! _global ) {
                        	String sqlStatement = JDBCSyntax.SELECT + "MAX(" + primKeyName + ") FROM " + tableName;
                            stmt = conn.prepareStatement(sqlStatement);
                            rs = stmt.executeQuery();
                        }
                        if ( _sqlType == Types.INTEGER ) {
                            int maxPK = 0;

                            if ( ! _global && rs.next() ) {
                                maxPK = rs.getInt(1);
                            }
                            last = new Integer( maxPK + 1 );
                            max = new Integer( maxPK + _grabSizeI );
                        } else if ( _sqlType == Types.BIGINT ) {
                            long maxPK = 0;

                            if ( ! _global && rs.next() ) {
                                maxPK = rs.getLong(1);
                            }
                            last = new Long( maxPK + 1 );
                            max = new Long( maxPK + _grabSizeI );
                        } else {
                            BigDecimal maxPK = null;

                            if ( ! _global && rs.next() ) {
                                maxPK = rs.getBigDecimal(1);
                            }
                            if ( maxPK == null ) {
                                maxPK = new BigDecimal( 0 );
                            }
                            last = maxPK.add( ONE );
                            max = maxPK.add( _grabSizeD );
                        }
                        stmt2.close();
                        
                        String sqlStatement = "INSERT INTO " + _seqTable + " (" + _seqKey + "," + _seqValue + ") VALUES (?, ?)";
                        stmt2 = conn.prepareStatement(sqlStatement);
                        stmt2.setString( 1, tableName );
                        stmt2.setObject( 2, max );
                        stmt2.executeUpdate();
                        success = true;
                    }
                }
                if ( ! _sameConnection ) {
                    if ( success )
                        conn.commit();
                    else {
                        conn.rollback();
                    }
                }
                if ( ! success )
                    throw new PersistenceException( Messages.format( "persist.keyGenFailed", getClass().getName() ) );
            } catch ( SQLException ex ) {
                if ( ! _sameConnection ) {
                    try {
                        conn.rollback();
                    } catch ( SQLException ex2 ) {
                        _log.warn ("Problem rolling back JDBC transaction.", ex2);
                    }
                }
                throw new PersistenceException( Messages.format(
                        "persist.keyGenSQL", getClass().getName(), ex.toString() ), ex );
            } finally {
                if ( stmt != null ) {
                    try {
                        stmt.close();
                    } catch ( SQLException ex ) {
                        _log.warn (Messages.message("persist.stClosingFailed"), ex);
                    }
                }
                if ( stmt2 != null ) {
                    try {
                        stmt2.close();
                    } catch ( SQLException ex ) {
                        _log.warn (Messages.message("persist.stClosingFailed"), ex);
                    }
                }
            }
        }

        if ( _sqlType == Types.INTEGER )
            inRange = ( ( (Integer) last ).intValue() < ( (Integer) max ).intValue() );
        else if ( _sqlType == Types.BIGINT )
            inRange = ( ( (Long) last ).longValue() < ( (Long) max ).longValue() );
        else
            inRange = ( ( (BigDecimal) last ).compareTo( (BigDecimal) max ) < 0 );

        if ( inRange ) {
            _lastValues.put( tableName, last );
            _maxValues.put( tableName, max );
        } else {
            _lastValues.remove( tableName );
            _maxValues.remove( tableName );
        }
        return last;
    }


    /**
     * Style of key generator: BEFORE_INSERT, DURING_INSERT or AFTER_INSERT ? 
     */
    public final byte getStyle() {
        return BEFORE_INSERT;
    }


    /**
     * Gives a possibility to patch the Castor-generated SQL statement
     * for INSERT (makes sense for DURING_INSERT key generators)
     */
    public final String patchSQL( String insert, String primKeyName ) {
        return insert;
    }


    /**
     * Is key generated in the same connection as INSERT?
     */
    public final boolean isInSameConnection() {
        return _sameConnection;
    }

}

