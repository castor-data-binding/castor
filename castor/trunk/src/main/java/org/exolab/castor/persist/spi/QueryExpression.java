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


package org.exolab.castor.persist.spi;


import org.exolab.castor.jdo.DbMetaInfo;

import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.oql.SyntaxNotSupportedException;


/**
 * Defines the interface for a query expression. The query
 * expression object is used to construct queries including
 * parameters, conditions and joins, and generates the SQL
 * statement for the underlying database.
 * <p>
 * A query experession object is created for each unique
 * query, populated with the query parameters and the
 * SQL statement is obtained from it at the proper time.
 * <p>
 * A query expression is generated from {@link PersistenceFactory},
 * see this interface for information on how to configure
 * it. The operators defined in this interface are part of
 * SQL 92 and the supported OQL syntax and are expected to
 * be supported by all query expressions.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public interface QueryExpression
{


    /**
     * Equality operator. (<tt>=</tt>)
     */
    public String OpEquals = "=";

    /**
     * Inequality operator. (<tt>&lt;&gt;</tt>)
     */
    public String OpNotEquals = "<>";

    /**
     * Greater then operator. (<tt>&gt;</tt>)
     */
    public String OpGreater = ">";

    /**
     * Greater then or equals operator. (<tt>&gt;=</tt>)
     */
    public String OpGreaterEquals = ">=";

    /**
     * Less then operator. (<tt>&lt;</tt>)
     */
    public String OpLess = "<";

    /**
     * Less then or equals operator. (<tt>&lt;=</tt>)
     */
    public String OpLessEquals = "<=";

    /**
     * Like operator. (<tt>LIKE</tt>)
     */
    public String OpLike = " LIKE ";

    /**
     * Not like operator. (<tt>NOT LIKE</tt>)
     */
    public String OpNotLike = " NOT LIKE ";

    /**
     * Between operator. (<tt>BETWEEN</tt>)
     *
     * @see #OpBetweenAnd
     */
    public String OpBetween = " BETWEEN ";

    /**
     * Between and operator. (<tt>AND</tt>)
     *
     * @see #OpBetween
     */
    public String OpBetweenAnd = " AND ";


    /**
     * Add a column used in the query. Columns must be retrieved in
     * the same order in which they were added to the query.
     *
     * @param tableName The table name
     * @param columnName The column name
     */    
    public void addColumn( String tableName, String columnName );

    /**
     * Add an entire select clause to the query with one call. The caller is
     * responsible for making sure that all mentioned tables are included in
     * the from clause.
     *
     * @param selectClause The entire sql select clause without the word SELECT
     */
    public void addSelect( String selectClause );

    /**
     * Set the query to be distinct.  No two rows which are the same will be returned.
     *
     * @param distinct If the query should include DISTINCT in the SQL select.
     */
    public void setDistinct( boolean distinct );


    /**
     * Add a table to the from clause.
     *
     * @param tableName The name of the table to add to the select clause
     */
    public void addTable( String tableName );


    /**
     * Add a table with an alias to the from clause.
     *
     * @param tableName The name of the table to add to the select clause
     * @param tableAlias The name of the alias under which the where clauses will access it
     */
    public void addTable( String tableName, String tableAlias );


    /**
     * Add a query paramater.
     *
     * @param tableName The table name
     * @param columnName The column name
     * @param condOp The conditional operation
     */
    public void addParameter( String tableName, String columnName, String condOp );


    /**
     * Add a condition.
     *
     * @param tableName The table name
     * @param columnName The column name
     * @param condOp The conditional operation
     * @param value The conditional value
     */
    public void addCondition( String tableName, String columnName,
                              String condOp, String value );

    /**
     * Encode a TableColumn for use in expressions
     *
     * @param tableName The table name
     * @param columnName The column name
     */
    public String encodeColumn( String tableName, String columnName );

    /**
     * Adds a where clause.  Caller is responsible for making sure all tables
     * mentioned in the where clause are included in the fromClause.
     *
     * @param whereClause The WHERE clause to add (without the word WHERE).
     */
    public void addWhereClause( String whereClause );
    

    /**
     * Adds an order by clause.  Caller is responsible for making sure all 
     * tables mentioned in the order by clause are included in the fromClause.
     *
     * @param orderClause The ORDER BY clause to add (without the words 
     *    ORDER BY).
     */
    public void addOrderClause( String orderClause );
    

    /**
     * Adds an limit clause. 
     * @param limitClause The LIMIT clause to add (without the word LIMIT).
     * @throws SyntaxNotSupportedException If the LIMIT clause is not supported by the RDBMS.
     */
    public void addLimitClause( String limitClause )
    	throws SyntaxNotSupportedException;  

    /**
     * Adds an offset clause. 
     * @param offsetClause The OFFSET clause to add (without the word OFFSET).
     * @throws SyntaxNotSupportedException If the OFFSET clause is not supported by the RDBMS.
     */
    public void addOffsetClause( String offsetClause )
    	throws SyntaxNotSupportedException;  

    /**
     * Add an inner join.
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column name on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column name on the right side
     */
    public void addInnerJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn );


    /**
     * Add an inner join with an aliases for the tables
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column name on the left side
     * @param leftTableAlias The alias name to use for the table on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column name on the right side
     * @param rightTableAlias The alias name to use for the table on the right side
     */
    public void addInnerJoin( String leftTable, String leftColumn, String leftTableAlias,
                              String rightTable, String rightColumn, String rightTableAlias );


    /**
     * Add an outer join. May use an inner join if outer
     * joins are not supported.
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column name on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column name on the right side
     */
    public void addOuterJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn );


    /**
     * Add an outer join. May use an inner join if outer
     * joins are not supported.
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column name on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column name on the right side
     * @param rightTableAlias The alias name to use for the table on the right side
     */
    public void addOuterJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn, String rightTableAlias );


    /**
     * Add an inner join
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column names on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column names on the right side
     */
    public void addInnerJoin( String leftTable, String[] leftColumn,
                              String rightTable, String[] rightColumn );


    /**
     * Add an inner join with an aliases for the tables
     *
     * @param leftTable The table name on the left side
     * @param leftColumn The column names on the left side
     * @param leftTableAlias The alias name to use for the table on the left side
     * @param rightTable The table name on the right side
     * @param rightColumn The column names on the right side
     * @param rightTableAlias The alias name to use for the table on the right side
     */
    public void addInnerJoin( String leftTable, String[] leftColumn, String leftTableAlias,
                              String rightTable, String[] rightColumn, String rightTableAlias );


    public void addOuterJoin( String leftTable, String[] leftColumn,
                              String rightTable, String[] rightColumn );

    public void addOuterJoin( String leftTable, String[] leftColumn,
                              String rightTable, String[] rightColumn, String rightTableAlias );

    /**
     * Return the query expression as an SQL statement. The resulting
     * SQL is fed directly to a JDBC statement. <tt>writeLock</tt> is
     * true if the query must obtain a write lock on the queried table.
     *
     * @param writeLock True if a write lock is required
     * @return The SQL statement
     * @throws QueryException The query cannot be constructed for
     *  this engine
     */
    public String getStatement( boolean writeLock )
        throws QueryException;

    /**
     * Returns a clone of the query expression that can be further
     * modified.
     */
    public Object clone();
    
    /**
     * Indicates whether a RDBMS supports LIMIT clauses.
     * @return True if a LIMIT clause is supported.
     */
    public boolean isLimitClauseSupported();
    
    /**
     * Indicates whether a RDBMS supports OFFSET clauses.
     * @return True if an OFFSET clause is supported.
     */
    public boolean isOffsetClauseSupported();

    /**
     * Store database meta information
     * @param dbInfo DbMetaInfo instance
     */    
    public void setDbMetaInfo( DbMetaInfo dbInfo );

}
