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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.TemporalType;

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
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new Comparison(new Equal());
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

       Equal operator = new Equal();
       Expression leftExp = new MockExpression();
       Expression rightExp = new MockExpression();      
       Comparison n = new Comparison(operator);
       n.setLeftSide(leftExp);
       n.setRightSide(rightExp);
       assertEquals(operator, n.getOperator());
       assertEquals(leftExp, n.getLeftSide());
       assertEquals(rightExp, n.getRightSide());
    }
    
    /**
     * Junit Test for toString.
     */
    public void testToString() {

       Equal operator = new Equal();
       Expression leftExp = new MockExpression();
       Expression rightExp = new MockExpression();      
       Comparison n = new Comparison(operator);
       n.setLeftSide(leftExp);
       n.setRightSide(rightExp);
       assertEquals("(expression = expression)", n.toString());
    }
    
    /**
     * Junit Test for factory method of Equal.
     * @throws ParseException 
     */
    public void testFactoryMethodEqual() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.equal(new BigDecimal("345.5"));
        assertEquals("(expression = 345.5)", condition.toString());
        
        condition = n.equal(true);
        assertEquals("(expression = true)", condition.toString());
        
        condition = n.equal(4.8);
        assertEquals("(expression = 4.8)", condition.toString());
        
        condition = n.equal(new MockExpression());
        assertEquals("(expression = expression)", condition.toString());
        
        condition = n.equal(45);
        assertEquals("(expression = 45)", condition.toString());
        
        condition = n.equal("string");
        assertEquals("(expression = 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.equal(TemporalType.TIME, date);
        assertEquals("(expression = TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.equal(TemporalType.TIME, cal);
        assertEquals("(expression = TIME '12:34:56.789')", condition.toString());
    
    }
    
    /**
     * Junit Test for factory method of NotEqual.
     * @throws ParseException 
     */
    public void testFactoryMethodNotEqual() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.notEqual(new BigDecimal("345.5"));
        assertEquals("(expression != 345.5)", condition.toString());
        
        condition = n.notEqual(true);
        assertEquals("(expression != true)", condition.toString());
        
        condition = n.notEqual(4.8);
        assertEquals("(expression != 4.8)", condition.toString());
        
        condition = n.notEqual(new MockExpression());
        assertEquals("(expression != expression)", condition.toString());
        
        condition = n.notEqual(45);
        assertEquals("(expression != 45)", condition.toString());
        
        condition = n.notEqual("string");
        assertEquals("(expression != 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.notEqual(TemporalType.TIME, date);
        assertEquals("(expression != TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.notEqual(TemporalType.TIME, cal);
        assertEquals("(expression != TIME '12:34:56.789')", condition.toString());
    
    }
    
    /**
     * Junit Test for factory method of LessThan.
     * @throws ParseException 
     */
    public void testFactoryMethodLessThan() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.lessThan(new BigDecimal("345.5"));
        assertEquals("(expression < 345.5)", condition.toString());
           
        condition = n.lessThan(4.8);
        assertEquals("(expression < 4.8)", condition.toString());
        
        condition = n.lessThan(new MockExpression());
        assertEquals("(expression < expression)", condition.toString());
        
        condition = n.lessThan(45);
        assertEquals("(expression < 45)", condition.toString());
        
        condition = n.lessThan("string");
        assertEquals("(expression < 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.lessThan(TemporalType.TIME, date);
        assertEquals("(expression < TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.lessThan(TemporalType.TIME, cal);
        assertEquals("(expression < TIME '12:34:56.789')", condition.toString());
    
    }
    
    /**
     * Junit Test for factory method of LessEqual.
     * @throws ParseException 
     */
    public void testFactoryMethodLessEqual() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.lessEqual(new BigDecimal("345.5"));
        assertEquals("(expression <= 345.5)", condition.toString());
           
        condition = n.lessEqual(4.8);
        assertEquals("(expression <= 4.8)", condition.toString());
        
        condition = n.lessEqual(new MockExpression());
        assertEquals("(expression <= expression)", condition.toString());
        
        condition = n.lessEqual(45);
        assertEquals("(expression <= 45)", condition.toString());
        
        condition = n.lessEqual("string");
        assertEquals("(expression <= 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.lessEqual(TemporalType.TIME, date);
        assertEquals("(expression <= TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.lessEqual(TemporalType.TIME, cal);
        assertEquals("(expression <= TIME '12:34:56.789')", condition.toString());
    
    }
    
    /**
     * Junit Test for factory method of GreaterEqual.
     * @throws ParseException 
     */
    public void testFactoryMethodGreaterEqual() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.greaterEqual(new BigDecimal("345.5"));
        assertEquals("(expression >= 345.5)", condition.toString());
           
        condition = n.greaterEqual(4.8);
        assertEquals("(expression >= 4.8)", condition.toString());
        
        condition = n.greaterEqual(new MockExpression());
        assertEquals("(expression >= expression)", condition.toString());
        
        condition = n.greaterEqual(45);
        assertEquals("(expression >= 45)", condition.toString());
        
        condition = n.greaterEqual("string");
        assertEquals("(expression >= 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.greaterEqual(TemporalType.TIME, date);
        assertEquals("(expression >= TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.greaterEqual(TemporalType.TIME, cal);
        assertEquals("(expression >= TIME '12:34:56.789')", condition.toString());
    
    }
    
    /**
     * Junit Test for factory method of GreaterThan.
     * @throws ParseException 
     */
    public void testFactoryMethodGreaterThan() throws ParseException {
        Expression n = new MockExpression();
        Condition condition = n.greaterThan(new BigDecimal("345.5"));
        assertEquals("(expression > 345.5)", condition.toString());
           
        condition = n.greaterThan(4.8);
        assertEquals("(expression > 4.8)", condition.toString());
        
        condition = n.greaterThan(new MockExpression());
        assertEquals("(expression > expression)", condition.toString());
        
        condition = n.greaterThan(45);
        assertEquals("(expression > 45)", condition.toString());
        
        condition = n.greaterThan("string");
        assertEquals("(expression > 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.greaterThan(TemporalType.TIME, date);
        assertEquals("(expression > TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.greaterThan(TemporalType.TIME, cal);
        assertEquals("(expression > TIME '12:34:56.789')", condition.toString());
    
    }
    //--------------------------------------------------------------
}
