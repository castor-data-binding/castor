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
 * Junit Test for testing comparison condition of query objects.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestComparison extends TestCase {
    //--------------------------------------------------------------
    
    /**
     * Junit Test for constants.
     */
    public void testConstants() {
        assertTrue(Comparison.EQUAL instanceof Equal);
        assertTrue(Comparison.NOT_EQUAL instanceof NotEqual);
        assertTrue(Comparison.LESS_THAN instanceof LessThan);
        assertTrue(Comparison.LESS_EQUAL instanceof LessEqual);
        assertTrue(Comparison.GREATER_EQUAL instanceof GreaterEqual);
        assertTrue(Comparison.GREATER_THAN instanceof GreaterThan);
    }
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Comparison(Comparison.EQUAL);
        assertTrue(n instanceof AbstractCondition);
        assertTrue(n instanceof Condition);
    }
    
    /**
     * Junit Test for constructor.
     */
    public void testConstructor() {
       try {
           new Comparison((ComparisonOperator) null);
           fail("expected NullPointerException");
       } catch (NullPointerException ex) {
           assertTrue(true);
       }
       
       Equal equal = new Equal();
       Comparison n = new Comparison(equal);
       assertEquals(equal, n.getOperator());
    }
    
    /**
     * Junit Test for getters and setters.
     */
    public void testGetSet() {
        Expression leftExp = new MockExpression();
        Expression rightExp = new MockExpression();      

        Comparison n = new Comparison(Comparison.EQUAL);
        n.setLeftSide(leftExp);
        n.setRightSide(rightExp);
        assertEquals(leftExp, n.getLeftSide());
        assertEquals(rightExp, n.getRightSide());
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {
        Expression leftExp = new MockExpression();
        Expression rightExp = new MockExpression();      

        Comparison n = new Comparison(Comparison.EQUAL);
        n.setLeftSide(leftExp);
        n.setRightSide(rightExp);
        assertEquals("(expression = expression)", n.toString());
    }
    
    /**
     * Junit Test for not factory method.
     */
    public void testFactoryMethodNot() {
        Comparison n = new Comparison(Comparison.EQUAL);
        assertTrue(n.getOperator() instanceof Equal);
        
        n.not();
        assertTrue(n.getOperator() instanceof NotEqual);

        n.not();
        assertTrue(n.getOperator() instanceof Equal);
    }
    
    //--------------------------------------------------------------
}
