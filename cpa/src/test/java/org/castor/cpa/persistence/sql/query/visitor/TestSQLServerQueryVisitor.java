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
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.query.visitor;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.castor.cpa.persistence.sql.query.Join;
import org.castor.cpa.persistence.sql.query.JoinOperator;
import org.castor.cpa.persistence.sql.query.Qualifier;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.TableAlias;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test if PostgreSQLQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestSQLServerQueryVisitor extends TestPostgreSQLQueryVisitor {
    //---------------------------SELECT--------------------------------------------------------

    @Test
    public void testSelectNoConditionNoExpressionWithLock() throws Exception {
        Select select = new Select("TestTable");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);
        
        String expected = "SELECT * FROM \"TestTable\" WITH (HOLDLOCK)";

        assertEquals(expected, queryVis.toString());

        Object result = null;
        try {
            Method method = queryVis.getClass().getDeclaredMethod("getSelect", (Class[]) null);
            method.setAccessible(true);
            result = method.invoke(queryVis, (Object[]) null);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        assertEquals(select, result);
    }

    @Test
    public void testSelectWithJoinsDepthWithLock() {
        Visitor queryVis = getVisitor();
        
        Table tab1 = new Table("FOO");
        Column col1 = new Column(tab1, "ID");

        Table tab2 = new Table("BAR");
        Column col21 = new Column(tab2, "ID");
        Column col22 = new Column(tab2, "ABC_ID");

        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");
        Column col3 = new Column(tab3, "ID");

        Select sel = new Select(tab1);
        sel.setLocked(true);
        queryVis.visit(sel);
        assertEquals("SELECT * FROM \"FOO\" WITH (HOLDLOCK)", queryVis.toString());

        queryVis = getVisitor();
        tab1.addInnerJoin(tab2, col1.equal(col21));
        queryVis.visit(sel);
        assertEquals("SELECT * "
                + "FROM (\"FOO\" WITH (HOLDLOCK) "
                + "INNER JOIN \"BAR\" WITH (HOLDLOCK) "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")", queryVis.toString());

        queryVis = getVisitor();
        tab2.addLeftJoin(tab3, col22.equal(col3));
        queryVis.visit(sel);
        assertEquals("SELECT * "
                + "FROM (\"FOO\" WITH (HOLDLOCK) "
                + "INNER JOIN (\"BAR\" WITH (HOLDLOCK) "
                + "LEFT JOIN \"ABC\" \"xyz\" WITH (HOLDLOCK) "
                + "ON \"BAR\".\"ABC_ID\"=\"xyz\".\"ID\") "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")", queryVis.toString());
    }

    @Test
    public void testSelectWithJoinsBreadthWithLock() {
        Visitor queryVis = getVisitor();
        
        Table tab1 = new Table("FOO");
        Column col1 = new Column(tab1, "ID");

        Table tab2 = new Table("BAR");
        Column col21 = new Column(tab2, "ID");

        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");
        Column col3 = new Column(tab3, "ID");

        Table tab4 = new Table("tab4");
        
        Table tab5 = new Table("tab5");

        Select sel = new Select(tab1);
        sel.setLocked(true);
        queryVis.visit(sel);
        assertEquals("SELECT * FROM \"FOO\" WITH (HOLDLOCK)", queryVis.toString());

        queryVis = getVisitor();
        tab1.addInnerJoin(tab2, col1.equal(col21));
        tab1.addInnerJoin(tab3, col1.equal(col3));
        tab1.addInnerJoin(tab4);
        tab1.addInnerJoin(tab5);
        queryVis.visit(sel);
        assertEquals("SELECT * FROM "
                + "((((\"FOO\" WITH (HOLDLOCK) INNER JOIN \"BAR\" WITH (HOLDLOCK) ON "
                + "\"FOO\".\"ID\"=\"BAR\".\"ID\") "
                + "INNER JOIN \"ABC\" \"xyz\" WITH (HOLDLOCK) ON \"FOO\".\"ID\"=\"xyz\".\"ID\") "
                + "INNER JOIN \"tab4\" WITH (HOLDLOCK)) "
                + "INNER JOIN \"tab5\" WITH (HOLDLOCK))", queryVis.toString());
    }


    @Test
    public void testSelectWithJoinsDepthAndBreadthWithLock() {
      Visitor queryVis = getVisitor();

      Table tab1 = new Table("table1");
      Column col1 = new Column(tab1, "col1");

      Table tab2 = new Table("table2");
      Column col2 = new Column(tab2, "col2");

      Table tab3 = new Table("table3");
      Column col3 = new Column(tab3, "col3");

      Table tab4 = new Table("table4");
      Column col4 = new Column(tab4, "col4");

      Table tab5 = new Table("table5");
      Column col5 = new Column(tab5, "col5");

      Table tab6 = new Table("table6");
      Column col6 = new Column(tab6, "col6");

      Table tab7 = new Table("table7");
      Column col7 = new Column(tab7, "col7");

      Select select = new Select(tab1);
      select.setLocked(true);
      
      tab1.addInnerJoin(tab2, col1.equal(col2));
      tab2.addFullJoin(tab3, col2.equal(col3));
      tab2.addLeftJoin(tab7, col2.equal(col7));

      tab1.addInnerJoin(tab4, col1.equal(col4));
      tab4.addRightJoin(tab5, col4.equal(col5));
      tab5.addLeftJoin(tab6, col5.equal(col6));

      queryVis.visit(select);

      assertEquals("SELECT * FROM "
              + "((\"table1\" WITH (HOLDLOCK) INNER JOIN "
              + "((\"table2\" WITH (HOLDLOCK) FULL JOIN \"table3\" WITH (HOLDLOCK) ON "
              + "\"table2\".\"col2\"=\"table3\".\"col3\") "
              + "LEFT JOIN \"table7\" WITH (HOLDLOCK) ON \"table2\".\"col2\"=\"table7\".\"col7\") "
              + "ON \"table1\".\"col1\"=\"table2\".\"col2\") "
              + "INNER JOIN (\"table4\" WITH (HOLDLOCK) "
              + "RIGHT JOIN (\"table5\" WITH (HOLDLOCK) LEFT JOIN \"table6\" WITH (HOLDLOCK) "
              + "ON \"table5\".\"col5\"=\"table6\".\"col6\") "
              + "ON \"table4\".\"col4\"=\"table5\".\"col5\") "
              + "ON \"table1\".\"col1\"=\"table4\".\"col4\")", queryVis.toString());
    }

    @Test
    public void testSelectWithFromWithLock() {
        Visitor queryVis = getVisitor();

        Table tab1 = new Table("FOO");
        Table tab2 = new Table("BAR");
        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");

        Select sel = new Select(tab1);
        sel.setLocked(true);
        sel.addFrom(tab2);
        sel.addFrom(tab3);

        queryVis.visit(sel);

        assertTrue(sel.hasFrom());

        Iterator<Qualifier> iter = sel.getFrom().iterator();
        assertEquals(iter.next(), tab1);
        assertEquals(iter.next(), tab2);
        assertEquals(iter.next(), tab3);
        assertFalse(iter.hasNext());

        assertEquals("SELECT * "
                + "FROM \"FOO\" WITH (HOLDLOCK), \"BAR\" WITH (HOLDLOCK), "
                + "\"ABC\" \"xyz\" WITH (HOLDLOCK)", queryVis.toString());
    }

    @Test
    public void testSelectWithFromAndJoinsWithLock() {
        Visitor queryVis = getVisitor();

        Table tab1 = new Table("FOO");
        Column col1 = new Column(tab1, "ID");

        Table tab2 = new Table("BAR");
        Column col21 = new Column(tab2, "ID");
        Column col22 = new Column(tab2, "ABC_ID");

        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");
        Column col3 = new Column(tab3, "ID");

        Table tab4 = new Table("FN");
        Table tab5 = new Table("ORD");
        TableAlias tab6 = new TableAlias(new Table("FN"), "abc");

        Select sel = new Select(tab1);
        sel.setLocked(true);
        tab1.addInnerJoin(tab2, col1.equal(col21));
        tab2.addLeftJoin(tab3, col22.equal(col3));

        sel.addFrom(tab4);
        sel.addFrom(tab5);
        tab5.addRightJoin(tab6);

        queryVis.visit(sel);

        assertTrue(sel.hasFrom());

        Iterator<Qualifier> iter = sel.getFrom().iterator();
        assertEquals(iter.next(), tab1);
        assertEquals(iter.next(), tab4);
        assertEquals(iter.next(), tab5);
        assertFalse(iter.hasNext());

        assertEquals("SELECT * "
                + "FROM (\"FOO\" WITH (HOLDLOCK) "
                + "INNER JOIN (\"BAR\" WITH (HOLDLOCK) "
                + "LEFT JOIN \"ABC\" \"xyz\" WITH (HOLDLOCK) "
                + "ON \"BAR\".\"ABC_ID\"=\"xyz\".\"ID\") "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")"
                + ", \"FN\" WITH (HOLDLOCK), (\"ORD\" WITH (HOLDLOCK) RIGHT JOIN \"FN\" \"abc\" "
                + "WITH (HOLDLOCK))",
                queryVis.toString());
    }

    //-----------------------------------------------------------------------------------

    @Test
    public void testVisitJoin() throws Exception {
        testVisitJoinWithLock();
        testVisitJoinWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testVisitJoinWithLock() throws Exception {
        Select select = new Select("test");
        select.setLocked(true);
        Visitor queryVis = getVisitor();
        Table tab1 = new Table("tab1");
        Column col1 = tab1.column("col1");
        Table tab2 = new Table("tab2");
        Column col2 = tab2.column("col2");
        Join join = new Join(JoinOperator.LEFT, new Table("tab1"), col1.equal(col2));

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        queryVis.visit(join);

        assertEquals("LEFT JOIN \"tab1\" WITH (HOLDLOCK) ON \"tab1\".\"col1\"=\"tab2\".\"col2\"",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testVisitJoinWithoutLock() throws Exception {
        Select select = new Select("test");
        select.setLocked(false);
        Visitor queryVis = getVisitor();
        Table tab1 = new Table("tab1");
        Column col1 = tab1.column("col1");
        Table tab2 = new Table("tab2");
        Column col2 = tab2.column("col2");
        Join join = new Join(JoinOperator.LEFT, new Table("tab1"), col1.equal(col2));

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        queryVis.visit(join);

        assertEquals("LEFT JOIN \"tab1\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\"",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstruction() {
        testHandleJoinConstructionWithLock();
        testHandleJoinConstructionWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionWithLock() {
        Select select = new Select("test");
        select.setLocked(true);
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("\"tab1\" WITH (HOLDLOCK)", queryVis.toString());

        table.addFullJoin(table2, col1.equal(col2));
        queryVis = getVisitor();

        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" WITH (HOLDLOCK) FULL JOIN \"tab2\" WITH (HOLDLOCK) ON "
                + "\"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionWithoutLock() {
        Select select = new Select("test");
        select.setLocked(false);
        Visitor queryVis = getVisitor();
        Table table = new Table("tab1");
        Column col1 = table.column("col1");
        Table table2 = new Table("tab2");
        Column col2 = table2.column("col2");

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("\"tab1\"", queryVis.toString());

        table.addFullJoin(table2, col1.equal(col2));
        queryVis = getVisitor();

        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" FULL JOIN \"tab2\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionDepth() {
        testHandleJoinConstructionDepthWithLock();
        testHandleJoinConstructionDepthWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionDepthWithLock() {
        Select select = new Select("test");
        select.setLocked(true);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" WITH (HOLDLOCK) FULL JOIN "
                + "(\"tab2\" WITH (HOLDLOCK) FULL JOIN "
                   + "(\"tab3\" WITH (HOLDLOCK) FULL JOIN \"tab4\" WITH (HOLDLOCK) ON "
                   + "\"tab3\".\"col3\"=\"tab4\".\"col4\") "
                + "ON \"tab2\".\"col2\"=\"tab3\".\"col3\") ON \"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionDepthWithoutLock() {
        Select select = new Select("test");
        select.setLocked(false);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("(\"tab1\" FULL JOIN "
                + "(\"tab2\" FULL JOIN "
                    + "(\"tab3\" FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\") "
                + "ON \"tab2\".\"col2\"=\"tab3\".\"col3\") ON \"tab1\".\"col1\"=\"tab2\".\"col2\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionBreadth() {
        testHandleJoinConstructionBreadthWithLock();
        testHandleJoinConstructionBreadthWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionBreadthWithLock() {
        Select select = new Select("test");
        select.setLocked(true);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("((("
                + "\"tab1\" WITH (HOLDLOCK) FULL JOIN \"tab2\" WITH (HOLDLOCK) ON "
                + "\"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab3\" WITH (HOLDLOCK) ON \"tab2\".\"col2\"=\"tab3\".\"col3\") "
                + "FULL JOIN \"tab4\" WITH (HOLDLOCK) ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionBreadthWithoutLock() {
        Select select = new Select("test");
        select.setLocked(false);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("((("
                + "\"tab1\" FULL JOIN \"tab2\" ON \"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab3\" ON \"tab2\".\"col2\"=\"tab3\".\"col3\") "
                + "FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @Test
    public void testHandleJoinConstructionDepthAndBreadth() {
        testHandleJoinConstructionDepthAndBreadthWithLock();
        testHandleJoinConstructionDepthAndBreadthWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionDepthAndBreadthWithLock() {
        Select select = new Select("test");
        select.setLocked(true);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("("
                + "(\"tab1\" WITH (HOLDLOCK) FULL JOIN "
                    + "(\"tab2\" WITH (HOLDLOCK) FULL JOIN \"tab3\" WITH (HOLDLOCK) ON "
                    + "\"tab2\".\"col2\"=\"tab3\".\"col3\")"
                + " ON \"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab4\" WITH (HOLDLOCK) ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testHandleJoinConstructionDepthAndBreadthWithoutLock() {
        Select select = new Select("test");
        select.setLocked(false);
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

        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        ((SQLServerQueryVisitor) queryVis).handleJoinConstruction(table);

        assertEquals("("
                + "(\"tab1\" FULL JOIN "
                    + "(\"tab2\" FULL JOIN \"tab3\" ON \"tab2\".\"col2\"=\"tab3\".\"col3\")"
                + " ON \"tab1\".\"col1\"=\"tab2\".\"col2\") "
                + "FULL JOIN \"tab4\" ON \"tab3\".\"col3\"=\"tab4\".\"col4\")",
                queryVis.toString());
    }

    @Test
    public void testAddTableNames() {
        testAddTableNamesWithLock();
        testAddTableNamesWithoutLock();
    }

    @SuppressWarnings("unchecked")
    public void testAddTableNamesWithLock() {
        Select select = new Select("test");
        select.setLocked(true);
        Visitor queryVis = getVisitor();
        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        Table table = new Table("TestTable");
        ((SQLServerQueryVisitor) queryVis).addTableNames(table);

        assertEquals("\"TestTable\" WITH (HOLDLOCK)", queryVis.toString());

        queryVis = getVisitor();

        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        TableAlias tblAls = new TableAlias(table, "TestTableAlias");
        ((SQLServerQueryVisitor) queryVis).addTableNames(tblAls);

        assertEquals("\"TestTable\" \"TestTableAlias\" WITH (HOLDLOCK)",
                queryVis.toString());
    }

    @SuppressWarnings("unchecked")
    public void testAddTableNamesWithoutLock() {
        Select select = new Select("test");
        select.setLocked(false);
        Visitor queryVis = getVisitor();
        Class[] clazz = {Select.class};
        Object[] obj = {select};
        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        Table table = new Table("TestTable");
        ((SQLServerQueryVisitor) queryVis).addTableNames(table);

        assertEquals("\"TestTable\"", queryVis.toString());

        queryVis = getVisitor();

        try {
            Method method = queryVis.getClass().getDeclaredMethod("setSelect", clazz);
            method.setAccessible(true);
            method.invoke(queryVis, obj);
        } catch (Exception e) {
            fail("Something went wrong using reflection to execute private method.");
            e.printStackTrace();
        }

        TableAlias tblAls = new TableAlias(table, "TestTableAlias");
        ((SQLServerQueryVisitor) queryVis).addTableNames(tblAls);

        assertEquals("\"TestTable\" \"TestTableAlias\"",
                queryVis.toString());
    }

    @Test
    public void testQuoteName() throws Exception {
        Visitor queryVis = getVisitor();

        String expected = "TestName"; 

        assertEquals(("\"" + expected + "\""),
                ((SQLServerQueryVisitor) queryVis).quoteName(expected));
    }

    @Test
    public void testGetSequenceNextValString() throws Exception {
        Visitor queryVis = getVisitor();

        String name = "TestName"; 

        assertEquals(null, ((SQLServerQueryVisitor) queryVis).getSequenceNextValString(name));
    }

    @Test
    public void testHandleLock() throws Exception {
        Select select = new Select("Test");
        select.setLocked(true);

        Visitor queryVis = getVisitor();
        ((SQLServerQueryVisitor) queryVis).handleLock(select);

        assertEquals("", queryVis.toString());
    }

    protected Visitor getVisitor() {
        return new SQLServerQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
