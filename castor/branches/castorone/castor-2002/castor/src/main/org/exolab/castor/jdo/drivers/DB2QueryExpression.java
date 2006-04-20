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
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for DB 2.
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class DB2QueryExpression
    extends JDBCQueryExpression
{


    public DB2QueryExpression( PersistenceFactory factory )
    {
        super( factory );
    }


    public String getStatement( boolean lock )
    {
        StringBuffer sql;
        Enumeration  enum;
        boolean      first;
        Hashtable    tables;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );

        if ( _select == null )  
          sql.append( getColumnList() );
        else
          sql.append( _select).append(" ");
        
        sql.append( JDBCSyntax.From );

        /* Thomas Fach reported that this variant doesn't work
        // Use join syntax for all joins (LEFT OUTER and INNER).
        // Tables the appear in the join are removed from the
        // tables list in the FROM clause.
        */
        // Use outer join syntax for all outer joins. Inner joins come later.
        tables = (Hashtable) _tables.clone();
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            join = (Join) _joins.elementAt( i );

            if ( ! join.outer ) 
                continue;
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TableSeparator );
            sql.append(  _factory.quoteName( join.leftTable ) );
            if ( join.outer )
                sql.append( JDBCSyntax.LeftJoin );
            else
                sql.append( JDBCSyntax.InnerJoin );
            sql.append(  _factory.quoteName( join.rightTable ) ).append( JDBCSyntax.On );
            for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                if ( j > 0 )
                    sql.append( JDBCSyntax.And );
                sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                                join.leftColumns[ j ] ) ).append( OpEquals );
                sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                join.rightColumns[ j ] ) );
            }
            
            tables.remove( join.leftTable );
            tables.remove( join.rightTable );
        }
        enum = tables.elements();
        while ( enum.hasMoreElements() ) {
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TableSeparator );
            sql.append( _factory.quoteName( (String) enum.nextElement() ) );
        }

        // Use standard join syntax for all inner joins 
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;
            
            join = (Join) _joins.elementAt( i );
            if ( ! join.outer ) {
                if ( first ) {
                    sql.append( JDBCSyntax.Where );
                    first = false;
                } else
                    sql.append( JDBCSyntax.And );
                for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                    if ( j > 0 )
                        sql.append( JDBCSyntax.And );
                    sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                                    join.leftColumns[ j ] ) ).append( OpEquals );
                    sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                    join.rightColumns[ j ] ) );
                }
            }
        } 
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);
          
        // Do not use FOR UPDATE to lock query.
        return sql.toString();
    }


}


