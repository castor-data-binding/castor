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

import org.castor.cpa.persistence.sql.query.Assignment;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
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
public final class TestSapDbQueryVisitor extends TestDefaultQueryVisitor {
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
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=?";

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
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=?";

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
            + "\"ID\"=\"PARAM1\" OR \"ID2\"=null OR \"ID3\"=?";

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
            + "WHERE \"ID\"=\"PARAM1\" OR \"ID2\"=null OR \"ID3\"=?";

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
            + "WHERE \"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=? AND "
            + "(\"ID4\"=\"PARAM4\" OR \"ID5\"=null OR \"ID6\"=?)";

        assertEquals(expected, queryVis.toString());
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
            + "VALUES (\"PARAM1\", null, ?)";

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
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=?";

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
            + "\"ID\"=\"PARAM1\" OR \"ID2\"=null OR \"ID3\"=?";

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
            + "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=? AND "
            + "(\"ID4\"=\"PARAM4\" OR \"ID5\"=null OR \"ID6\"=?)";

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

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=null, \"ID3\"=?";

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
            + " WHERE \"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=?";

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

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=null, \"ID3\"=? "
            + "WHERE \"ID4\"=\"PARAM4\" AND \"ID5\"=null AND \"ID6\"=?";

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
            + " WHERE \"ID\"=\"PARAM1\" OR \"ID2\"=null OR \"ID3\"=?";

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

        String expected = "UPDATE \"TESTTABLE\" SET \"ID\"=\"PARAM1\", \"ID2\"=null, \"ID3\"=? "
            + "WHERE \"ID3\"=\"PARAM3\" OR \"ID4\"=null OR \"ID5\"=?";

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
            + "WHERE \"ID3\"=\"PARAM3\" AND \"ID4\"=null AND \"ID5\"=? AND "
            + "(\"ID6\"=\"PARAM6\" OR \"ID7\"=null OR \"ID8\"=?)";

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

        String expected = "\"COLUMN1\"=null";

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

        String expected = "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=? "
            + "AND \"PARAM4\" IS NULL AND null IS NULL AND ? IS NULL "
            + "AND \"PARAM7\" IS NOT NULL AND null IS NOT NULL AND ? IS NOT NULL";

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

        String expected = "null=null";

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

        String expected = "\"ID\"=null";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitCompareNextValParameter() throws Exception {
        Visitor queryVis = getVisitor();
        Compare comp = new Compare(new NextVal("id"), CompareOperator.EQ, new Parameter("id2"));

        queryVis.visit(comp);

        String expected = "null=?";

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

        String expected = "null IS NOT NULL";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testVisitIsNullPredicateNextValTrue() throws Exception {
        Visitor queryVis = getVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new NextVal("id"));

        queryVis.visit(isNullPredicate);

        String expected = "null IS NULL";

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

        String expected = "\"ID\"=\"PARAM1\" OR \"ID2\"=null OR \"ID3\"=? "
            + "OR \"PARAM4\" IS NULL OR null IS NULL OR ? IS NULL "
            + "OR \"PARAM7\" IS NOT NULL OR null IS NOT NULL OR ? IS NOT NULL";

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

        String expected = "\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=? "
            + "AND (\"ID4\"=\"PARAM4\" OR \"ID5\"=null OR \"ID6\"=?)";

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

        String expected = "(\"ID\"=\"PARAM1\" AND \"ID2\"=null AND \"ID3\"=?) "
            + "OR \"ID4\"=\"PARAM4\" OR \"ID5\"=null OR \"ID6\"=?";

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

        String expected = "null";

        assertEquals(expected, queryVis.toString());
    }

    @Test
    public void testQuoteName() throws Exception {
        Visitor queryVis = getVisitor();

        String expected = "\"TESTTABLE\""; 

        assertEquals(("\"" + expected + "\"").toUpperCase(),
                ((SapDbQueryVisitor) queryVis).quoteName(expected));
    }

    @Test
    public void testGetSequenceNextValString() throws Exception {
        Visitor queryVis = getVisitor();

        String name = "\"TESTTABLE\""; 

        assertEquals(null,
                ((SapDbQueryVisitor) queryVis).getSequenceNextValString(name));
    }

    protected Visitor getVisitor() {
        return new SapDbQueryVisitor();
    }

    //-----------------------------------------------------------------------------------
}
