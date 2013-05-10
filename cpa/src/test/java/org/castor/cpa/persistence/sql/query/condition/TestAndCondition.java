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

import java.util.Iterator;

import junit.framework.TestCase;

import org.castor.cpa.persistence.sql.query.expression.Column;

/** 
 * Test if AndCondition works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestAndCondition extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(CompoundCondition.class.isAssignableFrom(AndCondition.class));
    }
    
    public void testConstructorDefault() {
        AndCondition comp = new AndCondition();
        Iterator<Condition> iter = comp.iterator();
        assertFalse(iter.hasNext());
    }
    
    public void testConstructorParam() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));

        AndCondition comp = new AndCondition(cond1, cond2);
        Iterator<Condition> iter = comp.iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testConstructorCopy() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));

        AndCondition comp1 = new AndCondition(cond1, cond2);
        
        AndCondition comp2 = new AndCondition(comp1);
        Iterator<Condition> iter = comp2.iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testAppend() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));
        Condition cond3 = new IsNullPredicate(new Column("C"));

        AndCondition cond23 = new AndCondition(cond2, cond3);

        AndCondition comp = new AndCondition();

        try {
            comp.append(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            Iterator<Condition> iter = comp.iterator();
            assertFalse(iter.hasNext());
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        comp.append(cond1);
        comp.append(cond23);
        
        Iterator<Condition> iter = comp.iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond3, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testAnd() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));
        Condition cond3 = new IsNullPredicate(new Column("C"));

        AndCondition comp = new AndCondition();
        Condition cond = comp.and(cond1).and(cond2).and(cond3);
        
        assertTrue(cond instanceof AndCondition);
        assertEquals(comp, cond);
        
        Iterator<Condition> iter = ((AndCondition) cond).iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond3, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testNot() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));

        Condition cond = cond1.and(cond2).not();
        
        assertTrue(cond instanceof OrCondition);

        Iterator<Condition> iter = ((OrCondition) cond).iterator();
        assertTrue(iter.hasNext());
        Condition test1 = iter.next();
        assertTrue(test1 instanceof IsNullPredicate);
        assertEquals(cond1, test1);
        assertFalse(((IsNullPredicate) test1).evaluateTo());
        assertTrue(iter.hasNext());
        Condition test2 = iter.next();
        assertTrue(test2 instanceof IsNullPredicate);
        assertEquals(cond2, test2);
        assertFalse(((IsNullPredicate) test2).evaluateTo());
        assertFalse(iter.hasNext());
    }
    
    public void testToString() {
        Condition cond1 = new IsNullPredicate(new Column("mycol1"));
        Condition cond2 = new IsNullPredicate(new Column("mycol2"));
        Condition cond3 = new IsNullPredicate(new Column("mycol3"));

        Condition cond = cond1.and(cond2.or(cond3));
        assertEquals("mycol1 IS NULL AND (mycol2 IS NULL OR mycol3 IS NULL)", cond.toString());
    }
}
