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
package org.castor.cpa.persistence.sql.driver;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * QueryExpression for MS SQL Server.
 *
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @version $Revision$ $Date: 2004-10-08 02:58:33 -0600 (Fri, 08 Oct 2004) $
 */
public final class SQLServerQueryExpression extends JDBCQueryExpression {
    public SQLServerQueryExpression(final PersistenceFactory factory) {
        super(factory);
    }

    public String getStatement(final boolean lock) {
        StringBuffer sql = new StringBuffer();
        sql.append(JDBCSyntax.SELECT);
        if (_distinct) {
            sql.append(JDBCSyntax.DISTINCT);
        }
        if (_limit != null) {
            if (!_limit.equals("")) {
                sql.append("TOP (").append(_limit).append(") ");
            }
        }
        if (_select == null) {
            sql.append(getColumnList());
        } else {
            sql.append(_select).append(" ");
        }

        sql.append(JDBCSyntax.FROM);

        // Use outer join syntax for all outer joins. Inner joins come later.
        Hashtable<String, String> tables = new Hashtable<String, String>(_tables);
        Vector<String> done = new Vector<String>();
        boolean first = true;
        // gather all outer joins with the same left part
        for (int i = 0; i < _joins.size(); ++i) {
            Join join = _joins.elementAt(i);

            if (!join._outer || done.contains(join._leftTable)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sql.append(JDBCSyntax.TABLE_SEPARATOR);
            }

            sql.append(_factory.quoteName(join._leftTable));
            
            if (lock) {
                sql.append(" WITH (HOLDLOCK) ");
            }
            
            sql.append(JDBCSyntax.LEFT_JOIN);
            
            String tableName = tables.get(join._rightTable);
                        
            if (join._rightTable.equals(tableName)) {
                sql.append(_factory.quoteName(tableName));
            } else {
                sql.append(_factory.quoteName(tableName) + " "
                        + _factory.quoteName(join._rightTable));
            }
            
            if (lock) {
                sql.append(" WITH (HOLDLOCK) ");
            }
            
            sql.append(JDBCSyntax.ON);
            for (int j = 0; j < join._leftColumns.length; ++j) {
                if (j > 0) {
                    sql.append(JDBCSyntax.AND);
                }
                sql.append(_factory.quoteName(join._leftTable
                        + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                        + join._leftColumns[j])).append(OP_EQUALS);
                sql.append(_factory.quoteName(join._rightTable
                        + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                        + join._rightColumns[j]));
            }

            tables.remove(join._leftTable);
            tables.remove(join._rightTable);
            for (int k = i + 1; k < _joins.size(); ++k) {
                Join join2;

                join2 = _joins.elementAt(k);
                if (!join2._outer || !join._leftTable.equals(join2._leftTable)) {
                    continue;
                }
                sql.append(JDBCSyntax.LEFT_JOIN);
                tableName = tables.get(join2._rightTable);

                if (join2._rightTable.equals(tableName)) {
                    sql.append(_factory.quoteName(tableName));
                } else {
                    sql.append(_factory.quoteName(tableName) + " "
                            + _factory.quoteName(join2._rightTable));
                }
                
                if (lock) {
                    sql.append(" WITH (HOLDLOCK) ");
                }
                
                sql.append(JDBCSyntax.ON);
                for (int j = 0; j < join2._leftColumns.length; ++j) {
                    if (j > 0) {
                        sql.append(JDBCSyntax.AND);
                    }
                    sql.append(_factory.quoteName(join2._leftTable
                            + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                            + join2._leftColumns[j])).append(OP_EQUALS);
                    sql.append(_factory.quoteName(join2._rightTable
                            + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                            + join2._rightColumns[j]));
                }
                tables.remove(join2._rightTable);
            }

            done.addElement(join._leftTable);
        }
        Enumeration<String> enumeration = tables.keys();
        while (enumeration.hasMoreElements()) {
            if (first) {
                first = false;
            } else {
                sql.append(JDBCSyntax.TABLE_SEPARATOR);
            }
            String tableAlias = enumeration.nextElement();
            String tableName = tables.get(tableAlias);
            if (tableAlias.equals(tableName)) {
                sql.append(_factory.quoteName(tableName));
            } else {
                sql.append(_factory.quoteName(tableName) + " "
                        + _factory.quoteName(tableAlias));
            }
            if (lock) {
                sql.append(" WITH (HOLDLOCK) ");
            }
        }

        // Use standard join syntax for all inner joins
        first = true;
        for (int i = 0; i < _joins.size(); ++i) {
            Join join;

            join = _joins.elementAt(i);
            if (!join._outer) {
                if (first) {
                    sql.append(JDBCSyntax.WHERE);
                    first = false;
                } else {
                    sql.append(JDBCSyntax.AND);
                }
                for (int j = 0; j < join._leftColumns.length; ++j) {
                    if (j > 0) {
                        sql.append(JDBCSyntax.AND);
                    }
                    sql.append(_factory.quoteName(join._leftTable
                            + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                            + join._leftColumns[j])).append(OP_EQUALS);
                    sql.append(_factory.quoteName(join._rightTable
                            + JDBCSyntax.TABLE_COLUMN_SEPARATOR
                            + join._rightColumns[j]));
                }
            }
        }
        first = addWhereClause(sql, first);

        if (_order != null) {
            sql.append(JDBCSyntax.ORDER_BY).append(_order);
        }

        // There is no standard way to lock selected tables.
        return sql.toString();
    }

    public boolean isLimitClauseSupported() {
        return true;
    }
}