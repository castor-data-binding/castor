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

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.QueryObject;

/**
 * Junit Test for testing not condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestNot extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Not();
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }
    
    /**
     * Junit Test for getters and setters.
     */
    public void testGetSet() {
        Condition condition = new MockCondition();

        Not not = new Not();
        not.setCondition(condition);
        assertEquals(condition, not.getCondition());
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        Not not = new Not();
        assertEquals("NOT ", not.toString());

        not.setCondition(new MockCondition());
        assertEquals("NOT condition", not.toString());

        CompoundCondition compound = new MockCompoundCondition();
        compound.addCondition(new MockCondition());
        compound.addCondition(new MockCondition());
        not.setCondition(compound);
        assertEquals("NOT (condition operator condition)", not.toString());
    }
    
    /**
     * Junit Test for not factory method.
     */
    public void testFactoryMethodNot() {
        Condition condition = new MockCondition();

        Not not = new Not();
        not.setCondition(condition);
        assertEquals(condition, not.not());
    }
    
    //--------------------------------------------------------------------------
}
