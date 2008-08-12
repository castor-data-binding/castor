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
package org.castor.cpa.query.object.expression;

import java.util.List;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.QueryObject;

import junit.framework.TestCase;

/**
 * Junit Test for testing abstract compound expression class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr
 *          2006) $
 * @since 1.3
 */
public final class TestCompoundExpression extends TestCase {
    // --------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new MockCompoundExpression();
        assertTrue(n instanceof CompoundExpression);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for getOperator.
     */
    public void testGetOperator() {
        MockCompoundExpression n = new MockCompoundExpression();
        assertEquals(" & ", n.getOperator());
    }

    /**
     * Junit Test for GetSetExpression.
     */
    public void testGetSetExpression() {
        MockExpression exp1 = new MockExpression();
        MockExpression exp2 = new MockExpression();
        MockExpression exp3 = new MockExpression();
        
        MockCompoundExpression n = new MockCompoundExpression();
        n.addExpression(exp1);
        n.addExpression(exp2);
        n.addExpression(exp3);
        
        List < Expression > exps = n.getExpressions();
        assertTrue(exps.contains(exp1));
        assertTrue(exps.contains(exp2));
        assertTrue(exps.contains(exp3));
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        MockCompoundExpression n = new MockCompoundExpression();
        n.addExpression(new MockExpression());
        n.addExpression(new MockExpression());
        assertEquals("expression & expression", n.toString());
    }

    // --------------------------------------------------------------------------
}
