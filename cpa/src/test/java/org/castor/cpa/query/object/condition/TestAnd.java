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

import java.util.ArrayList;
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
        And n = new And();
        assertEquals(" AND ", n.getOperator());
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        And n = new And();
        n.addCondition(new MockCondition());
        n.addCondition(new MockCondition());
        List < Condition > conditions = new ArrayList < Condition > ();
        conditions.add(new MockCondition());
        conditions.add(new MockCondition());
        n.addAllConditions(conditions);
        assertEquals("Condition AND Condition AND Condition AND Condition", n.toString());
    }
    
    /**
     * Junit Test for factory method.
     */
    public void testFactoryMethod() {
        Condition n = new MockCondition();
        Condition condition = n.and(new MockCondition());
        assertEquals("Condition AND Condition", condition.toString());
    }   
    
//  --------------------------------------------------------------
}
