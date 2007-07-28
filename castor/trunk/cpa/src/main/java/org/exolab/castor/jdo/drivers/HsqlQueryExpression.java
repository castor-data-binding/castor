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


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;


/**
 * QueryExpression for MySQL.
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2005-07-05 07:25:41 -0600 (Tue, 05 Jul 2005) $
 */
public final class HsqlQueryExpression extends JDBCQueryExpression {
    public HsqlQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    public String getStatement(final boolean lock) {
        return getStandardStatement( lock, false ).toString();
    }
    
    /**
     * Helper method. Can be used in two cases:
     * 1) for JDBC drivers which support "{oj ...OUTER JOIN ...}" notation (in accordance with
     * JDBC specification);
     * 2) for the databases which support "... OUTER JOIN ..." notation (in accordance with
     * SQL-92 standard); .
     * @param lock whether to lock selected tables
     * @param oj true in the first case above, false in the second case.
     **/
    protected StringBuffer getStandardStatement(final boolean lock, final boolean oj) {
        StringBuffer sql;
        Enumeration  enumeration;
        boolean      first;
        Hashtable    tables;
        Vector       done = new Vector();
        String       tableName;
        String       tableAlias;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.SELECT );

        if ( _limit != null ) {
            if (_offset != null) {
                sql.append(JDBCSyntax.LIMIT);
                sql.append(_offset).append(" "); // hsqldb doesn't use comma
                sql.append(_limit).append(" ");
            } else {
                sql.append(" TOP ").append(_limit).append(" ");
            }
        }

        if ( _distinct )
          sql.append( JDBCSyntax.DISTINCT );

        if ( _select == null )
          sql.append( getColumnList() );
        else
          sql.append( _select).append(" ");

        sql.append( JDBCSyntax.FROM );

        // Use outer join syntax for all outer joins. Inner joins come later.
        tables = (Hashtable) _tables.clone();
        first = true;
        // gather all outer joins with the same left part
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            join = (Join) _joins.elementAt( i );

            if ( ! join.outer || done.contains( join.leftTable ) )
                continue;
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TABLE_SEPARATOR );
            if ( oj )
                sql.append( "{oj " );
            sql.append(  _factory.quoteName( join.leftTable ) );
            sql.append( JDBCSyntax.LEFT_JOIN );
            tableName = (String) tables.get( join.rightTable );
            if (join.rightTable.equals(tableName)) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( join.rightTable ) );
            }
            sql.append( JDBCSyntax.ON );
            for (int j = 0 ; j < join.leftColumns.length ; ++j) {
                if ( j > 0 )
                    sql.append( JDBCSyntax.AND );
                sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                join.leftColumns[ j ] ) ).append( OP_EQUALS );
                sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                join.rightColumns[ j ] ) );
            }

            tables.remove( join.leftTable );
            tables.remove( join.rightTable );
            for ( int k = i + 1 ; k < _joins.size() ; ++k ) {
                Join join2;

                join2 = (Join) _joins.elementAt( k );
                if ( ! join2.outer || ! join.leftTable.equals( join2.leftTable ) )
                    continue;
                sql.append( JDBCSyntax.LEFT_JOIN );
                tableName = (String) tables.get( join2.rightTable );

                if (join2.rightTable.equals(tableName)) {
                    sql.append( _factory.quoteName( tableName ) );
                } else {
                    sql.append( _factory.quoteName( tableName ) + " " +
                                _factory.quoteName( join2.rightTable ) );
                }
                sql.append( JDBCSyntax.ON );
                for ( int j = 0 ; j < join2.leftColumns.length ; ++j ) {
                    if ( j > 0 )
                        sql.append( JDBCSyntax.AND );
                    sql.append( _factory.quoteName( join2.leftTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                    join2.leftColumns[ j ] ) ).append( OP_EQUALS );
                    sql.append( _factory.quoteName( join2.rightTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                    join2.rightColumns[ j ] ) );
                }
                tables.remove( join2.rightTable );
            }
            if ( oj )
                sql.append( "}" );
            done.addElement( join.leftTable );
        }
        enumeration = tables.keys();
        while ( enumeration.hasMoreElements() ) {
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TABLE_SEPARATOR );
            tableAlias = (String) enumeration.nextElement();
            tableName = (String) tables.get( tableAlias );
            if (tableAlias.equals(tableName)) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( tableAlias ) );
            }
        }

        // Use standard join syntax for all inner joins
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            join = (Join) _joins.elementAt( i );
            if ( ! join.outer ) {
                if ( first ) {
                    sql.append( JDBCSyntax.WHERE );
                    first = false;
                } else
                    sql.append( JDBCSyntax.AND );
                for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                    if ( j > 0 )
                        sql.append( JDBCSyntax.AND );
                    sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                    join.leftColumns[ j ] ) ).append( OP_EQUALS );
                    sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                    join.rightColumns[ j ] ) );
                }
            }
        }
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.ORDER_BY).append(_order);

        // There is no standard way to lock selected tables.
        return sql;
    }

    /** 
     * Provides an implementation of {@link QueryExpression#isLimitClauseSupported()}.
     * @return true to indicate that this feature is supported by mySQL. 
     * @see org.exolab.castor.persist.spi.QueryExpression#isLimitClauseSupported()
     */
    public boolean isLimitClauseSupported() {
    	return true;
    }
    
    /** 
     * Provides an implementation of {@link QueryExpression#isOffsetClauseSupported()}. 
     * @return true to indicate that this feature is supported by mySQL. 
     * @see org.exolab.castor.persist.spi.QueryExpression#isOffsetClauseSupported()
     */
    public boolean isOffsetClauseSupported() {
    	return true;
    }

}
