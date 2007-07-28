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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.jdo.oql.SyntaxNotSupportedException;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for Oracle 7/8.
 *
 * As of release 8.1.6, Oracle supports limiting the number of rows returned by a query
 * through the use of the following mechanism.
 * 
 * SELECT *
 * FROM (
 *	    SELECT  empno, ename, job, sal,
 *	            rank() over (order by sal) rnk
 *	    FROM    emp
 *	)
 *	WHERE   rnk BETWEEN 3 AND 8
 * 
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-03-24 03:45:01 -0700 (Thu, 24 Mar 2005) $
 */
public final class OracleQueryExpression extends JDBCQueryExpression {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log _log = LogFactory.getFactory().getInstance( OracleQueryExpression.class );

    public OracleQueryExpression( PersistenceFactory factory ) {
        super( factory );
    }

    public String getStatement( boolean lock ) throws SyntaxNotSupportedException {
        StringBuffer sql;
        boolean      first;
        Enumeration  enumeration;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.SELECT );
        if ( _distinct )
          sql.append( JDBCSyntax.DISTINCT );

        sql.append( getColumnList() );

        // add support for OQL LIMIT/OFFSET - part1
        if (_limit != null) {
        	sql.append (" , rank() over ( ");
            // add ORDER BY clause
            if ( _order != null ) {
            	sql.append(JDBCSyntax.ORDER_BY).append(_order);
            } else {
            	throw new SyntaxNotSupportedException ("To use a LIMIT clause with Oracle, an ORDER BY clause is required.");
            }
			sql.append ( " ) rnk ");
        }

        sql.append( JDBCSyntax.FROM );
        // Add all the tables to the FROM clause
        enumeration = _tables.keys();
        while ( enumeration.hasMoreElements() ) {
            String tableAlias = (String) enumeration.nextElement();
            String tableName = (String) _tables.get( tableAlias );
            if( tableAlias.equals( tableName ) ) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( tableAlias ) );
            }
            if ( enumeration.hasMoreElements() )
                sql.append( JDBCSyntax.TABLE_SEPARATOR );
        }
        first = true;
        // Use asterisk notation to denote a left outer join
        // and equals to denote an inner join
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            if ( first ) {
                sql.append( JDBCSyntax.WHERE );
                first = false;
            } else
                sql.append( JDBCSyntax.AND );

            join = (Join) _joins.elementAt( i );
            for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                if ( j > 0 )
                    sql.append( JDBCSyntax.AND );

                sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                join.leftColumns[ j ] ) );

                //if ( join.outer )
                //    sql.append( "(+)=" );
                //else
                //    sql.append( OpEquals );
                sql.append( OP_EQUALS );
                sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                                join.rightColumns[ j ] ) );
                if ( join.outer )
                    sql.append( "(+)" );
            }
        }
        first = addWhereClause( sql, first );
        
        // add ORDER BY clause, but only if no LIMIT clause has been specified
        if ( _order != null && _limit == null)
          sql.append(JDBCSyntax.ORDER_BY).append(_order);

        // Use FOR UPDATE to lock selected tables.
        if ( lock )
            sql.append( " FOR UPDATE" );

        // add LIMIT/OFFSET clause - part 2
        if ( _limit != null ) {
        	sql.insert (0, "select * from ( ");	// leads to problems when used with Castor's outer joins for master-details queries: "ORA-00918: Spalte nicht eindeutig definiert"

        	if ( _offset != null ) {
        		sql.append (" ) where rnk - " + _offset + " between 1 and " + _limit + " ");
        	} else {
        		sql.append (" ) where rnk <= " + _limit + " ");
        	}
        }

        if(_log.isDebugEnabled()) {
            _log.debug ("SQL statement = " + sql.toString());
        }
        return sql.toString();
    }


    /**
     * Indicates that Oracle supports an OQL LIMIT clause for versions >= 8.1.6.
     * 
     * @return true to indicate that Oracle supports an OQL LIMIT clause.
     */
	public boolean isLimitClauseSupported() {
	    return _dbInfo!=null? _dbInfo.compareDbVersion("8.1.6")>=0: false;
	}

    /**
     * Indicates that Oracle supports an OQL OFFSET clause for versions >= 8.1.6.
     * 
     * @return true to indicate that Oracle supports an OQL OFFSET clause.
     */
	public boolean isOffsetClauseSupported() {
	    return _dbInfo!=null? _dbInfo.compareDbVersion("8.1.6")>=0: false;
	}
}
