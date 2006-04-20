
package org.exolab.castor.persist.sql.drivers;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.persist.sql.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for Interbase.
 *
 */
public final class InterbaseQueryExpression
    extends JDBCQueryExpression
{
    private StringBuffer sql;

    public InterbaseQueryExpression( PersistenceFactory factory )
    {
        super( factory );
    }

    public String getStatement( boolean lock )
    {
        Enumeration  enum;
        boolean      first;
        Hashtable    tables;
        Vector       done = new Vector();

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );

        if ( _select == null )
          sql.append( getColumnList() );
        else
          sql.append( _select).append(" ");

        sql.append( JDBCSyntax.From );

        tables = (Hashtable) _tables.clone();
        first = true;
        // gather all joins with the same left part use SQL92 syntax
        for ( int i = 0 ; i < _joins.size() ; ++i ) {

            Join join = (Join) _joins.elementAt( i );

            if (done.contains( join.leftTable ) ) continue;

            if ( first ) {
                first = false;
                sql.append(  _factory.quoteName( join.leftTable ) );
            }

            appendJoin(join);

            tables.remove( join.leftTable );
            tables.remove( join.rightTable );

            for ( int k = i + 1 ; k < _joins.size() ; ++k ) {

                Join join2 = (Join) _joins.elementAt( k );

                if (join.leftTable.equals( join2.leftTable ) ){
                  appendJoin(join2);
                  tables.remove( join2.rightTable );
                }
            }
            done.addElement( join.leftTable );
        }
        enum = tables.keys();
        while ( enum.hasMoreElements() ) {
            if ( first ) {
                first = false;
            } else {
                sql.append( JDBCSyntax.TableSeparator );
            }
            String tableAlias = (String) enum.nextElement();
            String tableName = (String) tables.get( tableAlias );
            if( tableAlias.equals( tableName ) ) {
                sql.append( _factory.quoteName( tableName ) );
            } else {
                sql.append( _factory.quoteName( tableName ) + " " +
                            _factory.quoteName( tableAlias ) );
            }
        }

        first = addWhereClause( sql, true );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);

        // Do not use FOR UPDATE to lock query.
        return sql.toString();
    }

    void appendJoin(Join join){

      if(join.outer) sql.append( JDBCSyntax.LeftJoin );
      else sql.append( JDBCSyntax.InnerJoin );

      String tableAlias = join.rightTable;
      String tableName = (String) _tables.get( tableAlias );
      if( tableAlias.equals( tableName ) ) {
          sql.append( _factory.quoteName( tableName ) );
      } else {
          sql.append( _factory.quoteName( tableName ) + " " +
                      _factory.quoteName( tableAlias ) );
      }
      sql.append( JDBCSyntax.On );
      for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
          if ( j > 0 ) sql.append( JDBCSyntax.And );
          sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                          join.leftColumns[ j ] ) ).append( OpEquals );
          sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                          join.rightColumns[ j ] ) );
      }
    }

}


