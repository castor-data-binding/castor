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

import junit.framework.TestCase;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.literal.LongLiteral;

/**
 * Junit Test for testing subtract arithmetic expression of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestSubtract extends TestCase {
    // --------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Subtract();
        assertTrue(n instanceof CompoundExpression);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for getOperator.
     */
    public void testGetOperator() {
        Subtract subtract = new Subtract();
        assertEquals(" - ", subtract.getOperator());
    }
    
    /**
     * Junit Test for overwritten subtract factory method.
     */
    public void testFactoryMethodSubtract() {
        Subtract subtract = new Subtract();
        subtract.addExpression(new LongLiteral(3));
        subtract.addExpression(new LongLiteral(2));
        
        Expression n = subtract;
        Expression exp = n.subtract(new LongLiteral(1));
        assertEquals("3 - 2 - 1", exp.toString());
    }    

    // --------------------------------------------------------------------------
}