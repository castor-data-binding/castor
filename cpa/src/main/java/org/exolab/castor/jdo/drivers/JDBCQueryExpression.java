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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.jdo.oql.SyntaxNotSupportedException;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;


/**
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class JDBCQueryExpression implements QueryExpression {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance( JDBCQueryExpression.class );

    protected Hashtable _tables = new Hashtable();

    protected Vector    _cols = new Vector();

    protected Vector    _conds = new Vector();

    protected Vector    _joins = new Vector();

    protected String    _select;

    protected String    _where;

    protected String    _order;

    protected String    _limit;
    
    protected String    _offset;

    protected boolean   _distinct = false;

    protected PersistenceFactory  _factory;

	/**
	 * MetaInfo as acquired from the RDBMS
	 */
    protected DbMetaInfo _dbInfo;

    public JDBCQueryExpression( PersistenceFactory factory ) {
        _factory = factory;
    }

    /**
     * Store database meta information
     *
     * @param dbInfo DbMetaInfo instance
     */    
    public void setDbMetaInfo( DbMetaInfo dbInfo ) {
        _dbInfo = dbInfo;
    }

    public void setDistinct(boolean distinct) {
        _distinct = distinct;
    }

    public void addColumn( String tableName, String columnName ) {
        _tables.put( tableName, tableName );
        _cols.addElement( _factory.quoteName( tableName + JDBCSyntax.TABLE_COLUMN_SEPARATOR + columnName ) );
    }

    public void addTable( String tableName ) {
        _tables.put( tableName, tableName );
    }

    public void addTable( String tableName, String tableAlias ) {
        _tables.put( tableAlias, tableName );
    }

    public void addParameter( String tableName, String columnName, String condOp ) {
        addCondition( tableName, columnName, condOp, JDBCSyntax.PARAMETER );
    }

    public void addCondition(String tableName, String columnName, String condOp, String value) {
        _tables.put( tableName, tableName );
        _conds.addElement(_factory.quoteName( tableName + JDBCSyntax.TABLE_COLUMN_SEPARATOR + columnName) + condOp + value );
    }

    public String encodeColumn( String tableName, String columnName ) {
        return _factory.quoteName( tableName +
                   JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                   columnName );
    }

    public void addInnerJoin( String leftTable, String leftColumn, String rightTable, String rightColumn ) {
        addInnerJoin(leftTable, leftColumn, leftTable, rightTable, rightColumn, rightTable);
    }

    public void addInnerJoin( String leftTable, String leftColumn, String leftTableAlias, String rightTable, String rightColumn, String rightTableAlias) {
        int index;
        Join join;

        _tables.put( leftTableAlias, leftTable );
        _tables.put( rightTableAlias, rightTable );
        join = new Join( leftTableAlias, leftColumn, rightTableAlias, rightColumn, false );
        index = _joins.indexOf(join);
        if (index < 0) {
            _joins.add(join);
        } else {
            // inner join overrides outer joins
            _joins.set(index, join);
        }
    }

    public void addInnerJoin( String leftTable, String[] leftColumn, String rightTable, String[] rightColumn) {
        addInnerJoin(leftTable, leftColumn, leftTable, rightTable, rightColumn, rightTable);
    }

    public void addInnerJoin( String leftTable, String[] leftColumn, String leftTableAlias, String rightTable, String[] rightColumn, String rightTableAlias) {
        int index;
        Join join;

        _tables.put( leftTableAlias, leftTable );
        _tables.put( rightTableAlias, rightTable );
        join = new Join( leftTableAlias, leftColumn, rightTableAlias, rightColumn, false );
        index = _joins.indexOf(join);
        if (index < 0) {
            _joins.add(join);
        } else {
            // inner join overrides outer joins
            _joins.set(index, join);
        }
    }


    public void addOuterJoin( String leftTable, String leftColumn, String rightTable, String rightColumn) {
        addOuterJoin(leftTable, leftColumn, rightTable, rightColumn, rightTable);
    }

    public void addOuterJoin( String leftTable, String leftColumn, String rightTable, String rightColumn,  String rightTableAlias) {
        int index;
        Join join;

        _tables.put( leftTable, leftTable );
        _tables.put( rightTableAlias, rightTable );
        join = new Join( leftTable, leftColumn, rightTableAlias, rightColumn, true );
        index = _joins.indexOf(join);
        if (index < 0) {
            _joins.add(join);
        }
    }

    public void addOuterJoin( String leftTable, String[] leftColumn, String rightTable, String[] rightColumn) {
        addOuterJoin(leftTable, leftColumn, rightTable, rightColumn, rightTable);
    }

    public void addOuterJoin( String leftTable, String[] leftColumn, String rightTable, String[] rightColumn, String rightTableAlias) {
        int index;
        Join join;

        _tables.put( leftTable, leftTable );
        _tables.put( rightTableAlias, rightTable );
        join = new Join( leftTable, leftColumn, rightTableAlias, rightColumn, true );
        index = _joins.indexOf(join);
        if (index < 0) {
            _joins.add(join);
        }
    }

    public void addSelect( String selectClause ) {
        _select = selectClause;
    }

    public void addWhereClause( String where ) {
        _where = where;
    }

    public void addOrderClause( String order ) {
        _order = order;
    }

    public void addLimitClause( String limit ) throws SyntaxNotSupportedException {
    	if (isLimitClauseSupported()) {
    		_limit = limit;
    	} else {
    		throw new SyntaxNotSupportedException (Messages.format ("query.limitClauseNotSupported", _factory.getFactoryName()));
    	}
    }

    public void addOffsetClause( String offset ) throws SyntaxNotSupportedException {
    	if (isOffsetClauseSupported()) {
    		_offset = offset;
    	} else {
    		throw new SyntaxNotSupportedException (Messages.format ("query.offsetClauseNotSupported", _factory.getFactoryName()));
    	}
    }

    protected String getColumnList() {
        StringBuffer sql;

        if ( _cols.size() == 0  && _select == null )
            return "1";

        sql = new StringBuffer();
        int i = 0;
        for ( i = 0 ; i < _cols.size() ; ++i ) {
            if ( i > 0 )
                sql.append( JDBCSyntax.COLUMN_SEPARATOR );
            sql.append( (String) _cols.elementAt( i ) );
        }

        if ( _select != null )
          if ( i > 0 )
            sql.append( JDBCSyntax.COLUMN_SEPARATOR ).append( _select );
          else
            sql.append( _select );

        return sql.toString();
    }

    protected boolean addWhereClause( StringBuffer sql, boolean first ) {
        if ( _conds.size() > 0 ) {
            if ( first ) {
                sql.append( JDBCSyntax.WHERE );
                first = false;
            } else
                sql.append( JDBCSyntax.AND );
            for ( int i = 0 ; i < _conds.size() ; ++i ) {
                if ( i > 0 )
                    sql.append( JDBCSyntax.AND );
                sql.append( (String) _conds.elementAt( i ) );
            }
        }
        if ( _where != null ) {
            if ( first ) {
                sql.append( JDBCSyntax.WHERE );
                first = false;
            } else
                sql.append( JDBCSyntax.AND );
            sql.append( '(' );
            sql.append( _where );
            sql.append( ')' );
        }
        return first;
    }
    
    /**
	 * Creates a SQL statement.
     * In general, for a RDBMS/JDBC driver with a full support of the SQL standard/JDBC 
	 * specification, this will return a valid SQL statement. For some features,
	 * a particular RDBMS might indicate that it does not support this feature by 
	 * throwing a {@link SyntaxNotSupportedException}.
     * @throws SyntaxNotSupportedException If the RDBMS does not support a particular feature.
     */
    public String getStatement( boolean lock ) throws SyntaxNotSupportedException
    {
        return getStandardStatement( lock, true ).toString();
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
    protected StringBuffer getStandardStatement( boolean lock, boolean oj ) {
        StringBuffer sql;
        Enumeration  enumeration;
        boolean      first;
        Hashtable    tables;
        Vector       done = new Vector();
        String       tableName;
        String       tableAlias;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.SELECT );
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
            if( join.rightTable.equals( tableName ) ) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( join.rightTable ) );
            }
            sql.append( JDBCSyntax.ON );
            for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
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

                if( join2.rightTable.equals( tableName ) ) {
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
            if( tableAlias.equals( tableName ) ) {
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

    public String toString() {
    	StringBuffer buffer = new StringBuffer ();
        try {
			buffer.append ("<").append (getStatement( false )).append(">");
		} catch (SyntaxNotSupportedException e) {
			_log.error ("Problem turning this to a String", e);
		}
		return buffer.toString();
    }


    public Object clone() {
        JDBCQueryExpression clone;

        try {
            clone = (JDBCQueryExpression) getClass().
                getConstructor( new Class[] { PersistenceFactory.class } ).
                newInstance( new Object[] { _factory } );
        } catch ( Exception except ) {
            // This should never happen
            throw new RuntimeException( except.toString() );
        }
        clone._tables = (Hashtable) _tables.clone();
        clone._conds = (Vector) _conds.clone();
        clone._cols = (Vector) _cols.clone();
        clone._joins = (Vector) _joins.clone();
        return clone;
    }


    static class Join {
        final String   leftTable;

        final String[] leftColumns;

        final String   rightTable;

        final String[] rightColumns;

        final boolean  outer;

        Join( String leftTable, String leftColumn, String rightTable, String rightColumn, boolean outer) {
            this.leftTable = leftTable;
            this.leftColumns = new String[] { leftColumn };
            this.rightTable = rightTable;
            this.rightColumns = new String[] { rightColumn };
            this.outer = outer;
        }

        Join( String leftTable, String[] leftColumns, String rightTable, String[] rightColumns, boolean outer) {
            this.leftTable = leftTable;
            this.leftColumns = (String[]) leftColumns.clone();
            this.rightTable = rightTable;
            this.rightColumns =(String[]) rightColumns.clone();
            this.outer = outer;
        }

        public int hashCode() {
            return leftTable.hashCode() ^ rightTable.hashCode();
        }

        public boolean equals(Object obj) {
            Join join = (Join) obj;

            if (!leftTable.equals(join.leftTable) || !rightTable.equals(join.rightTable) ||
                    leftColumns.length != join.leftColumns.length ||
                    rightColumns.length != join.rightColumns.length) {
                return false;
            }
            for (int i = 0; i < leftColumns.length; i++) {
                if (!leftColumns[i].equals(join.leftColumns[i])) {
                    return false;
                }
            }
            for (int i = 0; i < rightColumns.length; i++) {
                if (!rightColumns[i].equals(join.rightColumns[i])) {
                    return false;
                }
            }
            // [oleg] Important: Don't compare "outer" field!
            // We need this to make sure that inner join overrides outer joins,
            // we use Vector.indexOf to solve this problem.
            return true;
        }

        public String toString() {
            return leftTable + "." + leftColumns[0] + (outer ? "*=" : "=") + rightTable + "." + rightColumns[0];

        }
        
        /**
         * Indicates whether the join in question is an outer join
         * @return True if it is an outer join.
         */
        public boolean isOuter() {
            return this.outer;
        }
    }

    /** 
     * Provides a default implementation of {@link QueryExpression#isLimitClauseSupported()}.
     * 
     * @return false to indicate that this feature is not supported by default. 
	 * @see org.exolab.castor.persist.spi.QueryExpression#isLimitClauseSupported()
	 */
	public boolean isLimitClauseSupported() {
		return false;
	}

    /** 
     * Provides a default implementation of {@link QueryExpression#isOffsetClauseSupported()}.
     * 
     * @return false to indicate that this feature is not supported by default. 
	 * @see org.exolab.castor.persist.spi.QueryExpression#isOffsetClauseSupported()
	 */
	public boolean isOffsetClauseSupported() {
		return false;
	}
}
