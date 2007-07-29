
package org.exolab.castor.jdo.drivers;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * QueryExpression for Interbase.
 *
 */
public final class InterbaseQueryExpression extends JDBCQueryExpression {
    private StringBuffer sql;

    public InterbaseQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    public String getStatement(final boolean lock) {
        Enumeration  enumeration;
        boolean      first;
        Hashtable    tables;
        Vector       done = new Vector();

        sql = new StringBuffer();
        sql.append(JDBCSyntax.SELECT);
        if (_distinct)
          sql.append(JDBCSyntax.DISTINCT);

        sql.append(getColumnList());

        sql.append(JDBCSyntax.FROM);

        tables = (Hashtable) _tables.clone();
        first = true;
        // gather all joins with the same left part use SQL92 syntax
        for (int i = 0; i < _joins.size(); ++i) {

            Join join = (Join) _joins.elementAt(i);

            if (done.contains(join.leftTable)) continue;

            if (first) {
                first = false;
                sql.append(_factory.quoteName(join.leftTable));
            }

            appendJoin(join);

            tables.remove(join.leftTable);
            tables.remove(join.rightTable);

            for (int k = i + 1; k < _joins.size(); ++k) {

                Join join2 = (Join) _joins.elementAt(k);

                if (join.leftTable.equals(join2.leftTable)) {
                  appendJoin(join2);
                  tables.remove(join2.rightTable);
                }
            }
            done.addElement(join.leftTable);
        }
        enumeration = tables.keys();
        while (enumeration.hasMoreElements()) {
            if (first) {
                first = false;
            } else {
                sql.append(JDBCSyntax.TABLE_SEPARATOR);
            }
            String tableAlias = (String) enumeration.nextElement();
            String tableName = (String) tables.get(tableAlias);
            if (tableAlias.equals(tableName)) {
                sql.append(_factory.quoteName(tableName));
            } else {
                sql.append(_factory.quoteName(tableName) + " " +
                            _factory.quoteName(tableAlias));
            }
        }

        first = addWhereClause(sql, true);

        if (_order != null)
          sql.append(JDBCSyntax.ORDER_BY).append(_order);

        // Do not use FOR UPDATE to lock query.
        return sql.toString();
    }

    void appendJoin(final Join join) {

      if (join.outer) sql.append(JDBCSyntax.LEFT_JOIN);
      else sql.append(JDBCSyntax.INNER_JOIN);

      String tableAlias = join.rightTable;
      String tableName = (String) _tables.get(tableAlias);
      if (tableAlias.equals(tableName)) {
          sql.append(_factory.quoteName(tableName));
      } else {
          sql.append(_factory.quoteName(tableName) + " " +
                      _factory.quoteName(tableAlias));
      }
      sql.append(JDBCSyntax.ON);
      for (int j = 0; j < join.leftColumns.length; ++j) {
          if (j > 0) sql.append(JDBCSyntax.AND);
          sql.append(_factory.quoteName(join.leftTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                          join.leftColumns[j])).append(OP_EQUALS);
          sql.append(_factory.quoteName(join.rightTable + JDBCSyntax.TABLE_COLUMN_SEPARATOR +
                                          join.rightColumns[j]));
      }
    }
}


