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
 * Test if Predicate works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestIsNullPredicate extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(Predicate.class.isAssignableFrom(IsNullPredicate.class));
    }
    
    public void testConstructorName() {
        Expression expression = new Column("mycolumn");

        Predicate predicate = null;
        try {
            predicate = new IsNullPredicate(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(predicate);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            predicate = new IsNullPredicate(null, true);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            assertNull(predicate);
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        try {
            predicate = new IsNullPredicate(expression);
            assertEquals(expression, predicate.expression());
            assertTrue(predicate.evaluateTo());
            assertEquals("mycolumn IS NULL", predicate.toString());

            predicate = new IsNullPredicate(expression, true);
            assertEquals(expression, predicate.expression());
            assertTrue(predicate.evaluateTo());
            assertEquals("mycolumn IS NULL", predicate.toString());

            predicate = new IsNullPredicate(expression, false);
            assertEquals(expression, predicate.expression());
            assertFalse(predicate.evaluateTo());
            assertEquals("mycolumn IS NOT NULL", predicate.toString());
        } catch (Exception ex) {
            fail("should not throw exception");
        }
    }
}
