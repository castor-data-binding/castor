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
package org.castor.cpa.persistence.sql.query.condition;

import junit.framework.TestCase;

import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/** 
 * Test if Compare works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestCompare extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(Condition.class.isAssignableFrom(Compare.class));
    }
    
    public void testConstructorName() {
        Expression left = new Column("left");
        Expression right = new Column("right");

        Compare compare = null;
        try {
            compare = new Compare(null, CompareOperator.EQ, right);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(compare);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            compare = new Compare(left, null, right);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(compare);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            compare = new Compare(left, CompareOperator.EQ, null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(compare);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            compare = new Compare(left, CompareOperator.EQ, right);
            assertEquals(left, compare.leftExpression());
            assertEquals(right, compare.rightExpression());
            assertEquals(CompareOperator.EQ, compare.operator());
            assertEquals("left=right", compare.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }

    public void testCompareNotFactory() {
        Expression left = new Column("left");
        Expression right = new Column("right");

        Compare compare = new Compare(left, CompareOperator.EQ, right);
        
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.EQ, compare.operator());
        assertEquals("left=right", compare.toString());
        
        Condition condition = compare.not();
        assertTrue(compare == condition);
        compare = (Compare) condition;
        
        assertEquals(left, compare.leftExpression());
        assertEquals(right, compare.rightExpression());
        assertEquals(CompareOperator.NE, compare.operator());
        assertEquals("left<>right", compare.toString());
    }
}
