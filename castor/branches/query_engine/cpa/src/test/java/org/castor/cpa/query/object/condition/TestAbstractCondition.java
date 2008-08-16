/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object.condition;

import java.util.List;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.QueryObject;
import junit.framework.TestCase;

/**
 * Junit Test for testing abstract condition class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestAbstractCondition extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new MockCondition();
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }

    /**
     * Junit Test for toString.
     */
    public void testToString() {
        AbstractCondition n = new MockCondition();
        assertEquals("condition", n.toString()); 
    } 

    /**
     * Junit Test for Factory Method And.
     */
    public void testFactoryMethodAnd() {
        Condition c1 = new MockCondition();
        Condition c2 = new MockCondition();
        Condition c3 = new MockCondition();
        
        Condition condition1 = c1.and(c2);
        assertTrue(condition1 instanceof And);
        List < Condition > conditions1 = ((And) condition1).getConditions();
        assertNotNull(conditions1);
        assertEquals(2, conditions1.size());
        assertEquals(c1, conditions1.get(0));
        assertEquals(c2, conditions1.get(1));
        
        Condition condition2 = c3.and(condition1);
        assertTrue(condition2 instanceof And);
        List < Condition > conditions2 = ((And) condition2).getConditions();
        assertNotNull(conditions2);
        assertEquals(3, conditions2.size());
        assertEquals(c3, conditions2.get(0));
        assertEquals(c1, conditions2.get(1));
        assertEquals(c2, conditions2.get(2));
    }
     
    /**
     * Junit Test for Factory Method Or.
     */
    public void testFactoryMethodOr() {
        Condition c1 = new MockCondition();
        Condition c2 = new MockCondition();
        Condition c3 = new MockCondition();
        
        Condition condition1 = c1.or(c2);
        assertTrue(condition1 instanceof Or);
        List < Condition > conditions1 = ((Or) condition1).getConditions();
        assertNotNull(conditions1);
        assertEquals(2, conditions1.size());
        assertEquals(c1, conditions1.get(0));
        assertEquals(c2, conditions1.get(1));
        
        Condition condition2 = c3.or(condition1);
        assertTrue(condition2 instanceof Or);
        List < Condition > conditions2 = ((Or) condition2).getConditions();
        assertNotNull(conditions2);
        assertEquals(3, conditions2.size());
        assertEquals(c3, conditions2.get(0));
        assertEquals(c1, conditions2.get(1));
        assertEquals(c2, conditions2.get(2));
    }
     
    //--------------------------------------------------------------------------
}
