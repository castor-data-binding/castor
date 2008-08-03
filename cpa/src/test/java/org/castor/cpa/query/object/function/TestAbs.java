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
package org.castor.cpa.query.object.function;

import junit.framework.TestCase;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

/**
 * Junit Test for testing ABS (absolute) function class of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestAbs extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Instantiates a new junit Test for ABS function class of query objects.
     * 
     * @param name the name
     */
    public TestAbs(final String name) {
        super(name);
    }

    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public static void testInstance() {
        QueryObject n = new Abs();
        assertTrue(n instanceof AbstractFunction);
        assertTrue(n instanceof Function);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
     }

    /**
     * Junit Test for Getter and Setter methods.
     */
    public static void testGSetter() {
        Abs n = new Abs();
        Expression number = new MockExpression();
        n.setNumber(number);
        assertEquals(number, n.getNumber()); 
     }
     
     /**
      * Junit Test for toString method.
      */
     public static void testToString() {
         Abs n = new Abs();
         
         n.setNumber(null);
         assertEquals("ABS()", n.toString()); 
         
         Expression number = new MockExpression();
         n.setNumber(number);
         assertEquals("ABS(expression)", n.toString()); 
     } 

     //--------------------------------------------------------------------------
}
