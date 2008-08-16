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
 * Junit Test for testing or compound condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestOr extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public final void testInstance() {
        QueryObject n = new Or();
        assertTrue(n instanceof CompoundCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }
    
    /**
     * Junit Test for getOperator.
     */
    public final void testGetOperator() {
        Or or = new Or();
        assertEquals(" OR ", or.getOperator());
    }
    
    /**
     * Junit Test for toString.
     */
    public final void testToString() {
        Or or = new Or();
        or.addCondition(new MockCondition());
        or.addCondition(new MockCondition());
        assertEquals("condition OR condition", or.toString());
    }
    
    /**
     * Junit Test for factory method.
     */
    public final void testFactoryMethod() {
        Condition condition1 = new MockCondition();
        Condition condition2 = new MockCondition();
        Condition condition3 = new MockCondition();
        Condition condition4 = new MockCondition();

        Or or1 = new Or();
        or1.addCondition(condition1);
        or1.addCondition(condition2);
        
        Or or2 = new Or();
        or2.addCondition(condition1);
        or2.addCondition(condition2);
        
        Or or3 = new Or();
        or3.addCondition(condition3);
        or3.addCondition(condition4);
        
        or1.or(condition3);
        List < Condition > conditions1 = or1.getConditions();
        assertNotNull(conditions1);
        assertEquals(3, conditions1.size());
        assertEquals(condition1, conditions1.get(0));
        assertEquals(condition2, conditions1.get(1));
        assertEquals(condition3, conditions1.get(2));
        
        or2.or(or3);
        List < Condition > conditions2 = or2.getConditions();
        assertNotNull(conditions2);
        assertEquals(4, conditions2.size());
        assertEquals(condition1, conditions2.get(0));
        assertEquals(condition2, conditions2.get(1));
        assertEquals(condition3, conditions2.get(2));
        assertEquals(condition4, conditions2.get(3));
    }
    
    //--------------------------------------------------------------------------
}
