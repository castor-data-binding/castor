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
 * Test if CompoundCondition works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class TestCompoundCondition extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(Condition.class.isAssignableFrom(CompoundCondition.class));
    }
    
    public void testConstructorDefault() {
        CompoundCondition comp = new CompoundConditionMock();
        Iterator<Condition> iter = comp.iterator();
        assertFalse(iter.hasNext());
    }
    
    public void testConstructorCopy() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));

        CompoundCondition comp1 = new CompoundConditionMock();
        comp1.append(cond1);
        comp1.append(cond2);

        CompoundCondition comp2 = new CompoundConditionMock(comp1);
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

        CompoundCondition comp = new CompoundConditionMock();

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
        comp.append(cond2);
        comp.append(cond3);
        
        Iterator<Condition> iter = comp.iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond3, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testInsert() {
        Condition cond1 = new IsNullPredicate(new Column("A"));
        Condition cond2 = new IsNullPredicate(new Column("B"));
        Condition cond3 = new IsNullPredicate(new Column("C"));

        CompoundCondition comp = new CompoundConditionMock();

        try {
            comp.insert(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException ex) {
            Iterator<Condition> iter = comp.iterator();
            assertFalse(iter.hasNext());
        } catch (Exception ex) {
            fail("should throw NullPointerException");
        }

        comp.insert(cond1);
        comp.insert(cond2);
        comp.insert(cond3);
        
        Iterator<Condition> iter = comp.iterator();
        assertTrue(iter.hasNext());
        assertEquals(cond3, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(cond1, iter.next());
        assertFalse(iter.hasNext());
    }
}
