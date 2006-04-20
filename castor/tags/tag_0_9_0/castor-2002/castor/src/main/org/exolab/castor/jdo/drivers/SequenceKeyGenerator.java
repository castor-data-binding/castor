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


package org.exolab.castor.jdo.drivers;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.util.Messages;

/**
 * SEQUENCE key generator.
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 * @see SequenceKeyGeneratorFactory
 */
public final class SequenceKeyGenerator implements KeyGenerator
{


    protected final PersistenceFactory _factory;


    protected final String _seqName;


    private final byte _style;


    private final int _sqlType;


    private int _increment;


    private boolean _triggerPresent;


    /**
     * Initialize the SEQUENCE key generator.
     */
    public SequenceKeyGenerator( PersistenceFactory factory,
            Properties params, int sqlType )
            throws MappingException
    {
        String fName = factory.getFactoryName();
        boolean returning = "true".equals( params.getProperty("returning") );
        _triggerPresent = "true".equals( params.getProperty("trigger","false") );


        if ( ! fName.equals( "oracle" ) && ! fName.equals( "postgresql" ) && ! fName.equals( "interbase" ) && ! fName.equals( "sapdb" ) ) {
            throw new MappingException( Messages.format( "mapping.keyGenNotCompatible",
                                        getClass().getName(), fName ) );
        }
        if ( ( fName.equals( "postgresql" ) || fName.equals( "interbase" ) || fName.equals( "sapdb" ) )
                && returning ) {
            throw new MappingException( Messages.format( "mapping.keyGenParamNotCompat",
                                        "returning=\"true\"", getClass().getName(), fName ) );
        }
        _factory = factory;
        _seqName = params.getProperty("sequence", "{0}_seq");
        _style = ( fName.equals( "postgresql" ) || fName.equals("interbase") ? BEFORE_INSERT :
                                   ( returning  ? DURING_INSERT : AFTER_INSERT) );

        if (_triggerPresent && _style==BEFORE_INSERT)
            throw new MappingException( Messages.format( "mapping.keyGenParamNotCompat",
                                        "trigger=\"true\"", getClass().getName(), fName ) );

        _sqlType = sqlType;
        if (sqlType != Types.INTEGER && sqlType != Types.NUMERIC && sqlType != Types.DECIMAL && sqlType != Types.BIGINT)
            throw new MappingException( Messages.format( "mapping.keyGenSQLType",
                                        getClass().getName(), new Integer( sqlType ) ) );
        try {
            _increment = Integer.parseInt(params.getProperty("increment","1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
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
    public Object generateKey( Connection conn, String tableName, String primKeyName,
            Properties props )
            throws PersistenceException
    {
        Statement stmt = null;
        ResultSet rs;
        int value;

        try {
            stmt = conn.createStatement();

            if (_factory.getFactoryName().equals("interbase")) {
                //interbase only does before_insert, and does it its own way
                rs = stmt.executeQuery("select gen_id(" +
                        MessageFormat.format(_seqName, new String[] {tableName}) +
                        "," + _increment + ") from rdb$database");
            } else {
                if ( _style == BEFORE_INSERT ) {
                    rs = stmt.executeQuery( "SELECT nextval('" +
                            MessageFormat.format( _seqName, new String[] {tableName}) + "')" );
                } else {
                    rs = stmt.executeQuery( "SELECT " + _factory.quoteName(
                            MessageFormat.format( _seqName, new String[] {tableName} ) +
                            ".currval") + " FROM " + _factory.quoteName( tableName ) );
                }
            }

            if ( rs.next() ) {
                value = rs.getInt( 1 );
                if ( _sqlType == Types.INTEGER )
                    return new Integer( value );
                else if ( _sqlType == Types.BIGINT )
                    return new Long( value );
                else
                    return new BigDecimal( value );
            } else {
                throw new PersistenceException( Messages.message( "persist.keyGenFailed" ) );
            }
        } catch ( SQLException ex ) {
            throw new PersistenceException( Messages.format(
                    "persist.keyGenSQL", ex.toString() ) );
        } finally {
            if ( stmt != null ) {
                try {
                    stmt.close();
                } catch ( SQLException ex ) {
                }
            }
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
        StringBuffer sb;
        int lp1;  // the first left parenthesis, which starts fields list
        int lp2;  // the second left parenthesis, which starts values list
        char c;

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

        // remove quotes
        if ( tableName.startsWith("\"") ) {
            tableName = tableName.substring( 1, tableName.length() - 1 );
        }
        seqName = MessageFormat.format( _seqName, new String[] {tableName});

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
                sb.insert( lp2 + 1, _factory.quoteName( seqName + ".nextval" ));
                sb.insert( lp1 + 1, "(" + _factory.quoteName( primKeyName ) + ") " );
            } else {
                // don't change the order of lines below,
                // otherwise index becomes invalid
                sb.insert( lp2 + 1, _factory.quoteName( seqName + ".nextval" ) + ",");
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
