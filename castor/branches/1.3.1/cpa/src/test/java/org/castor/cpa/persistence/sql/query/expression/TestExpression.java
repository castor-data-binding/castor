/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query.expression;

import junit.framework.TestCase;

import org.castor.cpa.persistence.sql.query.QueryObject;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompareOperator;
import org.castor.cpa.persistence.sql.query.condition.Predicate;

/** 
 * Test if Expression works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestExpression extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Expression.class));
    }
    
    public void testCompareEqualFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.equal(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.EQ, compare.operator());
    }
    
    public void testCompareNotEqualFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.notEqual(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.NE, compare.operator());
    }
    
    public void testCompareGreaterThanFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.greaterThan(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.GT, compare.operator());
    }
    
    public void testCompareGreaterEqualFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.greaterEqual(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.GE, compare.operator());
    }
    
    public void testCompareLessEqualFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.lessEqual(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.LE, compare.operator());
    }
    
    public void testCompareLessThanFactory() {
        Expression left = new ExpressionMock();
        Expression right = new ExpressionMock();
        Compare compare = left.lessThan(right);
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.LT, compare.operator());
    }
    
    public void testPredicateIsNullFactory() {
        Expression expression = new ExpressionMock();
        Predicate predicate = expression.isNull();
        assertEquals(expression, predicate.expression());
        assertTrue(predicate.evaluateTo());
    }
    
    public void testPredicateIsNotNullFactory() {
        Expression expression = new ExpressionMock();
        Predicate predicate = expression.isNotNull();
        assertEquals(expression, predicate.expression());
        assertFalse(predicate.evaluateTo());
    }
}
