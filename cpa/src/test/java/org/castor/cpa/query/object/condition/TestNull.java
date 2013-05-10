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
 * Junit Test for testing null condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestNull extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Null();
        assertTrue(n instanceof SimpleCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        Null n = new Null();
        n.setNot(false);
        n.setExpression(null);
        assertEquals("( IS NULL)", n.toString());

        n.setNot(true);
        n.setExpression(null);
        assertEquals("( IS NOT NULL)", n.toString());

        n.setNot(false);
        n.setExpression(new MockField());
        assertEquals("(field IS NULL)", n.toString());

        n.setNot(true);
        n.setExpression(new MockField());
        assertEquals("(field IS NOT NULL)", n.toString());
    }
    
    //--------------------------------------------------------------------------
}
