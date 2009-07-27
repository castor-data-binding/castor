package org.castor.cpa.persistence.sql.driver;

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
    private StringBuffer _sql;

    public InterbaseQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    public String getStatement(final boolean lock) {
        Enumeration  enumeration;
        boolean      first;
        Hashtable    tables;
        Vector       done = new Vector();

        _sql = new StringBuffer();
        _sql.append(JDBCSyntax.SELECT);
        if (_distinct) {
            _sql.append(JDBCSyntax.DISTINCT);
        }

        _sql.append(getColumnList());

        _sql.append(JDBCSyntax.FROM);

        tables = (Hashtable) _tables.clone();
        first = true;
        // gather all joins with the same left part use SQL92 syntax
        for (int i = 0; i < _joins.size(); ++i) {

            Join join = (Join) _joins.elementAt(i);

            if (done.contains(join._leftTable)) {
                continue;
            }

            if (first) {
                first = false;
                _sql.append(_factory.quoteName(join._leftTable));
            }

            appendJoin(join);

            tables.remove(join._leftTable);
            tables.remove(join._rightTable);

            for (int k = i + 1; k < _joins.size(); ++k) {

                Join join2 = (Join) _joins.elementAt(k);

                if (join._leftTable.equals(join2._leftTable)) {
                  appendJoin(join2);
                  tables.remove(join2._rightTable);
                }
            }
            done.addElement(join._leftTable);
        }
        enumeration = tables.keys();
        while (enumeration.hasMoreElements()) {
            if (first) {
                first = false;
            } else {
                _sql.append(JDBCSyntax.TABLE_SEPARATOR);
            }
            String tableAlias = (String) enumeration.nextElement();
            String tableName = (String) tables.get(tableAlias);
            if (tableAlias.equals(tableName)) {
                _sql.append(_factory.quoteName(tableName));
            } else {
                _sql.append(_factory.quoteName(tableName) + " "
                        + _factory.quoteName(tableAlias));
            }
        }

        first = addWhereClause(_sql, true);

        if (_order != null) {
            _sql.append(JDBCSyntax.ORDER_BY).append(_order);
        }

        // Do not use FOR UPDATE to lock query.
        return _sql.toString();
    }

    void appendJoin(final Join join) {
        if (join._outer) {
            _sql.append(JDBCSyntax.LEFT_JOIN);
        } else {
            _sql.append(JDBCSyntax.INNER_JOIN);
        }

        String tableAlias = join._rightTable;
        String tableName = (String) _tables.get(tableAlias);
        if (tableAlias.equals(tableName)) {
            _sql.append(_factory.quoteName(tableName));
        } else {
            _sql.append(_factory.quoteName(tableName) + " "
                    + _factory.quoteName(tableAlias));
        }
        _sql.append(JDBCSyntax.ON);
        for (int j = 0; j < join._leftColumns.length; ++j) {
            if (j > 0) {
                _sql.append(JDBCSyntax.AND);
            }
            _sql.append(_factory.quoteName(join._leftTable
                    + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                    + join._leftColumns[j])).append(OP_EQUALS);
            _sql.append(_factory.quoteName(join._rightTable
                    + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                    + join._rightColumns[j]));
        }
    }
}


