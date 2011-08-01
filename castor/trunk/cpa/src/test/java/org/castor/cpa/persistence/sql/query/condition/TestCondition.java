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

import org.castor.cpa.persistence.sql.query.QueryObject;

/** 
 * Test if Condition works as expected.
 *
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestCondition extends TestCase {
    public void testExtendsHierarchy() {
        assertTrue(QueryObject.class.isAssignableFrom(Condition.class));
    }
    
    public void testConditionAndFactory() {
        Condition first = new ConditionMock();
        Condition second = new ConditionMock();
        
        Condition condition = first.and(second);
        assertTrue(condition instanceof AndCondition);
        Iterator<Condition> iter = ((AndCondition) condition).iterator();
        assertTrue(iter.hasNext());
        assertEquals(first, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(second, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testConditionAndFactoryAnd() {
        Condition first = new ConditionMock();
        Condition second = new ConditionMock();
        Condition third = new ConditionMock();
        
        Condition and = new AndCondition(second, third);
        Condition condition = first.and(and);
        assertTrue(condition instanceof AndCondition);
        assertEquals(and, condition);
        Iterator<Condition> iter = ((AndCondition) condition).iterator();
        assertTrue(iter.hasNext());
        assertEquals(first, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(second, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(third, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testConditionOrFactory() {
        Condition first = new ConditionMock();
        Condition second = new ConditionMock();
        
        Condition condition = first.or(second);
        assertTrue(condition instanceof OrCondition);
        Iterator<Condition> iter = ((OrCondition) condition).iterator();
        assertTrue(iter.hasNext());
        assertEquals(first, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(second, iter.next());
        assertFalse(iter.hasNext());
    }
    
    public void testConditionOrFactoryOr() {
        Condition first = new ConditionMock();
        Condition second = new ConditionMock();
        Condition third = new ConditionMock();
        
        Condition or = new OrCondition(second, third);
        Condition condition = first.or(or);
        assertTrue(condition instanceof OrCondition);
        assertEquals(or, condition);
        Iterator<Condition> iter = ((OrCondition) condition).iterator();
        assertTrue(iter.hasNext());
        assertEquals(first, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(second, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(third, iter.next());
        assertFalse(iter.hasNext());
    }
}
