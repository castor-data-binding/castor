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
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.QueryObject;

/**
 * Junit Test for testing between condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestBetween extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Between();
        assertTrue(n instanceof SimpleCondition);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }

    /**
     * Junit Test for getters and setters.
     */
    public void testGetSet() {
        Expression high = new MockExpression();
        Expression low = new MockExpression();

        Between n = new Between();
        n.setHigh(high);
        n.setLow(low);
        assertEquals(high, n.getHigh());
        assertEquals(low, n.getLow());
    }
        
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        Between n = new Between();

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        n.setNot(true);
        assertEquals("(expression NOT BETWEEN expression AND expression)", n
                .toString());
        n.setNot(false);

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        assertEquals("(expression BETWEEN expression AND expression)", n
                .toString());

        n.setExpression(null);
        n.setHigh(new MockExpression());
        n.setLow(new MockExpression());
        assertEquals("( BETWEEN expression AND expression)", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(null);
        n.setLow(new MockExpression());
        assertEquals("(expression BETWEEN expression AND )", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(new MockExpression());
        n.setLow(null);
        assertEquals("(expression BETWEEN  AND expression)", n.toString());

        n.setExpression(null);
        n.setHigh(null);
        n.setLow(new MockExpression());
        assertEquals("( BETWEEN expression AND )", n.toString());

        n.setExpression(null);
        n.setHigh(new MockExpression());
        n.setLow(null);
        assertEquals("( BETWEEN  AND expression)", n.toString());

        n.setExpression(new MockExpression());
        n.setHigh(null);
        n.setLow(null);
        assertEquals("(expression BETWEEN  AND )", n.toString());

        n.setExpression(null);
        n.setHigh(null);
        n.setLow(null);
        assertEquals("( BETWEEN  AND )", n.toString());
    }

    //--------------------------------------------------------------------------
}
