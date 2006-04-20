package org.exolab.castor.jdo.drivers;

import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;
import java.util.Enumeration;
import java.util.Hashtable;

/**
  *  specialized generic driver for InstantDB database.
  *
  *  @author I. Burak Ozyurt
  *  @version 1.0
  */

public class InstantDBQueryExpression extends JDBCQueryExpression {

  public InstantDBQueryExpression( PersistenceFactory factory ) {
        super( factory );
  }


    public String getStatement( boolean lock ) {
        StringBuffer sql;
        boolean      first;
        Enumeration  enum;


        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );

        if ( _select == null )
          sql.append( getColumnList() );
        else
          sql.append( _select ).append(" ");

        sql.append( JDBCSyntax.From );

        
        first = true;
        // outer joins are not supported properly by InstantDB so all joins are treated as inner joins

        enum = _tables.elements();
        while ( enum.hasMoreElements() ) {
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TableSeparator );
            sql.append( _factory.quoteName( (String) enum.nextElement() ) );
        }

        // Use standard join syntax for all inner joins.
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            join = (Join) _joins.elementAt( i );
            if ( first ) {
               sql.append( JDBCSyntax.Where );
               first = false;
            } else {
              sql.append( JDBCSyntax.And );
            }
           for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
              if ( j > 0 )  sql.append( JDBCSyntax.And );
              sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                       join.leftColumns[ j ] ) ).append( OpEquals );
              sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                    join.rightColumns[ j ] ) );
           } //j

        }//i
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);

        // There is no standard way to lock selected tables, so lock flag is ignored
        return sql.toString();

    }



}