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
 */


package org.exolab.castor.jdo.drivers;


import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for HypersonicSQL.
 * <p>
 * This implementation was created mainly to allow the use of multiple outer
 * joins during HSQL query expression creation. The syntax provided by
 * the generic driver does not work with HSQL so this class was needed.
 *
 * @author <a href="mailto:santiago.arriaga@catnet.com.mx">Santiago Arriaga</a>
 */
public final class HsqlQueryExpression
    extends JDBCQueryExpression
{
    /**
     * Public constructor
     */
    public HsqlQueryExpression( PersistenceFactory factory )
    {
        super( factory );
    }

    /**
     * Redefinition of JDBCQueryExpression.addColumn() method. This is needed
     * because aliases are sometimes needed during the creation of HSQL 
     * expressions involving multiple outer joins. The original implementation
     * does not allows this.
     */
    public final void addColumn( String tableName, String columnName )
    {
        _tables.put( tableName, tableName );
        _cols.addElement( new HsqlColumn(tableName, columnName) );
    }

    /**
     * Redefinition of JDBCQueryExpression.addCondition() method. This is needed
     * because aliases are sometimes needed during the creation of HSQL 
     * expressions involving multiple outer joins. The original implementation
     * does not allows this.
     */
    public void addCondition( String tableName, String columnName,
                              String condOp, String value )
    {
      _tables.put( tableName, tableName );
      _conds.addElement
        ( new HsqlCondition(tableName, columnName, condOp, value) );
    }

    public String getStatement( boolean lock )
    {
        StringBuffer sql = new StringBuffer(128);

        HsqlAliasInfo aliasInfo = new HsqlAliasInfo(_joins);

        addSelectClause(sql, aliasInfo);
        addFromClause(sql, aliasInfo);
        boolean first = addJoinClause(sql, aliasInfo);
        addWhereClause( sql, aliasInfo, first );
        addOrderByClause(sql);
        addForUpdateClause(sql, lock);
 
        return sql.toString();
    }

    /**
     * Creates the select clause of the query expression
     * @param buffer The StringBuffer to which all the results are appended
     * @param aliasInfo The information about the possible aliases in the class
     */
    private void addSelectClause(StringBuffer buffer, HsqlAliasInfo aliasInfo)
    {
        buffer.append( JDBCSyntax.Select );
        if ( _distinct )
          buffer.append( JDBCSyntax.Distinct );

	addColumnList(buffer, aliasInfo);
    }
    
    /**
     * Add the from clause to the statement. This uses the hsql syntax for
     * outer joins. Much of the functionality of the superclass is duplicated
     * here but a refactoring should eliminate all the code that's been
     * repeated
     */
    private void addFromClause(StringBuffer buffer, HsqlAliasInfo aliasInfo)
    {
      buffer.append( JDBCSyntax.From );

      Hashtable tables = (Hashtable)_tables.clone();

      boolean first = addOuterJoins(buffer, tables, aliasInfo);
      addTables(buffer, tables, first);
    }

    /**
     * Add the necessary outer joins to the syntax
     * @param buffer The StringBuffer being appended
     * @param tables A table hash clone obtained from _tables. the tables
     *   participating in the outer join are being deleted from this hash in
     *   this method so the method that displays the tables will know what to
     *   displaty
     * @param aliasInfo The information of the aliases
     *   associated with the tables participating in multiple outer joins
     * @return true if there were outer joins added to the query; false
     *   otherwise
     */
    private boolean addOuterJoins
      (StringBuffer buffer, Hashtable tables, HsqlAliasInfo aliasInfo)
    {
      boolean first = true;

      for ( int i = 0 ; i < _joins.size() ; ++i )
      {
        Join join = (Join) _joins.elementAt( i );
        if ( join.outer )
        {
          if ( first )
            first = false;
          else
            buffer.append( JDBCSyntax.TableSeparator );

          addOuterJoin(buffer, join, aliasInfo);

          tables.remove( join.leftTable );
          tables.remove( join.rightTable );
        }
      }

      return first;
    }

    /**
     * Add a single outer join to the output buffer
     * @param buffer The StringBuffer being appended
     * @param join The join to be displayed
     * @param aliasInfo Says which columns need an alias because they are
     *   defined in multiple places
     */
    private void addOuterJoin
      (StringBuffer buffer, Join join, HsqlAliasInfo aliasInfo)
    {
      String leftAlias = aliasInfo.getAliasFor(join.leftTable, join);
      String rightAlias = aliasInfo.getAliasFor(join.rightTable, join);

      addJoinTable( buffer, join.leftTable, leftAlias );
      buffer.append( JDBCSyntax.LeftJoin );
      addJoinTable( buffer, join.rightTable, rightAlias );

      buffer.append( JDBCSyntax.On ).append(" (");

      String leftTable = (leftAlias == null)? join.leftTable : leftAlias;
      String rightTable = (rightAlias == null)? join.rightTable : rightAlias;

      addOuterJoinCondition
        (buffer, join.leftColumns, join.rightColumns, leftTable, rightTable);

      buffer.append( ")" );
    }

    /**
     * Add the outer join condition to the buffer
     * @param buffer The StringBuffer which is being appended
     * @param join The join that will provide the condition columns
     * @param leftTable The name of the left participant which may be a table
     *   name or an alias name
     * @param rightTable The name of the right participant which may be a table
     *   name or an alias name
     */
    private void addOuterJoinCondition
      (StringBuffer buffer, String[] leftColumns, String[] rightColumns,
       String leftTable, String rightTable)
    {
      String name;
      for ( int j = 0 ; j < leftColumns.length ; ++j )
      {
        if ( j > 0 )
          buffer.append( JDBCSyntax.And );

        name = leftTable + JDBCSyntax.TableColumnSeparator + leftColumns[ j ];
        buffer.append( _factory.quoteName( name ) );

        buffer.append( OpEquals );

        name = rightTable + JDBCSyntax.TableColumnSeparator + rightColumns[ j ];
        buffer.append( _factory.quoteName( name ) );
      }
    }

    /**
     * Add a table participant in the join with it's corresponding alias if
     *   necessary
     */
    private void addJoinTable(StringBuffer buffer, String table, String alias)
    {
      buffer.append(table);

      if(alias != null)
        buffer.append(' ').append(alias);
    }

    /**
     * This method add the tables to the FROM clause that do not participate
     * in an outer join. Those tables are contained in the tables structure
     * @param buffer StringBuffer instance which is being appended
     * @param tables The tables that participate in the query but do not
     *   participate in an outer join
     * @param first true if there were any outer joins added to the buffer
     *   false otherwise
     */
    private void addTables(StringBuffer buffer, Hashtable tables, boolean first)
    {
      Enumeration enum = tables.elements();
      while ( enum.hasMoreElements() )
      {
        if ( first )
          first = false;
        else
          buffer.append( JDBCSyntax.TableSeparator );

        buffer.append( _factory.quoteName( (String) enum.nextElement() ) );
      }
    }

    /**
     * Add the join clause to the current expression
     * @param buffer The buffer to which the generated output is beind appended
     * @param aliasInfo The class that contains the tables that
     *   are present in more than an outer join clause and therefore must be
     *   added to the join
     */
    private boolean addJoinClause(StringBuffer buffer, HsqlAliasInfo aliasInfo)
    {
        boolean first = true;
        Join join;

        for ( int i = 0 ; i < _joins.size() ; ++i )
        {
          join = (Join)_joins.elementAt( i );
          if(join.outer)
            continue;

          first = addWhereOrAnd(buffer, first);

          addJoin(buffer, aliasInfo, join);
        }

        first = addOuterJoins(buffer, aliasInfo, first);

        return first;
    }

    private void addJoin(StringBuffer buffer, HsqlAliasInfo aliasInfo, Join join)
    {
        String where;

        for ( int j = 0 ; j < join.leftColumns.length ; ++j )
        {
            if ( j > 0 )
                buffer.append( JDBCSyntax.And );

            where = quoteTableAndColumn( join.leftTable, join.leftColumns[j] );
            buffer.append
              ( checkForAlias(aliasInfo, join.leftTable, where) );
            buffer.append( OpEquals );

            where = quoteTableAndColumn( join.rightTable, join.rightColumns[j] );
            buffer.append
              (  checkForAlias(aliasInfo,join.rightTable, where) );
        }
    }

    private String checkForAlias(HsqlAliasInfo aliasInfo, String tableName, String where) {
        String alias = aliasInfo.getAnAliasFor(tableName);
        if( alias != null ) {
            return substituteAlias(where,tableName,alias);
        }
        return where;
    }

    /**
     * Add the outer join conditions to the WHERE part of the expression.
     * this implies a join between the tables defined more than once in outer
     * table statements
     * @param buffer The buffer which is being appended
     * @param aliasInfo Holds the information about the tables declared more
     *   than once in an outer join
     * @first true if no previous conditions were appended to the WHERE clause,
     *   false otherwise
     * @return the status of the first flag after processing the multiple
     *   outer join conditions
     */
    private boolean addOuterJoins
      (StringBuffer buffer, HsqlAliasInfo aliasInfo, boolean first)
    {
      for(Enumeration keys = aliasInfo.getTables(); keys.hasMoreElements();)
      {
        String key = (String)keys.nextElement();
        Hashtable hash = aliasInfo.getAliasHash(key);

        Enumeration joins = hash.keys();
        Join join = (Join)joins.nextElement();
        String[] columns = ( join.leftTable.equals(key) )?
          join.leftColumns : join.rightColumns;

        while( joins.hasMoreElements() )
        {
          first = addWhereOrAnd(buffer, first);

          addOuterJoinCondition(buffer, columns, columns,
            (String)hash.get(join), (String)hash.get( joins.nextElement() ));
        }
      }

      return first;
    }

    private String quoteTableAndColumn(String table, String column)
    {
        return _factory.quoteName
            (table + JDBCSyntax.TableColumnSeparator + column);
    }

    /**
     * This method adds the where clause taking into account possible aliases
     * for columns
     * @param buffer The buffer being appended
     * @param aliasInfo The information of the aliases to be substituted for
     *  some tables
     */
    protected boolean addWhereClause
      ( StringBuffer buffer, HsqlAliasInfo aliasInfo, boolean first )
    {
      first = addConditions(buffer, aliasInfo, first);

      first = addWhere(buffer, aliasInfo, first);

      return first;
    }

    /**
     * This method adds the conditions to the query being constructed
     */
    private boolean addConditions
      ( StringBuffer buffer, HsqlAliasInfo aliasInfo, boolean first )
    {
      HsqlCondition condition;

      if ( _conds.size() > 0 )
      {
        first = addWhereOrAnd(buffer, first);

        for ( int i = 0 ; i < _conds.size() ; ++i )
        {
          if ( i > 0 )
            buffer.append( JDBCSyntax.And );
          
          condition = (HsqlCondition)_conds.elementAt(i);
          buffer.append( getConditionString( condition, aliasInfo ) );
        }
      }

      return first;
    }

    /**
     * This method adds the where part to the query. This method performs an
     *   ugly workaround to substitute the aliases that may be a source of
     *   problems in some cases. The best solution to this is a refactoring
     *   that does not use the _where as a String but rather as a more flexible
     *   representation
     */
    private boolean addWhere
      ( StringBuffer buffer, HsqlAliasInfo aliasInfo, boolean first )
    {
      if ( _where != null )
      {
        first = addWhereOrAnd(buffer, first);
        String where = _where;

        for(Enumeration enum = aliasInfo.getTables(); enum.hasMoreElements();)
          {
          String table = (String)enum.nextElement();
          String alias = (String)aliasInfo.getAnAliasFor(table);

          where = substituteAlias(where, table + '.', alias + '.');
          }

        buffer.append(where);
      }

      return first;
    }

    /**
     * Make a substitution of the givne table with the given aliases. This is
     *   just a workaround, will work for most cases but is NOT bulletproof
     *   and may fail for some cases
     * @param expression The string representing the where class
     * @param table The table being replaced
     * @param alias The alias that will replace the table name
     */
    private String substituteAlias
      (String expression, String table, String alias)
    {
      StringBuffer buffer = new StringBuffer( expression.length() );

      int pos = 0;
      int end = expression.indexOf(table);
      for( ; end != -1; end = expression.indexOf(table, pos) )
      {
        buffer.append( expression.substring(pos, end) );
        buffer.append( alias );
        pos = end + table.length();
      }

      buffer.append( expression.substring(pos) );
      return buffer.toString();
    }

    /**
     * Add the order by clause
     */
    private void addOrderByClause(StringBuffer buffer)
    {
        if ( _order != null )
          buffer.append(JDBCSyntax.OrderBy).append(_order);
    }

    /**
     * Add the for update class
     */
    private void addForUpdateClause(StringBuffer buffer, boolean lock)
    {
        // Use FOR UPDATE to lock selected tables.
        //if ( lock )
        //    buffer.append( " FOR UPDATE" );
    }

   /**
    * This method adds the column list to the buffer. This method is used
    *   instead of getColumnList() in the superclass
    * @param buffer the buffer to which the list is being added
    * @param aliasInfo The class that defines which tables require to
    *   display an alias instead of a table name
    */
   private void addColumnList(StringBuffer buffer, HsqlAliasInfo aliasInfo)
   {
     if ( _cols.size() == 0 ) // ? (Preserved from superclass)
     {
       buffer.append("1");
       return;
     }

     int i = 0;
     for ( i = 0 ; i < _cols.size() ; ++i )
     {
       if ( i > 0 )
         buffer.append( JDBCSyntax.ColumnSeparator );

       buffer.append
         ( getColumnString( (HsqlColumn)_cols.elementAt(i), aliasInfo ) );
     }

     if ( _select != null ) // ? (Preserved from superclass)
       if ( i > 0 )
         buffer.append( JDBCSyntax.ColumnSeparator ).append( _select );
       else
         buffer.append( _select );
   }

   /**
    * This method creates the column syntax taking into account any alias
    *   definition given in outer hash
    * @param column The column instance to be returned
    * @param aliasInfo The class with the definition of the columns that
    *   need to display an alias
    */
   private String getColumnString(HsqlColumn column, HsqlAliasInfo aliasInfo)
   {
     String tableName = column.getTableName();
     String columnName = column.getColumnName();

     if( aliasInfo.tableExists(tableName) )
       tableName = aliasInfo.getAnAliasFor(tableName);

     return tableName + JDBCSyntax.TableColumnSeparator + columnName;
   }

   /**
    * This method creates the condition syntax taking into account any alias
    *   definition given in outer hash
    * @param condition The condition instance to be returned as a String
    * @param aliasInfo The object with the definition of the columns that
    *   need to display an alias
    */
   private String getConditionString
     (HsqlCondition condition, HsqlAliasInfo aliasInfo)
   {
     return getColumnString(condition.getColumn(), aliasInfo) +
       condition.getOperator() + condition.getValue();
   }

   /**
    * Add WHERE or AND keyword depending if is the first condition statement
    *   or not
    * @param buffer The buffer being appended
    * @param first Whether this is the first condition in the where part or not
    * @return always false as the resulting value of the first flag after this
    *   call
    */
   private boolean addWhereOrAnd(StringBuffer buffer, boolean first)
   {
     if ( first )
       buffer.append( JDBCSyntax.Where );
     else
       buffer.append( JDBCSyntax.And );

     return false;
   }

//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////

/**
 * This class encapsulates the information of the aliases related to sql
 * outer joins. This class is defined as inner to easily see the Join class.
 * A further refactoring should make this class a top-level class
 */
final class HsqlAliasInfo
{
  /**
   * Counter that will be used to generate different alias names
   */
  private int _count = 1;

  /**
   * A hashtable with the following structure:
   * <pre>
   *    key:name-of-the-table(String)
   *           value:(Hashtable):
   *                     {
   *                     key:join object which contains the table(Join)
   *                              values: a random alias string(String)
   *                     }
   * </pre>
   * That is a Hashtable with Hashtables as values. It purpose is to map a
   *   table name with the multiple join objects that declare it and each
   *   join with the alias that the column has in there. Only the tables
   *   that are defined in multiple places are placed inside this structure,
   *   because they are the only ones which require alias creation
   */
  private final Hashtable _hash;

  /**
   * Public constructor. This builds an alias info class from the joins 
   *   participating in a query
   */
  public HsqlAliasInfo(Vector joins)
    { _hash = getRepeatedTablesInOuterJoinsHash(joins); }

   /**
    * This method returns the hash internal values
    * @return a not-null Hashtable instance
    */
   private Hashtable getRepeatedTablesInOuterJoinsHash(Vector joins)
   {
     Hashtable hash = new Hashtable();

     Hashtable countHash = getTableCountInOuterJoinsHash(joins);
     String table;
     Vector vec;
     for(Enumeration keys = countHash.keys(); keys.hasMoreElements();)
     {
       table = (String)keys.nextElement();
       vec = (Vector)countHash.get( table );

       if(vec.size() > 1)
       {
         Hashtable temp = new Hashtable();
         for(Enumeration joinenum = vec.elements(); joinenum.hasMoreElements();)
           temp.put( joinenum.nextElement(), "a" + (_count++) );

         hash.put(table, temp);
       }
     }

     return hash;
   }

   /**
    * This method returns a hashtable strcture which the keys are each of the
    * tables defined in outer join clauses and the values is a Vector of
    * the join objects which define each one of the tables. This will be used
    * as an auxiliary to create the structure described in the
    * getRepeatedTablesInOuterJoinsHash() method
    */
   private Hashtable getTableCountInOuterJoinsHash(Vector joins)
   {
     Hashtable hash = new Hashtable();
     Join join = null;

     for ( int i = 0 ; i < joins.size() ; ++i )
     {
       join = (Join)joins.elementAt( i );
       if (join.outer)
       {
         addTableCount(hash, join.leftTable, join);
         addTableCount(hash, join.rightTable, join);
       }
     }

     return hash;
   }

   /**
    * This method adds a count to the structure described (and returned) in the
    * getTableCountInOutersJoinHash method
    * @param hash The hash to which the count will be added
    * @param table The table to which the join will be appended
    * @param join The join object
    */
   private void addTableCount(Hashtable hash, String table, Join join)
   {
     Vector vec = null;

     if( hash.containsKey(table) )
       vec = (Vector)hash.get(table);
     else
     {
       vec = new Vector();
       hash.put(table, vec);
     }

     vec.addElement(join);
   }

  ////////////////////////////////////////////////////////////////////////////
  // Public methods

  /**
   * Return a list of the tables involved in more than one outer join and
   * they need an alias
   */
  public Enumeration getTables()
    { return _hash.keys(); }

  /**
   * Check if the given table is involved in more thatn one oter join
   */
  public boolean tableExists(String table)
    { return _hash.containsKey(table); }

  /**
   * Return any alias for the given table
   * @param name a not null table name
   * @return an alias for the input table; or null if the table has no aliases
   *   because it does not participate in more than one outer join
   */
  public String getAnAliasFor(String table)
    {
    Hashtable hash = (Hashtable)_hash.get(table);

    return (hash == null)?
      null : (String)hash.elements().nextElement();
    }

  /**
   * Get the alias for the given table and join
   * @param table a not null table name that may participate in more than one
   *   outer join
   * @param join a not null join name in which the given table participates
   *  given table
   * @return the alias for the input table and join; or null if the table has
   *   no aliases because it does not participate in more than one outer join
   */
  public String getAliasFor(String table, Join join)
    {
    Hashtable hash = (Hashtable)_hash.get(table);

    return (hash == null)?
      null : (String)hash.get(join);
    }

  /**
   * Get the hash of aliases for the given table
   * @return a Hashtable instance which keys are the join objects and the
   *   values are the alias names for the given table. If the table has no
   *   aliases it returns null
   */
  public Hashtable getAliasHash(String table)
    { return (Hashtable)_hash.get(table); }

}

}

