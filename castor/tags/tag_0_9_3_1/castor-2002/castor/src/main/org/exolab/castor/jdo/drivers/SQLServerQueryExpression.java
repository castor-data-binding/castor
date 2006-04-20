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
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for MS SQL Server
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date$
 */
public final class SQLServerQueryExpression
    extends JDBCQueryExpression
{


    public SQLServerQueryExpression( PersistenceFactory factory )
    {
        super( factory );
    }


    public String getStatement( boolean lock )
    {
        StringBuffer sql;
        boolean      first;
        Enumeration  enum;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );

        if ( _limit != null )
          if ( _limit.equals("") == false )
            sql.append("TOP ").append(_limit).append(" ");

        sql.append( getColumnList() );

        sql.append( JDBCSyntax.From );

        // Use HOLDLOCK to lock selected tables.
        enum = _tables.keys();
        while ( enum.hasMoreElements() ) {
            String tableAlias = (String) enum.nextElement();
            String tableName = (String) _tables.get( tableAlias );
            if( tableAlias.equals( tableName ) ) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( tableAlias ) );
            }
            if ( lock )
                sql.append( " HOLDLOCK " );
            if ( enum.hasMoreElements() )
                sql.append( JDBCSyntax.TableSeparator );
        }

        first = true;
        // Use asterisk notation to denote a left outer join
        // and equals to denote an inner join
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

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
                if ( join.outer )
                    sql.append( "*=" );
                else
                    sql.append( OpEquals );
                sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                join.rightColumns[ j ] ) );
            }
        }
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);

        return sql.toString();
    }


}


