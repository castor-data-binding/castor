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
 * Junit Test for testing and compound condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestAnd extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new And();
        assertTrue(n instanceof CompoundCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }
    
    /**
     * Junit Test for getOperator.
     */
    public void testGetOperator() {
        And and = new And();
        assertEquals(" AND ", and.getOperator());
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        And and = new And();
        and.addCondition(new MockCondition());
        and.addCondition(new MockCondition());
        assertEquals("condition AND condition", and.toString());
    }
    
    /**
     * Junit Test for factory method.
     */
    public void testFactoryMethod() {
        Condition condition1 = new MockCondition();
        Condition condition2 = new MockCondition();
        Condition condition3 = new MockCondition();
        Condition condition4 = new MockCondition();

        And and1 = new And();
        and1.addCondition(condition1);
        and1.addCondition(condition2);
        
        And and2 = new And();
        and2.addCondition(condition1);
        and2.addCondition(condition2);
        
        And and3 = new And();
        and3.addCondition(condition3);
        and3.addCondition(condition4);
        
        and1.and(condition3);
        List < Condition > conditions1 = and1.getConditions();
        assertNotNull(conditions1);
        assertEquals(3, conditions1.size());
        assertEquals(condition1, conditions1.get(0));
        assertEquals(condition2, conditions1.get(1));
        assertEquals(condition3, conditions1.get(2));
        
        and2.and(and3);
        List < Condition > conditions2 = and2.getConditions();
        assertNotNull(conditions2);
        assertEquals(4, conditions2.size());
        assertEquals(condition1, conditions2.get(0));
        assertEquals(condition2, conditions2.get(1));
        assertEquals(condition3, conditions2.get(2));
        assertEquals(condition4, conditions2.get(3));
    }   
    
    //--------------------------------------------------------------------------
}