//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////

/**
 * Simple abstraction of a column. Note that this class is immutable so
 * that this class may be copied to other instances during the clonation
 * process performed in the superclass without the risk of introducing
 * undesired side effects.
 */
final class HsqlColumn
  {
  private final String _tableName;

  private final String _columnName;

  /**
   * Public constructor
   */
  public HsqlColumn(String tableName, String columnName)
    {
    _tableName = tableName;
    _columnName = columnName;
    }

  public final String getTableName()
    { return _tableName; }

  public final String getColumnName()
    { return _columnName; }

  }

//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////
// CLASS DELIMITER
//////////////////////////////////////////////////////////////////////////////

/**
 * Simple abstraction of a condition. Note that this class is immutable so
 * that this class may be copied to other instances during the clonation
 * process performed in the superclass without the risk of introducing
 * undesired side effects.
 */
final class HsqlCondition
  {
  private final HsqlColumn _column;

  private final String _operator;

  private final String _value;

  /**
   * Public constructor
   */
  public HsqlCondition
    (String tableName, String columnName, String operator, String value)
    {
    _column = new HsqlColumn(tableName, columnName);
    _operator = operator;
    _value = value;
    }

  public final HsqlColumn getColumn()
    { return _column; }

  public final String getOperator()
    { return _operator; }

  public final String getValue()
    { return _value; }

  }

