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
 * Junit Test for testing multiply arithmetic expression of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestMultiply extends TestCase {
    // --------------------------------------------------------------------------
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Multiply();
        assertTrue(n instanceof CompoundExpression);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for getOperator.
     */
    public void testGetOperator() {
        Multiply multiply = new Multiply();
        assertEquals(" * ", multiply.getOperator());
    }
    
    /**
     * Junit Test for overwritten multiply factory method.
     */
    public void testFactoryMethodMultiply() {
        Multiply multiply = new Multiply();
        multiply.addExpression(new LongLiteral(3));
        multiply.addExpression(new LongLiteral(2));
        
        Expression n = multiply;
        Expression exp = n.multiply(new LongLiteral(1));
        assertEquals("3 * 2 * 1", exp.toString());
    }    

    // --------------------------------------------------------------------------
}
