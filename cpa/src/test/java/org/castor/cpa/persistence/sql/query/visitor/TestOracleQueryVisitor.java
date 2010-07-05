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

import java.util.Iterator;

import org.castor.cpa.persistence.sql.query.Assignment;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Qualifier;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.TableAlias;
import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompareOperator;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.OrCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test if OracleQueryVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TestOracleQueryVisitor extends TestDefaultQueryVisitor {
    //---------------------------SELECT--------------------------------------------------------

    @Test
    public void testSelectNoConditionNoExpression() throws Exception {
        Select select = new Select("TestTable");

        Visitor queryVis = getVisitor();
        queryVis.visit(select);
        
        String expected = "SELECT * FROM \"TESTTABLE\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectEmptyCondition() throws Exception {
        Select select = new Select("TestTable");
        select.setCondition(new AndCondition());

        Visitor queryVis = getVisitor();
        queryVis.visit(select);
        
        String expected = "SELECT * FROM \"TESTTABLE\" WHERE ";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectWithoutCondition() throws Exception {
        Select select = new Select("TestTable");
        select.addSelect(new Column("column1"));
        select.addSelect(new Column("column2"));

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT \"COLUMN1\", \"COLUMN2\" FROM \"TESTTABLE\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectWithoutExpressionAndCondition() throws Exception {
        Select select = new Select("TestTable");

        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Column("param1")));
        condition.and(new Column("id2").equal(new NextVal("param2")));
        condition.and(new Column("id3").equal(new Parameter("param3")));
        select.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT * FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectAndCondition() throws Exception {
        Select select = new Select("TestTable");
        select.addSelect(new Column("column1"));

        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Column("param1")));
        condition.and(new Column("id2").equal(new NextVal("param2")));
        condition.and(new Column("id3").equal(new Parameter("param4")));
        select.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT \"COLUMN1\" FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectWithoutExpressionOrCondition() throws Exception {
        Select select = new Select("TestTable");

        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param3")));
        select.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT * FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" OR \"ID2\"=param2.nextval OR \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectOrCondition() throws Exception {
        Select select = new Select("TestTable");
        select.addSelect(new Column("column1"));

        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param3")));
        select.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT \"COLUMN1\" FROM \"TESTTABLE\" "
            + "WHERE \"ID\"=\"PARAM1\" OR \"ID2\"=param2.nextval OR \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectNestedCondition() throws Exception {
        Select select = new Select("TestTable");
        select.addSelect(new Column("column1"));

        Condition condition = new AndCondition();
        
        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Column("param1")));
        andCond.and(new Column("id2").equal(new NextVal("param2")));
        andCond.and(new Column("id3").equal(new Parameter("param3")));
        
        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id4").equal(new Column("param4")));
        orCond.or(new Column("id5").equal(new NextVal("param5")));
        orCond.or(new Column("id6").equal(new Parameter("param6")));
        
        condition.and(andCond);
        condition.and(orCond);
        select.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(select);

        String expected = "SELECT \"COLUMN1\" FROM \"TESTTABLE\" "
            + "WHERE \"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=? AND "
            + "(\"ID4\"=\"PARAM4\" OR \"ID5\"=param5.nextval OR \"ID6\"=?)";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testSelectWithJoinsDepth() {
        Visitor queryVis = getVisitor();
        
        Table tab1 = new Table("FOO");
        Column col1 = new Column(tab1, "ID");

        Table tab2 = new Table("BAR");
        Column col21 = new Column(tab2, "ID");
        Column col22 = new Column(tab2, "ABC_ID");

        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");
        Column col3 = new Column(tab3, "ID");

        Select sel = new Select(tab1);
        queryVis.visit(sel);
        assertEquals("SELECT * FROM \"FOO\"", queryVis.toString());

        queryVis = getVisitor();
        tab1.addInnerJoin(tab2, col1.equal(col21));
        queryVis.visit(sel);
        assertEquals("SELECT * "
                + "FROM (\"FOO\" "
                + "INNER JOIN \"BAR\" "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")", queryVis.toString());

        queryVis = getVisitor();
        tab2.addLeftJoin(tab3, col22.equal(col3));
        queryVis.visit(sel);
        assertEquals("SELECT * "
                + "FROM (\"FOO\" "
                + "INNER JOIN (\"BAR\" "
                + "LEFT JOIN \"ABC\" \"XYZ\" "
                + "ON \"BAR\".\"ABC_ID\"=\"XYZ\".\"ID\") "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")", queryVis.toString());
    }

    @Test
    public void testSelectWithJoinsBreadth() {
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
        queryVis.visit(sel);
        assertEquals("SELECT * FROM \"FOO\"", queryVis.toString());

        queryVis = getVisitor();
        tab1.addInnerJoin(tab2, col1.equal(col21));
        tab1.addInnerJoin(tab3, col1.equal(col3));
        tab1.addInnerJoin(tab4);
        tab1.addInnerJoin(tab5);
        queryVis.visit(sel);
        assertEquals("SELECT * FROM "
                + "((((\"FOO\" INNER JOIN \"BAR\" ON \"FOO\".\"ID\"=\"BAR\".\"ID\") "
                + "INNER JOIN \"ABC\" \"XYZ\" ON \"FOO\".\"ID\"=\"XYZ\".\"ID\") "
                + "INNER JOIN \"TAB4\") "
                + "INNER JOIN \"TAB5\")", queryVis.toString());
    }


    @Test
    public void testSelectWithJoinsDepthAndBreadth() {
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
      
      tab1.addInnerJoin(tab2, col1.equal(col2));
      tab2.addFullJoin(tab3, col2.equal(col3));
      tab2.addLeftJoin(tab7, col2.equal(col7));

      tab1.addInnerJoin(tab4, col1.equal(col4));
      tab4.addRightJoin(tab5, col4.equal(col5));
      tab5.addLeftJoin(tab6, col5.equal(col6));

      queryVis.visit(select);

      assertEquals("SELECT * FROM "
              + "((\"TABLE1\" INNER JOIN "
              + "((\"TABLE2\" FULL JOIN \"TABLE3\" ON \"TABLE2\".\"COL2\"=\"TABLE3\".\"COL3\") "
              + "LEFT JOIN \"TABLE7\" ON \"TABLE2\".\"COL2\"=\"TABLE7\".\"COL7\") "
              + "ON \"TABLE1\".\"COL1\"=\"TABLE2\".\"COL2\") "
              + "INNER JOIN (\"TABLE4\" "
              + "RIGHT JOIN (\"TABLE5\" LEFT JOIN \"TABLE6\" "
              + "ON \"TABLE5\".\"COL5\"=\"TABLE6\".\"COL6\") "
              + "ON \"TABLE4\".\"COL4\"=\"TABLE5\".\"COL5\") "
              + "ON \"TABLE1\".\"COL1\"=\"TABLE4\".\"COL4\")", queryVis.toString());
    }

    @Test
    public void testSelectWithFrom() {
        Visitor queryVis = getVisitor();
        
        Table tab1 = new Table("FOO");
        Table tab2 = new Table("BAR");
        TableAlias tab3 = new TableAlias(new Table("ABC"), "xyz");

        Select sel = new Select(tab1);
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
                + "FROM \"FOO\", \"BAR\", \"ABC\" \"XYZ\"", queryVis.toString());
    }

    @Test
    public void testSelectWithFromAndJoins() {
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
                + "FROM (\"FOO\" "
                + "INNER JOIN (\"BAR\" "
                + "LEFT JOIN \"ABC\" \"XYZ\" "
                + "ON \"BAR\".\"ABC_ID\"=\"XYZ\".\"ID\") "
                + "ON \"FOO\".\"ID\"=\"BAR\".\"ID\")"
                + ", \"FN\", (\"ORD\" RIGHT JOIN \"FN\" \"ABC\")", queryVis.toString());
    }

  //---------------------------INSERT--------------------------------------------------------

    @Test
    public void testInsertEmptyAssignment() throws Exception {
        Insert insert = new Insert("TestTable");

        Visitor queryVis = getVisitor();
        queryVis.visit(insert);
        
        String expected = "INSERT INTO \"TESTTABLE\" () VALUES ()";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testInsert() throws Exception {
        Insert insert = new Insert("TestTable");
        insert.addAssignment(new Column("id"), new Column("param1"));
        insert.addAssignment(new Column("id2"), new NextVal("param2"));
        insert.addAssignment(new Column("id3"), new Parameter("param3"));

        Visitor queryVis = getVisitor();
        queryVis.visit(insert);
        
        String expected = "INSERT INTO \"TESTTABLE\" (\"ID\", \"ID2\", \"ID3\") "
            + "VALUES (\"PARAM1\", param2.nextval, ?)";

        assertEquals(expected, queryVis.toString());
    }

    //---------------------------DELETE--------------------------------------------------------

    @Test
    public void testDeleteNoCondition() throws Exception {
        Delete delete = new Delete("TestTable");

        Visitor queryVis = getVisitor();
        queryVis.visit(delete);
        
        String expected = "DELETE FROM \"TESTTABLE\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testDeleteEmptyCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        delete.setCondition(new AndCondition());

        Visitor queryVis = getVisitor();
        queryVis.visit(delete);
        
        String expected = "DELETE FROM \"TESTTABLE\" WHERE ";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testDeleteAndCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        
        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Column("param1")));
        condition.and(new Column("id2").equal(new NextVal("param2")));
        condition.and(new Column("id3").equal(new Parameter("param3")));
        delete.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(delete);
        
        String expected = "DELETE FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testDeleteOrCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        
        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param3")));
        delete.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(delete);
        
        String expected = "DELETE FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" OR \"ID2\"=param2.nextval OR \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testDeleteNestedCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        
        Condition condition = new AndCondition();
        
        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Column("param1")));
        andCond.and(new Column("id2").equal(new NextVal("param2")));
        andCond.and(new Column("id3").equal(new Parameter("param3")));
        
        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id4").equal(new Column("param4")));
        orCond.or(new Column("id5").equal(new NextVal("param5")));
        orCond.or(new Column("id6").equal(new Parameter("param6")));
        
        condition.and(andCond);
        condition.and(orCond);
        delete.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(delete);
        
        String expected = "DELETE FROM \"TESTTABLE\" WHERE "
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=? AND "
            + "(\"ID4\"=\"PARAM4\" OR \"ID5\"=param5.nextval OR \"ID6\"=?)";

        assertEquals(expected, queryVis.toString());
    }

    //---------------------------UPDATE--------------------------------------------------------

    @Test
    public void testUpdateNoConditionNoAssignment() throws Exception {
        Update update = new Update("TestTable");

        Visitor queryVis = getVisitor();
        queryVis.visit(update);
        
        String expected = "UPDATE \"TESTTABLE\" SET ";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateEmptyConditionNoAssignment() throws Exception {
        Update update = new Update("TestTable");
        update.setCondition(new AndCondition());

        Visitor queryVis = getVisitor();
        queryVis.visit(update);
        
        String expected = "UPDATE \"TESTTABLE\" SET  WHERE ";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateAssignmentWithoutCondition() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Column("param1"));
        update.addAssignment(new Column("id2"), new NextVal("param2"));
        update.addAssignment(new Column("id3"), new Parameter("param3"));

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=param2.nextval, "
            + "\"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateWithoutAssignmentAndCondition() throws Exception {
        Update update = new Update("TestTable");

        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Column("param1")));
        condition.and(new Column("id2").equal(new NextVal("param2")));
        condition.and(new Column("id3").equal(new Parameter("param3")));
        update.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET "
            + " WHERE \"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateAndCondition() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Column("param1"));
        update.addAssignment(new Column("id2"), new NextVal("param2"));
        update.addAssignment(new Column("id3"), new Parameter("param3"));

        Condition condition = new AndCondition();
        condition.and(new Column("id4").equal(new Column("param4")));
        condition.and(new Column("id5").equal(new NextVal("param5")));
        condition.and(new Column("id6").equal(new Parameter("param6")));
        update.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=param2.nextval, "
            + "\"ID3\"=? WHERE \"ID4\"=\"PARAM4\" AND \"ID5\"=param5.nextval AND \"ID6\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateWithoutAssignmentOrCondition() throws Exception {
        Update update = new Update("TestTable");

        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param3")));
        update.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET "
            + " WHERE \"ID\"=\"PARAM1\" OR \"ID2\"=param2.nextval OR \"ID3\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateOrCondition() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Column("param1"));
        update.addAssignment(new Column("id2"), new NextVal("param2"));
        update.addAssignment(new Column("id3"), new Parameter("param3"));

        Condition condition = new OrCondition();
        condition.or(new Column("id3").equal(new Column("param3")));
        condition.or(new Column("id4").equal(new NextVal("param4")));
        condition.or(new Column("id5").equal(new Parameter("param5")));
        update.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=param2.nextval, "
            + "\"ID3\"=? WHERE \"ID3\"=\"PARAM3\" OR \"ID4\"=param4.nextval OR \"ID5\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testUpdateNestedCondition() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Parameter("param1"));
        update.addAssignment(new Column("id2"), new Parameter("param2"));

        Condition condition = new AndCondition();

        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id3").equal(new Column("param3")));
        andCond.and(new Column("id4").equal(new NextVal("param4")));
        andCond.and(new Column("id5").equal(new Parameter("param5")));

        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id6").equal(new Column("param6")));
        orCond.or(new Column("id7").equal(new NextVal("param7")));
        orCond.or(new Column("id8").equal(new Parameter("param8")));

        condition.and(andCond);
        condition.and(orCond);
        update.setCondition(condition);

        Visitor queryVis = getVisitor();
        queryVis.visit(update);

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=?, \"ID2\"=? "
            + "WHERE \"ID3\"=\"PARAM3\" AND \"ID4\"=param4.nextval AND \"ID5\"=? AND "
            + "(\"ID6\"=\"PARAM6\" OR \"ID7\"=param7.nextval OR \"ID8\"=?)";

        assertEquals(expected, queryVis.toString());
    }

    //---------------------------METHODS--------------------------------------------------------

    @Test
    public void testVisitAssignmentColumn() throws Exception {
        Visitor queryVis = getVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new Column("column2"));

        queryVis.visit(assignment);

        String expected = "\"COLUMN1\"=\"COLUMN2\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitAssignmentNextVal() throws Exception {
        Visitor queryVis = getVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new NextVal("column2"));

        queryVis.visit(assignment);

        String expected = "\"COLUMN1\"=column2.nextval";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitAssignmentParameter() throws Exception {
        Visitor queryVis = getVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new Parameter("column2"));

        queryVis.visit(assignment);

        String expected = "\"COLUMN1\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitColumn() throws Exception {
        Visitor queryVis = getVisitor();
        Column column = new Column(new Table("TestTable"), "column1");

        queryVis.visit(column);

        String expected = "\"TESTTABLE\".\"COLUMN1\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitTable() throws Exception {
        Visitor queryVis = getVisitor();
        Table table = new Table("TestTable");

        queryVis.visit(table);

        String expected = "\"TESTTABLE\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitAndCondition() throws Exception {
        Visitor queryVis = getVisitor();
        AndCondition condition = new AndCondition();
        condition.and(new Column("id").equal(new Column("param1")));
        condition.and(new Column("id2").equal(new NextVal("param2")));
        condition.and(new Column("id3").equal(new Parameter("param3")));
        condition.and(new IsNullPredicate(new Column("param4")));
        condition.and(new IsNullPredicate(new NextVal("param5")));
        condition.and(new IsNullPredicate(new Parameter("param6")));
        condition.and(new IsNullPredicate(new Column("param7"), false));
        condition.and(new IsNullPredicate(new NextVal("param8"), false));
        condition.and(new IsNullPredicate(new Parameter("param9"), false));

        queryVis.visit(condition);

        String expected = "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=? "
            + "AND \"PARAM4\" IS NULL AND param5.nextval IS NULL AND ? IS NULL "
            + "AND \"PARAM7\" IS NOT NULL AND param8.nextval IS NOT NULL AND ? IS NOT NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareColumns() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new Column("id2"));

        queryVis.visit(comp);

        String expected = "\"ID\"=\"ID2\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareNextVal() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new NextVal("id"), CompareOperator.EQ, new NextVal("id2"));

        queryVis.visit(comp);

        String expected = "id.nextval=id2.nextval";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareColumnParameter() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new Parameter("id2"));

        queryVis.visit(comp);

        String expected = "\"ID\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareColumnNextVal() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new NextVal("id2"));

        queryVis.visit(comp);

        String expected = "\"ID\"=id2.nextval";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareNextValParameter() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new NextVal("id"), CompareOperator.EQ, new Parameter("id2"));

        queryVis.visit(comp);

        String expected = "id.nextval=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitIsNullPredicateColumnFalse() throws Exception {
        Visitor queryVis = getVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new Column("id"), false);

        queryVis.visit(isNullPredicate);

        String expected = "\"ID\" IS NOT NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitIsNullPredicateColumnTrue() throws Exception {
        Visitor queryVis = getVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new Column("id"));

        queryVis.visit(isNullPredicate);

        String expected = "\"ID\" IS NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitIsNullPredicateNextValFalse() throws Exception {
        Visitor queryVis = getVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new NextVal("id"), false);

        queryVis.visit(isNullPredicate);

        String expected = "id.nextval IS NOT NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitIsNullPredicateNextValTrue() throws Exception {
        Visitor queryVis = getVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new NextVal("id"));

        queryVis.visit(isNullPredicate);

        String expected = "id.nextval IS NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitOrCondition() throws Exception {
        Visitor queryVis = getVisitor();
        OrCondition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param2")));
        condition.or(new IsNullPredicate(new Column("param4")));
        condition.or(new IsNullPredicate(new NextVal("param5")));
        condition.or(new IsNullPredicate(new Parameter("param6")));
        condition.or(new IsNullPredicate(new Column("param7"), false));
        condition.or(new IsNullPredicate(new NextVal("param8"), false));
        condition.or(new IsNullPredicate(new Parameter("param9"), false));

        queryVis.visit(condition);

        String expected = "\"ID\"=\"PARAM1\" OR \"ID2\"=param2.nextval OR \"ID3\"=? "
            + "OR \"PARAM4\" IS NULL OR param5.nextval IS NULL OR ? IS NULL "
            + "OR \"PARAM7\" IS NOT NULL OR param8.nextval IS NOT NULL OR ? IS NOT NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitNestedCondition() throws Exception {
        Visitor queryVis = getVisitor();
        AndCondition condition = new AndCondition();

        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Column("param1")));
        andCond.and(new Column("id2").equal(new NextVal("param2")));
        andCond.and(new Column("id3").equal(new Parameter("param3")));

        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id4").equal(new Column("param4")));
        orCond.or(new Column("id5").equal(new NextVal("param5")));
        orCond.or(new Column("id6").equal(new Parameter("param6")));

        condition.and(andCond);
        condition.and(orCond);

        queryVis.visit(condition);

        String expected = "\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=? "
            + "AND (\"ID4\"=\"PARAM4\" OR \"ID5\"=param5.nextval OR \"ID6\"=?)";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitNestedCondition2() throws Exception {
        Visitor queryVis = getVisitor();
        OrCondition condition = new OrCondition();

        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Column("param1")));
        andCond.and(new Column("id2").equal(new NextVal("param2")));
        andCond.and(new Column("id3").equal(new Parameter("param3")));

        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id4").equal(new Column("param4")));
        orCond.or(new Column("id5").equal(new NextVal("param5")));
        orCond.or(new Column("id6").equal(new Parameter("param6")));

        condition.or(andCond);
        condition.or(orCond);

        queryVis.visit(condition);

        String expected = "(\"ID\"=\"PARAM1\" AND \"ID2\"=param2.nextval AND \"ID3\"=?) "
            + "OR \"ID4\"=\"PARAM4\" OR \"ID5\"=param5.nextval OR \"ID6\"=?";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitColumnWithoutQualifier() throws Exception {
        Visitor queryVis = getVisitor();
        Column column = new Column("column1");

        queryVis.visit(column);

        String expected = "\"COLUMN1\"";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitNextVal() throws Exception {
        Visitor queryVis = getVisitor();
        NextVal nextVal = new NextVal("nextVal");

        queryVis.visit(nextVal);

        String expected = "nextVal.nextval";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testQuoteName() throws Exception {
        Visitor queryVis = getVisitor();

        String expected = "TestName"; 

        assertEquals(("\"" + expected + "\"").toUpperCase(),
                ((OracleQueryVisitor) queryVis).quoteName(expected));
    }

    @Test
    public void testGetSequenceNextValString() throws Exception {
        Visitor queryVis = getVisitor();

        String name = "TestName"; 

        assertEquals(name + ".nextval",
                ((OracleQueryVisitor) queryVis).getSequenceNextValString(name));
    }

    protected Visitor getVisitor() {
        return new OracleQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
