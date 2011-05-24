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
    //--------------------------------------------------------------------------
    
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
        Condition condition1 = new MockCondition();
        Condition condition2 = new MockCondition();

        List < Condition > conditions = new ArrayList < Condition > ();
        conditions.add(condition1);
        conditions.add(condition2);

        MockCompoundCondition compound1 = new MockCompoundCondition();

        try {
            compound1.addCondition(null);
            fail("NullPointerException expected.");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
        
        compound1.addCondition(condition1);
        compound1.addCondition(condition2);
        
        List < Condition > conditions1 = compound1.getConditions();
        assertNotNull(conditions1);
        assertEquals(2, conditions1.size());
        assertEquals(condition1, conditions1.get(0));
        assertEquals(condition2, conditions1.get(1));
        
        MockCompoundCondition compound2 = new MockCompoundCondition();

        try {
            List < Condition > nulllist = new ArrayList < Condition > ();
            nulllist.add(null);

            compound2.addAllConditions(nulllist);
            fail("NullPointerException expected.");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
        
        compound2.addAllConditions(conditions);
        
        List < Condition > conditions2 = compound2.getConditions();
        assertNotNull(conditions2);
        assertEquals(2, conditions2.size());
        assertEquals(condition1, conditions2.get(0));
        assertEquals(condition2, conditions2.get(1));
    }
    
    public void testToString() {
        MockCompoundCondition compound1 = new MockCompoundCondition();
        compound1.addCondition(new MockCondition());
        compound1.addCondition(new MockCondition());
        assertEquals("condition operator condition", compound1.toString());

        MockCompoundCondition compound2 = new MockCompoundCondition();
        compound2.addCondition(new MockCondition());
        compound2.addCondition(new MockCondition());
        assertEquals("condition operator condition", compound2.toString());

        MockCompoundCondition compound = new MockCompoundCondition();
        compound.addCondition(compound1);
        compound.addCondition(compound2);
        assertEquals("(condition operator condition) operator (condition operator condition)",
                compound.toString());
    }
    
    public void testFactoryMethodNot() {
        MockCompoundCondition compound = new MockCompoundCondition();
        Condition condition = compound.not();
        assertTrue(condition instanceof Not);
        assertEquals(compound, ((Not) condition).getCondition());
    }
    
    //--------------------------------------------------------------------------
}
