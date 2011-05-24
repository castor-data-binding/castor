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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;
import org.castor.cpa.query.Function;
import org.castor.cpa.query.QueryObject;
import org.castor.cpa.query.TemporalType;
import org.castor.cpa.query.TrimSpecification;
import org.castor.cpa.query.object.condition.MockParameter;
import org.castor.cpa.query.object.function.MockExpression;

import junit.framework.TestCase;

/**
 * Junit Test for testing abstract expression class.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestAbstractExpression extends TestCase {
    //--------------------------------------------------------------------------

    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new MockExpression();
        assertTrue(n instanceof AbstractExpression);
        assertTrue(n instanceof Expression);
    }

    /**
     * Junit Test for toString.
     */
    public void testToString() {
        MockExpression n = new MockExpression();
        assertEquals("expression", n.toString());
    }

    /**
     * Junit Test for factory method Add.
     */
    public void testFactoryMethodAdd() {
        AbstractExpression n = new MockExpression();
        Expression exp = n.add(new BigDecimal("55.55"));
        assertEquals("expression + 55.55", exp.toString());

        exp = n.add(60.45);
        assertEquals("expression + 60.45", exp.toString());

        exp = n.add(new MockExpression());
        assertEquals("expression + expression", exp.toString());

        exp = n.add(34);
        assertEquals("expression + 34", exp.toString());

    }

    /**
     * Junit Test for factory method Subtract.
     */
    public void testFactoryMethodSubtract() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.subtract(new BigDecimal("55.55"));
        assertEquals("expression - 55.55", exp.toString());

        exp = n.subtract(60.45);
        assertEquals("expression - 60.45", exp.toString());

        exp = n.subtract(new MockExpression());
        assertEquals("expression - expression", exp.toString());

        exp = n.subtract(34);
        assertEquals("expression - 34", exp.toString());
    }

    /**
     * Junit Test for factory method Concate.
     */
    public void testFactoryMethodConcate() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.concat("TEST");
        assertEquals("expression || 'TEST'", exp.toString());

        exp = n.concat(new MockExpression());
        assertEquals("expression || expression", exp.toString());
    }

    /**
     * Junit Test for factory method Multiply.
     */
    public void testFactoryMethodMultiply() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.multiply(new BigDecimal("55.55"));
        assertEquals("expression * 55.55", exp.toString());

        exp = n.multiply(60.45);
        assertEquals("expression * 60.45", exp.toString());

        exp = n.multiply(new MockExpression());
        assertEquals("expression * expression", exp.toString());

        exp = n.multiply(34);
        assertEquals("expression * 34", exp.toString());
    }

    /**
     * Junit Test for factory method Divide.
     */
    public void testFactoryMethodDivide() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.divide(new BigDecimal("55.55"));
        assertEquals("expression / 55.55", exp.toString());

        exp = n.divide(60.45);
        assertEquals("expression / 60.45", exp.toString());

        exp = n.divide(new MockExpression());
        assertEquals("expression / expression", exp.toString());

        exp = n.divide(34);
        assertEquals("expression / 34", exp.toString());
    }

    /**
     * Junit Test for factory method Remainder.
     */
    public void testFactoryMethodRemainder() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.remainder(new BigDecimal("55.55"));
        assertEquals("expression % 55.55", exp.toString());

        exp = n.remainder(60.45);
        assertEquals("expression % 60.45", exp.toString());

        exp = n.remainder(new MockExpression());
        assertEquals("expression % expression", exp.toString());

        exp = n.remainder(34);
        assertEquals("expression % 34", exp.toString());
    }

    /**
     * Junit Test for factory method Plus.
     */
    public void testFactoryMethodPlus() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.plus();
        assertEquals("expression", exp.toString());
    }

    /**
     * Junit Test for factory method Nagate.
     */
    public void testFactoryMethodNegate() {
        AbstractExpression n = new MockExpression();

        Expression exp = n.negate();
        assertEquals("-expression", exp.toString());
    }

    /**
     * Junit Test for factory method length.
     */
    public void testFactoryMethodLength() {
        AbstractExpression n = new MockExpression();
        Function function = n.length();
        assertEquals("LENGTH(expression)", function.toString());
    }

    /**
     * Junit Test for factory method abs.
     */
    public void testFactoryMethodAbs() {
        AbstractExpression n = new MockExpression();
        Function function = n.abs();
        assertEquals("ABS(expression)", function.toString());
    }

    /**
     * Junit Test for factory method sqrt(square root).
     */
    public void testFactoryMethodSqrt() {
        AbstractExpression n = new MockExpression();
        Function function = n.sqrt();
        assertEquals("SQRT(expression)", function.toString());
    }

    /**
     * Junit Test for factory method lower.
     */
    public void testFactoryMethodLower() {
        AbstractExpression n = new MockExpression();
        Function function = n.lower();
        assertEquals("LOWER(expression)", function.toString());
    }

    /**
     * Junit Test for factory method Upper.
     */
    public void testFactoryMethodUpper() {
        AbstractExpression n = new MockExpression();
        Function function = n.upper();
        assertEquals("UPPER(expression)", function.toString());
    }
    
    /**
     * Junit Test for factory method locate.
     */
    public void testFactoryMethodLocate() {
        AbstractExpression n = new MockExpression();

        Function function = n.locate(new MockExpression());
        assertEquals("LOCATE(expression, expression)", function.toString());

        function = n.locate("Test");
        assertEquals("LOCATE(expression, 'Test')", function.toString());

        function = n.locate(new MockExpression(), new MockExpression());
        assertEquals("LOCATE(expression, expression, expression)", function.toString());

        function = n.locate(new MockExpression(), 6);
        assertEquals("LOCATE(expression, expression, 6)", function.toString());

        function = n.locate("Test", new MockExpression());
        assertEquals("LOCATE(expression, 'Test', expression)", function.toString());

        function = n.locate("Test", 6);
        assertEquals("LOCATE(expression, 'Test', 6)", function.toString());
    }

    /**
     * Junit Test for factory method Substring.
     */
    public void testFactoryMethodSubstring() {
        AbstractExpression n = new MockExpression();

        Function function = n.substring(new MockExpression(), new MockExpression());
        assertEquals("SUBSTRING(expression, expression, expression)", function.toString());

        function = n.substring(new MockExpression(), 34);
        assertEquals("SUBSTRING(expression, expression, 34)", function.toString());

        function = n.substring(45, new MockExpression());
        assertEquals("SUBSTRING(expression, 45, expression)", function.toString());

        function = n.substring(45, 54);
        assertEquals("SUBSTRING(expression, 45, 54)", function.toString());
    }

    /**
     * Junit Test for factory method Trim.
     */
    public void testFactoryMethodTrim() {
        AbstractExpression n = new MockExpression();

        Function function = n.trim();
        assertEquals("TRIM(expression)", function.toString());

        function = n.trim('k');
        assertEquals("TRIM('k' FROM expression)", function.toString());

        function = n.trim(new MockParameter());
        assertEquals("TRIM(parameter FROM expression)", function.toString());

        function = n.trim(TrimSpecification.LEADING);
        assertEquals("TRIM(LEADING FROM expression)", function.toString());

        function = n.trim(TrimSpecification.LEADING, 'k');
        assertEquals("TRIM(LEADING 'k' FROM expression)", function.toString());

        function = n.trim(TrimSpecification.LEADING, new MockParameter());
        assertEquals("TRIM(LEADING parameter FROM expression)", function.toString());

    }

    /**
     * Junit Test for factory method of comparison Equal.
     */
    public void testFactoryMethodComparisonEqual() throws ParseException {
        AbstractExpression n = new MockExpression();
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
     */
    public void testFactoryMethodComparisonNotEqual() throws ParseException {
        AbstractExpression n = new MockExpression();
        Condition condition = n.notEqual(new BigDecimal("345.5"));
        assertEquals("(expression <> 345.5)", condition.toString());
        
        condition = n.notEqual(true);
        assertEquals("(expression <> true)", condition.toString());
        
        condition = n.notEqual(4.8);
        assertEquals("(expression <> 4.8)", condition.toString());
        
        condition = n.notEqual(new MockExpression());
        assertEquals("(expression <> expression)", condition.toString());
        
        condition = n.notEqual(45);
        assertEquals("(expression <> 45)", condition.toString());
        
        condition = n.notEqual("string");
        assertEquals("(expression <> 'string')", condition.toString());
        
        Date date = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        condition = n.notEqual(TemporalType.TIME, date);
        assertEquals("(expression <> TIME '12:34:56.789')", condition.toString());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        condition = n.notEqual(TemporalType.TIME, cal);
        assertEquals("(expression <> TIME '12:34:56.789')", condition.toString());
    }

    /**
     * Junit Test for factory method of LessThan.
     */
    public void testFactoryMethodComparisonLessThan() throws ParseException {
        AbstractExpression n = new MockExpression();
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
     */
    public void testFactoryMethodComparisonLessEqual() throws ParseException {
        AbstractExpression n = new MockExpression();
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
     */
    public void testFactoryMethodComparisonGreaterEqual() throws ParseException {
        AbstractExpression n = new MockExpression();
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
     */
    public void testFactoryMethodComparisonGreaterThan() throws ParseException {
        AbstractExpression n = new MockExpression();
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

    /**
     * Junit Test for factory method Like.
     */
    public void testFactoryMethodLike() {
        AbstractExpression exp = new MockExpression();
        Condition condition = exp.like(new MockParameter());
        assertEquals("(expression LIKE parameter)", condition.toString());
        condition = exp.notLike(new MockParameter());
        assertEquals("(expression NOT LIKE parameter)", condition.toString());
        
        condition = exp.like("pattern");
        assertEquals("(expression LIKE 'pattern')", condition.toString());
        condition = exp.notLike("pattern");
        assertEquals("(expression NOT LIKE 'pattern')", condition.toString());
        
        condition = exp.like(new MockParameter(), 'e');
        assertEquals("(expression LIKE parameter ESCAPE 'e')", condition.toString());
        condition = exp.notLike(new MockParameter(), 'e');
        assertEquals("(expression NOT LIKE parameter ESCAPE 'e')", condition.toString());
        
        condition = exp.like(new MockParameter(), new MockParameter());
        assertEquals("(expression LIKE parameter ESCAPE parameter)", condition.toString());
        condition = exp.notLike(new MockParameter(), new MockParameter());
        assertEquals("(expression NOT LIKE parameter ESCAPE parameter)", condition.toString());
        
        condition = exp.like("pattern", 'e');
        assertEquals("(expression LIKE 'pattern' ESCAPE 'e')", condition.toString());
        condition = exp.notLike("pattern", 'e');
        assertEquals("(expression NOT LIKE 'pattern' ESCAPE 'e')", condition.toString());
        
        condition = exp.like("pattern", new MockParameter());
        assertEquals("(expression LIKE 'pattern' ESCAPE parameter)", condition.toString());
        condition = exp.notLike("pattern", new MockParameter());
        assertEquals("(expression NOT LIKE 'pattern' ESCAPE parameter)", condition.toString());
    }

    /**
     * Junit Test for factory method Between.
     */
    public void testFactoryMethodBetween() throws ParseException {
        AbstractExpression exp = new MockExpression();
        Condition condition = exp.between(new BigDecimal("55.55"), new BigDecimal("54.23"));
        assertEquals("(expression BETWEEN 55.55 AND 54.23)", condition.toString());
        condition = exp.notBetween(new BigDecimal("55.55"), new BigDecimal("54.23"));
        assertEquals("(expression NOT BETWEEN 55.55 AND 54.23)", condition.toString());
    
        condition = exp.between(45.234, 324.234234);
        assertEquals("(expression BETWEEN 45.234 AND 324.234234)", condition.toString());
        condition = exp.notBetween(45.234, 324.234234);
        assertEquals("(expression NOT BETWEEN 45.234 AND 324.234234)", condition.toString());
    
        condition = exp.between(new MockExpression(), new MockExpression());
        assertEquals("(expression BETWEEN expression AND expression)", condition.toString());
        condition = exp.notBetween(new MockExpression(), new MockExpression());
        assertEquals("(expression NOT BETWEEN expression AND expression)", condition.toString());
    
        condition = exp.between(342, 2345);
        assertEquals("(expression BETWEEN 342 AND 2345)", condition.toString());
        condition = exp.notBetween(342, 2345);
        assertEquals("(expression NOT BETWEEN 342 AND 2345)", condition.toString());
    
        condition = exp.between("low", "high");
        assertEquals("(expression BETWEEN 'low' AND 'high')", condition.toString());
        condition = exp.notBetween("low", "high");
        assertEquals("(expression NOT BETWEEN 'low' AND 'high')", condition.toString());
    
        Date dateLow = new SimpleDateFormat("HH:mm:ss.SSS").parse("12:34:56.789");
        Date dateHigh = new SimpleDateFormat("HH:mm:ss.SSS").parse("23:45:34.456");
        condition = exp.between(TemporalType.TIME, dateLow, dateHigh);
        assertEquals("(expression BETWEEN TIME '12:34:56.789' AND TIME '23:45:34.456')",
                condition.toString());
        condition = exp.notBetween(TemporalType.TIME, dateLow, dateHigh);
        assertEquals("(expression NOT BETWEEN TIME '12:34:56.789' AND TIME '23:45:34.456')",
                condition.toString());
    
        dateLow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .parse("2007-08-08 12:34:56.789");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .parse("2008-08-08 12:34:56.789");
        condition = exp.between(TemporalType.TIMESTAMP, dateLow, dateHigh);
        assertEquals("(expression BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' "
                + "AND TIMESTAMP '2008-08-08 12:34:56.789')", condition.toString());
        condition = exp.notBetween(TemporalType.TIMESTAMP, dateLow, dateHigh);
        assertEquals("(expression NOT BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' "
                + "AND TIMESTAMP '2008-08-08 12:34:56.789')", condition.toString());
    
        dateLow = new SimpleDateFormat("yyyy-MM-dd").parse("2007-08-08");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08");
        condition = exp.between(TemporalType.DATE, dateLow, dateHigh);
        assertEquals("(expression BETWEEN DATE '2007-08-08' AND DATE '2008-08-08')",
                condition.toString());
        condition = exp.notBetween(TemporalType.DATE, dateLow, dateHigh);
        assertEquals("(expression NOT BETWEEN DATE '2007-08-08' AND DATE '2008-08-08')",
                condition.toString());
    
        dateLow = new SimpleDateFormat("HH:mm:ss.SSS").parse("13:45:34.456");
        dateHigh = new SimpleDateFormat("HH:mm:ss.SSS").parse("23:45:34.456");
        Calendar calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        Calendar calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.TIME, calLow, calHigh);
        assertEquals("(expression BETWEEN TIME '13:45:34.456' AND TIME '23:45:34.456')",
                condition.toString());
        condition = exp.notBetween(TemporalType.TIME, calLow, calHigh);
        assertEquals("(expression NOT BETWEEN TIME '13:45:34.456' AND TIME '23:45:34.456')",
                condition.toString());
    
        dateLow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .parse("2007-08-08 12:34:56.789");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .parse("2007-08-08 22:34:56.789");
        calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.TIMESTAMP, calLow, calHigh);
        assertEquals("(expression BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' "
                + "AND TIMESTAMP '2007-08-08 22:34:56.789')", condition.toString());
        condition = exp.notBetween(TemporalType.TIMESTAMP, calLow, calHigh);
        assertEquals("(expression NOT BETWEEN TIMESTAMP '2007-08-08 12:34:56.789' "
                + "AND TIMESTAMP '2007-08-08 22:34:56.789')", condition.toString());
    
        dateLow = new SimpleDateFormat("yyyy-MM-dd").parse("2007-08-08");
        dateHigh = new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08");
        calLow = Calendar.getInstance();
        calLow.setTime(dateLow);
        calHigh = Calendar.getInstance();
        calHigh.setTime(dateHigh);
        condition = exp.between(TemporalType.DATE, calLow, calHigh);
        assertEquals("(expression BETWEEN DATE '2007-08-08' AND DATE '2008-08-08')",
                condition.toString());
        condition = exp.notBetween(TemporalType.DATE, calLow, calHigh);
        assertEquals("(expression NOT BETWEEN DATE '2007-08-08' AND DATE '2008-08-08')",
                condition.toString());
    }

    //--------------------------------------------------------------------------
}
