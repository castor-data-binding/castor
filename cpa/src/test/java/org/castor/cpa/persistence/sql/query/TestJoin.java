/*
 * Copyright 20010 Dennis Butterstein, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.persistence.sql.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompareOperator;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.junit.Test;

/**
 * Test if Join works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestJoin {

    @Test
    public void testToString() {
        Join join = null;
        Table table = new Table("test");
        Table table2 = new Table("test2");
        Table table3 = new Table("test3");
        TableAlias tableAlias = new TableAlias(table, "testAlias");
        TableAlias tableAlias2 = new TableAlias(table2, "testAlias2");
        Compare compare =
            new Compare(new Column("column1"), CompareOperator.EQ, new Column("column2"));
        JoinOperator joinOper = JoinOperator.FULL;

        try {
            join = new Join(joinOper, table);

            assertEquals(join.getJoin(), table);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), null);
            assertEquals("FULL JOIN test", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            join = new Join(joinOper, tableAlias);

            assertEquals(join.getJoin(), tableAlias);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), null);
            assertEquals("FULL JOIN test testAlias", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            join = new Join(joinOper, table);
            table.addLeftJoin(tableAlias);

            assertEquals(join.getJoin(), table);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), null);
            assertEquals("FULL JOIN (test LEFT JOIN test testAlias)", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            table = new Table("test");
            join = new Join(joinOper, table, compare);
            table.addLeftJoin(tableAlias);
            table.addRightJoin(tableAlias2);

            assertEquals(join.getJoin(), table);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), compare);
            assertEquals("FULL JOIN ((test LEFT JOIN test testAlias) RIGHT JOIN test2 testAlias2) "
                    + "ON column1=column2", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            tableAlias = new TableAlias(table, "testAlias");
            tableAlias2 = new TableAlias(table2, "testAlias2");
            table = new Table("test");
            join = new Join(joinOper, table, compare);
            table.addRightJoin(tableAlias2);
            tableAlias2.addLeftJoin(tableAlias);
            tableAlias.addFullJoin(table3);
            

            assertEquals(join.getJoin(), table);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), compare);
            assertEquals("FULL JOIN (test RIGHT JOIN (test2 testAlias2 LEFT JOIN "
                    + "(test testAlias FULL JOIN test3))) ON column1=column2", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }

        try {
            Table table4 = new Table("tab4");
            Table table5 = new Table("tab5");
            Table table6 = new Table("tab6");
            table = new Table("test");
            join = new Join(joinOper, table, compare);
            table.addRightJoin(table4);
            table.addLeftJoin(table5);
            table5.addFullJoin(table6);
            

            assertEquals(join.getJoin(), table);
            assertEquals(join.getOperator(), joinOper);
            assertEquals(join.getCondition(), compare);
            assertEquals("FULL JOIN ((test RIGHT JOIN tab4) LEFT JOIN (tab5 FULL JOIN tab6)) "
                    + "ON column1=column2", join.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }
}
