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
import org.castor.cpa.query.object.literal.LongLiteral;

/**
 * Junit Test for testing LOCATE function class of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestLocate extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Instantiates a new junit Test for LOCATE function class of query objects.
     * 
     * @param name the name
     */
    public TestLocate(final String name) {
        super(name);
    }

    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Locate();
        assertTrue(n instanceof AbstractFunction);
        assertTrue(n instanceof Function);
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
        Locate n = new Locate();
        assertTrue(n.getIndex() instanceof LongLiteral);
        assertEquals(1, ((LongLiteral) n.getIndex()).getValue());
    }

    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        Locate n = new Locate();
        Expression string = new MockExpression();
        Expression value = new MockExpression();
        Expression index = new MockExpression();
        n.setString(string);
        n.setValue(value);
        n.setIndex(index);
        assertEquals(string, n.getString());
        assertEquals(value, n.getValue());
        assertEquals(index, n.getIndex());
    }
     
    /**
     * Junit Test for toString method.
     */
    public void testToString() {
        Locate n = new Locate();
        Expression string = new MockExpression();
        Expression value = new MockExpression();
        Expression index = new MockExpression();
        
        n.setString(string);
        n.setValue(value);
        n.setIndex(index);
        assertEquals("LOCATE(expression, expression, expression)", n.toString());

        n.setString(new MockExpression());
        n.setValue(null);
        n.setIndex(new MockExpression());
        assertEquals("LOCATE(expression, , expression)", n.toString());

        n.setString(new MockExpression());
        n.setValue(new MockExpression());
        n.setIndex(null);
        assertEquals("LOCATE(expression, expression, )", n.toString());

        n.setString(null);
        n.setValue(new MockExpression());
        n.setIndex(new MockExpression());
        assertEquals("LOCATE(, expression, expression)", n.toString());

        n.setString(null);
        n.setValue(null);
        n.setIndex(new MockExpression());
        assertEquals("LOCATE(, , expression)", n.toString());

        n.setString(new MockExpression());
        n.setValue(null);
        n.setIndex(null);
        assertEquals("LOCATE(expression, , )", n.toString());
        
        n.setString(null);
        n.setValue(new MockExpression());
        n.setIndex(null);
        assertEquals("LOCATE(, expression, )", n.toString());
        
        n.setString(null);
        n.setValue(null);
        n.setIndex(null);
        assertEquals("LOCATE(, , )", n.toString());
        
        // TODO add test with LongLiteral, DoubleLiteral and BigDecimalLiteral
        // that cause defaultStart flag to be true and false
    } 

    //--------------------------------------------------------------------------
}
