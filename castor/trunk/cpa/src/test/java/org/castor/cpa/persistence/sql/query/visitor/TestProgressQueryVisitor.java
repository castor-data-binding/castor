/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id$
 */

package org.castor.cpa.persistence.sql.query.visitor;

import static org.junit.Assert.assertEquals;

import org.castor.cpa.persistence.sql.query.Join;
import org.castor.cpa.persistence.sql.query.JoinOperator;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.TableAlias;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.junit.Test;

/**
 * Test if PostgreSQLQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision$ $Date$
 */
public final class TestProgressQueryVisitor extends TestPostgreSQLQueryVisitor {
    //-----------------------------------------------------------------------------------

    @Test
    public void testSelectNoConditionNoExpressionWithLock() throws Exception {
        Select select = new Select("TestTable");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);
        
        String expected = "SELECT * FROM \"TestTable\"";

        assertEquals(expected, queryVis.toString());
    }

    //-----------------------------------------------------------------------------------

    @Test
    public void testVisitJoin() throws Exception {
        Visitor queryVis = getVisitor();
        Table tab1 = new Table("tab1");
        Column col1 = tab1.column("col1");
        Table tab2 = new Table("tab2");
        Column col2 = tab2.column("col2");
        Join join = new Join(JoinOperator.LEFT, new Table("tab1"), col1.equal(col2));

        queryVis.visit(join);

        assertEquals("LEFT JOIN \"tab1\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\"",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstruction() {
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");

        ((ProgressQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("\"tab1\"", queryVis.toString());

        table.addFullJoin(table2, col1.equal(col2));
        queryVis = getVisitor();

        ((ProgressQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" FULL JOIN \"tab2\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionDepth() {
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");
        Table table3 = new Table("tab3");
        Column col3 = table3.column("col3");
        Table table4 = new Table("tab4");
        Column col4 = table4.column("col4");
        table.addFullJoin(table2, col1.equal(col2));
        table2.addFullJoin(table3, col2.equal(col3));
        table3.addFullJoin(table4, col3.equal(col4));

        ((ProgressQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" FULL JOIN "
                + "(\"tab2\" FULL JOIN "
                    + "(\"tab3\" FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\") "
                + "ON \"tab2\".\"col2\"=\"tab3\".\"col3\") ON \"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionBreadth() {
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");
        Table table3 = new Table("tab3");
        Column col3 = table3.column("col3");
        Table table4 = new Table("tab4");
        Column col4 = table4.column("col4");
        table.addFullJoin(table2, col1.equal(col2));
        table.addFullJoin(table3, col2.equal(col3));
        table.addFullJoin(table4, col3.equal(col4));

        ((ProgressQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("((("
                + "\"tab1\" FULL JOIN \"tab2\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab3\" ON \"tab2\".\"col2\"=\"tab3\".\"col3\") "
                + "FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionDepthAndBreadth() {
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");
        Table table3 = new Table("tab3");
        Column col3 = table3.column("col3");
        Table table4 = new Table("tab4");
        Column col4 = table4.column("col4");
        table.addFullJoin(table2, col1.equal(col2));
        table2.addFullJoin(table3, col2.equal(col3));
        table.addFullJoin(table4, col3.equal(col4));

        ((ProgressQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("("
                + "(\"tab1\" FULL JOIN "
                    + "(\"tab2\" FULL JOIN \"tab3\" ON \"tab2\".\"col2\"=\"tab3\".\"col3\")"
                + " ON \"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @Test
    public void testAddTableNames() {
        Visitor queryVis = getVisitor();

        Table table = new Table("TestTable");
        ((ProgressQueryVisitor) queryVis).addTableNames(table);

        assertEquals("\"TestTable\"", queryVis.toString());

        queryVis = getVisitor();

        TableAlias tblAls = new TableAlias(table, "TestTableAlias");
        ((ProgressQueryVisitor) queryVis).addTableNames(tblAls);

        assertEquals("\"TestTable\" \"TestTableAlias\"",
                queryVis.toString());
    }

    @Test
    public void testQuoteName() throws Exception {
        Visitor queryVis = getVisitor();

        String expected = "TestName"; 

        assertEquals(("\"" + expected + "\""),
                ((ProgressQueryVisitor) queryVis).quoteName(expected));
    }

    @Test
    public void testGetSequenceNextValString() throws Exception {
        Visitor queryVis = getVisitor();

        String name = "TestName"; 

        assertEquals(null, ((ProgressQueryVisitor) queryVis).getSequenceNextValString(name));
    }

    @Test
    public void testHandleLock() throws Exception {
        Select select = new Select("Test");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        ((ProgressQueryVisitor) queryVis).handleLock(select);

        assertEquals("", queryVis.toString());
    }

    protected Visitor getVisitor() {
        return new ProgressQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
