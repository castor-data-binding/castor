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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.castor.cpa.persistence.sql.query.Assignment;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Update;
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

/**
 * Test if ParameterVisitor works as expected.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision$ $Date$
 */
public final class TestParameterVisitor  {
    //---------------------------SELECT--------------------------------------------------------

    @Test
    public void testSelectNoCondition() throws Exception {
        Select select = new Select("TestTable");

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testSelectEmptyCondition() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new AndCondition();
        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testSelectConditionCompare() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Parameter("param1")));
        condition.and(new Column("id").equal(new Column("param2")));
        condition.and(new Column("id").equal(new NextVal("param2")));
        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    @Test
    public void testSelectAndCondition() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Parameter("param1")));
        condition.and(new Column("id2").equal(new Parameter("param2")));
        condition.and(new IsNullPredicate(new Parameter("param3"), false));
        condition.and(new IsNullPredicate(new Parameter("param4"), true));
        condition.and(new IsNullPredicate(new Column("param5"), false));
        condition.and(new IsNullPredicate(new Column("param6"), true));
        condition.and(new IsNullPredicate(new NextVal("param7"), false));
        condition.and(new IsNullPredicate(new NextVal("param8"), true));
        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param3"));
        assertEquals(new Integer(4), parameters.get("param4"));
    }

    @Test
    public void testSelectOrCondition() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Parameter("param1")));
        condition.or(new Column("id").equal(new Parameter("param2")));
        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(2, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
    }

    @Test
    public void testSelectNestedConditions() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new AndCondition();
        
        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Parameter("param1")));
        andCond.and(new Column("id2").equal(new Parameter("param2")));
        
        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id3").equal(new Parameter("param3")));
        orCond.or(new Column("id4").equal(new Parameter("param4")));
        
        condition.and(andCond);
        condition.and(orCond);

        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param3"));
        assertEquals(new Integer(4), parameters.get("param4"));
        
    }

    @Test
    public void testSelectIsNullPredicate() throws Exception {
        Select select = new Select("TestTable");
        Condition condition = new AndCondition();
        condition.and(new IsNullPredicate(new Parameter("param1")));
        condition.and(new IsNullPredicate(new Column("column1")));
        select.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(select);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    //---------------------------DELETE--------------------------------------------------------

    @Test
    public void testDeleteNoCondition() throws Exception {
        Delete delete = new Delete("TestTable");

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testDeleteEmptyCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new AndCondition();
        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testDeleteConditionCompare() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Parameter("param1")));
        condition.and(new Column("id").equal(new Column("param2")));
        condition.and(new Column("id").equal(new NextVal("param2")));
        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    @Test
    public void testDeleteAndCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new AndCondition();
        condition.and(new Column("id").equal(new Parameter("param1")));
        condition.and(new Column("id2").equal(new Parameter("param2")));
        condition.and(new Column("id3").equal(new Column("param2")));
        condition.and(new Column("id4").equal(new NextVal("param2")));
        condition.and(new IsNullPredicate(new Parameter("param3"), false));
        condition.and(new IsNullPredicate(new Parameter("param4"), true));
        condition.and(new IsNullPredicate(new Column("param5"), false));
        condition.and(new IsNullPredicate(new Column("param6"), true));
        condition.and(new IsNullPredicate(new NextVal("param7"), false));
        condition.and(new IsNullPredicate(new NextVal("param8"), true));
        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param3"));
        assertEquals(new Integer(4), parameters.get("param4"));
    }

    @Test
    public void testDeleteOrCondition() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new OrCondition();
        condition.or(new Column("id").equal(new Parameter("param1")));
        condition.or(new Column("id2").equal(new Parameter("param2")));
        condition.or(new Column("id3").equal(new Column("param3")));
        condition.or(new Column("id4").equal(new NextVal("param4")));
        condition.or(new IsNullPredicate(new Parameter("param5"), false));
        condition.or(new IsNullPredicate(new Parameter("param6"), true));
        condition.or(new IsNullPredicate(new Column("param7"), false));
        condition.or(new IsNullPredicate(new Column("param8"), true));
        condition.or(new IsNullPredicate(new NextVal("param9"), false));
        condition.or(new IsNullPredicate(new NextVal("param10"), true));
        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param5"));
        assertEquals(new Integer(4), parameters.get("param6"));
    }

    @Test
    public void testDeleteNestedConditions() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new AndCondition();
        
        AndCondition andCond = new AndCondition();
        andCond.and(new Column("id").equal(new Parameter("param1")));
        andCond.and(new Column("id2").equal(new Parameter("param2")));
        andCond.and(new Column("id3").equal(new Column("column3")));
        andCond.and(new Column("id4").equal(new NextVal("param4")));
        
        OrCondition orCond = new OrCondition();
        orCond.or(new Column("id5").equal(new Parameter("param5")));
        orCond.or(new Column("id6").equal(new Parameter("param6")));
        orCond.or(new Column("id7").equal(new Column("param7")));
        orCond.or(new Column("id8").equal(new NextVal("param8")));

        condition.and(andCond);
        condition.and(orCond);

        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param5"));
        assertEquals(new Integer(4), parameters.get("param6"));
        
    }

    @Test
    public void testDeleteIsNullPredicate() throws Exception {
        Delete delete = new Delete("TestTable");
        Condition condition = new AndCondition();
        condition.and(new IsNullPredicate(new Parameter("param1")));
        condition.and(new IsNullPredicate(new Column("column1")));
        delete.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(delete);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    //---------------------------UPDATE--------------------------------------------------------

    @Test
    public void testUpdateNoAssignmentNoCondition() throws Exception {
        Update update = new Update("TestTable");

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(update);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testUpdateAssignmentWithoutCondition() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Parameter("param1"));
        update.addAssignment(new Column("id2"), new Parameter("param2"));
        update.addAssignment(new Column("id3"), new Column("param3"));
        update.addAssignment(new Column("id4"), new NextVal("param4"));

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(update);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(2, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
    }

    @Test
    public void testUpdateConditionWithAssignment() throws Exception {
        Update update = new Update("TestTable");
        update.addAssignment(new Column("id"), new Parameter("param1"));
        update.addAssignment(new Column("id2"), new Parameter("param2"));
        update.addAssignment(new Column("id3"), new Column("param3"));
        update.addAssignment(new Column("id4"), new NextVal("param4"));
        
        Condition condition = new AndCondition();
        condition.and(new Column("id5").equal(new Parameter("param5")));
        condition.and(new Column("id6").equal(new Parameter("param6")));
        update.addAssignment(new Column("id7"), new Column("param7"));
        update.addAssignment(new Column("id8"), new NextVal("param8"));

        update.setCondition(condition);

        ParameterVisitor parmVis = new ParameterVisitor();
        parmVis.visit(update);
        
        Map<String, Integer> parameters = parmVis.getParameters();

        assertFalse(parameters.isEmpty());
        assertEquals(4, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
        assertEquals(new Integer(3), parameters.get("param5"));
        assertEquals(new Integer(4), parameters.get("param6"));
    }

    //---------------------------METHODS--------------------------------------------------------

    @Test
    public void testVisitAssignmentColumn() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new Column("column2"));

        parmVis.visit(assignment);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testVisitAssignmentNextVal() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new NextVal("column2"));

        parmVis.visit(assignment);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testVisitAssignmentParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Assignment assignment = new Assignment(new Column("column1"), new Parameter("param1"));

        parmVis.visit(assignment);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    @Test
    public void testVisitAndCondition() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
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

        parmVis.visit(condition);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(3, parameters.size());
        assertEquals(new Integer(1), parameters.get("param3"));
        assertEquals(new Integer(2), parameters.get("param6"));
        assertEquals(new Integer(3), parameters.get("param9"));
    }

    @Test
    public void testVisitCompareColumns() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new Column("id2"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testVisitCompareNextVal() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new NextVal("id"), CompareOperator.EQ, new NextVal("id2"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testVisitCompareParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new Parameter("param1"),
                CompareOperator.EQ, new Parameter("param2"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(2, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
    }

    @Test
    public void testVisitCompareColumnParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new Parameter("param1"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    @Test
    public void testVisitCompareColumnNextVal() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new Column("id"), CompareOperator.EQ, new NextVal("id2"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testVisitCompareNextValParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Compare comp = new Compare(new NextVal("id"), CompareOperator.EQ, new Parameter("param1"));

        parmVis.visit(comp);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    @Test
    public void testVisitIsNullPredicateParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        IsNullPredicate isNullPredicate = new IsNullPredicate(new Parameter("param1"), false);

        parmVis.visit(isNullPredicate);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));

        isNullPredicate = new IsNullPredicate(new Parameter("param2"));

        parmVis.visit(isNullPredicate);

        assertEquals(2, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
        assertEquals(new Integer(2), parameters.get("param2"));
    }

    @Test
    public void testVisitOrCondition() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        OrCondition condition = new OrCondition();
        condition.or(new Column("id").equal(new Column("param1")));
        condition.or(new Column("id2").equal(new NextVal("param2")));
        condition.or(new Column("id3").equal(new Parameter("param3")));
        condition.or(new IsNullPredicate(new Column("param4")));
        condition.or(new IsNullPredicate(new NextVal("param5")));
        condition.or(new IsNullPredicate(new Parameter("param6")));
        condition.or(new IsNullPredicate(new Column("param7"), false));
        condition.or(new IsNullPredicate(new NextVal("param8"), false));
        condition.or(new IsNullPredicate(new Parameter("param9"), false));

        parmVis.visit(condition);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(3, parameters.size());
        assertEquals(new Integer(1), parameters.get("param3"));
        assertEquals(new Integer(2), parameters.get("param6"));
        assertEquals(new Integer(3), parameters.get("param9"));
    }

    @Test
    public void testVisitParameter() throws Exception {
        ParameterVisitor parmVis = new ParameterVisitor();
        Parameter parameter = new Parameter("param1");

        parmVis.visit(parameter);

        Map<String, Integer> parameters = parmVis.getParameters();
        assertFalse(parameters.isEmpty());
        assertEquals(1, parameters.size());
        assertEquals(new Integer(1), parameters.get("param1"));
    }

    //-----------------------------------------------------------------------------------
}
