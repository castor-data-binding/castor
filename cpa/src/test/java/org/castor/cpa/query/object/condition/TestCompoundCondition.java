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
 * Junit Test for testing compound condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestCompoundCondition extends TestCase {
    //--------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new MockCompoundCondition();
        assertTrue(n instanceof CompoundCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
     }

    /**
     * Junit Test for getter setter.
     */
    public void testGetSet() {
        MockCompoundCondition n = new MockCompoundCondition();
        Condition condition1 = new MockCondition();
        Condition condition2 = new MockCondition();
        Condition condition3 = new MockCondition();
        Condition condition4 = new MockCondition();
        n.addCondition(condition1);
        n.addCondition(condition2);
        List < Condition > l = new ArrayList < Condition > ();
        l.add(condition3);
        l.add(condition4);
        List < Condition > list = n.getConditions();
        list.addAll(l);
        assertEquals(condition1, list.get(0));
        assertEquals(condition2, list.get(1));
        assertEquals(condition3, list.get(2));
        assertEquals(condition4, list.get(3));
    } 
    
    public void testToString() {
        MockCompoundCondition n = new MockCompoundCondition();
        Condition condition1 = new MockCondition();
        Condition condition2 = new MockCondition();
        Condition condition3 = new MockCondition();
        Condition condition4 = new MockCondition();
        n.addCondition(condition1);
        n.addCondition(condition2);
        List < Condition > l = new ArrayList < Condition > ();
        l.add(condition3);
        l.add(condition4);
        List < Condition > list = n.getConditions();
        list.addAll(l);
        assertEquals("Condition operator Condition" 
                + " operator Condition operator Condition", n.toString());
    }
    //--------------------------------------------------------------
}
