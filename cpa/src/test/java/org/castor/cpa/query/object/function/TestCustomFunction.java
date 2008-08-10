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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.object.expression.AbstractExpression;

/**
 * Junit Test for testing CustomFunction function class of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestCustomFunction extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Instantiates a new junit Test for CustomFunction function class of query objects.
     * 
     * @param name the name
     */
    public TestCustomFunction(final String name) {
        super(name);
    }

    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new CustomFunction("");
        assertTrue(n instanceof AbstractFunction);
        assertTrue(n instanceof Function);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
        
        try {
            n = new CustomFunction(null);
        } catch (NullPointerException ex) {
            assertTrue(n instanceof CustomFunction);
        }
     }

    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        CustomFunction n = new CustomFunction("");
        List < Expression >  parameters = new ArrayList < Expression > ();
        parameters.add(new MockExpression());
        parameters.add(new MockExpression());
        n.setParameters(parameters);
        assertEquals(parameters, n.getParameters());
     }
     
     /**
      * Junit Test for toString method.
      */
     public void testToString() {
         CustomFunction n = new CustomFunction("CUSTOM");
         
         n.setParameters(null);
         assertEquals("CUSTOM()", n.toString()); 
         
         List < Expression >  parameters = new ArrayList < Expression > ();
         parameters.add(new MockExpression());
         parameters.add(new MockExpression());
         n.setParameters(parameters);
         assertEquals("CUSTOM(expression, expression)", n.toString());
         
         parameters = new ArrayList < Expression > ();
         parameters.add(new MockExpression());
         parameters.add(null);
         n.setParameters(parameters);
         assertEquals("CUSTOM(expression, )", n.toString());
         
         parameters = new ArrayList < Expression > ();
         parameters.add(null);
         parameters.add(new MockExpression());
         n.setParameters(parameters);
         assertEquals("CUSTOM(, expression)", n.toString());
         
         parameters = new ArrayList < Expression > ();
         parameters.add(null);
         parameters.add(null);
         n.setParameters(parameters);
         assertEquals("CUSTOM(, )", n.toString()); 
     } 

     //--------------------------------------------------------------------------
}
