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
import java.util.Vector;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for SAP DB.
 *
 * @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public final class SapDbQueryExpression
    extends JDBCQueryExpression
{


    public SapDbQueryExpression( PersistenceFactory factory )
    {
        super( factory );
    }


    public String getStatement( boolean lock )
    {
        Join         join;
        StringBuffer sql;
        boolean      first;
        Enumeration  enum;
        int          size;
        Vector       sorted = new Vector();

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );

        sql.append( getColumnList() );

        sql.append( JDBCSyntax.From );
        // Add all the tables to the FROM clause
        // They should go in the special order: the table from the left side of outer join
        // should go before the table from the right side.
        // first add elements that participate in outer joins
        enum = _joins.elements();
        while ( enum.hasMoreElements() ) {
            int left;
            int right;

            join = (Join) enum.nextElement();
            if ( ! join.outer ) {
                continue;
            }
            left = sorted.indexOf(join.leftTable);
            right = sorted.indexOf(join.rightTable);
            if (left >= 0 && right >= 0) {
                if (left > right) {
                    sorted.removeElement(join.leftTable);
                    sorted.insertElementAt(join.leftTable, right);
                }
            } else if (left < 0 && right >= 0) {
                sorted.insertElementAt(join.leftTable, right);
            } else if (left >= 0 && right < 0) {
                sorted.insertElementAt(join.rightTable, left + 1);
            } else { // (left < 0 && right < 0)
                sorted.addElement(join.leftTable);
                sorted.addElement(join.rightTable);
            }
        }
        // now add elements that don't participate in outer joins
        enum = _tables.elements();
        while ( enum.hasMoreElements() ) {
            Object name;

            name = enum.nextElement();
            if (!sorted.contains(name)) {
                sorted.addElement(name);
            }
        }
        // Append all the tables (from sorted) to the sql string.
        enum = sorted.elements();
        while ( enum.hasMoreElements() ) {
            String tableAlias = (String) enum.nextElement();
            String tableName = (String) _tables.get( tableAlias );
            if( tableAlias.equals( tableName ) ) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( tableAlias ) );
            }
            if ( enum.hasMoreElements() )
                sql.append( JDBCSyntax.TableSeparator );
        }
        first = true;
        // Use asterisk notation to denote a left outer join
        // and equals to denote an inner join
        size = _joins.size();
        for ( int i = 0 ; i < size; ++i ) {
            if ( first ) {
                sql.append( JDBCSyntax.Where );
                first = false;
            } else
                sql.append( JDBCSyntax.And );

            join = (Join) _joins.elementAt( i );
            for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                if ( j > 0 )
                    sql.append( JDBCSyntax.And );

                sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                                join.leftColumns[ j ] ) );

                sql.append( OpEquals );
                sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                join.rightColumns[ j ] ) );
                if ( join.outer )
                    sql.append( "(+)" );
            }
        }
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);

        // Use WITH LOCK to lock selected tables.
        if ( lock ) {
            sql.append( " WITH LOCK" );
        }
        return sql.toString();
    }
    

}


